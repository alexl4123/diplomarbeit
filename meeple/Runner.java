package meeple;

import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import BackgroundMatrix.BackgroundGrid;


/**
 * @author alexl12
 * @version 1.1 - Draw
 * 
 * Not needed in  1.1
 * 
 * Runner class
 */
public class Runner extends SuperMeeple {



	/**
	 * Defalut constructor
	 * @param t team
	 * @param i ID
	 */
	public Runner(boolean t, int i, int x, int y) {
		super(t,i,x,y);
		super.setSize(30);
	}



	/**
	 * gets the icon of the runner.
	 * @return
	 */
	public ImageIcon getIcon(){

		if(super.isteam()==true){
			URL url=Runner.class.getResource("/Images/runnerWhite.png");
			ImageIcon icon = new ImageIcon(url);
			return icon;
		}else if(super.isteam()==false){
			URL url=Runner.class.getResource("/Images/runnerBlack.png");
			ImageIcon icon = new ImageIcon(url);
			return icon;
		}else{
			ImageIcon icon = new ImageIcon("");
			return icon;
		}



	}

	@Override
	/**
	 * alternative move method (has not been used).
	 */
	public boolean move(int X1, int Y1, int X2, int Y2, boolean team) {
		return false;
	}

	@Override
	/**
	 * primary move method.
	 */
	public boolean move2(int X1, int Y1, int X2, int Y2, BackgroundGrid BGG) {

		float dX=X2-X1;
		float dY=Y2-Y1;
		int forTheWatch=0;

		PosX.set(0, X1);
		PosY.set(0, Y1);

		if(super.team==BGG.getTeam()){						//checking many move conditions
			try{

				if((Math.abs(dX/dY)==1)){

					if(dX>0&&dY>0){
						Size = (int) dX;
						for(int i=1;i<dX; i++){
							forTheWatch=forTheWatch+BGG.getBackgroundGrid(X1+i, Y1+i);

							PosX.set(i, X1+i);
							PosY.set(i, Y1+i);
						}
					}else if(dX>0&&dY<0){
						Size = (int) dX;
						for(int i=1;i<dX;i++){
							forTheWatch=forTheWatch+BGG.getBackgroundGrid(X1+i, Y1-i);
							PosX.set(i, X1+i);
							PosY.set(i, Y1-i);
						}
					}else if(dX<0&&dY>0){
						Size = (int) dY;
						for(int i=1;i<dY;i++){
							forTheWatch=forTheWatch+BGG.getBackgroundGrid(X1-i, Y1+i);
							PosX.set(i, X1-i);
							PosY.set(i, Y1+i);
						}
					}else if(dX<0&&dY<0){
						Size = (int) Math.abs(dY);
						for(int i=1;i<Math.abs(dY);i++){
							forTheWatch=forTheWatch+BGG.getBackgroundGrid(X1-i, Y1-i);
							PosX.set(i, X1-i);
							PosY.set(i, Y1-i);
						}
					}
				}else if((Math.abs(dX)>0)&&(dY==0)){
					return false;
				}else{

					forTheWatch=5;  //https://www.youtube.com/watch?v=uOOllehJqcM    

				}

			}
			catch(Exception e){
				return false;
			}

			if(forTheWatch>0){
				return false;
			}
			else{
				higherHowOftenMoved();
				return true;
			}
		}else{

			return false;
		}

	}



	@Override
	/**
	 * Strike method (has not been used) 
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
