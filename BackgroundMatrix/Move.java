package BackgroundMatrix;

import Game.AI;
import Game.MovePos;
import Gui.BoardGui;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import meeple.Jumper;
import meeple.King;
import meeple.Queen;
import meeple.Runner;
import meeple.Tower;

































public class Move
{
  private int[][] _BGG;
  private int[][] TheMove;
  private boolean _bSelect;
  private boolean _Blackschach;
  private boolean _Whiteschach;
  private boolean _Bauerntausch;
  private boolean _Moved;
  private int _iSelect;
  private int _iPosX;
  private int _iPosY;
  private BackgroundGrid _BGG2;
  private ArrayList<int[]> _MoveList = new ArrayList();
  



  private ArrayList<int[]> _HitList = new ArrayList();
  



  private ArrayList<int[]> _LastMoveList = new ArrayList();
  



  private ArrayList<MovePos> _LastMove = new ArrayList();
  



  private BoardGui BG;
  



  public Move()
  {
    _bSelect = false;
  }
  















  public int[][] GetMove(int iPos, int iPosX, int iPosY, BackgroundGrid BGG2)
  {
    _BGG2 = BGG2;
    TheMove = _BGG2.iBackground;
    _Moved = false;
    int Check = Math.abs(iPos - _iSelect);
    double[][] BoardRep = new double[8][8];
    _MoveList.clear();
    _HitList.clear();
    if ((_bSelect) && (Check >= 50)) {
      ArrayList<MovePos> MoveList = getMoveMeeple(_BGG, _BGG2.getTeam(), _iSelect, _iPosX, _iPosY);
      
      for (MovePos MP : MoveList) {
        if ((iPosX == PX) && (iPosY == PY) && (AllowedMove(MP)))
        {
          _BGG2.Board = _BGG2.iBackground;
          



          _LastMove.clear();
          TheMove[X][Y] = 0;
          TheMove[PX][PY] = ID;
          
          if (ID3 > 0) {
            TheMove[X3][Y3] = 0;
          }
          
          if (ID4 > 0) {
            TheMove[X4][Y4] = 0;
            if (X3 > 0) {
              TheMove[X3][Y3] = 0;
            }
            TheMove[X5][Y5] = ID4;
            _BGG2.setbRookMoved(ID4, true);
            int[] iRoch1 = new int[1];
            int[] iRoch2 = new int[1];
            iRoch1[0] = (X5 + 8 * Y5);
            iRoch2[0] = (X4 + 8 * Y4);
            
            _LastMoveList.add(iRoch1);
            _LastMoveList.add(iRoch2);
          }
          
          if ((ID >= 100) && (ID < 110) && (_BGG2.getTeam()) && (PY == 7)) {
            Bauerntausch(_BGG2.getTeam(), PX, PY);
            _Bauerntausch = true;
          } else if ((ID >= 200) && (ID < 210) && (!_BGG2.getTeam()) && (PY == 0)) {
            Bauerntausch(_BGG2.getTeam(), PX, PY);
            _Bauerntausch = true;
          }
          
          _Moved = true;
          _bSelect = false;
          _LastMove.add(MP);
          _BGG2.setbKingMoved(ID, true);
          _BGG2.setbRookMoved(ID, true);
          int[] iH = new int[1];
          iH[0] = (PX + 8 * PY);
          _LastMoveList.add(iH);
          int[] iHH = new int[1];
          iHH[0] = (X + 8 * Y);
          _LastMoveList.add(iHH);
          
          for (int Y = 0; Y < 8; Y++) {
            for (int X = 0; X < 8; X++) {
              BoardRep[X][Y] = _BGG2.iBackground[X][Y];
            }
          }
          
          Board = BoardRep;
          _TotalMoveList.add(MP);
        }
      }
      








      MoveList.clear();
      
      _HitList.clear();







    }
    else if (iPos > 99) {
      _LastMoveList.clear();
      _iSelect = iPos;
      _iPosX = iPosX;
      _iPosY = iPosY;
      _bSelect = true;
      
      ArrayList<MovePos> MoveList = getMoveMeeple(_BGG, _BGG2.getTeam(), iPos, iPosX, iPosY);
      
      for (MovePos MP : MoveList) {
        if (ID2 == 0) {
          if (AllowedMove(MP)) {
            int[] Hit = new int[5];
            int H = PX + PY * 8;
            Hit[0] = H;
            if (ID3 > 0) {
              _HitList.add(Hit);
            } else {
              _MoveList.add(Hit);
            }
            
          }
        }
        else if (AllowedMove(MP)) {
          int[] Hit = new int[5];
          int H = PX + PY * 8;
          Hit[0] = H;
          _HitList.add(Hit);
        }
      }
      



      if (MoveList.size() == 0) {
        _bSelect = false;
      }
    }
    else {
      _bSelect = false;
    }
    


    if (_Moved)
    {

      getSchach2();
      BGG2.changeTeam();
      BGG2.higherTurnRound();
      _Moved = false;
    }
    return TheMove;
  }
  

