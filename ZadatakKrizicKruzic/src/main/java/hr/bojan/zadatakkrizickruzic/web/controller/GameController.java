package hr.bojan.zadatakkrizickruzic.web.controller;

import javax.servlet.http.HttpServletResponse;

import hr.bojan.zadatakkrizickruzic.core.model.Game;
import hr.bojan.zadatakkrizickruzic.core.model.exception.IllegalActionException;
import hr.bojan.zadatakkrizickruzic.core.service.GameService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {

	@Autowired
	private GameService gameService;
	
	@RequestMapping(value="game/status", method=RequestMethod.GET)
	public Game getStatus(int gameId){
		Game game = gameService.getGameStatus(gameId);
		return game;
	}
	
	@RequestMapping(value="game/new", method=RequestMethod.GET)
	public int getNewGame(String first, String second){
		return this.gameService.createNewGame(first, second);
	}
	
	@RequestMapping(value="game/play", method=RequestMethod.GET)
	public Game getPlayGame(int gameId, short row, short column, HttpServletResponse response){
		try{
			return this.gameService.playGame(gameId, row, column);
		}catch(IllegalActionException iae){
			response.setStatus(HttpStatus.PRECONDITION_FAILED.value());
			return null;
		}
	}
	
}
