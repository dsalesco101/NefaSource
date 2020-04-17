package ethos.sql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import lombok.extern.java.Log;

/**
 * Manages our databases and dispatching sql statements.
 * @author Michael Sasse (https://github.com/mikeysasse/)
 */
@Log
public class DatabaseManager {

    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final Map<DatabaseConfiguration, ConnectionProvider> dataSources = new ConcurrentHashMap<>();

    public DatabaseManager() {
        try {
            DriverManager.registerDriver(new org.apache.derby.jdbc.EmbeddedDriver());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() throws InterruptedException {
        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.MINUTES);
        log.info("Database manager shutdown.");
    }

    public <T> Future<T> execute(DatabaseConfiguration configuration, SqlQuery<T> query) {
        return executorService.submit(() -> {
            try (Connection connection = getConnectionProvider(configuration).getConnection()) {
                return query.execute(this, connection);
            } catch (Exception e) {
                log.severe("An error occured while trying to execute a query, database: "
                        + configuration + " query " + query);
                e.printStackTrace();
                return null;
            }
        });
    }

    public boolean isTablePresent(DatabaseTable table, Connection connection) throws SQLException {
        DatabaseMetaData meta = connection.getMetaData();
        ResultSet res = meta.getTables(null, "APP", table.getName().toUpperCase(), null);
        return res.next();
    }

    public void createTableIfNotPresent(DatabaseTable table, Connection connection) throws SQLException {
        if (!isTablePresent(table, connection)) {
            table.createTable(connection);
            log.info("Created table: " + table.getName());
        }
    }

    private ConnectionProvider getConnectionProvider(DatabaseConfiguration configuration) {
        if (dataSources.containsKey(configuration)) {
            return dataSources.get(configuration);
        } else {
            HikariConnectionProvider provider = new HikariConnectionProvider(configuration);
            dataSources.put(configuration, provider);
            return provider;
        }
    }

}
