package meeple;

import BackgroundMatrix.BackgroundGrid;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.ImageIcon;
















public class Farmer
  extends SuperMeeple
{
  public boolean specialMoved;
  
  public Farmer(boolean t, int i, int x, int y)
  {
    super(t, i, x, y);
    super.setSize(30);
    specialMoved = false;
  }
  




  public ImageIcon getIcon()
  {
    if (super.isteam()) {
      URL url = Farmer.class.getResource("/Images/pawnWhite.png");
      ImageIcon icon = new ImageIcon(url);
      return icon; }
    if (!super.isteam()) {
      URL url = Farmer.class.getResource("/Images/pawnBlack.png");
      ImageIcon icon = new ImageIcon(url);
      return icon;
    }
    ImageIcon icon = new ImageIcon("");
    return icon;
  }
  









  public boolean move(int X1, int Y1, int X2, int Y2, boolean team)
  {
    return false;
  }
  
  public boolean getSpecialMoved() {
    return specialMoved;
  }
  






  public boolean strike(int X1, int Y1, int X2, int Y2, boolean team)
  {
    int X = X1 - X2;
    int Y = Y1 - Y2;
    try {
      if (team == this.team) {
        if ((Math.abs(X / Y) == 1) && (Y == 1) && (!super.isteam())) {
          higherHowOftenMoved();
          return true; }
        if ((Math.abs(X / Y) == 1) && (Y == -1) && (super.isteam())) {
          higherHowOftenMoved();
          return true;
        }
        return false;
      }
      
      return false;
    }
    catch (Exception ex) {}
    return false;
  }
  









  public boolean move2(int X1, int Y1, int X2, int Y2, BackgroundGrid BGG)
  {
    boolean MoveAllowed = false;
    PosX.set(0, Integer.valueOf(X1));
    PosY.set(0, Integer.valueOf(Y1));
    if (BGG.getTeam() == team) {
      if ((X1 - X2 == 0) && (Y1 - Y2 == 1) && (!super.isteam())) {
        higherHowOftenMoved();
        MoveAllowed = true;
      } else if ((X1 - X2 == 0) && (Y2 - Y1 == 1) && (super.isteam())) {
        higherHowOftenMoved();
        MoveAllowed = true;
      } else if ((X1 - X2 == 0) && (Y2 - Y1 == 2) && (super.isteam()) && (super.getHowOftenMoved() < 1))
      {
        if ((BGG.getBackgroundGrid(X1, Y1 + 1) == 0) || (BGG.getBackgroundGrid(X1, Y1 + 1) > 400)) {
          specialMoved = true;
          higherHowOftenMoved();
          MoveAllowed = true;
        } else {
          MoveAllowed = false;
        }
      }
      else if ((X1 - X2 == 0) && (Y1 - Y2 == 2) && (!super.isteam()) && (super.getHowOftenMoved() < 1)) {
        if (BGG.getBackgroundGrid(X1, Y2 + 1) == 0) {
          specialMoved = true;
          higherHowOftenMoved();
          MoveAllowed = true;
        } else {
          MoveAllowed = false;
        }
      } else {
        MoveAllowed = false;
      }
    }
    return MoveAllowed;
  }
  



  public void LowerHowOftenMoved()
  {
    HowOftenMoved = ((short)(HowOftenMoved - 1));
  }
}
