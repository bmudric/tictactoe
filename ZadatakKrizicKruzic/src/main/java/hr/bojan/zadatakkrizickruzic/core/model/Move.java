package hr.bojan.zadatakkrizickruzic.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a move on a game board and it's score
 * @author Bojan
 */
@Data
@AllArgsConstructor
public class Move {

	private int row;
	private int col;
	private int score;
}
