package threadJobs;

import java.awt.Color;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import BackgroundMatrix.BackgroundGrid;
import GuiStuff.BoardGui;
/**
 * @author alexl12
 * @version 1.1 - Draw
 * 
 * Not needed in 1.1
 * 
 * waits for connection
 *
 */
public class TheThreadWhoIsAlwaysWaitingJob implements Runnable{

	BoardGui b; //boardgui
	ServerSocket s; //serversocket
	Socket cs; //clientsocket
	BackgroundGrid BGG; //Backgroundgrid
	
	@Override
	/**
	 * thread job => waits for connection
	 */
	public void run() {
		
		try {
			
			//if there is a connection the accept it
			cs=s.accept();
			b.setClientSock(cs);
			BGG.setIsConnectet(true);
			b.getTextArea().append("console>> Connection established");
			if(BGG.getIsConnectet()==true){
				b.con2.setBackground(Color.green);
			}else{
				b.con2.setBackground(Color.red);
			}
			if(BGG.getTeam()==true){
				b.player2.setBackground(Color.white);
			}else{
				b.player2.setBackground(Color.black);
			}
			b.createStreams(cs);
			b.makeANewReaderThread();
			b.getOOS().writeObject(BGG);
			
			
			
			
			
		} catch (IOException e) {
		
			//e.printStackTrace();
		}
		
		
		
	}
	
	/**
	 * sets the BackgroundGrid, BoardGui and ServerSocket.
	 * @param bg1-BoardGui
	 * @param ss-serversocket
	 * @param BGG1-BackgroundGrid
	 */
	public TheThreadWhoIsAlwaysWaitingJob(BoardGui bg1, ServerSocket ss,BackgroundGrid BGG1){
		BGG = BGG1;
		b=bg1;
		s=ss;
	}
	
}
