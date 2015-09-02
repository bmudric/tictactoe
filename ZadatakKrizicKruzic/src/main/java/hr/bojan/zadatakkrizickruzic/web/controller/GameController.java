package hr.bojan.zadatakkrizickruzic.web.controller;

import hr.bojan.zadatakkrizickruzic.core.model.Game;
import hr.bojan.zadatakkrizickruzic.core.service.GameService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {

	@Autowired
	private GameService gameService;
	
	@RequestMapping(value="game/status", method=RequestMethod.GET)
	public Game getStatus(@RequestParam("gameId") int gameId){
		Game game = gameService.getGameStatus(gameId);
		return game;
	}
}
