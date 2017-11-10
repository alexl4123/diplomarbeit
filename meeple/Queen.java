package meeple;

import BackgroundMatrix.BackgroundGrid;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.ImageIcon;
















public class Queen
  extends SuperMeeple
{
  public Queen(boolean t, int i, int x, int y)
  {
    super(t, i, x, y);
    super.setSize(30);
  }
  




  public ImageIcon getIcon()
  {
    if (super.isteam()) {
      URL url = Queen.class.getResource("/Images/queenWhite.png");
      ImageIcon icon = new ImageIcon(url);
      
      return icon; }
    if (!super.isteam()) {
      URL url = Queen.class.getResource("/Images/queenBlack.png");
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
    float dX = X2 - X1;
    float dY = Y2 - Y1;
    int samwellTarly = 0;
    
    PosX.set(0, Integer.valueOf(X1));
    PosY.set(0, Integer.valueOf(Y1));
    

    if (team == BGG.getTeam()) {
      try
      {
        if ((dX == 0.0F) && (dY > 0.0F)) {
          Size = ((int)dY);
          for (int i = 1; i < dY; i++) {
            samwellTarly += BGG.getBackgroundGrid(X1, Y1 + i);
            PosX.set(i, Integer.valueOf(X1));
            PosY.set(i, Integer.valueOf(Y1 + i));
          }
        } else if ((dX == 0.0F) && (dY < 0.0F)) {
          Size = ((int)Math.abs(dY));
          for (int i = 1; i < Math.abs(dY); i++) {
            samwellTarly += BGG.getBackgroundGrid(X1, Y1 - i);
            PosX.set(i, Integer.valueOf(X1));
            PosY.set(i, Integer.valueOf(Y1 - i));
          }
        } else if ((dX > 0.0F) && (dY == 0.0F)) {
          Size = ((int)dX);
          for (int i = 1; i < dX; i++) {
            samwellTarly += BGG.getBackgroundGrid(X1 + i, Y1);
            PosX.set(i, Integer.valueOf(X1 + i));
            PosY.set(i, Integer.valueOf(Y1));
          }
        } else if ((dX < 0.0F) && (dY == 0.0F)) {
          Size = ((int)Math.abs(dX));
          for (int i = 1; i < Math.abs(dX); i++) {
            samwellTarly += BGG.getBackgroundGrid(X1 - i, Y1);
            PosX.set(i, Integer.valueOf(X1 - i));
            PosY.set(i, Integer.valueOf(Y1));
          }
        }
        else if (Math.abs(dX / dY) == 1.0F) {
          if ((dX > 0.0F) && (dY > 0.0F)) {
            Size = ((int)dX);
            for (int i = 1; i < dX; i++) {
              samwellTarly += BGG.getBackgroundGrid(X1 + i, Y1 + i);
              PosX.set(i, Integer.valueOf(X1 + i));
              PosY.set(i, Integer.valueOf(Y1 + i));
            }
          } else if ((dX > 0.0F) && (dY < 0.0F)) {
            Size = ((int)dX);
            for (int i = 1; i < dX; i++) {
              samwellTarly += BGG.getBackgroundGrid(X1 + i, Y1 - i);
              PosX.set(i, Integer.valueOf(X1 + i));
              PosY.set(i, Integer.valueOf(Y1 - i));
            }
          } else if ((dX < 0.0F) && (dY > 0.0F)) {
            Size = ((int)dY);
            for (int i = 1; i < dY; i++) {
              samwellTarly += BGG.getBackgroundGrid(X1 - i, Y1 + i);
              PosX.set(i, Integer.valueOf(X1 - i));
              PosY.set(i, Integer.valueOf(Y1 + i));
            }
          } else if ((dX < 0.0F) && (dY < 0.0F)) {
            Size = ((int)Math.abs(dY));
            for (int i = 1; i < Math.abs(dY); i++) {
              samwellTarly += BGG.getBackgroundGrid(X1 - i, Y1 - i);
              PosX.set(i, Integer.valueOf(X1 - i));
              PosY.set(i, Integer.valueOf(Y1 - i));
            }
          }
        } else { if ((Math.abs(dX) > 0.0F) && (dY == 0.0F)) {
            return false;
          }
          samwellTarly = 2000;
        }
        
        if (samwellTarly > 0) {
          return false;
        }
        higherHowOftenMoved();
        return true;
      }
      catch (Exception ex) {
        return false;
      }
    }
    return false;
  }
  




  public void LowerHowOftenMoved()
  {
    HowOftenMoved = ((short)(HowOftenMoved - 1));
  }
}
