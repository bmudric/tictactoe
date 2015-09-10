package hr.bojan.zadatakkrizickruzic.core.service;

import java.util.ArrayList;
import java.util.List;

import hr.bojan.zadatakkrizickruzic.core.model.CellValue;
import hr.bojan.zadatakkrizickruzic.core.model.Game;
import hr.bojan.zadatakkrizickruzic.core.model.GameStatus;
import hr.bojan.zadatakkrizickruzic.core.model.IllegalAction;
import hr.bojan.zadatakkrizickruzic.core.model.Player;
import hr.bojan.zadatakkrizickruzic.core.model.exception.IllegalActionException;


/**
 * A mock GameService implementation.
 * @author Bojan
 */
//@Component
public class GameServiceMockImpl implements GameService {

	@Override
	public int createNewGame(String firstPlayer, String secondPlayer) {
		return -1;
	}

	@Override
	public Game getGameStatus(int gameId) {
		Game game = new Game();
		game.setGameId(gameId);
		game.setStatus(GameStatus.FINISHED);
		Player humanPlayer = new Player("Human");
		game.setFirstPlayer(humanPlayer);
		game.setSecondPlayer(new Player("Computer", true));
		game.setWinner(humanPlayer);
		return game;
	}

	@Override
	public Game playGame(int gameId, int row, int column)
			throws IllegalActionException {
		Game game = new Game();
		game.setGameId(gameId);
		game.setStatus(GameStatus.IN_PROGRESS);
		game.setFirstPlayer(new Player("Bojan"));
		game.setSecondPlayer(Player.COMPUTER);
		
		if(row < 1 || row > 3 || column < 1 || column > 3){
			throw new IllegalActionException(IllegalAction.MOVE_NOT_ALLOWED);
		}
		else{
			game.getGameBoard()[row - 1][column - 1].setValue(CellValue.X);
		}
		
		return game;
	}

	@Override
	public List<Player> loadStats() {
		List<Player> players = new ArrayList<Player>();
		players.add(new Player("human1"));
		players.add(new Player("human2"));
		return players;
	}
}
