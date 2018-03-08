package javachess.gui;

import javafx.scene.paint.Color;

/**
 * @author alexl12
 * @version 1.1 - Draw
 * A chess tile, it has a Location, with, color etc.
 * The chess tiles are the black and white things...
 *
 *
 */
public class Tile  {
	
	/**
	 * _X - double - where the tile is located - X-Coord
	 * _Y - double - where the tile is located - Y-Coord
	 * _W - double - the Width of the tile
	 * _H - double - the Height of the tile
	 */
	private double _X,_Y, _W, _H;
	
	/**
	 * _ID - int - the ID of the Tile (1 - 64)
	 * _XP - int - the X-Coord of the Tile in 8x8
	 * _YP - int - the Y-Coord of the Tile in 8x8
	 */
	private int _ID,_XP,_YP;
	
	/**
	 * current color of the tile
	 */
	Color _C;
		
	/**
	 * Constructor sets the inputs
	 * @param x - XPos
	 * @param y - YPos
	 * @param w - Width
	 * @param h - height
	 */
		public Tile(double x, double y,double w,double h){
			 _X = x;
			 _Y = y;
			 _W = w;
			 _H = h;
		}
		
		/**
		 * @return Color
		 */
		public Color getColor(){
			return _C;
		}
		
		/**
		 * @return XPos
		 */
		public double getX(){
			return _X;
		}
		
		/**
		 * @return YPos
		 */
		public double getY(){
			return _Y;
		}
		
		/**
		 * @return Width
		 */
		public double getW(){
			return _W;
		}
		
		/**
		 * @return Height
		 */
		public double getH(){
			return _H;
		}
		
		/**
		 * @return ID of the Tile (64 Tiles)
		 */
		public int getID(){
			return _ID;
		}
		
		/**
		 * @return XP is the X-Location of the Tile in 8x8
		 */
		public int getXP(){
			return _XP;
		}
		
		/**
		 * @return YP is the Y-Location of the Tile in 8x8
		 */
		public int getYP(){
			return _YP;
		}
		
		/**
		 * @param c - sets the color of the Tile
		 */
		public void setColor(Color c){
			_C = c;
		}
		
		/**
		 * @param X - sets the X-Location of the Tile
		 */
		public void setX(double X){
			_X = X;
		}
		
		/**
		 * @param Y - sets the Y-Location of the Tile
		 */
		public void setY(double Y){
			_Y = Y;
		}
		
		/**
		 * @param W - sets the width of the Tile
		 */
		public void setW(double W){
			_W = W;
		}
		
		/**
		 * @param H - sets the height of the Tile
		 */
		public void setH(double H){
			_H = H;
		}
		
		/**
		 * @param ID - sets the ID of the Tile
		 */
		public void setID(int ID){
			_ID = ID;
		}
		
		/**
		 * @param XP - sets the 8x8 -X-Location of the Tile
		 */
		public void setXP(int XP){
			_XP = XP;
		}
		
		/**
		 * @param YP - sets the 8x8 -Y-Location of the Tile
		 */
		public void setYP(int YP){
			_YP = YP;
		}
		
		
		/*
		 * Check wether a Tile has been selected or not
		 */
		public boolean Hit(double x, double y){
			double XMin, XMax, YMin, YMax;
			
			XMin = _X;
			XMax = _X + 10;
			YMin = _Y;
			YMax = _Y + 10;
			
			if(x > XMin && x < XMax && y < YMax && y > YMin){
				return true;
			}
			
			return false;
		}
}
