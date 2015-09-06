package hr.bojan.zadatakkrizickruzic.core.dao;

import hr.bojan.zadatakkrizickruzic.core.model.Game;

public interface GameDao {

	/**
	 * @param gameId
	 * @return game for gameId, null if none exists
	 */
	public Game loadGameById(int gameId);
	
	/**
	 * Inserts new game and updates gameId
	 * @param game
	 * @return true if insert successful
	 */
	public boolean insertNewGame(Game game);
}
