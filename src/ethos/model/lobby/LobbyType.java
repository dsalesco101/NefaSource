package ethos.model.lobby;

import java.util.stream.Stream;

import ethos.model.lobby.impl.ChambersOfXericLobby;
import ethos.model.lobby.impl.TheatreOfBloodLobby;
import ethos.model.lobby.impl.TrialsOfXericLobby;
import lombok.Getter;

public enum LobbyType {
	CHAMBERS_OF_XERIC(ChambersOfXericLobby.class),
	TRIALS_OF_XERIC(TrialsOfXericLobby.class),
	THEATRE_OF_BLOOD(TheatreOfBloodLobby.class)
	
	;
	
	private LobbyType(Class<? extends Lobby> lobbyClass) {
		this.lobbyClass = lobbyClass;
	}
	
	@Getter
	private Class<? extends Lobby> lobbyClass;

	public static Stream<LobbyType> stream() {
		// TODO Auto-generated method stub
		return Stream.of(LobbyType.values());
	}
}
