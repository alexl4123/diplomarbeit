package GuiStuff;

import BackgroundMatrix.BackgroundGrid;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import meeple.Farmer;
import meeple.Jumper;
import meeple.King;
import meeple.Queen;
import meeple.Runner;
import meeple.Tower;
import musik.SoundMachine;
import threadJobs.ThreadForReading;

public class BoardGui
{
  BackgroundGrid BGG;
  JFrame ChessFrame = new JFrame("Chess Game");
  BoardGui.ChessField.ChessButton[][] Buttons;
  JTextArea consoleField;
  public ServerSocket servSock;
  public Socket clientSock2 = null;
  ObjectOutputStream oos;
  ObjectInputStream ois;
  static BoardGui BG;
  public ChessField ChessPanel;
  boolean isHost;
  public JFrame Frame;
  public JTextArea consolefield;
  public boolean WhiteSchach;
  private JMenu network;
  public boolean BlackSchach;
  public JMenuItem host = new JMenuItem("Host Game");
  public JMenuItem connect = new JMenuItem("Join Game");
  public JMenuItem closeServ = new JMenuItem("Cut conncetion");
  public JMenuItem disconnect = new JMenuItem("Disconnect");
  public JButton player2 = new JButton();
  public JButton con2 = new JButton();
  JLabel turn1 = new JLabel();
  
  SoundMachine SM1;
  
  private JFrame VolumeFrame;
  private Thread TH;
  
  public BoardGui(BackgroundGrid BGG2)
  {
    BGG = BGG2;
    servSock = null;
    isHost = true;
    ChessFrame.setDefaultCloseOperation(3);
  }
  








  public static void main(String[] args)
  {
    BackgroundGrid BGG = new BackgroundGrid();
    BG = new BoardGui(BGG);
    BG.Gui();
  }
  





  public void Gui()
  {
    SM1 = new SoundMachine("/musik/music7.wav", 1);
    TH = new Thread(SM1);
    TH.start();
    java.net.URL url = BoardGui.class.getResource("/Images/gameIcon.png");
    ImageIcon GameIcon = new ImageIcon(url);
    ChessFrame.setIconImage(GameIcon.getImage());
    


    ChessFrame.setDefaultCloseOperation(0);
    ChessFrame.addWindowListener(new java.awt.event.WindowListener()
    {
      public void windowOpened(WindowEvent arg0) {}
      





      public void windowIconified(WindowEvent arg0) {}
      




      public void windowDeiconified(WindowEvent arg0) {}
      




      public void windowDeactivated(WindowEvent arg0) {}
      




      public void windowClosing(WindowEvent arg0)
      {
        if ((servSock != null) && (BGG.getIsConnectet())) {
          SoundMachine SM = new SoundMachine("/musik/menuClick.wav", 0);
          TH = new Thread(SM);
          TH.start();
          try {
            BoardGui.BG.getOOS().writeObject("end");
            System.out.println("end has been written! ");
            BoardGui.BG.getOIS().close();
            BoardGui.BG.getOOS().close();
            BGservSock.close();


          }
          catch (Exception e)
          {

            e.printStackTrace();
          }
          finally
          {
            System.exit(1);
          }
          
        }
        else if ((servSock == null) && (BGG.getIsConnectet()))
        {
          try
          {
            BoardGui.BG.getOOS().writeObject("end");
            BoardGui.BG.getOOS().flush();
            BoardGui.BG.getOOS().close();


          }
          catch (Exception localException1) {}finally
          {

            System.exit(1);

          }
          

        }
        else if (!BGG.getIsConnectet())
        {
          System.exit(1);
        }
      }
      



      public void windowClosed(WindowEvent arg0)
      {
        SoundMachine SM = new SoundMachine("/musik/menuClick.wav", 0);
        Thread TH = new Thread(SM);
        TH.start();
      }
      











      public void windowActivated(WindowEvent arg0) {}
    });
    ChessPanel = new ChessField();
    ChessFrame.add(ChessPanel);
    buildInfoPanel();
    buildMenu(ChessPanel);
    ChessFrame.setBounds(200, 200, 600, 600);
    ChessFrame.setVisible(true);
  }
  


