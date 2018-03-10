package javachess.game;

import java.io.Serializable;

/**
 * Saves the AI-Moves
 * @author alexl12 - 2017
 * @version 1.1 - Draw
 *
 */
public class MovePos implements Serializable{
	
	/**
	 * X - where the Meeple should move to on X
	 * Y - where the Meeple should move to on Y
	 * PY - where the Meeple came from on Y
	 * PX - where the Meeple came from on X
	 * ID - the ID of the Meeple
	 * ID2 - what was on that tile before it became ,,ID'' 
	 * Beata - for the AI
	 */
	public int X,Y,PY,PX,ID,ID2, Beta;
	
	/**
	 * For En Passant, Rochade,...
	 * third field to Write
	 */
	public int X3, Y3, ID3;
	
	/**
	 * For En Passant, Rochade,...
	 * fourth field to write
	 */
	public int X4, Y4, ID4;
	
	/**
	 * For En Passant, Rochade,...
	 * fifth field to write
	 */
	public int X5, Y5, ID5;
	
	/**
	 * the representation of the board
	 */
	public double[][] Board;	
}
