package meeple;

import BackgroundMatrix.BackgroundGrid;
import java.io.Serializable;
import java.util.ArrayList;








public abstract class SuperMeeple
  implements Serializable
{
  protected boolean team;
  private int Background;
  private int XPos;
  private int YPos;
  ArrayList<Object> PosX = new ArrayList();
  ArrayList<Object> PosY = new ArrayList();
  
  int Size;
  
  short HowOftenMoved;
  short LastMovedInRound;
  
  public SuperMeeple(boolean t, int i, int x, int y)
  {
    HowOftenMoved = 0;
    LastMovedInRound = 0;
    setteam(t);
    setID(i);
    setMeepleXPos(x);
    setMeepleYPos(y);
  }
  





  public void setLastMovedInRound(short i)
  {
    LastMovedInRound = i;
  }
  



  public short getLastMovedInRound()
  {
    return LastMovedInRound;
  }
  


  public void higherHowOftenMoved()
  {
    HowOftenMoved = ((short)(HowOftenMoved + 1));
  }
  
  public short getHowOftenMoved() {
    return HowOftenMoved;
  }
  




  public void setID(int i)
  {
    Background = i;
  }
  

  public int getID()
  {
    return Background;
  }
  



  public boolean isteam()
  {
    return team;
  }
  




  public void setSize(int Size)
  {
    for (int i = 0; i < Size; i++) {
      PosX.add(Integer.valueOf(i));
      PosY.add(Integer.valueOf(i));
    }
  }
  
  public int getSize()
  {
    return Size;
  }
  




  public ArrayList getAttX()
  {
    return PosX;
  }
  




  public ArrayList getAttY()
  {
    return PosY;
  }
  



  public void setteam(boolean team)
  {
    this.team = team;
  }
  





  public abstract boolean move(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean);
  




  public abstract boolean move2(int paramInt1, int paramInt2, int paramInt3, int paramInt4, BackgroundGrid paramBackgroundGrid);
  




  public abstract boolean strike(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean);
  




  public int getXMeeplePos()
  {
    return XPos;
  }
  
  public int getYMeeplePos() {
    return YPos;
  }
  
  public void setMeepleXPos(int i) {
    XPos = i;
  }
  
  public void setMeepleYPos(int i) { YPos = i; }
}
