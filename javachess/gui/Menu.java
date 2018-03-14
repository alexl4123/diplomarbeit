package javachess.gui;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;
import javachess.backgroundmatrix.BackgroundGrid;
import javachess.launchpad.Launchpad;
import javachess.network.Heartbeat;
import javachess.network.ReadingJob;
import javachess.network.hostingJob;
import javachess.saveload.Load;
import javachess.saveload.Save;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;

/**
 * @author alex - 2017
 * @version 1.1 - Draw
 *
 *
 *	Class Menu extends MenuBar
 *	Here all the menu components come toghether
 */
public class Menu extends MenuBar {

	/**
	 * GameMode0 - if GameMode Local has been selected
	 * GameMode1 - if GameMode Lan has been selected
	 * GameMode2 - if GameMode AI has been selected
	 */
	public RadioMenuItem GameMode0, GameMode1, GameMode2, GameMode4, GameMode5, soundMute; //once upon a time, GameMode3 was the black AI...
	public MenuItem newGame, Save, Load, Exit, disconnect, setUp, about_menu, help_menu, Draw;
	int _GM;
	public ReadingJob rj;
	public hostingJob hostJob;

	public javafx.scene.control.Menu menuFile;
	public javafx.scene.control.Menu menuGame;
	public javafx.scene.control.Menu menuOther;
	private PopUp pp;
	private About about;
	private Help help;




