package meeple;

import java.net.URL;

import javax.swing.ImageIcon;

import BackgroundMatrix.BackgroundGrid;

/**
 * @author alexl12
 * @version 1.1 - Draw
 * 
 * Not needed in  1.1
 * 
*King class.
*/
public class King extends SuperMeeple {

	
	boolean Schach;								//is the king in "schach"?
	public boolean setSchach;					//temporary "schach" status

	/**
	 * Default Constructor
	 * @param t team
	 * @param i ID
	 */
	public King(boolean t, int i, int x, int y) {

		super(t,i,x,y);

	}

	/**
	 * the icon of the Kindg.
	 */
	public ImageIcon getIcon(){

		if(super.isteam()==true){
			URL url=King.class.getResource("/Images/kingWhite.png");
			ImageIcon icon = new ImageIcon(url);
			return icon;
		}else if(super.isteam()==false){
			URL url=King.class.getResource("/Images/kingBlack.png");
			ImageIcon icon = new ImageIcon(url);
			return icon;
		}else{
			ImageIcon icon = new ImageIcon("");
			return icon;
		}



	}



	@Override
	/**
	 * Primary move method for the King.
	 * 
	 */
	public boolean move(int X1, int Y1, int X2, int Y2, boolean team) {
		boolean MoveAllowed;
		if(team==super.team){
			if(((Math.abs(X1-X2)==1)&&(Y1-Y2==0))||((Math.abs(Y1-Y2)==1)&&(X2-X1==0))||((Math.abs(Y2-Y1)==1)&&(Math.abs(X2-X1)==1))){
				higherHowOftenMoved();
				MoveAllowed = true;
			}else{
				MoveAllowed = false;
			}
		}
		else{
			MoveAllowed=false;
		}

		return MoveAllowed;
	}

	/**
	 * Sets "schach".
	 */
	public void setSchach(boolean Schach1){
		Schach = Schach1;
	}
	
	/**
	 * get "schach".
	 * @return
	 */
	public boolean getSchach(){
		return Schach;
	}
	
	
	/**
	 * Default Strike method.
	 */
	@Override
	public boolean strike(int X1, int Y1, int X2, int Y2, boolean team) {

		if(super.team==team){
			if(((Math.abs(X1-X2)==1)&&(Y1-Y2==0))||((Math.abs(Y1-Y2)==1)&&(X2-X1==0))||((Math.abs(Y2-Y1)==1)&&(Math.abs(X2-X1)==1))){			
				higherHowOftenMoved();
				return true;
			}else{
				return false;
			}
		}
		else{
			return false;
		}
	}

	@Override
	/**
	 * Alternative move method (has not been used).
	 */
	public boolean move2(int X1, int Y1, int X2, int Y2, BackgroundGrid BGG) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * Lower the number how often this Meeple has been moved by one
	 */
	public void LowerHowOftenMoved(){
		HowOftenMoved--;
	}

}
