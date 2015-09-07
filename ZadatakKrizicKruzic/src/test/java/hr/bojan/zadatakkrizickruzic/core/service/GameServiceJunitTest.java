package hr.bojan.zadatakkrizickruzic.core.service;

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
public class GameServiceJunitTest {
	
	@Autowired
	private GameService gameService;

	@Test(expected=IllegalArgumentException.class)
	public void testCreateNewGame_bothEmpty() {
		this.gameService.createNewGame("", "");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCreateNewGame_bothNull() {
		this.gameService.createNewGame(null, null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCreateNewGame_bothComputer() {
		this.gameService.createNewGame("Computer", "Computer");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCreateNewGame_bothHuman() {
		this.gameService.createNewGame("Human1", "Human2");
	}
	
	@Test()
	public void testCreateNewGame_humanFirst1() {
		int gameId = this.gameService.createNewGame("HumanFirst1", "Computer");
		Assert.isTrue(gameId > 0);
	}
	
	@Test()
	public void testCreateNewGame_humanFirst2() {
		int gameId = this.gameService.createNewGame("HumanFirst2", "");
		Assert.isTrue(gameId > 0);
	}
	
	@Test()
	public void testCreateNewGame_humanSecond1() {
		int gameId = this.gameService.createNewGame("Computer", "HumanSecond1");
		Assert.isTrue(gameId > 0);
	}
	
	@Test()
	public void testCreateNewGame_humanSecond2() {
		int gameId = this.gameService.createNewGame("", "HumanSecond2");
		Assert.isTrue(gameId > 0);
	}
	
	@Test()
	public void testGetGameStatus() {
		String humanName = "playerGameStatus1";
		int gameId = this.gameService.createNewGame(humanName, "");
		Assert.isTrue(gameId > 0);
		
		Game game = this.gameService.getGameStatus(gameId);
		Assert.isTrue(gameId == game.getGameId());
		Assert.isTrue(humanName.equals(game.getFirstPlayer().getName()));
	}

}
