package ethos;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import ethos.model.content.eventcalendar.EventCalendar;
import ethos.model.content.eventcalendar.EventCalendarWinnerSelection;
import ethos.model.AttributesSerializable;
import ethos.model.content.polls.PollTab;
import ethos.sql.DatabaseManager;
import ethos.util.TeeOutputStream;
import lombok.extern.java.Log;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.util.HashedWheelTimer;

import ethos.clip.ObjectDef;
import ethos.clip.Region;
import ethos.clip.doors.DoorDefinition;
import ethos.event.CycleEventHandler;
import ethos.world.event.CyclicEventManager;
import ethos.event.EventHandler;
import ethos.event.impl.BonusApplianceEvent;
import ethos.event.impl.DidYouKnowEvent;
import ethos.event.impl.HPRegen;
import ethos.event.impl.UpdateQuestTab;
import ethos.event.impl.WheatPortalEvent;
import ethos.model.content.collection_log.CollectionLog;
import ethos.model.content.godwars.GodwarsEquipment;
import ethos.model.content.godwars.GodwarsNPCs;
import ethos.model.content.preset.PresetManager;
import ethos.model.content.tournaments.TourneyManager;
import ethos.model.content.tradingpost.Listing;
import ethos.model.content.trails.CasketRewards;
import ethos.model.content.wogw.Wogw;
import ethos.model.items.ItemDefinition;
import ethos.model.lobby.LobbyManager;
import ethos.model.minigames.FightPits;
import ethos.model.minigames.pk_arena.Highpkarena;
import ethos.model.minigames.pk_arena.Lowpkarena;
import ethos.model.multiplayer_session.MultiplayerSessionListener;
import ethos.model.npcs.NPCHandler;
import ethos.model.npcs.drops.DropManager;
import ethos.model.players.PlayerHandler;
import ethos.model.players.PlayerSave;
import ethos.model.players.combat.monsterhunt.MonsterHunt;
import ethos.model.players.packets.Commands;
import ethos.net.PipelineFactory;
import ethos.punishments.PunishmentCycleEvent;
import ethos.punishments.Punishments;
import ethos.util.date.GameCalendar;
import ethos.util.log.Logger;
import ethos.world.ClanManager;
import ethos.world.ItemHandler;
import ethos.world.ShopHandler;
import ethos.world.objects.GlobalObjects;
import lombok.Getter;

/**
 * The main class needed to start the server.
 * 
 * @author Sanity
 * @author Graham
 * @author Blake
 * @author Ryan Lmctruck30 Revised by Shawn Notes by Shawn
 */
@Getter
@Log
public class Server {
	
	public static Server getWorld() {
		return singleton;
	}
	private static Server singleton = new Server();
	private static AtomicLong tickCount = new AtomicLong();
	
	private Server() {
	}

	private static final Punishments PUNISHMENTS = new Punishments();

	private static final DropManager dropManager = new DropManager();

	private static ServerAttributes serverAttributes = new ServerAttributes(Config.ATTRIBUTES_FILE);

	/**
	 * A class that will manage game events
	 */
	private static final EventHandler events = new EventHandler();

