package hr.bojan.zadatakkrizickruzic.core.manager;

import java.util.List;

import hr.bojan.zadatakkrizickruzic.core.model.Game;
import hr.bojan.zadatakkrizickruzic.core.model.Player;

public interface GameManager {

	/**
	 * Creates a new game and player if they don't exist
	 * @param humanPlayer name of human player
	 * @param computerGoesFirst if true computer is first player, else computer is second player
	 * @return newly created game
	 */
	public Game createNewGame(String humanPlayer, boolean computerGoesFirst);
	
	/**
	 * @param gameId
	 * @return game for gameId, null if none exists
	 */
	public Game loadGameById(int gameId);
	
	/**
	 * @return list of all game players
	 */
	public List<Player> loadStats();
}
