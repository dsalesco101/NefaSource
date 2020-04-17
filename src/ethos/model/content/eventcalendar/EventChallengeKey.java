package ethos.model.content.eventcalendar;

import java.util.Objects;

import lombok.extern.java.Log;

@Log
public class EventChallengeKey {

    public static EventChallengeKey fromString(String key) {
        String[] split = key.split(",");
        try {
            EventCalendarDay day = EventCalendarDay.valueOf(split[0]);
            EventChallenge challenge = EventChallenge.valueOf(split[1]);
            return new EventChallengeKey(day, challenge);
        } catch (IllegalArgumentException e) {
            log.warning("Error while retrieving EventChallengeKey from text: "
                + "[Day: " + split[0] + "]" + " " + "[Challenge: " + split[1] + "]");
            return null;
        }
    }

    public static String toSerializedString(EventChallengeKey eventChallengeKey) {
        return eventChallengeKey.eventCalendarDay + "," + eventChallengeKey.eventChallenge;
    }

    private EventCalendarDay eventCalendarDay;
    private final EventChallenge eventChallenge;

    public EventChallengeKey(EventCalendarDay eventCalendarDay, EventChallenge eventChallenge) {
        this.eventCalendarDay = eventCalendarDay;
        this.eventChallenge = eventChallenge;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EventChallengeKey that = (EventChallengeKey) o;
        return eventCalendarDay == that.eventCalendarDay &&
                eventChallenge == that.eventChallenge;
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventCalendarDay, eventChallenge);
    }
}
