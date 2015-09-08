package hr.bojan.zadatakkrizickruzic.core.service;

import hr.bojan.zadatakkrizickruzic.core.manager.GameManager;
import hr.bojan.zadatakkrizickruzic.core.model.Game;
import hr.bojan.zadatakkrizickruzic.core.model.Player;
import hr.bojan.zadatakkrizickruzic.core.model.exception.IllegalActionException;

import java.util.Random;

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
		
		short gameDifficulty = calculateGameDifficulty(newGame.getHumanPlayer());
		newGame.setDifficulty(gameDifficulty);
		
		return newGame.getGameId();
	}
	
	@Override
	public Game getGameStatus(int gameId) {
		if(gameId <= 0){
			throw new IllegalArgumentException("gameId must be a positive integer!");
		}
		return this.gameManager.loadGameById(gameId);
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
	 * @return game difficulty
	 */
	private short calculateGameDifficulty(Player player){
		// calculate player win ratio
		int wins = player.getWins();
		int losses = player.getLosses();
		int draws = player.getDraws();
		float totalGames = wins + losses + draws;
		float playerWinRatio = 0;
		if(wins > 0){
			playerWinRatio = wins / totalGames;
		}
		
		// calculate game difficulty
		if(playerWinRatio > GAME_DIFFICULTY_LIMIT_HIGH){
			return GAME_DIFFICULTY_MAX;
		}
		else if(playerWinRatio < GAME_DIFFICULTY_LIMIT_LOW){
			return GAME_DIFFICULTY_MIN;
		}
		else{
			short difficultySpan = GAME_DIFFICULTY_MAX - GAME_DIFFICULTY_MIN;
			return (short) (GAME_DIFFICULTY_MIN + new Random().nextInt(difficultySpan + 1));
		}
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
