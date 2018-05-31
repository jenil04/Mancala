import java.util.ArrayList;
import java.util.Random;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Models a Board class to gives the game functionalities,
 * keeps track of statues for both and checks who wins when game ends.
 * @author Team Green: Yen Huynh, Samantha Ignacio, & Anthony Minaise
 * 12/5/17
 */
public class Board {

	private int player;
	private boolean extraTurn;
	private ArrayList<ChangeListener> listeners;
	private int undoCount;
	private int[] previousState;
	private ArrayList<Pit> pits;
	private boolean redo;
	static final int PLAYER_ONE_MAX_INDEX = 6;
	static final int PLAYER_TWO_MAX_INDEX = 13;

	/**
	 * Board class constructor - creates a starting board initializing empty
	 * "pits" or containers with no marbles
	 * @param style a concrete implementation of BoardStyle determining the
	 * shape of the pits to be used in the game
	 */
	public Board(BoardStyle style) {
		redo = false;
		undoCount = 0;
		previousState = new int[14];
		Random gen = new Random();
		player = gen.nextInt(2) + 1;
		extraTurn = false;
		pits = new ArrayList<Pit>();
		// adds marbles to the pit when there's marbles present for player one
		for (int i = 0; i < PLAYER_ONE_MAX_INDEX; i++) {
			pits.add(new Pit(0, i, 1, style));
		}
		// adds marbles to the pit when no marbles are present for player one
		pits.add(new Mancala(0, PLAYER_ONE_MAX_INDEX, 1, style));
		// adds marbles to the pit when there's marbles present for player two
		for (int i = 7; i < PLAYER_TWO_MAX_INDEX; i++) {
			pits.add(new Pit(0, i, 2, style));
		}
		// adds marbles to the pit when no marbles are present for player two
		pits.add(new Mancala(0, PLAYER_TWO_MAX_INDEX, 2, style));

		listeners = new ArrayList<ChangeListener>();
	}

	/**
	 * Gets the current player's turn.
	 * @return the current player's turn
	 */
	public String getPlayer() {
		String turn = "";
		if (player == 1){
			turn =  "Player 1 --->";
		}
		else{
			turn = "<--- Player 2";
		}
		return turn;
	}
	
	/**
	 * Gets the list of all pits.
	 * @return a list of pits.
	 */
	public ArrayList<Pit> getData() {
		return pits;
	}
	
	/**
	 * Gets the mancala of the current player.
	 * @param player the current player
	 * @return the mancala of the current player
	 */
	public Mancala getMancala(int player) {
		for (Pit p : pits) {
			if (p instanceof Mancala && p.getPlayer() == player) {
				return (Mancala) p;
			}
		}
		return null;
	}

	/**
	 * Gets the index of the pit that the last marble lands in on the board.	
	 * @param pit the current pit that the last marble lands in
	 * @return the current pit index that the last marble lands in
	 */
	public int getLastMarble(Pit pit) {
		int numberOfMarbles = pit.getMarbles();
		int currentIndex = pit.getIndex();
		while (numberOfMarbles > 0) {
			if (currentIndex == 5 && player == 2) {
				currentIndex += 2;
			} else if (currentIndex == 12 && player == 1) {
				currentIndex += 2;
			} else {
				currentIndex++;
			}
			if (currentIndex == 14) {
				currentIndex = 0;
			}
			numberOfMarbles--;
		}
		return currentIndex;
	}

	/**
	 * Attaches listeners to the list of listeners for this model.
	 * @param listener ChangeListeners that notify of changes of the model data for view
	 */
	public void attach(ChangeListener listener) {
		listeners.add(listener);
	}

	/**
	 * Fills the board with starting number of marbles on each pit.
	 * @param marbles the number of marbles per pit.
	 */

	public void fillBoard(int marbles) {
		int counter = 0;
		for (Pit p : pits) {
			if (!(p instanceof Mancala)) {
				p.setMarbles(marbles);
				previousState[counter] = marbles;
			} else {
				previousState[counter] = 0;
				counter++;
			}
		}
		ChangeEvent event = new ChangeEvent(this);
		for (ChangeListener listener : listeners) {
			listener.stateChanged(event);
		}
	}
	
	/**
	 * Distributes marbles of the chosen pit up until no more marbles 
	 * for the current player's turn.
	 * @param startIndex the chosen index to distribute marbles
	 * @param marbles the number of marbles to distribute
	 */
	public void distributeMarbles(int startIndex, int marbles) {
		int numberOfMarbles = marbles;
		int currentIndex = startIndex;

		while (numberOfMarbles > 0) {
			if (currentIndex == 5 && player == 2) {
				currentIndex += 2;
			} else if (currentIndex == 12 && player == 1) {
				currentIndex = 0;
			} else {
				currentIndex++;
			}
			if (currentIndex == 14) {
				currentIndex = 0;
			}
			pits.get(currentIndex).setMarbles(pits.get(currentIndex).getMarbles() + 1);
			numberOfMarbles--;
		}
	}

