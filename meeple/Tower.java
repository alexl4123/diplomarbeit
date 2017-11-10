package meeple;

import BackgroundMatrix.BackgroundGrid;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.ImageIcon;














public class Tower
  extends SuperMeeple
{
  public Tower(boolean t, int i, int x, int y)
  {
    super(t, i, x, y);
    super.setSize(30);
  }
  




  public ImageIcon getIcon()
  {
    if (super.isteam()) {
      URL url = Tower.class.getResource("/Images/towerWhite.png");
      ImageIcon icon = new ImageIcon(url);
      return icon; }
    if (!super.isteam()) {
      URL url = Tower.class.getResource("/Images/towerBlack.png");
      ImageIcon icon = new ImageIcon(url);
      return icon;
    }
    ImageIcon icon = new ImageIcon("");
    return icon;
  }
  











  public boolean strike(int X1, int Y1, int X2, int Y2, boolean team)
  {
    return false;
  }
  



  public boolean move(int X1, int Y1, int X2, int Y2, boolean team)
  {
    return false;
  }
  




  public boolean move2(int X1, int Y1, int X2, int Y2, BackgroundGrid BGG)
  {
    int dX = X1 - X2;
    int dY = Y1 - Y2;
    int samwellTarly = 0;
    
    PosX.set(0, Integer.valueOf(X1));
    PosY.set(0, Integer.valueOf(Y1));
    
    if (team == BGG.getTeam()) {
      if ((dX == 0) && (dY > 0))
      {
        Size = dY;
        for (int i = 1; i < dY; i++) {
          PosX.set(i, Integer.valueOf(X1));
          PosY.set(i, Integer.valueOf(Y1 - i));
          samwellTarly += BGG.getBackgroundGrid(X1, Y1 - i);
        }
      } else if ((dX == 0) && (dY < 0)) {
        Size = Math.abs(dY);
        for (int i = 1; i < Math.abs(dY); i++)
        {
          samwellTarly += iBackground[X1][(Y1 + i)];
          PosX.set(i, Integer.valueOf(X1));
          PosY.set(i, Integer.valueOf(Y1 + i));
        }
      } else if ((dX > 0) && (dY == 0)) {
        Size = dX;
        for (int i = 1; i < dX; i++) {
          samwellTarly += BGG.getBackgroundGrid(X1 - i, Y1);
          PosX.set(i, Integer.valueOf(X1 - i));
          PosY.set(i, Integer.valueOf(Y1));
        }
      } else if ((dX < 0) && (dY == 0)) {
        Size = Math.abs(dX);
        for (int i = 1; i < Math.abs(dX); i++) {
          samwellTarly += BGG.getBackgroundGrid(X1 + i, Y1);
          PosX.set(i, Integer.valueOf(X1 + i));
          PosY.set(i, Integer.valueOf(Y1));
        }
      } else {
        samwellTarly = 2000;
      }
      
      if (samwellTarly > 0) {
        return false;
      }
      higherHowOftenMoved();
      return true;
    }
    
    return false;
  }
  





  public void LowerHowOftenMoved()
  {
    HowOftenMoved = ((short)(HowOftenMoved - 1));
  }
}
