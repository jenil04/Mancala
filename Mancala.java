import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;

/** 
 * Models a Mancala class to initializes mancalas on board and marbles within
 * as data changes.
 * 12/5/17
 */

public class Mancala extends Pit {
    
	/**
     * Constructor for mancalas.
     * @param marbles the number of marbles to start with
     * @param mancalaIndex the index of each pit
     * @param player the player's turn
     * @param style the chosen board style to start with
     */   
    public Mancala(int marbles, int mancalaIndex, int player, BoardStyle style) {
        super(marbles, mancalaIndex, player, style);
    }
    
    /**
	 * Gets the player's turn.
	 * @return the player's turn
	 */
    public int getPlayer() {
        return super.getPlayer();
    }
    
    /**
	 * Sets the number of marbles in the current mancala.
	 * @param marbles the number of marbles to be set to
	 */
    public void setMarbles(int marbles) {
        super.setMarbles(marbles);
    }
    
    /**
	 * Gets the number of marbles in the current mancala
	 * @return the number of mancala's marbles
	 */
    public int getMarbles() {
        return super.getMarbles();
    }
    
    /**
	 * Gets the index of the mancala.
	 * @return the index of each mancala
	 */
    public int getIndex() {
        return super.getIndex();
    }

    /**
	 * Gets the style of the mancala to draw. 
	 * @param style the style of the mancala
	 * @return the chose style for the mancala
	 */
    public Shape drawHolder(BoardStyle style) {
        return style.getMancala(getPlayer());
    }
    
    /**
	 * Draws the number of marbles for each mancala.
	 * @param g the graphics context
	 */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.draw(this.drawHolder(getStyle()));
    }
}
