package ethos.model.players.combat.formula;

import ethos.model.players.Player;
import ethos.util.Misc;

public class MagicSuccessFormula {

    /**
     * Is magic attack a successful hit?
     *
     * Source is <a href="https://oldschool.runescape.wiki/w/Magic#Magic_accuracy">Wiki</a>
     *
     * @param attacker the attacker
     * @param defender the defender
     * @return <code>true</code> for successful hit
     */
    public static boolean isHit(Player attacker, Player defender) {
        // Attacker
        double attackerPrayerBonus = attacker.prayerActive[4] ? 1.05
                : attacker.prayerActive[12] ? 1.10
                : attacker.prayerActive[20] ? 1.15
                : 1.0;
        double attackerEquipmentBonus = attacker.playerBonus[3];
        double attackerMagicLevel = attacker.playerLevel[6];
        double attackerEffectiveLevel = attackerMagicLevel * attackerPrayerBonus + 8;
        double attackRoll = attackerEffectiveLevel * (attackerEquipmentBonus + 64);


        // Defender
        double defenderPrayerBonus = defender.prayerActive[0] ? 1.05
                : defender.prayerActive[5] ? 1.10
                : defender.prayerActive[13] ? 1.15
                : defender.prayerActive[24] ? 1.20
                : defender.prayerActive[25] ? 1.25
                : 1.0;
        double defenderEquipmentBonus = defender.playerBonus[8];
        double defenderDefenceLevel = defender.playerLevel[1] * defenderPrayerBonus; // Formula doesn't mention defender prayer bonus at all
        double defenderMagicLevel = defender.playerLevel[6];
        double defenderEffectiveLevel = defenderMagicLevel * 0.7 + defenderDefenceLevel * 0.3 + 8;
        double defenceRoll = defenderEffectiveLevel * (defenderEquipmentBonus + 64);

        double roll;
        if (attackRoll > defenceRoll) {
            roll = 1 - (defenceRoll + 2) / (2 * (attackRoll + 1));
        } else {
            roll = attackRoll / (2 * defenceRoll + 1);
        }

        int hitChance = (int) Math.ceil(roll * 100);

        if (attacker.debugMessage) {
            attacker.sendMessage("Hit chance: " + hitChance + "%");
        }

        if (defender.debugMessage) {
            defender.sendMessage("Block chance: " + (100 - hitChance) + "%");
        }

        return Misc.random(0, 100) <= hitChance;
    }

}
