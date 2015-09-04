package hr.bojan.zadatakkrizickruzic.core.model;


/**
 * Represents a game of tic-tac-toe.
 * @author Bojan
 */
public class Game {

	private int gameId;
	private GameStatus status;
	private Player firstPlayer;
	private Player secondPlayer;
	private Player winner;
	private Cell[][] gameBoard;
	
	public Game() {
		this.gameBoard = new Cell[3][3];
		for (int row = 0; row < 3; row ++){
		    for (int col = 0; col < 3; col++){
		        this.gameBoard[row][col] = new Cell();
		    }
		}
	}
	
	public int getGameId() {
		return gameId;
	}
	
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	
	public GameStatus getStatus() {
		return status;
	}
	
	public void setStatus(GameStatus status) {
		this.status = status;
	}

	public Player getFirstPlayer() {
		return firstPlayer;
	}

	public void setFirstPlayer(Player firstPlayer) {
		this.firstPlayer = firstPlayer;
	}

	public Player getSecondPlayer() {
		return secondPlayer;
	}

	public void setSecondPlayer(Player secondPlayer) {
		this.secondPlayer = secondPlayer;
	}

	public Player getWinner() {
		return winner;
	}

	public void setWinner(Player winner) {
		this.winner = winner;
	}

	public Cell[][] getGameBoard() {
		return gameBoard;
	}

	public void setGameBoard(Cell[][] gameBoard) {
		this.gameBoard = gameBoard;
	}
}
