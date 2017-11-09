package threadJobs;

import BackgroundMatrix.BackgroundGrid;
import GuiStuff.BoardGui;
import java.awt.Color;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JTextArea;











public class TheThreadWhoIsAlwaysWaitingJob
  implements Runnable
{
  BoardGui b;
  ServerSocket s;
  Socket cs;
  BackgroundGrid BGG;
  
  public void run()
  {
    try
    {
      cs = s.accept();
      b.setClientSock(cs);
      BGG.setIsConnectet(true);
      b.getTextArea().append("console>> Connection established");
      if (BGG.getIsConnectet()) {
        b.con2.setBackground(Color.green);
      } else {
        b.con2.setBackground(Color.red);
      }
      if (BGG.getTeam()) {
        b.player2.setBackground(Color.white);
      } else {
        b.player2.setBackground(Color.black);
      }
      b.createStreams(cs);
      b.makeANewReaderThread();
      b.getOOS().writeObject(BGG);
    }
    catch (IOException localIOException) {}
  }
  















  public TheThreadWhoIsAlwaysWaitingJob(BoardGui bg1, ServerSocket ss, BackgroundGrid BGG1)
  {
    BGG = BGG1;
    b = bg1;
    s = ss;
  }
}