	/**
	 * the constructor builds the GUI
	 * @param Gui - for adding to Application purposes
	 */
	public Menu(GUI Gui){

		//Menus with Submenus 
		menuFile = new javafx.scene.control.Menu("File");
		menuGame = new javafx.scene.control.Menu("Gamemodes");
		menuOther = new javafx.scene.control.Menu("Other");

		//Adding Menu Items
		newGame = new MenuItem("New");
		Save = new MenuItem("Save");
		Load = new MenuItem("Load");
		Exit = new MenuItem("Exit");
		disconnect = new MenuItem("Disconnect");
		setUp = new MenuItem("Setup");

		about_menu = new MenuItem("About");
		help_menu = new MenuItem("Help");
		//refresh = new MenuItem("DEBUG/Refresh");

		//RadioButton Items
		GameMode0 = new RadioMenuItem("HotSeat");
		GameMode1 = new RadioMenuItem("Online");
		GameMode2 = new RadioMenuItem("Computer");
		//RIP Black AI
		GameMode4 = new RadioMenuItem("AI vs AI"); 
		GameMode5 = new RadioMenuItem("Launchpad");
		soundMute = new RadioMenuItem("Mute Sound");

		//For Game Menu Items
		Draw = new MenuItem("Draw");

		pp = new PopUp(Gui);
		pp.display();
		about = new About(Gui);
		about.display();
		help = new Help(Gui);
		help.display();

		//RadioButton - Group
		ToggleGroup group = new ToggleGroup();
		GameMode0.setToggleGroup(group);
		GameMode1.setToggleGroup(group);
		GameMode2.setToggleGroup(group);
		//RIP Black AI
		GameMode0.setSelected(true);

		//SoundGroup
		ToggleGroup soundGroup = new ToggleGroup();
		soundMute.setToggleGroup(soundGroup);



		menuFile.getItems().addAll(newGame, Save, Load, Exit);						//delete refresh when publish
		menuGame.getItems().addAll(GameMode0, GameMode1, GameMode2, GameMode5);
		menuOther.getItems().addAll(setUp,about_menu,help_menu,Draw);

		this.getMenus().addAll(menuFile, menuGame, menuOther);



		//-------------------------------------------------------------------------------------------------
		//File Menu

		//new Game
		newGame.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Gui.getBoardGui().soundPlayer.playSound("menu");
				Gui.newBG();
				Gui.getBoardGui().setHighlighting(true);
			}
		});

		//Exit
		Exit.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				Gui.getBoardGui().soundPlayer.playSound("menu");
				System.exit(1);

			}
		});

		//SoundMute

		soundMute.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) {
				if(Gui.getBoardGui().soundPlayer.getIsMuted()==false){
					Gui.getBoardGui().soundPlayer.setIsMuted(true);
					soundMute.setSelected(true);
				} else {
					Gui.getBoardGui().soundPlayer.setIsMuted(false);
					soundMute.setSelected(false);
					Gui.getBoardGui().soundPlayer.playSound("menu");

				}
			}
		});

		//Calling Popup
		setUp.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				Gui.getBoardGui().soundPlayer.playSound("menu");
				pp.showPopUpWindow();


			}
		});

		//About
		about_menu.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				Gui.getBoardGui().soundPlayer.playSound("menu");
				about.showAboutWindow();

			}
		});

		//Help
		help_menu.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				Gui.getBoardGui().soundPlayer.playSound("menu");
				help.showHelpWindow();

			}
		});





		//disconnect
		disconnect.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {		

				Gui.getBoardGui().soundPlayer.playSound("menu");
				Gui.getBoardGui().heartBeatJob.setDisconnectInitiation(true);
				Gui.getBoardGui().heartBeatJob.stopHeartBeat();
				Gui.getBoardGui().setOnlineHighlight(false);
				Gui.getBoardGui().setBthinking(false);
				Gui.setChoose(0);
				setSelect(0);

				try{
					Gui.getMenu().hostJob.stopSocket();
				}catch(Exception e){

				}
				Gui.getMenu().menuFile.getItems().addAll(Gui.getMenu().Load, Gui.getMenu().Save, Gui.getMenu().newGame);
				Gui.getMenu().menuGame.getItems().addAll(Gui.getMenu().GameMode0, Gui.getMenu().GameMode1, Gui.getMenu().GameMode2);
				Gui.getMenu().menuGame.getItems().removeAll(Gui.getMenu().disconnect);

			}

		});

		//Save
		Save.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				Gui.getBoardGui().soundPlayer.playSound("menu");
				FileChooser fileC = new FileChooser();
				fileC.setTitle("Open Game File");
				fileC.setInitialFileName("chess.sav");

				//Set extension filter
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("SAV files (*.sav)", "*.sav");
				fileC.getExtensionFilters().add(extFilter);

				File f = fileC.showSaveDialog(Gui.getStage());
				Save s = new Save();
				s.saveFile(Gui.getBGG2(), f); //Opens the SAVE class
			}
		});

		//Load
		Load.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				Gui.getBoardGui().soundPlayer.playSound("menu");
				FileChooser fileC = new FileChooser();
				fileC.setTitle("Open Game File");
				fileC.setInitialFileName("chess.sav");

				//Set extension filter
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("SAV files (*.sav)", "*.sav");
				fileC.getExtensionFilters().add(extFilter);

				File f = fileC.showOpenDialog(Gui.getStage());
				Load l = new Load();
				BackgroundGrid BGG2 = l.openFile(f); 
				if(BGG2 != null){
					Gui.LoadBG(BGG2); //Opens the LOAD class
				}
			}
		});

		//---------------------------------------------------------------------------------------------------------------------
		//Game Modes

		//Draw
		Draw.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if(!Gui.getBGG2().getSchachmattBlack() && !Gui.getBGG2().getSchachmattWhite() && !Gui.getBGG2().getDraw()){

					Alert alert = new Alert(AlertType.CONFIRMATION);
					Gui.getBoardGui().soundPlayer.playSound("menu");
					alert.setContentText("Do you really want to make a draw?");
					alert.setTitle("Wish to draw");
					alert.setHeaderText("Wish to draw.");

					Optional<ButtonType> result = alert.showAndWait();
					if(result.get() == ButtonType.OK){


						Gui.getBoardGui().soundPlayer.playSound("menu");
						if(_GM==0) {
							Gui.getBGG2().changeTeam();
							Gui.wishDraw();
						}else if(_GM==1) {
							Gui.wishDraw();
							//LAN
							//see GUI - wishDraw();
						}else if(_GM==2) {
							Gui.wishDraw();
						}
					} else {
						Gui.getBoardGui().soundPlayer.playSound("menu");
					}
				}
			}
		});


		//Local
		GameMode0.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Gui.getBoardGui().soundPlayer.playSound("menu");
				Gui.setChoose(0);
				setSelect(Gui.getChoose());
			}
		});

		//Lan

		GameMode1.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				//MOVE LATER
				Alert chooser = new Alert(AlertType.CONFIRMATION);
				Gui.getBoardGui().soundPlayer.playSound("menu");
				int Backup = Gui.getChoose();
				chooser.setTitle("Select Mode");
				chooser.setHeaderText("Choose, wether you would like to host or to join a game!");
				chooser.setContentText("Select your option:");

				ButtonType hostButton = new ButtonType("host");
				ButtonType joinButton = new ButtonType("join");
				ButtonType abortButton = new ButtonType("abort");


				chooser.getButtonTypes().setAll(hostButton, joinButton, abortButton);
				Optional <ButtonType> result = chooser.showAndWait();


				//------------------------------HOSTING------------------------------------------------------------------


				if(result.get() == hostButton){

					Gui.getBoardGui().soundPlayer.playSound("menu");
					Gui.setChoose(1);

					//Gui.getBGG2().setTeam(true);
					//Gui.getBoardGui().setHighlighting(true);

					/*try {
						Gui.setBGG1(Gui.getBGG2().getLan().initSeed);
						Gui.getBoardGui().DrawGrid(Gui.getBGG2().getLan().initSeed);
						Gui.getBoardGui().redraw();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/

					Gui.getBoardGui().setOnlineHighlight(true);

					hostJob = new hostingJob(Gui);
					Thread hostingThread = new Thread(hostJob);


					Gui.getBoardGui().setHighlighting(false);
					Gui.newBG();
					
					Gui.getBoardGui().drawBlurryMenu(hostJob);

					hostingThread.start();











					//------------------------------JOINING---------------------------------------------------------------
				}else if(result.get() == joinButton){

					Gui.setChoose(1);
					Gui.getBoardGui().soundPlayer.playSound("menu");
					InetAddress joinAdress = null;


					TextInputDialog ipDialogoue = new TextInputDialog("127.0.0.1");
					ipDialogoue.setTitle("Connecting");
					ipDialogoue.setHeaderText("Please enter the IP address of the host!");
					ipDialogoue.setContentText("The address should look like this: ");

					Optional<String> ipResult = ipDialogoue.showAndWait();
					if (ipResult.isPresent()){
						try {

							Gui.getBoardGui().soundPlayer.playSound("menu");
							System.out.println(ipResult.get());
							String SKY = ipResult.get();
							String SkyNet = "SkyNet";
							if(SKY.equals(SkyNet)) {
								System.out.println("Sky");
								_GM = 4;
								Gui.setChoose(4);
								ipDialogoue.close();
							} else {
								joinAdress = InetAddress.getByName(ipResult.get());
								System.out.println(joinAdress.toString());
								System.out.println(Gui.getBGG2().getLan().getIsConnectet());

								Gui.getBGG2().getLan().createSock(joinAdress);

								Gui.getBGG2().getLan().connecting(false);
								Gui.getBGG2().getLan().setIsConnectet(true);
								System.out.println("Clientisconnectedandrunning");
								Gui.getBGG2().setTeam(false);
								Gui.getBGG2().getLan().setFirstturn(true);
								Gui.getBoardGui().setBthinking(true);

								//	Gui.setBGG1( (int[][]) Gui.getBGG2().getLan().netReadStream.readObject());
								rj = new ReadingJob(Gui);
								Thread rt = new Thread(rj);
								rt.start();

								Gui.getBoardGui().heartBeatJob = new Heartbeat(joinAdress, false, Gui.getBoardGui().Heartbeat);
								Thread th = new Thread(Gui.getBoardGui().heartBeatJob);
								th.start();
								Heartbeat.heartThread=th;

								Gui.getBoardGui().L.setTeam(false);
								Gui.getBGG2().setTeam(false);
								Gui.getBoardGui().setOnlineHighlight(true);


								menuFile.getItems().removeAll(Load, Save, newGame);
								menuGame.getItems().removeAll(GameMode0, GameMode1, GameMode2);
								menuGame.getItems().addAll(disconnect);


								//Gui.getBoardGui().setLastMoveList((ArrayList<int[]>) Gui.getBGG2().getLan().netReadStream.readObject());
								Gui.getBoardGui().DrawGrid(Gui.getBGG1());
								Gui.getBoardGui().redraw();

							}


						} catch (UnknownHostException e) {
							Alert addressAlert = new Alert(AlertType.ERROR);
							addressAlert.setTitle("Error");
							addressAlert.setHeaderText("Connection Failed");
							addressAlert.setContentText("It seems that something is wrong with the IP address!");
							addressAlert.show();
							Gui.getBoardGui().setOnlineHighlight(false);
							Gui.setChoose(0);
							setSelect(Gui.getChoose());

						} catch (IOException e) {

							Alert connectionAlert = new Alert (AlertType.ERROR);
							connectionAlert.setTitle("Error");
							connectionAlert.setHeaderText("COnnection Failed");
							connectionAlert.setContentText("The desired host is not reachable.");
							connectionAlert.show();
							Gui.getBoardGui().setOnlineHighlight(false);
							Gui.setChoose(0);
							setSelect(Gui.getChoose());
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							Gui.getBoardGui().setOnlineHighlight(false);
							Gui.setChoose(0);
							setSelect(Gui.getChoose());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							Gui.getBoardGui().setOnlineHighlight(false);
							Gui.setChoose(0);
							setSelect(Gui.getChoose());
						}

					} else{
						System.out.println("yolo");
						Gui.getBoardGui().setOnlineHighlight(false);
						Gui.setChoose(Backup);
						setSelect(Gui.getChoose());
					}

					//TODO BUG somewhere around here!

					//----------------------------------------------------------------------------------------------------				


				}else if(result.get() == abortButton){

					Gui.getBoardGui().soundPlayer.playSound("menu");
					Gui.getBoardGui().setOnlineHighlight(false);
					Gui.setChoose(Backup);
					System.out.println(Gui.getChoose());
					setSelect(Gui.getChoose());

				}else{

					Gui.setChoose(0);
					Gui.getBoardGui().setOnlineHighlight(false);
					setSelect(Gui.getChoose());
				}

			}
		});
		//----------------------------------------------------------------------------------------------------	
		//AI
		GameMode2.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				Gui.setChoose(2);
				Gui.getBoardGui().soundPlayer.playSound("menu");

			}
		});

		setSelect(Gui.getChoose());


		GameMode4.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Gui.setChoose(4);
				Gui.getBoardGui().soundPlayer.playSound("menu");
			}
		});

		//Launchpad
		GameMode5.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if(GameMode5.isSelected()) {
					Gui.initLaunchad();
				}else {
					Launchpad.stop();
				}

				Gui.getBoardGui().soundPlayer.playSound("menu");
			}
		});



	}

	/**
	 * After a game mode has changed, this one here is called
	 * Sets all the GameModes accordingly
	 * @param i - int - GameMode 0 to 2 possible
	 */
	public void setSelect(int i){

		_GM=i;

		if(i == 0){
			GameMode0.setSelected(true);
			GameMode1.setSelected(false);
			GameMode2.setSelected(false);
		} else if(i == 1){
			GameMode1.setSelected(true);
			GameMode0.setSelected(false);
			GameMode2.setSelected(false);
		}else if (i == 2){
			GameMode2.setSelected(true);
			GameMode0.setSelected(false);
			GameMode1.setSelected(false);
		}
	}

	public void setLaunchpadMode(boolean status) {
		GameMode5.setSelected(status);
	}

	public boolean getLaunchpadMode() {
		return GameMode5.isSelected();
	}

	public int getGameMode(){

		return _GM;
	}

	public PopUp getPp() {
		return pp;
	}

	public void setPp(PopUp pp) {
		this.pp = pp;
	}


}


