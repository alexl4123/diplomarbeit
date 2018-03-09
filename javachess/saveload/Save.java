package javachess.saveload;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javachess.backgroundmatrix.BackgroundGrid;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * @author alex - 2017
 * @version 1.0
 * 
 * Saves the BackgroundGrid to a given location
 *
 */
public class Save {
	
	/**
	 * only method - simply saves the BackgroundGrid to File f
	 * @param BGG2 - BackgroundGrid - what to save? - this
	 * @param f - File - where to save? - here
	 */
	public void saveFile(BackgroundGrid BGG2, File f){
		try {
			FileOutputStream fos = new FileOutputStream(f);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			
			oos.writeObject(BGG2);
			oos.close();
			fos.close();
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Success");
			alert.setHeaderText("File could be saved!");
			alert.setContentText("File is successfully saved at your location!");
			alert.showAndWait();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("File couldn�t be saved!");
			alert.setContentText("File could not be found!");
			alert.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("File couldn�t be saved!");
			alert.setContentText("Input Output error. Should this error continue to pop up, please contact us!");
			alert.showAndWait();
		} catch (NullPointerException e){
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("File couldn�t be saved!");
			alert.setContentText("File could not be found!");
			alert.showAndWait();
		}
		
	}
}
