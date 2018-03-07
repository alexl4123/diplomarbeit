package javachess.saveload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import javachess.backgroundmatrix.BackgroundGrid;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * @author alex - 2017
 * @version 1.1
 *
 *	Class for loading.
 *	Gets the File and returns a BackgroundGrid
 */
public class load {
	
	/**
	 * only method - returns BackgroundGrid.
	 * Opens a given File
	 * 
	 * @param f - File - opens that given file
	 * @return BackgroundGrid - returns the BackgroundGrid of the given File
	 */
	public BackgroundGrid openFile(File f){
		BackgroundGrid BGG2;
		BGG2 = null;
		
		try {
			FileInputStream fis = new FileInputStream(f);
			ObjectInputStream ois = new ObjectInputStream(fis);
			
			BGG2 = (BackgroundGrid) ois.readObject();
			ois.close();
			fis.close();
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Success");
			alert.setHeaderText("File could be loaded!");
			alert.setContentText("File is successfully loaded from your location!");
			alert.showAndWait();
			return BGG2;
		} catch (FileNotFoundException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("File could not be loaded!");
			alert.setContentText("File could not be found at the location!");
			alert.showAndWait();
		} catch ( IOException e){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("File could not be loaded!");
			alert.setContentText("File could not be read!");
			alert.showAndWait();
		} catch (ClassNotFoundException e) {
			Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("File could not be loaded!");
		alert.setContentText("Wrong file?");
		alert.showAndWait();
		} catch (NullPointerException e){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("File could not be loaded!");
			alert.setContentText("File could not be found at the location!");
			alert.showAndWait();
		}
		
		return BGG2;
	}
}
