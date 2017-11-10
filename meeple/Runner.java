package meeple;

import BackgroundMatrix.BackgroundGrid;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.ImageIcon;

















public class Runner
  extends SuperMeeple
{
  public Runner(boolean t, int i, int x, int y)
  {
    super(t, i, x, y);
    super.setSize(30);
  }
  






  public ImageIcon getIcon()
  {
    if (super.isteam()) {
      URL url = Runner.class.getResource("/Images/runnerWhite.png");
      ImageIcon icon = new ImageIcon(url);
      return icon; }
    if (!super.isteam()) {
      URL url = Runner.class.getResource("/Images/runnerBlack.png");
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
  




  public boolean move2(int X1, int Y1, int X2, int Y2, BackgroundGrid BGG)
  {
    float dX = X2 - X1;
    float dY = Y2 - Y1;
    int forTheWatch = 0;
    
    PosX.set(0, Integer.valueOf(X1));
    PosY.set(0, Integer.valueOf(Y1));
    
    if (team == BGG.getTeam())
    {
      try {
        if (Math.abs(dX / dY) == 1.0F)
        {
          if ((dX > 0.0F) && (dY > 0.0F)) {
            Size = ((int)dX);
            for (int i = 1; i < dX; i++) {
              forTheWatch += BGG.getBackgroundGrid(X1 + i, Y1 + i);
              
              PosX.set(i, Integer.valueOf(X1 + i));
              PosY.set(i, Integer.valueOf(Y1 + i));
            }
          } else if ((dX > 0.0F) && (dY < 0.0F)) {
            Size = ((int)dX);
            for (int i = 1; i < dX; i++) {
              forTheWatch += BGG.getBackgroundGrid(X1 + i, Y1 - i);
              PosX.set(i, Integer.valueOf(X1 + i));
              PosY.set(i, Integer.valueOf(Y1 - i));
            }
          } else if ((dX < 0.0F) && (dY > 0.0F)) {
            Size = ((int)dY);
            for (int i = 1; i < dY; i++) {
              forTheWatch += BGG.getBackgroundGrid(X1 - i, Y1 + i);
              PosX.set(i, Integer.valueOf(X1 - i));
              PosY.set(i, Integer.valueOf(Y1 + i));
            }
          } else if ((dX < 0.0F) && (dY < 0.0F)) {
            Size = ((int)Math.abs(dY));
            for (int i = 1; i < Math.abs(dY); i++) {
              forTheWatch += BGG.getBackgroundGrid(X1 - i, Y1 - i);
              PosX.set(i, Integer.valueOf(X1 - i));
              PosY.set(i, Integer.valueOf(Y1 - i));
            }
          }
        } else { if ((Math.abs(dX) > 0.0F) && (dY == 0.0F)) {
            return false;
          }
          
          forTheWatch = 5;
        }
        
      }
      catch (Exception e)
      {
        return false;
      }
      
      if (forTheWatch > 0) {
        return false;
      }
      
      higherHowOftenMoved();
      return true;
    }
    

    return false;
  }
  








  public boolean strike(int X1, int Y1, int X2, int Y2, boolean team)
  {
    return false;
  }
  


  public void LowerHowOftenMoved()
  {
    HowOftenMoved = ((short)(HowOftenMoved - 1));
  }
}
