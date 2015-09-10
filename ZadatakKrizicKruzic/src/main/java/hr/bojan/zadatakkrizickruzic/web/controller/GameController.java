package hr.bojan.zadatakkrizickruzic.web.controller;

import hr.bojan.zadatakkrizickruzic.core.model.Game;
import hr.bojan.zadatakkrizickruzic.core.model.Player;
import hr.bojan.zadatakkrizickruzic.core.model.exception.IllegalActionException;
import hr.bojan.zadatakkrizickruzic.core.service.GameService;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class GameController {

	@Autowired
	private GameService gameService;
	
	// REQUEST HANDLERS
	
	@RequestMapping(value="game/status", method=RequestMethod.GET)
	public Game getStatus(int gameId){
		return this.gameService.getGameStatus(gameId);
	}
	
	@RequestMapping(value="game/new", method=RequestMethod.GET)
	public int getNewGame(String first, String second){
		return this.gameService.createNewGame(first, second);
	}
	
	@RequestMapping(value="game/play", method=RequestMethod.GET)
	public Game getPlayGame(int gameId, int row, int column){
		return this.gameService.playGame(gameId, row, column);
	}
	
	@RequestMapping(value="stats", method=RequestMethod.GET)
	public List<Player> getStats(){
		return this.gameService.loadStats();
	}
	
	// EXCEPTION HANDLERS
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e){
		log.error("IllegalArgumentException caught.", e);
		return new ResponseEntity<String>(e.getMessage(), HttpStatus.PRECONDITION_FAILED);
	}
	
	@ExceptionHandler(IllegalActionException.class)
	public ResponseEntity<String> handleIllegalAction(IllegalActionException e){
		log.error("IllegalActionException caught: " + e.getIllegalAction(), e);
		return new ResponseEntity<String>(e.getIllegalAction().toString(), HttpStatus.PRECONDITION_FAILED);
	}
	
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<String> handleRuntimeException(RuntimeException e){
		log.error("Unknown exception caught.", e);
		return new ResponseEntity<String>("Something went wrong. Sorry.", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
