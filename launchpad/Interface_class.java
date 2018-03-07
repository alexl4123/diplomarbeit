package launchpad;

import Gui.BoardGui;
import javachess.backgroundmatrix.*;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Interface_class {
	//getboard return array
	//getfig(x,y) return fig
	//movefig(x,y,x2,y2) return boolean
	//ismyturn return boolean
	
	
	private BackgroundGrid _BGG;
	
	private BoardGui _BG;
	
	//Needed for event listener
	public IntegerProperty iCount = new SimpleIntegerProperty(0);
	
	public void interface_class() {
		iCount.set(0);
	}
	
	/**
	 * simply returns the board
	 * @return int[][] - the board in the int[][] matrix
	 */
	public int[][] getboard(){
		
		
		
		
		return _BGG.Board;
	}
	
	/**
	 * returns the ID of the meeple
	 * @param x-Coord of meeple
	 * @param y-Coord of meeple
	 * @return int ID - ID of meeple
	 */
	public int getMeepleID(int x, int y) {
		return _BGG.getBackgroundGrid(x,y);
	}
	
	/**
	 * When the Launchpad wants to move a fig.
	 * @param x-X Coord of starting pos
	 * @param y-Y Coord of starting pos
	 * @param x2-X coord of moving pos
	 * @param y2-Y Coord of moving pos
	 * @return boolean - if move possible - true
	 */
	public boolean moveFig(int x, int y, int x2, int y2) {
		Move move = new Move();

		int ID = getMeepleID(x,y);
		
		move.setBSelect(true);
		move.setIPosX(x);
		move.setIPosY(y);
		move.setLastID(ID);
		move.setBGG(_BGG.iBackground);
		
		//move meeple from x&y to x2&y2
		move.GetMove(getMeepleID(x2, y2), x2, y2, _BGG);
		
		
		_BGG = move.getBGG2();
		_BG.setBGG2(_BGG);
		_BG.redraw();
		return move.getMoveAlowed();
	}
	
	/**
	 * Checks if the launchpad has its turn
	 * @return Boolean - true if move allowed
	 */
	public boolean isMyTurn(int x, int y) {
		boolean myTurn;
		int Choose = _BGG.getChoose();
		boolean team = _BGG.getTeam();
		int ID = _BGG.getBackgroundGrid(x, y);
		boolean SelectedTeam = false;
		
		if(ID < 200){
			SelectedTeam = true;
		}
		
		//TODO
		//For LAN and AI
		if((SelectedTeam == team && Choose==0) || (Choose==2 && team && SelectedTeam)){
			return true;
		}
		return false;
	}
	
	/**
	 * for updating the backgroundgrid
	 * @param BGG2-the new Backgroundgrid (Object of class BackgroundGrid)
	 */
	public void setBGG(BackgroundGrid BGG2) {
		_BGG = BGG2;
	}
	
	public void setBG(BoardGui BG){
		_BG = BG;
	}
	
	/**
	 * 
	 * @return Backgroundgrid - BGG2
	 */
	public BackgroundGrid getBGG2() {
		return _BGG;
	}
}



