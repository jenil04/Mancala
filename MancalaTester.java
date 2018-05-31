import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Models a MancalaTester class to gets the options and calls on other classes
 * to display and run the mancala board.
 * 12/5/17
 */

public class MancalaTester {
	public static void main(String[] args) {
		String[] styleString = { "Rectangular", "Circular" };
		String[] marbleString = { "3", "4" };
		JFrame frame = new JFrame("Mancala Game");
		JComboBox style = new JComboBox(styleString);
		JComboBox startingMarbles = new JComboBox(marbleString);
		JLabel instruction = new JLabel("Please select a style and number of marbles to start the game.");
		JButton start = new JButton("Start");

		// Puts in options
		JPanel options = new JPanel();
		options.add(style);
		options.add(startingMarbles);

		// activates action listener.
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				CircularStyle cS = new CircularStyle();
				RectangularStyle rS = new RectangularStyle();
				if (style.getSelectedItem().toString().equals("Rectangular")) {
					Board b = new Board(rS);
					BoardView bV = new BoardView(b);
					b.attach(bV);
					if (startingMarbles.getSelectedItem().toString().equals("3")) {
						b.fillBoard(3);
					} else {
						b.fillBoard(4);
					}
				} else {
					Board b = new Board(cS);
					BoardView bV = new BoardView(b);
					b.attach(bV);
					if (startingMarbles.getSelectedItem().toString().equals("3")) {
						b.fillBoard(3);
					} else {
						b.fillBoard(4);
					}
				}
			}
		});

		// Adds components to the frame
		frame.setSize(400, 200);
		frame.setLayout(new BorderLayout());
		frame.add(instruction, BorderLayout.NORTH);
		frame.add(options, BorderLayout.CENTER);
		frame.add(start, BorderLayout.SOUTH);
		frame.setVisible(true);
		frame.setResizable(false);
	}
}
