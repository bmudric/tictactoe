package hr.bojan.zadatakkrizickruzic.core.model;

/**
 * One cell on a tic-tac-toe game board.
 * @author Bojan
 */
public class Cell {

	private CellValue value;
	
	public Cell() {
		this.value = CellValue.BLANK;
	}

	public CellValue getValue() {
		return value;
	}
	public void setValue(CellValue value) {
		this.value = value;
	}
}
