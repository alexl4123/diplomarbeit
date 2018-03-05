package Gui;

import java.awt.Checkbox;

import javax.swing.GroupLayout.Alignment;
import javax.xml.crypto.KeySelector.Purpose;

import com.sun.javafx.geom.Rectangle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PopUp {


	public GUI gui;

	public PopUp(GUI g){
		this.gui = g;

	}

	public void display(){

		Stage popupwindow=new Stage();

		Canvas c = new Canvas(600,300);
		GraphicsContext gc = c.getGraphicsContext2D(); 


		Color lightBrown= Color.web("#8C603C");

		gc.setFill(Color.BLACK);
		gc.strokeRect(44, 50, 255, 23);
		gc.strokeRect(44, 138, 372, 23);


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

		Label INFO = new Label("Other Information: ");
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
		Label teamPrefix = new Label("Current Team:");
		teamPrefix.setLayoutX(355);
		teamPrefix.setLayoutY(221);

		Label teamLabel = new Label();

		if(gui.getBGG2().getTeam()==true){
			teamLabel.setText("White");
		}else{
			teamLabel.setText("Black");
		}

		teamLabel.setLayoutX(450);
		teamLabel.setLayoutY(221);






		Label AILabel = new Label("AI-Zuege voraus berechnen: " + gui.getBGG2().getAiDepth());
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


				} else{

					gui.getBoardGui().soundPlayer.setIsMuted(false);

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
		Slider AIslider = new Slider();
		AIslider.setMin(1);
		AIslider.setMax(10);
		AIslider.setValue(gui.getBGG2().getAiDepth());
		AIslider.setMajorTickUnit(1);
		AIslider.setMinorTickCount(0);
		AIslider.snapToTicksProperty().set(true);

		AIslider.setLayoutX(270);
		AIslider.setLayoutY(141);

		AIslider.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				AILabel.setText("AI-Zuege voraus berechnen: " + (int) AIslider.getValue());	
				gui.getBGG2().setAiDepth((int) AIslider.getValue());
			}
		});


		//------------------------------
		//AI-Combo Box
		
		ComboBox<String> AICombo = new ComboBox<String>();
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
		
		Group gp = new Group();
		gp.getChildren().addAll(c, vollabel,VolSlider, Audio, VolumeMute, VolTest, AI, INFO, Turncount, netPrefix, netLabel, teamPrefix, teamLabel, AILabel, AIslider, AICombo);
		Scene scene1= new Scene(gp, 600, 300, Color.WHITE);


		popupwindow.initModality(Modality.NONE);
		popupwindow.setTitle("Options and Information");

		popupwindow.setScene(scene1);
		popupwindow.setResizable(false);
		popupwindow.showAndWait();

	}

	private void changelabelText(String x, Label l){
		l.setText(x);
	}


}
