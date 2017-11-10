package meeple;

import BackgroundMatrix.BackgroundGrid;
import java.net.URL;
import javax.swing.ImageIcon;
















public class King
  extends SuperMeeple
{
  boolean Schach;
  public boolean setSchach;
  
  public King(boolean t, int i, int x, int y)
  {
    super(t, i, x, y);
  }
  




  public ImageIcon getIcon()
  {
    if (super.isteam()) {
      URL url = King.class.getResource("/Images/kingWhite.png");
      ImageIcon icon = new ImageIcon(url);
      return icon; }
    if (!super.isteam()) {
      URL url = King.class.getResource("/Images/kingBlack.png");
      ImageIcon icon = new ImageIcon(url);
      return icon;
    }
    ImageIcon icon = new ImageIcon("");
    return icon;
  }
  



  public boolean move(int X1, int Y1, int X2, int Y2, boolean team)
  {
    boolean MoveAllowed;
    


    boolean MoveAllowed;
    


    if (team == this.team) { boolean MoveAllowed;
      if (((Math.abs(X1 - X2) == 1) && (Y1 - Y2 == 0)) || ((Math.abs(Y1 - Y2) == 1) && (X2 - X1 == 0)) || ((Math.abs(Y2 - Y1) == 1) && (Math.abs(X2 - X1) == 1))) {
        higherHowOftenMoved();
        MoveAllowed = true;
      } else {
        MoveAllowed = false;
      }
    }
    else {
      MoveAllowed = false;
    }
    
    return MoveAllowed;
  }
  


  public void setSchach(boolean Schach1)
  {
    Schach = Schach1;
  }
  



  public boolean getSchach()
  {
    return Schach;
  }
  





  public boolean strike(int X1, int Y1, int X2, int Y2, boolean team)
  {
    if (this.team == team) {
      if (((Math.abs(X1 - X2) == 1) && (Y1 - Y2 == 0)) || ((Math.abs(Y1 - Y2) == 1) && (X2 - X1 == 0)) || ((Math.abs(Y2 - Y1) == 1) && (Math.abs(X2 - X1) == 1))) {
        higherHowOftenMoved();
        return true;
      }
      return false;
    }
    

    return false;
  }
  





  public boolean move2(int X1, int Y1, int X2, int Y2, BackgroundGrid BGG)
  {
    return false;
  }
  


  public void LowerHowOftenMoved()
  {
    HowOftenMoved = ((short)(HowOftenMoved - 1));
  }
}
