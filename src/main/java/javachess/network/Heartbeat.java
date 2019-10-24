package javachess.network;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import javafx.beans.property.IntegerProperty;

/**
 *
 * @author mhub - 2018
 *
 */
public class Heartbeat implements Runnable {

	public static final int TIMEOUT = 15000;
	public static final int PORT = 23420;
	/**
	 * Current Thread
	 */
	public static Thread heartThread;
	
	/**
	 * Some booleans used later
	 */
	private boolean isHoster, isRunning, firsttry, initiateDisconnect;
	
	/**
	 * Serversocket for Hosting a heartbeat
	 */
	private ServerSocket heartBeatSocket;
	
	/**
	 * Triggers Code in GUi if the hearbeat detects a disconnect.
	 */
	private IntegerProperty trigger;
	
	/**
	 * IP of the client
	 */
	private InetAddress clientAddress;
	
	/**
	 * Socket used for heatbeat - coms
	 */
	private Socket clientHeartSocket;
	
	/**
	 * Stream to write Heaertbeats
	 */
	public ObjectOutputStream heartWriteStream;
	
	/**
	 * Stream to read Heartbeats
	 */
	public ObjectInputStream heartReadStream;


	/**
	 * The Constructor.
	 * @param clientAdress - Address of the Client
	 * @param host - wether the heartbeat is host
	 * @param trigger - the trigger variable from the gui
	 */
	public Heartbeat(InetAddress clientAdress, boolean host, IntegerProperty trigger){
		
		this.clientAddress = clientAdress;
		this.isHoster = host;
		this.trigger = trigger;
		initiateDisconnect = false;

	}


	@Override
	/**
	 * Sets and starts the Heartbeat-Process. Used for client and host
	 */
	public void run() {

		try {
			
			if(isHoster == false){

				//Connecting with the normal socket - normal client behaviour. Setting up streams n stuff
				System.out.println("settintHeartClientSock");
				clientHeartSocket = new Socket(clientAddress, PORT);
				createHeartbeatStreams(clientHeartSocket);
				firsttry = true;
				clientHeartSocket.setSoTimeout(TIMEOUT);	//timeout - counts down to zero - then kills the connection. Resets if data is received.
				
			}



			else if(isHoster == true){
				
				//Normal hosting behaviour - waiting for connections. Setting up streams n timeout
				System.out.println("settingHostheartSock");
				heartBeatSocket = new ServerSocket(PORT);
				clientHeartSocket=heartBeatSocket.accept();
				createHeartbeatStreams(clientHeartSocket);
				clientHeartSocket.setSoTimeout(TIMEOUT);

			}
			
			int testnumber = 0;
			isRunning = true;
			while(isRunning){


				//makes the client read first!
				if(isHoster == false && firsttry == true){

					
					System.out.println("Trying first stream");
					firsttry = false;
					heartReadStream.readInt();
					System.out.println("ReadheartBeat");
				}

				//continously reading and writing heartbeats
				testnumber = 1337;			//1337!!! LEEET
				Thread.sleep(1000);			//setting the thread so sleep for one second. 
				heartWriteStream.writeInt(testnumber);
				heartWriteStream.flush();
				System.out.println("WroteheartBeat");
				Thread.sleep(1000);
				heartReadStream.readInt();
				System.out.println("ReadheartBeat");

				Thread currentThread = Thread.currentThread();
				currentThread.sleep(5);

			}

			//on all catches --> Kill the connection
		} catch (InterruptedIOException e){

			System.out.println("Disconnected ");
			isRunning = false;
			trigger.set(trigger.get()+1);

		} catch (IOException e) {
			
			System.out.println("Hearbeat IOEXCEPTION");
			isRunning = false;
			trigger.set(trigger.get()+1);
			
		} catch (InterruptedException e) {
			
			System.out.println("Heartbeat Interrupt Exception");
			isRunning = false;
			trigger.set(trigger.get()+1);
		}



	}

	/**
	 * Creates streams nessecary for communication in heartbeat
	 * @param tempSock - the socket for communication
	 * @throws IOException
	 */
	private void createHeartbeatStreams(Socket tempSock) throws IOException{
		
		this.heartWriteStream = new ObjectOutputStream(tempSock.getOutputStream());
		this.heartWriteStream.flush();
		heartReadStream = new ObjectInputStream(tempSock.getInputStream());
	}
	
	/**
	 * Stops the Heartbeat
	 */
	public void stopHeartBeat(){
		this.isRunning = false;
			
			this.heartThread.interrupt();
	
	}

	/**
	 * Stops the serversocket of the heartbeat
	 */
	public void stopServSocket(){
		try {
			heartBeatSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		}
	}
	
	/**
	 * Used to initate a normal disconnect by gui
	 * @param c -  initiate disconnect true or false
	 */
	public void setDisconnectInitiation(boolean c){
		this.initiateDisconnect = c;
	}
	
	/**
	 * 
	 * @return the state of the disconnect initiation
	 */
	public boolean getDisconnectInitiation(){
		return this.initiateDisconnect;
	}
	

}