package hr.bojan.zadatakkrizickruzic.core.dao;

import hr.bojan.zadatakkrizickruzic.core.model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

@Repository
public class PlayerDaoImpl implements PlayerDao {
	
	private final Map<String, Player> players;
	
	public PlayerDaoImpl() {
		this.players = new ConcurrentHashMap<String, Player>();
	}

	@Override
	public Player loadPlayerByName(String playerName) {
		return this.players.get(playerName);
	}

	@Override
	public boolean insertNewPlayer(Player player) {
		Player old = this.players.putIfAbsent(player.getName(), player);
		return old == null;
	}

	@Override
	public List<Player> loadAllPlayers() {
		return new ArrayList<Player>(this.players.values());
	}
}
