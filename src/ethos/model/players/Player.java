package ethos.model.players;

import ethos.Config;
import ethos.Highscores.Highscores;
import ethos.Server;
import ethos.database.impl.Hiscores;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.event.Event;
import ethos.event.impl.IronmanRevertEvent;
import ethos.event.impl.MinigamePlayersEvent;
import ethos.event.impl.RunEnergyEvent;
import ethos.event.impl.SkillRestorationEvent;
import ethos.model.Animation;
import ethos.model.content.*;
import ethos.model.content.Tutorial.Stage;
import ethos.model.content.achievement.AchievementHandler;
import ethos.model.content.achievement.Achievements;
import ethos.model.content.achievement_diary.AchievementDiary;
import ethos.model.content.achievement_diary.AchievementDiaryManager;
import ethos.model.content.achievement_diary.RechargeItems;
import ethos.model.content.barrows.Barrows;
import ethos.model.content.collection_log.CollectionLog;
import ethos.model.content.dailytasks.DailyTasks;
import ethos.model.content.dailytasks.DailyTasks.PossibleTasks;
import ethos.model.content.dailytasks.TaskTypes;
import ethos.model.content.eventcalendar.EventCalendar;
import ethos.model.content.eventcalendar.EventChallenge;
import ethos.model.content.explock.ExpLock;
import ethos.model.content.godwars.God;
import ethos.model.content.godwars.Godwars;
import ethos.model.content.godwars.GodwarsEquipment;
import ethos.model.content.instances.InstancedAreaManager;
import ethos.model.content.kill_streaks.Killstreak;
import ethos.model.content.loot.impl.HourlyRewardBox;
import ethos.model.content.loot.impl.NormalMysteryBox;
import ethos.model.content.loot.impl.PvmCasket;
import ethos.model.content.loot.impl.SuperMysteryBox;
import ethos.model.content.loot.impl.UltraMysteryBox;
import ethos.model.content.lootbag.LootingBag;
import ethos.model.content.polls.PollTab;
import ethos.model.content.preset.Preset;
import ethos.model.content.prestige.PrestigeSkills;
import ethos.model.content.safebox.SafeBox;
import ethos.model.content.staff.PunishmentPanel;
import ethos.model.content.teleportation.TeleportHandler;
import ethos.model.content.teleportation.TeleportationInterface.TeleportData;
import ethos.model.content.teleportation.TeleportationInterface.TeleportType;
import ethos.model.content.titles.Titles;
import ethos.model.content.tournaments.TourneyManager;
import ethos.model.content.trails.RewardLevel;
import ethos.model.content.trails.TreasureTrails;
import ethos.model.entity.Entity;
import ethos.model.entity.HealthStatus;
import ethos.model.items.*;
import ethos.model.items.bank.Bank;
import ethos.model.items.bank.BankPin;
import ethos.model.lobby.LobbyManager;
import ethos.model.lobby.LobbyType;
import ethos.model.minigames.bounty_hunter.BountyHunter;
import ethos.model.minigames.fight_cave.FightCave;
import ethos.model.minigames.inferno.Inferno;
import ethos.model.minigames.lighthouse.DagannothMother;
import ethos.model.minigames.pest_control.PestControl;
import ethos.model.minigames.pest_control.PestControlRewards;
import ethos.model.minigames.pk_arena.Highpkarena;
import ethos.model.minigames.pk_arena.Lowpkarena;
import ethos.model.minigames.raids.RaidConstants;
import ethos.model.minigames.raids.Raids;
import ethos.model.minigames.rfd.DisposeTypes;
import ethos.model.minigames.rfd.RecipeForDisaster;
import ethos.model.minigames.warriors_guild.WarriorsGuild;
import ethos.model.minigames.xeric.Xeric;
import ethos.model.minigames.xeric.XericLobby;
import ethos.model.miniquests.MageArena;
import ethos.model.multiplayer_session.MultiplayerSessionStage;
import ethos.model.multiplayer_session.MultiplayerSessionType;
import ethos.model.multiplayer_session.duel.Duel;
import ethos.model.multiplayer_session.duel.DuelSession;
import ethos.model.multiplayer_session.trade.Trade;
import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCDeathTracker;
import ethos.model.npcs.NPCHandler;
import ethos.model.npcs.bosses.DemonicGorilla;
import ethos.model.npcs.bosses.KalphiteQueen;
import ethos.model.npcs.bosses.Kraken;
import ethos.model.npcs.bosses.cerberus.Cerberus;
import ethos.model.npcs.bosses.cerberus.CerberusLostItems;
import ethos.model.npcs.bosses.skotizo.Skotizo;
import ethos.model.npcs.bosses.skotizo.SkotizoLostItems;
import ethos.model.npcs.bosses.vorkath.Vorkath;
import ethos.model.npcs.bosses.zulrah.Zulrah;
import ethos.model.npcs.bosses.zulrah.ZulrahLostItems;
import ethos.model.npcs.instance.InstanceSoloFight;
import ethos.model.npcs.pets.PetHandler;
import ethos.model.npcs.pets.PetHandler.Pets;
import ethos.model.players.combat.AttackPlayer;
import ethos.model.players.combat.CombatAssistant;
import ethos.model.players.combat.DamageQueueEvent;
import ethos.model.players.combat.Degrade;
import ethos.model.players.combat.Hitmark;
import ethos.model.players.combat.magic.MagicData;
import ethos.model.players.combat.melee.QuickPrayers;
import ethos.model.players.mode.Mode;
import ethos.model.players.mode.ModeType;
import ethos.model.players.mode.RegularMode;
import ethos.model.players.skills.*;
import ethos.model.players.skills.agility.AgilityHandler;
import ethos.model.players.skills.agility.impl.*;
import ethos.model.players.skills.agility.impl.rooftop.*;
import ethos.model.players.skills.crafting.Crafting;
import ethos.model.players.skills.farming.Farming;
import ethos.model.players.skills.fletching.Fletching;
import ethos.model.players.skills.herblore.Herblore;
import ethos.model.players.skills.hunter.Hunter;
import ethos.model.players.skills.mining.Mining;
import ethos.model.players.skills.prayer.Prayer;
import ethos.model.players.skills.runecrafting.Runecrafting;
import ethos.model.players.skills.slayer.Slayer;
import ethos.model.players.skills.thieving.Thieving;
import ethos.model.shops.ShopAssistant;
import ethos.net.Packet;
import ethos.net.Packet.Type;
import ethos.net.outgoing.UnnecessaryPacketDropper;
import ethos.util.Misc;
import ethos.util.SimpleTimer;
import ethos.util.Stopwatch;
import ethos.util.Stream;
import ethos.world.Clan;
import lombok.Getter;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

public class Player extends Entity {

	public static int maRound = 0;
	public boolean maOption = false, maIndeedyOption = false;

	@Getter
	private SkillExperience skills = new SkillExperience(this);
	public static int FIRE_OF_EXCHANGE = 0;

	public int lastTeleportX, lastTeleportY, lastTeleportZ;
	
	public boolean inTradingPost = false;
	
	public boolean isInTradingPost() {
		return inTradingPost;
	}

	public void setInTradingPost(boolean inTradingPost) {
		this.inTradingPost = inTradingPost;
	}
	public boolean inBank = false;

	public boolean isInBank() {
		return inBank;
	}

	public void setInBank(boolean inBank) {
		this.inBank = inBank;
	}
	public boolean inPresets = false;

	public boolean isInPresets() {
		return inPresets;
	}

	public void setInPrests(boolean inPresets) {
		this.inPresets = inPresets;
	}
	public boolean inDonatorBox = false;

	public boolean isInDonatorBox() {
		return inDonatorBox;
	}

	public void setInDonatorBox(boolean inDonatorBox) {
		this.inDonatorBox = inDonatorBox;
	}
	public boolean inLamp = false;

	public boolean isInLamp() {
		return inLamp;
	}

	public void setInLamp(boolean inLamp) {
		this.inLamp = inLamp;
	}
	public MageArena mageArena = new MageArena(this);

	public MageArena getMageArena() {
		return this.mageArena;
	}
	public int raidCount;

	private CycleEventContainer currentAction;

	public int pollOption;
	/**
	 * @author Grant_ | www.rune-server.ee/members/grant_ | 10/6/19
	 * Tourney
	 */
	public int tourneyX;
	public int tourneyY;
	public boolean canAttack = true;
	public ArrayList<Integer> tourneyItemsReceived = new ArrayList<>();
	
	/**
	 * @author Grant_ | www.rune-server.ee/members/grant_ | 9/30/19
	 * Presets
	 */
	public boolean presetViewingDefault;
	public int presetViewingIndex;
	public Preset currentPreset;
	public boolean viewingInitialPreset;
	public boolean viewingPresets;
	public long lastPresetClick;
	
	/**
	 * @author Grant_ | www.rune-server.ee/members/grant_ | 10/3/19
	 * Tournaments
	 */
	public int[] combatLevelBackUp = new int[7];
	public int magicBookBackup;
	
	/**
	 * @author Grant_ | www.rune-server.ee/members/grant_ | 10/3/19
	 * Bank tab saving position
	 */
	public int previousTab;
	
	/**
	 * @author Grant_ | www.rune-server.ee/members/grant_ | 10/7/19
	 * Collection log variables
	 */
	public CollectionLog collectionLog = new CollectionLog(this);
	public List<GameItem> dropItems;
	public CollectionLog.CollectionTabType collectionLogTab;
	public int previousSelectedCell = 0;
	public int previousSelectedTab = 0;
	
	//Trading post timer
	public long lastTradingPostView;

	/**
	 * Overload variables
	 */

	public int homeTeleport = 50;

	public int boxCurrentlyUsing = 0;

	public int overloadTimer;
	public boolean overloadBoosted;
	
	public int xericWaveType = 0;
	public int xericDamage = 0;
	

	/**
	 * Variables for trading post
	 */
	public boolean barbarian = false;
	public boolean debugMessage = false;
	public boolean breakVials = false;
	public boolean hasPartner;
	public boolean collectCoins = false;
	public boolean rubyspecial = false;
	/**
	 * New Daily Task Variables
	 */
	public PossibleTasks currentTask;
	public TaskTypes playerChoice;
	public boolean dailyEnabled = false, completedDailyTask;
	public int dailyTaskDate, totalDailyDone = 0;

	public int item, uneditItem, quantity, price, pageId = 1, searchId;
	public String lookup;
	public List<Integer> saleResults;
	public ArrayList<Integer> saleItems = new ArrayList<Integer>();
	public ArrayList<Integer> saleAmount = new ArrayList<Integer>();
	public ArrayList<Integer> salePrice = new ArrayList<Integer>();
	public int[] historyItems = new int[15];
	public int[] historyItemsN = new int[15];
	public int[] historyPrice = new int[15];

	public boolean inSelecting = false, isListing = false;

	private RechargeItems rechargeItems = new RechargeItems(this);
	/**
	 * Classes
	 */
	private ExpLock explock = new ExpLock(this);
	private PrestigeSkills prestigeskills = new PrestigeSkills(this);
	private LootingBag lootingBag = new LootingBag(this);
	private SafeBox safeBox = new SafeBox(this);

	public RechargeItems getRechargeItems() {
		return rechargeItems;
	}

	private UltraMysteryBox ultraMysteryBox= new UltraMysteryBox(this);

	public UltraMysteryBox getUltraMysteryBox() {
		return ultraMysteryBox;
	}
	
	private NormalMysteryBox normalMysteryBox = new NormalMysteryBox(this);
	
	public NormalMysteryBox getNormalMysteryBox() {
		return normalMysteryBox;
	}
	public TeleportType teleportType;
	public int teleSelected = 0;

	public TeleportData teleportData;
	public boolean placeHolderWarning = false;
	public int lastPlaceHolderWarning = 0;
	public boolean placeHolders = false;
	public final Stopwatch last_trap_layed = new Stopwatch();

	public List<Integer> searchList = new ArrayList<>();

	private final QuickPrayers quick = new QuickPrayers();

	private AchievementDiary<?> diary;
	private QuestTab questTab = new QuestTab(this);
	private EventCalendar eventCalendar = new EventCalendar(this);
	private RunePouch runePouch = new RunePouch(this);
	private HerbSack herbSack = new HerbSack(this);
	private GemBag gemBag = new GemBag(this);
	private RandomEventInterface randomEventInterface = new RandomEventInterface(this);
	private DemonicGorilla demonicGorilla = null;
	private Mining mining = new Mining(this);
	private PestControlRewards pestControlRewards = new PestControlRewards(this);
	private WarriorsGuild warriorsGuild = new WarriorsGuild(this);
	private Zulrah zulrah = new Zulrah(this);
	private Kraken kraken = new Kraken(this);
	private NPCDeathTracker npcDeathTracker = new NPCDeathTracker(this);
	private UnnecessaryPacketDropper packetDropper = new UnnecessaryPacketDropper();
	private DamageQueueEvent damageQueue = new DamageQueueEvent(this);
	private BountyHunter bountyHunter = new BountyHunter(this);
	private SuperMysteryBox superMysteryBox = new SuperMysteryBox(this);
	private CoinBagSmall coinBagSmall = new CoinBagSmall(this);
	private CoinBagMedium coinBagMedium = new CoinBagMedium(this);
	private CoinBagLarge coinBagLarge = new CoinBagLarge(this);
	private CoinBagBuldging coinBagBuldging = new CoinBagBuldging(this);
	private HourlyRewardBox hourlyRewardBox = new HourlyRewardBox();
	private PvmCasket pvmCasket = new PvmCasket();
	private SkillCasket skillCasket = new SkillCasket(this);
	private DailyGearBox dailyGearBox = new DailyGearBox(this);
	private DailyLogin dailylogin = new DailyLogin(this);
	private DailySkillBox dailySkillBox = new DailySkillBox(this);

	private LocalDate lastVote = LocalDate.of(1970, 1, 1);
	private long lastContainerSearch;
	private AchievementHandler achievementHandler;
	private PlayerKill playerKills;
	private String macAddress;
	private String referral;
	private Duel duelSession = new Duel(this);
	private Player itemOnPlayer;
	private Killstreak killstreaks;
	private PunishmentPanel punishmentPanel = new PunishmentPanel(this);
	private Tutorial tutorial = new Tutorial(this);
	private Mode mode = new RegularMode(ModeType.REGULAR);
	private Channel session;
	private Trade trade = new Trade(this);
	private ItemAssistant itemAssistant = new ItemAssistant(this);
	private ShopAssistant shopAssistant = new ShopAssistant(this);
	private PlayerAssistant playerAssistant = new PlayerAssistant(this);
	private CombatAssistant combat = new CombatAssistant(this);
	private ActionHandler actionHandler = new ActionHandler(this);
	private DialogueHandler dialogueHandler = new DialogueHandler(this);
	private Friends friend = new Friends(this);
	private Ignores ignores = new Ignores(this);
	private Queue<Packet> queuedPackets = new ConcurrentLinkedQueue<>();
	private Potions potions = new Potions(this);
	private PotionMixing potionMixing = new PotionMixing(this);
	private Food food = new Food(this);
	private Killstreak killingStreak = new Killstreak(this);
	private SkillInterfaces skillInterfaces = new SkillInterfaces(this);
	private ChargeTrident chargeTrident = new ChargeTrident(this);
	private PlayerAction playerAction = new PlayerAction(this);
	private TeleportHandler teleportHandler = new TeleportHandler(this);
	private Slayer slayer;
	private Runecrafting runecrafting = new Runecrafting();
	private AgilityHandler agilityHandler = new AgilityHandler();
	private PointItems pointItems = new PointItems(this);
	private GnomeAgility gnomeAgility = new GnomeAgility();
	private WildernessAgility wildernessAgility = new WildernessAgility();
	private Shortcuts shortcuts = new Shortcuts();
	private RooftopSeers rooftopSeers = new RooftopSeers();
	private RooftopAlkharid rooftopAlkharid = new RooftopAlkharid();
	private RooftopFalador rooftopFalador = new RooftopFalador();
	private RooftopVarrock rooftopVarrock = new RooftopVarrock();
	private RooftopDraynor rooftopDraynor = new RooftopDraynor();
	private RooftopArdougne rooftopArdougne = new RooftopArdougne();
	private RooftopCanafis rooftopCanafis = new RooftopCanafis();
	private RooftopPollnivneach rooftopPollnivneach = new RooftopPollnivneach();
	private RooftopRellekka rooftopRellekka = new RooftopRellekka();
	private BarbarianAgility barbarianAgility = new BarbarianAgility();
	private Lighthouse lighthouse = new Lighthouse();
	private Agility agility = new Agility(this);
	private Cooking cooking = new Cooking();
	private Crafting crafting = new Crafting(this);
	private Prayer prayer = new Prayer(this);
	private Smithing smith = new Smithing(this);
	private FightCave fightcave = null;
	private Raids raids = null;
	private Xeric xeric = null;
	//private Tournament tournament = null;
	private DagannothMother dagannothMother = null;
	private RecipeForDisaster recipeForDisaster = null;
	private KalphiteQueen kq = null;
	private Cerberus cerberus = null;
	private Skotizo skotizo = null;
	private InstanceSoloFight soloFight = null;
	private SmithingInterface smithInt = new SmithingInterface(this);
	private Herblore herblore = new Herblore(this);
	private Thieving thieving = new Thieving(this);
	private Fletching fletching = new Fletching(this);
	private Godwars godwars = new Godwars(this);
	private TreasureTrails trails = new TreasureTrails(this);
	private Optional<ItemCombination> currentCombination = Optional.empty();
	private Skilling skilling = new Skilling(this);
	private Farming farming = new Farming(this);
	private ZulrahLostItems lostItemsZulrah;
	private CerberusLostItems lostItemsCerberus;
	private SkotizoLostItems lostItemsSkotizo;
	private List<God> equippedGodItems;
	private Titles titles = new Titles(this);
	protected RightGroup rights;
	protected static Stream playerProps;
	public static PlayerSave save;
	public static Player cliento2;
	public Player diceHost;
	public Clan clan;
	public Smelting.Bars bar = null;
	public byte buffer[] = null;
	public Stream inStream = null, outStream = null;
	public SimpleTimer potionTimer = new SimpleTimer();
	public int[] degradableItem = new int[Degrade.MAXIMUM_ITEMS];
	public boolean[] claimDegradableItem = new boolean[Degrade.MAXIMUM_ITEMS];
	private Entity targeted;

	private Queue<Consumer<Player>> queuedActions = new ConcurrentLinkedQueue<>();

	public void addQueuedAction(Consumer<Player> action) {
		queuedActions.add(action);
	}

	public void processQueuedActions() {
		Consumer<Player> action;
		while ((action = queuedActions.poll()) != null) {
			action.accept(this);
		}
	}

	private Equipment equipment;

	public Equipment getEquipment() {
		return equipment;
	}
	
	private long infernoBestTime;

	public Inferno inferno = null;

	public void setInferno(Inferno inferno) {
		this.inferno = inferno;
	}

	public Inferno getInferno() {
		return inferno;
	}

	/**
	 * Integers
	 */
	private int enterAmountInterfaceId;
	public int raidPoints, ether, crawsbowCharge, elvenCharge, pages, thammaronCharge, viggoraCharge, unfPotHerb, unfPotAmount, wrenchObject = 0, halloweenOrderNumber = 0, speed1 = -1,
			speed2 = -1, safeBoxSlots = 15, hostAmount = 3, corpDamage = 0, direction = -1, dialogueOptions = 0,
			sireHits = 0, lastMenuChosen = 0, dreamTime, unNoteItemId = 0, lootValue = 0, lowMemoryVersion = 0, emote,
			gfx, timeOutCounter = 0, hitCount = 0, hydraAttackCount = 0, countUntilPoison = 0, returnCode = 2, currentRegion = 0, diceItem, page, specRestore = 0, gwdAltar = 0,
			lastClickedItem, slayerTasksCompleted, pestControlDamage, playerRank = 0, packetSize = 0, packetType = -1,
			makeTimes, event, ratsCaught, summonId, bossKills, droppedItem = -1, kbdCount, dagannothCount, krakenCount,
			chaosCount, armaCount, bandosCount, saraCount, zammyCount, barrelCount, moleCount, callistoCount,
			venenatisCount, vetionCount, rememberNpcIndex, diceMin, diceMax, otherDiceId, betAmount, totalProfit,
			betsWon, betsLost, slayerPoints = 0, playTime, killStreak, day, month, YEAR, totalLevel, xpTotal,
			smeltAmount = 0, smeltEventId = 5567, waveType, achievementsCompleted, achievementPoints, fireslit,
			crabsKilled, treesCut, pkp, pvmp, xpMaxSkills, tWin, tPoint, exchangeP,  RefU, RefP, LoyP, dayv, killcount, deathcount, votePoints, bloodPoints, amDonated, level1 = 0,
			showcase, streak, RDragonCount, ADragonCount, memberdays, autoRet,
			level2 = 0, level3 = 0, treeX, treeY, homeTele = 0, homeTeleDelay = 0, lastLoginDate, playerBankPin,
			recoveryDelay = 3, attemptsRemaining = 3, lastPinSettings = -1, setPinDate = -1, changePinDate = -1,
			deletePinDate = -1, firstPin, secondPin, thirdPin, fourthPin, bankPin1, bankPin2, bankPin3, bankPin4,
			pinDeleteDateRequested, saveDelay, playerKilled, totalPlayerDamageDealt, killedBy, lastChatId = 1,
			friendSlot = 0, dialogueId, specEffect, specBarId, attackLevelReq, defenceLevelReq, strengthLevelReq,
			rangeLevelReq, magicLevelReq, followId, skullTimer, votingPoints, nextChat = 0, talkingNpc = -1,
			dialogueAction = 0, autocastId, followDistance, followId2, barrageCount = 0, delayedDamage = 0,
			delayedDamage2 = 0, pcPoints = 0, donatorPoints = 0, magePoints = 0, lastArrowUsed = -1, clanId = -1,
			pcDamage = 0, xInterfaceId = 0, xRemoveId = 0, xRemoveSlot = 0, tzhaarToKill = 0,
			tzhaarKilled = 0, waveId, rfdWave = 0, rfdChat = 0, rfdGloves = 0, frozenBy = 0, teleAction = 0,
			newPlayerAct = 0, bonusAttack = 0, lastNpcAttacked = 0, killCount = 0, actionTimer, rfdRound = 0,
			roundNpc = 0, desertTreasure = 0, horrorFromDeep = 0, QuestPoints = 0, doricQuest = 0, teleGrabItem,
			teleGrabX, teleGrabY, duelCount, underAttackByPlayer, underAttackByNpc;
	public int wildLevel;
	public int teleTimer;
	public int respawnTimer;
	public int saveTimer = 0;
	public int teleBlockLength;
	public int focusPointX = -1;
	public int focusPointY = -1;
	public int WillKeepAmt1;
	public int WillKeepAmt2;
	public int WillKeepAmt3;
	public int WillKeepAmt4;
	public int WillKeepItem1;
	public int WillKeepItem2;
	public int WillKeepItem3;
	public int WillKeepItem4;
	public int WillKeepItem1Slot;
	public int WillKeepItem2Slot;
	public int WillKeepItem3Slot;
	public int WillKeepItem4Slot;
	public int EquipStatus;
	public int faceNPC = -1;
	public int DirectionCount = 0;
	public int itemUsing;
	public int attempts = 3;
	public int follow2 = 0;
	public int antiqueSelect = 0;
	public int leatherType = -1;
	public int DELAY = 1250;
	public int rangeEndGFX;
	public int boltDamage;
	public int teleotherType;
	public int playerTradeWealth;
	public int doAmount;
	public int woodcuttingTree;
	public int stageT;
	public int dfsCount;
	public int recoilHits;
	public int playerDialogue;
	public int clawDelay;
	public int previousDamage;
	public int prayerId = -1;
	public int headIcon = -1;
	public int bountyIcon = 0;
	public int headIconPk = -1;
	public int headIconHints;
	public int specMaxHitIncrease;
	public int freezeDelay;
	public int freezeTimer = -6;
	public int teleportTimer = 0;
	public int killerId;
	public int playerAttackingIndex;
	public int lastPlayerAttackingIndex;
	public int oldPlayerIndex;
	public int lastWeaponUsed;
	public int projectileStage;
	public int crystalBowArrowCount;
	public int playerMagicBook;
	public int teleGfx;
	public int teleEndAnimation;
	public int teleHeight;
	public int teleX;
	public int teleY;
	public int rangeItemUsed;
	public int killingNpcIndex;
	public int totalDamageDealt;
	public int oldNpcIndex;
	public int fightMode;
	public int attackTimer;
	public int npcAttackingIndex;
	public int lastNpcAttackingIndex;
	public int npcClickIndex;
	public int npcType;
	public int castingSpellId;
	public int oldSpellId;
	public int spellId;
	public int hitDelay;
	public int bowSpecShot;
	public int clickNpcType;
	public int clickObjectType;
	public int objectId;
	public int objectX;
	public int objectY;
	public int objectXOffset;
	public int objectYOffset;
	public int objectDistance;
	public int itemX;
	public int itemY;
	public int itemId;
	public int destroyingItemId;
	public int myShopId;
	public int tradeStatus;
	public int tradeWith;
	public int amountGifted;
	public int playerAppearance[] = new int[13];
	public int apset;
	public int actionID;
	public int wearItemTimer;
	public int wearId;
	public int wearSlot;
	public int interfaceId;
	public int XremoveSlot;
	public int XinterfaceID;
	public int XremoveID;
	public int Xamount;
	public int fishTimer = 0;
	public int smeltType;
	public int smeltTimer = 0;
	public int attackAnim;
	public int combatLevel;
	public int wcTimer = 0;
	public int miningTimer = 0;
	public int castleWarsTeam;
	public int npcId2 = 0;
	public int playerStandIndex = 0x328;
	public int playerTurnIndex = 0x337;
	public int playerWalkIndex = 0x333;
	public int playerTurn180Index = 0x334;
	public int playerTurn90CWIndex = 0x335;
	public int playerTurn90CCWIndex = 0x336;
	public int playerRunIndex = 0x338;
	public int playerHat = 0;
	public int playerCape = 1;
	public int playerAmulet = 2;
	public int playerWeapon = 3;
	public int playerChest = 4;
	public int playerShield = 5;
	public int playerLegs = 7;
	public int playerHands = 9;
	public int playerFeet = 10;
	public int playerRing = 12;
	public int playerArrows = 13;
	public int playerAttack = 0;
	public int playerDefence = 1;
	public int playerStrength = 2;
	public int playerHitpoints = 3;
	public int playerRanged = 4;
	public int playerPrayer = 5;
	public int playerMagic = 6;
	public int playerCooking = 7;
	public int playerWoodcutting = 8;
	public int playerFletching = 9;
	public int playerFishing = 10;
	public int playerFiremaking = 11;
	public int playerMining = 14;
	public int playerHerblore = 15;
	public int playerAgility = 16;
	public int playerThieving = 17;
	public int playerSlayer = 18;
	public int playerFarming = 19;
	public int playerRunecrafting = 20;
	public int fletchingType;
	public int getHeightLevel;
	public int mapRegionX;
	public int mapRegionY;
	public int absX;
	public int absY;
	public int currentX;
	public int currentY;
	public int heightLevel;
	public int playerSE = 0x328;
	public int playerSEW = 0x333;
	public int playerSER = 0x334;
	public int npcListSize = 0;
	public int dir1 = -1;
	public int dir2 = -1;
	public int poimiX = 0;
	public int poimiY = 0;
	public int playerListSize = 0;
	public int wQueueReadPtr = 0;
	public int wQueueWritePtr = 0;
	public int teleportToX = -1;
	public int teleportToY = -1;
	public int pitsStatus = 0;
	public int safeTimer = 0;
	public int mask100var1 = 0;
	public int mask100var2 = 0;
	public int face = -1;
	public int FocusPointX = -1;
	public int FocusPointY = -1;
	public int newWalkCmdSteps = 0;
	public int tablet = 0;
	public int wellItem = -1;
	public int wellItemPrice = -1;
	public int lastX;
	public int lastY;
	public int graniteMaulSpecialCharges;

