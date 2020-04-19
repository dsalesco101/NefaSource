package ethos.model.content;

import java.util.ArrayList;
import java.util.List;

import ethos.Server;
import ethos.model.players.Player;

public class DailyLoginUses {

    private static final String DL_ATTRIBUTE_KEY = "DailyLogins";

    public static List<String> getUsedDL() {
        if (Server.getServerAttributes().getList(DL_ATTRIBUTE_KEY) == null) {
            Server.getServerAttributes().setList(DL_ATTRIBUTE_KEY, new ArrayList<Object>());
        }

        return ((List<String>) Server.getServerAttributes().getList(DL_ATTRIBUTE_KEY));
    }

    public static void setUsedDL(Player c) {
    	getUsedDL().add(c.getMacAddress());
        Server.getServerAttributes().write();
    }

}
