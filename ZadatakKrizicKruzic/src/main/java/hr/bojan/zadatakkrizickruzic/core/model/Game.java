package hr.bojan.zadatakkrizickruzic.core.model;

import lombok.Data;

import org.springframework.util.StringUtils;


/**
 * Represents a game of tic-tac-toe.
 * @author Bojan
 */
@Data
public class Game {

	private int gameId;
	private GameStatus status;
	private short difficulty;
	private Player firstPlayer;
	private Player secondPlayer;
	private Player winner;
	private Cell[][] gameBoard;
	
	public Game() {
		this.gameBoard = createGameBoard("_________");
	}
	
	/**
	 * Creates a new game board using boardValues to fill it.
	 * @param boardValues must be of length 9, 3 chars for first row, 3 for second row and 3 for third row
	 * @return new game board
	 */
	public static Cell[][] createGameBoard(String boardValues){
		if(!StringUtils.hasText(boardValues) || boardValues.length() != 9){
			throw new IllegalArgumentException("Board must have 9 cells");
		}
		
		char[] charValues = boardValues.toCharArray();
		Cell[][] newGameBoard = new Cell[3][3];
		
		// translate list of characters onto the new game board
		for (int row = 0; row < 3; row ++){
			for (int col = 0; col < 3; col++){
		    	char cellValue = charValues[(row * 3) + col];
		    	newGameBoard[row][col] = new Cell(cellValue);
		    }
		}
		return newGameBoard;
	}
	
	public static String translateGameBoard(Cell[][] gameBoard){
		StringBuilder translation = new StringBuilder();
		for (int row = 0; row < 3; row ++){
			for (int col = 0; col < 3; col++){
				CellValue value = gameBoard[row][col].getValue();
				translation.append(value.getValue());
		    }
		}
		return translation.toString();
	}
	
	public Player determineHumanPlayer(){
		if(!firstPlayer.isComputer()){
			return firstPlayer;
		}
		else if(!secondPlayer.isComputer()){
			return secondPlayer;
		}
		else{
			throw new RuntimeException("No human players found in the game!");
		}
	}
	
	public Player determineComputerPlayer(){
		if(firstPlayer.isComputer()){
			return firstPlayer;
		}
		else if(secondPlayer.isComputer()){
			return secondPlayer;
		}
		else{
			throw new RuntimeException("No computer players found in the game!");
		}
	}
}
