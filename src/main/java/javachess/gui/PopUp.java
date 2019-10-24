package javachess.gui;
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
import javafx.stage.Screen;
import javafx.stage.Stage;


/**
 * 
 * @author mhub - 2018
 * @version 2.0
 *
 *	This class is used to create a new Window. 
 *  This window is uesd to display information about the game and provide additional game options.
 *
 */
public class PopUp {

	/**
	 * Label for displaying the team
	 */
	private Label teamLabel;

	/**
	 * Combobox for loading a turn -- currently not in use
	 */
	private ComboBox<String> LoadTurn;

	/**
	 * new Stage to display the windows
	 */
	private Stage popupwindow=new Stage();

	/**
	 * Slider for choosing the difficulty of the AI
	 */
	private Slider AIslider;

	/**
	 * Combovoc for choosing the AIs team
	 */
	private ComboBox<String> AICombo;

	/**
	 * Chechbox for the 'berrer AI mode'
	 */
	private CheckBox HardCoreAI;

	/**
	 * theh current gui
	 */
	public GUI gui;

	/**
	 * The Constructor.
	 * @param g - the current Gui
	 */
	public PopUp(GUI g){
		this.gui = g;

	}

	/**
	 * Method do create and position all the stuff displayed. Also the Actionlisteners are createt here. 
	 */
	public void display(){


		Canvas c = new Canvas(600,400);
		GraphicsContext gc = c.getGraphicsContext2D(); 


		Color lightBrown= Color.web("#8C603C");

		gc.setFill(Color.BLACK);
		gc.strokeRect(44, 50, 255, 23);							//some stiling
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
		AI.setLayoutX(30);												//big title labels
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
		Turncount.setLayoutX(50);																	//other labels
		Turncount.setLayoutY(221);

		Label netPrefix = new Label("Connection:");
		netPrefix.setLayoutX(190);
		netPrefix.setLayoutY(221);


		//---------------------------------------------NETLABEL CURRENTLY NOT IN USE -------------------------------------
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

		//-------------------------------------------------------------------------------------------------------------------

		Label teamPrefix = new Label("Current Team: ");
		teamPrefix.setLayoutX(175);
		teamPrefix.setLayoutY(221);

		teamLabel = new Label();

		if(gui.getBGG2().getTeam()==true){			//setting the correct text of the label
			teamLabel.setText("White");
		}else{
			teamLabel.setText("Black");
		}

		teamLabel.setLayoutX(270);
		teamLabel.setLayoutY(221);






		Label AILabel = new Label("Difficulty Level: " + gui.getBGG2().getAiDepth());
		AILabel.setLayoutX(50);
		AILabel.setLayoutY(138);

		//Label Listener - listeners to check wether a integerproperty changed in the boardgui.
		//------------------------------------CURRENTLY NOT IN USE ----------------------------------------------------------

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

		//------------------------------------------------------------------------------------------------------------------------

		gui.getBoardGui().turnProp.addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				Turncount.setText("TurnCount: " + gui.getBGG2().getTurnRound());
				if(gui.getBGG2().getTeam()==true){
					teamLabel.setText("White");						//everytime a turn happens, turnprop changes and this executes
				}else{
					teamLabel.setText("Black");
				}


			}
		});


		//Elements for controlling options following

		//VolSlider
		Slider VolSlider= new Slider(0.1,1.0,gui.getBoardGui().soundPlayer.getVolume());
		VolSlider.setLayoutX(155);
		VolSlider.setLayoutY(53);

		//when the slider changes --> this executes
		VolSlider.valueProperty().addListener(new ChangeListener<Number>(){

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

				gui.getBoardGui().soundPlayer.setVolume(VolSlider.getValue());
				vollabel.setText("Volume: " + (Math.round(gui.getBoardGui().soundPlayer.getVolume()*100)) + "%");	//displaying the volume

				if(gui.getBoardGui().soundPlayer.getVolume() > 0.9){
					vollabel.setTextFill(Color.RED);						//for our poor headphone users
				} else{
					vollabel.setTextFill(Color.BLACK);
				}

			}


		});

		
		//------------------------------
		//Checkbox Volume Mute

		CheckBox VolumeMute = new CheckBox("Mute");
		VolumeMute.setSelected(gui.getBoardGui().soundPlayer.getIsMuted());

		VolumeMute.setLayoutX(330);
		VolumeMute.setLayoutY(51);

		VolumeMute.selectedProperty().addListener(new ChangeListener<Boolean>() {			//when status changes

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
		
		//------------------------------

		//Checkbox Hardcore AI

		HardCoreAI = new CheckBox("Better AI");
		HardCoreAI.setSelected(gui.getBGG2().getHardCoreAI());

		HardCoreAI.setLayoutX(330);
		HardCoreAI.setLayoutY(141);

		HardCoreAI.selectedProperty().addListener(new ChangeListener<Boolean>() {  //when checkbox gets ticked

			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
				if(HardCoreAI.isSelected()){
					gui.getBGG2().setHardCoreAI(true);
				}else{
					gui.getBGG2().setHardCoreAI(false);
				}

			}
		});
		
		//------------------------------

		//Sqare Board for forcing the borad to a square - wish of beta testers
		CheckBox RectBoard = new CheckBox("Square Board");
		RectBoard.setLayoutX(355);
		RectBoard.setLayoutY(221);
		RectBoard.setSelected(gui.getBoardGui().isRectMode());
		RectBoard.selectedProperty().addListener(new ChangeListener<Boolean>() {			

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

				if(gui.getBoardGui().getBlurryButtonOn() == true){
						//better not do while a bulrry menu is shown 
				}else{



					if(gui.getBoardGui().isRectMode() == false){
						gui.getBoardGui().setRectMode(true);
						gui.getStage().setResizable(false);
						gui.S.setY(50);														//forcing the scene
						gui.S.setWidth(Screen.getPrimary().getVisualBounds().getHeight() - 200);
						gui.S.setHeight(Screen.getPrimary().getVisualBounds().getHeight() - 200);
					}else{
						gui.getBoardGui().setRectMode(false);
						gui.getStage().setResizable(true);
					}

				}
			}
		});
		//------------------------------

		//button for soundtesting
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

		//AI-Control
		AIslider = new Slider();
		AIslider.setMin(1);
		AIslider.setMax(5);
		AIslider.setValue(gui.getBGG2().getAiDepth());
		AIslider.setMajorTickUnit(1);								//its a slider hooking up to defined values - cool stuff, isn´t?
		AIslider.setMinorTickCount(0);
		AIslider.snapToTicksProperty().set(true);

		AIslider.setLayoutX(170);
		AIslider.setLayoutY(141);

		AIslider.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				AILabel.setText("Difficulty Level: " + (int) AIslider.getValue());					//setting the difficulty
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

