package ethos.model.content.eventcalendar;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

import com.google.common.base.Preconditions;
import ethos.Server;
import ethos.ServerState;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.sql.DatabaseConfiguration;
import ethos.sql.eventcalendar.queries.SelectWinnerQuery;
import ethos.util.Misc;
import lombok.extern.java.Log;

/**
 * Ticks all day every day until it selects a winner for the {@link EventCalendar}.
 * @author Michael Sasse (https://github.com/mikeysasse/)
 */
@Log
public class EventCalendarWinnerSelection {

    private static final ReentrantLock LOCK = new ReentrantLock();
    private static EventCalendarWinnerSelection singleton = getStandard();

    public static EventCalendarWinnerSelection getStandard() {
       return getStandard(LocalDate.now());
    }

    public static EventCalendarWinnerSelection getStandard(LocalDate current) {
        EventCalendarWinnerSelection winnerSelection = new EventCalendarWinnerSelection();
        winnerSelection.current = current;
        winnerSelection.selectWinnerPredicate = testDate -> testDate.toEpochDay() > winnerSelection.current.toEpochDay();
        return winnerSelection;
    }

    public static void setSingleton(EventCalendarWinnerSelection singleton) {
        Preconditions.checkState(Server.getConfiguration().getServerState() != ServerState.PUBLIC_PRIMARY);
        EventCalendarWinnerSelection.singleton = singleton;
    }

    public static EventCalendarWinnerSelection getSingleton() {
        return singleton;
    }

    private LocalDate current;
    private Predicate<LocalDate> selectWinnerPredicate;

    private EventCalendarWinnerSelection() {}

    private void setCurrent(LocalDate current) {
        LOCK.lock();
        try {
            this.current = current;
        } finally {
            LOCK.unlock();
        }
    }

    public Future<?> tick() {
        final LocalDate currentCopy = current;
        final LocalDate today = EventCalendar.getDateProvider().getLocalDate();
        if (EventCalendar.isEventRunning(current) && selectWinnerPredicate.test(today)) {
            setCurrent(today);
            return Server.getDatabaseManager().execute(DatabaseConfiguration.EVENT_CALENDAR, ((context, connection) -> {
                ChallengeParticipant winner;
                try {
                    winner = new SelectWinnerQuery(currentCopy.getDayOfMonth()).execute(context, connection);
                    if (winner != null) {
                        log.info("Chose calendar winner for " + currentCopy.toString() + ", " + winner.getUsername());
                    } else {
                        log.info("No calendar winner for " + currentCopy.toString());
                    }
                } catch (SQLException e) {
                    // When this happens we go backwards one day and attempt it again.
                    e.printStackTrace();
                    setCurrent(currentCopy);
                    return null;
                }

                PlayerHandler.addQueuedAction(() -> {
                    PlayerHandler.executeGlobalMessage(EventCalendar.MESSAGE_COLOUR + Misc.formatPlayerName(winner.getUsername())
                            + " has been selected as today's " + EventCalendar.EVENT_NAME + " winner!");
                    Optional<Player> playerOptional = PlayerHandler.getOptionalPlayer(winner.getUsername());
                    if (playerOptional.isPresent()) {
                        Player player = playerOptional.get();
                        player.sendMessage(EventCalendar.MESSAGE_COLOUR + "Congratulations, you've been selected as today's "
                                + EventCalendar.EVENT_NAME + " winner!");
                        player.sendMessage(EventCalendar.MESSAGE_COLOUR + "You can post a message on the discord whenever your ready to collect your reward.");
                    }
                });

                return null;
            }));
        } else {
            this.current = today;
            return null;
        }
    }
}