	private int chatTextColor = 0, chatTextEffects = 0, dragonfireShieldCharge, runEnergy = 100, lastEnergyRecovery,
			x1 = -1, y1 = -1, x2 = -1, y2 = -1, privateChat, shayPoints, arenaPoints, toxicStaffOfTheDeadCharge,
			toxicBlowpipeCharge, toxicBlowpipeAmmo, toxicBlowpipeAmmoAmount, serpentineHelmCharge, tridentCharge,
			toxicTridentCharge, arcLightCharge, runningDistanceTravelled, interfaceOpen;

	public final int walkingQueueSize = 50;
	public static int playerCrafting = 12, playerSmithing = 13;
	protected int numTravelBackSteps = 0, packetsReceived;

	/**
	 * Arrays
	 */
	public ArrayList<int[]> coordinates;
	private int[] farmingSeedId = new int[14];
	private int[] farmingTime = new int[14];
	private int[] originalFarmingTime = new int[14];
	private int[] farmingState = new int[14];
	private int[] farmingHarvest = new int[14];
	public int[] halloweenRiddleGiven = new int[10], halloweenRiddleChosen = new int[10],
			masterClueRequirement = new int[4], waveInfo = new int[3], keepItems = new int[4], keepItemsN = new int[4],
			voidStatus = new int[5], itemKeptId = new int[4], pouches = new int[4], playerStats = new int[8],
			playerBonus = new int[12], death = new int[4], twoHundredMil = new int[21], woodcut = new int[7],
			farm = new int[2], playerEquipment = new int[14], playerEquipmentN = new int[14], playerLevel = new int[25],
			playerXP = new int[25], damageTaken = new int[Config.MAX_PLAYERS], purchasedTeleport = new int[3],
			runeEssencePouch = new int[3], pureEssencePouch = new int[3];
	public int[] prestigeLevel = new int[25];
	public boolean[] skillLock = new boolean[25];
	public int playerItems[] = new int[28], playerItemsN[] = new int[28], bankItems[] = new int[Config.BANK_TAB_SIZE],
			bankItemsN[] = new int[Config.BANK_TAB_SIZE];
	public int counters[] = new int[20], raidsDamageCounters[] = new int[15];

	public boolean maxCape[] = new boolean[5];

	public int walkingQueueX[] = new int[walkingQueueSize], walkingQueueY[] = new int[walkingQueueSize];
	private int newWalkCmdX[] = new int[walkingQueueSize], newWalkCmdY[] = new int[walkingQueueSize];
	protected int travelBackX[] = new int[walkingQueueSize], travelBackY[] = new int[walkingQueueSize];
	public static final int maxPlayerListSize = Config.MAX_PLAYERS, maxNPCListSize = Config.MAX_NPCS_IN_LOCAL_LIST;
	public Player playerList[] = new Player[maxPlayerListSize];
	public int[][] playerSkillProp = new int[20][15];
	public final int[] POUCH_SIZE = { 3, 6, 9, 12 };
	public static int[] ranks = new int[11];

	public boolean receivedStarter = false;
	public boolean combatFollowing = false;

	/**
	 * Strings
	 */
	public String CERBERUS_ATTACK_TYPE = "";

	public String getATTACK_TYPE() {
		return CERBERUS_ATTACK_TYPE;
	}

	public void setATTACK_TYPE(String aTTACK_TYPE) {
		CERBERUS_ATTACK_TYPE = aTTACK_TYPE;
	}
	public String wrenchUsername = "", wogwOption = "", forcedText = "null", connectedFrom = "", quizAnswer = "",
			globalMessage = "", playerName = null, playerName2 = null, playerPass = null, date, clanName, properName,
			bankPin = "", lastReported = "", currentTime, barType = "", playerTitle = "", rottenPotatoOption = "";
	private String lastClanChat = "", revertOption = "";
	public static String[] rankPpl = new String[11];

	/**
	 * Booleans
	 */
	public boolean[] invSlot = new boolean[28], equipSlot = new boolean[14], playerSkilling = new boolean[20],
			clanWarRule = new boolean[10], duelRule = new boolean[22];
	public boolean teleportingToDistrict = false, morphed = false, isIdle = false, boneOnAltar = false,
			dropRateInKills = true, droppingItem = false, acceptAid = false, settingUnnoteAmount = false,
			settingLootValue = false, didYouKnow = true, yellChannel = true, documentGraphic = false,
			documentAnimation = false, inProcess = false, isStuck = false, isBusyFollow = false, hasOverloadBoost,
			needsNewTask = false, keepTitle = false, killTitle = false, hide = false, settingMin, settingMax,
			settingBet, playerIsCrafting,
			viewingRunePouch = false, hasFollower = false, updateItems = false, claimedReward, craftDialogue,
			battlestaffDialogue, braceletDialogue, isAnimatedArmourSpawned, isSmelting = false, hasEvent,
			expLock = false, buyingX, leverClicked = false, isBanking = true, inSafeBox = false, isCooking = false,
			initialized = false, disconnected = false, ruleAgreeButton = false, rebuildNPCList = false,
			isActive = false, isKicked = false, isSkulled = false, friendUpdate = false, newPlayer = false,
			hasMultiSign = false, saveCharacter = false, mouseButton = false, splitChat = false, chatEffects = true,
			nextDialogue = false, autocasting = false, autocastingDefensive, usedSpecial = false, mageFollow = false, dbowSpec = false,
			craftingLeather = false, properLogout = false, secDbow = false, maxNextHit = false, ssSpec = false,
			vengOn = false, addStarter = false, startPack = false, accountFlagged = false, msbSpec = false,
			dtOption = false, dtOption2 = false, doricOption = false, doricOption2 = false, caOption2 = false,
			caOption2a = false, caOption4a = false, caOption4b = false, caOption4c = false, caPlayerTalk1 = false,
			horrorOption = false, rfdOption = false, inDt = false, inHfd = false, disableAttEvt = false,
			AttackEventRunning = false, npcindex, spawned = false, hasBankPin, enterdBankpin, firstPinEnter,
			requestPinDelete, secondPinEnter, thirdPinEnter, fourthPinEnter, hasBankpin,
			appearanceUpdateRequired = true, isDead = false, randomEvent = false, FirstClickRunning = false,
			WildernessWarning = false, storing = false, canChangeAppearance = false, mageAllowed, isFullBody = false,
			isFullHelm = false, isFullMask = false, isOperate, usingLamp = false, normalLamp = false,
			antiqueLamp = false, setPin = false, teleporting, isWc, wcing, usingROD = false, multiAttacking,
			rangeEndGFXHeight, playerFletch, playerIsFletching, playerIsMining, playerIsFiremaking, playerIsFishing,
			playerIsCooking, below459 = true, defaultWealthTransfer, updateInventory, oldSpec, stopPlayerSkill,
			playerStun, stopPlayerPacket, usingClaws, playerBFishing, finishedBarbarianTraining, ignoreDefence,
			secondFormAutocast, usingArrows, usingOtherRangeWeapons, usingCross, usingBallista, magicDef, spellSwap,
			recoverysSet, protectItem = false, doubleHit, usingSpecial, npcDroppingItems, usingRangeWeapon, usingBow,
			usingMagic, usingAirSpells, usingWaterSpells, usingFireSpells, usingMelee, magicFailed, oldMagicFailed,
			isMoving, walkingToItem, isShopping, updateShop, forcedChatUpdateRequired, inDuel, tradeAccepted, goodTrade,
			inTrade, tradeRequested, tradeResetNeeded, tradeConfirmed, tradeConfirmed2, canOffer, acceptTrade,
			acceptedTrade, smeltInterface, patchCleared, saveFile = false, usingGlory = false, usingSkills = false,
			fishing = false, isRunning2 = true, takeAsNote, swaping, inCwGame, inCwWait, isNpc, seedPlanted = false,
			seedWatered = false, patchCleaned = false, patchRaked = false, inPits = false, bankNotes = false,
			isRunning = true, inSpecMode = false, didTeleport = false, mapRegionDidChange = false, createItems = false,
			slayerHelmetEffect, inArdiCC, attackSkill = false, strengthSkill = false, defenceSkill = false,
			mageSkill = false, rangeSkill = false, prayerSkill = false, healthSkill = false, usingChin, infoOn = false,
			pkDistrict = false, crystalDrop = false, hourlyBoxToggle = true, fracturedCrystalToggle = true,
			boltTips = false, arrowTips = false, javelinHeads = false;
	private boolean incentiveWarning, dropWarning = true, chatTextUpdateRequired = false, newWalkCmdIsRunning = false,
			dragonfireShieldActive, forceMovement, invisible, godmode, safemode, trading, stopPlayer, isBusy = false,
			isBusyHP = false, forceMovementActive = false;

	public boolean insidePost = false;

	/**
	 * @return the forceMovement
	 */
	public boolean isForceMovementActive() {
		return forceMovementActive;
	}

	protected boolean graphicMaskUpdate0x100 = false, faceUpdateRequired = false, faceNPCupdate = false;

	private final AchievementDiaryManager diaryManager = new AchievementDiaryManager(this);

	public int visibility = 0;
	/**
	 * Longs
	 */
	public long wogwDonationAmount, lastAuthClaim, totalGorillaDamage, totalGorillaHitsDone, totalMissedGorillaHits, totalHunllefDamage, totalHunllefDamage2, totalHunllefHitsDone, totalMissedHunllefHits, 
			lastImpling, lastWheatPass, lastRoll, lastCloseOfInterface, lastPerformedEmote, lastPickup, lastTeleport,
			lastMarkDropped, lastObstacleFail, lastForceMovement, lastDropTableSelected, lastDropTableSearch,
			lastDamageCalculation, buySlayerTimer, buyPestControlTimer,
			fightCaveLeaveTimer, infernoLeaveTimer, lastFire, lastMove, bonusXpTime, yellDelay, craftingDelay,
			lastSmelt = 0, lastMysteryBox, lastbrother, lastItemExchange, lastItem, lastYell, diceDelay, lastChat, lastRandom, lastCaught = 0, lastAttacked,
			lastTargeted, homeTeleTime, lastDagChange = -1, reportDelay, lastPlant, objectTimer, npcTimer, lastEss,
			lastClanMessage, lastCast, miscTimer, lastFlower, waitTime, saveButton = 0, lastButton, jailEnd, muteEnd,
			marketMuteEnd, lastReport = 0, stopPrayerDelay, prayerDelay, lastAntifirePotion, antifireDelay,
			staminaDelay, lastRunRecovery, rangeDelay, stuckTime, friends[] = new long[200],
			lastUpdate = System.currentTimeMillis(), lastPlayerMove = System.currentTimeMillis(), lastHeart = 0, openCasketTimer = 0,
			lastSpear = System.currentTimeMillis(), lastProtItem = System.currentTimeMillis(),
			dfsDelay = System.currentTimeMillis(), lastVeng = System.currentTimeMillis(),
			foodDelay = System.currentTimeMillis(), switchDelay = System.currentTimeMillis(),
			potDelay = System.currentTimeMillis(), vorkathDelay = System.currentTimeMillis(), teleGrabDelay = System.currentTimeMillis(),
			protMageDelay = System.currentTimeMillis(), protMeleeDelay = System.currentTimeMillis(),
			protRangeDelay = System.currentTimeMillis(), lastAction = System.currentTimeMillis(),
			lastThieve = System.currentTimeMillis(), lastLockPick = System.currentTimeMillis(),
			alchDelay = System.currentTimeMillis(), specCom = System.currentTimeMillis(),
			specDelay = System.currentTimeMillis(), duelDelay = System.currentTimeMillis(),
			teleBlockDelay = System.currentTimeMillis(), godSpellDelay = System.currentTimeMillis(),
			singleCombatDelay = System.currentTimeMillis(), singleCombatDelay2 = System.currentTimeMillis(),
			reduceStat = System.currentTimeMillis(), restoreStatsDelay = System.currentTimeMillis(),
			logoutDelay = System.currentTimeMillis(), buryDelay = System.currentTimeMillis(),
			cerbDelay = System.currentTimeMillis(), cleanDelay = System.currentTimeMillis(),
			wogwDelay = System.currentTimeMillis();
	private long revertModeDelay, experienceCounter, bestZulrahTime, lastIncentive, lastOverloadBoost, nameAsLong,
			lastDragonfireShieldAttack;
	public long clickDelay;

	/**
	 * The amount of time before we are out of combat.
	 */
	private long inCombatDelay = AttackPlayer.STANDARD_IN_COMBAT_DELAY;

	public void setInCombatDelay(long inCombatDelay) {
		this.inCombatDelay = inCombatDelay;
	}

	/**
	 * Others
	 */
	public ArrayList<String> killedPlayers = new ArrayList<String>(), lastConnectedFrom = new ArrayList<String>();
	public double specAmount = 0, specAccuracy = 1, specDamage = 1, prayerPoint = 1.0, crossbowDamage;
	public byte playerInListBitmap[] = new byte[(Config.MAX_PLAYERS + 7) >> 3],
			npcInListBitmap[] = new byte[(NPCHandler.maxNPCs + 7) >> 3],
			cachedPropertiesBitmap[] = new byte[(Config.MAX_PLAYERS + 7) >> 3];
	private byte chatText[] = new byte[4096], chatTextSize = 0;
	public NPC npcList[] = new NPC[maxNPCListSize];
	public ArrayList<Integer> attackedPlayers = new ArrayList<Integer>();
	private Map<Integer, TinterfaceText> interfaceText = new HashMap<>();

	@Override
	public String toString() {
		return "player[" + playerName + "]";
	}

	public Position getPosition() {
		return new Position(absX, absY, heightLevel);
	}

	public boolean isManagement() {
		return getRights().isOrInherits(Right.ADMINISTRATOR, Right.OWNER);
	}

//	private static int bot = 0;
//	public static Player newBot() {
//		int slot = Server.playerHandler.nextSlot();
//		Player player = new Player(slot, "Bot" + (++bot), null);
//		player.playerName = "Bot" + (++bot);
//		player.playerName2 = player.playerName;
//		player.playerPass = "467w4teus45ued4578esw547se57es4567";
//		player.setNameAsLong(Misc.playerNameToInt64(player.playerName));
//		player.outStream = null;
//		player.saveCharacter = false;
//		player.isActive = true;
//		player.connectedFrom = "";
//		player.setMacAddress("");
//		Server.playerHandler.add(player);
//		player.initialize();
//		player.initialized = true;
//		return player;
//	}
	
	public Player(int index, String name, Channel channel) {
		super(index, name);
		this.session = channel;
		rights = new RightGroup(this, Right.PLAYER);
		for (int i = 0; i < playerItems.length; i++) {
			playerItems[i] = 0;
		}
		for (int i = 0; i < playerItemsN.length; i++) {
			playerItemsN[i] = 0;
		}

		for (int i = 0; i < playerLevel.length; i++) {
			if (i == 3) {
				playerLevel[i] = 10;
			} else {
				playerLevel[i] = 1;
			}
		}

		for (int i = 0; i < playerXP.length; i++) {
			if (i == 3) {
				playerXP[i] = 1300;
			} else {
				playerXP[i] = 0;
			}
		}
		for (int i = 0; i < Config.BANK_TAB_SIZE; i++) {
			bankItems[i] = 0;
		}

		for (int i = 0; i < Config.BANK_TAB_SIZE; i++) {
			bankItemsN[i] = 0;
		}

		playerAppearance[0] = 0; // gender
		playerAppearance[1] = 0; // head
		playerAppearance[2] = 18;// Torso
		playerAppearance[3] = 26; // arms
		playerAppearance[4] = 33; // hands
		playerAppearance[5] = 36; // legs
		playerAppearance[6] = 42; // feet
		playerAppearance[7] = 10; // beard
		playerAppearance[8] = 0; // hair colour
		playerAppearance[9] = 0; // torso colour
		playerAppearance[10] = 0; // legs colour
		playerAppearance[11] = 0; // feet colour
		playerAppearance[12] = 0; // skin colour

		apset = 0;
		actionID = 0;

		playerEquipment[playerHat] = -1;
		playerEquipment[playerCape] = -1;
		playerEquipment[playerAmulet] = -1;
		playerEquipment[playerChest] = -1;
		playerEquipment[playerShield] = -1;
		playerEquipment[playerLegs] = -1;
		playerEquipment[playerHands] = -1;
		playerEquipment[playerFeet] = -1;
		playerEquipment[playerRing] = -1;
		playerEquipment[playerArrows] = -1;
		playerEquipment[playerWeapon] = -1;

		heightLevel = 0;

		teleportToX = Config.START_LOCATION_X;
		teleportToY = Config.START_LOCATION_Y;

		absX = absY = -1;
		mapRegionX = mapRegionY = -1;
		currentX = currentY = 0;
		resetWalkingQueue();
		// synchronized(this) {
		outStream = new Stream(new byte[Config.BUFFER_SIZE]);
		outStream.currentOffset = 0;

		inStream = new Stream(new byte[Config.BUFFER_SIZE]);
		inStream.currentOffset = 0;
		buffer = new byte[Config.BUFFER_SIZE];
		// }
	}

	public Player getClient(String name) {
		for (Player p : PlayerHandler.players) {
			if (p != null && p.playerName.equalsIgnoreCase(name)) {
				return p;
			}
		}
		return null;
	}

	private Bank bank;

	public Bank getBank() {
		if (bank == null)
			bank = new Bank(this);
		return bank;
	}
	
	public void setBank(Bank bank) {
		this.bank = bank;
	}

	private BankPin pin;

	public BankPin getBankPin() {
		if (pin == null)
			pin = new BankPin(this);
		return pin;
	}

	public void sendMessage(String s, int color) {
		// synchronized (this) {
		if (getOutStream() != null) {
			s = "<col=" + color + ">" + s + "</col>";
			getOutStream().createFrameVarSize(253);
			getOutStream().writeString(s);
			getOutStream().endFrameVarSize();
		}
	}

	public Player getClient(int id) {
		return PlayerHandler.players[id];
	}

	public void flushOutStream() {
		if (!session.isConnected() || disconnected || outStream.currentOffset == 0)
			return;

		byte[] temp = new byte[outStream.currentOffset];
		System.arraycopy(outStream.buffer, 0, temp, 0, temp.length);
		Packet packet = new Packet(-1, Type.FIXED, ChannelBuffers.wrappedBuffer(temp));
		session.write(packet);
		getOutStream().currentOffset = 0;
	}

	public class TinterfaceText {
		public int id;
		public String currentState;

		public TinterfaceText(String s, int id) {
			this.currentState = s;
			this.id = id;
		}

	}

	public boolean checkPacket126Update(String text, int id) {
		if (interfaceText.containsKey(id)) {
			TinterfaceText t = interfaceText.get(id);
			if (text.equals(t.currentState)) {
				return false;
			}
		}
		interfaceText.put(id, new TinterfaceText(text, id));
		return true;
	}

	public static final int playerHunter = 0;
	public boolean hunllefDead = false;

	public int VERIFICATION = 0;

	public void TournamentHiscores(Player c) {
		c.getDH().sendDialogues(983, 311);
	}
	private boolean updatedHs = false;

	public long disconnectTime = 0;
	public void setDisconnected() {
		disconnected = true;
		disconnectTime = System.currentTimeMillis();
	}

	public void destruct() {
		Hunter.abandon(this, null, true);
		if (session == null) {
			return;
		}
		if (!updatedHs) {
			if (this.getRights().getPrimary().getValue() != 2
					&& this.getRights().getPrimary().getValue() != 3) {
				new Thread(new Highscores(this)).start();
			}
			updatedHs = !updatedHs;
		}
		if (combatLevel >= 100) {
			if (Highpkarena.getState(this) != null) {
				Highpkarena.removePlayer(this, true);
			}
		} else if (combatLevel >= 80 && combatLevel <= 99) {
			if (Lowpkarena.getState(this) != null) {
				Lowpkarena.removePlayer(this, true);
			}
		}
		TourneyManager.getSingleton().handleXLog(this);
		if (getXeric() != null) {
			getXeric().removePlayer(this);
		}
		if (Boundary.isIn(this, Boundary.XERIC_LOBBY)) {
			XericLobby.removePlayer(this);
		}
		if(getRaidsInstance() != null) {
			getRaidsInstance().logout(this);
		}
		if (zulrah.getInstancedZulrah() != null) {
			InstancedAreaManager.getSingleton().disposeOf(zulrah.getInstancedZulrah());
		}
		if (getKraken().getInstance() != null) {
			getKraken().getInstance().dispose();
			InstancedAreaManager.getSingleton().disposeOf(getKraken().getInstance());
		}
		if (dagannothMother != null) {
			InstancedAreaManager.getSingleton().disposeOf(dagannothMother);
		}
		if (recipeForDisaster != null) {
			InstancedAreaManager.getSingleton().disposeOf(recipeForDisaster);
		}
		if (cerberus != null) {
			InstancedAreaManager.getSingleton().disposeOf(cerberus);
		}
		if (inferno != null) {
			InstancedAreaManager.getSingleton().disposeOf(inferno);
		}
		if (skotizo != null) {
			InstancedAreaManager.getSingleton().disposeOf(skotizo);
		}
		if (Vorkath.inVorkath(this)) {
			getPA().movePlayer(2272, 4052, 0);
		}
		if (getPA().viewingOtherBank) {
			getPA().resetOtherBank();
		}
		if (Boundary.isIn(this, PestControl.GAME_BOUNDARY)) {
			PestControl.removeGameMember(this);
		}
		if (Boundary.isIn(this, PestControl.LOBBY_BOUNDARY)) {
			PestControl.removeFromLobby(this);
		}

		if (disconnected) {
			saveCharacter = true;
		}
		Server.getMultiplayerSessionListener().removeOldRequests(this);
		if (clan != null) {
			clan.removeMember(this);
		}
		Server.getEventHandler().stop(this);
		CycleEventHandler.getSingleton().stopEvents(this);
		getFriends().notifyFriendsOfUpdate();
		 if (getRights().contains(Right.OWNER) || getRights().contains(Right.GAME_DEVELOPER)) {
		 } else if (getMode().isIronman()) {
	          new Thread(new Hiscores(this)).start();
		 } else if (getMode().isUltimateIronman()) {
	          new Thread(new Hiscores(this)).start();
		 } else if (getMode().isOsrs()) {
	          new Thread(new Hiscores(this)).start();
		 } else if (getMode().isHCIronman()) {
	          new Thread(new Hiscores(this)).start();
		 } else if (getMode().isRegular()) {
	          new Thread(new Hiscores(this)).start();
	      }
		teleBlockDelay = 0;
		teleBlockLength = 0;
		saveCharacter = true;
		Misc.println("[Logged out]: " + playerName);
		session.close();
		session = null;
		inStream = null;
		outStream = null;
		isActive = false;
		buffer = null;
		playerListSize = 0;
		for (int i = 0; i < maxPlayerListSize; i++)
			playerList[i] = null;
		absX = absY = -1;
		mapRegionX = mapRegionY = -1;
		currentX = currentY = 0;
		resetWalkingQueue();
	}

