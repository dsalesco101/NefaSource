package ethos.sql;

import ethos.Config;

public enum DatabaseConfiguration {

    TEST_EMBEDDED("test_database", true,"", ""),
    EVENT_CALENDAR("event_calendar", true, "", ""),
    ;

    private final String name;
    private final boolean embedded;
    private final String username;
    private final String password;

    DatabaseConfiguration(String name, boolean embedded, String username, String password) {
        this.name = name;
        this.embedded = embedded;
        this.username = username;
        this.password = password;
    }

    public String getUrl() {
        if (embedded) {
            return "jdbc:derby:" + Config.USER_FOLDER + "/" + name + ";create=true";
        } else {
            throw new UnsupportedOperationException("Non-embedded databases have not been tested so they have been disabled.");
        }
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
