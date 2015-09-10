package hr.bojan.zadatakkrizickruzic.core.service;

import hr.bojan.zadatakkrizickruzic.core.model.Game;
import hr.bojan.zadatakkrizickruzic.core.model.exception.IllegalActionException;

/**
 * @author Bojan
 */
public interface GameService {
	
	public static final int GAME_DIFFICULTY_MAX = 5;
	public static final int GAME_DIFFICULTY_MIN = 2;
	public static final float GAME_DIFFICULTY_LIMIT_LOW = 0.3f;
	public static final float GAME_DIFFICULTY_LIMIT_HIGH = 0.9f;

	/**
	 * '' (blank) is allowed instead of 'computer'. Two computer players are not allowed.
	 * @param firstPlayer name of first player or 'computer' if computer goes first
	 * @param secondPlayer name of second player or 'computer' if computer goes second
	 * @return Id of new game
	 */
	public int createNewGame(String firstPlayer, String secondPlayer);
	
	/**
	 * @param gameId
	 * @return Game for gameId, null if none found
	 */
	public Game getGameStatus(int gameId);
	
	/**
	 * Play the next move (row, column) in the game identified by gameId
	 * @param gameId 
	 * @param row
	 * @param column
	 * @return Game status after move is made
	 * @throws IllegalActionException if illegal move is made, game has already finished, etc.
	 */
	public Game playGame(int gameId, int row, int column) throws IllegalActionException;
}
