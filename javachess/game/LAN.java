package javachess.game;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;

import javachess.backgroundmatrix.BackgroundGrid;
import javachess.gui.GUI;

/**
 * Here should be the Lan game mode
 * @author alexl12 
 * @version 1.1 - Draw
 *
 */


public class LAN implements Serializable{

	private Socket _theSocket = null;
	public ObjectInputStream netReadStream;
	public ObjectOutputStream netWriteStream;
	private boolean _team;
	public int[][] initSeed;
	private BackgroundGrid bgg;

	

	private boolean isConnectet; 					// if a host-client connection is valid
	private boolean firstturn = false;				//used for offseting in the Lan game loop
	
	
	
	public LAN(int[][] BGG, BackgroundGrid BGG2){
		
		isConnectet = false;
		this.initSeed = BGG;
		this.bgg = BGG2;

	}
	
	/**
	 * Initializes the Input- and Outputstreams for networking
	 * @throws IOException 
	 */

	public void createNetworkStreams(Socket tempSock) throws IOException{
	 
		
		System.out.println("trying to create streams");
		
		
		netWriteStream = new ObjectOutputStream(tempSock.getOutputStream());
		
		System.out.println("Stream one created");
		
		netWriteStream.flush();
		
		netReadStream = new ObjectInputStream(tempSock.getInputStream());
		
		
		System.out.println("Stream two created");
		
		
		
		
	}
	
	public Socket getSocket() {
		return _theSocket;
	}

	public void setSocket(Socket socketOfHost) {
		this._theSocket = socketOfHost;
	}


	/**
	 * set if a session is established
	 * 
	 * @param temp
	 *            - boolean - when it is connected set here true
	 */
	public void setIsConnectet(boolean temp) {
		this.isConnectet = temp;
		//TODO
	}
	
	/**
	 * if a session is established
	 * 
	 * @return this.isConnected - boolean - true if a connection has been
	 *         established
	 */
	public boolean getIsConnectet() {
		return this.isConnectet;
	}
	
	public void connecting(boolean team){
		
		
		_team = team;
		
		try{
		createNetworkStreams(_theSocket);
		System.out.println("Streams Created");
		System.out.println(team);
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	
	public boolean getTeam(){
		return _team;
	}
	
	public void  createSock(InetAddress ip) {
		try {
			_theSocket = new Socket (ip,22359);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void setFirstturn(boolean choose){
		firstturn = choose;
	}
	
	public boolean getFirstturn(){
		return firstturn;
	}
	
	/**
	 * @param _team the _team to set
	 */
	public void set_team(boolean _team) {
		this._team = _team;
	}
		
}


