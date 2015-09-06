package hr.bojan.zadatakkrizickruzic.core.manager;

import hr.bojan.zadatakkrizickruzic.core.dao.GameDao;
import hr.bojan.zadatakkrizickruzic.core.dao.PlayerDao;
import hr.bojan.zadatakkrizickruzic.core.model.Game;
import hr.bojan.zadatakkrizickruzic.core.model.GameStatus;
import hr.bojan.zadatakkrizickruzic.core.model.Player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class GameManagerImpl implements GameManager {
	
	@Autowired
	private GameDao gameDao;
	@Autowired
	private PlayerDao playerDao;

	@Override
	public Game createNewGame(String humanPlayer, boolean computerGoesFirst) {
		// create new human player if no such player already exists
		Player human = this.playerDao.loadPlayerByName(humanPlayer);
		if(human == null){
			if(!StringUtils.hasText(humanPlayer)){
				throw new IllegalArgumentException("Player name must not be empty!");
			}
			human = new Player(humanPlayer);
			this.playerDao.insertNewPlayer(human);
		}
		
		// create a new human vs. computer game
		Game newGame = new Game();
		if(computerGoesFirst){
			newGame.setFirstPlayer(Player.COMPUTER);
			newGame.setSecondPlayer(human);
		}
		else{
			newGame.setFirstPlayer(human);
			newGame.setSecondPlayer(Player.COMPUTER);
		}
		newGame.setStatus(GameStatus.IN_PROGRESS);
		
		boolean success = this.gameDao.insertNewGame(newGame);
		if(!success){
			throw new RuntimeException("Creating new game failed!");
		}
		
		return newGame;
	}

}
