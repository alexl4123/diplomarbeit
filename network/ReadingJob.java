package network;

import java.io.IOException;
import java.time.chrono.IsoChronology;

import Gui.GUI;
import javafx.beans.property.IntegerProperty;

public class ReadingJob implements Runnable {
	
	private GUI theGui;
	private int[][] compBGG1, compBGG2;
	
	public ReadingJob(GUI newGui){
		
		this.theGui = newGui;
	}


	@Override
	public void run() {
		
		
		
		System.out.println("reading Job running");
		compBGG1 = theGui.getBGG1();
		compBGG2 = compBGG1;
		
		
		
			
			
			
			
			try {
				
				if (theGui.getBoardGui().getBthinking()== true){
				System.out.println("read in reader thread");
				
				/*compBGG2 = (int[][]) theGui.getBGG2().getLan().netReadStream.readObject();
				for(int y = 0; y<8;y++){
					for(int x = 0; x<8;x++){
						System.out.print(":"+compBGG2[x][y]+":");
					}
					System.out.println(" ");
				}
				*/
				
				System.out.println("finished reading in reader Thread");
				
				compBGG1 = compBGG2;
				System.out.println("successfully compared in reader thread");
				theGui.getBoardGui().BGGChange.set(theGui.getBoardGui().BGGChange.get()+1);;
				System.out.println("Trigger Value changed");
				
				}
				
				
				
				
				
				
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				
			}	
			
			
			
			
		}
		
	

	public int[][] getBGG(){
		return compBGG1;
	}
	
}
