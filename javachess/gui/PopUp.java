package javachess.gui;

import com.sun.glass.ui.Screen;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;



public class PopUp {

	private Label teamLabel;

	private ComboBox<String> LoadTurn;

	private Stage popupwindow=new Stage();

	private Slider AIslider;

	private ComboBox<String> AICombo;

	private CheckBox HardCoreAI;





	public GUI gui;

	public PopUp(GUI g){
		this.gui = g;

	}

	public void display(){



		Canvas c = new Canvas(600,400);
		GraphicsContext gc = c.getGraphicsContext2D(); 


		Color lightBrown= Color.web("#8C603C");

		gc.setFill(Color.BLACK);
		gc.strokeRect(44, 50, 255, 23);
		gc.strokeRect(44, 138, 272, 23);


		Label titleLabel= new Label("Options:");
		titleLabel.setLayoutX(10);
		Font TitleFont = new Font(30);
		titleLabel.setFont(TitleFont);
		Font subtitleFont = new Font(22);

		//subtitle Labels

		Label Audio = new Label("Audio Options:");
		Audio.setFont(subtitleFont);
		Audio.setLayoutX(30);
		Audio.setLayoutY(5);

		Label AI = new Label("AI Options: ");
		AI.setFont(subtitleFont);
		AI.setLayoutX(30);
		AI.setLayoutY(90);

		Label INFO = new Label("Other Information and Options: ");
		INFO.setFont(subtitleFont);
		INFO.setLayoutX(30);
		INFO.setLayoutY(175);



		//labels


		Label vollabel= new Label("Volume: " + (Math.round(gui.getBoardGui().soundPlayer.getVolume()*100)) + "%");
		vollabel.setLayoutX(50);
		vollabel.setLayoutY(51);

		Label Turncount  = new Label("TurnCount: " + gui.getBGG2().getTurnRound());
		Turncount.setLayoutX(50);
		Turncount.setLayoutY(221);

		Label LoadTurnLabel = new Label("Load turn: ");
		LoadTurnLabel.setLayoutX(50);
		LoadTurnLabel.setLayoutY(267);

		Label netPrefix = new Label("Connection:");
		netPrefix.setLayoutX(190);
		netPrefix.setLayoutY(221);



		Label netLabel2 = new Label("");
		if(gui.getBGG2().getLan().getIsConnectet() == true){
			netLabel2 = new Label("");
			netLabel2.setText("Online");
			System.out.println("Online");
			netLabel2.setTextFill(Color.GREEN);
			System.out.println("Greeeeeenee");
		} else{
			netLabel2 = new Label("");
			netLabel2.setText("Offline");
			netLabel2.setTextFill(Color.RED);
		}

		netLabel2.setLayoutX(270);
		netLabel2.setLayoutY(221);
		Label netLabel = netLabel2;
		Label teamPrefix = new Label("Current Team: ");
		teamPrefix.setLayoutX(175);
		teamPrefix.setLayoutY(221);

		teamLabel = new Label();

		if(gui.getBGG2().getTeam()==true){
			teamLabel.setText("White");
		}else{
			teamLabel.setText("Black");
		}

		teamLabel.setLayoutX(270);
		teamLabel.setLayoutY(221);






		Label AILabel = new Label("Difficulty Level: " + gui.getBGG2().getAiDepth());
		AILabel.setLayoutX(50);
		AILabel.setLayoutY(138);

		//Label Listener

		gui.getBoardGui().conProp.addListener(new ChangeListener<Number>(){

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

				if(gui.getBGG2().getLan().getIsConnectet() == true){
					netLabel.setText("Online");
					netLabel.setTextFill(Color.GREEN);

				} else{

					netLabel.setTextFill(Color.RED);

					changelabelText("offline", netLabel);
				}

			}



		});

