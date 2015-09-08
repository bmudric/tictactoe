package hr.bojan.zadatakkrizickruzic.core.service;

import hr.bojan.zadatakkrizickruzic.core.manager.GameManager;
import hr.bojan.zadatakkrizickruzic.core.model.Cell;
import hr.bojan.zadatakkrizickruzic.core.model.CellValue;
import hr.bojan.zadatakkrizickruzic.core.model.EndgameStatus;
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
		
		short gameDifficulty = calculateGameDifficulty(newGame.determineHumanPlayer());
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
	 * @param gameBoard to evaluate
	 * @return who won the game if it is over
	 */
	private EndgameStatus determineEndgameStatus(Cell[][] gameBoard){
		if(playerWon(gameBoard, CellValue.X)){
			return EndgameStatus.X_WON;
		}
		else if(playerWon(gameBoard, CellValue.O)){
			return EndgameStatus.O_WON;
		}
		else if(boardFull(gameBoard)){
			return EndgameStatus.DRAW;
		}
		else{
			return EndgameStatus.STILL_IN_PROGRESS;
		}
	}
	
	/**
	 * @param gameBoard to evaluate
	 * @param mark of the player
	 * @return true if the player has won
	 */
	private boolean playerWon(Cell[][] gameBoard, CellValue mark){
		short[] columnCounter = new short[]{0,0,0};
		short[] rowCounter = new short[]{0,0,0};
		short diagLRCounter = 0;
		short diagRLCounter = 0;
		
		// count in-line occurences
		for (int row = 0; row < 3; row ++){
			for (int col = 0; col < 3; col++){
				if(gameBoard[row][col].getValue() == mark){
					columnCounter[col]++;
					rowCounter[row]++;
				}
				if(row == col && gameBoard[row][col].getValue() == mark){
					diagLRCounter++;
				}
				if((2 - row) == col && gameBoard[row][col].getValue() == mark){
					diagRLCounter++;
				}
		    }
		}
		
		// check diagonal counters
		if(diagLRCounter == 3 || diagRLCounter == 3){
			return true;
		}
		
		// check row & column counters
		for (int i = 0; i < 3; i ++){
			if(rowCounter[i] == 3 || columnCounter[i] == 3){
				return true;
			}
		}
		
		// no winning line found
		return false;
	}
	
	/**
	 * @param gameBoard to evaluate
	 * @return true if the game board is completely full
	 */
	private boolean boardFull(Cell[][] gameBoard){
		for (int row = 0; row < 3; row ++){
			for (int col = 0; col < 3; col++){
				if(gameBoard[row][col].getValue() == CellValue.BLANK){
					return false;
				}
		    }
		}
		return true;
	}
	
	/**
	 * @return false if cell already taken or out of bounds, true if ok
	 */
	private boolean isMoveLegal(Cell[][] gameBoard, short row, short column){
		// out of board bounds
		if(gameBoard.length <= column || column < 0 || gameBoard[0].length <= row || row < 0){
			return false;
		}
		// cell already taken
		else if(gameBoard[row][column].getValue() != CellValue.BLANK){
			return false;
		}
		else{
			return true;
		}
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
