import java.awt.Shape;
import java.awt.geom.Rectangle2D;

/**
 * Models a strategy class for rectangular style.
 * 12/5/17 
 */

public class RectangularStyle implements BoardStyle {

	/**
	 * Creates a rectangular pit.
	 * @return a rectangular pit of 120x140
	 */
	public Shape getPit() {
		return new Rectangle2D.Double(0, 0, 120, 140);
	}

	/**
	 * Creates a rectangular mancala for the board.
	 * @param player the player
	 * @return a rectangular mancala of 120x400
	 */
	public Shape getMancala(int player) {
		return new Rectangle2D.Double(0, 0, 120, 400);
	}
}
