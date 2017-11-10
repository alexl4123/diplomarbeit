package BackgroundMatrix;

import Game.MovePos;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import meeple.Farmer;
import meeple.Jumper;
import meeple.King;
import meeple.Queen;
import meeple.Runner;
import meeple.Tower;






















public class BackgroundGrid
  implements Serializable
{
  public int[][] iBackground;
  public ArrayList<Object> Objectives = new ArrayList();
  
  boolean team;
  
  private boolean SchachmattWhite;
  
  private boolean SchachmattBlack;
  
  private boolean _Draw;
  
  private Socket socketOfHost;
  
  private Socket socketOfClient;
  
  public ObjectInputStream netReadStream;
  
  public ObjectOutputStream netWriteStream;
  
  public ArrayList<MovePos> _TotalMoveList;
  
  boolean move;
  
  boolean isConnectet;
  
  short TurnRound;
  
  int QueenNumber;
  
  int TowerNumber;
  
  int JumperNumber;
  
  int RunnerNumber;
  
  String name;
  
  int _Choose;
  
  private boolean[] bPawnSpecMoved;
  
  private boolean[] bKingMoved;
  
  private boolean[] bTowerMoved;
  
  int X;
  
  int Y;
  
  Object OBJ;
  
  private int _ID;
  
  private int _iX;
  
  private int _iY;
  private int iDanger;
  public int[][] Board;
  
  public BackgroundGrid()
  {
    bPawnSpecMoved = new boolean[20];
    bKingMoved = new boolean[2];
    bTowerMoved = new boolean[4];
    QueenNumber = 0;
    TowerNumber = 0;
    JumperNumber = 0;
    RunnerNumber = 0;
    TurnRound = 0;
    move = true;
    isConnectet = false;
    team = true;
    _TotalMoveList = new ArrayList();
    iBackground = new int[8][8];
    
    for (int i = 0; i < 300; i++) {
      Objectives.add(Integer.valueOf(i));
    }
    
    for (int Y = 0; Y < 8; Y++) {
      for (int X = 0; X < 8; X++)
      {



        if ((Y >= 2) && (Y <= 5)) {
          iBackground[X][Y] = 0;
        }
        else if (Y == 1)
        {
          iBackground[X][Y] = (101 + X);
          Farmer TheFarmer = new Farmer(true, iBackground[X][Y], X, Y);
          TheFarmer.setMeepleXPos(X);
          TheFarmer.setMeepleYPos(Y);
          Objectives.set(iBackground[X][Y], TheFarmer);
        }
        else if (Y == 6)
        {
          iBackground[X][Y] = (200 + X);
          
          Farmer TheFarmer = new Farmer(false, iBackground[X][Y], X, Y);
          TheFarmer.setMeepleXPos(X);
          TheFarmer.setMeepleYPos(Y);
          Objectives.set(iBackground[X][Y], TheFarmer);
        }
      }
    }
    

    iBackground[0][0] = 110;
    Tower TheTower = new Tower(true, iBackground[0][0], 0, 0);
    TheTower.setMeepleXPos(0);
    TheTower.setMeepleYPos(0);
    Objectives.set(iBackground[0][0], TheTower);
    iBackground[7][0] = 111;
    TheTower = new Tower(true, iBackground[7][0], 7, 0);
    TheTower.setMeepleXPos(7);
    TheTower.setMeepleYPos(0);
    Objectives.set(iBackground[7][0], TheTower);
    
    iBackground[1][0] = 120;
    Jumper Drogo = new Jumper(true, iBackground[1][0], 1, 0);
    Drogo.setMeepleXPos(1);
    Drogo.setMeepleYPos(0);
    Objectives.set(iBackground[1][0], Drogo);
    
    iBackground[6][0] = 121;
    Drogo = new Jumper(true, iBackground[6][0], 6, 0);
    Drogo.setMeepleXPos(6);
    Drogo.setMeepleYPos(0);
    Objectives.set(iBackground[6][0], Drogo);
    
    iBackground[2][0] = '';
    Runner TheRunner = new Runner(true, iBackground[2][0], 2, 0);
    TheRunner.setMeepleXPos(2);
    TheRunner.setMeepleYPos(0);
    Objectives.set(iBackground[2][0], TheRunner);
    iBackground[5][0] = '';
    TheRunner = new Runner(true, iBackground[5][0], 5, 0);
    TheRunner.setMeepleXPos(5);
    TheRunner.setMeepleYPos(0);
    Objectives.set(iBackground[5][0], TheRunner);
    
    iBackground[3][0] = '';
    Queen khaleesi = new Queen(true, iBackground[3][0], 3, 0);
    khaleesi.setMeepleXPos(3);
    khaleesi.setMeepleYPos(0);
    Objectives.set(iBackground[3][0], khaleesi);
    
    iBackground[4][0] = '';
    King TheKingWhite = new King(true, 150, 4, 0);
    TheKingWhite.setMeepleXPos(4);
    TheKingWhite.setMeepleYPos(0);
    Objectives.set(150, TheKingWhite);
    


    iBackground[0][7] = 'Ò';
    TheTower = new Tower(false, iBackground[0][7], 0, 7);
    TheTower.setMeepleXPos(0);
    TheTower.setMeepleYPos(7);
    Objectives.set(iBackground[0][7], TheTower);
    iBackground[7][7] = 'Ó';
    TheTower = new Tower(false, iBackground[7][7], 7, 7);
    TheTower.setMeepleXPos(7);
    TheTower.setMeepleYPos(7);
    Objectives.set(iBackground[7][7], TheTower);
    
    iBackground[1][7] = 'Ü';
    Drogo = new Jumper(false, iBackground[1][7], 1, 7);
    Drogo.setMeepleXPos(1);
    Drogo.setMeepleYPos(7);
    Objectives.set(iBackground[1][7], Drogo);
    
    iBackground[6][7] = 'Ý';
    Drogo = new Jumper(false, iBackground[6][7], 6, 7);
    Drogo.setMeepleXPos(6);
    Drogo.setMeepleYPos(7);
    Objectives.set(iBackground[6][7], Drogo);
    
    iBackground[2][7] = 'æ';
    TheRunner = new Runner(false, iBackground[2][7], 2, 7);
    TheRunner.setMeepleXPos(2);
    TheRunner.setMeepleYPos(7);
    Objectives.set(iBackground[2][7], TheRunner);
    iBackground[5][7] = 'ç';
    TheRunner = new Runner(false, iBackground[5][7], 5, 7);
    TheRunner.setMeepleXPos(5);
    TheRunner.setMeepleYPos(7);
    Objectives.set(iBackground[5][7], TheRunner);
    
    iBackground[3][7] = 'ð';
    khaleesi = new Queen(false, iBackground[3][7], 3, 7);
    khaleesi.setMeepleXPos(3);
    khaleesi.setMeepleYPos(7);
    Objectives.set(iBackground[3][7], khaleesi);
    
    iBackground[4][7] = 'ú';
    King TheKingBlack = new King(false, iBackground[4][7], 4, 7);
    TheKingBlack.setMeepleXPos(4);
    TheKingBlack.setMeepleYPos(7);
    Objectives.set(iBackground[4][7], TheKingBlack);
    

    for (int Y = 0; Y < 8; Y++) {
      for (int X = 0; X < 8; X++) {}
    }
  }
  







  public short getTurnRound()
  {
    return TurnRound;
  }
  


  public void higherTurnRound()
  {
    TurnRound = ((short)(TurnRound + 1));
  }
  







  public Object Objects(int Back)
  {
    return Objectives.get(Back);
  }
  




  public boolean getMove()
  {
    return move;
  }
  





  public boolean getIsConnectet()
  {
    return isConnectet;
  }
  





  public void setMove(boolean temp)
  {
    move = temp;
  }
  





  public void setIsConnectet(boolean temp)
  {
    isConnectet = temp;
  }
  




  public String getName()
  {
    return name;
  }
  





  public void setName(String n)
  {
    name = n;
  }
  


  public void higherQueenNumber()
  {
    QueenNumber += 1;
  }
  




  public int getQueenNumber()
  {
    return QueenNumber;
  }
  



  public void higherJumperNumber()
  {
    JumperNumber += 1;
  }
  





  public int getJumperNumber()
  {
    return JumperNumber;
  }
  



  public void higherRunnerNumber()
  {
    RunnerNumber += 1;
  }
  





  public int getRunnerNumber()
  {
    return RunnerNumber;
  }
  



  public void higherTowerNumber()
  {
    TowerNumber += 1;
  }
  




  public int getTowerNumber()
  {
    return TowerNumber;
  }
  








  public int getBackgroundGrid(int X, int Y)
  {
    return iBackground[X][Y];
  }
  











  public void setBackgroundGrid(int X, int Y, int iBG)
  {
    iBackground[X][Y] = iBG;
  }
  












  public void setXY(int X1, int Y1)
  {
    X = X1;
    Y = Y1;
  }
  






  public void setObject(Object Ob)
  {
    OBJ = Ob;
  }
  





  public int getX()
  {
    return X;
  }
  




  public int getY()
  {
    return Y;
  }
  




  public Object getObject()
  {
    return OBJ;
  }
  




  public boolean getTeam()
  {
    return team;
  }
  



  public void changeTeam()
  {
    if (team) {
      team = false;
    } else {
      team = true;
    }
  }
  


































  public boolean SchachKing(boolean team, BackgroundGrid BGG, int KingX, int KingY, boolean SchachMatt, boolean bSimKingOnTile)
  {
    SchachmattWhite = false;
    SchachmattBlack = false;
    
    if ((KingX < 0) || (KingX >= 8) || (KingY < 0) || (KingY >= 8)) {
      return false;
    }
    


    boolean Schach = false;
    int iID; int iID; if ((KingX <= 7) && (KingY <= 7)) {
      iID = iBackground[KingX][KingY];
    } else {
      iID = 0;
    }
    
    if ((bSimKingOnTile) && (team)) {
      iID = 150;
    } else if ((bSimKingOnTile) && (!team)) {
      iID = 250;
    }
    


    switch (iID) {
    case 150: 
      Schach = Schach(iBackground, KingX, KingY, team);
      
      break;
    case 250: 
      Schach = Schach(iBackground, KingX, KingY, team);
      
      break;
    default: 
      Schach = false;
    }
    
    

    if ((!SchachMatt) && (!Schach)) {
      _Draw = CalcDraw(iID, iBackground, KingX, KingY, team, BGG);
    }
    
    if ((Schach) && (!SchachMatt))
    {
      SchachmattWhite = SchachMatt(iID, iBackground, KingX, KingY, team, BGG);
      if (SchachmattWhite)
      {
        if (team)
        {
          Platform.runLater(new Runnable()
          {
            public void run()
            {
              try {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Check Mate");
                alert.setHeaderText("White has lost the game, black takes it all!");
                alert.showAndWait();
              }
              catch (Exception ex) {
                ex.printStackTrace();
              }
              
            }
            
          });
          SchachmattBlack = false;
          SchachmattWhite = true;
        }
        else {
          SchachmattBlack = true;
          SchachmattWhite = false;
          
          Platform.runLater(new Runnable()
          {
            public void run()
            {
              try {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Check Mate");
                alert.setHeaderText("Black has lost the game, white takes it all!");
                alert.showAndWait();
              }
              catch (Exception ex) {
                ex.printStackTrace();
              }
            }
          });
        }
      }
    }
    







    return Schach;
  }
  









  private boolean Schach(int[][] iBackground, int KingX, int KingY, boolean Team)
  {
    for (int Y = 0; Y < 8; Y++) {
      for (int X = 0; X < 8; X++)
      {
        int iBack = iBackground[X][Y];
        
        if (((iBack >= 200) && (iBack < 210) && (Team)) || ((iBack >= 100) && (iBack < 110) && (!Team))) {
          if ((KingY == Y - 1) && (Team)) {
            if ((KingX == X - 1) || (KingX == X + 1)) {
              _ID = iBack;_iX = X;_iY = Y;
              return true;
            }
          } else if ((KingY == Y + 1) && (!Team) && (
            (KingX == X - 1) || (KingX == X + 1))) {
            _ID = iBack;_iX = X;_iY = Y;
            return true;
          }
        }
        else if (((iBack >= 210) && (iBack < 220) && (Team)) || ((iBack >= 110) && (iBack < 120) && (!Team)))
        {

          if (KingY == Y) {
            int dX = Math.abs(KingX - X);
            int SumOfField = 0;
            if (dX == 1) {
              _ID = iBack;_iX = X;_iY = Y;
              return true;
            }
            for (int x = 1; x < dX; x++) {
              if ((KingX < x) && (X - x >= 0)) {
                SumOfField += iBackground[(X - x)][Y];
              } else if ((KingX > x) && (X + x < 8)) {
                SumOfField += iBackground[(x + X)][Y];
              }
            }
            

            if (SumOfField == 0) {
              _ID = iBack;_iX = X;_iY = Y;
              return true;
            }
          }
          else if (KingX == X) {
            int dY = Math.abs(KingY - Y);
            int SumOfField = 0;
            
            if (dY == 1) {
              _ID = iBack;_iX = X;_iY = Y;
              return true;
            }
            for (int y = 1; y < dY; y++) {
              if ((KingY < Y) && (Y - y >= 0)) {
                SumOfField += iBackground[X][(Y - y)];
              } else if ((KingY > Y) && (Y + y < 8)) {
                SumOfField += iBackground[X][(Y + y)];
              }
            }
            

            if (SumOfField == 0) {
              _ID = iBack;_iX = X;_iY = Y;
              return true;
            }
          }
        }
        else if (((iBack >= 220) && (iBack < 230) && (Team)) || ((iBack >= 120) && (iBack < 130) && (!Team))) {
          if (((KingX == X - 2) || (KingX == X + 2)) && ((KingY == Y - 1) || (KingY == Y + 1))) {
            _ID = iBack;_iX = X;_iY = Y;
            return true; }
          if (((KingX == X - 1) || (KingX == X + 1)) && ((KingY == Y - 2) || (KingY == Y + 2))) {
            _ID = iBack;_iX = X;_iY = Y;
            return true;
          }
        }
        else if (((iBack >= 230) && (iBack < 240) && (Team)) || ((iBack >= 130) && (iBack < 140) && (!Team)))
        {

          float dX = Math.abs(X - KingX);
          float dY = Math.abs(Y - KingY);
          float dD = dY / dX;
          int SumOfField = 0;
          
          if (dD == 1.0F) {
            if (dX == 1.0F) {
              _ID = iBack;_iX = X;_iY = Y;
              return true;
            }
            
            for (int iHelp = 1; iHelp < dX; iHelp++)
            {
              if ((X < KingX) && (Y > KingY) && (X + iHelp < 8) && (Y - iHelp >= 0)) {
                SumOfField += iBackground[(X + iHelp)][(Y - iHelp)];
              } else if ((X < KingX) && (Y < KingY) && (X + iHelp < 8) && (Y + iHelp < 8)) {
                SumOfField += iBackground[(X + iHelp)][(Y + iHelp)];
              } else if ((X > KingX) && (Y > KingY) && (X - iHelp >= 0) && (Y - iHelp >= 0)) {
                SumOfField += iBackground[(X - iHelp)][(Y - iHelp)];
              } else if ((X > KingX) && (Y < KingY) && (X - iHelp >= 0) && (Y + iHelp < 8)) {
                SumOfField += iBackground[(X - iHelp)][(Y + iHelp)];
              }
            }
            

            if (SumOfField == 0) {
              _ID = iBack;_iX = X;_iY = Y;
              if (iBack == 231) {
                System.out.println("231-KingX:" + KingX + ":KingY:" + KingY + ":");
              }
              return true;
            }
          }
        } else if (((iBack >= 240) && (iBack < 250) && (Team)) || ((iBack >= 140) && (iBack < 150) && (!Team)))
        {

          float dX = Math.abs(X - KingX);
          float dY = Math.abs(Y - KingY);
          float dD = dY / dX;
          int SumOfField = 0;
          
          if (dD == Float.POSITIVE_INFINITY)
          {
            dD = 2.0F;
          }
          
          if (dD == 1.0F)
          {
            if (dX == 1.0F) {
              _ID = iBack;_iX = X;_iY = Y;
              return true;
            }
            for (int iHelp = 1; iHelp < dX; iHelp++)
            {
              if ((X < KingX) && (Y > KingY) && (X + iHelp < 8) && (Y - iHelp >= 0)) {
                SumOfField += iBackground[(X + iHelp)][(Y - iHelp)];
              } else if ((X < KingX) && (Y < KingY) && (X + iHelp < 8) && (Y + iHelp < 8)) {
                SumOfField += iBackground[(X + iHelp)][(Y + iHelp)];
              } else if ((X > KingX) && (Y > KingY) && (X - iHelp >= 0) && (Y - iHelp >= 0)) {
                SumOfField += iBackground[(X - iHelp)][(Y - iHelp)];
              } else if ((X > KingX) && (Y < KingY) && (X - iHelp >= 0) && (Y + iHelp < 8)) {
                SumOfField += iBackground[(X - iHelp)][(Y + iHelp)];
              }
            }
            

            if (SumOfField == 0) {
              _ID = iBack;_iX = X;_iY = Y;
              return true;
            }
          } else if (KingY == Y) {
            dX = Math.abs(KingX - X);
            SumOfField = 0;
            
            if (dX == 1.0F) {
              _ID = iBack;_iX = X;_iY = Y;
              
              return true;
            }
            for (int x = 1; x < dX; x++) {
              if ((KingX < X) && (X - x >= 0)) {
                SumOfField += iBackground[(X - x)][Y];
              }
              else if ((KingX > X) && (x + X < 8)) {
                SumOfField += iBackground[(x + X)][Y];
              }
            }
            


            if (SumOfField == 0) {
              _ID = iBack;_iX = X;_iY = Y;
              return true;
            }
          }
          else if (KingX == X) {
            dY = Math.abs(KingY - Y);
            SumOfField = 0;
            
            if (dY == 1.0F) {
              _ID = iBack;_iX = X;_iY = Y;
              return true;
            }
            for (int y = 1; y < dY; y++) {
              if ((KingY < Y) && (Y - y >= 0)) {
                SumOfField += iBackground[X][(Y - y)];
              } else if ((KingY > Y) && (Y + y < 8)) {
                SumOfField += iBackground[X][(Y + y)];
              }
            }
            


            if (SumOfField == 0) {
              _ID = iBack;_iX = X;_iY = Y;
              return true;
            }
          }
        }
        else if (((iBack == 250) && (Team)) || ((iBack == 150) && (!Team))) {
          int dX = Math.abs(KingX - X);
          int dY = Math.abs(KingY - Y);
          
          if ((dX < 2) && (dY < 2)) {
            _ID = iBack;_iX = X;_iY = Y;
            return true;
          }
        }
      }
    }
    

    return false;
  }
  












  private boolean SchachMatt(int iID, int[][] iBackground, int KingX, int KingY, boolean Team, BackgroundGrid BGG)
  {
    for (int Y = -1; Y <= 1; Y++) {
      for (int X = -1; X <= 1; X++) {
        if ((KingX + X >= 0) && (KingX + X < 8) && (KingY + Y >= 0) && (KingY + Y < 8)) {
          BackgroundGrid BGG2 = new BackgroundGrid();
          iBackground = iBackground;
          int dBack = Math.abs(iBackground[(KingX + X)][(KingY + Y)] - iID);
          int iDBackup = iBackground[(KingX + X)][(KingY + Y)];
          iBackground[KingX][KingY] = 0;
          iBackground[(KingX + X)][(KingY + Y)] = iID;
          if ((!BGG2.SchachKing(Team, BGG, KingX + X, KingY + Y, true, false)) && (dBack > 50)) {
            iBackground[KingX][KingY] = iID;
            iBackground[(KingX + X)][(KingY + Y)] = iDBackup;
            return false;
          }
          iBackground[KingX][KingY] = iID;
          iBackground[(KingX + X)][(KingY + Y)] = iDBackup;
        }
      }
    }
    


    Move Moves = new Move();
    Moves.setBGG(iBackground);
    Moves.setBGG2(BGG);
    
    Moves.setBSelect(false);
    



    if (Schach(iBackground, _iX, _iY, !Team)) {
      return false;
    }
    int iBB = iBackground[_iX][_iY];
    ArrayList<MovePos> AttackMoves = Moves.getMoveMeeple(iBackground, !Team, iBB, _iX, _iY);
    int iYA; for (Iterator localIterator1 = AttackMoves.iterator(); localIterator1.hasNext(); 
        
        iYA < 8)
    {
      MovePos MP = (MovePos)localIterator1.next();
      System.out.println(AttackMoves.size() + "::Attackmoves::" + iBB + ":MPA.ID:" + ID);
      iYA = 0; continue;
      for (int iXA = 0; iXA < 8; iXA++) {
        int iBack = iBackground[iXA][iYA];
        ArrayList<MovePos> DefenseMoves = Moves.getMoveMeeple(iBackground, Team, iBack, iXA, iYA);
        
        for (MovePos MPA : DefenseMoves) {
          if ((PX != 6) || 
          

            (iBack == 131)) {
            System.out.println(DefenseMoves.size() + "::DefenseMoves::" + iBack + ":Foe:" + ID + ":Foe X:" + PX + ":Foe Y:" + PY);
          }
          

          if ((PX == PX) && (PY == PY) && (ID != 150) && (ID != 250)) {
            System.out.println(PX + "::" + PX + "::" + PY + "::" + PY + "::" + ID);
            iBackground[PX][PY] = ID;
            iBackground[X][Y] = ID2;
            if (!SchachKing(Team, BGG, KingX, KingY, true, false))
            {
              iBackground[PX][PY] = ID2;
              iBackground[X][Y] = ID;
              return false;
            }
            iBackground[PX][PY] = ID2;
            iBackground[X][Y] = ID;
          }
        }
      }
      iYA++;
    }
    






























    for (int iHelp = 0; iHelp < Moves.getMoveList().size(); iHelp++) {
      int[] IDAR = (int[])Moves.getMoveList().get(iHelp);
      int IDA = IDAR[0];
      int iYA;
      int iYA; if (IDA < 8)
      {
        iYA = 0; } else { int iYA;
        if ((IDA >= 8) && (IDA < 16)) {
          iYA = 1; } else { int iYA;
          if ((IDA >= 16) && (IDA < 24)) {
            iYA = 2; } else { int iYA;
            if ((IDA >= 24) && (IDA < 32)) {
              iYA = 3; } else { int iYA;
              if ((IDA >= 32) && (IDA < 40)) {
                iYA = 4; } else { int iYA;
                if ((IDA >= 40) && (IDA < 48)) {
                  iYA = 5; } else { int iYA;
                  if ((IDA >= 48) && (IDA < 56)) {
                    iYA = 6;
                  } else
                    iYA = 7;
                }
              } } } } }
      int iXA = IDA - iYA * 8;
      


      if (Schach(iBackground, _iX, _iY, !Team)) {
        return false;
      }
    }
    



    return true;
  }
  
















  private boolean CalcDraw(int iID, int[][] iBackground, int KingX, int KingY, boolean team, BackgroundGrid BGG)
  {
    boolean Draw = false;
    int iSum1 = 0;
    int iSum2 = 0;
    for (int Y = 0; Y < 8; Y++) {
      for (int X = 0; X < 8; X++) {
        if (((iBackground[X][Y] >= 100) && (iBackground[X][Y] < 160) && (team)) || ((iBackground[X][Y] >= 200) && (iBackground[X][Y] < 260) && (!team))) {
          iSum1 += iBackground[X][Y];
        }
        
        if (((iBackground[X][Y] >= 100) && (iBackground[X][Y] < 160) && (!team)) || ((iBackground[X][Y] >= 200) && (iBackground[X][Y] < 260) && (team))) {
          iSum2 += iBackground[X][Y];
        }
      }
    }
    


    if (((iSum1 == 150) && (team)) || ((iSum1 == 250) && (!team))) {
      Move M = new Move();
      M.setBGG(iBackground);
      M.setBGG2(BGG);
      M.GetMove(iID, KingX, KingY, BGG);
      if (M.getMoveList().size() == 0) {
        Draw = true;
        Platform.runLater(new Runnable()
        {
          public void run()
          {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Draw");
            alert.setContentText("A stalemate has caused a draw, no one won!");
            alert.setTitle("Draw");
            alert.showAndWait();
          }
        });
      }
    } else if (((iSum1 >= 270) && (iSum1 < 290) && (iSum2 == 250) && (team)) || ((iSum1 >= 470) && (iSum1 < 490) && (iSum2 == 150) && (!team))) {
      Platform.runLater(new Runnable()
      {
        public void run()
        {
          Alert alert = new Alert(Alert.AlertType.INFORMATION);
          alert.setHeaderText("Draw");
          alert.setContentText("The game cannot be continued due to the impossible of check mate.");
          alert.setTitle("Draw");
          alert.showAndWait();
        }
      });
    }
    
    MovePos MP;
    if (_TotalMoveList.size() >= 50) {
      System.out.println(">= 50");
      int iCounter = 0;
      for (int iHelp = 49; iHelp >= 0; iHelp--) {
        MP = (MovePos)_TotalMoveList.get(_TotalMoveList.size() - iHelp);
        
        if (((ID >= 100) && (ID < 110)) || ((ID >= 200) && (ID < 210)) || (ID2 != 0) || (ID4 != 0)) {
          iCounter = 0;
          break;
        }
        iCounter++;
      }
      

      if (iCounter >= 50) {
        Draw = true;
      }
    }
    
    if (_TotalMoveList.size() >= 6) {
      int icount = 0;
      Iterator localIterator; for (MP = _TotalMoveList.iterator(); MP.hasNext(); 
          
          localIterator.hasNext())
      {
        MovePos MP1 = (MovePos)MP.next();
        icount = 0;
        localIterator = _TotalMoveList.iterator(); continue;MovePos MP2 = (MovePos)localIterator.next();
        if (Arrays.deepEquals(Board, Board)) {
          System.out.println(ID + "::" + ID);
          icount++;
          if (icount > 3) {
            return true;
          }
        }
      }
    }
    



    return Draw;
  }
  




  public void setSchachmattWhite(boolean Schachmatt)
  {
    SchachmattWhite = Schachmatt;
  }
  



  public boolean getSchachmattWhite()
  {
    return SchachmattWhite;
  }
  



  public void setSchachmattBlack(boolean Schachmatt)
  {
    SchachmattBlack = Schachmatt;
  }
  


  public boolean getSchachmattBlack()
  {
    return SchachmattBlack;
  }
  



  public void setTeam(boolean team2)
  {
    team = team2;
  }
  





  public void setChoose(int i)
  {
    _Choose = i;
  }
  




  public int getChoose()
  {
    return _Choose;
  }
  



  public void setbKingMoved(int iID, boolean Moved)
  {
    switch (iID) {
    case 150:  bKingMoved[0] = Moved; break;
    case 250:  bKingMoved[1] = Moved;
    }
    
  }
  



  public boolean getbKingMoved(int iID)
  {
    switch (iID) {
    case 150:  return bKingMoved[0];
    case 250:  return bKingMoved[1];
    }
    return true;
  }
  



  public void setbRookMoved(int iID, boolean Moved)
  {
    switch (iID) {
    case 110:  bTowerMoved[0] = Moved; break;
    case 111:  bTowerMoved[1] = Moved; break;
    case 210:  bTowerMoved[2] = Moved; break;
    case 211:  bTowerMoved[3] = Moved;
    }
    
  }
  



  public boolean getbRookMoved(int iID)
  {
    switch (iID) {
    case 110:  return bTowerMoved[0];
    case 111:  return bTowerMoved[1];
    case 210:  return bTowerMoved[2];
    case 211:  return bTowerMoved[3];
    }
    
    return true;
  }
  


  public void lowerQueenNumber()
  {
    QueenNumber -= 1;
  }
  



  public boolean getSchachMattWhite()
  {
    return SchachmattWhite;
  }
  



  public boolean getSchachMattBlack()
  {
    return SchachmattBlack;
  }
  



  public boolean getDraw()
  {
    return _Draw;
  }
  



  public void setDraw(boolean Draw)
  {
    _Draw = Draw;
  }
  



  public void addMoveListItem(MovePos MP)
  {
    _TotalMoveList.add(MP);
  }
  


  public void removeLastMoveListItem()
  {
    if (_TotalMoveList.size() - 1 > 0) {
      _TotalMoveList.remove(_TotalMoveList.size() - 1);
    }
  }
  





  public MovePos getMovelistItem(int i)
  {
    if ((i >= 0) && (i < _TotalMoveList.size())) {
      return (MovePos)_TotalMoveList.get(i);
    }
    
    return null;
  }
  

  public Socket getSocketOfHost()
  {
    return socketOfHost;
  }
  
  public void setSocketOfHost(Socket socketOfHost) {
    this.socketOfHost = socketOfHost;
  }
  
  public Socket getSocketOfClient() {
    return socketOfClient;
  }
  
  public void setSocketOfClient(Socket socketOfClient) {
    this.socketOfClient = socketOfClient;
  }
  





  public void creatNetworkStreams(Socket tempSock)
    throws IOException
  {
    netReadStream = new ObjectInputStream(tempSock.getInputStream());
    netWriteStream = new ObjectOutputStream(tempSock.getOutputStream());
  }
}
