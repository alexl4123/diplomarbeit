package GuiStuff;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import javax.swing.*;
import javax.swing.border.Border;

import BackgroundMatrix.*;
import GuiStuff.BoardGui.ChessField.ChessButton;
import meeple.*;
import musik.SoundMachine;
import threadJobs.TheThreadWhoIsAlwaysWaitingJob;
import threadJobs.ThreadForReading;

/**
 * This class includes the GUI, Save/Load handling and network stuff.
 */
public class BoardGui {


	BackgroundGrid BGG;										//Backgroundgrud on which the GUI is based
	JFrame ChessFrame = new JFrame("Chess Game");			
	ChessButton[][] Buttons;								//Array of chessbuttons (Spielfeld)
	JTextArea consoleField;									//little console stuff
	public ServerSocket servSock;							//Serversocket in case of host
	public Socket clientSock2=null;							//cllient socket
	ObjectOutputStream oos;									//streams																
	ObjectInputStream ois;
	static BoardGui BG;	
	public ChessField ChessPanel;
	boolean isHost;											//is the current player the host? 
	public JFrame Frame;			
	public JTextArea consolefield;							//little debugging console
	public boolean WhiteSchach;
	private JMenu network;									//JMenustuff
	public boolean BlackSchach;
	public JMenuItem host=new JMenuItem("Host Game");
	public JMenuItem connect=new JMenuItem("Join Game");
	public JMenuItem closeServ=new JMenuItem("Cut conncetion");
	public JMenuItem disconnect=new JMenuItem("Disconnect");
	public JButton player2 = new JButton();
	public JButton con2 = new JButton();
	JLabel turn1 = new JLabel();
	SoundMachine SM1; 
	private JFrame VolumeFrame;
	private Thread TH; 

/**
 * Defuault constructor which sets the Backgroundgrid.
 */
	public BoardGui(BackgroundGrid BGG2){
		BGG = BGG2;
		servSock=null;
		isHost=true;
		ChessFrame.setDefaultCloseOperation(ChessFrame.EXIT_ON_CLOSE);
		
	}
	
	/**
	 * Main method. Instances a Backgroundgrid and a Boardgui.
	 */
	public static void main(String[] args) {
		
		
		
		
		
		BackgroundGrid BGG = new BackgroundGrid();
		BG = new BoardGui(BGG);
		BG.Gui();

	}
	

	/**
	 * Creates the whole GUI.
	 */
	public void Gui(){
		
		SM1 = new SoundMachine("/musik/music7.wav",1);
		TH = new Thread(SM1);
		TH.start();
		URL url=BoardGui.class.getResource("/Images/gameIcon.png");    //setting icon
		ImageIcon GameIcon = new ImageIcon(url);					   
		ChessFrame.setIconImage(GameIcon.getImage());
		
			
				
		ChessFrame.setDefaultCloseOperation(ChessFrame.DO_NOTHING_ON_CLOSE);			//setting a new default close operation
		ChessFrame.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowClosing(WindowEvent arg0) {

				if(servSock!=null&&BGG.getLan().getIsConnectet()==true){				//closing all these server-things
					SoundMachine SM = new SoundMachine("/musik/menuClick.wav",0);
					Thread TH = new Thread(SM);
					TH.start();
					try{
						BG.getOOS().writeObject("end");
						System.out.println("end has been written! ");
						BG.getOIS().close();
						BG.getOOS().close();
						BG.servSock.close();



					}catch(Exception e){


						e.printStackTrace();

					}finally{

						System.exit(1);
					}


				}else if(servSock==null&&BGG.getLan().getIsConnectet()==true){				//closing all these client-socket things


					try{
						BG.getOOS().writeObject("end");
						BG.getOOS().flush();							
						BG.getOOS().close();
					}catch(Exception e2){

						

					}finally{

						System.exit(1);
					}




				}else if(BGG.getLan().getIsConnectet()==false){

					System.exit(1);

				}


			}

			@Override
			public void windowClosed(WindowEvent arg0) {
				SoundMachine SM = new SoundMachine("/musik/menuClick.wav",0);
				Thread TH = new Thread(SM);
				TH.start();

			}

			@Override
			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		
		
		

		
		
		ChessPanel = new ChessField();
		ChessFrame.add(ChessPanel);
		buildInfoPanel();
		buildMenu(ChessPanel);
		ChessFrame.setBounds(200, 200, 600, 600);
		ChessFrame.setVisible(true);								//building the panel
	}
	
