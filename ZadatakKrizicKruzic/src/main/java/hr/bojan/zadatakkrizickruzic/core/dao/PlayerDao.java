package hr.bojan.zadatakkrizickruzic.core.dao;

import hr.bojan.zadatakkrizickruzic.core.model.Player;

public interface PlayerDao {

	/**
	 * @param playerName
	 * @return player for playerName or null if no such player exists
	 */
	public Player loadPlayerByName(String playerName);
	
	/**
	 * @param player
	 * @return true if player inserted, false if player with that name already exists
	 */
	public boolean insertNewPlayer(Player player);
}
