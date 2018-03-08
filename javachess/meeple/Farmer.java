package javachess.meeple;

import java.net.URL;
import javax.swing.ImageIcon;
import javachess.backgroundmatrix.BackgroundGrid;

/**
 * @author alexl12
 * @version 1.1 - Draw
 * 
 * Not needed in  1.1
 * 
 * Pawn-class
 */
public class Farmer extends SuperMeeple {

	public boolean specialMoved;				//boolean which is used in case of special moves
	
	
	
	/**
	 *  Default Constructor
	 * @param t team
	 * @param i ID
	 */
	public Farmer(boolean t, int i, int x, int y) {
		
		super(t,i, x, y);
		super.setSize(30);
		specialMoved = false;
	}

	/**
	 * Gets team depending the icon for the pawn
	 * @return
	 */
	public ImageIcon getIcon(){

		if(super.isteam()==true){											//Team white
			URL url=Farmer.class.getResource("/javachess/images/pawnWhite.png");		//path of the icon
			ImageIcon icon = new ImageIcon(url);							
			return icon;
		}else if(super.isteam()==false){									//Team black
			URL url=Farmer.class.getResource("/javachess/images/pawnBlack.png");
			ImageIcon icon = new ImageIcon(url);
			return icon;
		}else{
			ImageIcon icon = new ImageIcon("");
			return icon;
		}



	}



	@Override
	/**
	 * Standard move methode for the pawn
	 */
	public boolean move(int X1, int Y1, int X2, int Y2, boolean team) {
		return false;
	}
	
	public boolean getSpecialMoved(){
		return specialMoved;
	}


	/**
	 * If you want to have another Meeple that that stands in the Y+1 & X+1 or 
	 * Y-1 & X-1, way call this bro ;) 
	 */
	@Override
	public boolean strike(int X1, int Y1, int X2, int Y2, boolean team) {

		int X = X1-X2;
		int Y = Y1-Y2;
		try{
			if(team==super.team){
				if((Math.abs(X/Y)==1)&&(Y==1)&&(super.isteam()==false)){
					higherHowOftenMoved();
					return true;
				}else if((Math.abs(X/Y)==1)&&(Y==(-1))&&(super.isteam()==true)){
					higherHowOftenMoved();
					return true;
				}else{
					return false;
				}
			}else{
				return false;
			}
		}catch(Exception ex){
			return false;
		}




	}

	@Override
	/**
	 * Second, alternative move method for special moves.
	 * ONLY USED FOR MOVES
	 */
	public boolean move2(int X1, int Y1, int X2, int Y2, BackgroundGrid BGG) {
		boolean MoveAllowed = false;
			PosX.set(0, X1);
			PosY.set(0, Y1);
			if(BGG.getTeam()==super.team){
			if(((X1-X2)==0)&&((Y1-Y2)==1)&&(super.isteam()==false)){//for team black
				higherHowOftenMoved();
				MoveAllowed = true;
			}else if(((X1-X2)==0)&&((Y2-Y1)==1)&&(super.isteam()==true)){//for team white
				higherHowOftenMoved();
				MoveAllowed = true;
			}else if(((X1-X2)==0)&&((Y2-Y1)==2)&&(super.isteam()==true)&&(super.getHowOftenMoved()<1)){//special move for team white
			
				if(BGG.getBackgroundGrid(X1, Y1+1)==0 || BGG.getBackgroundGrid(X1, Y1+1) > 400){
					specialMoved = true;
					higherHowOftenMoved();
					MoveAllowed = true;
				}else{
					MoveAllowed = false;
				}

			}else if(((X1-X2)==0)&&((Y1-Y2)==2)&&(super.isteam()==false)&&(super.getHowOftenMoved()<1)){//special move for team black
				if(BGG.getBackgroundGrid(X1, Y2+1)==0){
					specialMoved = true;
					higherHowOftenMoved();
					MoveAllowed = true;
				}else{
					MoveAllowed = false;
				}
			}else{
				MoveAllowed = false;
			}
			}
		return MoveAllowed;
		
	}
	
	/**
	 * Lower the number how often this Meeple has been moved by one
	 */
	public void LowerHowOftenMoved(){
		HowOftenMoved--;
	}
}