  public void buildVolumeFrame()
  {
    VolumeFrame = new JFrame("Change VOL");
    JButton reduce = new JButton("-");
    JButton increase = new JButton("+");
    JButton mute = new JButton("mute");
    
    mute.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        try
        {
          SM1.muteVOL();
        } catch (Exception ex) {
          System.out.println("no mute");
        }
        
      }
      

    });
    reduce.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        try
        {
          SM1.redurceVOl();



        }
        catch (Exception localException) {}

      }
      


    });
    increase.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        try {
          SM1.increaseVOL();


        }
        catch (Exception localException) {}
      }
      

    });
    Point pointer = ChessFrame.getLocation();
    int Xpos = (int)pointer.getX() + 245;
    int Ypos = (int)pointer.getY() + 267;
    
    Point newPoint = new Point(Xpos, Ypos);
    

    VolumeFrame.setLocation(newPoint);
    
    VolumeFrame.setLayout(new java.awt.FlowLayout());
    VolumeFrame.add(reduce);
    VolumeFrame.add(increase);
    VolumeFrame.add(mute);
    VolumeFrame.setSize(110, 75);
    VolumeFrame.setResizable(false);
    VolumeFrame.setVisible(false);
  }
  



  private void setIJPG(int i) {}
  


  public void buildInfoPanel()
  {
    JPanel InfoPanel = new JPanel();
    

    turn1.setText("Turn: " + (BGG.getTurnRound() / 2 + 1));
    


    JLabel con1 = new JLabel();
    
    con1.setText("Connection Status: ");
    con2.setEnabled(false);
    if (BGG.getIsConnectet()) {
      con2.setBackground(Color.green);
    } else {
      con2.setBackground(Color.red);
    }
    
    JLabel player1 = new JLabel();
    player1.setText("Turn: ");
    player2.setEnabled(false);
    if (BGG.getTeam()) {
      player2.setBackground(Color.white);
    } else {
      player2.setBackground(Color.black);
    }
    InfoPanel.add(turn1);
    

    InfoPanel.add(con1);
    InfoPanel.add(con2);
    InfoPanel.add(player1);
    InfoPanel.add(player2);
    ChessFrame.add(InfoPanel, "South");
  }
  




  public void buildMenu(final ChessField ChessPanel)
  {
    JMenuBar jmbr = new JMenuBar();
    JMenu menFile = new JMenu("File");
    JMenuItem newGame = new JMenuItem("New");
    JMenuItem loadGame = new JMenuItem("Load");
    JMenuItem saveGame = new JMenuItem("Save");
    JMenu Sound = new JMenu("Sound");
    JMenuItem soundControl = new JMenuItem("Volume control");
    JMenuItem nextMusic = new JMenuItem("next Track");
    

    network = new JMenu("Network Game");
    








    newGame.addActionListener(new ActionListener()
    {

      public void actionPerformed(ActionEvent e)
      {
        SoundMachine SM = new SoundMachine("/musik/menuClick.wav", 0);
        Thread TH = new Thread(SM);
        TH.start();
        if ((isHost) && (!BGG.getIsConnectet())) {
          BackgroundGrid freshBgg = new BackgroundGrid();
          BGG = freshBgg;
          ChessPanel.renewPanel(0, 0, false);
          if (BGG.getTeam()) {
            player2.setBackground(Color.white);
          } else {
            player2.setBackground(Color.black);
          }
          int Turns = BGG.getTurnRound() / 2 + 1;
          turn1.setText("This is turn: " + Turns);
        }
        
      }
      
    });
    saveGame.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        SoundMachine SM = new SoundMachine("/musik/menuClick.wav", 0);
        Thread TH = new Thread(SM);
        TH.start();
        if (isHost) {
          boolean temp = BGG.getIsConnectet();
          BGG.setIsConnectet(false);
          JFrame frmClickTheRight = new JFrame();
          JFileChooser chooseFile = new JFileChooser();
          chooseFile.showSaveDialog(frmClickTheRight);
          File saveFile = chooseFile.getSelectedFile();
          try {
            java.io.FileOutputStream fw = new java.io.FileOutputStream(saveFile);
            ObjectOutputStream oos = new ObjectOutputStream(fw);
            oos.writeObject(BGG);
            oos.close();
            SM = new SoundMachine("/musik/menuClick.wav", 0);
            TH = new Thread(SM);
            TH.start();
          }
          catch (Exception ex)
          {
            JFrame Frame1 = new JFrame("a frame");
            JOptionPane.showMessageDialog(Frame1, "File has not been saved!", "Error", 0);
            SM = new SoundMachine("/musik/menuClick.wav", 0);
            TH = new Thread(SM);
            TH.start();
          }
          BGG.setIsConnectet(temp);
        }
        
      }
      

    });
    loadGame.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        SoundMachine SM = new SoundMachine("/musik/menuClick.wav", 0);
        Thread TH = new Thread(SM);
        TH.start();
        
        if ((isHost) && (!BGG.getIsConnectet())) {
          JFrame frmClickTheRight = new JFrame();
          JFileChooser chooseFile = new JFileChooser();
          chooseFile.showOpenDialog(frmClickTheRight);
          File loadFile = chooseFile.getSelectedFile();
          try {
            java.io.FileInputStream fin = new java.io.FileInputStream(loadFile);
            ObjectInputStream ois = new ObjectInputStream(fin);
            BGG = ((BackgroundGrid)ois.readObject());
            ChessPanel.renewPanel(0, 0, false);
            ois.close();
            if (BGG.getTeam()) {
              player2.setBackground(Color.white);
            } else {
              player2.setBackground(Color.black);
            }
            int Turns = BGG.getTurnRound() / 2 + 1;
            turn1.setText("This is turn: " + Turns);



          }
          catch (Exception localException) {}


        }
        

      }
      


    });
    closeServ.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        SoundMachine SM = new SoundMachine("/musik/menuClick.wav", 0);
        Thread TH = new Thread(SM);
        TH.start();
        try {
          BoardGui.BG.getOOS().writeObject("end");
          System.out.println("end has been written! ");
          BoardGui.BG.getOIS().close();
          BoardGui.BG.getOOS().close();



        }
        catch (Exception localException) {}finally
        {



          try
          {


            BGservSock.close();
          }
          catch (IOException e1) {
            e1.printStackTrace();
          }
          BGservSock = null;
          BGG.setIsConnectet(false);
          BGG.setMove(true);
          Frame.setVisible(false);
        }
        



        network.remove(closeServ);
      }
      

    });
    host.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent arg0)
      {
        SoundMachine SM = new SoundMachine("/musik/menuClick.wav", 0);
        Thread TH = new Thread(SM);
        TH.start();
        if (!BGG.getIsConnectet()) {
          consoleField = new JTextArea(10, 40);
          Frame = new JFrame("Console");
          Frame.setResizable(false);
          

          Point pointer = ChessFrame.getLocation();
          int Xpos = (int)pointer.getX() + 80;
          int Ypos = (int)pointer.getY() + 200;
          
          Point newPoint = new Point(Xpos, Ypos);
          
          Frame.setSize(500, 220);
          Frame.setLocation(newPoint);
          



          JPanel panel = new JPanel();
          String consout = "console>> ";
          Frame.setDefaultCloseOperation(0);
          
          JScrollPane scroller = new JScrollPane(consoleField);
          scroller.setVerticalScrollBarPolicy(20);
          scroller.setHorizontalScrollBarPolicy(31);
          panel.add(scroller);
          Frame.add(panel, "Center");
          consoleField.setLineWrap(true);
          consoleField.setEditable(false);
          Frame.setVisible(true);
          
          consoleField.append(consout + "trying to set up a network match...\n\n");
          try
          {
            servSock = new ServerSocket(7500);
          } catch (IOException e) {
            consoleField.append(consout + "ERROR while setting up the network match \n\n");
            e.printStackTrace();
          }
          

          consoleField.append(consout + "setting up the match was successfull \n\n");
          
          threadJobs.TheThreadWhoIsAlwaysWaitingJob tj = new threadJobs.TheThreadWhoIsAlwaysWaitingJob(BoardGui.BG, servSock, BGG);
          Thread listen = new Thread(tj);
          listen.start();
          

          consoleField.append(consout + "waiting for a second player... \n\n");
          
          BGG.setMove(false);
          network.add(closeServ);


        }
        


      }
      


    });
    connect = new JMenuItem("Connect");
    connect.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent arg0)
      {
        SoundMachine SM = new SoundMachine("/musik/menuClick.wav", 0);
        Thread TH = new Thread(SM);
        TH.start();
        
        isHost = false;
        try {
          if (!BGG.getIsConnectet())
          {
            final JFrame ipFrame = new JFrame();
            
            Point pointer = ChessFrame.getLocation();
            int Xpos = (int)pointer.getX() + 200;
            int Ypos = (int)pointer.getY() + 200;
            ipFrame.setResizable(false);
            
            Point newPoint = new Point(Xpos, Ypos);
            
            ipFrame.setSize(200, 110);
            ipFrame.setLocation(newPoint);
            JPanel northernPanel = new JPanel();
            northernPanel.setLayout(new javax.swing.BoxLayout(northernPanel, 3));
            JPanel southernPanel = new JPanel();
            JLabel ipLabel = new JLabel("Enter IP-Address!");
            final JTextField ipField = new JTextField();
            JButton IPButton = new JButton("Connect");
            JButton AbButton = new JButton("Abort");
            
            northernPanel.add(ipLabel);
            northernPanel.add(ipField);
            
            southernPanel.add(IPButton);
            southernPanel.add(AbButton);
            
            ipFrame.add(southernPanel, "South");
            ipFrame.add(northernPanel, "Center");
            ipFrame.setVisible(true);
            ipFrame.setDefaultCloseOperation(0);
            
            IPButton.addActionListener(new ActionListener()
            {
              public void actionPerformed(ActionEvent arg0)
              {
                try {
                  SoundMachine SM = new SoundMachine("/musik/menuClick.wav", 0);
                  Thread TH = new Thread(SM);
                  TH.start();
                  String address = ipField.getText();
                  

                  clientSock2 = new Socket(address, 7500);
                  
                  BGG.setIsConnectet(true);
                  oos = new ObjectOutputStream(clientSock2.getOutputStream());
                  ois = new ObjectInputStream(clientSock2.getInputStream());
                  BGG = ((BackgroundGrid)ois.readObject());
                  BGChessPanel.renewPanel(0, 0, false);
                  ipFrame.setVisible(false);
                  ipField.setText("");
                  
                  BGG.setMove(true);
                  
                  if (BGG.getIsConnectet()) {
                    con2.setBackground(Color.green);
                  } else {
                    con2.setBackground(Color.red);
                  }
                }
                catch (UnknownHostException e1)
                {
                  JOptionPane.showMessageDialog(ipFrame, "Connection refused - Host not reachable!", "ERROR", 0);
                } catch (IOException e2) {
                  JOptionPane.showMessageDialog(ipFrame, "Connection refused - Host not reachable!", "ERROR", 0);

                }
                catch (ClassNotFoundException localClassNotFoundException) {}
              }
              

            });
            AbButton.addActionListener(new ActionListener()
            {
              public void actionPerformed(ActionEvent arg0)
              {
                SoundMachine SM = new SoundMachine("/musik/menuClick.wav", 0);
                Thread TH = new Thread(SM);
                TH.start();
                ipFrame.setVisible(false);
                ipField.setText("");
                network.remove(disconnect);
              }
              

            });
          }
          

        }
        catch (Exception e)
        {

          e.printStackTrace();
        }
        

        network.add(disconnect);




      }
      




    });
    disconnect.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        try
        {
          SoundMachine SM = new SoundMachine("/musik/menuClick.wav", 0);
          Thread TH = new Thread(SM);
          TH.start();
          

          if ((BGG.getIsConnectet()) && (servSock == null))
          {
            network.remove(disconnect);
            

            try
            {
              BoardGui.BG.getOOS().writeObject("end");
              BoardGui.BG.getOOS().flush();
              BoardGui.BG.getOOS().close();
              BGG.setIsConnectet(false);
              BGG.setMove(true);



            }
            catch (IOException localIOException) {}finally
            {



              consoleField.setText("");
              clientSock2.close();
              consoleField.append("streams closed \n\n");
              Frame.setVisible(false);
              if (BGG.getIsConnectet()) {
                con2.setBackground(Color.green);
              } else {
                con2.setBackground(Color.red);

              }
              

            }
            

          }
          


        }
        catch (Exception localException) {}

      }
      

    });
    soundControl.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        SoundMachine SM = new SoundMachine("/musik/menuClick.wav", 0);
        Thread TH = new Thread(SM);
        TH.start();
        
        buildVolumeFrame();
        VolumeFrame.setVisible(true);
      }
      

    });
    nextMusic.addActionListener(new ActionListener()
    {

      public void actionPerformed(ActionEvent e)
      {

        SM1.killSound();
        
        SoundMachine SM = new SoundMachine("/musik/menuClick.wav", 0);
        Thread TH = new Thread(SM);
        
        TH.start();



      }
      



    });
    Sound.add(nextMusic);
    Sound.add(soundControl);
    
    network.add(host);
    network.add(connect);
    menFile.add(newGame);
    menFile.add(saveGame);
    menFile.add(loadGame);
    
    jmbr.add(menFile);
    jmbr.add(network);
    jmbr.add(Sound);
    


    ChessFrame.setJMenuBar(jmbr);
  }
  
  public Thread getSM1Thread()
  {
    return TH;
  }
  
  public JMenu getNetworkMenu()
  {
    return network;
  }
  
  public void setClientSock(Socket s)
  {
    clientSock2 = s;
  }
  
  public ObjectOutputStream getOOS()
  {
    return oos;
  }
  
  public ObjectInputStream getOIS() {
    return ois;
  }
  



  public void createStreams(Socket client)
  {
    try
    {
      oos = new ObjectOutputStream(client.getOutputStream());
      oos.flush();
      
      ois = new ObjectInputStream(client.getInputStream());

    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  





  public void sleep(int time)
  {
    try
    {
      java.util.concurrent.TimeUnit.MILLISECONDS.sleep(time);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  



  public synchronized void setBackgroundGridRead(BackgroundGrid BGG1)
  {
    BGG = BGG1;
    

    ChessPanel.renewPanel(0, 0, false);
    BGG.setMove(true);
  }
  
  public synchronized JTextArea getTextArea() {
    return consoleField;
  }
  

  public void makeANewReaderThread()
  {
    ThreadForReading tr = new ThreadForReading(BGG, BG);
    Thread listen2 = new Thread(tr);
    listen2.start();
  }
  


  public class ChessField
    extends JPanel
  {
    int Chooser;
    

    Boolean Bauerntausch;
    


    public ChessField()
    {
      Chooser = 0;
      Bauerntausch = Boolean.valueOf(false);
      java.awt.Font f = new java.awt.Font("Arial", 1, 20);
      Buttons = new ChessButton[9][9];
      
      super.setLayout(new java.awt.GridLayout(9, 9));
      

      for (int Y = 0; Y < 9; Y++) {
        for (int X = 0; X < 9; X++) {
          Buttons[X][Y] = new ChessButton(X, Y);
          


          if ((Y == 0) && (X >= 0))
          {
            JLabel JL = new JLabel();
            JL.setHorizontalAlignment(0);
            JL.setFont(f);
            switch (X) {
            case 0:  JL.setText(""); break;
            case 1:  JL.setText("A"); break;
            case 2:  JL.setText("B"); break;
            case 3:  JL.setText("C"); break;
            case 4:  JL.setText("D"); break;
            case 5:  JL.setText("E"); break;
            case 6:  JL.setText("F"); break;
            case 7:  JL.setText("G"); break;
            case 8:  JL.setText("H");
            }
            
            add(JL);
          }
          else if ((X == 0) && (Y >= 1)) {
            JLabel JL = new JLabel();
            JL.setHorizontalAlignment(0);
            JL.setVerticalAlignment(0);
            JL.setFont(f);
            switch (Y) {
            case 0:  JL.setText(""); break;
            case 1:  JL.setText("1"); break;
            case 2:  JL.setText("2"); break;
            case 3:  JL.setText("3"); break;
            case 4:  JL.setText("4"); break;
            case 5:  JL.setText("5"); break;
            case 6:  JL.setText("6"); break;
            case 7:  JL.setText("7"); break;
            case 8:  JL.setText("8");
            }
            
            add(JL);
          }
          else {
            if (((Y % 2 == 1) && (X % 2 == 1)) || ((Y % 2 == 0) && (X % 2 == 0))) {
              Buttons[(X - 1)][(Y - 1)].setBackground(Color.white);
              add(Buttons[(X - 1)][(Y - 1)]);
            } else {
              Buttons[(X - 1)][(Y - 1)].setBackground(Color.gray);
              add(Buttons[(X - 1)][(Y - 1)]);
            }
            
            int Back1 = BGG.getBackgroundGrid(X - 1, Y - 1);
            
            if ((BGG.Objects(Back1) instanceof Farmer)) {
              Farmer TheFarmer = (Farmer)BGG.Objects(Back1);
              Buttons[(X - 1)][(Y - 1)].setIcon(TheFarmer.getIcon());
            }
            else if ((BGG.Objects(Back1) instanceof King)) {
              King TheKing = (King)BGG.Objects(Back1);
              Buttons[(X - 1)][(Y - 1)].setIcon(TheKing.getIcon());
            } else if ((BGG.Objects(Back1) instanceof Tower)) {
              Tower TheTower = (Tower)BGG.Objects(Back1);
              Buttons[(X - 1)][(Y - 1)].setIcon(TheTower.getIcon());
            } else if ((BGG.Objects(Back1) instanceof Runner)) {
              Runner TheRunner = (Runner)BGG.Objects(Back1);
              Buttons[(X - 1)][(Y - 1)].setIcon(TheRunner.getIcon()); }
            if ((BGG.Objects(Back1) instanceof Queen)) {
              Queen khaleesi = (Queen)BGG.Objects(Back1);
              Buttons[(X - 1)][(Y - 1)].setIcon(khaleesi.getIcon()); }
            if ((BGG.Objects(Back1) instanceof Jumper)) {
              Jumper drogo = (Jumper)BGG.Objects(Back1);
              Buttons[(X - 1)][(Y - 1)].setIcon(drogo.getIcon());
            }
            



            final int XX = X;
            final int YY = Y;
            Buttons[(X - 1)][(Y - 1)].addActionListener(new ActionListener()
            {


              public void actionPerformed(ActionEvent e)
              {


                SoundMachine SM = new SoundMachine("/musik/buttonClick.wav", 0);
                Thread TH = new Thread(SM);
                TH.start();
                boolean moveMeeple = false;
                












                try
                {
                  if ((BGG.getBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos()) == 0) && (BGG.getMove()))
                  {

                    if ((BGG.getObject() instanceof Farmer))
                    {
                      Farmer TheFarmer = (Farmer)BGG.getObject();
                      
                      if (TheFarmer.move2(BGG.getX(), BGG.getY(), Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), BGG)) {
                        BGG.setBackgroundGrid(BGG.getX(), BGG.getY(), 0);
                        BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), TheFarmer.getID());
                        BGG.changeTeam();
                        
                        TheFarmer.setMeepleXPos(Buttons[XX][YY].getXPos());
                        TheFarmer.setMeepleYPos(Buttons[XX][YY].getYPos());
                        TheFarmer.setLastMovedInRound(BGG.getTurnRound());
                        moveMeeple = true;
                        

                        if ((TheFarmer.isteam()) && (Buttons[XX][YY].getYPos() == 7))
                        {
                          Bauerntausch(true, XX, YY);
                          Bauerntausch = Boolean.valueOf(true);
                        }
                        else if ((!TheFarmer.isteam()) && (Buttons[XX][YY].getYPos() == 0)) {
                          Bauerntausch(false, XX, YY);
                          Bauerntausch = Boolean.valueOf(true);
                        }
                        

                      }
                      else if ((BGG.Objects(BGG.getBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos() - 1)) instanceof Farmer))
                      {
                        Farmer TheFarmer2 = (Farmer)BGG.Objects(BGG.getBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos() - 1));
                        
                        if ((TheFarmer2.getHowOftenMoved() == 1) && (BGG.getTurnRound() - TheFarmer2.getLastMovedInRound() == 1) && (TheFarmer2.getSpecialMoved()))
                        {
                          if (TheFarmer.strike(BGG.getX(), BGG.getY(), Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), BGG.getTeam())) {
                            BGG.setBackgroundGrid(BGG.getX(), BGG.getY(), 0);
                            BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), TheFarmer.getID());
                            BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos() - 1, 0);
                            BGG.changeTeam();
                            renewPanel(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos() - 1, true);
                            TheFarmer.setMeepleXPos(Buttons[XX][YY].getXPos());
                            TheFarmer.setMeepleYPos(Buttons[XX][YY].getYPos());
                            
                            moveMeeple = true;
                            TheFarmer.setLastMovedInRound(BGG.getTurnRound());
                          }
                          
                        }
                      }
                      else if ((BGG.Objects(BGG.getBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos() + 1)) instanceof Farmer))
                      {
                        Farmer TheFarmer2 = (Farmer)BGG.Objects(BGG.getBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos() + 1));
                        if ((TheFarmer2.getHowOftenMoved() == 1) && (BGG.getTurnRound() - TheFarmer2.getLastMovedInRound() == 1) && (TheFarmer2.getSpecialMoved()))
                        {
                          if (TheFarmer.strike(BGG.getX(), BGG.getY(), Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), BGG.getTeam())) {
                            BGG.setBackgroundGrid(BGG.getX(), BGG.getY(), 0);
                            BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), TheFarmer.getID());
                            BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos() + 1, 0);
                            BGG.changeTeam();
                            renewPanel(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos() + 1, true);
                            TheFarmer.setMeepleXPos(Buttons[XX][YY].getXPos());
                            TheFarmer.setMeepleYPos(Buttons[XX][YY].getYPos());
                            moveMeeple = true;
                            TheFarmer.setLastMovedInRound(BGG.getTurnRound());
                          }
                          
                        }
                        
                      }
                      
                    }
                    else if ((BGG.getObject() instanceof King))
                    {
                      King TheKing = (King)BGG.getObject();
                      
                      if (TheKing.move(BGG.getX(), BGG.getY(), Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), BGG.getTeam())) {
                        BGG.setBackgroundGrid(BGG.getX(), BGG.getY(), 0);
                        
                        BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), TheKing.getID());
                        BGG.changeTeam();
                        TheKing.setMeepleXPos(Buttons[XX][YY].getXPos());
                        TheKing.setMeepleYPos(Buttons[XX][YY].getYPos());
                        moveMeeple = true;
                      } else if ((!TheKing.getSchach()) && (TheKing.getHowOftenMoved() < 1))
                      {
                        int dX = Buttons[XX][YY].getXPos() - BGG.getX();
                        int dY = Buttons[XX][YY].getYPos() - BGG.getY();
                        
                        boolean go = false;
                        boolean go2 = false;
                        int iF = 0;
                        

                        if ((dY == 0) && (dX == 2))
                        {

                          for (int i = 1; i < 4; i++) {
                            int X = BGG.getX() + i;
                            
                            if (!BGG.SchachKing(BGG.getTeam(), BGG, X, BGG.getY(), false, false))
                            {
                              if (BGG.getBackgroundGrid(X, BGG.getY()) == 0) {
                                go2 = true;
                              } else {
                                System.out.println("yes");
                                go2 = false;
                                go = false;
                                break;
                              }
                              iF++;
                              if ((iF == 3) && (go2)) {
                                go = true;
                                break;
                              }
                            }
                          }
                          

                          if (go) {
                            if (TheKing.getID() == 145) {
                              Tower TheTower = (Tower)BGG.Objects(110);
                              if (TheTower.getHowOftenMoved() < 1)
                              {
                                BGG.setBackgroundGrid(BGG.getX(), BGG.getY(), 0);
                                BGG.setBackgroundGrid(Buttons[XX][YY].getXPos() + 1, Buttons[XX][YY].getYPos(), 0);
                                BGG.setBackgroundGrid(Buttons[XX][YY].getXPos() + 2, Buttons[XX][YY].getYPos(), 0);
                                BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), TheKing.getID());
                                BGG.setBackgroundGrid(Buttons[XX][YY].getXPos() - 1, Buttons[XX][YY].getYPos(), TheTower.getID());
                                BGG.changeTeam();
                                TheTower.setMeepleXPos(Buttons[XX][YY].getXPos() - 1);
                                TheTower.setMeepleYPos(Buttons[XX][YY].getYPos());
                                TheKing.setMeepleXPos(Buttons[XX][YY].getXPos());
                                TheKing.setMeepleYPos(Buttons[XX][YY].getYPos());
                                renewPanel(Buttons[XX][YY].getXPos() + 2, Buttons[XX][YY].getYPos(), true);
                                renewPanel(Buttons[XX][YY].getXPos() + 1, Buttons[XX][YY].getYPos(), true);
                                renewPanel(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), true);
                                renewPanel(BGG.getX(), BGG.getY(), true);
                                moveMeeple = true;
                              }
                            } else if (TheKing.getID() == 245) {
                              Tower TheTower = (Tower)BGG.Objects(210);
                              if (TheTower.getHowOftenMoved() < 1)
                              {
                                BGG.setBackgroundGrid(BGG.getX(), BGG.getY(), 0);
                                BGG.setBackgroundGrid(Buttons[XX][YY].getXPos() + 1, Buttons[XX][YY].getYPos(), 0);
                                BGG.setBackgroundGrid(Buttons[XX][YY].getXPos() + 2, Buttons[XX][YY].getYPos(), 0);
                                BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), TheKing.getID());
                                BGG.setBackgroundGrid(Buttons[XX][YY].getXPos() - 1, Buttons[XX][YY].getYPos(), TheTower.getID());
                                BGG.changeTeam();
                                TheTower.setMeepleXPos(Buttons[XX][YY].getXPos() - 1);
                                TheTower.setMeepleYPos(Buttons[XX][YY].getYPos());
                                TheKing.setMeepleXPos(Buttons[XX][YY].getXPos());
                                TheKing.setMeepleYPos(Buttons[XX][YY].getYPos());
                                renewPanel(Buttons[XX][YY].getXPos() + 2, Buttons[XX][YY].getYPos(), true);
                                renewPanel(Buttons[XX][YY].getXPos() + 1, Buttons[XX][YY].getYPos(), true);
                                renewPanel(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), true);
                                renewPanel(BGG.getX(), BGG.getY(), true);
                                moveMeeple = true;
                              }
                            }
                          }
                        }
                        else if ((dY == 0) && (dX == -2))
                        {
                          for (int i = 1; i < 3; i++) {
                            int X = BGG.getX() - i;
                            

                            if (!BGG.SchachKing(BGG.getTeam(), BGG, X, BGG.getY(), false, false))
                            {
                              if (BGG.getBackgroundGrid(X, BGG.getY()) == 0) {
                                go2 = true;
                              } else {
                                go2 = false;
                                go = false;
                                break;
                              }
                              iF++;
                              if ((iF == 2) && (go2)) {
                                go = true;
                                break;
                              }
                            }
                          }
                          

                          if (go) {
                            if (TheKing.getID() == 145) {
                              Tower TheTower = (Tower)BGG.Objects(110);
                              if (TheTower.getHowOftenMoved() < 1)
                              {
                                BGG.setBackgroundGrid(BGG.getX(), BGG.getY(), 0);
                                BGG.setBackgroundGrid(Buttons[XX][YY].getXPos() - 1, Buttons[XX][YY].getYPos(), 0);
                                BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), TheKing.getID());
                                BGG.setBackgroundGrid(Buttons[XX][YY].getXPos() + 1, Buttons[XX][YY].getYPos(), TheTower.getID());
                                BGG.changeTeam();
                                TheTower.setMeepleXPos(Buttons[XX][YY].getXPos() + 1);
                                TheTower.setMeepleYPos(Buttons[XX][YY].getYPos());
                                TheKing.setMeepleXPos(Buttons[XX][YY].getXPos());
                                TheKing.setMeepleYPos(Buttons[XX][YY].getYPos());
                                renewPanel(Buttons[XX][YY].getXPos() - 1, Buttons[XX][YY].getYPos(), true);
                                renewPanel(Buttons[XX][YY].getXPos() + 1, Buttons[XX][YY].getYPos(), true);
                                renewPanel(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), true);
                                moveMeeple = true;
                              }
                            } else if (TheKing.getID() == 245) {
                              Tower TheTower = (Tower)BGG.Objects(210);
                              if (TheTower.getHowOftenMoved() < 1)
                              {
                                BGG.setBackgroundGrid(BGG.getX(), BGG.getY(), 0);
                                BGG.setBackgroundGrid(Buttons[XX][YY].getXPos() - 1, Buttons[XX][YY].getYPos(), 0);
                                BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), TheKing.getID());
                                BGG.setBackgroundGrid(Buttons[XX][YY].getXPos() + 1, Buttons[XX][YY].getYPos(), TheTower.getID());
                                BGG.changeTeam();
                                TheTower.setMeepleXPos(Buttons[XX][YY].getXPos() + 1);
                                TheTower.setMeepleYPos(Buttons[XX][YY].getYPos());
                                TheKing.setMeepleXPos(Buttons[XX][YY].getXPos());
                                TheKing.setMeepleYPos(Buttons[XX][YY].getYPos());
                                renewPanel(Buttons[XX][YY].getXPos() - 1, Buttons[XX][YY].getYPos(), true);
                                renewPanel(Buttons[XX][YY].getXPos() + 1, Buttons[XX][YY].getYPos(), true);
                                renewPanel(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), true);
                                moveMeeple = true;
                              }
                              
                            }
                            
                          }
                        }
                      }
                    }
                    else if ((BGG.getObject() instanceof Tower))
                    {
                      Tower TheTower = (Tower)BGG.getObject();
                      
                      if (TheTower.move2(BGG.getX(), BGG.getY(), Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), BGG)) {
                        BGG.setBackgroundGrid(BGG.getX(), BGG.getY(), 0);
                        BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), TheTower.getID());
                        BGG.changeTeam();
                        TheTower.setMeepleXPos(Buttons[XX][YY].getXPos());
                        TheTower.setMeepleYPos(Buttons[XX][YY].getYPos());
                        moveMeeple = true;
                      }
                    }
                    else if ((BGG.getObject() instanceof Runner))
                    {
                      Runner TheRunner = (Runner)BGG.getObject();
                      
                      if (TheRunner.move2(BGG.getX(), BGG.getY(), Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), BGG))
                      {
                        BGG.setBackgroundGrid(BGG.getX(), BGG.getY(), 0);
                        BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), TheRunner.getID());
                        BGG.changeTeam();
                        
                        TheRunner.setMeepleXPos(Buttons[XX][YY].getXPos());
                        TheRunner.setMeepleYPos(Buttons[XX][YY].getYPos());
                        moveMeeple = true;
                      }
                    }
                    else if ((BGG.getObject() instanceof Queen))
                    {
                      Queen khaleesi = (Queen)BGG.getObject();
                      
                      if (khaleesi.move2(BGG.getX(), BGG.getY(), Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), BGG)) {
                        BGG.setBackgroundGrid(BGG.getX(), BGG.getY(), 0);
                        BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), khaleesi.getID());
                        BGG.changeTeam();
                        khaleesi.setMeepleXPos(Buttons[XX][YY].getXPos());
                        khaleesi.setMeepleYPos(Buttons[XX][YY].getYPos());
                        moveMeeple = true;
                      }
                    }
                    else if ((BGG.getObject() instanceof Jumper))
                    {
                      Jumper drogo = (Jumper)BGG.getObject();
                      
                      if (drogo.move(BGG.getX(), BGG.getY(), Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), BGG.getTeam())) {
                        BGG.setBackgroundGrid(BGG.getX(), BGG.getY(), 0);
                        
                        BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), drogo.getID());
                        BGG.changeTeam();
                        drogo.setMeepleXPos(Buttons[XX][YY].getXPos());
                        drogo.setMeepleYPos(Buttons[XX][YY].getYPos());
                        moveMeeple = true;
                      }
                      
                    }
                    

                  }
                  else if ((Math.abs(BGG.getBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos()) - BGG.getBackgroundGrid(BGG.getX(), BGG.getY())) > 49) && (BGG.getMove())) {
                    if ((BGG.getObject() instanceof Farmer))
                    {
                      Farmer TheFarmer = (Farmer)BGG.getObject();
                      if (TheFarmer.strike(BGG.getX(), BGG.getY(), Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), BGG.getTeam()))
                      {
                        BGG.setBackgroundGrid(BGG.getX(), BGG.getY(), 0);
                        BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), TheFarmer.getID());
                        BGG.changeTeam();
                        TheFarmer.setMeepleXPos(Buttons[XX][YY].getXPos());
                        TheFarmer.setMeepleYPos(Buttons[XX][YY].getYPos());
                        moveMeeple = true;
                        TheFarmer.setLastMovedInRound(BGG.getTurnRound());
                        
                        SM = new SoundMachine("/musik/strike.wav", 0);
                        TH = new Thread(SM);
                        TH.start();
                        

                        if ((TheFarmer.isteam()) && (Buttons[XX][YY].getYPos() == 7))
                        {
                          Bauerntausch(true, XX, YY);
                          Bauerntausch = Boolean.valueOf(true);
                        }
                        else if ((!TheFarmer.isteam()) && (Buttons[XX][YY].getYPos() == 0)) {
                          Bauerntausch(false, XX, YY);
                          Bauerntausch = Boolean.valueOf(true);
                        }
                        
                      }
                      
                    }
                    else if ((BGG.getObject() instanceof King))
                    {
                      King TheKing = (King)BGG.getObject();
                      if (TheKing.strike(BGG.getX(), BGG.getY(), Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), BGG.getTeam())) {
                        BGG.setBackgroundGrid(BGG.getX(), BGG.getY(), 0);
                        BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), TheKing.getID());
                        BGG.changeTeam();
                        TheKing.setMeepleXPos(Buttons[XX][YY].getXPos());
                        TheKing.setMeepleYPos(Buttons[XX][YY].getYPos());
                        moveMeeple = true;
                        SM = new SoundMachine("/musik/strike.wav", 0);
                        TH = new Thread(SM);
                        TH.start();
                      }
                    }
                    else if ((BGG.getObject() instanceof Tower))
                    {
                      Tower TheTower = (Tower)BGG.getObject();
                      
                      if (TheTower.move2(BGG.getX(), BGG.getY(), Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), BGG)) {
                        BGG.setBackgroundGrid(BGG.getX(), BGG.getY(), 0);
                        BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), TheTower.getID());
                        BGG.changeTeam();
                        TheTower.setMeepleXPos(Buttons[XX][YY].getXPos());
                        TheTower.setMeepleYPos(Buttons[XX][YY].getYPos());
                        moveMeeple = true;
                        SM = new SoundMachine("/musik/strike.wav", 0);
                        TH = new Thread(SM);
                        TH.start();
                      }
                    } else if ((BGG.getObject() instanceof Runner))
                    {
                      Runner TheRunner = (Runner)BGG.getObject();
                      
                      if (TheRunner.move2(BGG.getX(), BGG.getY(), Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), BGG)) {
                        BGG.setBackgroundGrid(BGG.getX(), BGG.getY(), 0);
                        BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), TheRunner.getID());
                        BGG.changeTeam();
                        
                        TheRunner.setMeepleXPos(Buttons[XX][YY].getXPos());
                        TheRunner.setMeepleYPos(Buttons[XX][YY].getYPos());
                        moveMeeple = true;
                        SM = new SoundMachine("/musik/strike.wav", 0);
                        TH = new Thread(SM);
                        TH.start();
                      }
                    }
                    else if ((BGG.getObject() instanceof Queen))
                    {
                      Queen khaleesi = (Queen)BGG.getObject();
                      
                      if (khaleesi.move2(BGG.getX(), BGG.getY(), Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), BGG)) {
                        BGG.setBackgroundGrid(BGG.getX(), BGG.getY(), 0);
                        BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), khaleesi.getID());
                        BGG.changeTeam();
                        khaleesi.setMeepleXPos(Buttons[XX][YY].getXPos());
                        khaleesi.setMeepleYPos(Buttons[XX][YY].getYPos());
                        moveMeeple = true;
                        SM = new SoundMachine("/musik/strike.wav", 0);
                        TH = new Thread(SM);
                        TH.start();
                      }
                    } else if ((BGG.getObject() instanceof Jumper))
                    {
                      Jumper drogo = (Jumper)BGG.getObject();
                      if (drogo.move(BGG.getX(), BGG.getY(), Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), BGG.getTeam())) {
                        BGG.setBackgroundGrid(BGG.getX(), BGG.getY(), 0);
                        BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), drogo.getID());
                        BGG.changeTeam();
                        drogo.setMeepleXPos(Buttons[XX][YY].getXPos());
                        drogo.setMeepleYPos(Buttons[XX][YY].getYPos());
                        moveMeeple = true;
                        SM = new SoundMachine("/musik/strike.wav", 0);
                        TH = new Thread(SM);
                        TH.start();
                      }
                    }
                  }
                }
                catch (Exception localException) {}
                









                if ((moveMeeple) && (!Bauerntausch.booleanValue()))
                {
                  connectionWrite();
                  BGG.higherTurnRound();
                  renewPanel(XX, YY, false);
                  getSchach();
                  int Turns = BGG.getTurnRound() / 2 + 1;
                  turn1.setText("This is turn: " + Turns);
                  if (BGG.getTeam()) {
                    player2.setBackground(Color.white);
                  } else {
                    player2.setBackground(Color.black);
                  }
                  if (BGG.getIsConnectet()) {
                    con2.setBackground(Color.green);
                  } else {
                    con2.setBackground(Color.red);
                  }
                }
                
                int Back = BGG.getBackgroundGrid(XX - 1, YY - 1);
                if (BGG.getBackgroundGrid(XX - 1, YY - 1) > 0) {
                  BGG.setXY(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos());
                  BGG.setObject(BGG.Objects(Back));
                }
                if (BGG.getSchachmattWhite()) {
                  try {
                    SM1.redurceVOl();
                    SM1.redurceVOl();
                    SM1.redurceVOl();
                  }
                  catch (Exception localException1) {}
                  

                  int Turns = BGG.getTurnRound() / 2;
                  JOptionPane.showMessageDialog(ChessFrame, "Game Over; The game lastet: " + (Turns + 1) + " turns.", "Schachmatt", 1);
                }
              }
            });
          }
        }
      }
    }
    

















    public void Bauerntausch(final boolean team, final int XX, final int YY)
    {
      final JFrame ChooserF = new JFrame("which meeple?");
      Point pointer = ChessFrame.getLocation();
      int Xpos = (int)pointer.getX() + 200;
      int Ypos = (int)pointer.getY() + 200;
      ChooserF.setResizable(false);
      
      Point newPoint = new Point(Xpos, Ypos);
      
      ChooserF.setSize(200, 110);
      ChooserF.setLocation(newPoint);
      
      String[] Meeples = { "Queen", "Jumper", "Runner", "Tower" };
      final JComboBox JBox = new JComboBox(Meeples);
      


      JButton JBut = new JButton("Choose");
      
      JPanel southPanel = new JPanel();
      JPanel northPanel = new JPanel();
      
      northPanel.add(JBox);
      southPanel.add(JBut);
      
      JBut.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          int Number = 0;
          String NoOne = (String)JBox.getSelectedItem();
          int WhichOne = 0;
          



          if (NoOne == "Queen")
          {
            if (BGG.getQueenNumber() < 4)
            {
              Chooser = 1;
              BGG.setMove(true);
              
              if (team) {
                BGG.higherQueenNumber();
                Number = BGG.getQueenNumber();
                BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), Number + 140);
                Queen khaleesi = new Queen(true, Number + 140, Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos());
                BGG.Objectives.set(Number + 140, khaleesi);
              } else {
                BGG.higherQueenNumber();
                Number = BGG.getQueenNumber();
                BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), Number + 240);
                Queen khaleesi = new Queen(false, Number + 240, Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos());
                BGG.Objectives.set(Number + 240, khaleesi);
              }
            } else {
              JFrame Frame1 = new JFrame("");
              JOptionPane.showMessageDialog(Frame1, "No more Queens available! Choose another one!", "OTHER MEEPLE", 0);
              Bauerntausch(team, 0, 0);


            }
            



          }
          else if (NoOne == "Jumper")
          {
            Chooser = 2;
            
            if (team) {
              BGG.higherJumperNumber();
              Number = BGG.getJumperNumber();
              BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), Number + 121);
              Jumper drogo = new Jumper(true, Number + 121, Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos());
              BGG.Objectives.set(Number + 121, drogo);
            } else {
              BGG.higherJumperNumber();
              Number = BGG.getJumperNumber();
              BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), Number + 221);
              Jumper drogo = new Jumper(false, Number + 221, Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos());
              BGG.Objectives.set(Number + 221, drogo);
            }
            
          }
          else if (NoOne == "Runner")
          {
            Chooser = 3;
            
            if (team) {
              BGG.higherRunnerNumber();
              Number = BGG.getRunnerNumber();
              BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), Number + 131);
              Runner runni = new Runner(true, Number + 131, Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos());
              BGG.Objectives.set(Number + 131, runni);
            } else {
              BGG.higherRunnerNumber();
              Number = BGG.getRunnerNumber();
              BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), Number + 231);
              Runner runni = new Runner(false, Number + 231, Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos());
              BGG.Objectives.set(Number + 231, runni);
            }
            
          }
          else if (NoOne == "Tower")
          {
            Chooser = 4;
            

            if (team) {
              BGG.higherTowerNumber();
              Number = BGG.getTowerNumber();
              BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), Number + 111);
              Tower TheRedKeep = new Tower(true, Number + 111, Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos());
              BGG.Objectives.set(Number + 111, TheRedKeep);
            } else {
              BGG.higherTowerNumber();
              Number = BGG.getTowerNumber();
              BGG.setBackgroundGrid(Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos(), Number + 211);
              Tower TheRedKeep = new Tower(false, Number + 211, Buttons[XX][YY].getXPos(), Buttons[XX][YY].getYPos());
              BGG.Objectives.set(Number + 211, TheRedKeep);
            }
          }
          

          Bauerntausch = Boolean.valueOf(false);
          connectionWrite();
          BGG.higherTurnRound();
          renewPanel(XX, YY, false);
          getSchach();
          renewPanel(0, 0, false);
          ChooserF.setVisible(false);

        }
        


      });
      ChooserF.add(northPanel, "North");
      ChooserF.add(southPanel, "South");
      ChooserF.setUndecorated(true);
      ChooserF.setVisible(true);
    }
    






    public void connectionWrite()
    {
      try
      {
        if (BGG.getIsConnectet())
        {
          if (BGG.getMove())
          {
            BGG.setMove(false);
            
            oos.writeObject(BGG);
            

            ThreadForReading tr = new ThreadForReading(BGG, BoardGui.BG);
            Thread listen2 = new Thread(tr);
            listen2.start();
            
            BGG.setMove(false);
          }
        }
      }
      catch (Exception ex)
      {
        try
        {
          JFrame Frame1 = new JFrame("a frame");
          
          BoardGui.BG.getOIS().close();
          BoardGui.BG.getOOS().close();
          BGclientSock2.close();
          
          JOptionPane.showMessageDialog(Frame1, "The connection has been interrupted!", "CONNECTION INTERUPTION", 0);
          BGnetwork.remove(BGdisconnect);

        }
        catch (IOException e)
        {

          JFrame Frame1 = new JFrame("a frame");
          JOptionPane.showMessageDialog(Frame1, "The client connection has been interrupted!", "CONNECTION INTERUPTION", 0);
          
          BGnetwork.remove(BGdisconnect);
        }
        

        BGclientSock2 = null;
        BGG.setIsConnectet(false); if (BGG.getIsConnectet()) {
          con2.setBackground(Color.green);
        } else {
          con2.setBackground(Color.red);
        }
        if (BGG.getSchachmattWhite()) {
          BGG.setMove(false);
        } else {
          BGG.setMove(true);
        }
      }
    }
    









    public void renewPanel(int XX, int YY, boolean special)
    {
      for (int Y = 0; Y < 8; Y++) {
        for (int X = 0; X < 8; X++) {
          try {
            int Back1 = BGG.getBackgroundGrid(X, Y);
            if ((BGG.Objects(Back1) instanceof Farmer)) {
              Farmer TheFarmer2 = (Farmer)BGG.Objects(Back1);
              Buttons[X][Y].setIcon(TheFarmer2.getIcon());
            }
            else if ((BGG.Objects(Back1) instanceof King)) {
              King TheKking = (King)BGG.Objects(Back1);
              Buttons[X][Y].setIcon(TheKking.getIcon());
            }
            else if ((BGG.Objects(Back1) instanceof Tower)) {
              Tower TheTowerOfJoy = (Tower)BGG.Objects(Back1);
              Buttons[X][Y].setIcon(TheTowerOfJoy.getIcon());
            }
            else if ((BGG.Objects(Back1) instanceof Runner)) {
              Runner TheRunner = (Runner)BGG.Objects(Back1);
              Buttons[X][Y].setIcon(TheRunner.getIcon());
            }
            else if ((BGG.Objects(Back1) instanceof Queen)) {
              Queen khaleesi = (Queen)BGG.Objects(Back1);
              Buttons[X][Y].setIcon(khaleesi.getIcon());
            }
            else if ((BGG.Objects(Back1) instanceof Jumper)) {
              Jumper drogo = (Jumper)BGG.Objects(Back1);
              Buttons[X][Y].setIcon(drogo.getIcon());

















            }
            else if (BGG.getBackgroundGrid(BGG.getX(), BGG.getY()) < 1) {
              if (special) {
                Buttons[XX][YY].setIcon(null);
              }
              Buttons[BGG.getX()][BGG.getY()].setIcon(null);
              Buttons[X][Y].setIcon(null);
            } else {
              Buttons[X][Y].setIcon(null);
            }
          }
          catch (Exception localException) {}
        }
      }
    }
    





    public void getSchach()
    {
      JFrame Frame1 = new JFrame();
      BlackSchach = BGG.SchachKing(false, BGG, 50, 50, true, false);
      if (BlackSchach)
      {
        JOptionPane.showMessageDialog(Frame1, "Black is in Schach!", "Schach", 1);
      }
      
      WhiteSchach = BGG.SchachKing(true, BGG, 50, 50, true, false);
      if (WhiteSchach) {
        JOptionPane.showMessageDialog(Frame1, "White is in Schach!", "Schach", 1);
      }
    }
    





    public class ChessButton
      extends JButton
    {
      int X;
      




      int Y;
      





      public ChessButton(int X2, int Y2)
      {
        X = (X2 - 1);
        Y = (Y2 - 1);
      }
      




      public int getXPos()
      {
        return X;
      }
      



      public int getYPos()
      {
        return Y;
      }
      




      public void noIcon(int X1, int Y1)
      {
        setIcon(null);
      }
    }
  }
}
