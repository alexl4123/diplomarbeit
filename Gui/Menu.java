package Gui;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Optional;

import BackgroundMatrix.BackgroundGrid;
import Game.AILogic;
import Game.MovePos;
import SaveLoad.load;
import SaveLoad.save;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import network.hostingJob;

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
	RadioMenuItem GameMode0, GameMode1, GameMode2;
	
	/**
	 * the constructor builds the GUI
	 * @param Gui - for adding to Application purposes
	 */
	public Menu(GUI Gui){
		
		//Menus with Submenus 
		javafx.scene.control.Menu menuFile = new javafx.scene.control.Menu("File");
		javafx.scene.control.Menu menuGame = new javafx.scene.control.Menu("Game");
		javafx.scene.control.Menu menuSound = new javafx.scene.control.Menu("Sound");
		
		//Adding Menu Items
		MenuItem newGame = new MenuItem("New");
		MenuItem Save = new MenuItem("Save");
		MenuItem Load = new MenuItem("Load");
		MenuItem Exit = new MenuItem("Exit");
		
		//RadioButton Items
		GameMode0 = new RadioMenuItem("Game Mode Local");
		GameMode1 = new RadioMenuItem("Game Mode LAN");
		GameMode2 = new RadioMenuItem("Game Mode AI");
		
		//For Game Menu Items
		MenuItem Draw = new MenuItem("Draw");
		
		
		//RadioButton - Group
		ToggleGroup group = new ToggleGroup();
		GameMode0.setToggleGroup(group);
		GameMode1.setToggleGroup(group);
		GameMode2.setToggleGroup(group);
		GameMode0.setSelected(true);
		
		
		menuFile.getItems().addAll(newGame, Save, Load, Exit);
		menuGame.getItems().addAll(GameMode0, GameMode1, GameMode2, Draw);
		
		
		this.getMenus().addAll(menuFile, menuGame, menuSound);
		
		
		
		//-------------------------------------------------------------------------------------------------
		//File Menu
		
		//new Game
		newGame.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				Gui.newBG();
			}
		});
		
		//Exit
		Exit.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				System.exit(1);
				
			}
		});
		
		//Save
		Save.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
				FileChooser fileC = new FileChooser();
				fileC.setTitle("Open Game File");
				fileC.setInitialFileName("chess.sav");
				
				 //Set extension filter
	              FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("SAV files (*.sav)", "*.sav");
	              fileC.getExtensionFilters().add(extFilter);
				
				File f = fileC.showSaveDialog(Gui.getStage());
				save s = new save();
				s.saveFile(Gui.getBGG2(), f); //Opens the SAVE class
			}
		});
		
		//Load
		Load.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				FileChooser fileC = new FileChooser();
				fileC.setTitle("Open Game File");
				fileC.setInitialFileName("chess.sav");
				
				 //Set extension filter
	              FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("SAV files (*.sav)", "*.sav");
	              fileC.getExtensionFilters().add(extFilter);
				
				File f = fileC.showOpenDialog(Gui.getStage());
				load l = new load();
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
				if(!Gui.getBGG2().getSchachmattBlack() && !Gui.getBGG2().getSchachMattWhite() && !Gui.getBGG2().getDraw()){
				
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setContentText("Do you really want to make a draw?");
				alert.setTitle("Wish to draw");
				alert.setHeaderText("Wish to draw.");
				
				Optional<ButtonType> result = alert.showAndWait();
				if(result.get() == ButtonType.OK){
					Gui.getBGG2().changeTeam();
					Gui.wishDraw();
				} else {
					
				}
				}
			}
		});
		
		
		//Local
		GameMode0.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
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
					
					Gui.setChoose(1);
					
					Alert hostingAlert = new Alert(AlertType.INFORMATION);
					hostingAlert.setTitle("Hosting");
					hostingAlert.setHeaderText("You are trying to host a network game!");
					hostingAlert.setContentText("Please wait until another player joins you...");
					
					hostingJob hostJob = new hostingJob(Gui.getBGG2());
					Thread hostingThread = new Thread(hostJob);
					Gui.getBoardGui().drawBlurryMenu();
					//hostingThread.start();
					
					//lösung für Später: Platform.runLater --> KEIN EIGENER THRAD --> vielleicht mit listener arbeiten
					
					/*abortButton = new ButtonType("abort");
					
					hostingAlert.getButtonTypes().setAll(abortButton);
					AlertThreadJob ajob = new AlertThreadJob(AlertType.ERROR, "T", "e", "f", "c");
					
					
					/*
					try {
						Thread.currentThread().wait(1);
					} catch (InterruptedException e) {
						
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
					finally {
						System.out.println("finally");
					}
					
					
					Thread th = new Thread() {

			            public synchronized void run() {
			            	int i = 0;
			            	while(Gui.getBGG2().getIsConnectet() == false){
								//G
								System.out.println("Dies ist eine While");
								
								if(ajob.getButtonPressed() == true){
									Gui.setChoose(0);
									hostJob.stopSocket();
									
									System.out.println(hostingThread.isAlive());
									break;
								}
								
								//i++;
								
								
								
							}
			            	
			            	
			            }

			        };
					th.start();
					*/
					
					
					System.out.println(Gui.getBGG2().getIsConnectet());
					
					System.out.println("Ausgebrochen aus Hostingschleiffe");
					
					
					
//------------------------------JOINING---------------------------------------------------------------
				}else if(result.get() == joinButton){
					
					Gui.setChoose(1);
					
					InetAddress joinAdress = null;
					
					TextInputDialog ipDialogoue = new TextInputDialog("127.0.0.1");
					ipDialogoue.setTitle("Connecting");
					ipDialogoue.setHeaderText("Please enter the IP address of the host!");
					ipDialogoue.setContentText("The address should look like this: ");
					
					Optional<String> ipResult = ipDialogoue.showAndWait();
					if (ipResult.isPresent()){
						try {
							
							joinAdress = InetAddress.getByName(ipResult.get());
							System.out.println(joinAdress.toString());
							Socket sockForClient = new Socket(joinAdress, 22359);
							Gui.getBGG2().setSocketOfClient(sockForClient);
							
						} catch (UnknownHostException e) {
							Alert addressAlert = new Alert(AlertType.ERROR);
							addressAlert.setTitle("Error");
							addressAlert.setHeaderText("Connection Failed");
							addressAlert.setContentText("It seems that something is wrong with the IP address!");
							addressAlert.show();
							Gui.setChoose(0);
							setSelect(Gui.getChoose());
							
						} catch (IOException e) {
							
							Alert connectionAlert = new Alert (AlertType.ERROR);
							connectionAlert.setTitle("Error");
							connectionAlert.setHeaderText("COnnection Failed");
							connectionAlert.setContentText("The desired host is not reachable.");
							connectionAlert.show();
							Gui.setChoose(0);
							setSelect(Gui.getChoose());
						}
					}
					
//----------------------------------------------------------------------------------------------------				
					
					
				}else if(result.get() == abortButton){
					
					Gui.setChoose(0);
					System.out.println(Gui.getChoose());
					setSelect(Gui.getChoose());
					
				}else{
					
					
				}
				
			}
		});
		
		//AI
		GameMode2.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Gui.setChoose(2);
			}
		});
		
		setSelect(Gui.getChoose());
		
		
		
		
	}
	
	/**
	 * After a game mode has changed, this one here is called
	 * Sets all the GameModes accordingly
	 * @param i - int - GameMode 0 to 2 possible
	 */
	public void setSelect(int i){
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
}