  public boolean AllowedMove(MovePos MP)
  {
    TheMove[X][Y] = 0;
    TheMove[PX][PY] = ID;
    if (ID3 > 0) {
      TheMove[X3][Y3] = 0;
    }
    
    if (ID4 > 0) {
      TheMove[X4][Y4] = 0;
      if (X3 > 0) {
        TheMove[X3][Y3] = 0;
      }
      TheMove[X5][Y5] = ID4;
    }
    

    int KingX = 10;
    int KingY = 10;
    for (int Y = 0; Y < 8; Y++) {
      for (int X = 0; X < 8; X++) {
        if ((_BGG2.iBackground[X][Y] == 150) && (_BGG2.getTeam())) {
          KingX = X;
          KingY = Y;
        }
        if ((_BGG2.iBackground[X][Y] == 250) && (!_BGG2.getTeam())) {
          KingX = X;
          KingY = Y;
        }
      }
    }
    
    boolean allowed = !_BGG2.SchachKing(_BGG2.getTeam(), _BGG2, KingX, KingY, true, false);
    
    TheMove[X][Y] = ID;
    TheMove[PX][PY] = ID2;
    if (ID3 > 0) {
      TheMove[X3][Y3] = ID3;
    }
    
    if (ID4 > 0) {
      TheMove[X4][Y4] = ID4;
      if (X3 > 0) {
        TheMove[X3][Y3] = ID3;
      }
      TheMove[X5][Y5] = ID5;
    }
    
    return allowed;
  }
  
















  public ArrayList<MovePos> getMoveMeeple(int[][] BGG, boolean team, int iID, int iX, int iY)
  {
    ArrayList<MovePos> MP = new ArrayList();
    if (((iID >= 100) && (iID < 110) && (team)) || ((iID >= 200) && (iID < 210) && (!team))) {
      if (((BGG[iX][(iY + 1)] == 0) && (team)) || ((BGG[iX][(iY - 1)] == 0) && (!team))) {
        MovePos MPN = new MovePos();
        ID = iID;
        ID2 = 0;
        PX = iX;
        if (team) {
          PY = (iY + 1);
        } else {
          PY = (iY - 1);
        }
        X = iX;
        Y = iY;
        MP.add(MPN);
      }
      if (((iY == 1) && (BGG[iX][(iY + 2)] == 0) && (BGG[iX][(iY + 1)] == 0) && (team)) || (
        (iY == 6) && (BGG[iX][(iY - 2)] == 0) && (BGG[iX][(iY - 1)] == 0) && (!team))) {
        MovePos MPN = new MovePos();
        ID = iID;
        ID2 = 0;
        PX = iX;
        if (team) {
          PY = (iY + 2);
        } else {
          PY = (iY - 2);
        }
        X = iX;
        Y = iY;
        MP.add(MPN);
      }
      
      if (team) {
        int dBGrid = 0;
        int dBGrid2 = 0;
        if ((iX - 1 >= 0) && (iY + 1 <= 7)) {
          dBGrid = Math.abs(BGG[iX][iY] - BGG[(iX - 1)][(iY + 1)]);
        }
        if ((iX + 1 <= 7) && (iY + 1 <= 7)) {
          dBGrid2 = Math.abs(BGG[iX][iY] - BGG[(iX + 1)][(iY + 1)]);
        }
        
        if ((dBGrid > 50) && (BGG[(iX - 1)][(iY + 1)] > 160)) {
          MovePos MPN = new MovePos();
          ID = iID;
          ID2 = BGG[(iX - 1)][(iY + 1)];
          PX = (iX - 1);
          PY = (iY + 1);
          X = iX;
          Y = iY;
          MP.add(MPN);
        }
        if ((dBGrid2 > 50) && (BGG[(iX + 1)][(iY + 1)] > 160)) {
          MovePos MPN = new MovePos();
          ID = iID;
          ID2 = BGG[(iX + 1)][(iY + 1)];
          PX = (iX + 1);
          PY = (iY + 1);
          X = iX;
          Y = iY;
          MP.add(MPN);
        }
      } else {
        int dBGrid = 0;
        int dBGrid2 = 0;
        if ((iX - 1 >= 0) && (iY - 1 >= 0))
        {
          dBGrid = Math.abs(BGG[iX][iY] - BGG[(iX - 1)][(iY - 1)]);
        }
        
        if ((iX + 1 <= 7) && (iY - 1 >= 0)) {
          dBGrid2 = Math.abs(BGG[iX][iY] - BGG[(iX + 1)][(iY - 1)]);
        }
        if ((dBGrid > 50) && (BGG[(iX - 1)][(iY - 1)] > 90) && (BGG[(iX - 1)][(iY - 1)] < 200))
        {
          MovePos MPN = new MovePos();
          ID = iID;
          ID2 = BGG[(iX - 1)][(iY - 1)];
          PX = (iX - 1);
          PY = (iY - 1);
          X = iX;
          Y = iY;
          MP.add(MPN);
        }
        if ((dBGrid2 > 50) && (BGG[(iX + 1)][(iY - 1)] > 90) && (BGG[(iX + 1)][(iY - 1)] < 200)) {
          MovePos MPN = new MovePos();
          ID = iID;
          ID2 = BGG[(iX + 1)][(iY - 1)];
          PX = (iX + 1);
          PY = (iY - 1);
          X = iX;
          Y = iY;
          MP.add(MPN);
        }
      }
      if (_LastMove.size() > 0) {
        int iIDLM = _LastMove.get(0)).ID;
        int iDifM = Math.abs(_LastMove.get(0)).PY - _LastMove.get(0)).Y);
        if (iX + 1 <= 7) {
          int iX1 = BGG[(iX + 1)][iY];
          if ((iX1 >= 200) && (iX1 < 210) && (team)) {
            if ((iIDLM == iX1) && (iDifM == 2)) {
              MovePos MPN = new MovePos();
              ID = iID;
              ID2 = BGG[(iX + 1)][(iY + 1)];
              ID3 = BGG[(iX + 1)][iY];
              PX = (iX + 1);
              PY = (iY + 1);
              X = iX;
              Y = iY;
              X3 = (iX + 1);
              Y3 = iY;
              MP.add(MPN);
            }
          } else if ((iX1 >= 100) && (iX1 < 110) && (!team) && 
            (iIDLM == iX1) && (iDifM == 2)) {
            MovePos MPN = new MovePos();
            ID = iID;
            ID2 = BGG[(iX + 1)][(iY - 1)];
            ID3 = BGG[(iX + 1)][iY];
            PX = (iX + 1);
            PY = (iY - 1);
            X = iX;
            Y = iY;
            X3 = (iX + 1);
            Y3 = iY;
            MP.add(MPN);
          }
        }
        