	/**
	 * Gives the current player all the marbles if their last marble
	 * lands on an empty pit.
	 * @param lastMarbleDroppedIndex the index of the pit where the last marble dropped
	 */
	private void wonOpponentMarbles(int lastMarbleDroppedIndex) {
		int mancala = 6;
		if (lastMarbleDroppedIndex == PLAYER_ONE_MAX_INDEX || lastMarbleDroppedIndex == PLAYER_TWO_MAX_INDEX)
			return;
		else if (pits.get(lastMarbleDroppedIndex).getMarbles() == 1 && pits.get(lastMarbleDroppedIndex).getPlayer() == player) {
			if (pits.get(12 - lastMarbleDroppedIndex).getMarbles() == 0)
				return;
			if (player == 1)
				mancala = 6;
			else
				mancala = 13;

			pits.get(mancala)
					.setMarbles(pits.get(mancala).getMarbles() + pits.get(12 - lastMarbleDroppedIndex).getMarbles());
			pits.get(mancala).setMarbles(pits.get(mancala).getMarbles() + pits.get(lastMarbleDroppedIndex).getMarbles());
			pits.get(12 - lastMarbleDroppedIndex).setMarbles(0);
			pits.get(lastMarbleDroppedIndex).setMarbles(0);
		}
	}

	/**
	 * Starts activating multiple actions when current player chooses a pit.
	 * @param pit the chosen pit by the player.
	 */
	public void choosePit(Pit pit) {
		if (!redo)
			undoCount = 0;
		if (pit.getPlayer() != player)
			return;
		if (pit.getMarbles() == 0)
			return;
		for (Pit p : pits) {
			previousState[p.getIndex()] = p.getMarbles();
		}

		extraTurn = getExtraTurn(pit);
		int lastMarbleDropped = getLastMarble(pit);
		int numberOfMarbles = pit.getMarbles();
		pit.setMarbles(0);

		distributeMarbles(pit.getIndex(), numberOfMarbles);
		wonOpponentMarbles(lastMarbleDropped);
		if (gameOver())
			clearBoard();
		if (!extraTurn) {
			switchPlayer();
		}
		redo = false;
		ChangeEvent event = new ChangeEvent(this);
		for (ChangeListener listener : listeners) {
			listener.stateChanged(event);
		}
	}

	/**
	 * Switches turn between the two players.
	 */
	private void switchPlayer() {
		if (player == 1) {
			player = 2;
		} else {
			player = 1;
		}
		extraTurn = false;
	}
	
	/**
	 * Undoes an action if have previous state and valid undo's attempts leftover.
	 */
	public void undo() {
		if (!canUndo()) {
			System.out.println("You are not allowed to Undo anymore.");
			return;
		}

		redo = true;

		undoCount++;
		for (Pit p : pits)
			p.setMarbles(previousState[p.getIndex()]);

		if (!extraTurn)
			switchPlayer();

		ChangeEvent event = new ChangeEvent(this);
		for (ChangeListener listener : listeners) {
			listener.stateChanged(event);
		}
	}
	
	/**
	 * Clears all the pits on the board, no more marbles on all pits.
	 */
	private void clearBoard() {
		for (int i = 0; i < PLAYER_ONE_MAX_INDEX; i++) {
			pits.get(6).setMarbles(pits.get(6).getMarbles() + pits.get(i).getMarbles());
			pits.get(i).setMarbles(0);
		}
		for (int i = 7; i < PLAYER_TWO_MAX_INDEX; i++) {
			pits.get(13).setMarbles(pits.get(13).getMarbles() + pits.get(i).getMarbles());
			pits.get(i).setMarbles(0);
		}
	}
	
	/**
	 * Checks to see if the current player gets an extra turn depending on
	 * if the last marble lands on the current player's mancala.
	 * @param pit the chosen pit by the player    
	 * @return true if the player gets an extra turn, otherwise false
	 */
	private boolean getExtraTurn(Pit pit) {
		int totalMoves = pit.getIndex() + pit.getMarbles();
		if ((totalMoves - 6) % 13 == 0 && player == 1) {
			return true;
		}
		if ((totalMoves - 13) % 13 == 0 && player == 2) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if the game's over -- when either player's pits are all empty.
	 * @return true if the game's over, otherwise false
	 */
	public boolean gameOver() {
		boolean emptyRow = true;
		for (int i = 0; i < PLAYER_ONE_MAX_INDEX; i++) {
			if (pits.get(i).getMarbles() != 0) {
				emptyRow = false;
			}
		}
		if (emptyRow) {
			return emptyRow;
		}

		emptyRow = true;

		for (int i = 7; i < PLAYER_TWO_MAX_INDEX; i++) {
			if (pits.get(i).getMarbles() != 0) {
				emptyRow = false;
			}
		}

		return emptyRow;
	}

	/**
	 * Checks if the player have moved or not.
	 * @return true if there is a previous record of moving
	 */
	private boolean noPreviousState() {
		for (Pit p : pits)
			if (p.getMarbles() != previousState[p.getIndex()])
				return false;
		return true;
	}

	/**
	 * Checks if the player have used up all undo's attempts or haven't move.
	 * @return true if the player can undo, otherwise false
	 */
	private boolean canUndo() {
		if (gameOver())
			return false;

		if (noPreviousState()) {
			System.out.println("You haven't make any move yet.");
			return false;
		}

		if (undoCount >= 3) {
			System.out.println("You've used all undo's attempts.");
			return false;
		}
		return true;
	}
}  