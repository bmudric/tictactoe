package hr.bojan.zadatakkrizickruzic.core.service;

import hr.bojan.zadatakkrizickruzic.core.manager.GameManager;
import hr.bojan.zadatakkrizickruzic.core.model.Cell;
import hr.bojan.zadatakkrizickruzic.core.model.CellValue;
import hr.bojan.zadatakkrizickruzic.core.model.EndgameStatus;
import hr.bojan.zadatakkrizickruzic.core.model.Game;
import hr.bojan.zadatakkrizickruzic.core.model.GameStatus;
import hr.bojan.zadatakkrizickruzic.core.model.IllegalAction;
import hr.bojan.zadatakkrizickruzic.core.model.Move;
import hr.bojan.zadatakkrizickruzic.core.model.Player;
import hr.bojan.zadatakkrizickruzic.core.model.exception.IllegalActionException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Service handling gameplay. Computer is O, player is X
 * @author Bojan
 */
@Service
public class GameServiceImpl implements GameService {

	@Autowired
	private GameManager gameManager;
	
	@Override
	public int createNewGame(String firstPlayer, String secondPlayer) {
		
		// check parameters and create a new game
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
		
		int gameDifficulty = calculateGameDifficulty(newGame.determineHumanPlayer());
		newGame.setDifficulty(gameDifficulty);
		
		// if computer goes first, make computer move
		if(computerGoesFirst){
			Move firstMove = computerStartGame(gameDifficulty);
			Cell[][] gameBoard = newGame.getGameBoard();
			gameBoard[firstMove.getRow()][firstMove.getCol()].setValue(CellValue.O);
		}
		
		return newGame.getGameId();
	}
	
	private Move computerStartGame(int gameDifficulty){
		Random rand = new Random();
		int row, col;
		if(GameService.GAME_DIFFICULTY_MAX == gameDifficulty){
			// start in a corner
			row = rand.nextBoolean() ? 0 : 2; 
			col = rand.nextBoolean() ? 0 : 2;
		}
		else if(GameService.GAME_DIFFICULTY_MIN == gameDifficulty){
			// start on an edge
			row = rand.nextInt(3);
			col = 1;
			if(row == 1){
				col = rand.nextBoolean() ? 0 : 2;
			}
		}
		else{
			// start in the center or in a corner
			row = rand.nextInt(3);
			col = 1;
			if(row == 0 || row == 2){
				col = rand.nextBoolean() ? 0 : 2;
			}
		}
		return new Move(row, col, 0);
	}
	
	@Override
	public Game getGameStatus(int gameId) {
		if(gameId <= 0){
			throw new IllegalArgumentException("gameId must be a positive integer!");
		}
		return this.gameManager.loadGameById(gameId);
	}

	@Override
	public Game playGame(int gameId, int row, int column)
			throws IllegalActionException {
		
		// check all preconditions for player making a move are OK
		if(gameId <= 0){
			throw new IllegalArgumentException("gameId must be a positive integer!");
		}
		Game game = this.gameManager.loadGameById(gameId);
		
		int rowIndex = row - 1;
		int columnIndex = column - 1;
		Cell[][] gameBoard = game.getGameBoard();
		
		if(game.getStatus() == GameStatus.FINISHED){
			throw new IllegalActionException(IllegalAction.GAME_FINISHED);
		}
		else if(!isMoveLegal(gameBoard, rowIndex, columnIndex)){
			throw new IllegalActionException(IllegalAction.MOVE_NOT_ALLOWED);
		}
		
		// allow player move and evaluate game
		gameBoard[rowIndex][columnIndex].setValue(CellValue.X);
		evaluateGameState(game);
		if(game.getStatus() == GameStatus.FINISHED){
			return game;
		}
		
		// make computer move and reevaluate game state
		Move bestMove = calculateMove(game.getGameBoard(), game.getDifficulty(), 0, true);
		gameBoard[bestMove.getRow()][bestMove.getCol()].setValue(CellValue.O);
		evaluateGameState(game);
		
		return game;
	}