// Combobox for loading a specific round -- used for debbung - not in use anymore

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

		Group gp = new Group();					//getting all together
		gp.getChildren().addAll(c, vollabel,VolSlider, Audio, VolumeMute, VolTest, AI, INFO, Turncount, teamPrefix, teamLabel, AILabel, AIslider, AICombo, HardCoreAI, RectBoard);
		Scene scene1= new Scene(gp, 600, 300, Color.WHITE);


		popupwindow.initModality(Modality.NONE);			//making it parallely runable 
		popupwindow.setTitle("Options and Information");


		popupwindow.setScene(scene1);
		popupwindow.setResizable(false);
		popupwindow.getIcons().add(new Image("javachess/images/JavaChess.png"));
	}




	/**
	 * Method to show the popup. 
	 */
	public void showPopUpWindow(){
		popupwindow.showAndWait();
	}

	//---------------------------------------------------------------------------------------
	//Getter and setters


	
	/**
	 * changeing the text of a label 
	 * @param x - text the label should get
	 * @param l - the label which should get a new text
	 */
	private void changelabelText(String x, Label l){
		l.setText(x);
	}

	/**
	 * 
	 * @return the label displaying the team
	 */
	public Label getTeamLabel() {
		return teamLabel;
	}

	/**
	 * 
	 * @return the load turn combobox
	 */
	public ComboBox<String> getLoadTurn() {
		return LoadTurn;
	}

	/**
	 * 
	 * @return the slider with the AI - difficulty
	 */
	public Slider getAIslider() {
		return AIslider;
	}


	/**
	 * 
	 * @return the combobox for the ai team
	 */
	public ComboBox<String> getAICombo() {
		return AICombo;
	}


	/**
	 * Gets the combobox for heartcore AI
	 * @return
	 */
	public CheckBox getHardCoreAI() {
		return HardCoreAI;
	}


}
