package javachess.meeple;

import java.net.URL;
import javax.swing.ImageIcon;
import javachess.backgroundmatrix.BackgroundGrid;

/**
 * @author alexl12
 * @version 1.1 - Draw
 * 
 * 
 * Not needed in  1.1
 * 
 * Jumper class
 */
public class Jumper extends SuperMeeple {

	/**
	 * Default constructor.
	 * @param t team
	 * @param i ID
	 */
	public Jumper(boolean t, int i, int x, int y) {

		super(t,i,x ,y);
		super.setSize(30);

	}

	/**
	 * Gets the Icon of the Jumper.
	 */
	public ImageIcon getIcon(){

		if(super.isteam()==true){														//same like in the pawn class
			URL url=Farmer.class.getResource("/javachess/images/jumperWhite.png");
			ImageIcon icon = new ImageIcon(url);
			return icon;
		}else if(super.isteam()==false){
			URL url=Farmer.class.getResource("/javachess/images/jumperBlack.png");
			ImageIcon icon = new ImageIcon(url);
			return icon;
		}else{
			ImageIcon icon = new ImageIcon("");
			return icon;
		}



}

	@Override
	/**
	 * Standard move method for the jumper class. 
	 */
	public boolean move(int X1, int Y1, int X2, int Y2, boolean team) {
		
		int dX=X2-X1;
		int dY=Y2-Y1;
		PosX.set(0, X1);
		PosY.set(0, Y1);
		
		if(super.team==team){
		try{
			if(Math.abs(dX/dY)==2 && Math.abs(dX)==2){
				higherHowOftenMoved();
				return true;
			}else if(Math.abs(dY/dX)==2&&Math.abs(dY)==2){
				higherHowOftenMoved();
				return true;
			}else{
				return false;
			}
			
			
			
		}catch(Exception e){
			return false;
		}
		}return false;
		
	}

	@Override
	/**
	 * Alternative move method for this class (Has not been used).
	 */
	public boolean move2(int X1, int Y1, int X2, int Y2, BackgroundGrid BGG) {
		return false;
	}

	@Override
	/**
	 * Strike method (Has not been used because it is included in the move method).
	 */
	public boolean strike(int X1, int Y1, int X2, int Y2, boolean team) {
		return false;
	}
	
	/**
	 * Lower the number how often this Meeple has been moved by one
	 */
	public void LowerHowOftenMoved(){
		HowOftenMoved--;
	}

}