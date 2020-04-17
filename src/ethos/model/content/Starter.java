package ethos.model.content;

import java.util.ArrayList;
import java.util.List;

import ethos.Server;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.util.Misc;

public class Starter {

    private static final String STARTER_ATTRIBUTE_KEY = "starters";

    public static List<String> getStarters() {
        if (Server.getServerAttributes().getList(STARTER_ATTRIBUTE_KEY) == null) {
            Server.getServerAttributes().setList(STARTER_ATTRIBUTE_KEY, new ArrayList());
        }

        return (List<String>) Server.getServerAttributes().getList(STARTER_ATTRIBUTE_KEY);
    }

    public static void addStarter(Player c) {
        c.setDropWarning(false);
        List<String> starters = getStarters();
        long occurances = starters.stream().filter(data -> data.equals(c.getMacAddress())).count();

        if(c.receivedStarter){
            return;
        }
        if (occurances >= 5) {
            c.sendMessage("@red@You have already received 5 starters.");
            return;
        }
        c.receivedStarter=true;
        c.getItems().addItem(2841, 1);//double exp scroll
        c.getItems().addItem(995, 300000);//coins
        c.getItems().addItem(2528, 1);
        c.getItems().addItem(11739, 1);//vote mystery box
        c.getItems().addItem(8013, 50);//teleport to house
        c.getItems().addItem(380, 100);//noted lobster
        c.getItems().addItem(386, 100);//noted shark
        c.getItems().addItem(1323, 1);//iron scimi
        c.getItems().addItem(1333, 1);//rune scimi
        c.getItems().addItem(1153, 1);//iron helm
        c.getItems().addItem(1115, 1);//iron plate
        c.getItems().addItem(1067, 1);//iron legs
        c.getItems().addItem(3842, 1);//unholy book
        c.getItems().addItem(579, 1);//wizard hat
        c.getItems().addItem(577, 1);//wizard top
        c.getItems().addItem(1011, 1);//wiz robe
        c.getItems().addItem(1381, 1);//staff of air
        c.getItems().addItem(7458, 1);//mith gloves
        c.getItems().addItem(1169, 1);//coif
        c.getItems().addItem(1129, 1);//body
        c.getItems().addItem(1095, 1);//chaps
        c.getItems().addItem(863, 100);//iron knife
        c.getItems().addItem(868, 100);//rune knife
        c.getItems().addItem(558, 100);//mind runes
        c.getItems().addItem(562, 100);//chaos runes
        PlayerHandler.executeGlobalMessage("[@blu@New Player@bla@] " + Misc.capitalizeJustFirst(c.playerName) + " @bla@has logged in! Welcome!");

        List<String> startersList = getStarters();
        startersList.add(c.getMacAddress());
        Server.getServerAttributes().write();
    }

}
