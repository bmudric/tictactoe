package hr.bojan.zadatakkrizickruzic.core.service;

import hr.bojan.zadatakkrizickruzic.ZadatakKrizicKruzicApplication;
import hr.bojan.zadatakkrizickruzic.core.model.Game;
import hr.bojan.zadatakkrizickruzic.core.model.Player;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.reflect.Whitebox;
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
	
	@Test
	public void testCalculateGameDifficulty() throws Exception{
		Set<Short> possibleDifficulties = new HashSet<>();
		for(short i = GameService.GAME_DIFFICULTY_MIN; i <= GameService.GAME_DIFFICULTY_MAX; i++){
			possibleDifficulties.add(i);
		}
		
		Player newPlayer = new Player("newPlayer");
		Set<Short> newPlayerDiffs = new HashSet<>();
		for(int i = 0; i < 100; i++){
			short newPlayerDiff = Whitebox.<Short> invokeMethod(this.gameService, "calculateGameDifficulty", newPlayer);
			Assert.isTrue(GameService.GAME_DIFFICULTY_MIN <= newPlayerDiff);
			Assert.isTrue(GameService.GAME_DIFFICULTY_MAX >= newPlayerDiff);
			newPlayerDiffs.add(newPlayerDiff);
		}
		Assert.isTrue(possibleDifficulties.containsAll(newPlayerDiffs));
		
		Player goodPlayer1 = new Player("goodPlayer1");
		goodPlayer1.setLosses(10);
		goodPlayer1.setWins(100);
		goodPlayer1.setDraws(0);
		short goodPlayer1Diff = Whitebox.<Short> invokeMethod(this.gameService, "calculateGameDifficulty", goodPlayer1);
		Assert.isTrue(GameService.GAME_DIFFICULTY_MAX == goodPlayer1Diff);
		
		Player goodPlayer2 = new Player("goodPlayer2");
		goodPlayer2.setLosses(0);
		goodPlayer2.setWins(100);
		goodPlayer2.setDraws(10);
		short goodPlayer2Diff = Whitebox.<Short> invokeMethod(this.gameService, "calculateGameDifficulty", goodPlayer2);
		Assert.isTrue(GameService.GAME_DIFFICULTY_MAX == goodPlayer2Diff);
		
		Player badPlayer1 = new Player("badPlayer1");
		badPlayer1.setLosses(100);
		badPlayer1.setWins(10);
		badPlayer1.setDraws(1);
		short badPlayer1Diff = Whitebox.<Short> invokeMethod(this.gameService, "calculateGameDifficulty", badPlayer1);
		Assert.isTrue(GameService.GAME_DIFFICULTY_MIN == badPlayer1Diff);
		
		Player badPlayer2 = new Player("badPlayer2");
		badPlayer2.setLosses(1);
		badPlayer2.setWins(10);
		badPlayer2.setDraws(100);
		short badPlayer2Diff = Whitebox.<Short> invokeMethod(this.gameService, "calculateGameDifficulty", badPlayer2);
		Assert.isTrue(GameService.GAME_DIFFICULTY_MIN == badPlayer2Diff);
		
		Player averagePlayer1 = new Player("averagePlayer1");
		Set<Short> averagePlayer1Diffs = new HashSet<>();
		averagePlayer1.setLosses(10);
		averagePlayer1.setWins(10);
		averagePlayer1.setDraws(10);
		for(int i = 0; i < 100; i++){
			short averagePlayer1Diff = Whitebox.<Short> invokeMethod(this.gameService, "calculateGameDifficulty", averagePlayer1);
			Assert.isTrue(GameService.GAME_DIFFICULTY_MIN <= averagePlayer1Diff);
			Assert.isTrue(GameService.GAME_DIFFICULTY_MAX >= averagePlayer1Diff);
			averagePlayer1Diffs.add(averagePlayer1Diff);
		}
		Assert.isTrue(possibleDifficulties.containsAll(averagePlayer1Diffs));
		
		Player averagePlayer2 = new Player("averagePlayer2");
		Set<Short> averagePlayer2Diffs = new HashSet<>();
		averagePlayer2.setLosses(1);
		averagePlayer2.setWins(100);
		averagePlayer2.setDraws(10);
		for(int i = 0; i < 100; i++){
			short averagePlayer2Diff = Whitebox.<Short> invokeMethod(this.gameService, "calculateGameDifficulty", averagePlayer2);
			Assert.isTrue(GameService.GAME_DIFFICULTY_MIN <= averagePlayer2Diff);
			Assert.isTrue(GameService.GAME_DIFFICULTY_MAX >= averagePlayer2Diff);
			averagePlayer2Diffs.add(averagePlayer2Diff);
		}
		Assert.isTrue(possibleDifficulties.containsAll(averagePlayer2Diffs));
		
		Player averagePlayer3 = new Player("averagePlayer3");
		Set<Short> averagePlayer3Diffs = new HashSet<>();
		averagePlayer3.setLosses(20);
		averagePlayer3.setWins(100);
		averagePlayer3.setDraws(20);
		for(int i = 0; i < 100; i++){
			short averagePlayer3Diff = Whitebox.<Short> invokeMethod(this.gameService, "calculateGameDifficulty", averagePlayer3);
			Assert.isTrue(GameService.GAME_DIFFICULTY_MIN <= averagePlayer3Diff);
			Assert.isTrue(GameService.GAME_DIFFICULTY_MAX >= averagePlayer3Diff);
			averagePlayer3Diffs.add(averagePlayer3Diff);
		}
		Assert.isTrue(possibleDifficulties.containsAll(averagePlayer3Diffs));
	}
	
	@Test
	public void testCalculateGameDifficulty_newGame(){
		int gameId = this.gameService.createNewGame("humanCalculateNewGame", "Computer");
		Game game = this.gameService.getGameStatus(gameId);
		Assert.isTrue(game.getDifficulty() > 0);
	}

}
