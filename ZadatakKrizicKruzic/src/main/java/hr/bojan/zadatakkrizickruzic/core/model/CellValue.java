package hr.bojan.zadatakkrizickruzic.core.model;

/**
 * Possible cell values in a game of tic-tac-toe
 * @author Bojan
 */
public enum CellValue {
	O('O'),
	X('X'),
	BLANK('_');
	
	public static CellValue getValueForChar(char value){
		for(CellValue cv : values()){
			if(cv.value == value){
				return cv;
			}
		}
		throw new IllegalArgumentException("Nonexistant cell value.");
	}
	
	private CellValue(char value){
		this.value = value;
	}
	
	private char value;
}
