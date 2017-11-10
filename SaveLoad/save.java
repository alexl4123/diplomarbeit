package SaveLoad;

import BackgroundMatrix.BackgroundGrid;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;










public class save
{
  public save() {}
  
  public void saveFile(BackgroundGrid BGG2, File f)
  {
    try
    {
      FileOutputStream fos = new FileOutputStream(f);
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      
      oos.writeObject(BGG2);
      oos.close();
      fos.close();
      
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Success");
      alert.setHeaderText("File could be saved!");
      alert.setContentText("File is successfully saved at your location!");
      alert.showAndWait();
    }
    catch (FileNotFoundException e) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText("File couldn�t be saved!");
      alert.setContentText("File could not be found!");
      alert.showAndWait();
    }
    catch (IOException e) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText("File couldn�t be saved!");
      alert.setContentText("Input Output error. Should this error continue to pop up, please contact us!");
      alert.showAndWait();
    } catch (NullPointerException e) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText("File couldn�t be saved!");
      alert.setContentText("File could not be found!");
      alert.showAndWait();
    }
  }
}
