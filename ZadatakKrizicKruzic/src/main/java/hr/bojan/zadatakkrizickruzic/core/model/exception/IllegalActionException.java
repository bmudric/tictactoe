package hr.bojan.zadatakkrizickruzic.core.model.exception;

import hr.bojan.zadatakkrizickruzic.core.model.IllegalAction;

/**
 * @author Bojan
 */
public class IllegalActionException extends RuntimeException {

	private static final long serialVersionUID = 2605961888093573443L;
	
	private IllegalAction illegalAction;
	
	

	public IllegalAction getIllegalAction() {
		return illegalAction;
	}

	public void setIllegalAction(IllegalAction illegalAction) {
		this.illegalAction = illegalAction;
	}

}
