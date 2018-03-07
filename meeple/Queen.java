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
 * Queen class 
 */
public class Queen extends SuperMeeple {
	
	
	/**
	 * Default constructor.
	 * @param t team
	 * @param i ID
	 */
	public Queen(boolean t, int i, int x, int y) {

		super(t,i, x, y);
		super.setSize(30);
	}

	/**
	 * the icon of the Queen.
	 * @return
	 */
	public ImageIcon getIcon(){

		if(super.isteam()==true){
			URL url=Queen.class.getResource("/Images/queenWhite.png");
			ImageIcon icon = new ImageIcon(url);

			return icon;
		}else if(super.isteam()==false){
			URL url=Queen.class.getResource("/Images/queenBlack.png");
			ImageIcon icon = new ImageIcon(url);
			return icon;
		}else{
			ImageIcon icon = new ImageIcon("");
			return icon;
		}



	}

	

	
	
	/**
	 * Strike method (has not been used). 
	 */
	public boolean strike(int X1, int Y1, int X2, int Y2, boolean team) {
		return false;
	}

	@Override
	/**
	 * Alternative move method (Has not been used). 
	 */
	public boolean move(int X1, int Y1, int X2, int Y2, boolean team ) {
		return false;
	}

	@Override
	/**
	 * Primary move method. 
	 */
	public boolean move2(int X1, int Y1, int X2, int Y2, BackgroundGrid BGG) {

		float dX=X2-X1;
		float dY=Y2-Y1;
		int samwellTarly=0;   //to check if a meeple stands in the way of move/strike

		PosX.set(0, X1); //to set the current loc. of the meeple
		PosY.set(0, Y1);


		if(super.team==BGG.getTeam()){
			try{
				//move like tower
				if((dX==0)&&(dY>0)){
					Size = (int) dY;
					for(int i=1;i<dY;i++){
						samwellTarly=samwellTarly+BGG.getBackgroundGrid(X1, Y1+i);
						PosX.set(i, X1);
						PosY.set(i, Y1+i);
					}
				}else if((dX==0)&&(dY<0)){
					Size = (int) Math.abs(dY);
					for(int i=1;i<Math.abs(dY);i++){
						samwellTarly=samwellTarly+BGG.getBackgroundGrid(X1, Y1-i);
						PosX.set(i, X1);
						PosY.set(i, Y1-i);
					}
				}else if((dX>0)&&(dY==0)){
					Size = (int) dX;
					for(int i=1;i<dX;i++){
						samwellTarly=samwellTarly+BGG.getBackgroundGrid(X1+i, Y1);
						PosX.set(i, X1+i);
						PosY.set(i, Y1);
					}
				}else if((dX<0)&&(dY==0)){
					Size = (int) Math.abs(dX);
					for(int i=1;i<Math.abs(dX); i++){
						samwellTarly=samwellTarly+BGG.getBackgroundGrid(X1-i, Y1);
						PosX.set(i, X1-i);
						PosY.set(i, Y1);
					}
				//move like runner
				}else if((Math.abs(dX/dY)==1)){
					if(dX>0&&dY>0){
						Size = (int) dX;
						for(int i=1;i<dX; i++){
							samwellTarly=samwellTarly+BGG.getBackgroundGrid(X1+i, Y1+i);
							PosX.set(i, X1+i);
							PosY.set(i, Y1+i);
						}
					}else if(dX>0&&dY<0){
						Size = (int) dX;
						for(int i=1;i<dX;i++){
							samwellTarly=samwellTarly+BGG.getBackgroundGrid(X1+i, Y1-i);
							PosX.set(i, X1+i);
							PosY.set(i, Y1-i);
						}
					}else if(dX<0&&dY>0){
						Size = (int) dY;
						for(int i=1;i<dY;i++){
							samwellTarly=samwellTarly+BGG.getBackgroundGrid(X1-i, Y1+i);
							PosX.set(i, X1-i);
							PosY.set(i, Y1+i);
						}
					}else if(dX<0&&dY<0){
						Size = (int) Math.abs(dY);
						for(int i=1;i<Math.abs(dY);i++){
							samwellTarly=samwellTarly+BGG.getBackgroundGrid(X1-i, Y1-i);
							PosX.set(i, X1-i);
							PosY.set(i, Y1-i);
						}
					}
				}else if((Math.abs(dX)>0)&&(dY==0)){
					return false;
				}else{
					samwellTarly=2000; 
				}

				if(samwellTarly>0){
					return false;
				}else{
					higherHowOftenMoved();
					return true; //if way of move is correct and no other meeple stands in the way
				}
			}catch(Exception ex){
				return false;
			}
		}else{
			return false;
		}

	}
	
	/**
	 * Lower the number how often this Meeple has been moved by one
	 */
	public void LowerHowOftenMoved(){
		HowOftenMoved--;
	}

}
