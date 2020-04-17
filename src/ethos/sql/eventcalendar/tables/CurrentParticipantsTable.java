package ethos.sql.eventcalendar.tables;

import java.sql.Connection;
import java.sql.SQLException;

import ethos.sql.DatabaseTable;

public class CurrentParticipantsTable implements DatabaseTable {

    public static final String USERNAME = "username";
    public static final String IP_ADDRESS = "ip";
    public static final String MAC_ADDRESS = "mac";
    public static final String ENTRY_DAY = "day";

    @Override
    public String getName() {
        return "current_participants";
    }

    @Override
    public void createTable(Connection connection) throws SQLException {
        connection.createStatement().execute("CREATE TABLE " + getName()
                + "(" + USERNAME + " VARCHAR (255),"
                + IP_ADDRESS + " VARCHAR (255),"
                + MAC_ADDRESS + " VARCHAR (255),"
                + ENTRY_DAY + " INT NOT NULL"
                + ")"
        );
    }
}
