package network;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import BackgroundMatrix.BackgroundGrid;
import Gui.BoardGui;
import Gui.GUI;


public class hostingJob implements Runnable {

	
	private ServerSocket serversock;
	private BackgroundGrid bgg;
	private Socket tempsock;
	private boolean running;
	private BoardGui BG;
	private GUI gui;
	
	public hostingJob(GUI newgui){
		this.gui = newgui;
		this.bgg=gui.getBGG2();
		this.BG=gui.getBoardGui();
	}
	
	public void run() {
		
		try {
			
			setServersock(new ServerSocket(22359));
			running = true;
			
			
			
		} catch (IOException e) {
			System.out.println("ERROR during Socket generation");
		}
		
		
		while(running){
			
			
			try {
				
				System.out.println("Hosting");
				tempsock = getServersock().accept();
				bgg.getLan().setSocket(tempsock);
				bgg.getLan().connecting(true);
				bgg.getLan().setIsConnectet(true);
				running =false;
				
				System.out.println("Hosting connection cycle finished");
	
				
					// resetting the blurry menu by a trick
					PointerInfo a = MouseInfo.getPointerInfo();
					Point b = a.getLocation();
					int xOrig = (int)b.getX();
		            int yOrig = (int)b.getY();
		            
		            try {
		                Robot r = new Robot();
		                r.mouseMove((int)(BG.getGui().getStage().getX()+(BG.getGui().getStage().getWidth()/2)),(int) (BG.getGui().getStage().getY()+(BG.getGui().getStage().getHeight()/2)));
		              // r.mouseMove((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2);
		                r.mousePress(InputEvent.BUTTON1_MASK); //press the left mouse button
		                r.mouseRelease(InputEvent.BUTTON1_MASK); //release the left mouse button

		                //move the mouse back to the original position
		                r.mouseMove(xOrig, yOrig);
		            } catch (Exception e) {
		                System.out.println(e.toString());
		            }
		        
		          //  bgg.getLan().netWriteStream.writeObject(BG.getGui().getBGG1());
		           // bgg.getLan().netWriteStream.flush();
		           // bgg.getLan().netWriteStream.writeObject(BG.getLastMoveList());
				
		            
					
				
			
				
				
				
			} catch (Exception e) {
				
				System.out.println("HOSTINGTHREAD STOPPED WORKING");
			}
			
			
		}
	}

	public ServerSocket getServersock() {
		return serversock;
	}

	public void setServersock(ServerSocket serversock) {
		this.serversock = serversock;
	}

	public void stopSocket(){
		try {
			
			this.serversock.close();
			this.running=false;
			
		} catch (IOException e) {
			
			System.out.println("Strange socket closure");
		}
	}
}
