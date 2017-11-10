package threadJobs;

import BackgroundMatrix.BackgroundGrid;
import GuiStuff.BoardGui;
import GuiStuff.BoardGui.ChessField;
import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;








public class ThreadForReading
  implements Runnable
{
  BackgroundGrid BGG;
  BoardGui BG;
  
  public ThreadForReading(BackgroundGrid BGG1, BoardGui BG1)
  {
    BGG = BGG1;
    BG = BG1;
  }
  








  public void run()
  {
    try
    {
      do
      {
        Object o = BG.getOIS().readObject();
        

        if ((BG.servSock != null) && (o.equals("end"))) {
          JFrame Frame1 = new JFrame("a frame");
          JOptionPane.showMessageDialog(Frame1, "The client connection has been interrupted!", "CONNECTION INTERUPTION", 0);
          System.out.println("Connection interrupting!!!");
          BG.getNetworkMenu().remove(BG.closeServ);
          System.out.println("connection interrupted!!");
          BG.getOIS().close();
          BG.getOOS().close();
          BG.servSock.close();
          BG.servSock = null;
          BGG.setIsConnectet(false);
          BGG.setMove(true);
          BG.Frame.setVisible(false);
          BG.getTextArea().setText("");
          break;
        }
        
        if ((BG.servSock == null) && (o.equals("end"))) {
          JFrame Frame1 = new JFrame("a frame");
          JOptionPane.showMessageDialog(Frame1, "The host connection has been interrupted!", "CONNECTION INTERUPTION", 0);
          BG.getOIS().close();
          BG.getOOS().close();
          BG.clientSock2.close();
          BG.clientSock2 = null;
          BGG.setIsConnectet(false);
          BGG.setMove(true);
          break;
        }
        







        System.out.println("I AM LISTENING");
        BGG = ((BackgroundGrid)o);


      }
      while (BGG.getMove());
      BGG.setName("NEWNAME");
      BG.setBackgroundGridRead(BGG);
      BG.ChessPanel.renewPanel(0, 0, false);
      BG.ChessPanel.getSchach();
      if (BGG.getIsConnectet()) {
        BG.con2.setBackground(Color.green);
      } else {
        BG.con2.setBackground(Color.red);
      }
      if (BGG.getTeam()) {
        BG.player2.setBackground(Color.white);
      } else {
        BG.player2.setBackground(Color.black);
      }
      
    }
    catch (ClassNotFoundException e)
    {
      System.out.println("fail1");
    }
    catch (IOException e)
    {
      System.out.println("fail2");
    }
  }
}
