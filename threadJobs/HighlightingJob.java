package threadJobs;

import Gui.BoardGui;
import Gui.Tile;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class HighlightingJob implements Runnable{

	BoardGui bg;
	Color Color1;
	Color Color2;
	Tile T;
	GraphicsContext gc;
	
	public HighlightingJob(BoardGui newBg, Tile tile, GraphicsContext context, Color c1, Color c2){
		
		bg=newBg;
		T=tile;
		Color1=c1;
		Color2=c2;
		gc=context;
		
	}
	
	
	public void run() {
		
		System.out.println("I AM RUNNING");
		
		while(bg.isHighlightAnimationRunning()==true){
			
			for(int i=0; i<11; i++){
				
				double P1X=(bg.get_X()/100);
				double P1Y=(bg.get_Y()/100);
				
				double flowValue=(double)i/10;
				gc.setStroke(bg.getLinearGradient(Color1, Color2, flowValue));
				gc.strokeRect(T.getX()*P1X, T.getY()*P1Y, T.getW()*P1X, T.getH()*P1Y);
				
				try {
					wait(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					System.out.println("ThrowException");
				}
			} 
			
		}  
		
	}
 

}
