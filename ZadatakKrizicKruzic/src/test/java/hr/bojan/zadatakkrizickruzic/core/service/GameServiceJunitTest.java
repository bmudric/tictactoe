package hr.bojan.zadatakkrizickruzic.core.service;

import hr.bojan.zadatakkrizickruzic.ZadatakKrizicKruzicApplication;
import hr.bojan.zadatakkrizickruzic.core.model.Cell;
import hr.bojan.zadatakkrizickruzic.core.model.EndgameStatus;
import hr.bojan.zadatakkrizickruzic.core.model.Game;
import hr.bojan.zadatakkrizickruzic.core.model.Move;
import hr.bojan.zadatakkrizickruzic.core.model.Player;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.reflect.Whitebox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
		Assert.assertTrue(gameId > 0);
	}
	
	@Test()
	public void testCreateNewGame_humanFirst2() {
		int gameId = this.gameService.createNewGame("HumanFirst2", "");
		Assert.assertTrue(gameId > 0);
	}
	
	@Test()
	public void testCreateNewGame_humanSecond1() {
		int gameId = this.gameService.createNewGame("Computer", "HumanSecond1");
		Assert.assertTrue(gameId > 0);
	}
	
	@Test()
	public void testCreateNewGame_humanSecond2() {
		int gameId = this.gameService.createNewGame("", "HumanSecond2");
		Assert.assertTrue(gameId > 0);
	}
	
	@Test()
	public void testGetGameStatus() {
		String humanName = "playerGameStatus1";
		int gameId = this.gameService.createNewGame(humanName, "");
		Assert.assertTrue(gameId > 0);
		
		Game game = this.gameService.getGameStatus(gameId);
		Assert.assertTrue(gameId == game.getGameId());
		Assert.assertTrue(humanName.equals(game.getFirstPlayer().getName()));
	}
	
	@Test
	public void testCalculateGameDifficulty() throws Exception{
		Set<Integer> possibleDifficulties = new HashSet<>();
		for(int i = GameService.GAME_DIFFICULTY_MIN; i <= GameService.GAME_DIFFICULTY_MAX; i++){
			possibleDifficulties.add(i);
		}
		
		Set<Integer> newPlayerDiffs = new HashSet<>();
		for(int i = 0; i < 100; i++){
			int newPlayerDiff = invokeCalculateDifficulty(0, 0, 0);
			Assert.assertTrue(GameService.GAME_DIFFICULTY_MIN <= newPlayerDiff);
			Assert.assertTrue(GameService.GAME_DIFFICULTY_MAX >= newPlayerDiff);
			newPlayerDiffs.add(newPlayerDiff);
		}
		Assert.assertTrue(possibleDifficulties.containsAll(newPlayerDiffs));
		
		Assert.assertTrue(GameService.GAME_DIFFICULTY_MAX == invokeCalculateDifficulty(100, 10, 0));
		Assert.assertTrue(GameService.GAME_DIFFICULTY_MAX == invokeCalculateDifficulty(100, 0, 10));
		
		Assert.assertTrue(GameService.GAME_DIFFICULTY_MIN == invokeCalculateDifficulty(10, 100, 1));
		Assert.assertTrue(GameService.GAME_DIFFICULTY_MIN == invokeCalculateDifficulty(10, 1, 100));
		
		Set<Integer> averagePlayer1Diffs = new HashSet<>();
		for(int i = 0; i < 100; i++){
			int averagePlayer1Diff = invokeCalculateDifficulty(10, 10, 10);
			Assert.assertTrue(GameService.GAME_DIFFICULTY_MIN <= averagePlayer1Diff);
			Assert.assertTrue(GameService.GAME_DIFFICULTY_MAX >= averagePlayer1Diff);
			averagePlayer1Diffs.add(averagePlayer1Diff);
		}
		Assert.assertTrue(possibleDifficulties.containsAll(averagePlayer1Diffs));
		
		Set<Integer> averagePlayer2Diffs = new HashSet<>();
		for(int i = 0; i < 100; i++){
			int averagePlayer2Diff = invokeCalculateDifficulty(100, 1, 10);
			Assert.assertTrue(GameService.GAME_DIFFICULTY_MIN <= averagePlayer2Diff);
			Assert.assertTrue(GameService.GAME_DIFFICULTY_MAX >= averagePlayer2Diff);
			averagePlayer2Diffs.add(averagePlayer2Diff);
		}
		Assert.assertTrue(possibleDifficulties.containsAll(averagePlayer2Diffs));
		
		Set<Integer> averagePlayer3Diffs = new HashSet<>();
		for(int i = 0; i < 100; i++){
			int averagePlayer3Diff = invokeCalculateDifficulty(100, 20, 20);
			Assert.assertTrue(GameService.GAME_DIFFICULTY_MIN <= averagePlayer3Diff);
			Assert.assertTrue(GameService.GAME_DIFFICULTY_MAX >= averagePlayer3Diff);
			averagePlayer3Diffs.add(averagePlayer3Diff);
		}
		Assert.assertTrue(possibleDifficulties.containsAll(averagePlayer3Diffs));
	}
	
	private int invokeCalculateDifficulty(int wins, int losses, int draws) throws Exception{
		Player player = new Player("player");
		player.setWins(wins);
		player.setLosses(losses);
		player.setDraws(draws);
		return Whitebox.<Integer> invokeMethod(this.gameService, "calculateGameDifficulty", player);
	}
	
	@Test
	public void testCalculateGameDifficulty_newGame(){
		int gameId = this.gameService.createNewGame("humanCalculateNewGame", "Computer");
		Game game = this.gameService.getGameStatus(gameId);
		Assert.assertTrue(game.getDifficulty() > 0);
	}
	
	@Test
	public void testDetermineEndgameStatus() throws Exception{
		Assert.assertTrue(EndgameStatus.STILL_IN_PROGRESS == invokeDetermineEndgame("_________"));
		Assert.assertTrue(EndgameStatus.STILL_IN_PROGRESS == invokeDetermineEndgame("XO_XO_OX_"));
		Assert.assertTrue(EndgameStatus.STILL_IN_PROGRESS == invokeDetermineEndgame("_XXX_OOO_"));
		Assert.assertTrue(EndgameStatus.STILL_IN_PROGRESS == invokeDetermineEndgame("_OOO_XXX_"));
		Assert.assertTrue(EndgameStatus.STILL_IN_PROGRESS == invokeDetermineEndgame("XXOXXOOO_"));
		Assert.assertTrue(EndgameStatus.STILL_IN_PROGRESS == invokeDetermineEndgame("XOXO_OXOX"));
		Assert.assertTrue(EndgameStatus.STILL_IN_PROGRESS == invokeDetermineEndgame("OXOX_XOXO"));
		
		Assert.assertTrue(EndgameStatus.DRAW == invokeDetermineEndgame("XOXOXOOXO"));
		Assert.assertTrue(EndgameStatus.DRAW == invokeDetermineEndgame("XOXOOXXXO"));
		Assert.assertTrue(EndgameStatus.DRAW == invokeDetermineEndgame("OXXXOOXOX"));
		
		Assert.assertTrue(EndgameStatus.X_WON == invokeDetermineEndgame("XXXXXXXXX"));
		Assert.assertTrue(EndgameStatus.X_WON == invokeDetermineEndgame("X__X__X__"));
		Assert.assertTrue(EndgameStatus.X_WON == invokeDetermineEndgame("_X__X__X_"));
		Assert.assertTrue(EndgameStatus.X_WON == invokeDetermineEndgame("__X__X__X"));
		Assert.assertTrue(EndgameStatus.X_WON == invokeDetermineEndgame("XXX______"));
		Assert.assertTrue(EndgameStatus.X_WON == invokeDetermineEndgame("___XXX___"));
		Assert.assertTrue(EndgameStatus.X_WON == invokeDetermineEndgame("______XXX"));
		Assert.assertTrue(EndgameStatus.X_WON == invokeDetermineEndgame("X___X___X"));
		Assert.assertTrue(EndgameStatus.X_WON == invokeDetermineEndgame("__X_X_X__"));
		Assert.assertTrue(EndgameStatus.X_WON == invokeDetermineEndgame("XOX_OXO_X"));
		Assert.assertTrue(EndgameStatus.X_WON == invokeDetermineEndgame("XOOOXXOXX"));
		Assert.assertTrue(EndgameStatus.X_WON == invokeDetermineEndgame("XOXOX_XO_"));
		Assert.assertTrue(EndgameStatus.X_WON == invokeDetermineEndgame("_OX_XOX__"));
		Assert.assertTrue(EndgameStatus.X_WON == invokeDetermineEndgame("XOXOXOXOX"));
		Assert.assertTrue(EndgameStatus.X_WON == invokeDetermineEndgame("XXXXOOXOO"));
		
		Assert.assertTrue(EndgameStatus.O_WON == invokeDetermineEndgame("OOOOOOOOO"));
		Assert.assertTrue(EndgameStatus.O_WON == invokeDetermineEndgame("O__O__O__"));
		Assert.assertTrue(EndgameStatus.O_WON == invokeDetermineEndgame("_O__O__O_"));
		Assert.assertTrue(EndgameStatus.O_WON == invokeDetermineEndgame("__O__O__O"));
		Assert.assertTrue(EndgameStatus.O_WON == invokeDetermineEndgame("OOO______"));
		Assert.assertTrue(EndgameStatus.O_WON == invokeDetermineEndgame("___OOO___"));
		Assert.assertTrue(EndgameStatus.O_WON == invokeDetermineEndgame("______OOO"));
		Assert.assertTrue(EndgameStatus.O_WON == invokeDetermineEndgame("O___O___O"));
		Assert.assertTrue(EndgameStatus.O_WON == invokeDetermineEndgame("__O_O_O__"));
		Assert.assertTrue(EndgameStatus.O_WON == invokeDetermineEndgame("OXO_XOX_O"));
		Assert.assertTrue(EndgameStatus.O_WON == invokeDetermineEndgame("OXXXOOXOO"));
		Assert.assertTrue(EndgameStatus.O_WON == invokeDetermineEndgame("OXOXO_OX_"));
		Assert.assertTrue(EndgameStatus.O_WON == invokeDetermineEndgame("_XO_OXO__"));
		Assert.assertTrue(EndgameStatus.O_WON == invokeDetermineEndgame("OXOXOXOXO"));
		Assert.assertTrue(EndgameStatus.O_WON == invokeDetermineEndgame("XXOXXOOOO"));
	}
	
	private EndgameStatus invokeDetermineEndgame(String gameBoard) throws Exception{
		Cell[][] gameCells = Game.createGameBoard(gameBoard);
		return Whitebox.invokeMethod(this.gameService, "determineEndgameStatus", (Object) gameCells);
	}
	
	@Test
	public void testIsMoveLegal() throws Exception{
		Assert.assertTrue(invokeMoveLegal("_________", 0, 0));
		Assert.assertTrue(invokeMoveLegal("_________", 1, 1));
		Assert.assertTrue(invokeMoveLegal("_________", 2, 2));
		
		Assert.assertTrue(!invokeMoveLegal("_________", 3, 3));
		Assert.assertTrue(!invokeMoveLegal("_________", 0, 3));
		Assert.assertTrue(!invokeMoveLegal("_________", 3, 0));
		Assert.assertTrue(!invokeMoveLegal("_________", -1, -1));
		Assert.assertTrue(!invokeMoveLegal("_________", -1, 1));
		Assert.assertTrue(!invokeMoveLegal("_________", 1, -1));
		
		Assert.assertTrue(!invokeMoveLegal("X________", 0, 0));
		Assert.assertTrue(!invokeMoveLegal("O________", 0, 0));
		
		Assert.assertTrue(invokeMoveLegal("X________", 1, 0));
		Assert.assertTrue(invokeMoveLegal("O________", 0, 1));
		Assert.assertTrue(invokeMoveLegal("O________", 1, 1));
	}

	private boolean invokeMoveLegal(String gameBoard, int row, int column) throws Exception{
		Cell[][] gameCells = Game.createGameBoard(gameBoard);
		return Whitebox.invokeMethod(this.gameService, "isMoveLegal", (Object) gameCells, row, column);
	}
	
	@Test
	public void testCalculateMove_diff2() throws Exception{
		testCalculateMove("XX______O", 2, 0, true, new Move(0, 2, 0));
		testCalculateMove("__X_X__O_", 2, 0, true, new Move(2, 0, 0));
		testCalculateMove("_X_OO__X_", 2, 0, true, new Move(1, 2, 9));
		testNotCalculateMove("XO__X_X_O", 2, 0, true);
	}
	
	@Test
	public void testCalculateMove_diff3() throws Exception{
		testCalculateMove("XX______O", 3, 0, true, new Move(0, 2, 0));
		testCalculateMove("__X_X__O_", 3, 0, true, new Move(2, 0, 0));
		testCalculateMove("_X_OO__X_", 3, 0, true, new Move(1, 2, 9));
		testNotCalculateMove("XO__X_X_O", 3, 0, true);
	}
	
	public void testCalculateMove(String gameBoard, int difficulty, int depth, boolean computerTurn, Move exp) throws Exception{
		Move res = invokeCalculateMove(gameBoard, difficulty, depth, true);
		Assert.assertEquals(exp, res);
	}
	
	public void testNotCalculateMove(String gameBoard, int difficulty, int depth, boolean computerTurn) throws Exception{
		Move res = invokeCalculateMove(gameBoard, difficulty, depth, true);
		Assert.assertNotEquals(new Move(-1, -1, 0), res);
	}
	
	public Move invokeCalculateMove(String gameBoard, int difficulty, int depth, boolean computerTurn) throws Exception{
		Cell[][] gameCells = Game.createGameBoard(gameBoard);
		return Whitebox.invokeMethod(this.gameService, "calculateMove", (Object) gameCells, difficulty, depth, computerTurn);
	}
}