	/**
	 * a frame for regulating the musik sound
	 */
	public void buildVolumeFrame(){
		VolumeFrame = new JFrame("Change VOL");
		JButton reduce = new JButton("-");
		JButton increase = new JButton("+");
		JButton mute = new JButton("mute");
		
		mute.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					
					SM1.muteVOL();
				}catch(Exception ex){
					System.out.println("no mute");
				}
				
				
			}
		});
		
		reduce.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				try{
					SM1.redurceVOl();
					
				}catch(Exception ex){
					
					
				}
				
				
				
			}
		});
		
		increase.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					SM1.increaseVOL();
				}catch(Exception ex){
					
				}
				
				
			}
		});
		
		Point pointer=ChessFrame.getLocation();
		int Xpos=(int) pointer.getX()+245;			//setting the window in the middle of the other frame
		int Ypos=(int) pointer.getY()+267;

		Point newPoint=new Point(Xpos, Ypos);

		
		VolumeFrame.setLocation(newPoint);
		
		VolumeFrame.setLayout(new FlowLayout());
		VolumeFrame.add(reduce);
		VolumeFrame.add(increase);
		VolumeFrame.add(mute);
		VolumeFrame.setSize( 110, 75);
		VolumeFrame.setResizable(false);
		VolumeFrame.setVisible(false);
		
		
	}
	
	private void setIJPG(int i){
		
		
	}

	
	public void buildInfoPanel(){
		JPanel InfoPanel = new JPanel();
		
		
		turn1.setText("Turn: "+((BGG.getTurnRound()/2)+1));
		
		
		
		JLabel	con1 = new JLabel();
		
		con1.setText("Connection Status: ");
		con2.setEnabled(false);
		if(BGG.getLan().getIsConnectet()==true){
			con2.setBackground(Color.green);
		}else{
			con2.setBackground(Color.red);
		}
		
		JLabel player1 = new JLabel();
		player1.setText("Turn: ");
		player2.setEnabled(false);
		if(BGG.getTeam()==true){
			player2.setBackground(Color.white);
		}else{
			player2.setBackground(Color.black);
		}
		InfoPanel.add(turn1);
		
		
		InfoPanel.add(con1);
		InfoPanel.add(con2);
		InfoPanel.add(player1);
		InfoPanel.add(player2);
		ChessFrame.add(InfoPanel,BorderLayout.SOUTH);
		
		
	}
	
	/**
	 * Builds the JMenu including network and SAVE/LOAD stuff.
	 */
	public void buildMenu(ChessField ChessPanel){
		JMenuBar jmbr=new JMenuBar();
		JMenu menFile=new JMenu("File");				//Menubar
		JMenuItem newGame=new JMenuItem("New");			//save-load things
		JMenuItem loadGame=new JMenuItem("Load");
		JMenuItem saveGame=new JMenuItem("Save");
		JMenu Sound = new JMenu("Sound");
		JMenuItem soundControl = new JMenuItem("Volume control");
		JMenuItem nextMusic = new JMenuItem("next Track");
		

		network=new JMenu("Network Game");




		//------------------------------------------------------------------------------------------------------		
		//Save - Load - STUFF HERE

		
		
		newGame.addActionListener(new ActionListener() {	//creates a new Backgroundgrid (a new game) and refreshes the panel

			@Override
			public void actionPerformed(ActionEvent e) {
				
				SoundMachine SM = new SoundMachine("/musik/menuClick.wav",0);
				Thread TH = new Thread(SM);
				TH.start();
				if(isHost==true&&BGG.getLan().getIsConnectet()==false){
					BackgroundGrid freshBgg=new BackgroundGrid();
					BGG=freshBgg;
					ChessPanel.renewPanel(0,0,false);
					if(BGG.getTeam()==true){
						player2.setBackground(Color.white);
					}else{
						player2.setBackground(Color.black);
					}
					int Turns = (BGG.getTurnRound()/2)+1;
					turn1.setText("This is turn: "+Turns);
				}
			}
		});
		//------------------------------------------------------------------------------------------------------		

		saveGame.addActionListener(new ActionListener() {				//saves the game
			
			@Override
			public void actionPerformed(ActionEvent e) {
				SoundMachine SM = new SoundMachine("/musik/menuClick.wav",0);
				Thread TH = new Thread(SM);
				TH.start();
				if(isHost==true){
					boolean temp=BGG.getLan().getIsConnectet();					//temporary connection state
					BGG.getLan().setIsConnectet(false);
					JFrame frmClickTheRight=new JFrame();				
					JFileChooser chooseFile = new JFileChooser();		
					chooseFile.showSaveDialog( frmClickTheRight);		//New file chooser for choosing the destination
					File saveFile = chooseFile.getSelectedFile();
					try{
						FileOutputStream fw=new FileOutputStream(saveFile);
						ObjectOutputStream oos=new ObjectOutputStream(fw);		//streams and exception handling
						oos.writeObject(BGG);
						oos.close();
						SM = new SoundMachine("/musik/menuClick.wav",0);
						TH = new Thread(SM);
						TH.start();

					}
					catch(Exception ex){
						JFrame Frame1 = new JFrame("a frame");						//POPUP
						JOptionPane.showMessageDialog(Frame1, "File has not been saved!", "Error", 0);
						SM = new SoundMachine("/musik/menuClick.wav",0);
						TH = new Thread(SM);
						TH.start();
					}
					BGG.getLan().setIsConnectet(temp);
				}
			}		
		});
		//------------------------------------------------------------------------------------------------------		


		loadGame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {	
				SoundMachine SM = new SoundMachine("/musik/menuClick.wav",0);
				Thread TH = new Thread(SM);
				TH.start();
				//loading games
				if(isHost==true&&BGG.getLan().getIsConnectet()==false){
					JFrame frmClickTheRight=new JFrame();
					JFileChooser chooseFile = new JFileChooser();
					chooseFile.showOpenDialog( frmClickTheRight); 			//file chooser
					File loadFile = chooseFile.getSelectedFile();
					try{
						FileInputStream fin=new FileInputStream(loadFile);
						ObjectInputStream ois=new ObjectInputStream(fin);	//input streams and exception handling
						BGG=(BackgroundGrid) ois.readObject();
						ChessPanel.renewPanel(0, 0,false);
						ois.close();
						if(BGG.getTeam()==true){
							player2.setBackground(Color.white);
						}else{
							player2.setBackground(Color.black);
						}
						int Turns = (BGG.getTurnRound()/2)+1;
						turn1.setText("This is turn: "+Turns);

					}
					catch(Exception ex){

					}
				}

			}
		});

		//-------------------------------------------------------------------------------------------------------		
		//-------------------------------------------------------------------------------------------------------
		//NETWORK-STUFF HERE


		closeServ.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {			//writes an end object and ends all connections
				SoundMachine SM = new SoundMachine("/musik/menuClick.wav",0);
				Thread TH = new Thread(SM);
				TH.start();
				try{
					BG.getOOS().writeObject("end");
					System.out.println("end has been written! ");
					BG.getOIS().close();
					BG.getOOS().close();





				}catch(Exception e1){




				}finally{

					try {
						BG.servSock.close();
					} catch (IOException e1) {

						e1.printStackTrace();
					}
					BG.servSock=null;								//ending connections
					BGG.getLan().setIsConnectet(false);
					BGG.setMove(true);
					Frame.setVisible(false);


				}


				network.remove(closeServ);

			}
		});
		//---------------------------------------------------------------------------------------------------------------
		host.addActionListener(new ActionListener() {			//hosts a game


			public void actionPerformed(ActionEvent arg0) {
				SoundMachine SM = new SoundMachine("/musik/menuClick.wav",0);
				Thread TH = new Thread(SM);
				TH.start();
				if(BGG.getLan().getIsConnectet()==false){				
					consoleField = new JTextArea(10,40);		//creating a little console
					Frame = new JFrame("Console");
					Frame.setResizable(false);


					Point pointer=ChessFrame.getLocation();
					int Xpos=(int) pointer.getX()+80;			//setting the window in the middle of the other frame
					int Ypos=(int) pointer.getY()+200;

					Point newPoint=new Point(Xpos, Ypos);

					Frame.setSize(500, 220);
					Frame.setLocation(newPoint);




					JPanel panel = new JPanel();		
					String consout="console>> ";
					Frame.setDefaultCloseOperation(Frame.DO_NOTHING_ON_CLOSE);

					JScrollPane scroller = new JScrollPane(consoleField);
					scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
					scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);			//scrolling panes!!
					panel.add(scroller);
					Frame.add(panel, BorderLayout.CENTER);
					consoleField.setLineWrap(true);
					consoleField.setEditable(false);
					Frame.setVisible(true);

					consoleField.append(consout+"trying to set up a network match...\n\n");

					try {
						servSock=new ServerSocket(7500);															//creating serversocket
					} catch (IOException e) {
						consoleField.append(consout+"ERROR while setting up the network match \n\n");
						e.printStackTrace();
					}


					consoleField.append(consout+"setting up the match was successfull \n\n");						

					TheThreadWhoIsAlwaysWaitingJob tj=new TheThreadWhoIsAlwaysWaitingJob(BG, servSock, BGG);		//starts the connection-listener
					Thread listen=new Thread(tj);
					listen.start();


					consoleField.append(consout+"waiting for a second player... \n\n");

					BGG.setMove(false);																				
					network.add(closeServ);																			//adding the close menu option
				}





			}
		});

		//----------------------------------------------------------------------------------------------------------------

		connect=new JMenuItem("Connect");												
		connect.addActionListener(new ActionListener() {


			public void actionPerformed(ActionEvent arg0) {		
				SoundMachine SM = new SoundMachine("/musik/menuClick.wav",0);
				Thread TH = new Thread(SM);
				TH.start();
				//connect to a host
				isHost=false;
				try {
					if(BGG.getLan().getIsConnectet()==false){

						JFrame ipFrame=new JFrame();

						Point pointer=ChessFrame.getLocation();
						int Xpos=(int) pointer.getX()+200;						//making the frame right in the middle of the other frame
						int Ypos=(int) pointer.getY()+200;
						ipFrame.setResizable(false);

						Point newPoint=new Point(Xpos, Ypos);

						ipFrame.setSize(200, 110);
						ipFrame.setLocation(newPoint);							
						JPanel northernPanel=new JPanel();						
						northernPanel.setLayout(new BoxLayout(northernPanel, BoxLayout.PAGE_AXIS));
						JPanel southernPanel=new JPanel();
						JLabel ipLabel=new JLabel("Enter IP-Address!");		//building this frame
						JTextField ipField=new JTextField();
						JButton IPButton=new JButton("Connect");
						JButton AbButton=new JButton("Abort");

						northernPanel.add(ipLabel);
						northernPanel.add(ipField);

						southernPanel.add(IPButton);
						southernPanel.add(AbButton);

						ipFrame.add(southernPanel, BorderLayout.SOUTH);
						ipFrame.add(northernPanel, BorderLayout.CENTER);		
						ipFrame.setVisible(true);
						ipFrame.setDefaultCloseOperation(ipFrame.DO_NOTHING_ON_CLOSE);

						IPButton.addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent arg0) {
								try{
									SoundMachine SM = new SoundMachine("/musik/menuClick.wav",0);
									Thread TH = new Thread(SM);
									TH.start();
									String address=ipField.getText();				//Sets IP input, connects to the host

									
									clientSock2=new Socket(address, 7500);

									BGG.getLan().setIsConnectet(true);
									oos=new ObjectOutputStream(clientSock2.getOutputStream());		//creating streams
									ois=new ObjectInputStream(clientSock2.getInputStream());
									BGG= (BackgroundGrid) ois.readObject();
									BG.ChessPanel.renewPanel(0, 0,false);							//renewing panel
									ipFrame.setVisible(false);
									ipField.setText("");
									
									BGG.setMove(true);
									
									if(BGG.getLan().getIsConnectet()==true){
										con2.setBackground(Color.green);
									}else{
										con2.setBackground(Color.red);
									}

								}
								catch(UnknownHostException e1){										//exception handling
									JOptionPane.showMessageDialog(ipFrame, "Connection refused - Host not reachable!", "ERROR", 0);
								}catch(IOException e2){
									JOptionPane.showMessageDialog(ipFrame, "Connection refused - Host not reachable!", "ERROR", 0);
								}catch(ClassNotFoundException e3){
									
								}

							}
						});

						AbButton.addActionListener(new ActionListener() {				//aborting button

							@Override
							public void actionPerformed(ActionEvent arg0) {
								SoundMachine SM = new SoundMachine("/musik/menuClick.wav",0);
								Thread TH = new Thread(SM);
								TH.start();
								ipFrame.setVisible(false);
								ipField.setText("");
								network.remove(disconnect);


							}
						});


					}



				} catch(Exception e){
					e.printStackTrace();
				}


				network.add(disconnect);	






			}
		});
		//----------------------------------------------------------------------------------------------------------------------------------


		disconnect.addActionListener(new ActionListener() {		//button for disconnecting

			@Override
			public void actionPerformed(ActionEvent e) {
				
				try{
					SoundMachine SM = new SoundMachine("/musik/menuClick.wav",0);
					Thread TH = new Thread(SM);
					TH.start();


					if(BGG.getLan().getIsConnectet()==true&&servSock==null){

						network.remove(disconnect);	
						try {	



							BG.getOOS().writeObject("end");
							BG.getOOS().flush();									//ending all the streams
							BG.getOOS().close();
							BGG.getLan().setIsConnectet(false);
							BGG.setMove(true);

						} catch (IOException e1) {
									
							
							
						}finally{



							consoleField.setText("");
							clientSock2.close();
							consoleField.append("streams closed \n\n");
							Frame.setVisible(false);
							if(BGG.getLan().getIsConnectet()==true){
								con2.setBackground(Color.green);
							}else{
								con2.setBackground(Color.red);
							}

						}


					}



				}catch (Exception ex){

				}

			}
		});
		
		//--------------------------------------------------------------------------------------------------------------------------------------
		
		soundControl.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				SoundMachine SM = new SoundMachine("/musik/menuClick.wav",0);
				Thread TH = new Thread(SM);
				TH.start();
				
				buildVolumeFrame();
				VolumeFrame.setVisible(true);
			}
		});
		//------------------------------------------------------------------------------------------------------------------------------------------
		
		nextMusic.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				//getSM1Thread().interrupt();
				SM1.killSound();
				
				SoundMachine SM = new SoundMachine("/musik/menuClick.wav",0);
				Thread TH = new Thread(SM);
				
				TH.start();
				
				
				
			}
		});
		
		//------------------------------------------------------------------------------------------------------------------------------------------
		
		
		Sound.add(nextMusic);
		Sound.add(soundControl);
		
		network.add(host);
		network.add(connect);
		menFile.add(newGame);
		menFile.add(saveGame);					//sticking the whole menu toghether 
		menFile.add(loadGame);
		
		jmbr.add(menFile);
		jmbr.add(network);
		jmbr.add(Sound);



		ChessFrame.setJMenuBar(jmbr);

	}

	public Thread getSM1Thread(){
		return TH;
	}
	
	public JMenu getNetworkMenu(){

		return network;

	}

	public void setClientSock(Socket s){
		clientSock2=s;
	}

	public ObjectOutputStream getOOS()
	{
		return oos;
	}

	public ObjectInputStream getOIS(){
		return ois;
	}


	/**
	 * Method which creates streams.
	 */
	public void createStreams(Socket client){
		try {

			oos=new ObjectOutputStream(client.getOutputStream());
			oos.flush();

			ois=new ObjectInputStream(client.getInputStream());



		} catch (IOException e) {
			e.printStackTrace();
		}


	}


