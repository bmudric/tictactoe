package hr.bojan.zadatakkrizickruzic.core.service;

import hr.bojan.zadatakkrizickruzic.core.manager.GameManager;
import hr.bojan.zadatakkrizickruzic.core.model.Game;
import hr.bojan.zadatakkrizickruzic.core.model.Player;
import hr.bojan.zadatakkrizickruzic.core.model.exception.IllegalActionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Service handling gameplay.
 * @author Bojan
 */
@Service
public class GameServiceImpl implements GameService {

	@Autowired
	private GameManager gameManager;
	
	@Override
	public int createNewGame(String firstPlayer, String secondPlayer) {
		
		boolean firstPlayerIsHuman = StringUtils.hasText(firstPlayer) 
				&& !Player.COMPUTER.getName().equalsIgnoreCase(firstPlayer);
		boolean secondPlayerIsHuman = StringUtils.hasText(secondPlayer) 
				&& !Player.COMPUTER.getName().equalsIgnoreCase(secondPlayer);
		
		if(firstPlayerIsHuman && secondPlayerIsHuman){
			throw new IllegalArgumentException("One of the players must be human!");
		}
		else if(!firstPlayerIsHuman && !secondPlayerIsHuman){
			throw new IllegalArgumentException("One of the players must be Computer!");
		}
		
		String humanPlayer = null;
		if(firstPlayerIsHuman){
			humanPlayer = firstPlayer;
		}
		else{
			humanPlayer = secondPlayer;
		}
		boolean computerGoesFirst = !firstPlayerIsHuman;
		
		Game newGame = this.gameManager.createNewGame(humanPlayer, computerGoesFirst);
		
		return newGame.getGameId();
	}

	@Override
	public Game getGameStatus(int gameId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Game playGame(int gameId, short row, short column)
			throws IllegalActionException {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * @return true if win or no more moves available
	 */
	private boolean isGameOver(){
		return false;
	}
	
	/**
	 * @return false if cell already taken or out of bounds
	 */
	private boolean isMoveLegal(){
		return false;
	}
	
	/**
	 * Choose between available AI strategies based on human player's past performance.
	 * Difficult AI if player won 90% or more games, easy AI if player won 30% or less, else random.
	 */
	private void determinePlayingStrategy(){
		
	}
	
	/**
	 * Come up with best move based on scoring all possible outcomes.  
	 */
	private void minimax(){
		
	}
	
	/**
	 * Get all board positions not already taken.
	 */
	private void generatePossibleMoves(){
		
	}
	
	/**
	 * Score the game if it is a win or draw.
	 */
	private void score(){
		
	}

}
