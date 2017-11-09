package meeple;

import BackgroundMatrix.BackgroundGrid;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.ImageIcon;














public class Jumper
  extends SuperMeeple
{
  public Jumper(boolean t, int i, int x, int y)
  {
    super(t, i, x, y);
    super.setSize(30);
  }
  




  public ImageIcon getIcon()
  {
    if (super.isteam()) {
      URL url = Farmer.class.getResource("/Images/jumperWhite.png");
      ImageIcon icon = new ImageIcon(url);
      return icon; }
    if (!super.isteam()) {
      URL url = Farmer.class.getResource("/Images/jumperBlack.png");
      ImageIcon icon = new ImageIcon(url);
      return icon;
    }
    ImageIcon icon = new ImageIcon("");
    return icon;
  }
  








  public boolean move(int X1, int Y1, int X2, int Y2, boolean team)
  {
    int dX = X2 - X1;
    int dY = Y2 - Y1;
    PosX.set(0, Integer.valueOf(X1));
    PosY.set(0, Integer.valueOf(Y1));
    
    if (this.team == team)
      try {
        if ((Math.abs(dX / dY) == 2) && (Math.abs(dX) == 2)) {
          higherHowOftenMoved();
          return true; }
        if ((Math.abs(dY / dX) == 2) && (Math.abs(dY) == 2)) {
          higherHowOftenMoved();
          return true;
        }
        return false;

      }
      catch (Exception e)
      {

        return false;
      }
    return false;
  }
  




  public boolean move2(int X1, int Y1, int X2, int Y2, BackgroundGrid BGG)
  {
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
