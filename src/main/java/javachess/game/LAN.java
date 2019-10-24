package javachess.game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import javachess.backgroundmatrix.BackgroundGrid;

/**
 * 
 * @author mhub - 2018
 * @version 2.0
 * 
 * This class contains all Variables used by client and Host in LAN-Mode.
 *
 */
public class LAN implements Serializable{

	/**
	 * Socket thath is used for communication by client and host
	 */
	private Socket _theSocket = null;
	
	/**
	 * Stream to Read from Socket
	 */
	public ObjectInputStream netReadStream;
	
	/**
	 * Stream to write from Socket
	 */
	public ObjectOutputStream netWriteStream;
	
	/**
	 * Boolean to choose the team used in LAN
	 */
	private boolean _team;
	
	/**
	 * First Matric to be loaded
	 */
	public int[][] initSeed;
	
	/**
	 * The current backgroundgrid
	 */
	private BackgroundGrid bgg;

	/**
	 * Represents the connection status
	 */
	private boolean isConnectet; 			

	/**
	 * Used for differencing wether to read or write first in LAN mode
	 */
	private boolean firstturn = false;				
	
	/**
	 * The constructor
	 * @param BGG	- First matrix to be loaded
	 * @param BGG2 - CUrrent backgroundgrid
	 */
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
	 
		netWriteStream = new ObjectOutputStream(tempSock.getOutputStream());
		netWriteStream.flush();	
		netReadStream = new ObjectInputStream(tempSock.getInputStream());	
	}
	
	/**
	 * 
	 * @return the currently used socket
	 */
	public Socket getSocket() {
		return _theSocket;
	}

	/**
	 * Sets the Socket (used of setting the socket of the host)
	 * @param socketOfHost - the socket to be set
	 */
	public void setSocket(Socket socketOfHost) {
		this._theSocket = socketOfHost;
	}


	/**
	 * set if a session is established
	 * @param temp - boolean - when it is connected set here true
	 */
	public void setIsConnectet(boolean temp) {
		this.isConnectet = temp;
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
	
	/**
	 * Method that calls the Method for creating streams and sets the team.
	 * @param team - the tream to be set
	 */
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
	
	/**
	 * 
	 * @return the current team
	 */
	public boolean getTeam(){
		return _team;
	}
	
	/**
	 *  Creates a new Socket with the port for chess
	 * @param ip - IP-Adress of the target. 
	 */
	public void  createSock(InetAddress ip) {
		try {
			_theSocket = new Socket (ip,22359);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Setthing the first turn status
	 * @param choose - true for getting the first turn, false for getting the second one
	 */
	public void setFirstturn(boolean choose){
		firstturn = choose;
	}
	
	/**
	 * 
	 * @return the firstturn status
	 */
	public boolean getFirstturn(){
		return firstturn;
	}
	
	/**
	 * @param _team - set the team
	 */
	public void set_team(boolean _team) {
		this._team = _team;
	}
		
}


