package ethos.model.npcs.bosses;

import ethos.model.content.instances.SingleInstancedArea;
import ethos.model.npcs.NPCHandler;
import ethos.model.players.Boundary;
import ethos.model.players.Player;

/**
 * 
 * @author Grant_ | www.rune-server.ee/members/grant_ | 12/5/19
 *
 */
public class KrakenInstance extends SingleInstancedArea {

	public KrakenInstance(Player player, Boundary boundary, int height) {
		super(player, boundary, height);
	}

	@Override
	public void onDispose() {
		Kraken kraken = player.getKraken();
		if (kraken.getNpc() != null) {
			NPCHandler.kill(kraken.getNpc().npcType, height);
		}
	}
}
