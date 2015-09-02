package hr.bojan.zadatakkrizickruzic.core.service;

import hr.bojan.zadatakkrizickruzic.core.model.Game;
import hr.bojan.zadatakkrizickruzic.core.model.GameStatus;
import hr.bojan.zadatakkrizickruzic.core.model.exception.IllegalActionException;

import org.springframework.stereotype.Component;

/**
 * A mock GameService implemetation.
 * @author Bojan
 */
@Component
public class GameServiceMockImpl implements GameService {

	@Override
	public int createNewGame(String firstPlayer, String secondPlayer) {
		return -1;
	}

	@Override
	public Game getGameStatus(int gameId) {
		Game game = new Game();
		game.setGameId(-1);
		game.setGameStatus(GameStatus.IN_PROGRESS);
		return game;
	}

	@Override
	public Game playGame(int gameId, short row, short column)
			throws IllegalActionException {
		// TODO Auto-generated method stub
		return null;
	}

}
