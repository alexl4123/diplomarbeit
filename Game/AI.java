package Game;

import BackgroundMatrix.BackgroundGrid;
import Gui.BoardGui;
import java.io.PrintStream;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javax.swing.JFrame;






























public class AI
  extends Thread
{
  public static BackgroundGrid _BGG2;
  public static BoardGui _Gui;
  private static boolean _Blackschach;
  private static boolean _Whiteschach;
  boolean bStart = false;
  



  private ArrayList<int[]> LastMoveList = new ArrayList();
  






  private static boolean bRunning = false;
  




  public AI(BackgroundGrid BGG2, BoardGui Gui)
  {
    _BGG2 = BGG2;
    _Gui = Gui;
  }
  





  public void run()
  {
    if (bRunning)
      return;
    bRunning = true;
    bStart = true;
    while (bRunning) {
      if (bStart) {
        bStart = false;
        
        AILogic AIL = new AILogic();
        BackgroundGrid BGGX = _BGG2;
        

        float fx = AIL.alphaBeta(5, _BGG2, _BGG2.getTeam());
        


        if (fx > -5000.0F) {
          int i = BestMove.size();
          

          MovePos A = (MovePos)BestMove.get(i - 1);
          _BGG2iBackground[PX][PY] = ID;
          _BGG2iBackground[X][Y] = 0;
          if (ID3 > 0) {
            _BGG2iBackground[X3][Y3] = 0;
          }
          
          if (ID4 > 0) {
            _BGG2iBackground[X4][Y4] = 0;
            if (X3 > 0) {
              _BGG2iBackground[X3][Y3] = 0;
            }
            _BGG2iBackground[X5][Y5] = ID4;
            _BGG2.setbRookMoved(ID, true);
            _BGG2.setbKingMoved(ID, true);
            int[] iRoch1 = new int[1];
            int[] iRoch2 = new int[1];
            iRoch1[0] = (X5 + 8 * Y5);
            iRoch2[0] = (X4 + 8 * Y4);
            
            LastMoveList.add(iRoch1);
            LastMoveList.add(iRoch2);
          }
          
          if ((ID >= 100) && (ID < 110) && (_BGG2.getTeam()) && (PY == 7)) {
            _BGG2.higherQueenNumber();
            _BGG2iBackground[PX][PY] = (140 + _BGG2.getQueenNumber());
          } else if ((ID >= 200) && (ID < 210) && (!_BGG2.getTeam()) && (PY == 0)) {
            _BGG2.higherQueenNumber();
            _BGG2iBackground[PX][PY] = (240 + _BGG2.getQueenNumber());
          }
          


          _BGG2.higherTurnRound();
          


          int[] LML = new int[8];
          LML[0] = (X + Y * 8);
          
          LastMoveList.add(LML);
          int[] LML1 = new int[8];
          LML1[0] = (PX + PY * 8);
          
          LastMoveList.add(LML1);
          LastMoveList.add(LML);
        }
        else
        {
          _BGG2.setSchachmattBlack(true);
        }
      }
      





      bRunning = false;
    }
    _Gui.setThinking(false);
    _Gui.redraw();
    Platform.runLater(new Runnable()
    {
      public void run()
      {
        getSchach();
      }
      

    });
    bRunning = false;
  }
  





  public void getSchach()
  {
    JFrame Frame1 = new JFrame();
    

    int XKing2 = 10;
    int XKing1 = 10;
    int YKing1 = 10;
    int YKing2 = 10;
    for (int Y = 0; Y < 8; Y++) {
      for (int X = 0; X < 8; X++) {
        if (_BGG2iBackground[X][Y] == 150) {
          XKing1 = X;
          YKing1 = Y;
        }
        if (_BGG2iBackground[X][Y] == 250) {
          XKing2 = X;
          YKing2 = Y;
        }
      }
    }
    

    _Blackschach = _BGG2.SchachKing(false, _BGG2, XKing2, YKing2, false, false);
    if ((_Blackschach) && (!_BGG2.getSchachmattBlack())) {
      System.out.println("BlackCheck");
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("check");
      alert.setHeaderText("Blackking is in check!");
      alert.setContentText("Blackking is in check!");
      alert.showAndWait();
    }
    _Whiteschach = _BGG2.SchachKing(true, _BGG2, XKing1, YKing1, false, false);
    if ((_Whiteschach) && (!_BGG2.getSchachmattWhite())) {
      System.out.println("WhiteCHeck");
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("check");
      alert.setHeaderText("Whiteking is in check!");
      alert.setContentText("Whiteking is in check!");
      alert.showAndWait();
    }
  }
  






  public BackgroundGrid getBGG()
  {
    return _BGG2;
  }
  



  public ArrayList<int[]> getLMoveList()
  {
    return LastMoveList;
  }
}
