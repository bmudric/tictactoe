package hr.bojan.zadatakkrizickruzic.core.dao;

import hr.bojan.zadatakkrizickruzic.ZadatakKrizicKruzicApplication;
import hr.bojan.zadatakkrizickruzic.core.model.Game;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ZadatakKrizicKruzicApplication.class)
public class GameDaoJunitTest {
	
	@Autowired
	private GameDao gameDao;

	@Test
	public void testLoadGameById() {
		Game noGame = this.gameDao.loadGameById(0);
		Assert.isNull(noGame);
		
		Game firstGame = new Game();
		Assert.isTrue(firstGame.getGameId() == 0);
		this.gameDao.insertNewGame(firstGame);
		
		Game loadedGame = this.gameDao.loadGameById(firstGame.getGameId());
		Assert.isTrue(firstGame == loadedGame);
		Assert.isTrue(firstGame.getGameId() == loadedGame.getGameId());
	}

	@Test
	public void testInsertNewGame() {
		Game newGame = new Game();
		Assert.isTrue(newGame.getGameId() == 0);
		boolean success = this.gameDao.insertNewGame(newGame);
		Assert.isTrue(success);
		Assert.isTrue(newGame.getGameId() != 0);
	}

}