	/**
	 * Represents our calendar with a given delay using the TimeUnit class
	 */
	private static GameCalendar calendar = new ethos.util.date.GameCalendar(
			new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"), "GMT-3:00");



	private static MultiplayerSessionListener multiplayerSessionListener = new MultiplayerSessionListener();

	private static GlobalObjects globalObjects = new GlobalObjects();
	
	private CyclicEventManager cyclicEventManager = new CyclicEventManager();
	
	/**
	 * ClanChat Added by Valiant
	 */
	public static ClanManager clanManager = new ClanManager();

	/**
	 * Sleep mode of the server.
	 */
	public static boolean sleeping;
	/**
	 * The test thingy
	 */
	public static boolean canGiveReward;

	public static long lastReward = 0;

	/**
	 * Server updating.
	 */
	public static boolean UpdateServer = false;

	/**
	 * Calls in which the server was last saved.
	 */
	public static long lastMassSave = System.currentTimeMillis();

	/**
	 * Forced shutdowns.
	 */
	public static boolean shutdownClientHandler;

	public static boolean canLoadObjects = false;

	/**
	 * Calls the usage of player items.
	 */
	public static ItemHandler itemHandler = new ItemHandler();

	/**
	 * Handles logged in players.
	 */
	public static PlayerHandler playerHandler = new PlayerHandler();

	/**
	 * Handles global NPCs.
	 */
	public static NPCHandler npcHandler = new NPCHandler();
	private EventHandler eventHandler = new EventHandler();
	/**
	 * Handles global shops.
	 */
	public static ShopHandler shopHandler = new ShopHandler();

	/**
	 * Handles the fightpits minigame.
	 */
	public static FightPits fightPits = new FightPits();

	/**
	 * The server configuration.
	 */
	private static ServerConfiguration configuration;

	/**
	 * The database manager.
	 */
	private static final DatabaseManager databaseManager = new DatabaseManager();
	
	/**
	 * Handles the main game processing.
	 */
	private static final ScheduledExecutorService GAME_THREAD = Executors.newSingleThreadScheduledExecutor();

	private static final ScheduledExecutorService IO_THREAD = Executors.newSingleThreadScheduledExecutor();
	
	private static final Runnable SERVER_TASKS = () -> {
		try {
			EventCalendarWinnerSelection.getSingleton().tick();
			itemHandler.process();
			playerHandler.process();
			npcHandler.process();
			shopHandler.process();
			Highpkarena.process();
			Lowpkarena.process();
			globalObjects.pulse();
			CycleEventHandler.getSingleton().process();
			events.process();
			tickCount.set(tickCount.get() + 1);
		} catch (Throwable t) {
			t.printStackTrace();
			t.getCause();
			t.getMessage();
			t.fillInStackTrace();
			System.out.println("Server tasks - Check for error");
			PlayerHandler.stream().filter(Objects::nonNull).forEach(PlayerSave::save);
		}
	};

	public void init() throws Exception {
		npcHandler.init();
	}

	private static void enableExceptionLogging() throws FileNotFoundException {
		TeeOutputStream outputStream = new TeeOutputStream(System.err, new FileOutputStream(Config.ERROR_LOG_FILE, true));
		System.setErr(new PrintStream(outputStream));
	}
	
	public static void main(java.lang.String args[]) {
		try {
			enableExceptionLogging();
			long startTime = System.currentTimeMillis();
			System.setOut(extracted());

			loadConfiguration();
			loadAttributes();
			EventCalendar.verifyCalendar();
			PUNISHMENTS.initialize();
			CharacterBackup.sequence();
			events.submit(new DidYouKnowEvent());
			events.submit(new WheatPortalEvent());
			events.submit(new BonusApplianceEvent());
			events.submit(new PunishmentCycleEvent(PUNISHMENTS, 50));
			events.submit(new UpdateQuestTab());
			events.submit(new HPRegen());
			Listing.init();
			Wogw.init();
			PollTab.init();
			ItemDefinition.load();
			DoorDefinition.load();
			GodwarsEquipment.load();
			GodwarsNPCs.load();
			LobbyManager.initializeLobbies();
			TourneyManager.initialiseSingleton();
			TourneyManager.getSingleton().init();
			dropManager.read();
			CasketRewards.read();
			
			PresetManager.getSingleton().init();
			ObjectDef.loadConfig();
			CollectionLog.init();
			Region.load();
			globalObjects.loadGlobalObjectFile();
			bindPorts();
			MonsterHunt.spawnNPC();
			Runtime.getRuntime().addShutdownHook(new ShutdownHook());
			Commands.initializeCommands();
			long endTime = System.currentTimeMillis();
			long elapsed = endTime - startTime;
			System.out.println(Config.SERVER_NAME + " has successfully started up in " + elapsed + " milliseconds.");
			GAME_THREAD.scheduleAtFixedRate(SERVER_TASKS, 0, 600, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void loadAttributes() throws IOException {
		serverAttributes = AttributesSerializable.getFromFile(Config.ATTRIBUTES_FILE, serverAttributes);
	}

	private static void loadConfiguration() throws IOException {
		File configurationFile = new File(ServerConfiguration.CONFIGURATION_FILE);
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER));
		if (!configurationFile.exists()) {
			configuration = ServerConfiguration.getDefault();
			mapper.writeValue(configurationFile, configuration);
			log.info("No configuration present, wrote default configuration file to " + configurationFile.getAbsolutePath());
		} else {
			configuration = mapper.readValue(configurationFile, ServerConfiguration.class);
			log.info("Loaded configuration.");
			log.info("Server state: " + configuration.getServerState());
		}
	}

	private static Logger extracted() {
		return new Logger(System.out);
	}

	public static MultiplayerSessionListener getMultiplayerSessionListener() {
		return multiplayerSessionListener;
	}

	/**
	 * Java connection. Ports.
	 */
	private static void bindPorts() {
		ServerBootstrap serverBootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
		serverBootstrap.setPipelineFactory(new PipelineFactory(new HashedWheelTimer()));
		serverBootstrap.bind(new InetSocketAddress(configuration.getServerState().getPort()));
	}

	public static GameCalendar getCalendar() {
		return calendar;
	}

	public static GlobalObjects getGlobalObjects() {
		return globalObjects;
	}

	public static EventHandler getEventHandler() {
		return events;
	}

	public static DropManager getDropManager() {
		return dropManager;
	}
	public void reloadNPCHandler() {
		npcHandler = new NPCHandler();
	}
	public static Punishments getPunishments() {
		return PUNISHMENTS;
	}

	public static String getStatus() {
		return "IO_THREAD\n" + "\tShutdown? " + IO_THREAD.isShutdown() + "\n" + "\tTerminated? "
				+ IO_THREAD.isTerminated();
	}

	public static ServerConfiguration getConfiguration() {
		return configuration;
	}

	public static DatabaseManager getDatabaseManager() {
		return databaseManager;
	}

	public static ServerAttributes getServerAttributes() {
		return serverAttributes;
	}

	public static long getTickCount() {
		return tickCount.get();
	}
}