/**
 * Method which lets the thread sleep
 */
	public void sleep(int time){
		try {

			TimeUnit.MILLISECONDS.sleep(time);


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets a new Backgroundgrid and refreshes the Panel (Synchronized for network - stuff).
	 * 
	 */
	public synchronized void setBackgroundGridRead(BackgroundGrid BGG1){
		BGG = BGG1;
		
		//ChessField ChessPanel = new ChessField();
		ChessPanel.renewPanel(0, 0,false);
		BGG.setMove(true);
	}

	public synchronized JTextArea getTextArea(){
		return consoleField;

	}

	
	public void makeANewReaderThread(){
		ThreadForReading tr=new ThreadForReading(BGG,BG);
		Thread listen2=new Thread(tr);
		listen2.start();
	}

	/**
	 * Extention of JPanel which contains the Play Area
	 *
	 */
	public class ChessField extends JPanel{

		int Chooser;
		Boolean Bauerntausch;
		
		
		/**
		 * Default constructor. 
		 */
		public ChessField(){
			Chooser = 0;
			Bauerntausch = false;
			Font f = new Font("Arial", Font.BOLD, 20);			//creating a new Font *~*
			Buttons = new ChessButton[9][9];					//Creating the actual field

			super.setLayout(new GridLayout(9, 9));


			for(int Y = 0; Y < 9; Y++){							
				for(int X = 0; X < 9; X++){						
					Buttons[X][Y] = new ChessButton(X,Y);
					//ChessButton ChessButton = new ChessButton(X,Y);

					//some cool maths...
					if((Y == 0)&&(X >= 0)){

						JLabel JL = new JLabel();
						JL.setHorizontalAlignment(0);
						JL.setFont(f);
						switch(X){
						case 0:{JL.setText("");}break;
						case 1:{JL.setText("A");}break;
						case 2:{JL.setText("B");}break;
						case 3:{JL.setText("C");}break;
						case 4:{JL.setText("D");}break;
						case 5:{JL.setText("E");}break;
						case 6:{JL.setText("F");}break;
						case 7:{JL.setText("G");}break;
						case 8:{JL.setText("H");}break;

						}
						add(JL);

					}else if((X == 0) && (Y >= 1)){
						JLabel JL = new JLabel();
						JL.setHorizontalAlignment(0);
						JL.setVerticalAlignment(0);
						JL.setFont(f);
						switch(Y){
						case 0:{JL.setText("");}break;
						case 1:{JL.setText("1");}break;
						case 2:{JL.setText("2");}break;
						case 3:{JL.setText("3");}break;
						case 4:{JL.setText("4");}break;
						case 5:{JL.setText("5");}break;
						case 6:{JL.setText("6");}break;
						case 7:{JL.setText("7");}break;
						case 8:{JL.setText("8");}break;	
						}

						add(JL);
						// working with the modulo funktion makes the "Rest" of a division
					}else{ 
						if(((Y%2 == 1)&&(X%2==1))||((Y%2==0)&&(X%2==0))){
							Buttons[X-1][Y-1].setBackground(Color.white);
							add(Buttons[X-1][Y-1]);									//setting colors
						}else{
							Buttons[X-1][Y-1].setBackground(Color.gray);
							add(Buttons[X-1][Y-1]);
						}

						int Back1 = BGG.getBackgroundGrid(X-1, Y-1);

						if(BGG.Objects(Back1) instanceof Farmer){
							Farmer TheFarmer = (Farmer) BGG.Objects(Back1);			//setting different icons for different meeples
							Buttons[X-1][Y-1].setIcon(TheFarmer.getIcon());

						}else if(BGG.Objects(Back1) instanceof King){
							King TheKing = (King) BGG.Objects(Back1);
							Buttons[X-1][Y-1].setIcon(TheKing.getIcon());
						}else if(BGG.Objects(Back1) instanceof Tower){
							Tower TheTower = (Tower) BGG.Objects(Back1);
							Buttons[X-1][Y-1].setIcon(TheTower.getIcon());
						}else if(BGG.Objects(Back1) instanceof Runner){
							Runner TheRunner = (Runner) BGG.Objects(Back1);
							Buttons[X-1][Y-1].setIcon(TheRunner.getIcon());
						}if(BGG.Objects(Back1) instanceof Queen){
							Queen khaleesi = (Queen) BGG.Objects(Back1);
							Buttons[X-1][Y-1].setIcon(khaleesi.getIcon());
						}if(BGG.Objects(Back1) instanceof Jumper){
							Jumper drogo=(Jumper)BGG.Objects(Back1);
							Buttons[X-1][Y-1].setIcon(drogo.getIcon());
						}

						/*
						 * asks the backgroundmatrix who is standing on that button (field)
						 */
						int XX = X; 
						int YY = Y;
						Buttons[X-1][Y-1].addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
								
							
								//Buttons[BGG.getX()][BGG.getY()].setBackground(Color.blue);
								
								SoundMachine SM = new SoundMachine("/musik/buttonClick.wav",0);
								Thread TH = new Thread(SM);
								TH.start();
								boolean moveMeeple = false;
								//System.out.println(BGG.getBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos()));
								//first try of a move (ONLY FOR TRY!!!)
								try{

									
									//following code will contain mathematical violence. If you are attend to a standard "gym", then DON`T LOOK AT THIS CODE!!
									

									/*
									 * check if a move is possible
									 * and if then rewrite the
									 * background matrix:
									 * Previous location = 0 (empty)
									 * Current Location = ID-OF-OBJECT (e.g.: 103 (a Pawn))
									 */
									if((BGG.getBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos())==0&&BGG.getMove()==true)){

										//move
										if(BGG.getObject() instanceof Farmer){  //check the Object and call the right move method

											Farmer TheFarmer = (Farmer) BGG.getObject();

											if(TheFarmer.move2(BGG.getX(), BGG.getY(), Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(),BGG)==true){
												BGG.setBackgroundGrid(BGG.getX(),BGG.getY(),0);
												BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), TheFarmer.getID());
												BGG.changeTeam();

												TheFarmer.setMeepleXPos(Buttons[XX][YY].getXPos());
												TheFarmer.setMeepleYPos(Buttons[XX][YY].getYPos());
												TheFarmer.setLastMovedInRound(BGG.getTurnRound());
												moveMeeple = true;

												//Bauerntausch
												if(TheFarmer.isteam()==true&&Buttons[XX][YY].getYPos()==7){
													
													Bauerntausch(true, XX,YY);
													Bauerntausch = true;

												}else if(TheFarmer.isteam()==false&&Buttons[XX][YY].getYPos()==0){
													Bauerntausch(false,XX,YY);
													Bauerntausch = true;

												}

											}//en Passant (enpassant)
											else if(BGG.Objects(BGG.getBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos()-1)) instanceof Farmer){

												Farmer TheFarmer2 = (Farmer) BGG.Objects(BGG.getBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos()-1));

												if((TheFarmer2.getHowOftenMoved()==1)&&(BGG.getTurnRound()-TheFarmer2.getLastMovedInRound()==1)&&(TheFarmer2.getSpecialMoved()==true)){

													if((TheFarmer.strike(BGG.getX(), BGG.getY(), Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), BGG.getTeam())==true)){
														BGG.setBackgroundGrid(BGG.getX(),BGG.getY(),0);
														BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), TheFarmer.getID());
														BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos()-1, 0);
														BGG.changeTeam();
														renewPanel(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos()-1, true);
														TheFarmer.setMeepleXPos(Buttons[XX][YY].getXPos());
														TheFarmer.setMeepleYPos(Buttons[XX][YY].getYPos());

														moveMeeple = true;
														TheFarmer.setLastMovedInRound(BGG.getTurnRound());


													}
												}
											}else if(BGG.Objects(BGG.getBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos()+1)) instanceof Farmer){

												Farmer TheFarmer2 = (Farmer) BGG.Objects(BGG.getBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos()+1));
												if((TheFarmer2.getHowOftenMoved()==1)&&(BGG.getTurnRound()-TheFarmer2.getLastMovedInRound()==1)&&(TheFarmer2.getSpecialMoved()==true)){

													if((TheFarmer.strike(BGG.getX(), BGG.getY(), Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), BGG.getTeam())==true)){
														BGG.setBackgroundGrid(BGG.getX(),BGG.getY(),0);
														BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), TheFarmer.getID());
														BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos()+1, 0);
														BGG.changeTeam();
														renewPanel(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos()+1, true);
														TheFarmer.setMeepleXPos(Buttons[XX][YY].getXPos());
														TheFarmer.setMeepleYPos(Buttons[XX][YY].getYPos());
														moveMeeple = true;
														TheFarmer.setLastMovedInRound(BGG.getTurnRound());


													}
												}
											}
										}

										else if(BGG.getObject() instanceof King){

											King TheKing = (King) BGG.getObject();

											if(TheKing.move(BGG.getX(), BGG.getY(), Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), BGG.getTeam())==true){
												BGG.setBackgroundGrid(BGG.getX(),BGG.getY(),0);

												BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), TheKing.getID());
												BGG.changeTeam();
												TheKing.setMeepleXPos(Buttons[XX][YY].getXPos());
												TheKing.setMeepleYPos(Buttons[XX][YY].getYPos());
												moveMeeple = true;
											}else if(!(TheKing.getSchach())&&(TheKing.getHowOftenMoved()<1)){

												int dX = Buttons[XX][YY].getXPos() - BGG.getX();
												int dY = Buttons[XX][YY].getYPos() - BGG.getY();
												boolean Team;
												boolean go=false;
												boolean go2 = false;
												int iF = 0;

												//for the right rochade
												if((dY==0)&&(dX==2)){


													for(int i = 1; i<4;i++){
														int X = BGG.getX() + i;

														if(BGG.SchachKing(BGG.getTeam(), BGG, X, BGG.getY(), false, false)==false){

															if(BGG.getBackgroundGrid(X,BGG.getY())==0){
																go2 = true;
															}else{
																System.out.println("yes");
																go2 = false;
																go = false;
																break;
															}
															iF++;
															if(iF==3&&go2){
																go = true;
																break;
															}

														}
													}

													if(go==true){
														if(TheKing.getID()==145){
															Tower TheTower = (Tower) BGG.Objects(110);
															if(TheTower.getHowOftenMoved()<1){

																BGG.setBackgroundGrid(BGG.getX(),BGG.getY(),0);
																BGG.setBackgroundGrid(Buttons[XX][YY].getXPos()+1, Buttons[XX][YY].getYPos(), 0);
																BGG.setBackgroundGrid(Buttons[XX][YY].getXPos()+2, Buttons[XX][YY].getYPos(), 0);
																BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), TheKing.getID());
																BGG.setBackgroundGrid(Buttons[XX][YY].getXPos()-1, Buttons[XX][YY].getYPos(), TheTower.getID());
																BGG.changeTeam();
																TheTower.setMeepleXPos(Buttons[XX][YY].getXPos()-1);
																TheTower.setMeepleYPos(Buttons[XX][YY].getYPos());
																TheKing.setMeepleXPos(Buttons[XX][YY].getXPos());
																TheKing.setMeepleYPos(Buttons[XX][YY].getYPos());
																renewPanel(Buttons[XX][YY].getXPos()+2, Buttons[XX][YY].getYPos(), true);
																renewPanel(Buttons[XX][YY].getXPos()+1, Buttons[XX][YY].getYPos(), true);
																renewPanel(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), true);
																renewPanel(BGG.getX(), BGG.getY(),true);
																moveMeeple = true;
															}
														}else if(TheKing.getID()==245){
															Tower TheTower = (Tower) BGG.Objects(210);
															if(TheTower.getHowOftenMoved()<1){

																BGG.setBackgroundGrid(BGG.getX(),BGG.getY(),0);
																BGG.setBackgroundGrid(Buttons[XX][YY].getXPos()+1, Buttons[XX][YY].getYPos(), 0);
																BGG.setBackgroundGrid(Buttons[XX][YY].getXPos()+2, Buttons[XX][YY].getYPos(), 0);
																BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), TheKing.getID());
																BGG.setBackgroundGrid(Buttons[XX][YY].getXPos()-1, Buttons[XX][YY].getYPos(), TheTower.getID());
																BGG.changeTeam();
																TheTower.setMeepleXPos(Buttons[XX][YY].getXPos()-1);
																TheTower.setMeepleYPos(Buttons[XX][YY].getYPos());
																TheKing.setMeepleXPos(Buttons[XX][YY].getXPos());
																TheKing.setMeepleYPos(Buttons[XX][YY].getYPos());
																renewPanel(Buttons[XX][YY].getXPos()+2, Buttons[XX][YY].getYPos(), true);
																renewPanel(Buttons[XX][YY].getXPos()+1, Buttons[XX][YY].getYPos(), true);
																renewPanel(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), true);
																renewPanel(BGG.getX(), BGG.getY(),true);
																moveMeeple = true;
															}
														}
													}
													//for the left rochade
												}else if((dY==0)&&(dX==-2)){

													for(int i = 1; i<3;i++){
														int X = BGG.getX() - i;


														if(BGG.SchachKing(BGG.getTeam(), BGG, X, BGG.getY(), false, false)==false){

															if(BGG.getBackgroundGrid(X,BGG.getY())==0){
																go2 = true;
															}else{
																go2 = false;
																go = false;
																break;
															}
															iF++;
															if(iF==2&&go2==true){
																go = true;
																break;
															}

														}
													}

													if(go==true){
														if(TheKing.getID()==145){
															Tower TheTower = (Tower) BGG.Objects(110);
															if(TheTower.getHowOftenMoved()<1){

																BGG.setBackgroundGrid(BGG.getX(),BGG.getY(),0);
																BGG.setBackgroundGrid(Buttons[XX][YY].getXPos()-1, Buttons[XX][YY].getYPos(), 0);
																BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), TheKing.getID());
																BGG.setBackgroundGrid(Buttons[XX][YY].getXPos()+1, Buttons[XX][YY].getYPos(), TheTower.getID());
																BGG.changeTeam();
																TheTower.setMeepleXPos(Buttons[XX][YY].getXPos()+1);
																TheTower.setMeepleYPos(Buttons[XX][YY].getYPos());
																TheKing.setMeepleXPos(Buttons[XX][YY].getXPos());
																TheKing.setMeepleYPos(Buttons[XX][YY].getYPos());
																renewPanel(Buttons[XX][YY].getXPos()-1, Buttons[XX][YY].getYPos(), true);
																renewPanel(Buttons[XX][YY].getXPos()+1, Buttons[XX][YY].getYPos(), true);
																renewPanel(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), true);
																moveMeeple = true;
															}
														}else if(TheKing.getID()==245){
															Tower TheTower = (Tower) BGG.Objects(210);
															if(TheTower.getHowOftenMoved()<1){

																BGG.setBackgroundGrid(BGG.getX(),BGG.getY(),0);
																BGG.setBackgroundGrid(Buttons[XX][YY].getXPos()-1, Buttons[XX][YY].getYPos(), 0);
																BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), TheKing.getID());
																BGG.setBackgroundGrid(Buttons[XX][YY].getXPos()+1, Buttons[XX][YY].getYPos(), TheTower.getID());
																BGG.changeTeam();
																TheTower.setMeepleXPos(Buttons[XX][YY].getXPos()+1);
																TheTower.setMeepleYPos(Buttons[XX][YY].getYPos());
																TheKing.setMeepleXPos(Buttons[XX][YY].getXPos());
																TheKing.setMeepleYPos(Buttons[XX][YY].getYPos());
																renewPanel(Buttons[XX][YY].getXPos()-1, Buttons[XX][YY].getYPos(), true);
																renewPanel(Buttons[XX][YY].getXPos()+1, Buttons[XX][YY].getYPos(), true);
																renewPanel(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), true);
																moveMeeple = true;
															}
														}
													}

												}
											}


										}else if(BGG.getObject() instanceof Tower){

											Tower TheTower = (Tower) BGG.getObject();

											if(TheTower.move2(BGG.getX(), BGG.getY(), Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(),BGG)==true){
												BGG.setBackgroundGrid(BGG.getX(),BGG.getY(),0);
												BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), TheTower.getID());
												BGG.changeTeam();
												TheTower.setMeepleXPos(Buttons[XX][YY].getXPos());
												TheTower.setMeepleYPos(Buttons[XX][YY].getYPos());
												moveMeeple = true;

											}
										}else if(BGG.getObject() instanceof Runner){

											Runner TheRunner = (Runner) BGG.getObject();

											if(TheRunner.move2(BGG.getX(), BGG.getY(), Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(),BGG)==true){

												BGG.setBackgroundGrid(BGG.getX(),BGG.getY(),0);
												BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), TheRunner.getID());
												BGG.changeTeam();

												TheRunner.setMeepleXPos(Buttons[XX][YY].getXPos());
												TheRunner.setMeepleYPos(Buttons[XX][YY].getYPos());
												moveMeeple = true;

											}
										}else if(BGG.getObject() instanceof Queen){

											Queen khaleesi = (Queen) BGG.getObject();

											if(khaleesi.move2(BGG.getX(), BGG.getY(), Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(),BGG)==true){
												BGG.setBackgroundGrid(BGG.getX(),BGG.getY(),0);
												BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), khaleesi.getID());
												BGG.changeTeam();
												khaleesi.setMeepleXPos(Buttons[XX][YY].getXPos());
												khaleesi.setMeepleYPos(Buttons[XX][YY].getYPos());
												moveMeeple = true;

											}
										}else if(BGG.getObject() instanceof Jumper){

											Jumper drogo = (Jumper) BGG.getObject();

											if(drogo.move(BGG.getX(), BGG.getY(), Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), BGG.getTeam())==true){
												BGG.setBackgroundGrid(BGG.getX(),BGG.getY(),0);

												BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), drogo.getID());
												BGG.changeTeam();
												drogo.setMeepleXPos(Buttons[XX][YY].getXPos());
												drogo.setMeepleYPos(Buttons[XX][YY].getYPos());
												moveMeeple = true;

											}
										}



									}else if(Math.abs(BGG.getBackgroundGrid(Buttons[XX][YY].getXPos(),Buttons[XX][YY].getYPos())-BGG.getBackgroundGrid(BGG.getX(), BGG.getY()))>49&&BGG.getMove()==true){ //strike
										if(BGG.getObject() instanceof Farmer){

											Farmer TheFarmer = (Farmer) BGG.getObject();
											if((TheFarmer.strike(BGG.getX(), BGG.getY(), Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), BGG.getTeam())==true)){
												
												BGG.setBackgroundGrid(BGG.getX(),BGG.getY(),0);
												BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), TheFarmer.getID());
												BGG.changeTeam();
												TheFarmer.setMeepleXPos(Buttons[XX][YY].getXPos());
												TheFarmer.setMeepleYPos(Buttons[XX][YY].getYPos());
												moveMeeple = true;
												TheFarmer.setLastMovedInRound(BGG.getTurnRound());
												
												SM = new SoundMachine("/musik/strike.wav",0);
												TH = new Thread(SM);
												TH.start();
												
												//Bauerntausch
												if(TheFarmer.isteam()==true&&Buttons[XX][YY].getYPos()==7){
													
													Bauerntausch(true, XX,YY);
													Bauerntausch = true;

												}else if(TheFarmer.isteam()==false&&Buttons[XX][YY].getYPos()==0){
													Bauerntausch(false,XX,YY);
													Bauerntausch = true;

												}

											}

										}else if(BGG.getObject() instanceof King){

											King TheKing = (King) BGG.getObject();
											if((TheKing.strike(BGG.getX(), BGG.getY(), Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), BGG.getTeam())==true)){
												BGG.setBackgroundGrid(BGG.getX(),BGG.getY(),0);
												BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), TheKing.getID());
												BGG.changeTeam();
												TheKing.setMeepleXPos(Buttons[XX][YY].getXPos());
												TheKing.setMeepleYPos(Buttons[XX][YY].getYPos());
												moveMeeple = true;
												SM = new SoundMachine("/musik/strike.wav",0);
												TH = new Thread(SM);
												TH.start();
											}

										}else if(BGG.getObject() instanceof Tower){

											Tower TheTower = (Tower) BGG.getObject();

											if(TheTower.move2(BGG.getX(), BGG.getY(), Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(),BGG)==true){
												BGG.setBackgroundGrid(BGG.getX(),BGG.getY(),0);
												BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), TheTower.getID());
												BGG.changeTeam();
												TheTower.setMeepleXPos(Buttons[XX][YY].getXPos());
												TheTower.setMeepleYPos(Buttons[XX][YY].getYPos());
												moveMeeple = true;
												SM = new SoundMachine("/musik/strike.wav",0);
												TH = new Thread(SM);
												TH.start();
											}
										}else if(BGG.getObject() instanceof Runner){

											Runner TheRunner = (Runner) BGG.getObject();

											if(TheRunner.move2(BGG.getX(), BGG.getY(), Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(),BGG)==true){
												BGG.setBackgroundGrid(BGG.getX(),BGG.getY(),0);
												BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), TheRunner.getID());
												BGG.changeTeam();

												TheRunner.setMeepleXPos(Buttons[XX][YY].getXPos());
												TheRunner.setMeepleYPos(Buttons[XX][YY].getYPos());
												moveMeeple = true;
												SM = new SoundMachine("/musik/strike.wav",0);
												TH = new Thread(SM);
												TH.start();

											}
										}else if(BGG.getObject() instanceof Queen){

											Queen khaleesi = (Queen) BGG.getObject();

											if(khaleesi.move2(BGG.getX(), BGG.getY(), Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(),BGG)==true){
												BGG.setBackgroundGrid(BGG.getX(),BGG.getY(),0);
												BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), khaleesi.getID());
												BGG.changeTeam();
												khaleesi.setMeepleXPos(Buttons[XX][YY].getXPos());
												khaleesi.setMeepleYPos(Buttons[XX][YY].getYPos());
												moveMeeple = true;
												SM = new SoundMachine("/musik/strike.wav",0);
												TH = new Thread(SM);
												TH.start();
											}
										}else if(BGG.getObject() instanceof Jumper){

											Jumper drogo = (Jumper) BGG.getObject();
											if((drogo.move(BGG.getX(), BGG.getY(), Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), BGG.getTeam())==true)){
												BGG.setBackgroundGrid(BGG.getX(),BGG.getY(),0);
												BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), drogo.getID());
												BGG.changeTeam();
												drogo.setMeepleXPos(Buttons[XX][YY].getXPos());
												drogo.setMeepleYPos(Buttons[XX][YY].getYPos());
												moveMeeple = true;
												SM = new SoundMachine("/musik/strike.wav",0);
												TH = new Thread(SM);
												TH.start();

											}
										}



									}//the other meeples

								}catch(Exception ex){

								}


								//renew the panel and send data

								if(moveMeeple == true&&Bauerntausch == false){
									
									connectionWrite();
									BGG.higherTurnRound();
									renewPanel(XX,YY,false);
									getSchach();
									int Turns = (BGG.getTurnRound()/2)+1;
										turn1.setText("This is turn: "+Turns);
									if(BGG.getTeam()==true){
										player2.setBackground(Color.white);
									}else{
										player2.setBackground(Color.black);
									}
									if(BGG.getLan().getIsConnectet()==true){
										con2.setBackground(Color.green);
									}else{
										con2.setBackground(Color.red);
									}
								}
								//transmit selected field
								int Back = BGG.getBackgroundGrid(XX-1,YY-1);
								if(BGG.getBackgroundGrid(XX-1, YY-1)>0){
									BGG.setXY(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos());
									BGG.setObject(BGG.Objects(Back));
								}
								if(BGG.getSchachmattWhite()==true){
									try{
										SM1.redurceVOl();
										SM1.redurceVOl();
										SM1.redurceVOl();
									}catch(Exception ex){
										
									}
									
									int Turns = BGG.getTurnRound()/2;
									JOptionPane.showMessageDialog(ChessFrame, "Game Over; The game lastet: "+(Turns+1)+" turns.", "Schachmatt", 1);
									
								}


							}



						});

					}

				}

			}

		}

		/**
		 * Bauerntausch: if a pawn gets to the other side of the fied, than the player can change him
		 * to a Queen, Jumper, Runner of tower.
		 * @param team-for which team
		 * @param XX-on which pos.
		 * @param YY-on which pos.
		 */
		public void Bauerntausch(boolean team,int XX,int YY){
			
			JFrame ChooserF = new JFrame("which meeple?");
			Point pointer=ChessFrame.getLocation();
			int Xpos=(int) pointer.getX()+200;
			int Ypos=(int) pointer.getY()+200;
			ChooserF.setResizable(false);

			Point newPoint=new Point(Xpos, Ypos);

			ChooserF.setSize(200, 110);
			ChooserF.setLocation(newPoint);

			String[] Meeples = {"Queen","Jumper","Runner","Tower"};
			JComboBox JBox = new JComboBox(Meeples); //a combo box with all .sav�s

			//save or load

			JButton JBut = new JButton("Choose");

			JPanel southPanel = new JPanel();
			JPanel northPanel = new JPanel();

			northPanel.add(JBox);
			southPanel.add(JBut);

			JBut.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					int Number = 0;
					String NoOne = (String)JBox.getSelectedItem();
					int WhichOne = 0;




					if(NoOne=="Queen"){

						if(BGG.getQueenNumber()<4){
						
							Chooser = 1;
							BGG.setMove(true);

							if(team == true){
								BGG.higherQueenNumber();
								Number = BGG.getQueenNumber();
								BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), Number+140);
								Queen khaleesi = new Queen(true, Number+140, Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos());
								BGG.Objectives.set(Number+140, khaleesi);							
							}else{
								BGG.higherQueenNumber();
								Number = BGG.getQueenNumber();
								BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), Number+240);
								Queen khaleesi = new Queen(false, Number+240, Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos());
								BGG.Objectives.set(Number+240, khaleesi);
							}
						}else{
							JFrame Frame1 = new JFrame("");
							JOptionPane.showMessageDialog(Frame1, "No more Queens available! Choose another one!", "OTHER MEEPLE", 0);	
							Bauerntausch(team,0,0);
						}







					}else if(NoOne=="Jumper"){
					
						Chooser = 2;

						if(team == true){
							BGG.higherJumperNumber();
							Number = BGG.getJumperNumber();
							BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), Number+121);
							Jumper drogo = new Jumper(true, Number+121, Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos());
							BGG.Objectives.set(Number+121, drogo);	
						}else{
							BGG.higherJumperNumber();
							Number = BGG.getJumperNumber();
							BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), Number+221);
							Jumper drogo = new Jumper(false, Number+221, Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos());
							BGG.Objectives.set(Number+221, drogo);
						}

					
					}else if(NoOne == "Runner"){
						
						Chooser=3;
						
						if(team == true){
							BGG.higherRunnerNumber();
							Number = BGG.getRunnerNumber();
							BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), Number+131);
							Runner runni = new Runner(true, Number+131, Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos());
							BGG.Objectives.set(Number+131, runni);
						}else{
							BGG.higherRunnerNumber();
							Number = BGG.getRunnerNumber();
							BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), Number+231);
							Runner runni = new Runner(false, Number+231, Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos());
							BGG.Objectives.set(Number+231, runni);
						}


					}else if (NoOne=="Tower"){
					
						Chooser = 4;
						

						if(team == true){
							BGG.higherTowerNumber();
							Number = BGG.getTowerNumber();
							BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), Number+111);
							Tower TheRedKeep = new Tower(true, Number+111, Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos());
							BGG.Objectives.set(Number+111, TheRedKeep);
						}else{
							BGG.higherTowerNumber();
							Number = BGG.getTowerNumber();
							BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), Number+211);
							Tower TheRedKeep = new Tower(false, Number+211, Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos());
							BGG.Objectives.set(Number+211, TheRedKeep);
						}


					}
					Bauerntausch = false;
					connectionWrite();
					BGG.higherTurnRound();
					renewPanel(XX,YY,false);
					getSchach();
					renewPanel(0, 0, false);
					ChooserF.setVisible(false);

				}
			});



			ChooserF.add(northPanel, BorderLayout.NORTH);
			ChooserF.add(southPanel, BorderLayout.SOUTH);
			ChooserF.setUndecorated(true);
			ChooserF.setVisible(true);


		}


		/*
		 * After moving setting Move = false;
		 * then starting thread job (ThreadForReading)
		 * this should read until the other player moved
		 */
		public void connectionWrite(){
			try{
				if(BGG.getLan().getIsConnectet()==true){

					if(BGG.getMove()==true){

						BGG.setMove(false);

						oos.writeObject(BGG);


						ThreadForReading tr=new ThreadForReading(BGG,BG);
						Thread listen2=new Thread(tr);
						listen2.start();

						BGG.setMove(false);
					}
				}

			}catch(Exception ex){

				try {

					JFrame Frame1 = new JFrame("a frame");

					BG.getOIS().close();
					BG.getOOS().close();
					BG.clientSock2.close();

					JOptionPane.showMessageDialog(Frame1, "The connection has been interrupted!", "CONNECTION INTERUPTION", 0);	
					BG.network.remove(BG.disconnect);

				} catch (IOException e) {



					JFrame Frame1 = new JFrame("a frame");
					JOptionPane.showMessageDialog(Frame1, "The client connection has been interrupted!", "CONNECTION INTERUPTION", 0);
					
					BG.network.remove(BG.disconnect);

				}

				BG.clientSock2=null;
				BGG.getLan().setIsConnectet(false);if(BGG.getLan().getIsConnectet()==true){
						con2.setBackground(Color.green);
					}else{
						con2.setBackground(Color.red);
					}
				if(BGG.getSchachmattWhite()==true){
					BGG.setMove(false);
				}else{
					BGG.setMove(true);
				}
				
			}


		}

		/**
		 * To renew the panel (if a move has occured)
		 * @param XX-which field want to be renewed
		 * @param YY-which field want to be renewed
		 * @param special-if a specific field has to be upgraded
		 */
		public void renewPanel(int XX, int YY, boolean special){

			for(int Y = 0; Y < 8; Y++){
				for(int X = 0; X < 8; X++){
					try{
						int Back1 = BGG.getBackgroundGrid(X, Y);
						if(BGG.Objects(Back1) instanceof Farmer){
							Farmer TheFarmer2 = (Farmer) BGG.Objects(Back1);
							Buttons[X][Y].setIcon(TheFarmer2.getIcon());

						}else if(BGG.Objects(Back1) instanceof King){
							King TheKking = (King) BGG.Objects(Back1);
							Buttons[X][Y].setIcon(TheKking.getIcon());

						}else if(BGG.Objects(Back1) instanceof Tower){
							Tower TheTowerOfJoy = (Tower) BGG.Objects(Back1);
							Buttons[X][Y].setIcon(TheTowerOfJoy.getIcon());

						}else if(BGG.Objects(Back1) instanceof Runner){
							Runner TheRunner = (Runner) BGG.Objects(Back1);
							Buttons[X][Y].setIcon(TheRunner.getIcon());

						}else if(BGG.Objects(Back1) instanceof Queen){
							Queen khaleesi = (Queen) BGG.Objects(Back1);
							Buttons[X][Y].setIcon(khaleesi.getIcon());

						}else if(BGG.Objects(Back1) instanceof Jumper){
							Jumper drogo = (Jumper) BGG.Objects(Back1);
							Buttons[X][Y].setIcon(drogo.getIcon());
						}

						/*
						 * Conversations (DON�T DELETE!!!): 
						 * 
						 * Soooo much space for meeples!!!<3
						 * (?)
						 *  do you even woman?
						 *  yes i are! 
						 *  
						 *  
						 *  some men look at children and think they are cute...
						 *  some just determine those in: can bite, don�t bite...
						 */




						else if(BGG.getBackgroundGrid(BGG.getX(), BGG.getY())<1){
							if(special==true){
								Buttons[XX][YY].setIcon(null);
							}
							Buttons[BGG.getX()][BGG.getY()].setIcon(null);
							Buttons[X][Y].setIcon(null);
						}else{
							Buttons[X][Y].setIcon(null);
						}

					}catch(Exception ex){

					}
				}
			}
		}

		/**
		 * to check if a king is in "Schach".
		 * 
		 */
		public void getSchach(){
			JFrame Frame1 = new JFrame();
			BlackSchach = BGG.SchachKing(false, BGG,50,50,true,false);
			if(BlackSchach == true){

				JOptionPane.showMessageDialog(Frame1, "Black is in Schach!", "Schach", 1);
				//System.out.println("Black is in Schach");
			}
			WhiteSchach = BGG.SchachKing(true, BGG,50,50,true,false);
			if(WhiteSchach == true){
				JOptionPane.showMessageDialog(Frame1, "White is in Schach!", "Schach", 1);
				//System.out.println("White is in Schach");
			}

		}



		/**
		 * The Buttons for the playing area
		 * => every meeple stands on a button 
		 * for move: click on a button (selected)
		 * and then on the field you want to move
		 *
		 */
		public class ChessButton extends JButton{

			//X and Y pos of the Button
			int X;
			int Y;

			/**
			 * Sets the correct X and Y pos 
			 * @param X2
			 * @param Y2
			 */
			public ChessButton(int X2,int Y2){
				X = X2-1;
				Y = Y2-1;

			}

			/**
			 * returns the X pos
			 * @return
			 */
			public int getXPos(){
				return X;
			}

			/**
			 * returns the Y pos
			 * @return
			 */
			public int getYPos(){
				return Y;
			}

			/**
			 * methode for deleting the icon of a specific button
			 * @param X1-XPos
			 * @param Y1-YPos
			 */
			public void noIcon(int X1, int Y1){
				setIcon(null);
			}


		}

	}

}