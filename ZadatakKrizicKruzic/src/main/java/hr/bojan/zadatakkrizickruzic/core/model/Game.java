package hr.bojan.zadatakkrizickruzic.core.model;

public class Game {

	private int gameId;
	private GameStatus gameStatus;
	
	public int getGameId() {
		return gameId;
	}
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	public GameStatus getGameStatus() {
		return gameStatus;
	}
	public void setGameStatus(GameStatus gameStatus) {
		this.gameStatus = gameStatus;
	}
}
