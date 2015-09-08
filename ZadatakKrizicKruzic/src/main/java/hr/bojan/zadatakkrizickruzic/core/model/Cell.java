package hr.bojan.zadatakkrizickruzic.core.model;

import lombok.Data;

/**
 * One cell on a tic-tac-toe game board.
 * @author Bojan
 */
@Data
public class Cell {

	private CellValue value;
	
	public Cell() {
		this.value = CellValue.BLANK;
	}
	public Cell(char value) {
		this.value = CellValue.getValueForChar(value);
	}

}
