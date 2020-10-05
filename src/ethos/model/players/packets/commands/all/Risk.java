package ethos.model.players.packets.commands.all;

import ethos.Server;
import ethos.model.players.Player;
import ethos.punishments.PunishmentType;
import ethos.punishments.Punishments;

import java.util.Optional;

public class Risk extends Commands {

    @Override
    public void execute(Player c, String input) {
        if (Server.getMultiplayerSessionListener().inAnySession(c)) {
            return;
        }
        if (c.inClanWars() || c.inClanWarsSafe()) {
             return;
        }
        if (c.inWild()) {
            return;
        }
        c.getPA().spellTeleport(2279, 3753, 0, false);
    }

    @Override
    public Optional<String> getDescription() {
        return Optional.of("Teles you to risk pk arena.");
    }

}
