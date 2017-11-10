package Gui;

import javafx.scene.paint.Color;


























public class Tile
{
  private double _X;
  private double _Y;
  private double _W;
  private double _H;
  private int _ID;
  private int _XP;
  private int _YP;
  Color _C;
  
  public Tile(double x, double y, double w, double h)
  {
    _X = x;
    _Y = y;
    _W = w;
    _H = h;
  }
  


  public Color getColor()
  {
    return _C;
  }
  


  public double getX()
  {
    return _X;
  }
  


  public double getY()
  {
    return _Y;
  }
  


  public double getW()
  {
    return _W;
  }
  


  public double getH()
  {
    return _H;
  }
  


  public int getID()
  {
    return _ID;
  }
  


  public int getXP()
  {
    return _XP;
  }
  


  public int getYP()
  {
    return _YP;
  }
  


  public void setColor(Color c)
  {
    _C = c;
  }
  


  public void setX(double X)
  {
    _X = X;
  }
  


  public void setY(double Y)
  {
    _Y = Y;
  }
  


  public void setW(double W)
  {
    _W = W;
  }
  


  public void setH(double H)
  {
    _H = H;
  }
  


  public void setID(int ID)
  {
    _ID = ID;
  }
  


  public void setXP(int XP)
  {
    _XP = XP;
  }
  


  public void setYP(int YP)
  {
    _YP = YP;
  }
  





  public boolean Hit(double x, double y)
  {
    double XMin = _X;
    double XMax = _X + 10.0D;
    double YMin = _Y;
    double YMax = _Y + 10.0D;
    
    if ((x > XMin) && (x < XMax) && (y < YMax) && (y > YMin)) {
      return true;
    }
    
    return false;
  }
}
