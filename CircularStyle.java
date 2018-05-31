import java.awt.Shape;
import java.awt.geom.Ellipse2D;

/** 
 * Models a strategy class for circular style. 
 * 12/5/17
 */

public class CircularStyle implements BoardStyle{

	/**
	 * Creates a circular pit.
	 * @return a circular pit of 120x140
	 */
	public Shape getPit() {
		return new Ellipse2D.Double(0, 0, 120, 140);
	}

	/**
	 * Creates a circular mancala for the board.
	 * @param player the player
	 * @return a circular mancala of 120x400
	 */
	public Shape getMancala(int player) {
		return new Ellipse2D.Double(0, 0, 120, 400);
	}
}
