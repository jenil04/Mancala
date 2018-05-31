import java.awt.Shape;

/**
 * Models a BoardStyle interface for strategy classes: RegularStyle &
 * CircularStyle.
 * 12/5/17 
 */

public interface BoardStyle {	
    /**
     * Gets the chosen style for the pit.
     * @return the pit's shape
     */
	public Shape getPit();
	
    /**
     * Gets the chosen style for the mancala.
     * @param player the player
     * @return the mancala's shape
     */
	public Shape getMancala(int player);
}
