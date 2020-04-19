package ethos.model.content;

import java.util.ArrayList;
import java.util.List;

import ethos.Server;
import ethos.model.players.Player;

public class Referrals {

    private static final String REFERALL_ATTRIBUTE_KEY = "referalls";

    public static List<String> getUsedReferrals() {
        if (Server.getServerAttributes().getList(REFERALL_ATTRIBUTE_KEY) == null) {
            Server.getServerAttributes().setList(REFERALL_ATTRIBUTE_KEY, new ArrayList<Object>());
        }

        return (List<String>) Server.getServerAttributes().getList(REFERALL_ATTRIBUTE_KEY);
    }

    public static void setUsedReferral(Player c) {
        getUsedReferrals().add(c.getMacAddress());
        Server.getServerAttributes().write();
    }

}
