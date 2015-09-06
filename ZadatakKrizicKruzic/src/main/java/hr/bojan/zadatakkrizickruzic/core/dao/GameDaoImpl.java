package hr.bojan.zadatakkrizickruzic.core.dao;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import hr.bojan.zadatakkrizickruzic.core.model.Game;

import org.springframework.stereotype.Repository;

@Repository
public class GameDaoImpl implements GameDao {
	
	private final Map<Integer, Game> games;
	private final AtomicInteger pkGenerator;
	
	public GameDaoImpl() {
		this.games = new ConcurrentHashMap<Integer, Game>();
		this.pkGenerator = new AtomicInteger();
	}

	@Override
	public Game loadGameById(int gameId) {
		return this.games.get(gameId);
	}

	@Override
	public boolean insertNewGame(Game game) {
		int newKey = this.pkGenerator.incrementAndGet();
		game.setGameId(newKey);
		Game old = this.games.putIfAbsent(newKey, game);
		return old == null;
	}

}