	public void sendMessage(String s) {
		// synchronized (this) {
		if (getOutStream() != null) {
			getOutStream().createFrameVarSize(253);
			getOutStream().writeString(s);
			getOutStream().endFrameVarSize();
		}
	}

	public void setSidebarInterface(int menuId, int form) {
		// synchronized (this) {
		if (getOutStream() != null) {
			getOutStream().createFrame(71);
			getOutStream().writeUnsignedWord(form);
			getOutStream().writeByteA(menuId);
		}

	}

	public int diaryAmount = 0;

	public int amountOfDiariesComplete() {
		if (getDiaryManager().getVarrockDiary().hasDoneAll()) {
			diaryAmount += 1;
		    d1Complete = true;
		}
		if (getDiaryManager().getArdougneDiary().hasDoneAll()) {
			diaryAmount += 1;
			d2Complete = true;	
		}
		if (getDiaryManager().getDesertDiary().hasDoneAll()) {
			diaryAmount += 1;
		    d3Complete = true;	
		}
		if (getDiaryManager().getFaladorDiary().hasDoneAll()) {
			diaryAmount += 1;
			d4Complete = true;
		}
		if (getDiaryManager().getFremennikDiary().hasDoneAll()) {
			diaryAmount += 1;
			d5Complete = true;
		}
		if (getDiaryManager().getKandarinDiary().hasDoneAll()) {
			diaryAmount += 1;
			d6Complete = true;
		}
		if (getDiaryManager().getKaramjaDiary().hasDoneAll()) {
			diaryAmount += 1;
			d7Complete = true;
		}
		if (getDiaryManager().getLumbridgeDraynorDiary().hasDoneAll()) {
			diaryAmount += 1;
			d8Complete = true;
		}
		if (getDiaryManager().getMorytaniaDiary().hasDoneAll()) {
			diaryAmount += 1;
			d9Complete = true;
		}
		if (getDiaryManager().getWesternDiary().hasDoneAll()) {
			diaryAmount += 1;
			d10Complete = true;
		}
		if (getDiaryManager().getWildernessDiary().hasDoneAll()) {
			diaryAmount += 1;
			d11Complete = true;
		}
			if (diaryAmount >= 11) {
				PlayerHandler.executeGlobalMessage("@red@[DIARY]@pur@"+playerName+" has just completed all of the diarys.");
			}

		return diaryAmount;
	}

	public void refreshQuestTab(int i) {

	}

	private enum RankUpgrade {
		REGULAR_DONATOR(Right.REGULAR_DONATOR, 10), SUPER_DONOR(Right.SUPER_DONOR, 50), EXTREME_DONOR(Right.EXTREME_DONOR, 100), ULTRA_DONATOR(
				Right.ULTRA_DONATOR, 200), LEGENDARY_DONATOR(Right.LEGENDARY_DONATOR,
						300), DIAMOND_CLUB(Right.DIAMOND_CLUB, 500), ONYX_CLUB(Right.ONYX_CLUB, 1000), PLATINUM(Right.PLATINUM, 2500), DIVINE(Right.DIVINE, 5000);

		/**
		 * The rights that will be appended if upgraded
		 */
		private final Right rights;

		/**
		 * The amount required for the upgrade
		 */
		private final int amount;

		private RankUpgrade(Right rights, int amount) {
			this.rights = rights;
			this.amount = amount;
		}
	}

