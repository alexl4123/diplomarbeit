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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;


/**
 * This class gives information about the developers, the licence and a link to the GitHub page.
 * @author alexl12
 *
 */
public class Help {


	public GUI gui;

	private Stage HelpWindow = new Stage();

	public Help(GUI g){
		this.gui = g;

	}

	public void display(){


		Font TitleFont = new Font(30);
		Font subtitleFont = new Font(22);
		Font subSubTitleFont = new Font(18);




		Canvas c = new Canvas(600,300);
		GraphicsContext gc = c.getGraphicsContext2D(); 

		ScrollPane scrollPane = new ScrollPane(c);
		scrollPane.setPrefSize(600, 300);
		scrollPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		scrollPane.setFitToWidth(true);
		scrollPane.setFitToHeight(true);

		scrollPane.setStyle("-fx-focus-color: transparent;");

		//----------------------------------------------------------
		Label Header = new Label("Help - JavaChess:");
		Header.setLayoutX(5);
		Header.setLayoutY(5);
		Header.setFont(TitleFont);

		Text general = new Text();
		general.setFont(new Font(16));
		general.setWrappingWidth(500);
		general.setLayoutX(20);
		general.setLayoutY(65);
		general.setTextAlignment(TextAlignment.JUSTIFY);
		general.setText("This help box will give you an overview, how to use this game properly. \n"
				+ "1) GameModes - What are the gamemodes? \n"
				+ "2) GUI - How to use the graphical user interface \n"
				+ "3) LAN - How to use the Online mode \n"
				+ "4) AI - What are the AI-Options?");

		//----------------------------------------------------------
		Label gameModes_Label = new Label("1) GameModes");
		gameModes_Label.setLayoutX(5);
		gameModes_Label.setLayoutY(190);
		gameModes_Label.setFont(subtitleFont);

		Text gameModes = new Text();
		gameModes.setFont(new Font(16));
		gameModes.setWrappingWidth(500);
		gameModes.setLayoutX(20);
		gameModes.setLayoutY(235);
		gameModes.setTextAlignment(TextAlignment.JUSTIFY);
		gameModes.setText("In this game, there are three ,,main'' GameModes, one hidden GameMode and the Launchpad support. \n"
				+ "a) The default GameMode is the ,,Hot-Seat'' mode. In this mode the player can play against a friend (or himself) on the screen. \n"
				+ "b) The LAN mode ist the online mode. Here you can play against your friends in the local area network. See  3) for further instructions.\n"
				+ "c) The AI mode is the ,,computer'' mode. You can play here against the Artificial Intelligence. See 4) for further instructions. \n"
				+ "d) The ,,hidden'' mode, is were two AIs turn agains each other. Let's be curios who wins... \n"
				+ "e) The Launchpad mode. This is the result of the cooperation with an other thesis. Here you can plug in the launchpad (8x8-LED-Touch Field), select which version you want to have, and play with the launchpad or the Java application.");

		//----------------------------------------------------------
		Label GUI_Label = new Label("2) GUI - Graphical User Interface");
		GUI_Label.setLayoutX(5);
		GUI_Label.setLayoutY(540);
		GUI_Label.setFont(subtitleFont);

		Text GUI_Text = new Text();
		GUI_Text.setFont(new Font(16));
		GUI_Text.setWrappingWidth(500);
		GUI_Text.setLayoutX(20);
		GUI_Text.setLayoutY(585);
		GUI_Text.setTextAlignment(TextAlignment.JUSTIFY);
		GUI_Text.setText("In your front you will see the chess field, an 8x8 field.\n"
				+ "In this field you play chess and win against your friens! \n"
				+ "Select a meeple by pressing on it. The game will show you all allowed moves. The other possibility is to drag a meeple and let loose at the wished destination (again the movable fields will be shown). \n"
				+ "There is also an upper bar. This bar is the ,,menu-bar''. Here are three different options: \n"
				+ "Game: Here you can start a new, save the current, load a previous or exit the game.\n"
				+ "GameModes: You can select the differenct game modes, disscussed in 1).\n"
				+ "Other: Here are the settings and the option to claim a draw.\n"
				+ "Help: The help page and the 'about page' is found here. ");

		//----------------------------------------------------------
		Label LAN_Label = new Label("3) LAN - Local Area Network (Online)");
		LAN_Label.setLayoutX(5);
		LAN_Label.setLayoutY(860);
		LAN_Label.setFont(subtitleFont);

		Text LAN_Text = new Text();
		LAN_Text.setFont(new Font(16));
		LAN_Text.setWrappingWidth(500);
		LAN_Text.setLayoutX(20);
		LAN_Text.setLayoutY(905);
		LAN_Text.setTextAlignment(TextAlignment.JUSTIFY);
		LAN_Text.setText("With the LAN mode, you can play with friends, even when they use an other computer.\n"
				+ "To play via netowrk, one player needs to be the 'host' and the other one the 'client'.\n"
				+ "The host opens a game and waits for connections, the client activeley connects to a host.\n"
				+ "If you want to play via network, you need to click the 'Gamemodes' button in the menu bar. Then you need to select 'online'.\n"
				+ "After that, you can choose wether you want to be the client or the host. If you are the host, just wait for a client to connect to you.\n"
				+ "if you are the client, you need to enter the IP-address of the host. The host-IP is displayed on the screen of the host.\n"
				+ "To abort a game, simply click on 'Gamemodes' in the menu bar and select 'disconnect'."); //TODO -For HEGL



		//----------------------------------------------------------
		//TODO -after LAN has been added, fix layout
		Label AI_Label = new Label("4) AI - Artificial Intelligence");
		AI_Label.setLayoutX(5);
		AI_Label.setLayoutY(1200);
		AI_Label.setFont(subtitleFont);

		Text AI_Text = new Text();
		AI_Text.setFont(new Font(16));
		AI_Text.setWrappingWidth(500);
		AI_Text.setLayoutX(20);
		AI_Text.setLayoutY(1245);
		AI_Text.setTextAlignment(TextAlignment.JUSTIFY);
		AI_Text.setText("This doen't want to explain, how the AI works (you can read it on GitHub, see ,,about''), "
				+ "this gives the player enough information to play the game properly. \n"
				+ "Default, the AI is the black team with difficulty level 3. The difficulty level indicates the pre simulated moves, so three means,"
				+ "that the AI thinks three rounds into the future. \n"
				+ "If you increase this value via the slider in setup, the AI performs unlinear better, "
				+ "but your pc might be to slow, to handle the necessary computing \n"
				+ "There is also an ,,Hardcore-Mode''. In this mode the AI is bound to an much better core algorithm, which means for you,"
				+ "that you probably won't beat it on difficulty >= 4. \n"
				+ "...but there is one more thing... if you open the LAN mode and try to host, try to put into the IP Address field"
				+ "the following words: SkyNet (attention Case sensitive without space). No click on a random white meeple and whoops, "
				+ "the AI plays against itself... ;-) \n"
				+ "Futher you can choose against which AI you want to play. You can choose this in the drop down "
				+ "menu in the setup field (Black and White). This is not the team you want to play with, it is the AI Team!");

		
		
		Text End_Text = new Text();
		End_Text.setFont(new Font(18));
		End_Text.setWrappingWidth(500);
		End_Text.setLayoutX(20);
		End_Text.setLayoutY(1700);
		End_Text.setTextAlignment(TextAlignment.JUSTIFY);
		End_Text.setText("We hope, we could inform you how to play this game. If the very very unlikely situation occurs, "
				+ "that you still have questions, feel free to contact us on GitHub (alexl4123 and Hegl1). Link is in the about field.\n");
		//----------------------------------------------------------

		Group gp = new Group();
		gp.getChildren().addAll(c,Header, general,gameModes_Label, gameModes, GUI_Label, GUI_Text,LAN_Label,LAN_Text, AI_Label, AI_Text, End_Text);
		scrollPane.setContent(gp);
		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		VBox root = new VBox();
		root.getChildren().addAll(scrollPane);
		Scene scene1= new Scene(root, Color.WHITE);

		HelpWindow.initModality(Modality.NONE);
		HelpWindow.setTitle("Help");

		HelpWindow.getIcons().add(new Image("javachess/images/JavaChess.png"));
		HelpWindow.setScene(scene1);
		HelpWindow.setResizable(false);


	}

	public void showHelpWindow(){
		HelpWindow.showAndWait();
	}
}
