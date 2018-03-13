package javachess.gui;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.sun.deploy.uitoolkit.impl.fx.HostServicesFactory;

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
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;


/**
 * This class gives information about the developers, the licence and a link to the GitHub page.
 * @author alexl12
 *
 */
public class About {


	public GUI gui;
	
	private Stage AboutWindow = new Stage();

	public About(GUI g){
		this.gui = g;

	}

	public void display(){
		
		
		Font TitleFont = new Font(30);
		Font subtitleFont = new Font(22);
		Font subSubTitleFont = new Font(18);

		
		Canvas c = new Canvas(600,300);
		GraphicsContext gc = c.getGraphicsContext2D(); 
		
		
		Label Title = new Label("JavaChess, ChessPI AndChess");
		Title.setFont(subtitleFont);
		Title.setLayoutX(300);
		Title.setLayoutY(5);
		
		Image image = new Image("/javachess/images/JavaChess.png");
		gc.drawImage(image, 0, 0, 300, 300);
		
		//-----------------------------------------------------------------
		Label Devs = new Label("Developers:");
		Devs.setLayoutX(300);
		Devs.setLayoutY(40);
		Devs.setFont(subSubTitleFont);
		
		Label Beiser = new Label("Alexander Beiser");
		Beiser.setLayoutX(325);
		Beiser.setLayoutY(60);
		
		Label Beiser_Task = new Label("- AI, Game Logic");
		Beiser_Task.setLayoutX(440);
		Beiser_Task.setLayoutY(60);
		
		Label Huber = new Label("Marcel Huber");
		Huber.setLayoutX(325);
		Huber.setLayoutY(80);
		
		Label Huber_Task = new Label("- GUI, LAN");
		Huber_Task.setLayoutX(440);
		Huber_Task.setLayoutY(80);
		//-----------------------------------------------------------------
		
		Label Licence = new Label("Licence:");
		Licence.setLayoutX(300);
		Licence.setLayoutY(110);
		Licence.setFont(subSubTitleFont);
		
		Label Licence_Text = new Label("The programm is developed under the \r,,Creative Commons Attribution-\rNonCommercial ShareAlike 4.0'' licence.");
		Licence_Text.setLayoutX(325);
		Licence_Text.setLayoutY(130);
		//----------------------------------------------------------
		
		Label Further = new Label("For further information feel free to visit:");
		Further.setLayoutX(300);
		Further.setLayoutY(200);
		
		Hyperlink gitHub = new Hyperlink();
		gitHub.setText("View on GitHub");
		gitHub.setLayoutX(325);
		gitHub.setLayoutY(220);
		
		Label URL_Label = new Label("https://github.com/alexl4123/diplomarbeit");
		URL_Label.setLayoutX(325);
		URL_Label.setLayoutY(250);
		
		
		//----------------------------------------------------------
		Group gp = new Group();
		gp.getChildren().addAll(Title, c,Devs,Beiser,Beiser_Task,Huber,Huber_Task, Licence, Licence_Text,Further,gitHub);
		Scene scene1= new Scene(gp, 600, 300, Color.WHITE);
		
		
		
		gitHub.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				 URI u;
				try {
					u = new URI("https://github.com/alexl4123/diplomarbeit");
					java.awt.Desktop.getDesktop().browse(u);
				} catch (URISyntaxException | IOException e) {
					gp.getChildren().add(URL_Label);
					e.printStackTrace();
				}
			        
				
			}
		});
		
		


		AboutWindow.initModality(Modality.NONE);
		AboutWindow.setTitle("About");


		AboutWindow.setScene(scene1);
		AboutWindow.setResizable(false);
		//popupwindow.showAndWait();
		
	}
	
	public void showAboutWindow(){
		AboutWindow.showAndWait();
	}
	
	
	
}
