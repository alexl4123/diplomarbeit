package javachess.meeple;

import java.io.Serializable;
import java.util.ArrayList;
import javachess.backgroundmatrix.BackgroundGrid;

/**
 * @author alexl12
 * @version 1.1 - Draw
 * 
 * Not needed in  1.1
 * 
 * Super Meeple which all other meeples inherit from. Contains basic variables and methods. 
 */
public abstract class SuperMeeple implements Serializable{				

	protected boolean team;								//false --> Black, true --> white
	private int Background;								//number in the backgroundgrid
	private int XPos, YPos;							//position of the meeple, only used for "schachabfrage"
	ArrayList<Object> PosX = new ArrayList<Object>();	//also used for the "schachabrage", like "drawing lines" on the board
	ArrayList<Object> PosY = new ArrayList<Object>();
	int Size;											//integer which is used in some move methods
	short HowOftenMoved;								//how often has the meeple moved?
	short LastMovedInRound;								//which round was the last moved round? 

	/**	 * Default constructor. Team can be chosen with the boolean.
	 * @param t the team which the meeple should be part of
	 */
	public SuperMeeple(boolean t, int i, int x, int y){
		HowOftenMoved = 0;
		LastMovedInRound = 0;
		setteam(t);
		setID(i);
		setMeepleXPos(x);
		setMeepleYPos(y);



	}
	/**
	 * When has the meeple be last moved?
	 * @param i
	 */
	public void setLastMovedInRound(short i){
		LastMovedInRound = i;
	}
	
	/**
	 * get the round in which the Meeple has been moved
	 * @return
	 */
	public short getLastMovedInRound(){
		return LastMovedInRound;
	}
	
	/**
	 * highers the short moved by one
	 */
	public void higherHowOftenMoved(){
		HowOftenMoved++;
	}
	
	public short getHowOftenMoved(){
		return HowOftenMoved;
	}
	
	
	/**
	 * 
	 * @param i the value of the Object in the Matrix
	 */
	public void setID(int i){
		Background = i;

	}


	public int getID(){
		return Background;
	}


	/**
	 * @return the team
	 */
	public boolean isteam() {
		return team;
	}

	/**
	 * Just for setting def. size of ArrayLists for SchachMatt
	 * @param Size
	 */
	public void setSize(int Size){

		for(int i=0;i<Size;i++){
			PosX.add(i);
			PosY.add(i);
			
		}
	}
	
	public int getSize(){
		return Size;
	}
	
	/**
	 * Returns the X-Pos of the "Attack Vector" (= Schachmatt)
	 * @param Index
	 * @return
	 */
	public ArrayList getAttX(){
		return PosX;
	}
	
	/**
	 * Returns the Y-Pos of the "Attack Vector" (= Schachmatt)
	 * @param Index
	 * @return
	 */
	public ArrayList getAttY(){
		return PosY;
	}
	

	/**
	 * @param team the team to set
	 */
	public void setteam(boolean team) {
		this.team = team;
	}






	/**
	 * For moving the meeple into a empty field
	 * Abstract method which all meeples should inherit.
	 */
	public abstract boolean move(int X1, int Y1, int X2, int Y2, boolean team);

	/**
	 * alternative for moving the meeple into a empty field
	 * Abstract method which all meeples should inherit.
	 */
	public abstract boolean move2(int X1, int Y1, int X2, int Y2, BackgroundGrid BGG);


	/**
	 * Abstract methode for beeting the enemies
	 */
	public abstract boolean strike(int X1, int Y1, int X2, int Y2, boolean team);

	
	public int getXMeeplePos(){
		return XPos;
	}

	public int getYMeeplePos(){
		return YPos;
	}

	public void setMeepleXPos(int i){
		XPos=i;
	}
	public void setMeepleYPos(int i){
		YPos=i;
	}
}
