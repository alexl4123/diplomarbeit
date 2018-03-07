
package meeple;

import java.net.URL;

import javax.swing.ImageIcon;

import javachess.backgroundmatrix.BackgroundGrid;

/**
 * @author alexl12
 * @version 1.1 - Draw
 * 
 * Not needed in  1.1
 * 
 *	Tower class.
 */
public class Tower extends SuperMeeple {
	/**
	 * Default constructor.
	 * @param t team
	 * @param i ID
	 */
	public Tower(boolean t, int i, int x, int y) {

		super(t,i,x,y);
		super.setSize(30);
	}

	/**
	 * Gets the icon of the Tower.
	 */
	public ImageIcon getIcon(){

		
		if(super.isteam()==true){
			URL url=Tower.class.getResource("/Images/towerWhite.png");
			ImageIcon icon = new ImageIcon(url);
			return icon;
		}else if(super.isteam()==false){
			URL url=Tower.class.getResource("/Images/towerBlack.png");
			ImageIcon icon = new ImageIcon(url);
			return icon;
		}else{
			ImageIcon icon = new ImageIcon("");
			return icon;
		}



	}





	@Override
	/**
	 * Strike method (has not been used)
	 */
	public boolean strike(int X1, int Y1, int X2, int Y2, boolean team) {
		return false;
	}

	@Override
	/**
	 * alternative move method (has not been used)
	 */
	public boolean move(int X1, int Y1, int X2, int Y2, boolean team) {
		return false;
	}

	@Override
	/**
	 * THE ONLY MOVE METHOD!!!
	 */
	public boolean move2(int X1, int Y1, int X2, int Y2, BackgroundGrid BGG) {

		int dX=X1-X2;
		int dY=Y1-Y2;
		int samwellTarly=0;
		
		PosX.set(0, X1);
		PosY.set(0, Y1);
		
	if(super.team==BGG.getTeam()){
		if((dX==0)&&(dY>0)){
			
			Size = (int) dY;
			for(int i=1;i<dY;i++){
				PosX.set(i, X1);
				PosY.set(i, Y1-i);
				samwellTarly=samwellTarly+BGG.getBackgroundGrid(X1, Y1-i);
			}
		}else if((dX==0)&&(dY<0)){
			Size = (int) Math.abs(dY);
			for(int i=1;i<Math.abs(dY);i++){
				//samwellTarly=samwellTarly+BGG.getBackgroundGrid(X1, Y1+i);
				samwellTarly = samwellTarly + BGG.iBackground[X1][Y1+i];
				PosX.set(i, X1);
				PosY.set(i, Y1+i);
			}
		}else if((dX>0)&&(dY==0)){
			Size = (int) dX;
			for(int i=1;i<dX;i++){
				samwellTarly=samwellTarly+BGG.getBackgroundGrid(X1-i, Y1);
				PosX.set(i, X1-i);
				PosY.set(i, Y1);
			}
		}else if((dX<0)&&(dY==0)){
			Size = (int) Math.abs(dX);
			for(int i=1;i<Math.abs(dX); i++){
				samwellTarly=samwellTarly+BGG.getBackgroundGrid(X1+i, Y1);
				PosX.set(i, X1+i);
				PosY.set(i, Y1);
			}
		}else{
			samwellTarly=2000; 
		}

		if(samwellTarly>0 ){
			return false;
		}else{
			higherHowOftenMoved();
			return true;
		}
	}else{
		return false;
	}

	}
	
	/**
	 * Lower the number how often this Meeple has been moved by one
	 */
	public void LowerHowOftenMoved()
	{
		HowOftenMoved--;
	}



}
