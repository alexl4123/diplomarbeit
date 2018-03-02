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
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
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
	Group root = new Group();
	
	gc.setFill(Color.BLACK);
	gc.strokeRect(44, 50, 255, 23);
	


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
	
	
	
	
	


	
	
	Group gp = new Group();
	gp.getChildren().addAll(c, vollabel,VolSlider, Audio, VolumeMute, VolTest, AI, INFO, Turncount);
	Scene scene1= new Scene(gp, 600, 300, Color.WHITE);
	
	
	popupwindow.initModality(Modality.NONE);
	popupwindow.setTitle("Options and Information");
	      
	popupwindow.setScene(scene1);
	popupwindow.setResizable(false);
	popupwindow.showAndWait();
	       
	}
	
	
}
