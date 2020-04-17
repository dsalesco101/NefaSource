package ethos.sql;

import java.sql.Connection;
import java.sql.SQLException;

public interface SqlQuery<T> {

    T execute(DatabaseManager context, Connection connection) throws SQLException;

}
