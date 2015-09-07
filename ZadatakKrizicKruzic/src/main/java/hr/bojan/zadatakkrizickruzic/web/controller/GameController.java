package hr.bojan.zadatakkrizickruzic.web.controller;

import hr.bojan.zadatakkrizickruzic.core.model.exception.IllegalActionException;
import hr.bojan.zadatakkrizickruzic.core.service.GameService;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {

	@Autowired
	private GameService gameService;
	
	private final String MESSAGE_SOMETHING_WENT_WRONG = "Something went wrong. Sorry.";
	
	@RequestMapping(value="game/status", method=RequestMethod.GET)
	public Object getStatus(int gameId, HttpServletResponse response){
		try{
			return this.gameService.getGameStatus(gameId);
		}catch(IllegalArgumentException iae){
			response.setStatus(HttpStatus.PRECONDITION_FAILED.value());
			return iae.getMessage();
		}catch(RuntimeException re){
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return MESSAGE_SOMETHING_WENT_WRONG;
		}
	}
	
	@RequestMapping(value="game/new", method=RequestMethod.GET)
	public Object getNewGame(String first, String second, HttpServletResponse response){
		try{
			return this.gameService.createNewGame(first, second);
		}catch(IllegalArgumentException iae){
			response.setStatus(HttpStatus.PRECONDITION_FAILED.value());
			return iae.getMessage();
		}catch(RuntimeException re){
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return MESSAGE_SOMETHING_WENT_WRONG;
		}
	}
	
	@RequestMapping(value="game/play", method=RequestMethod.GET)
	public Object getPlayGame(int gameId, short row, short column, HttpServletResponse response){
		try{
			return this.gameService.playGame(gameId, row, column);
		}catch(IllegalArgumentException iae){
			response.setStatus(HttpStatus.PRECONDITION_FAILED.value());
			return iae.getMessage();
		}catch(IllegalActionException iae){
			response.setStatus(HttpStatus.PRECONDITION_FAILED.value());
			return iae.getIllegalAction();
		}catch(RuntimeException re){
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return MESSAGE_SOMETHING_WENT_WRONG;
		}
	}
	
	
	
}
