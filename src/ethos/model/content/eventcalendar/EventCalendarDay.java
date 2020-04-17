package ethos.model.content.eventcalendar;

import static ethos.model.content.eventcalendar.EventChallenge.*;

public enum EventCalendarDay {

    DAY_1(WIN_AN_OUTLAST_TOURNAMENT),
    DAY_2(OBTAIN_X_WILDY_EVENT_KEYS),
    DAY_3(GAIN_X_PEST_CONTROL_POINTS),
    DAY_4(COMPLETE_X_HARD_SLAYER_ASSIGNMENTS),
    DAY_5(CUT_DOWN_X_MAGIC_LOGS),
    DAY_6(KILL_X_BARROWS_BROTHERS),
    DAY_7(GET_X_KILLS_IN_WILDY),
    DAY_8(WIN_AN_OUTLAST_TOURNAMENT),
    DAY_9(COMPLETE_X_RAIDS),
    DAY_10(WIELD_A_DRAGON_DEFENDER),
    DAY_11(BURY_X_DRAGON_BONES),
    DAY_12(HAVE_126_COMBAT),
    DAY_13(COMPLETE_A_63_WAVE_FIRE_CAPE),
    DAY_14(KILL_X_REVS_IN_WILDY),
    DAY_15(WIN_AN_OUTLAST_TOURNAMENT),
    DAY_16(GAIN_X_EXCHANGE_POINTS),
    DAY_17(KILL_X_WILDY_BOSSES),
    DAY_18(OBTAIN_X_WILDY_EVENT_KEYS),
    DAY_19(COMPLETE_X_HARD_ACHIEVEMENTS),
    DAY_20(CATCH_X_BLACK_CHINCHOMPAS),
    DAY_21(GAIN_X_PVM_POINTS),
    DAY_22(WIN_AN_OUTLAST_TOURNAMENT),
    DAY_23(PARTICIPATE_IN_X_OUTLAST_TOURNIES),
    DAY_24(WIELD_FULL_ROGUE),
    DAY_25(KILL_ZULRAH_X_TIMES),
    DAY_26(KILL_HUNLLEF_X_TIMES),
    DAY_27(KILL_X_GODWARS_BOSSES_OF_ANY_TYPE),
    DAY_28(HAVE_2000_TOTAL_LEVEL),
    DAY_29(WIN_AN_OUTLAST_TOURNAMENT),
    ;

    public static EventCalendarDay forDayOfTheMonth(long dayOfTheMonth) {
        long dayIndex = dayOfTheMonth - 1;
        if (dayIndex < 0 || dayIndex >= values().length) {
            throw new IllegalArgumentException();
        } else {
            return values()[(int)dayIndex];
        }
    }

    private final EventChallenge challenge;

    EventCalendarDay(EventChallenge challenge) {
        this.challenge = challenge;
    }

    public int[] getReward() {
        return isLastDay() ? new int[] {100, 125} : new int[] {50, 65};
    }

    public boolean isLastDay() {
        return ordinal() == values().length - 1;
    }

    public int getDay() {
        return ordinal() + 1;
    }

    public EventChallenge getChallenge() {
        return challenge;
    }
}