	public void initialize() {
		try {
			graceSum();
			Achievements.checkIfFinished(this);
			getPA().loadQuests();
			setStopPlayer(false);
			getPlayerAction().setAction(false);
			getPlayerAction().canWalk(true);
			inPresets = false;
			inTradingPost = false;
			inBank = false;
			inLamp = false;
			inDonatorBox = false;
			getSuperMysteryBox().canMysteryBox();
			getNormalMysteryBox().canMysteryBox();
			getUltraMysteryBox().canMysteryBox();
			getPA().sendFrame126(runEnergy + "%", 149);
			isFullHelm = Item.isFullHat(playerEquipment[playerHat]);
			isFullMask = Item.isFullMask(playerEquipment[playerHat]);
			isFullBody = Item.isFullBody(playerEquipment[playerChest]);
			getPA().sendFrame36(173, isRunning2 ? 1 : 0);
			getPA().setConfig(427, acceptAid ? 1 : 0);
			
			/**
			 * Welcome messages
			 */
			sendMessage("@bla@Welcome to Wisdom! Don't forget to join our ::discord");
			if(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY 
	        		|| Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY){
				sendMessage("@bla@Bonus XP Weekend is currently [@gre@ACTIVE@bla@]");
			}
			if (combatLevel >= 126) {
				getEventCalendar().progress(EventChallenge.HAVE_126_COMBAT);
			}
			if (getRights().getPrimary().equals(Right.HELPER)) {
					PlayerHandler.executeGlobalMessage(
							"[@red@Staff@bla@]@blu@ @cr10@ <col=255>" + Misc.formatPlayerName(this.playerName) + "@bla@ has just logged in!");
				} else if (getRights().getPrimary().equals(Right.MODERATOR)) {
					PlayerHandler.executeGlobalMessage(
							"[@red@Staff@bla@]@blu@ @cr1@ <col=255>" + Misc.formatPlayerName(this.playerName) + "@bla@ has just logged in!");
				} else if (getRights().getPrimary().equals(Right.GAME_DEVELOPER)) {
					PlayerHandler.executeGlobalMessage(
							"[@red@Staff@bla@]@blu@ @cr3@ <col=255>" + Misc.formatPlayerName(this.playerName) + "@bla@ has just logged in!");
				} else if (getRights().getPrimary().equals(Right.ADMINISTRATOR)) {
					PlayerHandler.executeGlobalMessage(
							"[@red@Staff@bla@]@blu@ @cr3@ <col=255>" + Misc.formatPlayerName(this.playerName) + "@bla@ has just logged in!");
				} else if (getRights().getPrimary().equals(Right.OWNER)) {
					PlayerHandler.executeGlobalMessage(
							"[@red@Staff@bla@]@blu@ @cr3@ <col=255>" + Misc.formatPlayerName(this.playerName) + "@bla@ has just logged in!");
				}
			if (getSlayer().superiorSpawned) {
				getSlayer().superiorSpawned = false;
			}
			
			if (getRights().isOrInherits(Right.IRONMAN) || getRights().isOrInherits(Right.HC_IRONMAN)) {
				ArrayList<RankUpgrade> orderedList = new ArrayList<>(Arrays.asList(RankUpgrade.values()));
				orderedList.sort((one, two) -> Integer.compare(two.amount, one.amount));
				orderedList.stream().filter(r -> amDonated >= r.amount).findFirst().ifPresent(rank -> {
					RightGroup rights = getRights();
					Right right = rank.rights;
					if (!rights.contains(right)) {
						sendMessage("@blu@Congratulations, your rank has been upgraded to " + right.toString() + ".");
						sendMessage("@blu@This rank is hidden, but you will have all it's perks.");
						rights.add(right);
					}
				});
			}
			combatLevel = calculateCombatLevel();
			getOutStream().createFrame(249);
			getOutStream().writeByteA(1); // 1 for members, zero for free
			getOutStream().writeWordBigEndianA(getIndex());

			for (int j = 0; j < PlayerHandler.players.length; j++) {
				if (j == getIndex())
					continue;
				if (PlayerHandler.players[j] != null) {
					if (PlayerHandler.players[j].playerName.equalsIgnoreCase(playerName))
						disconnected = true;
				}
			}
			for (int p = 0; p < PRAYER.length; p++) { // reset prayer glows
				prayerActive[p] = false;
				getPA().sendFrame36(PRAYER_GLOW[p], 0);
			}

			accountFlagged = getPA().checkForFlags();
			getPA().sendFrame36(108, 0);
			getPA().sendFrame36(172, 1);
			getPA().sendFrame107(); // reset screen
			setSidebarInterface(0, 2423);
			setSidebarInterface(1, 13917); // Skilltab > 3917
			setSidebarInterface(2, QuestTab.INTERFACE_ID);
			setSidebarInterface(3, 3213);
			setSidebarInterface(4, 1644);
			setSidebarInterface(5, 15608);
			switch (playerMagicBook) {
			case 0:
				setSidebarInterface(6, 938); // modern
				break;

			case 1:
				setSidebarInterface(6, 838); // ancient
				break;

			case 2:
				setSidebarInterface(6, 29999); // ancient
				break;
			}

			if (hasFollower) {
				if (summonId > 0) {
					Pets pet = PetHandler.forItem(summonId);
					if (pet != null) {
						PetHandler.spawn(this, pet, true, false);
					}
				}
			}
			if (splitChat) {
				getPA().sendFrame36(502, 1);
				getPA().sendFrame36(287, 1);
			}
			setSidebarInterface(7, 18128);
			setSidebarInterface(8, 5065);
			setSidebarInterface(9, 5715);
			setSidebarInterface(10, 2449);
			setSidebarInterface(11, 42500); // wrench tab
			setSidebarInterface(12, 147); // run tab
			setSidebarInterface(13, 21406);
			getPA().showOption(4, 0, "Follow", 3);
			getPA().showOption(5, 0, "Trade with", 4);
			// getPA().showOption(6, 0, nu, 5);
			getItems().resetItems(3214);
			getItems().sendWeapon(playerEquipment[playerWeapon]
			);
			getItems().resetBonus();
			getItems().getBonus();
			getItems().writeBonus();
			getItems().setEquipment(playerEquipment[playerHat], 1, playerHat);
			getItems().setEquipment(playerEquipment[playerCape], 1, playerCape);
			getItems().setEquipment(playerEquipment[playerAmulet], 1, playerAmulet);
			getItems().setEquipment(playerEquipment[playerArrows], playerEquipmentN[playerArrows], playerArrows);
			getItems().setEquipment(playerEquipment[playerChest], 1, playerChest);
			getItems().setEquipment(playerEquipment[playerShield], 1, playerShield);
			getItems().setEquipment(playerEquipment[playerLegs], 1, playerLegs);
			getItems().setEquipment(playerEquipment[playerHands], 1, playerHands);
			getItems().setEquipment(playerEquipment[playerFeet], 1, playerFeet);
			getItems().setEquipment(playerEquipment[playerRing], 1, playerRing);
			getItems().setEquipment(playerEquipment[playerWeapon], playerEquipmentN[playerWeapon], playerWeapon);
			getCombat().getPlayerAnimIndex(ItemAssistant.getItemName(playerEquipment[playerWeapon]).toLowerCase());
			if (getPrivateChat() > 2) {
				setPrivateChat(0);
			}

			getOutStream().createFrame(221);
			getOutStream().writeByte(2);

			getOutStream().createFrame(206);
			getOutStream().writeByte(0);
			getOutStream().writeByte(getPrivateChat());
			getOutStream().writeByte(0);
			getFriends().sendList();
			getIgnores().sendList();
			getPA().sendTimeZone();

			getQuestTab().openTab(QuestTab.Tab.INFORMATION);
			getItems().addSpecialBar(playerEquipment[playerWeapon]);
			saveTimer = Config.SAVE_TIMER;
			spawnedbarrows = false;
			saveCharacter = true;
			Server.playerHandler.updatePlayer(this, outStream);
			Server.playerHandler.updateNPC(this, outStream);
			flushOutStream();
			totalLevel = getPA().totalLevel();
			xpTotal = getPA().xpTotal();
			getPA().updateQuestTab(); //diary tab
			getQuestTab().updateInformationTab();
			getPA().sendFrame126("Combat Level: " + combatLevel + "", 3983);
			getPA().sendFrame126("Total level:", 19209);
			getPA().sendFrame126(totalLevel + "", 3984);
			getPA().resetFollow();
			getPA().clearClanChat();
			getPA().resetFollow();
			getPA().setClanData();
			updateRank();
			getBank().onLogin();
			getRunePouch().sendPouchRuneInventory();
			getPA().updatePoisonStatus();
			getItems().updateFightModeConfig();

			if (TourneyManager.getSingleton().isInLobbyBoundsOnLogin(this)) {
				TourneyManager.getSingleton().handleLoginWithinLobby(this);
			}
			if (TourneyManager.getSingleton().isInArenaBoundsOnLogin(this)) {
				TourneyManager.getSingleton().handleLoginWithinArena(this);
			}
			if (totalLevel >= 2000) {
				getEventCalendar().progress(EventChallenge.HAVE_2000_TOTAL_LEVEL);
			}
			List<String> DailyLogin = DailyLoginUses.getUsedDL();
			long usedDailyLogin = DailyLogin.stream().filter(data -> data.equals(getMacAddress())).count();
			if (usedDailyLogin > 1) {
				getDailyLogin().LoggedIn();
			}
			if (startPack == false) {
				getRights().remove(Right.IRONMAN);
				getRights().remove(Right.ULTIMATE_IRONMAN);
				getRights().remove(Right.HC_IRONMAN);
				startPack = true;
				Server.clanManager.getHelpClan().addMember(this);
				tutorial.setStage(Stage.START);
				mode = Mode.forType(ModeType.REGULAR);
			} else {
				if (mode == null && tutorial.getStage() == null) {
					mode = Mode.forType(ModeType.REGULAR);
					tutorial.autoComplete();
				}
				Server.clanManager.joinOnLogin(this);
			}
			if (tutorial.isActive()) {
				tutorial.refresh();
			}
			if (autoRet == 1)
				getPA().sendFrame36(172, 1);
			else
				getPA().sendFrame36(172, 0);
			addEvents();
			if (Config.BOUNTY_HUNTER_ACTIVE) {
				bountyHunter.updateTargetUI();
			}
			for (int i = 0; i < 22; i++) {
				getPA().setSkillLevel(i, playerLevel[i], playerXP[i]);
				getPA().refreshSkill(i);
			}
			health.setMaximumHealth(getPA().getLevelForXP(playerXP[playerHitpoints]));
			BankPin pin = getBankPin();
			if (pin.requiresUnlock()) {
				pin.open(2);
				
			}
			correctCoordinates();
			initialized = true;
			if (health.getCurrentHealth() < 10) {
				health.setCurrentHealth(10);
			}
			int[] ids = new int[playerLevel.length];
			for (int skillId = 0; skillId < ids.length; skillId++) {
				ids[skillId] = skillId;
			}
			if (experienceCounter > 0L) {
				playerAssistant.sendExperienceDrop(false, experienceCounter, ids);
			}
			getCollectionLog().loadCollections();
			rechargeItems.onLogin();
			DailyTasks.complete(this);
			DailyTasks.assignTask(this);
			for (int i = 0; i < getQuick().getNormal().length; i++) {
				if (getQuick().getNormal()[i]) {
					getPA().sendConfig(QuickPrayers.CONFIG + i, 1);
				} else {
					getPA().sendConfig(QuickPrayers.CONFIG + i, 0);
				}
			}

            PollTab.updateInterface(this);

			if (EventCalendar.isEventRunning()) {
				sendMessage(EventCalendar.MESSAGE_COLOUR + "The " + EventCalendar.EVENT_NAME + " is in progress! @red@Use ::cal for details.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Player login - Check for error");
		}
	}
	public boolean firstMove;
	public static void move(Player c, int EndX, int EndY) {
		c.getPlayerAction().setAction(true);
		c.getPlayerAction().canWalk(false);
		c.getPA().walkTo2(EndX, EndY);
	}

	public void addEvents() {
		Server.getEventHandler().submit(new MinigamePlayersEvent(this));
		Server.getEventHandler().submit(new SkillRestorationEvent(this));
		Server.getEventHandler().submit(new IronmanRevertEvent(this, 50));
		Server.getEventHandler().submit(new RunEnergyEvent(this, 1));
		CycleEventHandler.getSingleton().addEvent(this, bountyHunter, 1);
		//CycleEventHandler.getSingleton().addEvent(CycleEventHandler.Event.PLAYER_COMBAT_DAMAGE, this, damageQueue, 1,
				//true);
	}

	public void update() {
		Server.playerHandler.updatePlayer(this, outStream);
		Server.playerHandler.updateNPC(this, outStream);
		flushOutStream();

	}

	public void wildyWarning() {
		getPA().showInterface(1908);
	}

	/**
	 * Update {@link #equippedGodItems}, which is a list of all gods of which the
	 * player has at least 1 item equipped.
	 */
	
	public void updateGodItems() {
		equippedGodItems = new ArrayList<>();
		for (God god : God.values()) {
			for (Integer itemId : GodwarsEquipment.EQUIPMENT.get(god)) {
				if (getItems().isWearingItem(itemId)) {
					equippedGodItems.add(god);
					break;
				}
			}
		}
	}


	public List<God> getEquippedGodItems() {
		return equippedGodItems;
	}

	public void logout() {
		if (this.clan != null) {
			this.clan.removeMember(this);
		}
		if (Vorkath.inVorkath(this)) {
			this.getPA().movePlayer(2272, 4052, 0);
		}
		if (getPA().viewingOtherBank) {
			getPA().resetOtherBank();
		}
		if (!updatedHs) {
			if (this.getRights().getPrimary().getValue() != 2
					&& this.getRights().getPrimary().getValue() != 3) {
				new Thread(new Highscores(this)).start();
			}
			updatedHs = !updatedHs;
		}
		DuelSession duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(this,
				MultiplayerSessionType.DUEL);
		if (Objects.nonNull(duelSession) && duelSession.getStage().getStage() > MultiplayerSessionStage.REQUEST) {
			if (duelSession.getStage().getStage() >= MultiplayerSessionStage.FURTHER_INTERATION) {
				sendMessage("You are not permitted to logout during a duel. If you forcefully logout you will");
				sendMessage("lose all of your staked items to your opponent.");
			}
		}
		if (!isIdle && underAttackByNpc > 0) {
			sendMessage("You can't log out until 10 seconds after the end of combat.");
			return;
		}
		if (underAttackByPlayer > 0) {
			sendMessage("You can't log out until 10 seconds after the end of combat.");
			return;
		}
		if (System.currentTimeMillis() - logoutDelay > 1000) {
			Hunter.abandon(this, null, true);
			if (skotizo != null)
				skotizo.end(DisposeTypes.INCOMPLETE);
			CycleEventHandler.getSingleton().stopEvents(this);
			getPA().sendLogout();
			properLogout = true;
			disconnected = true;
			ConnectedFrom.addConnectedFrom(this, connectedFrom);
		}
	}

	public int totalRaidsFinished;
	public int totalXericFinished;

	public boolean hasClaimedRaidChest;

	public int[] SLAYER_HELMETS = { 11864, 11865, 19639, 19641, 19643, 19645, 19647, 19649, 21888, 21890, 21264, 21266 };
	public int[] IMBUED_SLAYER_HELMETS = { 11865, 19641, 19645, 19649, 21890, 21266 };

	public int[] GRACEFUL = { 11850, 11852, 11854, 11856, 11858, 11860, 13579, 13581, 13583, 13585, 13587, 13589, 13591,
			13593, 13595, 13597, 13599, 13601, 13603, 13605, 13607, 13609, 13611, 13613, 13615, 13617, 13619, 13621,
			13623, 13625, 13627, 13629, 13631, 13633, 13635, 13637, 13667, 13669, 13671, 13673, 13675, 13677, 21061,
			21064, 21067, 21070, 21073, 21076 };

	private boolean wearingGrace() {
		return getItems().isWearingAnyItem(GRACEFUL);
	}

	public int graceSum = 0;

	public void graceSum() {
		graceSum = 0;
		for (int grace : GRACEFUL) {
			if (getItems().isWearingItem(grace)) {
				graceSum++;
			}
		}
		if (SkillcapePerks.AGILITY.isWearing(this) || SkillcapePerks.isWearingMaxCape(this)) {
			graceSum++;
		}
	}

	public int olmType, leftClawType, rightClawType;

	public boolean leftClawDead;
	public boolean rightClawDead;

	public boolean hasSpawnedOlm;

	public void checkInstanceCoords() {
		if (getInstance() != null && (!getInstance().getBoundary().in(this) || getInstance().isDisposed())) {
			getInstance().remove(this);
		}
	}
	
	public void processDamageQueue() {
		damageQueue.execute();
	}

	public void process() {
		processQueuedActions();
		if (xpScrollTicks > 0) {
			xpScrollTicks--;
			if (xpScrollTicks <= 0) {
				xpScrollTicks = 0;
				xpScroll = false;
				sendMessage("@red@Your xp scroll has run out!");
			}
		}
		if (getInstance() != null) {
			getInstance().tick(this);
		}
		farming.farmingProcess();

		if (isRunning && runEnergy <= 0) {
			isRunning = false;
			isRunning2 = false;
			playerAssistant.sendFrame126(Integer.toString(runEnergy) + "%", 149);
			playerAssistant.setConfig(173, 0);
		}

		if (staminaDelay > 0) {
			staminaDelay--;
		}

		if (gwdAltar > 0) {
			gwdAltar--;
		}
		if (gwdAltar == 1) {
			sendMessage("You can now operate the godwars prayer altar again.");
		}

		if (isRunning && runningDistanceTravelled > (wearingGrace() ? 1 + graceSum : staminaDelay != -1 ? 3 : 1)) {
			runningDistanceTravelled = 0;
			runEnergy -= 1;
			playerAssistant.sendFrame126(Integer.toString(runEnergy) + "%", 149);
		}

		if (isRunning && runningDistanceTravelled > (wearingGrace() ? 1 + graceSum : staminaDelay != -1 ? 3 : 1)) {
			runningDistanceTravelled = 0;
			runEnergy -= 1;
			playerAssistant.sendFrame126(Integer.toString(runEnergy) + "%", 149);
		}

		if (updateItems) {
			itemAssistant.updateItems();
			updateItems = false;
		}

		if (bonusXpTime > 0) {
			bonusXpTime--;
		}
		if (bonusXpTime == 1) {
			sendMessage("@blu@Your time is up. Your XP is no longer boosted by the voting reward.");
		}

		if (isDead && respawnTimer == -6) {
			getPA().applyDead();
		}

		if (respawnTimer == 7) {
			respawnTimer = -6;
			getPA().giveLife();
		} else if (respawnTimer == 12) {

			// Set killer in combat delay
			if (underAttackByPlayer > 0 && underAttackByPlayer < PlayerHandler.players.length && PlayerHandler.players[underAttackByPlayer] != null) {
				PlayerHandler.players[underAttackByPlayer].setInCombatDelay(AttackPlayer.KILLED_PLAYER_COMBAT_DELAY);
			}

			// Animation
			respawnTimer--;
			startAnimation(0x900);
		}
		if (Boundary.isIn(this, Zulrah.BOUNDARY) && getZulrahEvent().isInToxicLocation()) {
			appendDamage(1 + Misc.random(3), Hitmark.VENOM);
		}

		if (respawnTimer > -6) {
			respawnTimer--;
		}
		if (hitDelay > 0) {
			hitDelay--;
		}

		getAgilityHandler().agilityProcess(this);

		if (specRestore > 0) {
			specRestore--;
		}

		if (rangeDelay > 0) {
			rangeDelay--;
		}
		if (playTime < Integer.MAX_VALUE && !isIdle) {
			playTime++;
		}

		//getPA().sendFrame126("@or1@Players Online: @gre@" + PlayerHandler.getPlayerCount() + "", 10222);
		if (System.currentTimeMillis() - specDelay > Config.INCREASE_SPECIAL_AMOUNT) {
			specDelay = System.currentTimeMillis();
			if (specAmount < 10) {
				specAmount += 1;
				if (specAmount > 10)
					specAmount = 10;
				getItems().updateSpecialBar();
				getItems().addSpecialBar(playerEquipment[playerWeapon]);
			}
		}

		getCombat().handlePrayerDrain();
		if (underAttackByPlayer > 0) {
			if (System.currentTimeMillis() - singleCombatDelay > inCombatDelay) {
				underAttackByPlayer = 0;
				setInCombatDelay(AttackPlayer.STANDARD_IN_COMBAT_DELAY);
			}
		}
		if (underAttackByNpc > 0) {
			if (System.currentTimeMillis() - singleCombatDelay2 > inCombatDelay) {
				underAttackByNpc = 0;
				setInCombatDelay(AttackPlayer.STANDARD_IN_COMBAT_DELAY);
			}
		}

		if (hasOverloadBoost) {
			if (System.currentTimeMillis() - lastOverloadBoost > 15000) {
				getPotions().doOverloadBoost();
				lastOverloadBoost = System.currentTimeMillis();
			}
		}
		if (inWild() && Boundary.isIn(this, Boundary.SAFEPK)) {
			int modY = absY > 6400 ? absY - 6400 : absY;
			wildLevel = (((modY - 3520) / 8) + 1);
			if (Config.SINGLE_AND_MULTI_ZONES) {
				getPA().sendFrame126("@yel@Level: " + wildLevel, 199);
			} else {
				getPA().multiWay(-1);
				getPA().sendFrame126("@yel@Level: " + wildLevel, 199);
			}
			getPA().showOption(3, 0, "Attack", 1);
			if (Config.BOUNTY_HUNTER_ACTIVE && !inClanWars()) {
				getPA().walkableInterface(28000);
				getPA().sendFrame171(1, 28070);
				getPA().sendFrame171(0, 196);
			} else {
				getPA().walkableInterface(197);
			}
		} else if (inWild() && !inClanWars() && !Boundary.isIn(this, Boundary.SAFEPK)) {
			int modY = absY > 6400 ? absY - 6400 : absY;
			wildLevel = (((modY - 3520) / 8) + 1);
			if (Config.SINGLE_AND_MULTI_ZONES) {
				//System.out.println("ATTEMPTING TO SEND LEVEL: " + wildLevel);
				getPA().sendFrame126("@yel@Level: " + wildLevel, 199);
			} else {
				getPA().multiWay(-1);
				getPA().sendFrame126("@yel@Level: " + wildLevel, 199);
			}
			getPA().showOption(3, 0, "Attack", 1);
			if (Config.BOUNTY_HUNTER_ACTIVE && !inClanWars()) {
				getPA().walkableInterface(28000);
				getPA().sendFrame171(1, 28070);
				getPA().sendFrame171(0, 196);
			} else {
				getPA().walkableInterface(197);
			}
			
			if (Boundary.isIn(this, Boundary.DEEP_WILDY_CAVES)) {
				getPA().sendFrame126("", 199);
				getPA().sendFrame126("@yel@Level: " + wildLevel, 250);
			} else {
				getPA().sendFrame126("", 250);
			}

			// } else if (Boundary.isIn(this, Boundary.SKELETAL_MYSTICS)) {
			// getPA().walkableInterface(42300);
		} else if (inClanWars() && inWild()) {
			getPA().showOption(3, 0, "Attack", 1);
			getPA().walkableInterface(197);
			getPA().sendFrame126("@yel@3-126", 199);
			wildLevel = 126;
		} else if (Boundary.isIn(this, Boundary.SCORPIA_LAIR)) {
			getPA().sendFrame126("@yel@Level: 54", 199);
			// getPA().walkableInterface(197);
			wildLevel = 54;
		} else if (getItems().isWearingItem(10501, 3) && !inWild()) {
			getPA().showOption(3, 0, "Throw-At", 1);
		} else if (inEdgeville()) {
			if (Config.BOUNTY_HUNTER_ACTIVE) {
				if (bountyHunter.hasTarget()) {
					getPA().walkableInterface(28000);
					getPA().sendFrame171(0, 28070);
					getPA().sendFrame171(1, 196);
					bountyHunter.updateOutsideTimerUI();
				} else {
					getPA().walkableInterface(-1);
				}
			} else {
				getPA().sendFrame99(0);
				getPA().walkableInterface(-1);
				getPA().showOption(3, 0, "Null", 1);
			}
			getPA().showOption(3, 0, "null", 1);
		} else if (Boundary.isIn(this, PestControl.LOBBY_BOUNDARY)) {
			getPA().walkableInterface(21119);
			PestControl.drawInterface(this, "lobby");
		} else if (Boundary.isIn(this, PestControl.GAME_BOUNDARY)) {
			getPA().walkableInterface(21100);
			PestControl.drawInterface(this, "game");
		} else if ((inDuelArena() || Boundary.isIn(this, Boundary.DUEL_ARENA))) {
			getPA().walkableInterface(201);
			if (Boundary.isIn(this, Boundary.DUEL_ARENA)) {
				getPA().showOption(3, 0, "Attack", 1);
			} else {
				getPA().showOption(3, 0, "Challenge", 1);
			}
			wildLevel = 126;
		} else if (inGodwars()) {
			godwars.drawInterface();
			getPA().walkableInterface(16210);
		} else if (inCwGame || inPits) {
			getPA().showOption(3, 0, "Attack", 1);
		} else if (getPA().inPitsWait()) {
			getPA().showOption(3, 0, "Null", 1);
		} else if (Boundary.isIn(this, Boundary.SKOTIZO_BOSSROOM)) {
			getPA().walkableInterface(29230);
		} else if (TourneyManager.getSingleton().isInArena(this)) {
			getPA().showOption(3, 0, "Attack", 1);
			getPA().walkableInterface(264);
		} else if (TourneyManager.getSingleton().isInLobbyBounds(this)) {
			getPA().walkableInterface(264);
		} else if (inRaidLobby()) {
			getPA().walkableInterface(6673);
		} else {
			getPA().walkableInterface(-1);
			getPA().showOption(3, 0, "Null", 1);
		}
		//if (Boundary.isIn(this, Boundary.PURO_PURO)) { Turn back Puro Puro Block
			//getPA().sendFrame99(2);
		//}

		if (Boundary.isIn(this, Boundary.ICE_PATH)) {
			getPA().sendFrame99(2);
			if (getRunEnergy() > 0)
				setRunEnergy(0);
			if (heightLevel > 0)
				getPA().icePath();
		}

		if (!inWild()) {
			wildLevel = 0;
		}
		if(Boundary.isIn(this, Boundary.EDGEVILLE_PERIMETER) && !Boundary.isIn(this, Boundary.EDGE_BANK) && getHeight() == 8){
			wildLevel=126;
		}
		if (!hasMultiSign && inMulti()) {
			hasMultiSign = true;
			getPA().multiWay(1);
		}
		if (hasMultiSign && !inMulti()) {
			hasMultiSign = false;
			getPA().multiWay(-1);
		}
		if (!inMulti() && inWild())
			getPA().sendFrame70(30, 0, 196);
		else if (inMulti() && inWild())
			getPA().sendFrame70(0, 0, 196);
		if (this.skullTimer > 0) {
			--skullTimer;
			if (skullTimer == 1) {
				isSkulled = false;
				attackedPlayers.clear();
				headIconPk = -1;
				skullTimer = -1;
				getPA().requestUpdates();
			}
		}

		if (freezeTimer > -6) {
			freezeTimer--;
			if (frozenBy > 0) {
				if (PlayerHandler.players[frozenBy] == null) {
					freezeTimer = -1;
					frozenBy = -1;
				} else if (!goodDistance(absX, absY, PlayerHandler.players[frozenBy].absX,
						PlayerHandler.players[frozenBy].absY, 20)) {
					freezeTimer = -1;
					frozenBy = -1;
				}
			}
		}

		if (teleTimer > 0) {
			teleTimer--;
			if (!isDead) {
				if (teleTimer == 1) {
					teleTimer = 0;
				}
				if (teleTimer == 5) {
					teleTimer--;
					getPA().processTeleport();
				}
				if (teleTimer == 9 && teleGfx > 0) {
					teleTimer--;
					gfx100(teleGfx);
				}
			} else {
				teleTimer = 0;
			}
		}

		if (attackTimer > 0) {
			attackTimer--;
		}

		
		if (targeted != null) {
			if (distanceToPoint(targeted.getX(), targeted.getY()) > 10) {
				getPA().sendEntityTarget(0, targeted);
				targeted = null;
			}
		}

        if (npcAttackingIndex > 0 && clickNpcType == 0 || playerAttackingIndex > 0) {
            // Attempt to execute a granite maul special if queued
            getCombat().doQueuedGraniteMaulSpecials();
        }

		if (attackTimer <= 1) {
			if (npcAttackingIndex > 0 && clickNpcType == 0) {
				getCombat().attackNpc(npcAttackingIndex);
			}
			if (playerAttackingIndex > 0) {
				getCombat().attackPlayer(playerAttackingIndex);
			}
		}
		
		
		if (underAttackByPlayer <= 0 && underAttackByNpc <= 0 && !inMulti() && lastAttacked < System.currentTimeMillis() - 4000
				&& lastTargeted < System.currentTimeMillis() - 4000) {
			NPC closestNPC = null;
			int closestDistance = Integer.MAX_VALUE;
			if (!isIdle) {
				for (NPC npc : NPCHandler.npcs) {
					if (npc == null || !isTargetableBy(npc) || npc.killerId == index) {
						continue;
					}
					int distance = Misc.distanceToPoint(absX, absY, npc.absX, npc.absY);
					if (distance < closestDistance && distance <= Server.npcHandler.distanceRequired(npc.getIndex())
							+ Server.npcHandler.followDistance(npc.getIndex())) {
						closestDistance = distance;
						closestNPC = npc;
					}
				}
				if (closestNPC != null) {
					closestNPC.killerId = getIndex();
					lastTargeted = System.currentTimeMillis();
				}
			}
		}
		
		getItems().processContainerUpdates();
		getPA().sendXpDrops();
	}

	public boolean isTargetableBy(NPC npc) {
		return !npc.isDead && Server.npcHandler.isAggressive(npc.getIndex(), false) && !npc.underAttack
				&& npc.killerId <= 0 && npc.getHeight() == heightLevel;
	}

	public Stream getInStream() {
		return inStream;
	}

	public int getPacketType() {
		return packetType;
	}

	public int getPacketSize() {
		return packetSize;
	}

	public Stream getOutStream() {
		return outStream;
	}

	public ItemAssistant getItems() {
		return itemAssistant;
	}

	public PlayerAssistant getPA() {
		return playerAssistant;
	}
	
	public CollectionLog getCollectionLog() {
		return collectionLog;
	}

	public DialogueHandler getDH() {
		return dialogueHandler;
	}

	public ChargeTrident getCT() {
		return chargeTrident;
	}

	public ShopAssistant getShops() {
		return shopAssistant;
	}

	public CombatAssistant getCombat() {
		return combat;
	}

	public ActionHandler getActions() {
		return actionHandler;
	}

	public Killstreak getStreak() {
		return killingStreak;
	}

	public Channel getSession() {
		return session;
	}

	public Potions getPotions() {
		return potions;
	}

	public PotionMixing getPotMixing() {
		return potionMixing;
	}

	public Food getFood() {
		return food;
	}

	public boolean checkBusy() {
		/*
		 * if (getCombat().isFighting()) { return true; }
		 */
		if (isBusy) {
			// actionAssistant.sendMessage("You are too busy to do that.");
		}
		return isBusy;
	}

	public boolean checkBusyHP() {
		return isBusyHP;
	}

	public boolean checkBusyFollow() {
		return isBusyFollow;
	}

	public void setBusy(boolean isBusy) {
		this.isBusy = isBusy;
	}

	public boolean isBusy() {
		return isBusy;
	}

	public void setBusyFollow(boolean isBusyFollow) {
		this.isBusyFollow = isBusyFollow;
	}

	public void setBusyHP(boolean isBusyHP) {
		this.isBusyHP = isBusyHP;
	}

	public boolean isBusyHP() {
		return isBusyHP;
	}

	public boolean isBusyFollow() {
		return isBusyFollow;
	}

	public PlayerAssistant getPlayerAssistant() {
		return playerAssistant;
	}

	public SkillInterfaces getSI() {
		return skillInterfaces;
	}

	public int getRuneEssencePouch(int index) {
		return runeEssencePouch[index];
	}

	public void setRuneEssencePouch(int index, int runeEssencePouch) {
		this.runeEssencePouch[index] = runeEssencePouch;
	}

	public int getPureEssencePouch(int index) {
		return pureEssencePouch[index];
	}

	public void setPureEssencePouch(int index, int pureEssencePouch) {
		this.pureEssencePouch[index] = pureEssencePouch;
	}

	public Slayer getSlayer() {
		if (slayer == null) {
			slayer = new Slayer(this);
		}
		return slayer;
	}

	public Runecrafting getRunecrafting() {
		return runecrafting;
	}

	public Cooking getCooking() {
		return cooking;
	}

	public Agility getAgility() {
		return agility;
	}

	public Crafting getCrafting() {
		return crafting;
	}

	public Thieving getThieving() {
		return thieving;
	}

	public Herblore getHerblore() {
		return herblore;
	}

	public Godwars getGodwars() {
		return godwars;
	}

	public TreasureTrails getTrails() {
		return trails;
	}

	public GnomeAgility getGnomeAgility() {
		return gnomeAgility;
	}

	public PointItems getPoints() {
		return pointItems;
	}

	public PlayerAction getPlayerAction() {
		return playerAction;
	}

	public WildernessAgility getWildernessAgility() {
		return wildernessAgility;
	}

	public Shortcuts getAgilityShortcuts() {
		return shortcuts;
	}

	public RooftopPollnivneach getRooftopPollnivneach() {
		return this.rooftopPollnivneach;
	}

	public RooftopCanafis getRooftopCanafis() {
		return this.rooftopCanafis;
	}

	public RooftopAlkharid getRooftopAlkharid() {
		return this.rooftopAlkharid;
	}

	public RooftopSeers getRoofTopSeers() {
		return this.rooftopSeers;
	}

	public RooftopFalador getRooftopFalador() {
		return this.rooftopFalador;
	}

	public RooftopVarrock getRoofTopVarrock() {
		return this.rooftopVarrock;
	}

	public RooftopDraynor getRoofTopDraynor() {
		return this.rooftopDraynor;
	}

	public RooftopArdougne getRoofTopArdougne() {
			return this.rooftopArdougne;
	}
	
	public RooftopRellekka getRooftopRellekka() {
		return this.rooftopRellekka;
	}
	
	public Lighthouse getLighthouse() {
		return lighthouse;
	}

	public BarbarianAgility getBarbarianAgility() {
		return barbarianAgility;
	}

	public AgilityHandler getAgilityHandler() {
		return agilityHandler;
	}

	public Smithing getSmithing() {
		return smith;
	}

	public FightCave getFightCave() {
		if (fightcave == null)
			fightcave = new FightCave(this);
		return fightcave;
	}
	public Raids getRaids() {
		return raids;
	}

	public DagannothMother getDagannothMother() {
		return dagannothMother;
	}
	public DemonicGorilla getDemonicGorilla() {
		return demonicGorilla;
	}

	public RecipeForDisaster getrecipeForDisaster() {
		return recipeForDisaster;
	}

	public Cerberus getCerberus() {
		return cerberus;
	}

	public Skotizo getSkotizo() {
		return skotizo;
	}

	public InstanceSoloFight getSoloFight() {
		return soloFight;
	}

	public DagannothMother createDagannothMotherInstance() {
		Boundary boundary = Boundary.LIGHTHOUSE;

		int height = InstancedAreaManager.getSingleton().getNextOpenHeight(boundary);

		dagannothMother = new DagannothMother(this, boundary, height);

		return dagannothMother;
	}
	public RecipeForDisaster createRecipeForDisasterInstance() {
		Boundary boundary = Boundary.RFD;

		int height = InstancedAreaManager.getSingleton().getNextOpenHeightCust(boundary, 2);

		recipeForDisaster = new RecipeForDisaster(this, boundary, height);

		return recipeForDisaster;
	}

	public Cerberus createCerberusInstance() {
		Boundary boundary = Boundary.BOSS_ROOM_WEST;

		int height = InstancedAreaManager.getSingleton().getNextOpenHeightCust(boundary, 4);

		cerberus = new Cerberus(this, boundary, height);

		return cerberus;
	}

	public Skotizo createSkotizoInstance() {
		Boundary boundary = Boundary.SKOTIZO_BOSSROOM;

		int height = InstancedAreaManager.getSingleton().getNextOpenHeightCust(boundary, 4);

		skotizo = new Skotizo(this, boundary, height);

		return skotizo;
	}

	public InstanceSoloFight createSoloFight() {
		Boundary boundary = Boundary.FIGHT_ROOM;

		int height = InstancedAreaManager.getSingleton().getNextOpenHeightCust(boundary, 4);

		soloFight = new InstanceSoloFight(this, boundary, height);

		return soloFight;
	}

	public SmithingInterface getSmithingInt() {
		return smithInt;
	}

	public int getPrestigePoints() {
		return prestigePoints;
	}
	/*
	 * public Fletching getFletching() { return fletching; }
	 */

	public Prayer getPrayer() {
		return prayer;
	}

	/**
	 * End of Skill Constructors
	 */

	public void queueMessage(Packet arg1) {
		packetsReceived++;
		queuedPackets.add(arg1);
	}

	public boolean processQueuedPackets() {
		Packet p = null;
		int processed = 0;
		packetsReceived = 0;
		while ((p = queuedPackets.poll()) != null) {
			if (processed > Config.MAX_INCOMING_PACKETS_PER_CYCLE) {
				break;
			}
			inStream.currentOffset = 0;
			packetType = p.getOpcode();
			packetSize = p.getLength();
			inStream.buffer = p.getPayload().array();
			if (packetType > 0) {
				PacketHandler.processPacket(this, packetType, packetSize);
				processed++;
			}
		}
		return true;
	}

	public void correctCoordinates() {
		final Boundary pc = PestControl.GAME_BOUNDARY;
		final Boundary fc = Boundary.FIGHT_CAVE;
		final Boundary zulrah = Zulrah.BOUNDARY;
		final Boundary inferno = Boundary.INFERNO;
		int x = teleportToX;
		int y = teleportToY;
		if (x > pc.getMinimumX() && x < pc.getMaximumX() && y > pc.getMinimumY() && y < pc.getMaximumY()) {
			teleportToX = 2657;
			teleportToY = 2639;
			heightLevel = 0;
		}
		if (Boundary.isIn(this, Boundary.XERIC) || Boundary.isIn(this, Boundary.XERIC_LOBBY)) {
			teleportToX = 3033;
			teleportToY = 6068;
			setHeight(0);
		}
		if (x > fc.getMinimumX() && x < fc.getMaximumX() && y > fc.getMinimumY() && y < fc.getMaximumY()) {
			heightLevel = getIndex() * 4;
			sendMessage("Wave " + (this.waveId + 1) + " will start in approximately 5-10 seconds. ");
			getFightCave().spawn();
		}
		if (x > inferno.getMinimumX() && x < inferno.getMaximumY() && y > inferno.getMinimumY()
				&& y < inferno.getMaximumY()) {
			Inferno.moveToExit(this);
		}
		if (x > zulrah.getMinimumX() && x < zulrah.getMaximumX() && y > zulrah.getMinimumY()
				&& y < zulrah.getMaximumY()) {
			teleportToX = Config.EDGEVILLE_X;
			teleportToY = Config.EDGEVILLE_Y;
			heightLevel = 0;
		}
	}




	public void updateRank() {
		if (amDonated <= 0) {
			amDonated = 0;
		} 
		if (amDonated >= 5 && amDonated < 10) { 
			if (getRights().isOrInherits(Right.IRONMAN) || getRights().isOrInherits(Right.ULTIMATE_IRONMAN) || getRights().isOrInherits(Right.OSRS) || getRights().isOrInherits(Right.HELPER) || getRights().isOrInherits(Right.MODERATOR) || getRights().isOrInherits(Right.HC_IRONMAN)) {
				getRights().add(Right.TRIAL_DONATOR);
				//sendMessage("Your hidden trial donator rank is now active.");
			} else {
				getRights().setPrimary(Right.TRIAL_DONATOR);
				//sendMessage("Please relog to receive your trial donator rank.");
			}
		}
		if (amDonated >= 10 && amDonated < 50) { 
			if (getRights().isOrInherits(Right.IRONMAN) || getRights().isOrInherits(Right.ULTIMATE_IRONMAN) || getRights().isOrInherits(Right.OSRS) || getRights().isOrInherits(Right.HELPER) || getRights().isOrInherits(Right.MODERATOR) || getRights().isOrInherits(Right.HC_IRONMAN)) {
				getRights().add(Right.REGULAR_DONATOR);
				//sendMessage("Your hidden donator rank is now active.");
			} else {
				getRights().setPrimary(Right.REGULAR_DONATOR);
				//sendMessage("Please relog to receive your donator rank.");
			}
		}
		if (amDonated >= 50 && amDonated < 100) {
			if (getRights().isOrInherits(Right.IRONMAN) || getRights().isOrInherits(Right.ULTIMATE_IRONMAN) || getRights().isOrInherits(Right.OSRS) || getRights().isOrInherits(Right.HELPER) || getRights().isOrInherits(Right.MODERATOR) || getRights().isOrInherits(Right.HC_IRONMAN)) {
				getRights().add(Right.SUPER_DONOR);
				//sendMessage("Your hidden super donator rank is now active.");
			} else {
				getRights().setPrimary(Right.SUPER_DONOR);
				//sendMessage("Please relog to receive your super donator rank.");
			}
		}
		if (amDonated >= 100 && amDonated < 200) {
			if (getRights().isOrInherits(Right.IRONMAN) || getRights().isOrInherits(Right.ULTIMATE_IRONMAN) || getRights().isOrInherits(Right.OSRS) || getRights().isOrInherits(Right.HELPER) || getRights().isOrInherits(Right.MODERATOR) || getRights().isOrInherits(Right.HC_IRONMAN)) {
				getRights().add(Right.EXTREME_DONOR);
				//sendMessage("Your hidden extreme donator rank is now active.");
			} else {
				getRights().setPrimary(Right.EXTREME_DONOR);
				//sendMessage("Please relog to receive your extreme donator rank.");
			}
		}
		if (amDonated >= 200 && amDonated < 300) {
			if (getRights().isOrInherits(Right.IRONMAN) || getRights().isOrInherits(Right.ULTIMATE_IRONMAN) || getRights().isOrInherits(Right.OSRS) || getRights().isOrInherits(Right.HELPER) || getRights().isOrInherits(Right.MODERATOR) || getRights().isOrInherits(Right.HC_IRONMAN)) {
				getRights().add(Right.ULTRA_DONATOR);
				//sendMessage("Your hidden ultra donator rank is now active.");
			} else {
				getRights().setPrimary(Right.ULTRA_DONATOR);
				//sendMessage("Please relog to receive your ultra donator rank.");
			}
		}
		if (amDonated >= 300 && amDonated < 500) {
			if (getRights().isOrInherits(Right.IRONMAN) || getRights().isOrInherits(Right.ULTIMATE_IRONMAN) || getRights().isOrInherits(Right.OSRS) || getRights().isOrInherits(Right.HELPER) || getRights().isOrInherits(Right.MODERATOR) || getRights().isOrInherits(Right.HC_IRONMAN)) {
				getRights().add(Right.LEGENDARY_DONATOR);
				//sendMessage("Your hidden legendary donator rank is now active.");
			} else {
				getRights().setPrimary(Right.LEGENDARY_DONATOR);
				//sendMessage("Please relog to receive your legendary donator rank.");
			}
		}
		if (amDonated >= 500 && amDonated < 1000) {
			if (getRights().isOrInherits(Right.IRONMAN) || getRights().isOrInherits(Right.ULTIMATE_IRONMAN) || getRights().isOrInherits(Right.OSRS) || getRights().isOrInherits(Right.HELPER) || getRights().isOrInherits(Right.MODERATOR) || getRights().isOrInherits(Right.HC_IRONMAN)) {
				getRights().add(Right.DIAMOND_CLUB);
				//sendMessage("Your hidden diamond club rank is now active.");
			} else {
				getRights().setPrimary(Right.DIAMOND_CLUB);
				//sendMessage("Please relog to receive your diamond club rank.");
			}
		}
		if (amDonated >= 1000 && amDonated < 2500) {
			if (getRights().isOrInherits(Right.IRONMAN) || getRights().isOrInherits(Right.ULTIMATE_IRONMAN) || getRights().isOrInherits(Right.OSRS) || getRights().isOrInherits(Right.HELPER) || getRights().isOrInherits(Right.MODERATOR) || getRights().isOrInherits(Right.HC_IRONMAN)) {
				getRights().add(Right.ONYX_CLUB);
				//sendMessage("Your hidden onyx club donator rank is now active.");
			} else {
				getRights().setPrimary(Right.ONYX_CLUB);
				//sendMessage("Please relog to receive your Onyx Club donator rank.");
			}
		}
		if (amDonated >= 2500 && amDonated < 5000) {
			if (getRights().isOrInherits(Right.IRONMAN) || getRights().isOrInherits(Right.ULTIMATE_IRONMAN) || getRights().isOrInherits(Right.OSRS) || getRights().isOrInherits(Right.HELPER) || getRights().isOrInherits(Right.MODERATOR) || getRights().isOrInherits(Right.HC_IRONMAN)) {
				getRights().add(Right.PLATINUM);
				//sendMessage("Your hidden Platinum donator rank is now active.");
			} else {
				getRights().setPrimary(Right.PLATINUM);
				//sendMessage("Please relog to receive your PLATINUM donator rank.");
			}
		}
		if (amDonated >= 5000) {
			if (getRights().isOrInherits(Right.IRONMAN) || getRights().isOrInherits(Right.ULTIMATE_IRONMAN) || getRights().isOrInherits(Right.OSRS) || getRights().isOrInherits(Right.HELPER) || getRights().isOrInherits(Right.MODERATOR) || getRights().isOrInherits(Right.HC_IRONMAN)) {
				getRights().add(Right.DIVINE);
				//sendMessage("Your hidden Divine donator rank is now active.");
			} else {
				getRights().setPrimary(Right.DIVINE);
				//sendMessage("Please relog to receive your Divine donator rank.");
			}
		}
		//sendMessage("Your updated total amount donated is now $" + amDonated + ".");
	}

	public int getPrivateChat() {
		return privateChat;
	}

	public Friends getFriends() {
		return friend;
	}

	public Ignores getIgnores() {
		return ignores;
	}
	public void setPrivateChat(int option) {
		this.privateChat = option;
	}

	public Trade getTrade() {
		return trade;
	}

	public int localX() {
		return this.getX() - this.getMapRegionX() * 8;
	}

	public int localY() {
		return this.getY() - this.getMapRegionY() * 8;
	}

	public AchievementHandler getAchievements() {
		if (achievementHandler == null)
			achievementHandler = new AchievementHandler(this);
		return achievementHandler;
	}



	public long getLastContainerSearch() {
		return lastContainerSearch;
	}

	public void setLastContainerSearch(long lastContainerSearch) {
		this.lastContainerSearch = lastContainerSearch;
	}

	public CoinBagSmall getCoinBagSmall() {
		return coinBagSmall;
	}
	public CoinBagMedium getCoinBagMedium() {
		return coinBagMedium;
	}
	public CoinBagLarge getCoinBagLarge() {
		return coinBagLarge;
	}
	public CoinBagBuldging getCoinBagBuldging() {
		return coinBagBuldging;
	}

	public SuperMysteryBox getSuperMysteryBox() {
		return superMysteryBox;
	}
	public CoinBagSmall geCoinBagSmall() {
		return coinBagSmall;
	}
	public HourlyRewardBox getHourlyRewardBox() {
		return hourlyRewardBox;
	}

	public PvmCasket getPvmCasket() {
		return pvmCasket;
	}

	public SkillCasket getSkillCasket() {
		return skillCasket;
	}

	public DailyGearBox getDailyGearBox() {
		return dailyGearBox;
	}
	public DailyLogin getDailyLogin() {
		return dailylogin;
	}
	public DailySkillBox getDailySkillBox() {
		return dailySkillBox;
	}



	public DamageQueueEvent getDamageQueue() {
		return damageQueue;
	}

	public final int[] BOWS = { 19481, 19478, 12788, 9185, 11785, 21012, 839, 845, 847, 851, 855, 859, 841, 843, 849,
			853, 857, 12424, 861, 4212, 4214, 4215, 12765, 12766, 12767, 12768, 11235, 4216, 4217, 4218, 4219, 4220,
			4221, 4222, 4223, 4734, 6724, 20997, 21902, 22550 };
	public final int[] ARROWS = { 9341, 4160, 11959, 10033, 10034, 882, 883, 884, 885, 886, 887, 888, 889, 890, 891,
			892, 893, 4740, 5616, 5617, 5618, 5619, 5620, 5621, 5622, 5623, 5624, 5625, 5626, 5627, 9139, 9140, 9141,
			9142, 9143, 11875, 21316, 21326, 9144, 9145, 9240, 9241, 9242, 9243, 9244, 9245, 9286, 9287, 9288, 9289,
			9290, 9291, 9292, 9293, 9294, 9295, 9296, 9297, 9298, 9299, 9300, 9301, 9302, 9303, 9304, 9305, 9306, 11212,
			11227, 11228, 11229, 9335, 9336, 9337, 9338, 9339, 9340, 9341, 11875, 9340, 21905, 21906, 21316, 21924, 21925,
			21926, 21927, 21928, 21929, 21930, 21931, 21932, 21933, 21934, 21935, 21936, 21937, 21938, 21939, 21940, 21941,
			21942, 21943, 21944, 21945, 21946, 21947, 21948, 21949, 21950, 21951, 21952, 21953, 21954, 21955, 21956, 21957,
			21958, 21959, 21960, 21961, 21962, 21963, 21964, 21965, 21966, 21967, 21968, 21969, 21970, 21971, 21972, 21973, 
			21974};
	public final int[] CRYSTAL_BOWS = { 4212, 4214, 4215, 4216, 4217, 4218, 4219, 4220, 4221, 4222, 4223, 22550};

	public final int[] NO_ARROW_DROP = { 11959, 10033, 10034, 4212, 22550, 4214, 4215, 4216, 4217, 4218, 4219, 4220, 4221,
			4222, 4223, 4734, 4934, 4935, 4936, 4937 };
	public final int[] OTHER_RANGE_WEAPONS = { 11959, 10033, 10034, 800, 801, 802, 803, 804, 805, 20849, 806, 807, 808,
			809, 810, 811, 812, 813, 814, 815, 816, 817, 825, 826, 827, 828, 829, 830, 831, 832, 833, 834, 835, 836,
			863, 864, 865, 866, 867, 868, 869, 870, 871, 872, 873, 874, 875, 876, 4934, 4935, 4936, 4937, 5628, 5629,
			5630, 5632, 5633, 5634, 5635, 5636, 5637, 5639, 5640, 5641, 5642, 5643, 5644, 5645, 5646, 5647, 5648, 5649,
			5650, 5651, 5652, 5653, 5654, 5655, 5656, 5657, 5658, 5659, 5660, 5661, 5662, 5663, 5664, 5665, 5666, 5667,
			6522, 11230 };
	public int compostBin = 0;
	public int reduceSpellId;
	public final int[] REDUCE_SPELL_TIME = { 250000, 250000, 250000, 500000, 500000, 500000 };
	public long[] reduceSpellDelay = new long[6];
	public final int[] REDUCE_SPELLS = { 1153, 1157, 1161, 1542, 1543, 1562 };
	public boolean[] canUseReducingSpell = { true, true, true, true, true, true };
	public boolean usingPrayer;
	public final int[] PRAYER_DRAIN_RATE = { 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500,
			500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500 };
	public final int[] PRAYER_LEVEL_REQUIRED = { 1, 4, 7, 8, 9, 10, 13, 16, 19, 22, 25, 26, 27, 28, 31, 34, 37, 40, 43,
			44, 45, 46, 49, 52, 55, 60, 70, 74, 77 };
	public final int[] PRAYER = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23,
			24, 25, 26, 27, 28 };
	public final String[] PRAYER_NAME = { "Thick Skin", "Burst of Strength", "Clarity of Thought", "Sharp Eye",
			"Mystic Will", "Rock Skin", "Superhuman Strength", "Improved Reflexes", "Rapid Restore", "Rapid Heal",
			"Protect Item", "Hawk Eye", "Mystic Lore", "Steel Skin", "Ultimate Strength", "Incredible Reflexes",
			"Protect from Magic", "Protect from Missiles", "Protect from Melee", "Eagle Eye", "Mystic Might",
			"Retribution", "Redemption", "Smite", "Preserve", "Chivalry", "Piety", "Rigour", "Augury" };
	public final int[] PRAYER_GLOW = { 83, 84, 85, 700, 701, 86, 87, 88, 89, 90, 91, 702, 703, 92, 93, 94, 95, 96, 97,
			704, 705, 98, 99, 100, 708, 706, 707, 710, 712 };
	public boolean isSelectingQuickprayers = false;
	public final int[] PRAYER_HEAD_ICONS = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 2, 1, 0,
			-1, -1, 3, 5, 4, -1, -1, -1, -1, -1 };
	public boolean[] prayerActive = { false, false, false, false, false, false, false, false, false, false, false,
			false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
			false, false, false };

	// Used by farming processor to not update the object every click
	// Created an array of booleans based on the patch number, not using an array
	// for each patch creates graphic glitches. - Tyler
	public boolean[] farmingLagReducer = new boolean[Farming.MAX_PATCHES];
	public boolean[] farmingLagReducer2 = new boolean[Farming.MAX_PATCHES];
	public boolean[] farmingLagReducer3 = new boolean[Farming.MAX_PATCHES];
	public boolean[] farmingLagReducer4 = new boolean[Farming.MAX_PATCHES];

	public Farming getFarming() {
		return farming;
	}

	public int getFarmingSeedId(int index) {
		return farmingSeedId[index];
	}

	public void setFarmingSeedId(int index, int farmingSeedId) {
		this.farmingSeedId[index] = farmingSeedId;
	}

	public int getFarmingTime(int index) {
		return this.farmingTime[index];
	}

	public int getOriginalFarmingTime(int index) { // originalFarming
		return this.originalFarmingTime[index];
	}

	public void setFarmingTime(int index, int farmingTime) {
		this.farmingTime[index] = farmingTime;
	}

	public void setOriginalFarmingTime(int index, int originalFarmingTime) {// originalFarmingTime
		this.originalFarmingTime[index] = originalFarmingTime;
	}

	public int getFarmingState(int index) {
		return farmingState[index];
	}

	public void setFarmingState(int index, int farmingState) {
		this.farmingState[index] = farmingState;
	}

	public int getFarmingHarvest(int index) {
		return farmingHarvest[index];
	}

	public void setFarmingHarvest(int index, int farmingHarvest) {
		this.farmingHarvest[index] = farmingHarvest;
	}

	/**
	 * Retrieves the bounty hunter instance for this client object. We use lazy
	 * initialization because we store values from the player save file in the
	 * bountyHunter object upon login. Without lazy initialization the value would
	 * be overwritten.
	 * 
	 * @return the bounty hunter object
	 */
	public BountyHunter getBH() {
		if (Objects.isNull(bountyHunter)) {
			bountyHunter = new BountyHunter(this);
		}
		return bountyHunter;
	}

	public UnnecessaryPacketDropper getPacketDropper() {
		return packetDropper;
	}

	public Optional<ItemCombination> getCurrentCombination() {
		return currentCombination;
	}

	public void setCurrentCombination(Optional<ItemCombination> combination) {
		this.currentCombination = combination;
	}

	public PlayerKill getPlayerKills() {
		if (Objects.isNull(playerKills)) {
			playerKills = new PlayerKill();
		}
		return playerKills;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	public String getReferral() {
		return referral;
	}

	public void setReferral(String referral) {
		this.referral = referral;
	}
	public String getIpAddress() {
		return connectedFrom;
	}

	public void setIpAddress(String ipAddress) {
		this.connectedFrom = ipAddress;
	}

	public int getMaximumHealth() {
		int base = getLevelForXP(playerXP[3]);
		if (EquipmentSet.GUTHAN.isWearingBarrows(this) && getItems().isWearingItem(12853)) {
			base += 10;
		}
		return base;
	}

	public int getMaximumPrayer() {
		return getLevelForXP(playerXP[playerPrayer]);
	}

	public Duel getDuel() {
		return duelSession;
	}

	public void setItemOnPlayer(Player player) {
		this.itemOnPlayer = player;
	}

	public Player getItemOnPlayer() {
		return itemOnPlayer;
	}

	public Skilling getSkilling() {
		return skilling;
	}



	public Killstreak getKillstreak() {
		if (killstreaks == null) {
			killstreaks = new Killstreak(this);
		}
		return killstreaks;
	}

	/**
	 * Returns the single instance of the {@link NPCDeathTracker} class for this
	 * player.
	 * 
	 * @return the tracker clas
	 */
	public NPCDeathTracker getNpcDeathTracker() {
		return npcDeathTracker;
	}

	/**
	 * The zulrah event
	 * 
	 * @return event
	 */
	public Zulrah getZulrahEvent() {
		return zulrah;
	}
	
	public Kraken getKraken() {
		return kraken;
	}

	/**
	 * The single {@link WarriorsGuild} instance for this player
	 * 
	 * @return warriors guild
	 */
	public WarriorsGuild getWarriorsGuild() {
		return warriorsGuild;
	}

	/**
	 * The single instance of the {@link PestControlRewards} class for this player
	 * 
	 * @return the reward class
	 */
	public PestControlRewards getPestControlRewards() {
		return pestControlRewards;
	}

	public Mining getMining() {
		return mining;
	}

	public PunishmentPanel getPunishmentPanel() {
		return punishmentPanel;
	}

	public void faceNPC(int index) {
		faceNPC = index;
		faceNPCupdate = true;
		setUpdateRequired(true);
	}

	public void appendFaceNPCUpdate(Stream str) {
		str.writeWordBigEndian(faceNPC);
	}

	public void ResetKeepItems() {
		WillKeepAmt1 = -1;
		WillKeepItem1 = -1;
		WillKeepAmt2 = -1;
		WillKeepItem2 = -1;
		WillKeepAmt3 = -1;
		WillKeepItem3 = -1;
		WillKeepAmt4 = -1;
		WillKeepItem4 = -1;
	}

	public void StartBestItemScan(Player c) {
		if (c.isSkulled && !c.prayerActive[10]) {
			ItemKeptInfo(c, 0);
			return;
		}
		FindItemKeptInfo(c);
		ResetKeepItems();
		BestItem1(c);
	}

	public void FindItemKeptInfo(Player c) {
		if (isSkulled && c.prayerActive[10])
			ItemKeptInfo(c, 1);
		else if (!isSkulled && !c.prayerActive[10])
			ItemKeptInfo(c, 3);
		else if (!isSkulled && c.prayerActive[10])
			ItemKeptInfo(c, 4);
	}

	public void ItemKeptInfo(Player c, int Lose) {
		for (int i = 17109; i < 17131; i++) {
			c.getPA().sendFrame126("", i);
		}
		c.getPA().sendFrame126("Items you will keep on death:", 17104);
		c.getPA().sendFrame126("Items you will lose on death:", 17105);
		c.getPA().sendFrame126("Player Information", 17106);
		c.getPA().sendFrame126("Max items kept on death:", 17107);
		c.getPA().sendFrame126("~ " + Lose + " ~", 17108);
		c.getPA().sendFrame126("The normal amount of", 17111);
		c.getPA().sendFrame126("items kept is three.", 17112);
		switch (Lose) {
		case 0:
		default:
			c.getPA().sendFrame126("Items you will keep on death:", 17104);
			c.getPA().sendFrame126("Items you will lose on death:", 17105);
			c.getPA().sendFrame126("You're marked with a", 17111);
			c.getPA().sendFrame126("@red@skull. @lre@This reduces the", 17112);
			c.getPA().sendFrame126("items you keep from", 17113);
			c.getPA().sendFrame126("three to zero!", 17114);
			break;
		case 1:
			c.getPA().sendFrame126("Items you will keep on death:", 17104);
			c.getPA().sendFrame126("Items you will lose on death:", 17105);
			c.getPA().sendFrame126("You're marked with a", 17111);
			c.getPA().sendFrame126("@red@skull. @lre@This reduces the", 17112);
			c.getPA().sendFrame126("items you keep from", 17113);
			c.getPA().sendFrame126("three to zero!", 17114);
			c.getPA().sendFrame126("However, you also have", 17115);
			c.getPA().sendFrame126("the @red@Protect @lre@Items prayer", 17116);
			c.getPA().sendFrame126("active, which saves you", 17117);
			c.getPA().sendFrame126("one extra item!", 17118);
			break;
		case 3:
			c.getPA().sendFrame126("Items you will keep on death(if not skulled):", 17104);
			c.getPA().sendFrame126("Items you will lose on death(if not skulled):", 17105);
			c.getPA().sendFrame126("You have no factors", 17111);
			c.getPA().sendFrame126("affecting the items you", 17112);
			c.getPA().sendFrame126("keep.", 17113);
			break;
		case 4:
			c.getPA().sendFrame126("Items you will keep on death(if not skulled):", 17104);
			c.getPA().sendFrame126("Items you will lose on death(if not skulled):", 17105);
			c.getPA().sendFrame126("You have the @red@Protect", 17111);
			c.getPA().sendFrame126("@red@Item @lre@prayer active,", 17112);
			c.getPA().sendFrame126("which saves you one", 17113);
			c.getPA().sendFrame126("extra item!", 17114);
			break;
		}
	}

	public void BestItem1(Player c) {
		int BestValue = 0;
		int NextValue = 0;
		int ItemsContained = 0;
		WillKeepItem1 = 0;
		WillKeepItem1Slot = 0;
		for (int ITEM = 0; ITEM < 28; ITEM++) {
			if (playerItems[ITEM] > 0) {
				ItemsContained += 1;
				NextValue = (int) Math.floor(ShopAssistant.getItemShopValue(playerItems[ITEM] - 1));
				if (NextValue > BestValue) {
					BestValue = NextValue;
					WillKeepItem1 = playerItems[ITEM] - 1;
					WillKeepItem1Slot = ITEM;
					if (playerItemsN[ITEM] > 2 && !c.prayerActive[10]) {
						WillKeepAmt1 = 3;
					} else if (playerItemsN[ITEM] > 3 && c.prayerActive[10]) {
						WillKeepAmt1 = 4;
					} else {
						WillKeepAmt1 = playerItemsN[ITEM];
					}
				}
			}
		}
		for (int EQUIP = 0; EQUIP < 14; EQUIP++) {
			if (playerEquipment[EQUIP] > 0) {
				ItemsContained += 1;
				NextValue = (int) Math.floor(ShopAssistant.getItemShopValue(playerEquipment[EQUIP]));
				if (NextValue > BestValue) {
					BestValue = NextValue;
					WillKeepItem1 = playerEquipment[EQUIP];
					WillKeepItem1Slot = EQUIP + 28;
					if (playerEquipmentN[EQUIP] > 2 && !c.prayerActive[10]) {
						WillKeepAmt1 = 3;
					} else if (playerEquipmentN[EQUIP] > 3 && c.prayerActive[10]) {
						WillKeepAmt1 = 4;
					} else {
						WillKeepAmt1 = playerEquipmentN[EQUIP];
					}
				}
			}
		}
		if (!isSkulled && ItemsContained > 1 && (WillKeepAmt1 < 3 || (c.prayerActive[10] && WillKeepAmt1 < 4))) {
			BestItem2(c, ItemsContained);
		}
	}

	public void BestItem2(Player c, int ItemsContained) {
		int BestValue = 0;
		int NextValue = 0;
		WillKeepItem2 = 0;
		WillKeepItem2Slot = 0;
		for (int ITEM = 0; ITEM < 28; ITEM++) {
			if (playerItems[ITEM] > 0) {
				NextValue = (int) Math.floor(ShopAssistant.getItemShopValue(playerItems[ITEM] - 1));
				if (NextValue > BestValue && !(ITEM == WillKeepItem1Slot && playerItems[ITEM] - 1 == WillKeepItem1)) {
					BestValue = NextValue;
					WillKeepItem2 = playerItems[ITEM] - 1;
					WillKeepItem2Slot = ITEM;
					if (playerItemsN[ITEM] > 2 - WillKeepAmt1 && !c.prayerActive[10]) {
						WillKeepAmt2 = 3 - WillKeepAmt1;
					} else if (playerItemsN[ITEM] > 3 - WillKeepAmt1 && c.prayerActive[10]) {
						WillKeepAmt2 = 4 - WillKeepAmt1;
					} else {
						WillKeepAmt2 = playerItemsN[ITEM];
					}
				}
			}
		}
		for (int EQUIP = 0; EQUIP < 14; EQUIP++) {
			if (playerEquipment[EQUIP] > 0) {
				NextValue = (int) Math.floor(ShopAssistant.getItemShopValue(playerEquipment[EQUIP]));
				if (NextValue > BestValue
						&& !(EQUIP + 28 == WillKeepItem1Slot && playerEquipment[EQUIP] == WillKeepItem1)) {
					BestValue = NextValue;
					WillKeepItem2 = playerEquipment[EQUIP];
					WillKeepItem2Slot = EQUIP + 28;
					if (playerEquipmentN[EQUIP] > 2 - WillKeepAmt1 && !c.prayerActive[10]) {
						WillKeepAmt2 = 3 - WillKeepAmt1;
					} else if (playerEquipmentN[EQUIP] > 3 - WillKeepAmt1 && c.prayerActive[10]) {
						WillKeepAmt2 = 4 - WillKeepAmt1;
					} else {
						WillKeepAmt2 = playerEquipmentN[EQUIP];
					}
				}
			}
		}
		if (!isSkulled && ItemsContained > 2
				&& (WillKeepAmt1 + WillKeepAmt2 < 3 || (c.prayerActive[10] && WillKeepAmt1 + WillKeepAmt2 < 4))) {
			BestItem3(c, ItemsContained);
		}
	}

	public void BestItem3(Player c, int ItemsContained) {
		int BestValue = 0;
		int NextValue = 0;
		WillKeepItem3 = 0;
		WillKeepItem3Slot = 0;
		for (int ITEM = 0; ITEM < 28; ITEM++) {
			if (playerItems[ITEM] > 0) {
				NextValue = (int) Math.floor(ShopAssistant.getItemShopValue(playerItems[ITEM] - 1));
				if (NextValue > BestValue && !(ITEM == WillKeepItem1Slot && playerItems[ITEM] - 1 == WillKeepItem1)
						&& !(ITEM == WillKeepItem2Slot && playerItems[ITEM] - 1 == WillKeepItem2)) {
					BestValue = NextValue;
					WillKeepItem3 = playerItems[ITEM] - 1;
					WillKeepItem3Slot = ITEM;
					if (playerItemsN[ITEM] > 2 - (WillKeepAmt1 + WillKeepAmt2) && !c.prayerActive[10]) {
						WillKeepAmt3 = 3 - (WillKeepAmt1 + WillKeepAmt2);
					} else if (playerItemsN[ITEM] > 3 - (WillKeepAmt1 + WillKeepAmt2) && c.prayerActive[10]) {
						WillKeepAmt3 = 4 - (WillKeepAmt1 + WillKeepAmt2);
					} else {
						WillKeepAmt3 = playerItemsN[ITEM];
					}
				}
			}
		}
		for (int EQUIP = 0; EQUIP < 14; EQUIP++) {
			if (playerEquipment[EQUIP] > 0) {
				NextValue = (int) Math.floor(ShopAssistant.getItemShopValue(playerEquipment[EQUIP]));
				if (NextValue > BestValue
						&& !(EQUIP + 28 == WillKeepItem1Slot && playerEquipment[EQUIP] == WillKeepItem1)
						&& !(EQUIP + 28 == WillKeepItem2Slot && playerEquipment[EQUIP] == WillKeepItem2)) {
					BestValue = NextValue;
					WillKeepItem3 = playerEquipment[EQUIP];
					WillKeepItem3Slot = EQUIP + 28;
					if (playerEquipmentN[EQUIP] > 2 - (WillKeepAmt1 + WillKeepAmt2) && !c.prayerActive[10]) {
						WillKeepAmt3 = 3 - (WillKeepAmt1 + WillKeepAmt2);
					} else if (playerEquipmentN[EQUIP] > 3 - WillKeepAmt1 && c.prayerActive[10]) {
						WillKeepAmt3 = 4 - (WillKeepAmt1 + WillKeepAmt2);
					} else {
						WillKeepAmt3 = playerEquipmentN[EQUIP];
					}
				}
			}
		}
		if (!isSkulled && ItemsContained > 3 && c.prayerActive[10]
				&& ((WillKeepAmt1 + WillKeepAmt2 + WillKeepAmt3) < 4)) {
			BestItem4(c);
		}
	}

	public void BestItem4(Player c) {
		int BestValue = 0;
		int NextValue = 0;
		WillKeepItem4 = 0;
		WillKeepItem4Slot = 0;
		for (int ITEM = 0; ITEM < 28; ITEM++) {
			if (playerItems[ITEM] > 0) {
				NextValue = (int) Math.floor(ShopAssistant.getItemShopValue(playerItems[ITEM] - 1));
				if (NextValue > BestValue && !(ITEM == WillKeepItem1Slot && playerItems[ITEM] - 1 == WillKeepItem1)
						&& !(ITEM == WillKeepItem2Slot && playerItems[ITEM] - 1 == WillKeepItem2)
						&& !(ITEM == WillKeepItem3Slot && playerItems[ITEM] - 1 == WillKeepItem3)) {
					BestValue = NextValue;
					WillKeepItem4 = playerItems[ITEM] - 1;
					WillKeepItem4Slot = ITEM;
				}
			}
		}
		for (int EQUIP = 0; EQUIP < 14; EQUIP++) {
			if (playerEquipment[EQUIP] > 0) {
				NextValue = (int) Math.floor(ShopAssistant.getItemShopValue(playerEquipment[EQUIP]));
				if (NextValue > BestValue
						&& !(EQUIP + 28 == WillKeepItem1Slot && playerEquipment[EQUIP] == WillKeepItem1)
						&& !(EQUIP + 28 == WillKeepItem2Slot && playerEquipment[EQUIP] == WillKeepItem2)
						&& !(EQUIP + 28 == WillKeepItem3Slot && playerEquipment[EQUIP] == WillKeepItem3)) {
					BestValue = NextValue;
					WillKeepItem4 = playerEquipment[EQUIP];
					WillKeepItem4Slot = EQUIP + 28;
				}
			}
		}
	}

	/**
	 * A method for updating the items a player keeps on death
	 */
	public void updateItemsOnDeath() {
		if (!isSkulled) { // what items to keep
			itemAssistant.keepItem(0, true);
			itemAssistant.keepItem(1, true);
			itemAssistant.keepItem(2, true);
		}
		if (prayerActive[10] && System.currentTimeMillis() - lastProtItem > 700) {
			itemAssistant.keepItem(3, true);
		}
	}

	/**
	 * Determines if the player should keep the item on death
	 * 
	 * @param itemId
	 *            the item to be kept
	 * @return true if the player keeps the item on death, otherwise false
	 */
	public boolean keepsItemOnDeath(int itemId) {
		return WillKeepItem1 == itemId || WillKeepItem2 == itemId || WillKeepItem3 == itemId || WillKeepItem4 == itemId;
	}

	public boolean isAutoButton(int button) {
		for (int j = 0; j < MagicData.autocastIds.length; j += 2) {
			if (MagicData.autocastIds[j] == button)
				return true;
		}
		return false;
	}

	public void assignAutocast(int button) {
		for (int j = 0; j < MagicData.autocastIds.length; j++) {
			if (MagicData.autocastIds[j] == button) {
				Player c = PlayerHandler.players[this.getIndex()];
				autocasting = true;
				autocastId = MagicData.autocastIds[j + 1];
				if (c.autocastingDefensive) {
                    c.getPA().sendFrame36(109, 1);
					c.getPA().sendFrame36(108, 0);
                } else {
                    c.getPA().sendFrame36(108, 1);
					c.getPA().sendFrame36(109, 0);
                }

				c.setSidebarInterface(0, 328);
				break;
			}
		}
	}

	public int getLocalX() {
		return getX() - 8 * getMapRegionX();
	}

	public int getLocalY() {
		return getY() - 8 * getMapRegionY();
	}

	public String getSpellName(int id) {
		switch (id) {
		case 0:
			return "Air Strike";
		case 1:
			return "Water Strike";
		case 2:
			return "Earth Strike";
		case 3:
			return "Fire Strike";
		case 4:
			return "Air Bolt";
		case 5:
			return "Water Bolt";
		case 6:
			return "Earth Bolt";
		case 7:
			return "Fire Bolt";
		case 8:
			return "Air Blast";
		case 9:
			return "Water Blast";
		case 10:
			return "Earth Blast";
		case 11:
			return "Fire Blast";
		case 12:
			return "Air Wave";
		case 13:
			return "Water Wave";
		case 14:
			return "Earth Wave";
		case 15:
			return "Fire Wave";
		case 32:
			return "Shadow Rush";
		case 33:
			return "Smoke Rush";
		case 34:
			return "Blood Rush";
		case 35:
			return "Ice Rush";
		case 36:
			return "Shadow Burst";
		case 37:
			return "Smoke Burst";
		case 38:
			return "Blood Burst";
		case 39:
			return "Ice Burst";
		case 40:
			return "Shadow Blitz";
		case 41:
			return "Smoke Blitz";
		case 42:
			return "Blood Blitz";
		case 43:
			return "Ice Blitz";
		case 44:
			return "Shadow Barrage";
		case 45:
			return "Smoke Barrage";
		case 46:
			return "Blood Barrage";
		case 47:
			return "Ice Barrage";
		default:
			return "Select Spell";
		}
	}

	public boolean fullVoidRange() {
		// return playerEquipment[playerHat] == 11664 && playerEquipment[playerLegs] ==
		// 8840 || playerEquipment[playerLegs] == 13073 && playerEquipment[playerChest]
		// == 8839
		// || playerEquipment[playerChest] == 13072 && playerEquipment[playerHands] ==
		// 8842;

		if (getItems().isWearingItem(11664) && getItems().isWearingItem(8840) && getItems().isWearingItem(8839)
				&& getItems().isWearingItem(8842)) {
			return true;
		}
		if (getItems().isWearingItem(11664) && getItems().isWearingItem(13073) && getItems().isWearingItem(13072)
				&& getItems().isWearingItem(8842)) {
			return true;
		}
		return false;
	}

	public boolean fullVoidMage() {
		// return playerEquipment[playerHat] == 11663 && playerEquipment[playerLegs] ==
		// 8840 || playerEquipment[playerLegs] == 13073 && playerEquipment[playerChest]
		// == 8839
		// || playerEquipment[playerChest] == 13072 && playerEquipment[playerHands] ==
		// 8842;

		if (getItems().isWearingItem(11663) && getItems().isWearingItem(8840) && getItems().isWearingItem(8839)
				&& getItems().isWearingItem(8842)) {
			return true;
		}
		if (getItems().isWearingItem(11663) && getItems().isWearingItem(13073) && getItems().isWearingItem(13072)
				&& getItems().isWearingItem(8842)) {
			return true;
		}
		return false;
	}

	public boolean fullVoidMelee() {
		if (getItems().isWearingItem(11665) && getItems().isWearingItem(8840) && getItems().isWearingItem(8839)
				&& getItems().isWearingItem(8842)) {
			return true;
		}
		if (getItems().isWearingItem(11665) && getItems().isWearingItem(13073) && getItems().isWearingItem(13072)
				&& getItems().isWearingItem(8842)) {
			return true;
		}
		return false;
	}

	/**
	 * SouthWest, NorthEast, SouthWest, NorthEast
	 */
	public boolean inArea(int x, int y, int x1, int y1) {
		if (absX > x && absX < x1 && absY < y && absY > y1) {
			return true;
		}
		return false;
	}

	public boolean Area(final int x1, final int x2, final int y1, final int y2) {
		return (absX >= x1 && absX <= x2 && absY >= y1 && absY <= y2);
	}

	public boolean inBank() {
		return Area(3090, 3099, 3487, 3500) || Area(3089, 3090, 3492, 3498) || Area(3248, 3258, 3413, 3428)
				|| Area(3179, 3191, 3432, 3448) || Area(2944, 2948, 3365, 3374) || Area(2942, 2948, 3367, 3374)
				|| Area(2944, 2950, 3365, 3370) || Area(3008, 3019, 3352, 3359) || Area(3017, 3022, 3352, 3357)
				|| Area(3203, 3213, 3200, 3237) || Area(3212, 3215, 3200, 3235) || Area(3215, 3220, 3202, 3235)
				|| Area(3220, 3227, 3202, 3229) || Area(3227, 3230, 3208, 3226) || Area(3226, 3228, 3230, 3211)
				|| Area(3227, 3229, 3208, 3226) || Area(3025, 3032, 3374, 3384) || Area(3806, 3820, 2840, 2848);
	}
	
	public boolean inOlmRoom() {//checks to see if player is in olm room
		return (getX() > 3200 && getX() < 3260 && getY() > 5710 && getY() < 5770);
	}
	
	public boolean inRaidLobby() {//checks to see if player is in the raid lobby
		if (Boundary.isIn(this, Boundary.RAIDS_LOBBY))  {
			return true;
		}
		return false;
	}
	public boolean isInJail() {
		if (absX >= 2066 && absX <= 2108 && absY >= 4452 && absY <= 4478) {
			return true;
		}
		return false;
	}

	public boolean inClanWars() {
		if (absX > 3272 && absX < 3391 && absY > 4759 && absY < 4863) {
			return true;
		}
		return false;
	}

	public boolean inClanWarsSafe() {
		if (absX > 3263 && absX < 3390 && absY > 4735 && absY < 4761) {
			return true;
		}
		return false;
	}
	public boolean inRaids() {
		return (getX() > 3210 && getX() < 3368 && getY() > 5137 && getY() < 5759);
	}

	public boolean inRaidsMountain() {
		return (getX() > 1219 && getX() < 1259 && getY() > 3542 && getY() < 3577);

	}
	public boolean inWild() {
		if (inClanWars())
			return true;
		if (Boundary.isIn(this, Boundary.LOBBY)) {
			return false;
		}
		if(Boundary.isIn(this, Boundary.EDGEVILLE_PERIMETER) && !Boundary.isIn(this, Boundary.EDGE_BANK) && getHeight() == 8){
			return true;
		}
		if (Boundary.isIn(this, Boundary.SAFEPK))
			return true;
		if (Boundary.isIn(this, Boundary.WILDERNESS_UNDERGROUND))
			return true;
		if (Boundary.isIn(this, Boundary.WILDERNESS_PARAMETERS)) {
			return true;
		}
		return false;
	}

	public boolean inEdgeville() {
		return (absX > 3040 && absX < 3200 && absY > 3460 && absY < 3519);
	}

	public boolean maxRequirements(Player c) {
		int amount = 0;
		for (int i = 0; i <= 21; i++) {
			if (getLevelForXP(c.playerXP[i]) >= 99) {
				amount++;
			}
			if (amount == 22) {
				return true;
			}
		}
		return false;
	}

	public boolean maxedCertain(Player c, int min, int max) {
		int amount = 0;
		int total = min + max;
		for (int i = min; i <= max; i++) {
			if (getLevelForXP(c.playerXP[i]) >= 99) {
				amount++;
			}
			if (amount == total) {
				return true;
			}
		}
		return false;
	}

	public boolean maxedSkiller(Player c) {
		int amount = 0;
		for (int id = 0; id <= 6; id++) {
			if (c.playerLevel[id] <= 1 && id != 3) {
				amount++;
			}
		}
		for (int i = 7; i <= 22; i++) {
			if (c.playerLevel[i] >= 99) {
				amount++;
			}
		}
		if (amount == 22) {
			return true;
		}
		return false;
	}

	public boolean arenas() {
		if (absX > 3331 && absX < 3391 && absY > 3242 && absY < 3260) {
			return true;
		}
		return false;
	}

	public boolean inDuelArena() {
		if ((absX > 3322 && absX < 3394 && absY > 3195 && absY < 3291)
				|| (absX > 3311 && absX < 3323 && absY > 3223 && absY < 3248)) {
			return true;
		}
		return false;
	}
	public boolean inRevs() {
		return (absX > 3143 && absX < 3262 && absY > 10053 && absY < 10231);
	}

	public boolean inMulti() {
		if (Boundary.isIn(this, Zulrah.BOUNDARY) || Boundary.isIn(this, Boundary.CORPOREAL_BEAST_LAIR)
				|| Boundary.isIn(this, Boundary.KRAKEN_CAVE) || Boundary.isIn(this, Boundary.SCORPIA_LAIR)
				|| Boundary.isIn(this, Boundary.CERBERUS_BOSSROOMS) || Boundary.isIn(this, Boundary.INFERNO)
				|| Boundary.isIn(this, Boundary.SKOTIZO_BOSSROOM) || Boundary.isIn(this, Boundary.LIZARDMAN_CANYON)
				|| Boundary.isIn(this, Boundary.BANDIT_CAMP_BOUNDARY) || Boundary.isIn(this, Boundary.COMBAT_DUMMY)
				|| Boundary.isIn(this, Boundary.TEKTON) || Boundary.isIn(this, Boundary.SKELETAL_MYSTICS)
				|| Boundary.isIn(this, Boundary.RAIDS) || Boundary.isIn(this, Boundary.OLM)
				|| Boundary.isIn(this, Boundary.ICE_DEMON) || Boundary.isIn(this, Boundary.INFERNO) || Boundary.isIn(this, Boundary.CATACOMBS) 
				|| Boundary.isIn(this, Boundary.DZ_BOSS)) {
			return true;
		}
		if(inRevs()) {
			return true;
		}
		if (Boundary.isIn(this, Boundary.KALPHITE_QUEEN) && heightLevel == 0) {
			return true;
		}
		if ((absX >= 3136 && absX <= 3327 && absY >= 3519 && absY <= 3607)
				|| (absX >= 3190 && absX <= 3327 && absY >= 3648 && absY <= 3839)
				|| (absX >= 3200 && absX <= 3390 && absY >= 3840 && absY <= 3967)
				|| (absX >= 2992 && absX <= 3007 && absY >= 3912 && absY <= 3967)
				|| (absX >= 2946 && absX <= 2959 && absY >= 3816 && absY <= 3831)
				|| (absX >= 3008 && absX <= 3199 && absY >= 3856 && absY <= 3903)
				|| (absX >= 2824 && absX <= 2944 && absY >= 5258 && absY <= 5369)
				|| (absX >= 3008 && absX <= 3071 && absY >= 3600 && absY <= 3711)
				|| (absX >= 3072 && absX <= 3327 && absY >= 3608 && absY <= 3647)
				|| (absX >= 2624 && absX <= 2690 && absY >= 2550 && absY <= 2619)
				|| (absX >= 2371 && absX <= 2422 && absY >= 5062 && absY <= 5117)
				|| (absX >= 2896 && absX <= 2927 && absY >= 3595 && absY <= 3630)
				|| (absX >= 2892 && absX <= 2932 && absY >= 4435 && absY <= 4464)
				|| (absX >= 2256 && absX <= 2287 && absY >= 4680 && absY <= 4711)
				|| (absX >= 2962 && absX <= 3006 && absY >= 3621 && absY <= 3659)
				|| (absX >= 3155 && absX <= 3214 && absY >= 3755 && absY <= 3803)
				|| (absX >= 1889 && absX <= 1912 && absY >= 4396 && absY <= 4413)
				|| (absX >= 3717 && absX <= 3772 && absY >= 5765 && absY <= 5820)
				|| (absX >= 3341 && absX <= 3378 && absY >= 4760 && absY <= 4853)) {
			return true;
		}
		return false;
	}

	public boolean inGodwars() {
		return Boundary.isIn(this, Godwars.GODWARS_AREA);
	}


	public boolean checkFullGear(Player c) {
		int amount = 0;
		for (int i = 0; i < c.playerEquipment.length; i++) {
			if (c.playerEquipment[0] >= 0) {
				amount++;
			}
			if (amount == c.playerEquipment.length) {
				return true;
			}
		}
		return false;
	}

	public void updateshop(int i) {
		Player p = PlayerHandler.players[getIndex()];
		p.getShops().resetShop(i);
	}

	public void println_debug(String str) {
		System.out.println("[player-" + getIndex() + "][User: " + playerName + "]: " + str);
	}

	public void println(String str) {
		System.out.println("[player-" + getIndex() + "][User: " + playerName + "]: " + str);
	}

	public boolean WithinDistance(int objectX, int objectY, int playerX, int playerY, int distance) {
		for (int i = 0; i <= distance; i++) {
			for (int j = 0; j <= distance; j++) {
				if ((objectX + i) == playerX
						&& ((objectY + j) == playerY || (objectY - j) == playerY || objectY == playerY)) {
					return true;
				} else if ((objectX - i) == playerX
						&& ((objectY + j) == playerY || (objectY - j) == playerY || objectY == playerY)) {
					return true;
				} else if (objectX == playerX
						&& ((objectY + j) == playerY || (objectY - j) == playerY || objectY == playerY)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isWithinDistance() {
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			if (PlayerHandler.players[i] != null) {
				Player other = (Player) PlayerHandler.players[i];

				int deltaX = other.absX - absX, deltaY = other.absY - absY;
				return deltaX <= 15 && deltaX >= -16 && deltaY <= 15 && deltaY >= -16;
			}
		}
		return false;
	}

	public boolean withinDistance(Player otherPlr) {
		if (heightLevel != otherPlr.heightLevel)
			return false;
		int deltaX = otherPlr.absX - absX, deltaY = otherPlr.absY - absY;
		return deltaX <= 15 && deltaX >= -16 && deltaY <= 15 && deltaY >= -16;
	}

	public boolean withinDistance(NPC npc) {
		if (heightLevel != npc.heightLevel)
			return false;
		if (npc.needRespawn == true)
			return false;
		int deltaX = npc.absX - absX, deltaY = npc.absY - absY;
		return deltaX <= 15 && deltaX >= -16 && deltaY <= 15 && deltaY >= -16;
	}

	public boolean withinDistance(int absX, int getY, int getHeightLevel) {
		if (this.getHeightLevel() != getHeightLevel)
			return false;
		int deltaX = this.getX() - absX, deltaY = this.getY() - getY;
		return deltaX <= 15 && deltaX >= -16 && deltaY <= 15 && deltaY >= -16;
	}

	public int getHeightLevel() {
		return getHeightLevel;
	}

	public int distanceToPoint(int pointX, int pointY) {
		return (int) Math.sqrt(Math.pow(absX - pointX, 2) + Math.pow(absY - pointY, 2));
	}

	public int distanceToPoint(int pointX, int pointY, int pointZ) {
		return (int) Math.sqrt(
				Math.pow(absX - pointX, 2) + Math.pow(absY - pointY, 2) + Math.pow(Math.abs(heightLevel) - pointZ, 2));
	}

	public void resetWalkingQueue() {
		wQueueReadPtr = wQueueWritePtr = 0;

		for (int i = 0; i < walkingQueueSize; i++) {
			walkingQueueX[i] = currentX;
			walkingQueueY[i] = currentY;
		}
	}

	public void addToWalkingQueue(int x, int y) {
		// if (VirtualWorld.I(heightLevel, absX, absY, x, y, 0)) {
		int next = (wQueueWritePtr + 1) % walkingQueueSize;
		if (next == wQueueWritePtr)
			return;
		walkingQueueX[wQueueWritePtr] = x;
		walkingQueueY[wQueueWritePtr] = y;
		wQueueWritePtr = next;
		// }
	}

	public boolean goodDistance(int objectX, int objectY, int playerX, int playerY, int distance) {
		return Misc.goodDistance(objectX, objectY, playerX, playerY, distance);
	}
	
	public boolean goodDistance(int objectX, int objectY, int playerX, int playerY, int width, int length) {
		return Misc.goodDistance(objectX, objectY, playerX, playerY, width, length);
	}

	public boolean isWithinDistance(Position other, int dist) {
		int deltaX = other.getX() - x, deltaY = other.getY() - y;
		return deltaX <= dist && deltaX >= -dist && deltaY <= dist && deltaY >= -dist;
	}

	/**
	 * Checks the combat distance to see if the player is in an appropriate location
	 * based on the attack style.
	 * 
	 * @param attacker
	 * @param target
	 * @param followingLogic TODO
	 * @return
	 */
	public boolean checkCombatDistance(Player attacker, Player target, boolean followingLogic) {
		double distance = Misc.distance(attacker.absX, attacker.absY, target.absX, target.absY);
		double required_distance = this.getDistanceRequired(target);
		
		if (!followingLogic) {
			required_distance += this.isRunning && target.isRunning ? 2 : 1;
		}

		return distance <= required_distance && distance != 0;
	}

	public double getDistanceRequired(Player target) {
		if (usingMagic || autocasting) {
			return 10;
		} else if (usingBallista || usingRangeWeapon || usingBow) {
			return 9;
		} else {
			return 1;
		}
	}
	
	public boolean checkNpcAttackDistance(NPC npc) {
		int otherX = npc.getX();
		int otherY = npc.getY();

		double distanceToNpc = npc.getDistance(absX, absY);
		boolean sameSpot = absX == otherX && absY == otherY;
		
		int bowDist = 8;
		int rangedWeaponDist = 4;
		
		if (npc.npcType == 7706) {
			bowDist = 18;
			rangedWeaponDist = 18;
		}

		boolean hallyDistance = distanceToNpc <= 2;
		boolean bowDistance = distanceToNpc <= bowDist;
		boolean rangeWeaponDistance = distanceToNpc <= rangedWeaponDist;

		if (distanceToNpc <= 0) {
			return false;
		}

		if (distanceToNpc == 1.0) {
			return true;
		}
		
		if (playerEquipment[playerWeapon] == 11907
				|| playerEquipment[playerWeapon] == 12899 && bowDistance && !sameSpot) {
			return true;
		}

		if ((usingBow || mageFollow || autocasting) && bowDistance && !sameSpot) {
			return true;
		}

		if (getCombat().usingHally() && hallyDistance && !sameSpot) {
			return true;
		}

		if ((usingBallista || usingRangeWeapon) && rangeWeaponDistance && !sameSpot) {
			return true;
		}
		
		return false;
	}

	public int otherDirection;
	public boolean invincible;

	public boolean isWalkingQueueEmpty() {
	return wQueueReadPtr == wQueueWritePtr || Misc.direction(currentX, currentY, walkingQueueX[wQueueReadPtr], walkingQueueY[wQueueReadPtr]) == -1;
	}
	
	public boolean isWalking() {
		return dir1 == -1;
	}
	
	public int getNextWalkingDirection() {
		if (wQueueReadPtr == wQueueWritePtr)
			return -1;
		int dir;
		do {
			dir = Misc.direction(currentX, currentY, walkingQueueX[wQueueReadPtr], walkingQueueY[wQueueReadPtr]);
			if (dir == -1 && otherDirection != dir) {
				otherDirection = dir;
			}
			if (dir == -1) {
				wQueueReadPtr = (wQueueReadPtr + 1) % walkingQueueSize;
			} else if ((dir & 1) != 0) {
				println_debug("Invalid waypoint in walking queue!");
				resetWalkingQueue();
				return -1;
			}
		} while ((dir == -1) && (wQueueReadPtr != wQueueWritePtr));
		if (dir == -1) {
			return -1;
		}
		dir >>= 1;
		lastX = absX;
		lastY = absY;
		currentX += Misc.directionDeltaX[dir];
		currentY += Misc.directionDeltaY[dir];
		absX += Misc.directionDeltaX[dir];
		absY += Misc.directionDeltaY[dir];
		
		/*  if (isRunning()) {
	     Player c = (Player) this; if (runEnergy > 0) {
		 runEnergy--; 
		 c.getPA().sendFrame126(runEnergy + "%", 149);
		 } else {
		  isRunning2 = false; c.getPA().setConfig(173, 0); 
		  } 
		}*/
		 
		return dir;
	}

	public boolean isRunning() {
		return isNewWalkCmdIsRunning() || (isRunning2 && isMoving);
	}

	private boolean forceRegionReload = false;
	public void reloadMapRegion() {
		teleportToX = absX;
		teleportToY = absY;
		forceRegionReload = true;
	}
	
	public void getNextPlayerMovement() {
		mapRegionDidChange = false;
		didTeleport = false;
		dir1 = dir2 = -1;
		if (teleportToX != -1 && teleportToY != -1) {
			mapRegionDidChange = true;
			if (!forceRegionReload && mapRegionX != -1 && mapRegionY != -1) {
				int relX = teleportToX - mapRegionX * 8, relY = teleportToY - mapRegionY * 8;
				if (relX >= 2 * 8 && relX < 11 * 8 && relY >= 2 * 8 && relY < 11 * 8)
					mapRegionDidChange = false;
			}
			if (mapRegionDidChange) {
				mapRegionX = (teleportToX >> 3) - 6;
				mapRegionY = (teleportToY >> 3) - 6;
			}
			currentX = teleportToX - 8 * mapRegionX;
			currentY = teleportToY - 8 * mapRegionY;
			absX = teleportToX;
			absY = teleportToY;
			lastX = absX;
			lastY = absY - 1;

			resetWalkingQueue();
			teleportToX = teleportToY = -1;
			didTeleport = true;
			postTeleportProcessing();
			forceRegionReload = false;
		} else {
			dir1 = getNextWalkingDirection();
			if (dir1 == -1)
				return;
			if (isRunning) {
				dir2 = getNextWalkingDirection();
				runningDistanceTravelled++;
			}
			int deltaX = 0, deltaY = 0;
			if (currentX < 2 * 8) {
				deltaX = 4 * 8;
				mapRegionX -= 4;
				mapRegionDidChange = true;
			} else if (currentX >= 11 * 8) {
				deltaX = -4 * 8;
				mapRegionX += 4;
				mapRegionDidChange = true;
			}
			if (currentY < 2 * 8) {
				deltaY = 4 * 8;
				mapRegionY -= 4;
				mapRegionDidChange = true;
			} else if (currentY >= 11 * 8) {
				deltaY = -4 * 8;
				mapRegionY += 4;
				mapRegionDidChange = true;
			}

			if (mapRegionDidChange) {
				currentX += deltaX;
				currentY += deltaY;
				for (int i = 0; i < walkingQueueSize; i++) {
					walkingQueueX[i] += deltaX;
					walkingQueueY[i] += deltaY;
				}
			}

		}
		if(firstMove) {
			firstMove = false;
			checkLocationOnLogin();
		}
	}
	public void checkLocationOnLogin() {

		if (Boundary.isIn(this, PestControl.GAME_BOUNDARY)) {
			getPA().movePlayerUnconditionally(2657, 2639, 0);
		}
	
		if (Boundary.isIn(this, Boundary.FIGHT_CAVE)) {
			getPA().movePlayerUnconditionally(2401, 5087, (getIndex() + 1) * 4);
			sendMessage("Wave " + (this.waveId + 1) + " will start in approximately 5-10 seconds. ");
			getFightCave().spawn();
		}
		if (Boundary.isIn(this, Zulrah.BOUNDARY)) {
			getPA().movePlayerUnconditionally(Config.EDGEVILLE_X, Config.EDGEVILLE_Y, 0);
		}
		for(LobbyType lobbyType : LobbyType.values()) {
			LobbyManager.get(lobbyType)
			.ifPresent(lobby -> {
				if(lobby.inLobby(this)) {
					if(lobby.canJoin(this))
						lobby.attemptJoin(this);
					else
						getPA().movePlayerUnconditionally(3033, 6068, 0);//TODO Make this independent for all lobbies
				}
			});
		}
		if (Boundary.isIn(this, Boundary.RAIDS) || Boundary.isIn(this, Boundary.OLM)) {
			RaidConstants.checkLogin(this);
		}
		
		//TODO Add other instance teleporting here
	}

	public void postTeleportProcessing() {
		if (inGodwars()) {
			if (equippedGodItems == null) {
				updateGodItems();
			}
		} else if (equippedGodItems != null) {
			equippedGodItems = null;
			godwars.initialize();
		}
	}
	
	

	public void updateThisPlayerMovement(Stream str) {
		// synchronized(this) {
		if (mapRegionDidChange) {
			str.createFrame(73);
			str.writeWordA(mapRegionX + 6);
			str.writeUnsignedWord(mapRegionY + 6);
		}

		if (didTeleport) {
			str.createFrameVarSizeWord(81);
			str.initBitAccess();
			str.writeBits(1, 1);
			str.writeBits(2, 3);
			str.writeBits(2, heightLevel);
			str.writeBits(1, 1);
			str.writeBits(1, (isUpdateRequired()) ? 1 : 0);
			str.writeBits(7, currentY);
			str.writeBits(7, currentX);
			return;
		}

		if (dir1 == -1) {
			// don't have to update the character position, because we're just
			// standing
			str.createFrameVarSizeWord(81);
			str.initBitAccess();
			isMoving = false;
			if (isUpdateRequired()) {
				// tell client there's an update block appended at the end
				str.writeBits(1, 1);
				str.writeBits(2, 0);
			} else {
				str.writeBits(1, 0);
			}
			if (DirectionCount < 50) {
				DirectionCount++;
			}
		} else {
			DirectionCount = 0;
			str.createFrameVarSizeWord(81);
			str.initBitAccess();
			str.writeBits(1, 1);

			if (dir2 == -1) {
				isMoving = true;
				str.writeBits(2, 1);
				str.writeBits(3, Misc.xlateDirectionToClient[dir1]);
				if (isUpdateRequired())
					str.writeBits(1, 1);
				else
					str.writeBits(1, 0);
			} else {
				isMoving = true;
				str.writeBits(2, 2);
				str.writeBits(3, Misc.xlateDirectionToClient[dir1]);
				str.writeBits(3, Misc.xlateDirectionToClient[dir2]);
				if (isUpdateRequired())
					str.writeBits(1, 1);
				else
					str.writeBits(1, 0);
			}
		}

	}

	public void updatePlayerMovement(Stream str) {
		// synchronized(this) {
		if (dir1 == -1) {
			if (isUpdateRequired() || isChatTextUpdateRequired()) {

				str.writeBits(1, 1);
				str.writeBits(2, 0);
			} else
				str.writeBits(1, 0);
		} else if (dir2 == -1) {

			str.writeBits(1, 1);
			str.writeBits(2, 1);
			str.writeBits(3, Misc.xlateDirectionToClient[dir1]);
			str.writeBits(1, (isUpdateRequired() || isChatTextUpdateRequired()) ? 1 : 0);
		} else {

			str.writeBits(1, 1);
			str.writeBits(2, 2);
			str.writeBits(3, Misc.xlateDirectionToClient[dir1]);
			str.writeBits(3, Misc.xlateDirectionToClient[dir2]);
			str.writeBits(1, (isUpdateRequired() || isChatTextUpdateRequired()) ? 1 : 0);
		}

	}

	public void addNewNPC(NPC npc, Stream str, Stream updateBlock) {
		// synchronized(this) {
		int id = npc.getIndex();
		npcInListBitmap[id >> 3] |= 1 << (id & 7);
		npcList[npcListSize++] = npc;

		str.writeBits(14, id);

		int z = npc.absY - absY;
		if (z < 0)
			z += 32;
		str.writeBits(5, z);
		z = npc.absX - absX;
		if (z < 0)
			z += 32;
		str.writeBits(5, z);

		str.writeBits(1, 0);
		str.writeBits(14, npc.npcType);

		boolean savedUpdateRequired = npc.updateRequired;
		npc.updateRequired = true;
		npc.appendNPCUpdateBlock(updateBlock);
		npc.updateRequired = savedUpdateRequired;
		str.writeBits(1, 1);
	}

	public void addNewPlayer(Player plr, Stream str, Stream updateBlock) {
		if (playerListSize >= 79) {
			return;
		}
		int id = plr.getIndex();
		playerInListBitmap[id >> 3] |= 1 << (id & 7);
		playerList[playerListSize++] = plr;
		str.writeBits(11, id);
		str.writeBits(1, 1);
		boolean savedFlag = plr.isAppearanceUpdateRequired();
		boolean savedUpdateRequired = plr.isUpdateRequired();
		plr.setAppearanceUpdateRequired(true);
		plr.setUpdateRequired(true);
		plr.appendPlayerUpdateBlock(updateBlock);
		plr.setAppearanceUpdateRequired(savedFlag);
		plr.setUpdateRequired(savedUpdateRequired);
		str.writeBits(1, 1);
		int z = plr.absY - absY;
		if (z < 0)
			z += 32;
		str.writeBits(5, z);
		z = plr.absX - absX;
		if (z < 0)
			z += 32;
		str.writeBits(5, z);
	}

	protected void appendPlayerAppearance(Stream str) {
		playerProps.currentOffset = 0;
		playerProps.writeByte(playerAppearance[0]);
		StringBuilder sb = new StringBuilder(titles.getCurrentTitle());
		if (titles.getCurrentTitle().equalsIgnoreCase("None")) {
			sb.delete(0, sb.length());
		}
		playerProps.writeString(sb.toString());
		sb = new StringBuilder(rights.getPrimary().getColor());
		if (titles.getCurrentTitle().equalsIgnoreCase("None")) {
			sb.delete(0, sb.length());
		}
		playerProps.writeString(sb.toString());
		playerProps.writeByte(getHealth().getStatus().getMask());
		playerProps.writeByte(headIcon);
		playerProps.writeByte(headIconPk);
		if (isNpc == false) {
			if (playerEquipment[playerHat] > 1) {
				playerProps.writeUnsignedWord(0x200 + playerEquipment[playerHat]);
			} else {
				playerProps.writeByte(0);
			}

			if (playerEquipment[playerCape] > 1) {
				playerProps.writeUnsignedWord(0x200 + playerEquipment[playerCape]);
			} else {
				playerProps.writeByte(0);
			}

			if (playerEquipment[playerAmulet] > 1) {
				playerProps.writeUnsignedWord(0x200 + playerEquipment[playerAmulet]);
			} else {
				playerProps.writeByte(0);
			}

			if (playerEquipment[playerWeapon] > 1) {
				playerProps.writeUnsignedWord(0x200 + playerEquipment[playerWeapon]);
			} else {
				playerProps.writeByte(0);
			}

			if (playerEquipment[playerChest] > 1) {
				playerProps.writeUnsignedWord(0x200 + playerEquipment[playerChest]);
			} else {
				playerProps.writeUnsignedWord(0x100 + playerAppearance[2]);
			}

			if (playerEquipment[playerShield] > 1) {
				playerProps.writeUnsignedWord(0x200 + playerEquipment[playerShield]);
			} else {
				playerProps.writeByte(0);
			}

			if (!Item.isFullBody(playerEquipment[playerChest])) {
				playerProps.writeUnsignedWord(0x100 + playerAppearance[3]);
			} else {
				playerProps.writeByte(0);
			}

			if (playerEquipment[playerLegs] > 1) {
				playerProps.writeUnsignedWord(0x200 + playerEquipment[playerLegs]);
			} else {
				playerProps.writeUnsignedWord(0x100 + playerAppearance[5]);
			}

			if (!Item.isFullHat(playerEquipment[playerHat]) && !Item.isFullMask(playerEquipment[playerHat])) {
				playerProps.writeUnsignedWord(0x100 + playerAppearance[1]);
			} else {
				playerProps.writeByte(0);
			}

			if (playerEquipment[playerHands] > 1) {
				playerProps.writeUnsignedWord(0x200 + playerEquipment[playerHands]);
			} else {
				playerProps.writeUnsignedWord(0x100 + playerAppearance[4]);
			}

			if (playerEquipment[playerFeet] > 1) {
				playerProps.writeUnsignedWord(0x200 + playerEquipment[playerFeet]);
			} else {
				playerProps.writeUnsignedWord(0x100 + playerAppearance[6]);
			}

			if (playerAppearance[0] != 1 && !Item.isFullMask(playerEquipment[playerHat])) {
				playerProps.writeUnsignedWord(0x100 + playerAppearance[7]);
			} else {
				playerProps.writeByte(0);
			}
		} else {
			playerProps.writeUnsignedWord(-1);
			playerProps.writeUnsignedWord(npcId2);
		}
		playerProps.writeByte(playerAppearance[8]);
		playerProps.writeByte(playerAppearance[9]);
		playerProps.writeByte(playerAppearance[10]);
		playerProps.writeByte(playerAppearance[11]);
		playerProps.writeByte(playerAppearance[12]);
		playerProps.writeUnsignedWord(playerStandIndex); // standAnimIndex
		playerProps.writeUnsignedWord(playerTurnIndex); // standTurnAnimIndex
		playerProps.writeUnsignedWord(playerWalkIndex); // walkAnimIndex
		playerProps.writeUnsignedWord(playerTurn180Index); // turn180AnimIndex
		playerProps.writeUnsignedWord(playerTurn90CWIndex); // turn90CWAnimIndex
		playerProps.writeUnsignedWord(playerTurn90CCWIndex); // turn90CCWAnimIndex
		playerProps.writeUnsignedWord(playerRunIndex); // runAnimIndex
		playerProps.writeQWord(Misc.playerNameToInt64(playerName));
		playerProps.writeByte(invisible ? 1 : 0);
		combatLevel = calculateCombatLevel();
		playerProps.writeByte(combatLevel); // combat level

		Set<Right> rightsSet = rights.getSet();
		playerProps.writeByte(rightsSet.size());
		for (Right right : rightsSet) {
			playerProps.writeByte(right.ordinal());
		}

		playerProps.writeUnsignedWord(0);
		str.writeByteC(playerProps.currentOffset);
		str.writeBytes(playerProps.buffer, playerProps.currentOffset, 0);
	}

	public int calculateCombatLevel() {
		int j = getLevelForXP(playerXP[playerAttack]);
		int k = getLevelForXP(playerXP[playerDefence]);
		int l = getLevelForXP(playerXP[playerStrength]);
		int i1 = getLevelForXP(playerXP[playerHitpoints]);
		int j1 = getLevelForXP(playerXP[playerPrayer]);
		int k1 = getLevelForXP(playerXP[playerRanged]);
		int l1 = getLevelForXP(playerXP[playerMagic]);
		int combatLevel = (int) (((k + i1) + Math.floor(j1 / 2)) * 0.24798D) + 1;
		double d = (j + l) * 0.32500000000000001D;
		double d1 = Math.floor(k1 * 1.5D) * 0.32500000000000001D;
		double d2 = Math.floor(l1 * 1.5D) * 0.32500000000000001D;
		if (d >= d1 && d >= d2) {
			combatLevel += d;
		} else if (d1 >= d && d1 >= d2) {
			combatLevel += d1;
		} else if (d2 >= d && d2 >= d1) {
			combatLevel += d2;
		}
		return combatLevel;
	}

	public int getLevelForXP(int exp) {
		int points = 0;
		int output = 0;

		for (int lvl = 1; lvl <= 99; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if (output >= exp)
				return lvl;
		}
		return 99;
	}

	protected void appendPlayerChatText(Stream str) {
		str.writeWordBigEndian(((getChatTextColor() & 0xFF) << 8) + (getChatTextEffects() & 0xFF));
		str.writeByte(rights.getPrimary().getValue());
		str.writeByteC(getChatTextSize());
		str.writeBytes_reverse(getChatText(), getChatTextSize(), 0);

	}

	public void forcedChat(String text) {
		forcedText = text;
		forcedChatUpdateRequired = true;
		setUpdateRequired(true);
		setAppearanceUpdateRequired(true);
	}

	public void appendForcedChat(Stream str) {
		// synchronized(this) {
		str.writeString(forcedText);
	}

	public void appendMask100Update(Stream str) {
		// synchronized(this) {
		str.writeWordBigEndian(mask100var1);
		str.writeDWord(mask100var2);

	}

	public void gfx(int gfx, int height) {
		mask100var1 = gfx;
		mask100var2 = 65536 * height;
		graphicMaskUpdate0x100 = true;
		setUpdateRequired(true);
	}

	public void gfx100(int gfx) {
		mask100var1 = gfx;
		mask100var2 = 6553600;
		graphicMaskUpdate0x100 = true;
		setUpdateRequired(true);
	}

	public void gfx0(int gfx) {
		mask100var1 = gfx;
		mask100var2 = 65536;
		graphicMaskUpdate0x100 = true;
		setUpdateRequired(true);
	}

	public boolean wearing2h() {
		Player c = this;
		String s = ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]);
		if (s.contains("2h"))
			return true;
		if (s.contains("bulwark") || s.contains("elder maul"))
			return true;
		if (s.contains("godsword"))
			return true;

		return false;
	}

	/**
	 * Animations
	 **/
	public void startAnimation(int animId) {
		startAnimation(new Animation(animId));
	}

	public void startAnimation(int animId, int time) {
		startAnimation(new Animation(animId, time));
	}

	public void stopAnimation() {
		startAnimation(new Animation(65535));
	}

	public void appendAnimationRequest(Stream str) {
		str.writeWordBigEndian((getAnimation() == null || getAnimation().getId() == -1) ? 65535 : getAnimation().getId());
		str.writeByteC(getAnimation().getDelay());
	}

	public void faceUpdate(int index) {
		face = index;
		faceUpdateRequired = true;
		setUpdateRequired(true);
	}

	public void appendFaceUpdate(Stream str) {
		// synchronized(this) {
		str.writeWordBigEndian(face);

	}

	public void turnPlayerTo(int pointX, int pointY) {
		FocusPointX = 2 * pointX + 1;
		FocusPointY = 2 * pointY + 1;
		setUpdateRequired(true);
	}

	private void appendSetFocusDestination(Stream str) {
		// synchronized(this) {
		str.writeWordBigEndianA(FocusPointX);
		str.writeWordBigEndian(FocusPointY);

	}

	@Override
	public void appendDamage(int damage, Hitmark h) {
		lastAttacked = System.currentTimeMillis();
		if (damage < 0) {
			damage = 0;
			h = Hitmark.MISS;
		}
		if (getHealth().getCurrentHealth() - damage < 0) {
			damage = getHealth().getCurrentHealth();
		}
		if (teleTimer <= 0) {
			if (!invincible)
				getHealth().reduce(damage);
			if (!hitUpdateRequired) {
				hitUpdateRequired = true;
				hitDiff = damage;
				hitmark1 = h;
			} else if (!hitUpdateRequired2) {
				hitUpdateRequired2 = true;
				hitDiff2 = damage;
				hitmark2 = h;
			}
		} else {
			if (hitUpdateRequired) {
				hitUpdateRequired = false;
			}
			if (hitUpdateRequired2) {
				hitUpdateRequired2 = false;
			}
		}
		setUpdateRequired(true);
	}

	@Override
	protected void appendHitUpdate(Stream str) {
		str.writeByte(hitDiff);
		if (hitmark1 == null) {
			str.writeByteA(0);
		} else {
			str.writeByteA(hitmark1.getId());
		}
		if (getHealth().getCurrentHealth() <= 0) {
			isDead = true;
		}
		str.writeByteC(getHealth().getCurrentHealth());
		str.writeByte(getHealth().getMaximumHealth());
	}

	@Override
	protected void appendHitUpdate2(Stream str) {
		str.writeByte(hitDiff2);
		if (hitmark2 == null) {
			str.writeByteS(0);
		} else {
			str.writeByteS(hitmark2.getId());
		}
		if (getHealth().getCurrentHealth() <= 0) {
			isDead = true;
		}
		str.writeByte(getHealth().getCurrentHealth());
		str.writeByteC(getHealth().getMaximumHealth());
	}

	/**
	 * Direction, 2 = South, 0 = North, 3 = West, 2 = East?
	 * 
	 * @param xOffset
	 * @param yOffset
	 * @param speed1
	 * @param speed2
	 * @param direction
	 * @param emote
	 */
	private int xOffsetWalk, yOffsetWalk;
	public int dropSize = 0;
	public boolean canUpdateHighscores = true;
	public boolean zukDead = false;
	public boolean sellingX;
	public boolean firstBankLogin = true;
	public int currentPrestigeLevel, prestigeNumber;
	public boolean canPrestige = false;
	public int prestigePoints;
	public boolean newStarter = false;
	public int slayerTask;
	public int taskAmount;
	public boolean spawnedbarrows = false;
	public boolean absorption;
	public boolean insurance = false;
	public boolean announce = true;
	public boolean lootPickUp = false;
	public boolean xpScroll;
	public long xpScrollTicks;
	public boolean augury;
	public boolean rigour;
	public int i;
	public boolean usedFc;
	public boolean hasOpenedChest;
	public boolean easyDifficulty;
	public boolean normalDifficulty;
	public boolean hardDifficulty;
	public int change;
	public int startDate = -1;
	public int LastLoginYear = 0;
    public int LastLoginMonth = 0;
    public int LastLoginDate = 0;
    public int LoginStreak = 1;
	public boolean inTOB;

	public boolean killedMaiden;
	public boolean killedBloat;
	public boolean killedNylocas;
	public boolean killedSotetseg;
	public boolean killedXarpus;
	public boolean killedVerzik;
	public boolean changedmac = false;
	
	
	public int christmasP;
	public boolean christmasEvent = false;
	
	/*
	 * diary completion
	 */
	public boolean d1Complete = false;
	public boolean d2Complete = false;
	public boolean d3Complete = false;
	public boolean d4Complete = false;
	public boolean d5Complete = false;
	public boolean d6Complete = false;
	public boolean d7Complete = false;
	public boolean d8Complete = false;
	public boolean d9Complete = false;
	public boolean d10Complete = false;
	public boolean d11Complete = false;
	
	/**
	 * 0 North 1 East 2 South 3 West
	 */
	public void setForceMovement(int xOffset, int yOffset, int speedOne, int speedTwo, String directionSet,
			int animation) {
		if (isForceMovementActive() || forceMovement) {
			return;
		}
		stopMovement();
		xOffsetWalk = xOffset - absX;
		yOffsetWalk = yOffset - absY;
		playerStandIndex = animation;
		playerRunIndex = animation;
		playerWalkIndex = animation;
		forceMovementActive = true;
		getPA().requestUpdates();
		setAppearanceUpdateRequired(true);
		Server.getEventHandler().submit(new Event<Player>("force_movement", this, 2) {

			@Override
			public void execute() {
				if (attachment == null || attachment.disconnected) {
					super.stop();
					return;
				}
				attachment.setUpdateRequired(true);
				attachment.forceMovement = true;
				attachment.x1 = currentX;
				attachment.y1 = currentY;
				attachment.x2 = currentX + xOffsetWalk;
				attachment.y2 = currentY + yOffsetWalk;
				attachment.speed1 = speedOne;
				attachment.speed2 = speedTwo;
				attachment.direction = directionSet == "NORTH" ? 0
						: directionSet == "EAST" ? 1 : directionSet == "SOUTH" ? 2 : directionSet == "WEST" ? 3 : 0;
				super.stop();
			}
		});
		Server.getEventHandler()
				.submit(new Event<Player>("force_movement", this, Math.abs(xOffsetWalk) + Math.abs(yOffsetWalk)) {

					@Override
					public void execute() {
						if (attachment == null || attachment.disconnected) {
							super.stop();
							return;
						}
						forceMovementActive = false;
						attachment.getPA().movePlayer(xOffset, yOffset, attachment.heightLevel);
						if (attachment.playerEquipment[attachment.playerWeapon] == -1) {
							attachment.playerStandIndex = 0x328;
							attachment.playerTurnIndex = 0x337;
							attachment.playerWalkIndex = 0x333;
							attachment.playerTurn180Index = 0x334;
							attachment.playerTurn90CWIndex = 0x335;
							attachment.playerTurn90CCWIndex = 0x336;
							attachment.playerRunIndex = 0x338;
						} else {
							attachment.getCombat().getPlayerAnimIndex(Item
									.getItemName(attachment.playerEquipment[attachment.playerWeapon]).toLowerCase());
						}
						forceMovement = false;
						super.stop();
					}
				});
	}

	public void appendMask400Update(Stream str) {
		str.writeByteS(x1);
		str.writeByteS(y1);
		str.writeByteS(x2);
		str.writeByteS(y2);
		str.writeWordBigEndianA(speed1);
		str.writeWordA(speed2);
		str.writeByteS(direction);
	}

	public void appendPlayerUpdateBlock(Stream str) {
		if (!isUpdateRequired() && !isChatTextUpdateRequired())
			return;
		int updateMask = 0;

		if (forceMovement) {
			updateMask |= 0x400;
		}

		if (graphicMaskUpdate0x100) {
			updateMask |= 0x100;
		}

		if (isAnimationUpdateRequired()) {
			updateMask |= 8;
		}

		if (forcedChatUpdateRequired) {
			updateMask |= 4;
		}

		if (isChatTextUpdateRequired()) {
			updateMask |= 0x80;
		}

		if (isAppearanceUpdateRequired()) {
			updateMask |= 0x10;
		}

		if (faceUpdateRequired) {
			updateMask |= 1;
		}

		if (FocusPointX != -1) {
			updateMask |= 2;
		}

		if (hitUpdateRequired) {
			updateMask |= 0x20;
		}

		if (hitUpdateRequired2) {
			updateMask |= 0x200;
		}

		if (updateMask >= 0x100) {
			updateMask |= 0x40;
			str.writeByte(updateMask & 0xFF);
			str.writeByte(updateMask >> 8);
		} else {
			str.writeByte(updateMask);
		}

		if (forceMovement) {
			appendMask400Update(str);
		}

		if (graphicMaskUpdate0x100) {
			appendMask100Update(str);
		}

		if (isAnimationUpdateRequired()) {
			appendAnimationRequest(str);
		}

		if (forcedChatUpdateRequired) {
			appendForcedChat(str);
		}

		if (isChatTextUpdateRequired()) {
			appendPlayerChatText(str);
		}

		if (faceUpdateRequired) {
			appendFaceUpdate(str);
		}

		if (isAppearanceUpdateRequired()) {
			appendPlayerAppearance(str);
		}

		if (FocusPointX != -1) {
			appendSetFocusDestination(str);
		}

		if (hitUpdateRequired) {
			appendHitUpdate(str);
		}

		if (hitUpdateRequired2) {
			appendHitUpdate2(str);
		}

	}

	public void clearUpdateFlags() {
		setUpdateRequired(false);
		setChatTextUpdateRequired(false);
		setAppearanceUpdateRequired(false);
		hitUpdateRequired = false;
		hitUpdateRequired2 = false;
		forcedChatUpdateRequired = false;
		graphicMaskUpdate0x100 = false;
		FocusPointX = -1;
		FocusPointY = -1;
		faceUpdateRequired = false;
		forceMovement = false;
		face = 65535;
		resetAfterUpdate();
	}

	public void stopMovement() {
		if (teleportToX <= 0 && teleportToY <= 0) {
			teleportToX = absX;
			teleportToY = absY;
		}
		newWalkCmdSteps = 0;
		getNewWalkCmdX()[0] = getNewWalkCmdY()[0] = travelBackX[0] = travelBackY[0] = 0;
		getNextPlayerMovement();
	}

	public void preProcessing() {
		newWalkCmdSteps = 0;
	}

	public int setPacketsReceived(int packetsReceived) {
		return packetsReceived;
	}

	public int getPacketsReceived() {
		return packetsReceived;
	}

	public void postProcessing() {
		if (newWalkCmdSteps > 0) {
			int firstX = getNewWalkCmdX()[0], firstY = getNewWalkCmdY()[0];

			int lastDir = 0;
			boolean found = false;
			numTravelBackSteps = 0;
			int ptr = wQueueReadPtr;
			int dir = Misc.direction(currentX, currentY, firstX, firstY);
			if (dir != -1 && (dir & 1) != 0) {
				do {
					lastDir = dir;
					if (--ptr < 0)
						ptr = walkingQueueSize - 1;

					travelBackX[numTravelBackSteps] = walkingQueueX[ptr];
					travelBackY[numTravelBackSteps++] = walkingQueueY[ptr];
					dir = Misc.direction(walkingQueueX[ptr], walkingQueueY[ptr], firstX, firstY);
					if (lastDir != dir) {
						found = true;
						break;
					}

				} while (ptr != wQueueWritePtr);
			} else
				found = true;

			if (!found)
				println_debug("Fatal: couldn't find connection vertex! Dropping packet.");
			else {
				wQueueWritePtr = wQueueReadPtr;

				addToWalkingQueue(currentX, currentY);

				if (dir != -1 && (dir & 1) != 0) {

					for (int i = 0; i < numTravelBackSteps - 1; i++) {
						addToWalkingQueue(travelBackX[i], travelBackY[i]);
					}
					int wayPointX2 = travelBackX[numTravelBackSteps - 1],
							wayPointY2 = travelBackY[numTravelBackSteps - 1];
					int wayPointX1, wayPointY1;
					if (numTravelBackSteps == 1) {
						wayPointX1 = currentX;
						wayPointY1 = currentY;
					} else {
						wayPointX1 = travelBackX[numTravelBackSteps - 2];
						wayPointY1 = travelBackY[numTravelBackSteps - 2];
					}

					dir = Misc.direction(wayPointX1, wayPointY1, wayPointX2, wayPointY2);
					if (dir == -1 || (dir & 1) != 0) {
						println_debug("Fatal: The walking queue is corrupt! wp1=(" + wayPointX1 + ", " + wayPointY1
								+ "), " + "wp2=(" + wayPointX2 + ", " + wayPointY2 + ")");
					} else {
						dir >>= 1;
						found = false;
						int x = wayPointX1, y = wayPointY1;
						while (x != wayPointX2 || y != wayPointY2) {
							x += Misc.directionDeltaX[dir];
							y += Misc.directionDeltaY[dir];
							if ((Misc.direction(x, y, firstX, firstY) & 1) == 0) {
								found = true;
								break;
							}
						}
						if (!found) {
							println_debug("Fatal: Internal error: unable to determine connection vertex!" + "  wp1=("
									+ wayPointX1 + ", " + wayPointY1 + "), wp2=(" + wayPointX2 + ", " + wayPointY2
									+ "), " + "first=(" + firstX + ", " + firstY + ")");
						} else
							addToWalkingQueue(wayPointX1, wayPointY1);
					}
				} else {
					for (int i = 0; i < numTravelBackSteps; i++) {
						addToWalkingQueue(travelBackX[i], travelBackY[i]);
					}
				}

				for (int i = 0; i < newWalkCmdSteps; i++) {
					addToWalkingQueue(getNewWalkCmdX()[i], getNewWalkCmdY()[i]);
				}

			}

			isRunning = isNewWalkCmdIsRunning() || isRunning2;
		}
	}

	public int getMapRegionX() {
		return mapRegionX;
	}

	public int getMapRegionY() {
		return mapRegionY;
	}

	public int getX() {
		return absX;
	}

	public int getY() {
		return absY;
	}

	public Coordinate getCoordinate() {
		return new Coordinate(absX, absY, heightLevel);
	}

	public boolean inPcBoat() {
		return absX >= 2660 && absX <= 2663 && absY >= 2638 && absY <= 2643;
	}

	public boolean inPcGame() {
		return absX >= 2624 && absX <= 2690 && absY >= 2550 && absY <= 2619;
	}

	public void setHitDiff(int hitDiff) {
		this.hitDiff = hitDiff;
	}

	public void setHitDiff2(int hitDiff2) {
		this.hitDiff2 = hitDiff2;
	}

	public int getHitDiff() {
		return hitDiff;
	}

	public void setAppearanceUpdateRequired(boolean appearanceUpdateRequired) {
		this.appearanceUpdateRequired = appearanceUpdateRequired;
	}

	public boolean isAppearanceUpdateRequired() {
		return appearanceUpdateRequired;
	}

	public void setChatTextEffects(int chatTextEffects) {
		this.chatTextEffects = chatTextEffects;
	}

	public int getChatTextEffects() {
		return chatTextEffects;
	}

	public void setChatTextSize(byte chatTextSize) {
		this.chatTextSize = chatTextSize;
	}

	public byte getChatTextSize() {
		return chatTextSize;
	}

	public void setChatTextUpdateRequired(boolean chatTextUpdateRequired) {
		this.chatTextUpdateRequired = chatTextUpdateRequired;
	}

	public boolean isChatTextUpdateRequired() {
		return chatTextUpdateRequired;
	}

	public void setChatText(byte chatText[]) {
		this.chatText = chatText;
	}

	public byte[] getChatText() {
		return chatText;
	}

	public void setChatTextColor(int chatTextColor) {
		this.chatTextColor = chatTextColor;
	}

	public int getChatTextColor() {
		return chatTextColor;
	}

	public void setNewWalkCmdX(int newWalkCmdX[]) {
		this.newWalkCmdX = newWalkCmdX;
	}

	public int[] getNewWalkCmdX() {
		return newWalkCmdX;
	}

	public void setNewWalkCmdY(int newWalkCmdY[]) {
		this.newWalkCmdY = newWalkCmdY;
	}

	public int[] getNewWalkCmdY() {
		return newWalkCmdY;
	}

	public void setNewWalkCmdIsRunning(boolean newWalkCmdIsRunning) {
		this.newWalkCmdIsRunning = newWalkCmdIsRunning;
	}

	public boolean isNewWalkCmdIsRunning() {
		return newWalkCmdIsRunning;
	}

	public boolean getRingOfLifeEffect() {
		return maxCape[0];
	}

	public boolean setRingOfLifeEffect(boolean effect) {
		return maxCape[0] = effect;
	}

	public boolean getFishingEffect() {
		return maxCape[1];
	}

	public boolean setFishingEffect(boolean effect) {
		return maxCape[1] = effect;
	}

	public boolean getMiningEffect() {
		return maxCape[2];
	}

	public boolean setMiningEffect(boolean effect) {
		return maxCape[2] = effect;
	}

	public boolean getWoodcuttingEffect() {
		return maxCape[3];
	}

	public boolean setWoodcuttingEffect(boolean effect) {
		return maxCape[3] = effect;
	}
	public int getSkeletalMysticDamageCounter() {
		return raidsDamageCounters[0];
	}

	public void setSkeletalMysticDamageCounter(int damage) {
		this.raidsDamageCounters[0] = damage;
	}

	public int getTektonDamageCounter() {
		return raidsDamageCounters[1];
	}

	public void setTektonDamageCounter(int damage) {
		this.raidsDamageCounters[1] = damage;
	}

	public int getIceDemonDamageCounter() {
		return raidsDamageCounters[2];
	}

	public void setIceDemonDamageCounter(int damage) {
		this.raidsDamageCounters[2] = damage;
	}


	public int getGlodDamageCounter() {
		return raidsDamageCounters[9];
	}

	public void setGlodDamageCounter(int damage) {
		this.raidsDamageCounters[9] = damage;
	}
	
	public int getDgDamageCounter() {
		return raidsDamageCounters[10];
	}
	
	public void setDgDamageCounter(int damage) {
		this.raidsDamageCounters[10] = damage;
	}
	
	public int getMuttadileDamageCounter() {
		return raidsDamageCounters[11];
	}

	public void setMuttadileDamageCounter(int damage) {
		this.raidsDamageCounters[11] = damage;
	}

	public int getSerenDamageCounter() {
		return raidsDamageCounters[3];
	}

	public void setSerenDamageCounter(int damage) {
		this.raidsDamageCounters[3] = damage;
	}
	public int getIceQueenDamageCounter() {
		return raidsDamageCounters[4];
	}

	public void setIceQueenDamageCounter(int damage) {
		this.raidsDamageCounters[4] = damage;
	}

	public static int relicUpgrade;

	public void assignClueCounter(RewardLevel rewardLevel) {
		switch (rewardLevel) {
		case EASY:
			counters[0]++;
		case MEDIUM:
			counters[1]++;
		case HARD:
			counters[2]++;
		case MASTER:
			counters[3]++;
		default:
			break;
		}
	}

	public int getClueCounter(RewardLevel rewardLevel) {
		switch (rewardLevel) {
		case EASY:
			return counters[0];
		case MEDIUM:
			return counters[1];
		case HARD:
			return counters[2];
		case MASTER:
			return counters[3];
		default:
			return 0;
		}
	}

	public int getEasyClueCounter() {
		return counters[0];
	}

	public void setEasyClueCounter(int counters) {
		this.counters[0] = counters;
	}

	public int getMediumClueCounter() {
		return counters[1];
	}

	public void setMediumClueCounter(int counters) {
		this.counters[1] = counters;
	}

	public int getHardClueCounter() {
		return counters[2];
	}

	public void setHardClueCounter(int counters) {
		this.counters[2] = counters;
	}

	public int getMasterClueCounter() {
		return counters[3];
	}

	public void setMasterClueCounter(int counters) {
		this.counters[3] = counters;
	}

	public int getBarrowsChestCounter() {
		return counters[4];
	}

	public void setBarrowsChestCounter(int counters) {
		this.counters[4] = counters;
	}

	public int getDuelWinsCounter() {
		return counters[5];
	}

	public void setDuelWinsCounter(int counters) {
		this.counters[5] = counters;
	}

	public int getDuelLossCounter() {
		return counters[6];
	}

	public void setDuelLossCounter(int counters) {
		this.counters[6] = counters;
	}

	public int getHalloweenOrderCount() {
		return counters[7];
	}

	public void setHalloweenOrderCount(int counters) {
		this.counters[7] = counters;
	}

	public boolean samePlayer() {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (j == getIndex())
				continue;
			if (PlayerHandler.players[j] != null) {
				if (PlayerHandler.players[j].playerName.equalsIgnoreCase(playerName)) {
					disconnected = true;
					return true;
				}
			}
		}
		return false;
	}

	public void putInCombat(int attacker) {
		underAttackByPlayer = attacker;
		logoutDelay = System.currentTimeMillis();
		singleCombatDelay = System.currentTimeMillis();
	}

	public String getLastClanChat() {
		return lastClanChat;
	}
	public void setLastClanChat(String founder) {
		lastClanChat = founder;
	}
	public long getNameAsLong() {
		return nameAsLong;
	}

	public void setNameAsLong(long hash) {
		this.nameAsLong = hash;
	}

	public boolean isStopPlayer() {
		return stopPlayer;
	}

	public void setStopPlayer(boolean stopPlayer) {
		this.stopPlayer = stopPlayer;
	}

	public int getFace() {
		return this.getIndex() + '\u8000';
	}

	public int getLockIndex() {
		return -this.getIndex() - 1;
	}

	public int getHeight() {
		return this.heightLevel;
	}

	public boolean isDead() {
		return getHealth().getCurrentHealth() <= 0 || this.isDead;
	}

	public void healPlayer(int heal) {
		getHealth().increase(heal);
	}

	int maxLevel() {
		return 99;
	}

	public void sendGraphic(int id, int height) {
		if (height == 0) {
			this.gfx0(id);
		}

		if (height == 100) {
			this.gfx100(id);
		}

	}

	public boolean protectingRange() {
		return this.prayerActive[17];
	}

	public boolean protectingMagic() {
		return this.prayerActive[16];
	}

	public boolean protectingMelee() {
		return this.prayerActive[18];
	}

	public void setTrading(boolean trading) {
		this.trading = trading;
	}

	public boolean isTrading() {
		return this.trading;
	}

	public boolean isInvisible() {
		return invisible;
	}

	public void setInvisible(boolean invisible) {
		this.invisible = invisible;
	}

	public boolean inGodmode() {
		return godmode;
	}

	public void setGodmode(boolean godmode) {
		this.godmode = godmode;
	}

	public boolean inSafemode() {
		return safemode;
	}

	public void setSafemode(boolean safemode) {
		this.safemode = safemode;
	}

	public TeleportHandler getTeleport() {
		return teleportHandler;
	}

	public void setDragonfireShieldCharge(int charge) {
		this.dragonfireShieldCharge = charge;
	}

	public int getDragonfireShieldCharge() {
		return dragonfireShieldCharge;
	}

	public void setLastDragonfireShieldAttack(long lastAttack) {
		this.lastDragonfireShieldAttack = lastAttack;
	}

	public long getLastDragonfireShieldAttack() {
		return lastDragonfireShieldAttack;
	}

	public boolean isDragonfireShieldActive() {
		return dragonfireShieldActive;
	}

	public void setDragonfireShieldActive(boolean dragonfireShieldActive) {
		this.dragonfireShieldActive = dragonfireShieldActive;
	}

	/**
	 * Retrieves the rights for this player.
	 * 
	 * @return the rights
	 */
	public RightGroup getRights() {
		if (rights == null) {
			rights = new RightGroup(this, Right.PLAYER);
		}
		return rights;
	}

	/**
	 * Returns a single instance of the Titles class for this player
	 * 
	 * @return the titles class
	 */
	public Titles getTitles() {
		if (titles == null) {
			titles = new Titles(this);
		}
		return titles;
	}
	public BountyHunter getBountyHunter() {
		return bountyHunter;
	}
	public RandomEventInterface getInterfaceEvent() {
		return randomEventInterface;
	}
	public UltraMysteryBox getUltraInterface() {
		return ultraMysteryBox;
	}
	public NormalMysteryBox getNormalBoxInterface() {
		return normalMysteryBox;
	}
	public SuperMysteryBox getSuperBoxInterface() {
		return superMysteryBox;
	}
	/**
	 * Modifies the current interface open
	 * 
	 * @param interfaceOpen
	 *            the interface id
	 */
	public void setInterfaceOpen(int interfaceOpen) {
		this.interfaceOpen = interfaceOpen;
	}

	/**
	 * The interface that is opened
	 * 
	 * @return the interface id
	 */
	public int getInterfaceOpen() {
		return interfaceOpen;
	}

	/**
	 * Determines whether a warning will be shown when dropping an item.
	 * 
	 * @return True if it's the case, False otherwise.
	 */
	public boolean showDropWarning() {
		return dropWarning;
	}

	/**
	 * Change whether a warning will be shown when dropping items.
	 * 
	 * @param shown
	 *            True in case a warning must be shown, False otherwise.
	 */
	public void setDropWarning(boolean shown) {
		dropWarning = shown;
	}

	public boolean getHourlyBoxToggle() {
		return hourlyBoxToggle;
	}

	public void setHourlyBoxToggle(boolean toggle) {
		hourlyBoxToggle = toggle;
	}

	public boolean getFracturedCrystalToggle() {
		return fracturedCrystalToggle;
	}

	public void setFracturedCrystalToggle(boolean toggle1) {
		fracturedCrystalToggle = toggle1;
	}

	public long setBestZulrahTime(long bestZulrahTime) {
		return this.bestZulrahTime = bestZulrahTime;
	}

	public long getBestZulrahTime() {
		return bestZulrahTime;
	}

	public ZulrahLostItems getZulrahLostItems() {
		if (lostItemsZulrah == null) {
			lostItemsZulrah = new ZulrahLostItems(this);
		}
		return lostItemsZulrah;
	}

	public CerberusLostItems getCerberusLostItems() {
		if (lostItemsCerberus == null) {
			lostItemsCerberus = new CerberusLostItems(this);
		}
		return lostItemsCerberus;
	}

	public SkotizoLostItems getSkotizoLostItems() {
		if (lostItemsSkotizo == null) {
			lostItemsSkotizo = new SkotizoLostItems(this);
		}
		return lostItemsSkotizo;
	}

	public int getArcLightCharge() {
		return arcLightCharge;
	}

	public void setArcLightCharge(int chargeArc) {
		this.arcLightCharge = chargeArc;
	}

	public int getToxicBlowpipeCharge() {
		return toxicBlowpipeCharge;
	}

	public void setToxicBlowpipeCharge(int charge) {
		this.toxicBlowpipeCharge = charge;
	}

	public int getToxicBlowpipeAmmo() {
		return toxicBlowpipeAmmo;
	}
	public void increasePages(int pages) {
		this.pages+= pages;
	}
	public void decreasePages(int pages) {
		this.pages-= pages;
	}
	public void increaseElvenCharges(int elvenCharge) {
		this.elvenCharge+= elvenCharge;
	}
	public void decreaseElvenCharge(int elvenCharge) {
		this.elvenCharge-= elvenCharge;
	}
	public void decreaseCrawsBowCharge(int crawsbowCharge) {
		this.crawsbowCharge-= crawsbowCharge;
	}
	public void decreaseViggoraCharge(int viggoraCharge) {
		this.viggoraCharge-= viggoraCharge;
	}
	public void decreaseThammaronCharge(int thammaronCharge) {
		this.thammaronCharge-= thammaronCharge;
	}
	public void increaseCrawsBowCharge(int crawsbowCharge) {
		this.crawsbowCharge+= crawsbowCharge;
	}
	public void increaseViggoraCharge(int viggoraCharge) {
		this.viggoraCharge+= viggoraCharge;
	}
	public void increaseThammaronCharge(int thammaronCharge) {
		this.thammaronCharge+= thammaronCharge;
	}
	public int getToxicBlowpipeAmmoAmount() {
		return toxicBlowpipeAmmoAmount;
	}

	public void setToxicBlowpipeAmmoAmount(int amount) {
		this.toxicBlowpipeAmmoAmount = amount;
	}

	public void setToxicBlowpipeAmmo(int ammo) {
		this.toxicBlowpipeAmmo = ammo;
	}

	public int getSerpentineHelmCharge() {
		return this.serpentineHelmCharge;
	}

	public void setSerpentineHelmCharge(int charge) {
		this.serpentineHelmCharge = charge;
	}

	public int getTridentCharge() {
		return tridentCharge;
	}

	public void setTridentCharge(int tridentCharge) {
		this.tridentCharge = tridentCharge;
	}

	public int getToxicTridentCharge() {
		return toxicTridentCharge;
	}

	public void setToxicTridentCharge(int toxicTridentCharge) {
		this.toxicTridentCharge = toxicTridentCharge;
	}

	public Fletching getFletching() {
		return fletching;
	}

	public long getLastIncentive() {
		return lastIncentive;
	}

	public void setLastIncentive(long lastIncentive) {
		this.lastIncentive = lastIncentive;
	}

	public boolean receivedIncentiveWarning() {
		return this.incentiveWarning;
	}

	public void updateIncentiveWarning() {
		this.incentiveWarning = true;
	}

	public Tutorial getTutorial() {
		return tutorial;
	}

	public Mode getMode() {
		return mode;
	}

	public Mode setMode(Mode mode) {
		return this.mode = mode;
	}

	public String getRevertOption() {
		return revertOption;
	}

	public void setRevertOption(String revertOption) {
		this.revertOption = revertOption;
	}

	public long getRevertModeDelay() {
		return revertModeDelay;
	}

	public void setRevertModeDelay(long revertModeDelay) {
		this.revertModeDelay = revertModeDelay;
	}

	/**
	 * 
	 * @param skillId
	 * @param amount
	 */
	public void replenishSkill(int skillId, int amount) {
		if (skillId < 0 || skillId > playerLevel.length - 1) {
			return;
		}
		int maximum = getLevelForXP(playerXP[skillId]);
		if (playerLevel[skillId] == maximum) {
			return;
		}
		playerLevel[skillId] += amount;
		if (playerLevel[skillId] > maximum) {
			playerLevel[skillId] = maximum;
		}
		playerAssistant.refreshSkill(skillId);
	}

	public void setArenaPoints(int arenaPoints) {
		this.arenaPoints = arenaPoints;
	}

	public int getArenaPoints() {
		return arenaPoints;
	}

	public void setShayPoints(int shayPoints) {
		this.shayPoints = shayPoints;
	}

	public int getShayPoints() {
		return shayPoints;
	}

	public void setRaidPoints(int raidPoints) {
		this.raidPoints = raidPoints;
	}
	public void membershipdecrease(int amDonated) {
		this.amDonated -=  amDonated;
	}
	public int getRaidPoints() {
		return raidPoints;
	}
	public void braceletDecrease(int  ether) {
		this.ether -=  ether;
	}
	public void braceletIncrease(int  ether) {
		this.ether +=  ether;
	}
	static {
		playerProps = new Stream(new byte[100]);
	}

	@Override
	public boolean susceptibleTo(HealthStatus status) {
		if (getItems().isWearingItem(12931, playerHat) || getItems().isWearingItem(13199, playerHat)
				|| getItems().isWearingItem(13197, playerHat)) {
			return false;
		}
		return true;
	}

	public int getToxicStaffOfTheDeadCharge() {
		return toxicStaffOfTheDeadCharge;
	}

	public void setToxicStaffOfTheDeadCharge(int toxicStaffOfTheDeadCharge) {
		this.toxicStaffOfTheDeadCharge = toxicStaffOfTheDeadCharge;
	}

	public long getExperienceCounter() {
		return experienceCounter;
	}

	public void setExperienceCounter(long experienceCounter) {
		this.experienceCounter = experienceCounter;
	}

	public int getRunEnergy() {
		return runEnergy;
	}

	public void setRunEnergy(int runEnergy) {
		this.runEnergy = runEnergy;
	}

	public int getLastEnergyRecovery() {
		return lastEnergyRecovery;
	}

	public void setLastEnergyRecovery(int lastEnergyRecovery) {
		this.lastEnergyRecovery = lastEnergyRecovery;
	}

	public Entity getTargeted() {
		return targeted;
	}

	public void setTargeted(Entity targeted) {
		this.targeted = targeted;
	}

	public LootingBag getLootingBag() {
		return lootingBag;
	}

	public PrestigeSkills getPrestige() {
		return prestigeskills;
	}

	public ExpLock getExpLock() {
		return explock;
	}

	public void setLootingBag(LootingBag lootingBag) {
		this.lootingBag = lootingBag;
	}

	public SafeBox getSafeBox() {
		return safeBox;
	}

	public void setSafeBox(SafeBox safeBox) {
		this.safeBox = safeBox;
	}

	public RunePouch getRunePouch() {
		return runePouch;
	}

	public void setRunePouch(RunePouch runePouch) {
		this.runePouch = runePouch;
	}

	public HerbSack getHerbSack() {
		return herbSack;
	}

	public void setHerbSack(HerbSack herbSack) {
		this.herbSack = herbSack;
	}

	public GemBag getGemBag() {
		return gemBag;
	}

	public void setGemBag(GemBag gemBag) {
		this.gemBag = gemBag;
	}

	public AchievementDiary<?> getDiary() {
		return diary;
	}

	public void setDiary(AchievementDiary<?> diary) {
		this.diary = diary;
	}

	public AchievementDiaryManager getDiaryManager() {
		return diaryManager;
	}

	public KalphiteQueen getKq() {
		return kq;
	}

	public void setKq(KalphiteQueen kq) {
		this.kq = kq;
	}

	public QuickPrayers getQuick() {
		return quick;
	}

	public Barrows getBarrows() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public long getInfernoBestTime() {
		return infernoBestTime;
	}

	public void setInfernoBestTime(long infernoBestTime) {
		this.infernoBestTime = infernoBestTime;
	}

	public QuestTab getQuestTab() {
		return questTab;
	}

	public EventCalendar getEventCalendar() {
		return eventCalendar;
	}

	public LocalDate getLastVote() {
		return lastVote;
	}

	public void setLastVote(LocalDate lastVote) {
		this.lastVote = lastVote;
	}

    public int getEnterAmountInterfaceId() {
        return enterAmountInterfaceId;
    }

    public void setEnterAmountInterfaceId(int enterAmountInterfaceId) {
        this.enterAmountInterfaceId = enterAmountInterfaceId;
    }
}

