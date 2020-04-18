package ethos.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseManagerTest {

    private static final String TABLE_NAME = "TEST_TABLE";
    private static String[] INSERTS = {

    };

    private DatabaseTable getTestTable() {
        return new DatabaseTable() {
            @Override
            public String getName() {
                return TABLE_NAME;
            }

            @Override
            public void createTable(Connection connection) throws SQLException {
                connection.createStatement().execute(
                        "CREATE TABLE " + getName() + "("
                        + "name VARCHAR (255),"
                        + "age INT NOT NULL"
                        + ")"
                );
            }
        };
    }

    @Test
    public void test_database() throws ExecutionException, InterruptedException {
        DatabaseManager manager = new DatabaseManager();

        DatabaseTable testTable = getTestTable();
        manager.execute(DatabaseConfiguration.TEST_EMBEDDED, (context, connection) -> {
            if (!manager.isTablePresent(testTable, connection)) {
                testTable.createTable(connection);
            }

            Statement statement = connection.createStatement();
            for (String insert : INSERTS) {
                statement.execute(insert);
            }

            ResultSet rs = statement.executeQuery("SELECT * FROM " + testTable.getName()
                    + " WHERE name='michael'");

            while (rs.next()) {
                assertEquals("michael", rs.getString("name"));
                System.out.println(rs.getString("name"));
            }

            return null;
        }).get();
    }

}