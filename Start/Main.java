package Start;

import BackgroundMatrix.BackgroundGrid;
import Gui.GUI;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;







public class Main
  extends Application
{
  public Main() {}
  
  public static void main(String[] args)
  {
    launch(
      new String[0]);
  }
  
  public void start(Stage primaryStage) throws Exception {
    Platform.runLater(new Runnable()
    {
      public void run()
      {
        try {
          BackgroundGrid BGG = new BackgroundGrid();
          GUI G = new GUI(BGG);
          G.start(new Stage());
        }
        catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    });
  }
}