		gui.getBoardGui().turnProp.addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				Turncount.setText("TurnCount: " + gui.getBGG2().getTurnRound());
				if(gui.getBGG2().getTeam()==true){
					teamLabel.setText("White");
				}else{
					teamLabel.setText("Black");
				}


			}
		});


		//Elements


		//VolSlider
		Slider VolSlider= new Slider(0.1,1.0,gui.getBoardGui().soundPlayer.getVolume());

		VolSlider.setLayoutX(155);
		VolSlider.setLayoutY(53);

		VolSlider.valueProperty().addListener(new ChangeListener<Number>(){

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

				gui.getBoardGui().soundPlayer.setVolume(VolSlider.getValue());
				vollabel.setText("Volume: " + (Math.round(gui.getBoardGui().soundPlayer.getVolume()*100)) + "%");

				if(gui.getBoardGui().soundPlayer.getVolume() > 0.9){
					vollabel.setTextFill(Color.RED);
				} else{
					vollabel.setTextFill(Color.BLACK);
				}

			}


		});

		//Checkbox Volume Mute

		CheckBox VolumeMute = new CheckBox("Mute");
		VolumeMute.setSelected(gui.getBoardGui().soundPlayer.getIsMuted());

		VolumeMute.setLayoutX(330);
		VolumeMute.setLayoutY(51);

		VolumeMute.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {



				if(gui.getBoardGui().soundPlayer.getIsMuted()==false){


					gui.getBoardGui().soundPlayer.setIsMuted(true);
					vollabel.setDisable(true);
					VolSlider.setDisable(true);


				} else{

					gui.getBoardGui().soundPlayer.setIsMuted(false);
					vollabel.setDisable(false);
					VolSlider.setDisable(false);

				}
			}
		});

		//Checkbox Hardcore AI

		HardCoreAI = new CheckBox("Better AI");
		HardCoreAI.setSelected(gui.getBGG2().getHardCoreAI());

		HardCoreAI.setLayoutX(330);
		HardCoreAI.setLayoutY(141);

		HardCoreAI.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
				if(HardCoreAI.isSelected()){
					gui.getBGG2().setHardCoreAI(true);
				}else{
					gui.getBGG2().setHardCoreAI(false);
				}

			}
		});

		//Sqare Board
		CheckBox RectBoard = new CheckBox("Square Board");
		RectBoard.setLayoutX(355);
		RectBoard.setLayoutY(221);
		RectBoard.setSelected(gui.getBoardGui().isRectMode());
		RectBoard.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

				if(gui.getBoardGui().getBlurryButtonOn() == true){

				}else{



					if(gui.getBoardGui().isRectMode() == false){
						gui.getBoardGui().setRectMode(true);
						gui.getStage().setResizable(false);

						gui.S.setY(50);
						gui.S.setWidth(Screen.getMainScreen().getVisibleHeight() - 200);
						gui.S.setHeight(Screen.getMainScreen().getVisibleHeight() - 200);
					}else{
						gui.getBoardGui().setRectMode(false);
						gui.getStage().setResizable(true);
					}

				}
			}
		});


		Button VolTest= new Button("Test sound");
		VolTest.setLayoutX(440);
		VolTest.setLayoutY(45);

		VolTest.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent Event) {


				gui.getBoardGui().soundPlayer.playSound("startup");

			}


		});




		//------------------------------
		//AI

		//AI-Control
		AIslider = new Slider();
		AIslider.setMin(1);
		AIslider.setMax(5);
		AIslider.setValue(gui.getBGG2().getAiDepth());
		AIslider.setMajorTickUnit(1);
		AIslider.setMinorTickCount(0);
		AIslider.snapToTicksProperty().set(true);

		AIslider.setLayoutX(170);
		AIslider.setLayoutY(141);

		AIslider.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				AILabel.setText("Difficulty Level: " + (int) AIslider.getValue());	
				gui.getBGG2().setAiDepth((int) AIslider.getValue()+1);
			}
		});


		//------------------------------
		//AI-Combo Box

		AICombo = new ComboBox<String>();
		AICombo.setEditable(false);
		AICombo.getItems().add("Black-AI");
		AICombo.getItems().add("White-AI");

		if(gui.getBGG2().getAITeam()) {
			AICombo.getSelectionModel().select(1);
		}else {
			AICombo.getSelectionModel().select(0);
		}

		AICombo.setLayoutX(440);
		AICombo.setLayoutY(134);

		AICombo.valueProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(newValue.equals("Black-AI")) {
					gui.getBGG2().setAITeam(false);
				}else if(newValue.equals("White-AI")) {
					gui.getBGG2().setAITeam(true);
				}else {
					System.out.println("Error fucking error in PopUp.java");
				}

			}
		});

		//Undo and Redo

		Button BUndo = new Button("Undo");
		BUndo.setLayoutX(157);
		BUndo.setLayoutY(267);
		

		BUndo.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent Event) {

				if(gui.getBGG2().getChoose()!=1){
					BUndo.setDisable(false);
					try{
						int LoadTurn = gui.getBGG2().getTurnRound()-1;
						System.out.println("Loads Turn Round:"+LoadTurn);
						//Get team and Board
						
						int[][] iBoard = new int[8][8];
						for(int iHY = 0; iHY < 8; iHY++){
							for(int iHX = 0; iHX < 8; iHX++){
								iBoard[iHX][iHY] = gui.getBGG2().getBoardList().get(LoadTurn)[iHX][iHY];
							}

						}
						gui.getBGG2().iBackground = iBoard;
						gui.getBGG2().Board = iBoard;
						gui.getBGG2().setTeam(gui.getBGG2().getTeamList().get(LoadTurn)[0]);
						//System.out.println("team...:"+gui.getBGG2().getTeam());

						//Draw team and Board
						gui.getBoardGui().redraw();
						gui.getBoardGui().DrawGrid(gui.getBGG2().iBackground);

						gui.getBGG2().setTurnRound((short) LoadTurn);
						gui.getBoardGui().turnProp.set(LoadTurn);

					}catch(Exception ex){
						System.out.println("Exceptioned..."+ex.getMessage());
					}

					gui.getBoardGui().soundPlayer.playSound("menu");
				}else{
					BUndo.setDisable(true);
				}
			}


		});
		
		
		Button BRedo = new Button("Redo");
		BRedo.setLayoutX(230);
		BRedo.setLayoutY(267);
		

		BRedo.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent Event) {

				if(gui.getBGG2().getChoose()!=1){
					BRedo.setDisable(false);
					try{
						if(gui.getBGG2().getBoardList().size()-1 > gui.getBGG2().getTurnRound()){
							int LoadTurn = gui.getBGG2().getTurnRound()+1;
							System.out.println("Loads Turn Round:"+LoadTurn);
							//Get team and Board
							//Security measure (QA told me so)
							int[][] iBoard = new int[8][8];
							for(int iHY = 0; iHY < 8; iHY++){
								for(int iHX = 0; iHX < 8; iHX++){
									iBoard[iHX][iHY] = gui.getBGG2().getBoardList().get(LoadTurn)[iHX][iHY];
								}

							}
							gui.getBGG2().iBackground = iBoard;
							gui.getBGG2().Board = iBoard;
							gui.getBGG2().setTeam(gui.getBGG2().getTeamList().get(LoadTurn)[0]);
							
							//Draw team and Board
							gui.getBoardGui().redraw();
							gui.getBoardGui().DrawGrid(gui.getBGG2().iBackground);
							gui.getBGG2().setTurnRound((short) LoadTurn);
							gui.getBoardGui().turnProp.set(LoadTurn);
						}

					}catch(Exception ex){
						System.out.println("Exceptioned..."+ex.getMessage());
					}

					gui.getBoardGui().soundPlayer.playSound("menu");
				}else{
					BRedo.setDisable(true);
				}
			}


		});

		//Label-TurnRound-ComboBox
		/*
		LoadTurn = new ComboBox<String>();
		LoadTurn.setEditable(false);
		LoadTurn.getItems().add("Turn:"+gui.getBGG2().getTurnRound());


		LoadTurn.getSelectionModel().select(0);
		LoadTurn.setLayoutX(175);
		LoadTurn.setLayoutY(267);

		LoadTurn.valueProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				try{
					String[] Splits = newValue.split(":");
					int LoadTurn = (Integer.parseInt(Splits[1]));

					//Get team and Board
					gui.getBGG2().iBackground = gui.getBGG2().getBoardList().get(LoadTurn);
					gui.getBGG2().Board = gui.getBGG2().getBoardList().get(LoadTurn);
					gui.getBGG2().setTeam(gui.getBGG2().getTeamList().get(LoadTurn)[0]);
					System.out.println("team...:"+gui.getBGG2().getTeam());

					//Draw team and Board
					gui.getBoardGui().redraw();
					gui.getBoardGui().DrawGrid(gui.getBGG2().iBackground);

					//New Board state

					int Size = gui.getBGG2().getBoardList().size();

					for(int i = Size-1; i > LoadTurn; i--){
						gui.getBGG2().getBoardList().remove(i);
						gui.getBGG2().getTeamList().remove(i);
						PopUp.this.LoadTurn.getItems().remove(i);
					}
					gui.getBGG2().setTurnRound((short) LoadTurn);

				}catch(Exception ex){
					System.out.println("Exceptioned..."+ex.getMessage());
				}
			}
		});
		 */

		Group gp = new Group();
		gp.getChildren().addAll(c, vollabel,VolSlider, Audio, VolumeMute, VolTest, AI, INFO, Turncount, teamPrefix, teamLabel, AILabel, AIslider, AICombo, HardCoreAI, RectBoard, LoadTurnLabel, BUndo,BRedo);
		Scene scene1= new Scene(gp, 600, 300, Color.WHITE);


		popupwindow.initModality(Modality.NONE);
		popupwindow.setTitle("Options and Information");


		popupwindow.setScene(scene1);
		popupwindow.setResizable(false);
		popupwindow.getIcons().add(new Image("javachess/images/JavaChess.png"));
		//popupwindow.showAndWait();

	}





	public void showPopUpWindow(){
		popupwindow.showAndWait();
	}

	//---------------------------------------------------------------------------------------
	//Getter and setters
	private void changelabelText(String x, Label l){
		l.setText(x);
	}

	public Label getTeamLabel() {
		return teamLabel;
	}

	public void setTeamLabel(Label teamLabel) {
		this.teamLabel = teamLabel;
	}

	public ComboBox<String> getLoadTurn() {
		return LoadTurn;
	}

	public void setLoadTurn(ComboBox<String> loadTurn) {
		LoadTurn = loadTurn;
	}

	public Slider getAIslider() {
		return AIslider;
	}

	public void setAIslider(Slider aIslider) {
		AIslider = aIslider;
	}

	public ComboBox<String> getAICombo() {
		return AICombo;
	}

	public void setAICombo(ComboBox<String> aICombo) {
		AICombo = aICombo;
	}

	public CheckBox getHardCoreAI() {
		return HardCoreAI;
	}

	public void setHardCoreAI(CheckBox hardCoreAI) {
		HardCoreAI = hardCoreAI;
	}


}
