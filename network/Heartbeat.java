package network;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import javafx.beans.property.IntegerProperty;

public class Heartbeat implements Runnable {

	public static Thread heartThread;
	private boolean isHoster, isRunning, firsttry, initiateDisconnect;
	private ServerSocket heartBeatSocket;
	private IntegerProperty trigger;
	private InetAddress Clientadress;
	private Socket clientHeartSocket;
	public ObjectOutputStream heartWriteStream;
	public ObjectInputStream heartReadStream;


	public Heartbeat(InetAddress Clientadress, boolean host, IntegerProperty trigger){

		
		this.Clientadress = Clientadress;
		this.isHoster = host;
		this.trigger = trigger;
		initiateDisconnect = false;



	}


	@Override
	public void run() {

		try {
			if(isHoster == false){

				
				System.out.println("settintHeartClientSock");
				clientHeartSocket = new Socket(Clientadress, 23420);
				createHeartbeatStreams(clientHeartSocket);
				firsttry = true;
				clientHeartSocket.setSoTimeout(15000);
			}



			else if(isHoster == true){

				System.out.println("settingHostheartSock");
				heartBeatSocket = new ServerSocket(23420);
				clientHeartSocket=heartBeatSocket.accept();
				createHeartbeatStreams(clientHeartSocket);
				clientHeartSocket.setSoTimeout(15000);

			}
			int testnumber = 0;
			isRunning = true;
			while(isRunning){



				if(isHoster == false && firsttry == true){

					
					System.out.println("Trying first stream");
					firsttry = false;
					heartReadStream.readInt();
					System.out.println("ReadheartBeat");
				}

				testnumber = 1337;
				Thread.sleep(1000);
				heartWriteStream.writeInt(testnumber);
				heartWriteStream.flush();
		//		System.out.println("WroteheartBeat");
				Thread.sleep(1000);
				heartReadStream.readInt();
		//		System.out.println("ReadheartBeat");

				Thread currentThread = Thread.currentThread();
				currentThread.sleep(5);

			}


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

	private void createHeartbeatStreams(Socket tempSock) throws IOException{


		System.out.println("trying to create Heartstreams");


		this.heartWriteStream = new ObjectOutputStream(tempSock.getOutputStream());

		System.out.println("HeartStream one created");

		this.heartWriteStream.flush();

		heartReadStream = new ObjectInputStream(tempSock.getInputStream());
		
		


		System.out.println("HeartStream two created");
	}
	
	public void stopHeartBeat(){
		this.isRunning = false;
		//this.heartThread.interrupt();
		
			
			this.heartThread.interrupt();
	
	}
	
	public void stopServSocket(){
		try {
			heartBeatSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		}
	}
	
	public void setDisconnectInitiation(boolean c){
		this.initiateDisconnect = c;
	}
	
	public boolean getDisconnectInitiation(){
		return this.initiateDisconnect;
	}
	

}