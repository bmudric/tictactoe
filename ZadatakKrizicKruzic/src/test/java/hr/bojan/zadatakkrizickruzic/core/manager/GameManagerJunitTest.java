package hr.bojan.zadatakkrizickruzic.core.manager;

import hr.bojan.zadatakkrizickruzic.ZadatakKrizicKruzicApplication;
import hr.bojan.zadatakkrizickruzic.core.model.Game;
import hr.bojan.zadatakkrizickruzic.core.model.GameStatus;
import hr.bojan.zadatakkrizickruzic.core.model.Player;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ZadatakKrizicKruzicApplication.class)
public class GameManagerJunitTest {
	
	@Autowired
	private GameManager gameManager;

	@Test
	public void testCreateNewGame_humanFirst() {
		String humanName = "humanFirst";
		Game game = this.gameManager.createNewGame(humanName, false);
		
		Assert.notNull(game);
		Assert.isTrue(game.getGameId() != 0);
		Assert.isTrue(GameStatus.IN_PROGRESS == game.getStatus());
		Assert.isNull(game.getWinner());
		
		Player firstPlayer = game.getFirstPlayer();
		Assert.notNull(firstPlayer);
		Assert.isTrue(!firstPlayer.isComputer());
		Assert.isTrue(humanName.equals(firstPlayer.getName()));
		
		Assert.isTrue(Player.COMPUTER == game.getSecondPlayer());
	}

	@Test
	public void testCreateNewGame_computerFirst() {
		String humanName = "humanSecond";
		Game game = this.gameManager.createNewGame(humanName, true);
		
		Assert.notNull(game);
		Assert.isTrue(game.getGameId() != 0);
		Assert.isTrue(GameStatus.IN_PROGRESS == game.getStatus());
		Assert.isNull(game.getWinner());
		
		Assert.isTrue(Player.COMPUTER == game.getFirstPlayer());
		
		Player secondPlayer = game.getSecondPlayer();
		Assert.notNull(secondPlayer);
		Assert.isTrue(!secondPlayer.isComputer());
		Assert.isTrue(humanName.equals(secondPlayer.getName()));
	}
	
	@Test(expected=NullPointerException.class)
	public void testCreateNewGame_invalidName_null(){
		this.gameManager.createNewGame(null, true);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCreateNewGame_invalidName_empty(){
		this.gameManager.createNewGame("", true);
	}
}
