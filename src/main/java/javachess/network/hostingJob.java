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


/**
 * 
 * @author mhub
 * @version 2.0
 * 
 * Threadjob, which is used to host a game.
 */
public class hostingJob implements Runnable {

	/**
	 * ServerSocket to Host the game
	 */
	private ServerSocket serversock = null;
	
	/**
	 * The current Backgroundgrid
	 */
	private BackgroundGrid bgg;
	
	/**
	 * Socket whihc later is the Socket in LAN-Class
	 */
	private Socket tempsock;
	
	/**
	 * Thread to start a hearbeat
	 */
	public Thread th;
	
	/**
	 * Boolean to determin wether the hosting-process is running
	 */
	private boolean running;
	
	/**
	 * Current BoardGui
	 */
	private BoardGui BG;
	
	/**
	 * Current Gui
	 */
	private GUI gui;
	
	
	/**
	 * The Constructor.
	 * @param newgui - hte current Gui.
	 */
	public hostingJob(GUI newgui){
		this.gui = newgui;
		this.bgg=gui.getBGG2();
		this.BG=gui.getBoardGui();
	}
	
	/**
	 * Starts when starting the thread. 
	 * Does everything needed in Host-Process.
	 */
	public void run() {
		
		try {
			
			//Setting the Socket
			setServersock(new ServerSocket(22359));
			running = true;
			
			
			
		} catch (IOException e) {
			System.out.println("ERROR during Socket generation");
		}
		
		
		while(running){
			
			
			try {
				gui.getBoardGui().heartBeatJob = new Heartbeat(null, true, gui.getBoardGui().Heartbeat);		//Setting new Heartbeat
				this. th = new Thread(gui.getBoardGui().heartBeatJob);
				this.th.start();
				Heartbeat.heartThread=th;
				System.out.println("Hosting");
				bgg.getLan().set_team(true);
				tempsock = getServersock().accept();				//Waiting for connections
				bgg.getLan().setSocket(tempsock);
				bgg.getLan().connecting(true);
				bgg.getLan().setIsConnectet(true);				//doing some connection stuff
				running =false;
				
				
				System.out.println("Hosting connection cycle finished");

					// resetting the blurry menu by a trick - pressing the mouse on the screen by the robot.
					PointerInfo a = MouseInfo.getPointerInfo();
					Point b = a.getLocation();
					int xOrig = (int)b.getX();
		            int yOrig = (int)b.getY();
		            
		            try {
		                Robot r = new Robot();
		                r.mouseMove((int)(BG.getGui().getStage().getX()+(BG.getGui().getStage().getWidth()/2)),(int) (BG.getGui().getStage().getY()+(BG.getGui().getStage().getHeight()/2)));
		                r.mousePress(InputEvent.BUTTON1_MASK); //press the left mouse button
		                r.mouseRelease(InputEvent.BUTTON1_MASK); //release the left mouse button

		                //move the mouse back to the original position
		                r.mouseMove(xOrig, yOrig);
		            } catch (Exception e) {
		                System.out.println(e.toString());
		            }
		        
	
				
			} catch (Exception e) {
				
				System.out.println("HOSTINGTHREAD STOPPED WORKING");
			//	e.printStackTrace(); 
				
			}
			
			
		}
	}

	/**
	 * 
	 * @return the current serverscoket
	 */
	public ServerSocket getServersock() {
		return serversock;
	}

	/**
	 * Sets the Serversocket
	 * @param serversock the Socket to be set
	 */
	public void setServersock(ServerSocket serversock) {
		this.serversock = serversock;
	}

	/**
	 * Stops the Serversocket and clears up the connection. 
	 */
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