        if (iX - 1 >= 0) {
          int iX1 = BGG[(iX - 1)][iY];
          if ((iX1 >= 200) && (iX1 < 210) && (team)) {
            if ((iIDLM == iX1) && (iDifM == 2)) {
              MovePos MPN = new MovePos();
              ID = iID;
              ID2 = BGG[(iX - 1)][(iY + 1)];
              ID3 = BGG[(iX - 1)][iY];
              PX = (iX - 1);
              PY = (iY + 1);
              X = iX;
              Y = iY;
              X3 = (iX - 1);
              Y3 = iY;
              MP.add(MPN);
            }
          } else if ((iX1 >= 100) && (iX1 < 110) && (!team) && 
            (iIDLM == iX1) && (iDifM == 2)) {
            MovePos MPN = new MovePos();
            ID = iID;
            ID2 = BGG[(iX - 1)][(iY - 1)];
            ID3 = BGG[(iX - 1)][iY];
            PX = (iX - 1);
            PY = (iY - 1);
            X = iX;
            Y = iY;
            X3 = (iX - 1);
            Y3 = iY;
            MP.add(MPN);
          }
          
        }
      }
    }
    else if (((iID >= 110) && (iID < 120) && (team)) || ((iID >= 210) && (iID < 220) && (!team)))
    {
      boolean X1 = false;
      boolean X2 = false;
      boolean Y1 = false;
      boolean Y2 = false;
      for (int iXHelp = 1; iXHelp < 8; iXHelp++)
      {
        if ((iX + iXHelp <= 7) && (!X1)) {
          if (((BGG[(iX + iXHelp)][iY] <= 190) && (BGG[(iX + iXHelp)][iY] != 0)) || ((team) || (
            (BGG[(iX + iXHelp)][iY] < 200) && (!team)))) {
            MovePos MPN = new MovePos();
            ID = iID;
            ID2 = BGG[(iX + iXHelp)][iY];
            PX = (iX + iXHelp);
            PY = iY;
            X = iX;
            Y = iY;
            MP.add(MPN);
            if (BGG[(iX + iXHelp)][iY] > 0) {
              X1 = true;
            }
          } else {
            X1 = true;
          }
        }
        
        if ((iX - iXHelp >= 0) && (!X2)) {
          if (((BGG[(iX - iXHelp)][iY] <= 190) && (BGG[(iX - iXHelp)][iY] != 0)) || ((team) || (
            (BGG[(iX - iXHelp)][iY] < 200) && (!team)))) {
            MovePos MPN = new MovePos();
            ID = iID;
            ID2 = BGG[(iX - iXHelp)][iY];
            PX = (iX - iXHelp);
            PY = iY;
            X = iX;
            Y = iY;
            MP.add(MPN);
            
            if (BGG[(iX - iXHelp)][iY] > 0) {
              X2 = true;
            }
          } else {
            X2 = true;
          }
        }
        if ((iY - iXHelp >= 0) && (!Y1)) {
          if (((BGG[iX][(iY - iXHelp)] != 0) && (BGG[iX][(iY - iXHelp)] <= 190)) || ((team) || (
            (BGG[iX][(iY - iXHelp)] < 200) && (!team))))
          {
            MovePos MPN = new MovePos();
            ID = iID;
            ID2 = BGG[iX][(iY - iXHelp)];
            PX = iX;
            PY = (iY - iXHelp);
            X = iX;
            Y = iY;
            MP.add(MPN);
            if (BGG[iX][(iY - iXHelp)] > 0) {
              Y1 = true;
            }
          } else {
            Y1 = true;
          }
        }
        
        if ((iY + iXHelp <= 7) && (!Y2)) {
          if (((BGG[iX][(iY + iXHelp)] != 0) && (BGG[iX][(iY + iXHelp)] <= 190)) || ((team) || (
            (BGG[iX][(iY + iXHelp)] < 200) && (!team))))
          {
            MovePos MPN = new MovePos();
            ID = iID;
            ID2 = BGG[iX][(iY + iXHelp)];
            PX = iX;
            PY = (iY + iXHelp);
            X = iX;
            Y = iY;
            MP.add(MPN);
            
            if (BGG[iX][(iY + iXHelp)] > 0) {
              Y2 = true;
            }
          } else {
            Y2 = true;
          }
        }
      }
    }
    else if (((iID >= 120) && (iID < 130) && (team)) || ((iID >= 220) && (iID < 230) && (!team)))
    {
      if ((iX + 2 < 8) && (iY + 1 < 8)) {
        int iDif = Math.abs(BGG[(iX + 2)][(iY + 1)] - BGG[iX][iY]);
        if (iDif > 50) {
          MovePos MPN = new MovePos();
          ID = iID;
          ID2 = BGG[(iX + 2)][(iY + 1)];
          PX = (iX + 2);
          PY = (iY + 1);
          X = iX;
          Y = iY;
          MP.add(MPN);
        }
      }
      
      if ((iX + 2 < 8) && (iY - 1 >= 0)) {
        int iDif = Math.abs(BGG[(iX + 2)][(iY - 1)] - BGG[iX][iY]);
        if (iDif > 50) {
          MovePos MPN = new MovePos();
          ID = iID;
          ID2 = BGG[(iX + 2)][(iY - 1)];
          PX = (iX + 2);
          PY = (iY - 1);
          X = iX;
          Y = iY;
          MP.add(MPN);
        }
      }
      
      if ((iX + 1 < 8) && (iY + 2 < 8)) {
        int iDif = Math.abs(BGG[(iX + 1)][(iY + 2)] - BGG[iX][iY]);
        if (iDif > 50) {
          MovePos MPN = new MovePos();
          ID = iID;
          ID2 = BGG[(iX + 1)][(iY + 2)];
          PX = (iX + 1);
          PY = (iY + 2);
          X = iX;
          Y = iY;
          MP.add(MPN);
        }
      }
      
      if ((iX + 1 < 8) && (iY - 2 >= 0)) {
        int iDif = Math.abs(BGG[(iX + 1)][(iY - 2)] - BGG[iX][iY]);
        if (iDif > 50) {
          MovePos MPN = new MovePos();
          ID = iID;
          ID2 = BGG[(iX + 1)][(iY - 2)];
          PX = (iX + 1);
          PY = (iY - 2);
          X = iX;
          Y = iY;
          MP.add(MPN);
        }
      }
      
      if ((iX - 1 >= 0) && (iY + 2 < 8)) {
        int iDif = Math.abs(BGG[(iX - 1)][(iY + 2)] - BGG[iX][iY]);
        if (iDif > 50) {
          MovePos MPN = new MovePos();
          ID = iID;
          ID2 = BGG[(iX - 1)][(iY + 2)];
          PX = (iX - 1);
          PY = (iY + 2);
          X = iX;
          Y = iY;
          MP.add(MPN);
        }
      }
      
      if ((iX - 1 >= 0) && (iY - 2 >= 0)) {
        int iDif = Math.abs(BGG[(iX - 1)][(iY - 2)] - BGG[iX][iY]);
        if (iDif > 50) {
          MovePos MPN = new MovePos();
          ID = iID;
          ID2 = BGG[(iX - 1)][(iY - 2)];
          PX = (iX - 1);
          PY = (iY - 2);
          X = iX;
          Y = iY;
          MP.add(MPN);
        }
      }
      
      if ((iX - 2 >= 0) && (iY + 1 < 8)) {
        int iDif = Math.abs(BGG[(iX - 2)][(iY + 1)] - BGG[iX][iY]);
        if (iDif > 50) {
          MovePos MPN = new MovePos();
          ID = iID;
          ID2 = BGG[(iX - 2)][(iY + 1)];
          PX = (iX - 2);
          PY = (iY + 1);
          X = iX;
          Y = iY;
          MP.add(MPN);
        }
      }
      
      if ((iX - 2 >= 0) && (iY - 1 >= 0)) {
        int iDif = Math.abs(BGG[(iX - 2)][(iY - 1)] - BGG[iX][iY]);
        if (iDif > 50) {
          MovePos MPN = new MovePos();
          ID = iID;
          ID2 = BGG[(iX - 2)][(iY - 1)];
          PX = (iX - 2);
          PY = (iY - 1);
          X = iX;
          Y = iY;
          MP.add(MPN);
        }
      }
    }
    else if (((iID >= 130) && (iID < 140) && (team)) || ((iID >= 230) && (iID < 240) && (!team)))
    {
      boolean D1 = false;
      boolean D2 = false;
      boolean D3 = false;
      boolean D4 = false;
      for (int iHelp = 1; iHelp < 8; iHelp++)
      {
        if ((iX + iHelp <= 7) && (iY + iHelp <= 7) && (!D1)) {
          if (((BGG[(iX + iHelp)][(iY + iHelp)] != 0) && (BGG[(iX + iHelp)][(iY + iHelp)] <= 190)) || ((team) || (
            (BGG[(iX + iHelp)][(iY + iHelp)] < 200) && (!team)))) {
            MovePos MPN = new MovePos();
            ID = iID;
            ID2 = BGG[(iX + iHelp)][(iY + iHelp)];
            PX = (iX + iHelp);
            PY = (iY + iHelp);
            X = iX;
            Y = iY;
            MP.add(MPN);
            
            if (BGG[(iX + iHelp)][(iY + iHelp)] > 0) {
              D1 = true;
            }
          } else {
            D1 = true;
          }
        }
        
        if ((iX + iHelp <= 7) && (iY - iHelp >= 0) && (!D2)) {
          if (((BGG[(iX + iHelp)][(iY - iHelp)] != 0) && (BGG[(iX + iHelp)][(iY - iHelp)] <= 190)) || ((team) || (
            (BGG[(iX + iHelp)][(iY - iHelp)] < 200) && (!team)))) {
            MovePos MPN = new MovePos();
            ID = iID;
            ID2 = BGG[(iX + iHelp)][(iY - iHelp)];
            PX = (iX + iHelp);
            PY = (iY - iHelp);
            X = iX;
            Y = iY;
            MP.add(MPN);
            
            if (BGG[(iX + iHelp)][(iY - iHelp)] > 0) {
              D2 = true;
            }
          } else {
            D2 = true;
          }
        }
        
        if ((iX - iHelp >= 0) && (iY - iHelp >= 0) && (!D3)) {
          if (((BGG[(iX - iHelp)][(iY - iHelp)] != 0) && (BGG[(iX - iHelp)][(iY - iHelp)] <= 190)) || ((team) || (
            (BGG[(iX - iHelp)][(iY - iHelp)] < 200) && (!team)))) {
            MovePos MPN = new MovePos();
            ID = iID;
            ID2 = BGG[(iX - iHelp)][(iY - iHelp)];
            PX = (iX - iHelp);
            PY = (iY - iHelp);
            X = iX;
            Y = iY;
            MP.add(MPN);
            
            if (BGG[(iX - iHelp)][(iY - iHelp)] > 0) {
              D3 = true;
            }
          } else {
            D3 = true;
          }
        }
        
        if ((iX - iHelp >= 0) && (iY + iHelp <= 7) && (!D4)) {
          if (((BGG[(iX - iHelp)][(iY + iHelp)] != 0) && (BGG[(iX - iHelp)][(iY + iHelp)] <= 190)) || ((team) || (
            (BGG[(iX - iHelp)][(iY + iHelp)] < 200) && (!team)))) {
            MovePos MPN = new MovePos();
            ID = iID;
            ID2 = BGG[(iX - iHelp)][(iY + iHelp)];
            PX = (iX - iHelp);
            PY = (iY + iHelp);
            X = iX;
            Y = iY;
            MP.add(MPN);
            
            if (BGG[(iX - iHelp)][(iY + iHelp)] > 0) {
              D4 = true;
            }
          } else {
            D4 = true;
          }
        }
      }
    }
    else if (((iID >= 140) && (iID < 150) && (team)) || ((iID >= 240) && (iID < 250) && (!team)))
    {

      boolean X1 = false;
      boolean X2 = false;
      boolean Y1 = false;
      boolean Y2 = false;
      boolean D1 = false;
      boolean D2 = false;
      boolean D3 = false;
      boolean D4 = false;
      for (int iHelp = 1; iHelp < 8; iHelp++)
      {
        if ((iX + iHelp <= 7) && (!X1)) {
          if (((BGG[(iX + iHelp)][iY] <= 190) && (BGG[(iX + iHelp)][iY] != 0)) || ((team) || (
            (BGG[(iX + iHelp)][iY] < 200) && (!team))))
          {
            MovePos MPN = new MovePos();
            ID = iID;
            ID2 = BGG[(iX + iHelp)][iY];
            PX = (iX + iHelp);
            PY = iY;
            X = iX;
            Y = iY;
            MP.add(MPN);
            if (BGG[(iX + iHelp)][iY] > 0) {
              X1 = true;
            }
          } else {
            X1 = true;
          }
        }
        
        if ((iX - iHelp >= 0) && (!X2)) {
          if (((BGG[(iX - iHelp)][iY] <= 190) && (BGG[(iX - iHelp)][iY] != 0)) || ((team) || (
            (BGG[(iX - iHelp)][iY] < 200) && (!team)))) {
            MovePos MPN = new MovePos();
            ID = iID;
            ID2 = BGG[(iX - iHelp)][iY];
            PX = (iX - iHelp);
            PY = iY;
            X = iX;
            Y = iY;
            MP.add(MPN);
            
            if (BGG[(iX - iHelp)][iY] > 0) {
              X2 = true;
            }
          } else {
            X2 = true;
          }
        }
        if ((iY - iHelp >= 0) && (!Y1)) {
          if (((BGG[iX][(iY - iHelp)] != 0) && (BGG[iX][(iY - iHelp)] <= 190)) || ((team) || (
            (BGG[iX][(iY - iHelp)] < 200) && (!team))))
          {
            MovePos MPN = new MovePos();
            ID = iID;
            ID2 = BGG[iX][(iY - iHelp)];
            PX = iX;
            PY = (iY - iHelp);
            X = iX;
            Y = iY;
            MP.add(MPN);
            if (BGG[iX][(iY - iHelp)] > 0) {
              Y1 = true;
            }
          } else {
            Y1 = true;
          }
        }
        
        if ((iY + iHelp <= 7) && (!Y2)) {
          if (((BGG[iX][(iY + iHelp)] != 0) && (BGG[iX][(iY + iHelp)] <= 190)) || ((team) || (
            (BGG[iX][(iY + iHelp)] < 200) && (!team))))
          {
            MovePos MPN = new MovePos();
            ID = iID;
            ID2 = BGG[iX][(iY + iHelp)];
            PX = iX;
            PY = (iY + iHelp);
            X = iX;
            Y = iY;
            MP.add(MPN);
            
            if (BGG[iX][(iY + iHelp)] > 0) {
              Y2 = true;
            }
          } else {
            Y2 = true;
          }
        }
        
        if ((iX + iHelp <= 7) && (iY + iHelp <= 7) && (!D1)) {
          if (((BGG[(iX + iHelp)][(iY + iHelp)] != 0) && (BGG[(iX + iHelp)][(iY + iHelp)] <= 190)) || ((team) || (
            (BGG[(iX + iHelp)][(iY + iHelp)] < 200) && (!team)))) {
            MovePos MPN = new MovePos();
            ID = iID;
            ID2 = BGG[(iX + iHelp)][(iY + iHelp)];
            PX = (iX + iHelp);
            PY = (iY + iHelp);
            X = iX;
            Y = iY;
            MP.add(MPN);
            
            if (BGG[(iX + iHelp)][(iY + iHelp)] > 0) {
              D1 = true;
            }
          } else {
            D1 = true;
          }
        }
        
        if ((iX + iHelp <= 7) && (iY - iHelp >= 0) && (!D2)) {
          if (((BGG[(iX + iHelp)][(iY - iHelp)] != 0) && (BGG[(iX + iHelp)][(iY - iHelp)] <= 190)) || ((team) || (
            (BGG[(iX + iHelp)][(iY - iHelp)] < 200) && (!team)))) {
            MovePos MPN = new MovePos();
            ID = iID;
            ID2 = BGG[(iX + iHelp)][(iY - iHelp)];
            PX = (iX + iHelp);
            PY = (iY - iHelp);
            X = iX;
            Y = iY;
            MP.add(MPN);
            
            if (BGG[(iX + iHelp)][(iY - iHelp)] > 0) {
              D2 = true;
            }
          } else {
            D2 = true;
          }
        }
        
        if ((iX - iHelp >= 0) && (iY - iHelp >= 0) && (!D3)) {
          if (((BGG[(iX - iHelp)][(iY - iHelp)] != 0) && (BGG[(iX - iHelp)][(iY - iHelp)] <= 190)) || ((team) || (
            (BGG[(iX - iHelp)][(iY - iHelp)] < 200) && (!team)))) {
            MovePos MPN = new MovePos();
            ID = iID;
            ID2 = BGG[(iX - iHelp)][(iY - iHelp)];
            PX = (iX - iHelp);
            PY = (iY - iHelp);
            X = iX;
            Y = iY;
            MP.add(MPN);
            
            if (BGG[(iX - iHelp)][(iY - iHelp)] > 0) {
              D3 = true;
            }
          } else {
            D3 = true;
          }
        }
        
        if ((iX - iHelp >= 0) && (iY + iHelp <= 7) && (!D4)) {
          if (((BGG[(iX - iHelp)][(iY + iHelp)] != 0) && (BGG[(iX - iHelp)][(iY + iHelp)] <= 190)) || ((team) || (
            (BGG[(iX - iHelp)][(iY + iHelp)] < 200) && (!team)))) {
            MovePos MPN = new MovePos();
            ID = iID;
            ID2 = BGG[(iX - iHelp)][(iY + iHelp)];
            PX = (iX - iHelp);
            PY = (iY + iHelp);
            X = iX;
            Y = iY;
            MP.add(MPN);
            
            if (BGG[(iX - iHelp)][(iY + iHelp)] > 0) {
              D4 = true;
            }
          } else {
            D4 = true;
          }
        }
      }
    }
    else if (((iID == 150) && (team)) || ((iID == 250) && (!team))) {
      for (int Y = -1; Y <= 1; Y++) {
        for (int X = -1; X <= 1; X++) {
          if ((iX + X >= 0) && (iX + X < 8) && (iY + Y < 8) && (iY + Y >= 0) && (
            ((BGG[(iX + X)][(iY + Y)] != 0) && (BGG[(iX + X)][(iY + Y)] <= 160)) || ((team) || (
            (BGG[(iX + X)][(iY + Y)] < 190) && (!team))))) {
            MovePos MPN = new MovePos();
            ID = iID;
            ID2 = BGG[(iX + X)][(iY + Y)];
            PX = (iX + X);
            PY = (iY + Y);
            X = iX;
            Y = iY;
            
            MP.add(MPN);
          }
        }
      }
      
      if ((iX - 4 >= 0) && 
        (BGG[(iX - 1)][iY] == 0) && (BGG[(iX - 2)][iY] == 0) && (BGG[(iX - 3)][iY] == 0)) {
        boolean Schach = false;
        for (int iXHelp = 0; iXHelp <= 4; iXHelp++) {
          if (_BGG2.SchachKing(team, _BGG2, iX - iXHelp, iY, true, true)) {
            Schach = true;
            break;
          }
        }
        
        if (!Schach) {
          int iIDRook = BGG[(iX - 4)][iY];
          if ((!_BGG2.getbKingMoved(iID)) && (!_BGG2.getbRookMoved(iIDRook))) {
            MovePos MPN = new MovePos();
            ID = iID;
            ID2 = BGG[(iX - 2)][iY];
            ID3 = BGG[(iX - 3)][iY];
            ID4 = BGG[(iX - 4)][iY];
            ID5 = BGG[(iX - 1)][iY];
            PX = (iX - 2);
            PY = iY;
            X = iX;
            Y = iY;
            X3 = (iX - 3);
            Y3 = iY;
            X4 = (iX - 4);
            Y4 = iY;
            X5 = (iX - 1);
            Y5 = iY;
            MP.add(MPN);
          }
        }
      }
      


      if ((iX + 3 <= 7) && 
        (BGG[(iX + 1)][iY] == 0) && (BGG[(iX + 2)][iY] == 0)) {
        boolean Schach = false;
        for (int iXHelp = 0; iXHelp <= 3; iXHelp++) {
          if (_BGG2.SchachKing(team, _BGG2, iX + iXHelp, iY, true, true)) {
            break;
          }
        }
        
        if (!Schach) {
          int iIDRook = BGG[(iX + 3)][iY];
          if ((!_BGG2.getbKingMoved(iID)) && (!_BGG2.getbRookMoved(iIDRook))) {
            MovePos MPN = new MovePos();
            ID = iID;
            ID2 = BGG[(iX + 2)][iY];
            ID4 = BGG[(iX + 3)][iY];
            ID5 = BGG[(iX + 1)][iY];
            PX = (iX + 2);
            PY = iY;
            X = iX;
            Y = iY;
            X4 = (iX + 3);
            Y4 = iY;
            X5 = (iX + 1);
            Y5 = iY;
            MP.add(MPN);
          }
        }
      }
    }
    


    return MP;
  }
  













  public void Bauerntausch(final boolean team, final int XX, final int YY)
  {
    final JFrame ChooserF = new JFrame("which meeple?");
    int Xpos = 200;
    int Ypos = 200;
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
          if (_BGG2.getQueenNumber() < 4)
          {

            _BGG2.setMove(true);
            
            if (team) {
              _BGG2.higherQueenNumber();
              Number = _BGG2.getQueenNumber();
              _BGG2.setBackgroundGrid(XX, YY, Number + 140);
              Queen khaleesi = new Queen(true, Number + 140, XX, YY);
              _BGG2.Objectives.set(Number + 140, khaleesi);
              TheMove[XX][YY] = (140 + Number);
            } else {
              _BGG2.higherQueenNumber();
              Number = _BGG2.getQueenNumber();
              _BGG2.setBackgroundGrid(XX, YY, Number + 240);
              Queen khaleesi = new Queen(false, Number + 240, XX, YY);
              _BGG2.Objectives.set(Number + 240, khaleesi);
              TheMove[XX][YY] = (240 + Number);
            }
          } else {
            JFrame Frame1 = new JFrame("");
            JOptionPane.showMessageDialog(Frame1, "No more Queens available! Choose another one!", 
              "OTHER MEEPLE", 0);
            Bauerntausch(team, 0, 0);
          }
        }
        else if (NoOne == "Jumper")
        {


          if (team) {
            _BGG2.higherJumperNumber();
            Number = _BGG2.getJumperNumber();
            _BGG2.setBackgroundGrid(XX, YY, Number + 121);
            Jumper drogo = new Jumper(true, Number + 121, XX, YY);
            _BGG2.Objectives.set(Number + 121, drogo);
            TheMove[XX][YY] = (121 + Number);
          } else {
            _BGG2.higherJumperNumber();
            Number = _BGG2.getJumperNumber();
            _BGG2.setBackgroundGrid(XX, YY, Number + 221);
            Jumper drogo = new Jumper(false, Number + 221, XX, YY);
            _BGG2.Objectives.set(Number + 221, drogo);
            TheMove[XX][YY] = (221 + Number);
          }
        }
        else if (NoOne == "Runner")
        {


          if (team) {
            _BGG2.higherRunnerNumber();
            Number = _BGG2.getRunnerNumber();
            _BGG2.setBackgroundGrid(XX, YY, Number + 131);
            Runner runni = new Runner(true, Number + 131, XX, YY);
            _BGG2.Objectives.set(Number + 131, runni);
            TheMove[XX][YY] = (131 + Number);
          } else {
            _BGG2.higherRunnerNumber();
            Number = _BGG2.getRunnerNumber();
            _BGG2.setBackgroundGrid(XX, YY, Number + 231);
            Runner runni = new Runner(false, Number + 231, XX, YY);
            _BGG2.Objectives.set(Number + 231, runni);
            TheMove[XX][YY] = (231 + Number);
          }
        }
        else if (NoOne == "Tower")
        {


          if (team) {
            _BGG2.higherTowerNumber();
            Number = _BGG2.getTowerNumber();
            _BGG2.setBackgroundGrid(XX, YY, Number + 111);
            Tower TheRedKeep = new Tower(true, Number + 111, XX, YY);
            _BGG2.Objectives.set(Number + 111, TheRedKeep);
            TheMove[XX][YY] = (111 + Number);
          } else {
            _BGG2.higherTowerNumber();
            Number = _BGG2.getTowerNumber();
            _BGG2.setBackgroundGrid(XX, YY, Number + 211);
            Tower TheRedKeep = new Tower(false, Number + 211, XX, YY);
            _BGG2.Objectives.set(Number + 211, TheRedKeep);
            TheMove[XX][YY] = (211 + Number);
          }
        }
        
        _Bauerntausch = false;
        



        _BGG2.iBackground = TheMove;
        getSchach2();
        BG.redraw();
        if (BG.getChoose() == 2) {
          AI _AI = new AI(_BGG2, BG);
          _AI.start();
          BG.setLastMoveList(_AI.getLMoveList());
          BG.setThinking(true);
        }
        

        ChooserF.setVisible(false);
      }
      

    });
    ChooserF.add(northPanel, "North");
    ChooserF.add(southPanel, "South");
    ChooserF.setUndecorated(true);
    ChooserF.setVisible(true);
  }
  










  public void getSchach()
  {
    King K1 = (King)_BGG2.Objects(150);
    int XKing1 = K1.getXMeeplePos();
    int YKing1 = K1.getYMeeplePos();
    
    King K2 = (King)_BGG2.Objects(250);
    int XKing2 = K2.getXMeeplePos();
    int YKing2 = K2.getYMeeplePos();
    
    _Blackschach = _BGG2.SchachKing(false, _BGG2, XKing2, YKing2, true, false);
    


    _Whiteschach = _BGG2.SchachKing(true, _BGG2, XKing1, YKing1, true, false);
  }
  







  public void getSchach2()
  {
    JFrame Frame1 = new JFrame();
    

    int XKing2 = 10;
    int XKing1 = 10;
    int YKing1 = 10;
    int YKing2 = 10;
    for (int Y = 0; Y < 8; Y++) {
      for (int X = 0; X < 8; X++) {
        if (TheMove[X][Y] == 150) {
          XKing1 = X;
          YKing1 = Y;
        }
        if (TheMove[X][Y] == 250) {
          XKing2 = X;
          YKing2 = Y;
        }
      }
    }
    _BGG2.iBackground = TheMove;
    
    boolean Blackschach = _BGG2.SchachKing(false, _BGG2, XKing2, YKing2, false, false);
    if ((Blackschach) && (!_BGG2.getSchachMattBlack())) {
      Platform.runLater(new Runnable()
      {
        public void run()
        {
          try {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("check");
            alert.setHeaderText("Blackking is in check!");
            alert.setContentText("Blackking is in check!");
            alert.showAndWait();
          }
          catch (Exception ex) {
            ex.printStackTrace();
          }
        }
      });
    }
    

    boolean Whiteschach = _BGG2.SchachKing(true, _BGG2, XKing1, YKing1, false, false);
    if ((Whiteschach) && (!_BGG2.getSchachmattWhite())) {
      Platform.runLater(new Runnable()
      {
        public void run() {
          try {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("check");
            alert.setHeaderText("Whiteking is in check!");
            alert.setContentText("Whiteking is in check!");
            alert.showAndWait();
          }
          catch (Exception ex) {
            ex.printStackTrace();
          }
        }
      });
    }
  }
  






  public void setBGG(int[][] BGG)
  {
    _BGG = BGG;
  }
  



  public void setBSelect(boolean sel)
  {
    _bSelect = sel;
  }
  


  public int[][] getBGG()
  {
    return _BGG;
  }
  


  public int getISelect()
  {
    return _iSelect;
  }
  


  public boolean getBSelect()
  {
    return _bSelect;
  }
  



  public boolean getBauer()
  {
    return _Bauerntausch;
  }
  


  public ArrayList<int[]> getMoveList()
  {
    return _MoveList;
  }
  


  public ArrayList<int[]> getHitList()
  {
    return _HitList;
  }
  



  public ArrayList<int[]> getLastMoveList()
  {
    return _LastMoveList;
  }
  



  public int getIPosX()
  {
    return _iPosX;
  }
  



  public int getIPosY()
  {
    return _iPosY;
  }
  




  public BackgroundGrid getBGG2()
  {
    return _BGG2;
  }
  





  public void setBGG2(BackgroundGrid BGG)
  {
    _BGG2 = BGG;
  }
  




  public void setBoardGui(BoardGui Gui)
  {
    BG = Gui;
  }
}
