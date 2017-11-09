package Gui;

import BackgroundMatrix.BackgroundGrid;
import SaveLoad.load;
import SaveLoad.save;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Optional;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleGroup;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import network.hostingJob;













public class Menu
  extends MenuBar
{
  RadioMenuItem GameMode0;
  RadioMenuItem GameMode1;
  RadioMenuItem GameMode2;
  
  public Menu(final GUI Gui)
  {
    javafx.scene.control.Menu menuFile = new javafx.scene.control.Menu("File");
    javafx.scene.control.Menu menuGame = new javafx.scene.control.Menu("Game");
    javafx.scene.control.Menu menuSound = new javafx.scene.control.Menu("Sound");
    

    MenuItem newGame = new MenuItem("New");
    MenuItem Save = new MenuItem("Save");
    MenuItem Load = new MenuItem("Load");
    MenuItem Exit = new MenuItem("Exit");
    

    GameMode0 = new RadioMenuItem("Game Mode Local");
    GameMode1 = new RadioMenuItem("Game Mode LAN");
    GameMode2 = new RadioMenuItem("Game Mode AI");
    

    MenuItem Draw = new MenuItem("Draw");
    


    ToggleGroup group = new ToggleGroup();
    GameMode0.setToggleGroup(group);
    GameMode1.setToggleGroup(group);
    GameMode2.setToggleGroup(group);
    GameMode0.setSelected(true);
    

    menuFile.getItems().addAll(new MenuItem[] { newGame, Save, Load, Exit });
    menuGame.getItems().addAll(new MenuItem[] { GameMode0, GameMode1, GameMode2, Draw });
    

    getMenus().addAll(new javafx.scene.control.Menu[] { menuFile, menuGame, menuSound });
    






    newGame.setOnAction(new EventHandler()
    {
      public void handle(ActionEvent event)
      {
        Gui.newBG();
      }
      

    });
    Exit.setOnAction(new EventHandler()
    {
      public void handle(ActionEvent event)
      {
        System.exit(1);

      }
      

    });
    Save.setOnAction(new EventHandler()
    {

      public void handle(ActionEvent event)
      {
        FileChooser fileC = new FileChooser();
        fileC.setTitle("Open Game File");
        fileC.setInitialFileName("chess.sav");
        

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("SAV files (*.sav)", new String[] { "*.sav" });
        fileC.getExtensionFilters().add(extFilter);
        
        File f = fileC.showSaveDialog(Gui.getStage());
        save s = new save();
        s.saveFile(Gui.getBGG2(), f);
      }
      

    });
    Load.setOnAction(new EventHandler()
    {
      public void handle(ActionEvent event)
      {
        FileChooser fileC = new FileChooser();
        fileC.setTitle("Open Game File");
        fileC.setInitialFileName("chess.sav");
        

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("SAV files (*.sav)", new String[] { "*.sav" });
        fileC.getExtensionFilters().add(extFilter);
        
        File f = fileC.showOpenDialog(Gui.getStage());
        load l = new load();
        BackgroundGrid BGG2 = l.openFile(f);
        if (BGG2 != null) {
          Gui.LoadBG(BGG2);

        }
        

      }
      

    });
    Draw.setOnAction(new EventHandler()
    {
      public void handle(ActionEvent event)
      {
        if ((!Gui.getBGG2().getSchachmattBlack()) && (!Gui.getBGG2().getSchachMattWhite()) && (!Gui.getBGG2().getDraw()))
        {
          Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
          alert.setContentText("Do you really want to make a draw?");
          alert.setTitle("Wish to draw");
          alert.setHeaderText("Wish to draw.");
          
          Optional<ButtonType> result = alert.showAndWait();
          if (result.get() == ButtonType.OK) {
            Gui.getBGG2().changeTeam();
            Gui.wishDraw();

          }
          
        }
        
      }
      

    });
    GameMode0.setOnAction(new EventHandler()
    {
      public void handle(ActionEvent event)
      {
        Gui.setChoose(0);
        setSelect(Gui.getChoose());

      }
      

    });
    GameMode1.setOnAction(new EventHandler()
    {

      public void handle(ActionEvent event)
      {
        Alert chooser = new Alert(Alert.AlertType.CONFIRMATION);
        chooser.setTitle("Select Mode");
        chooser.setHeaderText("Choose, wether you would like to host or to join a game!");
        chooser.setContentText("Select your option:");
        
        ButtonType hostButton = new ButtonType("host");
        ButtonType joinButton = new ButtonType("join");
        ButtonType abortButton = new ButtonType("abort");
        
        chooser.getButtonTypes().setAll(new ButtonType[] { hostButton, joinButton, abortButton });
        Optional<ButtonType> result = chooser.showAndWait();
        




        if (result.get() == hostButton)
        {
          Gui.setChoose(1);
          
          Alert hostingAlert = new Alert(Alert.AlertType.INFORMATION);
          hostingAlert.setTitle("Hosting");
          hostingAlert.setHeaderText("You are trying to host a network game!");
          hostingAlert.setContentText("Please wait until another player joins you...");
          
          hostingJob hostJob = new hostingJob(Gui.getBGG2());
          Thread hostingThread = new Thread(hostJob);
          Gui.getBoardGui().drawBlurryMenu();
          





















































          System.out.println(Gui.getBGG2().getIsConnectet());
          
          System.out.println("Ausgebrochen aus Hostingschleiffe");



        }
        else if (result.get() == joinButton)
        {
          Gui.setChoose(1);
          
          InetAddress joinAdress = null;
          
          TextInputDialog ipDialogoue = new TextInputDialog("127.0.0.1");
          ipDialogoue.setTitle("Connecting");
          ipDialogoue.setHeaderText("Please enter the IP address of the host!");
          ipDialogoue.setContentText("The address should look like this: ");
          
          Optional<String> ipResult = ipDialogoue.showAndWait();
          if (ipResult.isPresent()) {
            try
            {
              joinAdress = InetAddress.getByName((String)ipResult.get());
              System.out.println(joinAdress.toString());
              Socket sockForClient = new Socket(joinAdress, 22359);
              Gui.getBGG2().setSocketOfClient(sockForClient);
            }
            catch (UnknownHostException e) {
              Alert addressAlert = new Alert(Alert.AlertType.ERROR);
              addressAlert.setTitle("Error");
              addressAlert.setHeaderText("Connection Failed");
              addressAlert.setContentText("It seems that something is wrong with the IP address!");
              addressAlert.show();
              Gui.setChoose(0);
              setSelect(Gui.getChoose());
            }
            catch (IOException e)
            {
              Alert connectionAlert = new Alert(Alert.AlertType.ERROR);
              connectionAlert.setTitle("Error");
              connectionAlert.setHeaderText("COnnection Failed");
              connectionAlert.setContentText("The desired host is not reachable.");
              connectionAlert.show();
              Gui.setChoose(0);
              setSelect(Gui.getChoose());
            }
            
          }
          

        }
        else if (result.get() == abortButton)
        {
          Gui.setChoose(0);
          System.out.println(Gui.getChoose());
          setSelect(Gui.getChoose());


        }
        

      }
      


    });
    GameMode2.setOnAction(new EventHandler()
    {
      public void handle(ActionEvent event)
      {
        Gui.setChoose(2);
      }
      
    });
    setSelect(Gui.getChoose());
  }
  








  public void setSelect(int i)
  {
    if (i == 0) {
      GameMode0.setSelected(true);
      GameMode1.setSelected(false);
      GameMode2.setSelected(false);
    } else if (i == 1) {
      GameMode1.setSelected(true);
      GameMode0.setSelected(false);
      GameMode2.setSelected(false);
    } else if (i == 2) {
      GameMode2.setSelected(true);
      GameMode0.setSelected(false);
      GameMode1.setSelected(false);
    }
  }
}
