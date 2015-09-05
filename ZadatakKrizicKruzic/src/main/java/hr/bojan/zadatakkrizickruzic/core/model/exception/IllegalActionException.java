package hr.bojan.zadatakkrizickruzic.core.model.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import hr.bojan.zadatakkrizickruzic.core.model.IllegalAction;

/**
 * Thrown when trying to execute an illegal action in a game of tic-tac-toe
 * @author Bojan
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class IllegalActionException extends RuntimeException {

	private static final long serialVersionUID = 2605961888093573443L;
	
	@NonNull private IllegalAction illegalAction;
	
}
