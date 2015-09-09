package hr.bojan.zadatakkrizickruzic.core.model;

import hr.bojan.zadatakkrizickruzic.ZadatakKrizicKruzicApplication;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ZadatakKrizicKruzicApplication.class)
public class GameJunitTest {

	@Test
	public void testGameBoardTranslation() {
		gameBoardTranslationTest("_________");
		gameBoardTranslationTest("XO_XO_OX_");
		gameBoardTranslationTest("_XXX_OOO_");
		gameBoardTranslationTest("_OOO_XXX_");
		gameBoardTranslationTest("XXOXXOOO_");
		gameBoardTranslationTest("XOXO_OXOX");
		gameBoardTranslationTest("OXOX_XOXO");
		gameBoardTranslationTest("XOXOXOOXO");
		gameBoardTranslationTest("XOXOOXXXO");
		gameBoardTranslationTest("OXXXOOXOX");
		gameBoardTranslationTest("XXXXXXXXX");
		gameBoardTranslationTest("X__X__X__");
		gameBoardTranslationTest("_X__X__X_");
		gameBoardTranslationTest("__X__X__X");
		gameBoardTranslationTest("XXX______");
		gameBoardTranslationTest("___XXX___");
		gameBoardTranslationTest("______XXX");
		gameBoardTranslationTest("X___X___X");
		gameBoardTranslationTest("__X_X_X__");
		gameBoardTranslationTest("XOX_OXO_X");
		gameBoardTranslationTest("XOOOXXOXX");
		gameBoardTranslationTest("XOXOX_XO_");
		gameBoardTranslationTest("_OX_XOX__");
		gameBoardTranslationTest("XOXOXOXOX");
		gameBoardTranslationTest("XXXXOOXOO");
	}

	private void gameBoardTranslationTest(String boardValues){
		Cell[][] board =  Game.createGameBoard(boardValues);
		Assert.assertNotNull(board);
		String reverseTranslation = Game.translateGameBoard(board);
		Assert.assertEquals(boardValues, reverseTranslation);
	}
}