	/**
	 * Checks if game is over, assigns victor if any, increments counters
	 * @param game to evaluate
	 */
	private void evaluateGameState(Game game) {
		EndgameStatus status = determineEndgameStatus(game.getGameBoard());
		if(status == EndgameStatus.X_WON){
			game.setStatus(GameStatus.FINISHED);
			Player human = game.determineHumanPlayer();
			game.setWinner(human);
			human.incrementWins();
			game.determineComputerPlayer().incrementLosses();
		}
		else if(status == EndgameStatus.O_WON){
			game.setStatus(GameStatus.FINISHED);
			Player computer = game.determineComputerPlayer();
			game.setWinner(computer);
			computer.incrementWins();
			game.determineHumanPlayer().incrementLosses();
		}
		else if(status == EndgameStatus.DRAW){
			game.setStatus(GameStatus.FINISHED);
			game.determineComputerPlayer().incrementDraws();
			game.determineHumanPlayer().incrementDraws();
		}
	}
	
	/**
	 * Calculates the computer's next move. 
	 * Come up with best move based on scoring all possible outcomes up to a certain depth (difficulty).
	 * @param gameBoard to choose next best move on
	 * @param difficulty representing depth of search for best move
	 * @param depth current search depth
	 * @param computerTurn true if the current turn on the game board is the computer's
	 */
	private Move calculateMove(Cell[][] gameBoard, int difficulty, int depth, boolean computerTurn) {
		EndgameStatus endgameStatus = determineEndgameStatus(gameBoard);
		// if game is over, score the game and return the score
		if(endgameStatus != EndgameStatus.STILL_IN_PROGRESS){
			// score game
			int score = scoreGame(endgameStatus, depth);
			return new Move(-1, -1, score);
		}
		// if search limit has been reached, return score 0
		else if(difficulty == depth){
			return new Move(-1, -1, 0);
		}
		
		
		// for each possible move generate a new game board and score
		List<Move> possibleMoves = generatePossibleMoves(gameBoard);
		Collections.shuffle(possibleMoves);
		String originalBoard = Game.translateGameBoard(gameBoard);
		CellValue currentPlayerValue = computerTurn ? CellValue.O : CellValue.X;
		
		Move bestMove = new Move(-1, -1, computerTurn ? Integer.MIN_VALUE : Integer.MAX_VALUE);
		for(Move move : possibleMoves){
			Cell[][] newBoard = Game.createGameBoard(originalBoard);
			newBoard[move.getRow()][move.getCol()].setValue(currentPlayerValue);
			// calculate next move for the opposing player
			Move nextMove = calculateMove(newBoard, difficulty, depth + 1, !computerTurn);
			if(computerTurn && nextMove.getScore() >= bestMove.getScore()
					|| !computerTurn && nextMove.getScore() <= bestMove.getScore()){
				bestMove = move;
				bestMove.setScore(nextMove.getScore());
			}
		}
		return bestMove;
	}
	
	/**
	 * Score the game if it is a win or draw.
	 */
	private int scoreGame(EndgameStatus endgameStatus,int depth){
		int maxScore = 10;
		if(endgameStatus == EndgameStatus.O_WON){
			return maxScore - depth;
		}
		else if(endgameStatus == EndgameStatus.X_WON){
			return depth - maxScore;
		}
		else{
			return 0;
		}
	}

	/**
	 * Get all board positions not already taken.
	 * @param gameBoard to search
	 * @return list of available moves
	 */
	private List<Move> generatePossibleMoves(Cell[][] gameBoard){
		List<Move> possibleMoves = new ArrayList<>();
		for (int row = 0; row < 3; row ++){
			for (int col = 0; col < 3; col++){
		    	if(gameBoard[row][col].getValue() == CellValue.BLANK){
		    		possibleMoves.add(new Move(row, col, 0));
		    	}
		    }
		}
		return possibleMoves;
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
		int[] columnCounter = new int[]{0,0,0};
		int[] rowCounter = new int[]{0,0,0};
		int diagLRCounter = 0;
		int diagRLCounter = 0;
		
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
	private boolean isMoveLegal(Cell[][] gameBoard, int row, int column){
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
	private int calculateGameDifficulty(Player player){
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
			int difficultySpan = GAME_DIFFICULTY_MAX - GAME_DIFFICULTY_MIN;
			return GAME_DIFFICULTY_MIN + new Random().nextInt(difficultySpan + 1);
		}
	}

	@Override
	public List<Player> loadStats() {
		List<Player> players = this.gameManager.loadStats();
		players.remove(Player.COMPUTER);
		return players;
	}
}
