package network;

import BackgroundMatrix.BackgroundGrid;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class hostingJob implements Runnable
{
  private ServerSocket serversock;
  private BackgroundGrid bgg;
  private Socket tempsock;
  private int connectionCounter;
  private boolean running;
  
  public hostingJob(BackgroundGrid bgg)
  {
    this.bgg = bgg;
  }
  
  public void run()
  {
    try
    {
      setServersock(new ServerSocket(22359));
      connectionCounter = 0;
      running = true;
    }
    catch (IOException e)
    {
      System.out.println("ERROR during Socket generation");
    }
    

    while (running)
    {
      try
      {

        System.out.println("Hosting");
        tempsock = getServersock().accept();
        System.out.println("connectet");
        

        if (connectionCounter < 1)
        {
          bgg.setSocketOfHost(tempsock);
          bgg.setIsConnectet(true);
          connectionCounter += 1;
          System.out.println("connection established");

        }
        


      }
      catch (IOException e)
      {


        System.out.println("THREAD STOPPED WORKinG");
      }
    }
  }
  

  public ServerSocket getServersock()
  {
    return serversock;
  }
  
  public void setServersock(ServerSocket serversock) {
    this.serversock = serversock;
  }
  
  public void stopSocket()
  {
    try {
      serversock.close();
      running = false;
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}
