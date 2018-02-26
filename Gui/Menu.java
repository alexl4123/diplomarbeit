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
import network.Heartbeat;
import network.ReadingJob;
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
	public RadioMenuItem GameMode0, GameMode1, GameMode2, GameMode3;
	public MenuItem newGame, Save, Load, Exit, refresh;
	int _GM;
	public ReadingJob rj;
	public hostingJob hostJob;
	
	public javafx.scene.control.Menu menuFile;
	public javafx.scene.control.Menu menuGame;
	public javafx.scene.control.Menu menuSound;
	
	/**
	 * the constructor builds the GUI
	 * @param Gui - for adding to Application purposes
	 */
	public Menu(GUI Gui){
		
		//Menus with Submenus 
		 menuFile = new javafx.scene.control.Menu("File");
		 menuGame = new javafx.scene.control.Menu("Game");
		 menuSound = new javafx.scene.control.Menu("Sound");
		
		//Adding Menu Items
		 newGame = new MenuItem("New");
		 Save = new MenuItem("Save");
		 Load = new MenuItem("Load");
		 Exit = new MenuItem("Exit");
		 refresh = new MenuItem("DEBUG/Refresh");
		
		//RadioButton Items
		GameMode0 = new RadioMenuItem("Game Mode Local");
		GameMode1 = new RadioMenuItem("Game Mode LAN");
		GameMode2 = new RadioMenuItem("Game Mode AI");
		GameMode3 = new RadioMenuItem("Launchpad");
		
		//For Game Menu Items
		MenuItem Draw = new MenuItem("Draw");
		
		
		//RadioButton - Group
		ToggleGroup group = new ToggleGroup();
		GameMode0.setToggleGroup(group);
		GameMode1.setToggleGroup(group);
		GameMode2.setToggleGroup(group);
		GameMode0.setSelected(true);
		
		
		
		menuFile.getItems().addAll(newGame, Save, Load, Exit, refresh);						//delete refresh when publish
		menuGame.getItems().addAll(GameMode0, GameMode1, GameMode2, GameMode3, Draw);
		
		
		this.getMenus().addAll(menuFile, menuGame, menuSound);
		
		
		
		//-------------------------------------------------------------------------------------------------
		//File Menu
		
		//new Game
		newGame.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				Gui.newBG();
				Gui.getBoardGui().setHighlighting(true);
			}
		});
		
		//Exit
		Exit.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				System.exit(1);
				
			}
		});
		
		refresh.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				Gui.getBoardGui().redraw();
				
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
					//Gui.newBG();
					//Gui.getBoardGui().setHighlighting(true);
				
					
					hostJob = new hostingJob(Gui);
					Thread hostingThread = new Thread(hostJob);
					
					
					Gui.getBoardGui().setHighlighting(false);
					Gui.getBoardGui().drawBlurryMenu(hostJob);
					
					hostingThread.start();
					
					
					
					
					
					
					
					
					
					
					
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
							
							Heartbeat hb = new Heartbeat(joinAdress, false);
							Thread th = new Thread(hb);
							th.start();
							
							Gui.getBoardGui().L.setTeam(false);
							Gui.getBGG2().setTeam(false);
							
							
							menuFile.getItems().removeAll(Load, Save, newGame, refresh);
							menuGame.getItems().removeAll(GameMode0, GameMode1, GameMode2, GameMode3);
							
							
							//Gui.getBoardGui().setLastMoveList((ArrayList<int[]>) Gui.getBGG2().getLan().netReadStream.readObject());
							Gui.getBoardGui().DrawGrid(Gui.getBGG1());
							Gui.getBoardGui().redraw();
							
							
							
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
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
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
		//----------------------------------------------------------------------------------------------------	
		//AI
		GameMode2.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Gui.setChoose(2);
			}
		});
		
		setSelect(Gui.getChoose());
		
		//----------------------------------------------------------------------------------------------------	
		//Launchpad
		GameMode3.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Gui.initLaunchad();
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
	

}


