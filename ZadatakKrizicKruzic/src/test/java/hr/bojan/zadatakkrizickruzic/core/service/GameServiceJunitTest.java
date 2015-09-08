package hr.bojan.zadatakkrizickruzic.core.service;

import hr.bojan.zadatakkrizickruzic.ZadatakKrizicKruzicApplication;
import hr.bojan.zadatakkrizickruzic.core.model.Cell;
import hr.bojan.zadatakkrizickruzic.core.model.EndgameStatus;
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
		
		Set<Short> newPlayerDiffs = new HashSet<>();
		for(int i = 0; i < 100; i++){
			short newPlayerDiff = invokeCalculateDifficulty(0, 0, 0);
			Assert.isTrue(GameService.GAME_DIFFICULTY_MIN <= newPlayerDiff);
			Assert.isTrue(GameService.GAME_DIFFICULTY_MAX >= newPlayerDiff);
			newPlayerDiffs.add(newPlayerDiff);
		}
		Assert.isTrue(possibleDifficulties.containsAll(newPlayerDiffs));
		
		Assert.isTrue(GameService.GAME_DIFFICULTY_MAX == invokeCalculateDifficulty(100, 10, 0));
		Assert.isTrue(GameService.GAME_DIFFICULTY_MAX == invokeCalculateDifficulty(100, 0, 10));
		
		Assert.isTrue(GameService.GAME_DIFFICULTY_MIN == invokeCalculateDifficulty(10, 100, 1));
		Assert.isTrue(GameService.GAME_DIFFICULTY_MIN == invokeCalculateDifficulty(10, 1, 100));
		
		Set<Short> averagePlayer1Diffs = new HashSet<>();
		for(int i = 0; i < 100; i++){
			short averagePlayer1Diff = invokeCalculateDifficulty(10, 10, 10);
			Assert.isTrue(GameService.GAME_DIFFICULTY_MIN <= averagePlayer1Diff);
			Assert.isTrue(GameService.GAME_DIFFICULTY_MAX >= averagePlayer1Diff);
			averagePlayer1Diffs.add(averagePlayer1Diff);
		}
		Assert.isTrue(possibleDifficulties.containsAll(averagePlayer1Diffs));
		
		Set<Short> averagePlayer2Diffs = new HashSet<>();
		for(int i = 0; i < 100; i++){
			short averagePlayer2Diff = invokeCalculateDifficulty(100, 1, 10);
			Assert.isTrue(GameService.GAME_DIFFICULTY_MIN <= averagePlayer2Diff);
			Assert.isTrue(GameService.GAME_DIFFICULTY_MAX >= averagePlayer2Diff);
			averagePlayer2Diffs.add(averagePlayer2Diff);
		}
		Assert.isTrue(possibleDifficulties.containsAll(averagePlayer2Diffs));
		
		Set<Short> averagePlayer3Diffs = new HashSet<>();
		for(int i = 0; i < 100; i++){
			short averagePlayer3Diff = invokeCalculateDifficulty(100, 20, 20);
			Assert.isTrue(GameService.GAME_DIFFICULTY_MIN <= averagePlayer3Diff);
			Assert.isTrue(GameService.GAME_DIFFICULTY_MAX >= averagePlayer3Diff);
			averagePlayer3Diffs.add(averagePlayer3Diff);
		}
		Assert.isTrue(possibleDifficulties.containsAll(averagePlayer3Diffs));
	}
	
	private short invokeCalculateDifficulty(int wins, int losses, int draws) throws Exception{
		Player player = new Player("player");
		player.setWins(wins);
		player.setLosses(losses);
		player.setDraws(draws);
		return Whitebox.<Short> invokeMethod(this.gameService, "calculateGameDifficulty", player);
	}
	
	@Test
	public void testCalculateGameDifficulty_newGame(){
		int gameId = this.gameService.createNewGame("humanCalculateNewGame", "Computer");
		Game game = this.gameService.getGameStatus(gameId);
		Assert.isTrue(game.getDifficulty() > 0);
	}
	
	@Test
	public void testDetermineEndgameStatus() throws Exception{
		Assert.isTrue(EndgameStatus.STILL_IN_PROGRESS == invokeDetermineEndgame("_________"));
		Assert.isTrue(EndgameStatus.STILL_IN_PROGRESS == invokeDetermineEndgame("XO_XO_OX_"));
		Assert.isTrue(EndgameStatus.STILL_IN_PROGRESS == invokeDetermineEndgame("_XXX_OOO_"));
		Assert.isTrue(EndgameStatus.STILL_IN_PROGRESS == invokeDetermineEndgame("_OOO_XXX_"));
		Assert.isTrue(EndgameStatus.STILL_IN_PROGRESS == invokeDetermineEndgame("XXOXXOOO_"));
		
		Assert.isTrue(EndgameStatus.DRAW == invokeDetermineEndgame("XOXOXOOXO"));
		Assert.isTrue(EndgameStatus.DRAW == invokeDetermineEndgame("XOXOOXXXO"));
		Assert.isTrue(EndgameStatus.DRAW == invokeDetermineEndgame("OXXXOOXOX"));
		
		Assert.isTrue(EndgameStatus.X_WON == invokeDetermineEndgame("XXXXXXXXX"));
		Assert.isTrue(EndgameStatus.X_WON == invokeDetermineEndgame("X__X__X__"));
		Assert.isTrue(EndgameStatus.X_WON == invokeDetermineEndgame("_X__X__X_"));
		Assert.isTrue(EndgameStatus.X_WON == invokeDetermineEndgame("__X__X__X"));
		Assert.isTrue(EndgameStatus.X_WON == invokeDetermineEndgame("XXX______"));
		Assert.isTrue(EndgameStatus.X_WON == invokeDetermineEndgame("___XXX___"));
		Assert.isTrue(EndgameStatus.X_WON == invokeDetermineEndgame("______XXX"));
		Assert.isTrue(EndgameStatus.X_WON == invokeDetermineEndgame("X___X___X"));
		Assert.isTrue(EndgameStatus.X_WON == invokeDetermineEndgame("__X_X_X__"));
		Assert.isTrue(EndgameStatus.X_WON == invokeDetermineEndgame("XOX_OXO_X"));
		Assert.isTrue(EndgameStatus.X_WON == invokeDetermineEndgame("XOOOXXOXX"));
		Assert.isTrue(EndgameStatus.X_WON == invokeDetermineEndgame("XOXOX_XO_"));
		Assert.isTrue(EndgameStatus.X_WON == invokeDetermineEndgame("_OX_XOX__"));
		
		Assert.isTrue(EndgameStatus.O_WON == invokeDetermineEndgame("OOOOOOOOO"));
		Assert.isTrue(EndgameStatus.O_WON == invokeDetermineEndgame("O__O__O__"));
		Assert.isTrue(EndgameStatus.O_WON == invokeDetermineEndgame("_O__O__O_"));
		Assert.isTrue(EndgameStatus.O_WON == invokeDetermineEndgame("__O__O__O"));
		Assert.isTrue(EndgameStatus.O_WON == invokeDetermineEndgame("OOO______"));
		Assert.isTrue(EndgameStatus.O_WON == invokeDetermineEndgame("___OOO___"));
		Assert.isTrue(EndgameStatus.O_WON == invokeDetermineEndgame("______OOO"));
		Assert.isTrue(EndgameStatus.O_WON == invokeDetermineEndgame("O___O___O"));
		Assert.isTrue(EndgameStatus.O_WON == invokeDetermineEndgame("__O_O_O__"));
		Assert.isTrue(EndgameStatus.O_WON == invokeDetermineEndgame("OXO_XOX_O"));
		Assert.isTrue(EndgameStatus.O_WON == invokeDetermineEndgame("OXXXOOXOO"));
		Assert.isTrue(EndgameStatus.O_WON == invokeDetermineEndgame("OXOXO_OX_"));
		Assert.isTrue(EndgameStatus.O_WON == invokeDetermineEndgame("_XO_OXO__"));
	}
	
	private EndgameStatus invokeDetermineEndgame(String gameBoard) throws Exception{
		Cell[][] gameCells = Game.createGameBoard(gameBoard);
		return Whitebox.invokeMethod(this.gameService, "determineEndgameStatus", (Object) gameCells);
	}
	
	@Test
	public void testIsMoveLegal() throws Exception{
		Assert.isTrue(invokeMoveLegal("_________", 0, 0));
		Assert.isTrue(invokeMoveLegal("_________", 1, 1));
		Assert.isTrue(invokeMoveLegal("_________", 2, 2));
		
		Assert.isTrue(!invokeMoveLegal("_________", 3, 3));
		Assert.isTrue(!invokeMoveLegal("_________", 0, 3));
		Assert.isTrue(!invokeMoveLegal("_________", 3, 0));
		Assert.isTrue(!invokeMoveLegal("_________", -1, -1));
		Assert.isTrue(!invokeMoveLegal("_________", -1, 1));
		Assert.isTrue(!invokeMoveLegal("_________", 1, -1));
		
		Assert.isTrue(!invokeMoveLegal("X________", 0, 0));
		Assert.isTrue(!invokeMoveLegal("O________", 0, 0));
		
		Assert.isTrue(invokeMoveLegal("X________", 1, 0));
		Assert.isTrue(invokeMoveLegal("O________", 0, 1));
		Assert.isTrue(invokeMoveLegal("O________", 1, 1));
	}

	private boolean invokeMoveLegal(String gameBoard, int row, int column) throws Exception{
		Cell[][] gameCells = Game.createGameBoard(gameBoard);
		return Whitebox.invokeMethod(this.gameService, "isMoveLegal", (Object) gameCells, (short) row, (short) column);
	}
}
