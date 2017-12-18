package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import BackgroundMatrix.BackgroundGrid;

public class hostingJob implements Runnable {

	
	private ServerSocket serversock;
	private BackgroundGrid bgg;
	private Socket tempsock;
	private int connectionCounter;		//counts how many clients are connectet. If > 1 --> Viewer
	private boolean running;
	
	public hostingJob(BackgroundGrid bgg){
		this.bgg=bgg;
	}
	
	public void run() {
		
		try {
			
			setServersock(new ServerSocket(22359));
			connectionCounter = 0;
			running = true;
			
			
		} catch (IOException e) {
			System.out.println("ERROR during Socket generation");
		}
		
		
		while(running){
			
			
			try {
				
				System.out.println("Hosting");
				tempsock = getServersock().accept();
				System.out.println("connectet");

	//-------------------------------PLAYING CLIENT----------------------------------------------------------//
				if(connectionCounter<1){
					
					bgg.setSocketOfHost(tempsock);
					bgg.setIsConnectet(true);
					connectionCounter++;
					System.out.println("connection established");
					
				}
	//--------------------------------VIEWVING CLIENT---------------------------------------------------------//			
				
				else{
					
				}
				
			} catch (IOException e) {
				
				System.out.println("THREAD STOPPED WORKinG");
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
			
			e.printStackTrace();
		}
	}
}
