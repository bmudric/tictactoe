package hr.bojan.zadatakkrizickruzic.core.dao;

import hr.bojan.zadatakkrizickruzic.ZadatakKrizicKruzicApplication;
import hr.bojan.zadatakkrizickruzic.core.model.Player;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ZadatakKrizicKruzicApplication.class)
public class PlayerDaoJunitTest {
	
	@Autowired
	private PlayerDao playerDao;

	@Test
	public void testLoadPlayerByName() {
		String playerName = "humanPlayerName1";
		
		Player noPlayer = this.playerDao.loadPlayerByName(playerName);
		Assert.isNull(noPlayer);
		
		Player newPlayer = new Player(playerName);
		this.playerDao.insertNewPlayer(newPlayer);
		Player existingPlayer = this.playerDao.loadPlayerByName(playerName);
		Assert.notNull(existingPlayer);
		Assert.isTrue(playerName.equals(existingPlayer.getName()));
	}

	@Test
	public void testInsertNewPlayer() {
		String playerName = "humanPlayerName2";
		
		Player noPlayer = this.playerDao.loadPlayerByName(playerName);
		Assert.isNull(noPlayer);
		
		Player newPlayer = new Player(playerName);
		boolean success = this.playerDao.insertNewPlayer(newPlayer);
		Assert.isTrue(success);

		Player existingPlayer = this.playerDao.loadPlayerByName(playerName);
		Assert.notNull(existingPlayer);
		Assert.isTrue(playerName.equals(existingPlayer.getName()));
	}

}
