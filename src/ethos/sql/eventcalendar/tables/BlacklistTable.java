package ethos.sql.eventcalendar.tables;

import java.sql.Connection;
import java.sql.SQLException;

import ethos.sql.DatabaseTable;

public class BlacklistTable implements DatabaseTable {

    public static final String IP_ADDRESS = "ip";
    public static final String MAC_ADDRESS = "mac";

    @Override
    public String getName() {
        return "blacklists";
    }

    @Override
    public void createTable(Connection connection) throws SQLException {
        connection.createStatement().execute("CREATE TABLE " + getName()
                + "(" + IP_ADDRESS + " VARCHAR (255),"
                + MAC_ADDRESS + " VARCHAR (255)" + ")");
    }
}
