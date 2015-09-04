package hr.bojan.zadatakkrizickruzic.core.model;

/**
 * A player in a game of tic-tac-toe
 * @author Bojan
 */
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
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isComputer() {
		return isComputer;
	}
	public void setComputer(boolean isComputer) {
		this.isComputer = isComputer;
	}
	public int getWins() {
		return wins;
	}
	public void setWins(int wins) {
		this.wins = wins;
	}
	public int getLosses() {
		return losses;
	}
	public void setLosses(int losses) {
		this.losses = losses;
	}
	
}
