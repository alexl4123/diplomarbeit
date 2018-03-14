package javachess.start;

import javachess.backgroundmatrix.BackgroundGrid;
import javachess.gui.GUI;
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
	
	GUI G;

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
					 G = new GUI(BGG);
					 BGG.setGui(G);
					G.start(new Stage());
					

				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}
		});

	}
	
	@Override
	public void stop(){
		
	    try {
	    if(G.getMenu().hostJob.getServersock() != null){
	    	G.getBoardGui().heartBeatJob.stopHeartBeat();
	    	G.getMenu().hostJob.stopSocket();
	    	
	    	
	    }
	    }catch(Exception ex) {
	    	System.exit(0);
	    }
	    
	}

}
