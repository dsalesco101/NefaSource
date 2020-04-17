package ethos.util.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import ethos.Config;
import ethos.Server;
import ethos.ServerState;
import ethos.model.players.Player;
import ethos.util.DelayedStringSupplier;
import lombok.extern.java.Log;


/**
 * Logs player related data
 * 
 * @author Mobster
 *
 */
@Log
public class PlayerLogging {

	/**
	 * Different types of logs
	 * 
	 * @author Mobster
	 *
	 */
	public enum LogType {
		COMMAND,
		PUBLIC_CHAT,
		PRIVATE_CHAT,
		CHANGE_IP_ADDRESS,
		CHANGE_MAC_ADDRESS,
		DC_LOG,

		TRADE,
		STAKE,
		DROP,
		PICKUP,
		SHOP,
		VOTE,
		DEATH,
		DONATION,
		DICE

		;

		public String getFileName() {
			return toString().toLowerCase() + ".txt";
		}
	}

	/**
	 * The log directory which we can use to log data
	 */
	public static final File LOG_DIRECTORY = new File("./data/logs/");

	/**
	 * The date format.
	 */
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

	/**
	 * A thread pool to handle logging queries, no need to keep synchronization
	 * of anything as we're simply logging data, not modifying
	 */
	private static final Executor service = Executors.newCachedThreadPool();

	/**
	 * Writes a log.
	 * @param type log type
	 * @param player the player
	 * @param message the message
	 */
	public static void write(LogType type, Player player, String message) {
		if (Server.getConfiguration().getServerState() == ServerState.PUBLIC_PRIMARY) {
			Path path = Paths.get(LOG_DIRECTORY.getPath(),
					player.playerName.charAt(0) + "",
					player.playerName);

			write(type, message, path.resolve(type.getFileName()));
			write(type, message, path.resolve(String.format("%s.txt", player.playerName)));
		}
	}

	private static void write(LogType type, String message, Path...paths) {
		service.execute(() -> {
			for (Path path : paths) {
				if (!Files.exists(path)) {
					try {
						Files.createDirectories(path.getParent());
						Files.createFile(path);
					} catch (IOException e) {
						// Ignore exception
					}
				}

				try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
					writer.write(String.format("[%s] [%s] %s", type.toString().toLowerCase(), DATE_FORMAT.format(new Date()), message));
					writer.newLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}


}
