package hr.bojan.zadatakkrizickruzic.core.model;

import lombok.Data;

/**
 * A player in a game of tic-tac-toe
 * @author Bojan
 */
@Data
public class Player {
	
	public static final Player COMPUTER = new Player("Computer", true);

	private String name;
	private boolean isComputer;
	private int wins;
	private int losses;
	
	public Player(String name) {
		this.name = name;
	}
	public Player(String name, boolean isComputer){
		this(name);
		this.isComputer = isComputer;
	}
	
}
