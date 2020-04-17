package ethos;

/**
 * Contains the configuration stored in YAML format.
 * @author Michael Sasse (https://github.com/mikeysasse/)
 */
public class ServerConfiguration {

    public static final String CONFIGURATION_FILE = "config.yaml";

    public static ServerConfiguration getDefault() {
        ServerConfiguration configuration = new ServerConfiguration();
        configuration.serverState = ServerState.PUBLIC_PRIMARY;
        return configuration;
    }

    private ServerState serverState;

    public ServerConfiguration() {
    }

    public ServerState getServerState() {
        return serverState;
    }
}
