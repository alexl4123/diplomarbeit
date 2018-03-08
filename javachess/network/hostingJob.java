package javachess.network;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javachess.backgroundmatrix.BackgroundGrid;
import javachess.gui.BoardGui;
import javachess.gui.GUI;


public class hostingJob implements Runnable {

	
	private ServerSocket serversock = null;
	private BackgroundGrid bgg;
	private Socket tempsock;
	public Thread th;
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
				gui.getBoardGui().heartBeatJob = new Heartbeat(null, true, gui.getBoardGui().Heartbeat);
				this. th = new Thread(gui.getBoardGui().heartBeatJob);
				this.th.start();
				Heartbeat.heartThread=th;
				System.out.println("Hosting");
				bgg.getLan().set_team(true);
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
			//	e.printStackTrace(); 
				
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
			gui.getBoardGui().heartBeatJob.stopHeartBeat();
			this.running=false;
			gui.getBGG2().getLan().setIsConnectet(false);
			
		} catch (IOException e) {
			
			System.out.println("Strange socket closure");
		}
	}
}
