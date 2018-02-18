package Start;

import BackgroundMatrix.BackgroundGrid;
import Game.Local;
import Gui.GUI;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * Here starts the fun ;) Main extends Application and launches it then it makes
 * a Platform.runLater with a new Runnable. This one starts the real chess Gui.
 * 
 * @author alexl12
 * @version 1.1 - Draw
 *
 */
public class Main extends Application {

	public static void main(String[] args) {

		launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				try {
					BackgroundGrid BGG = new BackgroundGrid();
					GUI G = new GUI(BGG);
					G.start(new Stage());
					

				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}
		});

	}

}
