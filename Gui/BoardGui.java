package Gui;

import java.awt.Button;
import java.util.ArrayList;
import java.util.Objects;
import java.beans.*;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.plaf.synth.SynthSeparatorUI;

import BackgroundMatrix.BackgroundGrid;
import BackgroundMatrix.Move;
import Game.*;
import javafx.event.EventHandler;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import network.ReadingJob;
import network.hostingJob;
import threadJobs.HighlightingJob;
import javafx.scene.input.MouseEvent;
import launchpad.*;

/**
 * @author alex12 - 2017
 * @version 1.1 - Draw
 * 
 *          BoardGui extends Canvas BoardGui contains a GraphicsContext. With
 *          this GC we display the chess area Also calls the Moves
 * 
 *
 */
public class BoardGui extends Canvas {

	/**
	 * where the entire board is drawn onto
	 */
	GraphicsContext gc;

	/**
	 * _X - double - X-Pos where the player has clicked _Y - double - Y-Pos
	 * where the player has clicked P1X - double - P1Y - double DGX - double -
	 * (not used anymore?) DGY - double - (not used anymore?)
	 */
	private double _X, _Y, P1X, P1Y, DGX, DGY;

	/**
	 * ArrayList<Tile> - contains all tiles
	 */
	private ArrayList<Tile> TileList = new ArrayList<Tile>();

	/**
	 * int[][] - contains all meeples as a numeric value
	 */
	private int[][] _BGG;

	/**
	 * BackgroundGrid - _BGG2 - for saving, ...
	 */
	private BackgroundGrid _BGG2;

	/**
	 * int - for setting the game mode
	 */
	private int _iChoose;

	/**
	 * Local - if gamemode local has been set, contains everything needed for
	 * that
	 */
	public Local L;

	/**
	 * Move - the move class
	 */
	private Move OMove;

	/**
	 * bDrag - boolean - if a drag is currently happening bThinking - boolean -
	 * if the AI is doing its job
	 */
	private boolean bDrag, bThinking;

	/**
	 * _Gui - GUI - for redrawing the GUI (?)
	 */
	private GUI _Gui;

	/**
	 * ArrayList<int[]> - LastMoveList - is for the last move of the AI
	 */
	
	/**
	 * activates or deactivates highlighting
	 */
	private boolean highlighting;
	
	/**
	 * activates or deactivates highlightanimations
	 */
	private boolean highlightAnimations;
	
	/**
	 * determins wether an highlightanimation is running
	 */
	private boolean highlightAnimationRunning;
	
	/**
	 * Used for enable and disable the Hosting Menu Button
	 */
	private boolean _blurryButtonOn;
	
	private ArrayList<int[]> LastMoveList = new ArrayList<int[]>();
	
	/**
	 * For Holds Launchpad
	 * BGG ist updated in redraw methode
	 */
	private interface_class _Lauch = new interface_class();

	/**
	 * Initial Setup for the GUI Contains the Listeners: .setOnMousePressed:
	 * normal Click - Click game .setOnDragDetected: Start the Drag
	 * .setOnMouseDragged: How the Meeple moves with the Mouse ;)
	 * .setOnMouseReleased: Writes and Draws the Gui after Drag
	 * @param <T>
	 */
	
	public IntegerProperty BGGChange;
	
	public <T> BoardGui(GUI Gui) {
		bThinking = false;
		_Gui = Gui;
		bDrag = false;
		DGX = 0;
		DGY = 0;
		BGGChange = new SimpleIntegerProperty(0);
		L = new Local();
		L.startUpLocal();
		gc = this.getGraphicsContext2D();
		OMove = new Move();
		OMove.setBoardGui(this);
		this.widthProperty().addListener(observable -> redraw());
		this.heightProperty().addListener(observable -> redraw());
		this._X = this.getWidth();
		this._Y = this.getHeight();
		
		
		this.BGGChange.addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				
				
				
				System.out.println("TRIGGERED");
				try {
					_BGG =  (int[][]) Gui.getBGG2().getLan().netReadStream.readObject();
					if (_BGG2.getLan().getFirstturn() == true){
						L.setTeam(false);
						_BGG2.setTeam(false);
					} else if (_BGG2.getLan().getFirstturn() == false){
						L.setTeam(true);
						_BGG2.setTeam(true);
					}
				
					
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				_BGG2.iBackground = _BGG;
				_Gui.setBGG2(_BGG2);
				bThinking = false;
				System.out.println("switches bThinking to off");
				redraw();
				System.out.println("hab neu gezeichnet");
				
				
			}
			
			
		
			
			
			
		});
		
		
		_Lauch.iCount.addListener(new ChangeListener<Number>() {
		
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				
				
				System.out.println("Old val:" + oldValue + "::NEW_VAL::" + newValue);
				
				_BGG2 = _Lauch.getBGG2();
				L.setTeam(_BGG2.getTeam());
				_Gui.setBGG2(_BGG2);
				redraw();
			}
		});
		

		this.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				System.out.println(bThinking + "::" + _BGG2.getTeam() + "::" + L.getTeam());
				if (!bThinking && !_BGG2.getSchachmattWhite() && !_BGG2.getSchachmattBlack() && !_BGG2.getDraw()) {
					ButtonClick(event);
				}
				
				event.consume();
			}
		});

		this.setOnDragDetected(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (!bThinking && !_BGG2.getSchachmattWhite() && !_BGG2.getSchachmattBlack()  && !_BGG2.getDraw()) {
					bDrag = true;

					ButtonDragE(event);
				}
			}
		});

		this.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (!bThinking && !_BGG2.getSchachmattWhite() && !_BGG2.getSchachmattBlack()  && !_BGG2.getDraw()) {
					ButtonDragD(event);
				}
			}
		});

		this.setOnMouseReleased(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (!bThinking && !_BGG2.getSchachmattWhite() && !_BGG2.getSchachmattBlack()  && !_BGG2.getDraw()) {
					bDrag = false;
					ButtonDragF(event);
				}

			}
		});

	}

	/**
	 * Drag Start
	 * 
	 * @param e
	 *            - MouseEvenet
	 */
	private void ButtonDragE(MouseEvent e) {
		if (!bThinking) {
			LastMoveList.clear();
			try {
				for (Tile T : TileList) {
					if (T.Hit(e.getX() / P1X, e.getY() / P1Y) && !OMove.getBauer()) {
						int iMatrix = _BGG[T.getXP()][T.getYP()];
						int iPos = OMove.getISelect();

						if (((_iChoose == 1 && L.getTeam()) || (_iChoose == 2 && L.getTeam()) || _iChoose == 0)
								&& iMatrix != iPos) { // if move is possible

						}

					}
				}

			} catch (Exception ex) {

			}
		}

	}

	/**
	 * Drag End
	 * 
	 * @param e
	 *            - Event (Mouse Event)
	 */
	private void ButtonDragF(MouseEvent e) {
		try {
			LastMoveList.clear();
			for (Tile T : TileList) {
				if (T.Hit(e.getX() / P1X, e.getY() / P1Y) && !OMove.getBauer()) {
					OMove.setBoardGui(this);
					int iMatrix = _BGG[T.getXP()][T.getYP()];
					_BGG2.setTeam(L.getTeam());
					_BGG = _BGG2.iBackground;
					int iPos = OMove.getISelect();
					if ((_iChoose == 1 || (_iChoose == 2 && L.getTeam()) || _iChoose == 0)
							&& iPos != iMatrix) { // if move is possible
						int[][] XY = OMove.GetMove(iMatrix, T.getXP(), T.getYP(), _BGG2);
						_BGG = XY;
						_BGG2 = OMove.getBGG2();
						L.setTeam(_BGG2.getTeam());
						_Gui.setBGG2(_BGG2);
						
					}
					System.out.println("::" + L.getTeam() + "::" + _BGG2.getLan().getFirstturn());
					if (_iChoose == 1 && ((!L.getTeam() && !_BGG2.getLan().getFirstturn()) || (L.getTeam() && _BGG2.getLan().getFirstturn()))){ // if move has happend,
															// do what it takes
															// ,,LAN''
						
						
						
						
					/*	if(_BGG2.getLan().getFirstturn() == true){
							
							_BGG2.getLan().setFirstturn(false);
							bThinking = true;
							System.out.println("habala 1");
							_BGG = (int[][]) _BGG2.getLan().netReadStream.readObject();
							System.out.println("habala 2");
							bThinking = false;
						}*/
						
						
						System.out.println("schreib jetzt1");
						_BGG2.getLan().netWriteStream.writeObject(_BGG);
						_BGG2.getLan().netWriteStream.flush();
						for(int y = 0; y<8;y++){
							for(int x = 0; x<8;x++){
								System.out.print(":"+_BGG[x][y]+":");
							}
							System.out.println(" ");
						}
						System.out.println("Hab gschrieben1");
						
						_BGG2.iBackground = _BGG;
						_Gui.setBGG2(_BGG2);
						
						redraw();
						
						
						bThinking = true;
						_Gui.getMenu().rj = new ReadingJob(_Gui);
						Thread rt = new Thread(_Gui.getMenu().rj);
						rt.start();
						System.out.println("Habs gezeichnet");
						
						
						/*
						_BGG = (int[][]) _BGG2.getLan().netReadStream.readObject();
						System.out.println("Hab gelesen du affe");
						bThinking = false;
						redraw();
						*/
						
						
						//Give board to LAN Partner
						//No press possible
						//get Board
						
						
						
						
						
						
					} else if (_iChoose == 2 && !L.getTeam() && !OMove.getBauer()) {// if
																					// move
																					// has
						// happend, do
						// what it
						// necessary
						// ,,AI''
						// Give and take Area
						AI _AI = new AI(_BGG2, this);
						_AI.start();
						LastMoveList = _AI.getLMoveList();
						bThinking = true;
						redraw();
						L.setTeam(true);
					}

				}
			}
		} catch (Exception ex) {

		}
		_Lauch.setBGG(_BGG2);
		redraw();
	}

	/**
	 * Is called while Dragging Simply redraws the Node and it looks like you
	 * have the meeple in your hand ;)
	 * 
	 * @param e
	 *            - MouseEvent
	 */
	private void ButtonDragD(MouseEvent e) {
		int iBGG = _BGG[OMove.getIPosX()][OMove.getIPosY()];
		double X, Y;
		Y = e.getY();
		X = e.getX() - (3.75 * P1X);

		redraw();

		// System.out.println(X + "::" + Y + "::");

		if (iBGG < 110 && iBGG >= 100) { // white pawn
			Image image = new Image("/Images/pawnWhite.png");
			gc.drawImage(image, X, Y, 7.5 * P1X, 7.5 * P1Y);
		} else if (iBGG >= 110 && iBGG < 120) { // white Tower
			Image image = new Image("/Images/towerWhite.png");
			gc.drawImage(image, X, Y, 7.5 * P1X, 7.5 * P1Y);
		} else if (iBGG >= 120 && iBGG < 130) { // white Rider
			Image image = new Image("/Images/jumperWhite.png");
			gc.drawImage(image, X, Y, 7.5 * P1X, 7.5 * P1Y);
		} else if (iBGG >= 130 && iBGG < 140) { // white Runner
			Image image = new Image("/Images/runnerWhite.png");
			gc.drawImage(image, X, Y, 7.5 * P1X, 7.5 * P1Y);
		} else if (iBGG >= 140 && iBGG < 150) { // white Queen
			Image image = new Image("/Images/queenWhite.png");
			gc.drawImage(image, X, Y, 7.5 * P1X, 7.5 * P1Y);
		} else if (iBGG == 150) { // white king
			Image image = new Image("/Images/kingWhite.png");
			gc.drawImage(image, X, Y, 7.5 * P1X, 7.5 * P1Y);
		}

		/*
		 * black team
		 */
		if (iBGG < 210 && iBGG >= 200) { // black pawn
			Image image = new Image("/Images/pawnBlack.png");
			gc.drawImage(image, X, Y, 7.5 * P1X, 7.5 * P1Y);
		} else if (iBGG >= 210 && iBGG < 220) { // black Tower
			Image image = new Image("/Images/towerBlack.png");
			gc.drawImage(image, X, Y, 7.5 * P1X, 7.5 * P1Y);
		} else if (iBGG >= 220 && iBGG < 230) { // black Rider
			Image image = new Image("/Images/jumperBlack.png");
			gc.drawImage(image, X, Y, 7.5 * P1X, 7.5 * P1Y);
		} else if (iBGG >= 230 && iBGG < 240) { // black Runner
			Image image = new Image("/Images/runnerBlack.png");
			gc.drawImage(image, X, Y, 7.5 * P1X, 7.5 * P1Y);
		} else if (iBGG >= 240 && iBGG < 250) { // black Queen
			Image image = new Image("/Images/queenBlack.png");
			gc.drawImage(image, X, Y, 7.5 * P1X, 7.5 * P1Y);
		} else if (iBGG == 250) { // black king
			Image image = new Image("/Images/kingBlack.png");
			gc.drawImage(image, X, Y, 7.5 * P1X, 7.5 * P1Y);
		}

	}

	/**
	 * What happens when a mouse click has occurd? It is in this method,
	 * basically at first it gets the Tile in 8x8 then it calls the getMove
	 * method from the object OMove (class Move). Then it redraws the Scene
	 * 
	 * @param e
	 *            - MouseEvent
	 */
	private void ButtonClick(MouseEvent e) {
		try {
			LastMoveList.clear();
			for (Tile T : TileList) {
				if (T.Hit(e.getX() / P1X, e.getY() / P1Y) && OMove.getBauer() == false) {
					OMove.setBoardGui(this);
					int iMatrix;
					iMatrix = _BGG[T.getXP()][T.getYP()];

					_BGG2.setTeam(L.getTeam());
					_BGG = _BGG2.iBackground;
					OMove.setBGG(_BGG);
					if ((_iChoose == 1 && L.getTeam()) || (_iChoose == 2 && L.getTeam()) || _iChoose == 0) { // if
																												// move
																												// is
																												// possible
						System.out.println("Moved");
						int[][] XY = OMove.GetMove(iMatrix, T.getXP(), T.getYP(), _BGG2);
						_BGG = XY;
						_BGG2 = OMove.getBGG2();
						L.setTeam(_BGG2.getTeam());
						_Gui.setBGG2(_BGG2);
					}

					if (_iChoose == 1 && _BGG2.getTeam() != L.getTeam()) { // if
																			// move
																			// has
																			// happend,
																			// do
																			// what
																			// it
																			// takes
																			// to
																			// LAN
						
						System.out.println("schreib jetzt2");
						_BGG2.getLan().netWriteStream.writeObject(_BGG);
						_BGG2.getLan().netWriteStream.flush();
						for(int y = 0; y<8;y++){
							for(int x = 0; x<8;x++){
								System.out.print(":"+_BGG[x][y]+":");
							}
							System.out.println(" ");
						}
						System.out.println("Hab gschrieben2");
						bThinking = true;
						
						
						
						
						
						
						
					} else if (_iChoose == 2 && _BGG2.getTeam() != L.getTeam() && !OMove.getBauer()) {// if
						// move
						// has
						// happend,
						// do
						// what
						// it
						// takes
						// to
						// AI

						AI _AI = new AI(_BGG2, this);
						_AI.start();
						LastMoveList = _AI.getLMoveList();
						bThinking = true;
						redraw();
						L.setTeam(true);
					}

				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		redraw();
	}

	/**
	 * Highlight fields where move is possible
	 * 
	 * @param i
	 *            - Which field to Highlight (64 Fields)
	 */
	public void HighlightField(int i) {

		Tile T = TileList.get(i);

		gc.setGlobalAlpha(0.1);
		gc.setFill(Color.ROSYBROWN);
		gc.fillRect(T.getX() * P1X, T.getY() * P1Y, T.getW() * P1X, T.getH() * P1Y);
		gc.setGlobalAlpha(1);
	}

	/**
	 * Highlight fields where strike possible
	 * 
	 * @param i-
	 *            Which field to Highlight (64 Fields)
	 */
	public void HighlightSField(int i) {
		Tile T = TileList.get(i);
		gc.setGlobalAlpha(0.04);

		gc.setFill(Color.PINK);
		gc.fillRect(T.getX() * P1X, T.getY() * P1Y, T.getW() * P1X, T.getH() * P1Y);
		gc.setGlobalAlpha(1);
	}

	/**
	 * Highlight fields were you have last moved from and to
	 * 
	 * @param i
	 *            - int - which field to highlight (64 Fields)
	 */
	public void HighlightLField(int i) {
		Tile T = TileList.get(i);
		gc.setGlobalAlpha(0.01);

		gc.setFill(Color.GREEN);
		gc.fillRect(T.getX() * P1X, T.getY() * P1Y, T.getW() * P1X, T.getH() * P1Y);
		gc.setGlobalAlpha(1);
	}
//-----------------------------------------------------------------------------------------------------------
	
	public void newHighlightMoveField(int tileNumber){
		
		Tile T= TileList.get(tileNumber);
		Color moveBlue = Color.rgb(164, 228, 234);
		Color secondMoveBlue= Color.rgb(27, 112, 118);
		
		if(getHighlighting()){
			
			if(getHighlightAnimations()==true){
				
				setHighlightAnimationRunning(true);
				HighlightingJob hj=new HighlightingJob(this, T, this.gc, moveBlue, secondMoveBlue);
				Thread th=new Thread(hj);
				th.start();
				
				
			}else{
				
				
				gc.setStroke(getLinearGradient(secondMoveBlue, moveBlue, 1));
				gc.setLineWidth(2);
				gc.strokeRect(T.getX()*P1X, T.getY()*P1Y, T.getH()*P1X, T.getW()*P1Y);
				
			}
			
			
		}
		
	}
	
	public void newHighlightStrikeField(int tileNumber){
		Tile T= TileList.get(tileNumber);
		Color agressorRed = Color.rgb(187, 6, 6);
		Color secondAgressorRed = Color.rgb(249, 53, 53);
		
		if(getHighlighting()){
			
			 if(getHighlightAnimations()==true){
					
					//insert Animatet codes here
					
				}else{
					
					gc.setStroke(getLinearGradient(agressorRed, secondAgressorRed, 1));
					gc.setLineWidth(2);
					gc.strokeRect(T.getX()*P1X, T.getY()*P1Y, T.getH()*P1X, T.getW()*P1Y);
					
				}
		}
	}
	
	
	public void newHighlightLastField(int tileNumber){
		
		if(getHighlighting()){
			
			if(getHighlightAnimations()==true){
				
				//insert Animatet codes here
				
			}else{
				
				//insert non - Animatet codes here
				
			}
			
		}
		
	}
	

	// ------------------------------------------------------------------------------------------------------
	/**
	 * Simply draw the board first the Lines then the Tiles then highlights
	 * certain tiles, the images and finally the signs
	 */
	public void DrawGrid(int[][] BGG) throws Exception {

		_BGG = BGG;
		
		//Fuckloads of Colors
		Color darkbrown=Color.web("#4D3322");
		Color lightBrown= Color.web("#8C603C");
		Color lightBrownGrad= Color.web("B78357");
		Color birchBrown=Color.web("#D4AC7B");
		Color birchBrownGrad= Color.web("#E8D1B7");
		
		
		
		
		
		
		
		TileList.clear();
		P1X = (_X / 100);
		P1Y = (_Y / 100);
		// _Gui.setBGG2(_BGG2);

		/*
		 * Lines Vertical
		 */
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, 0.3 * P1X, _Y);
		gc.fillRect(99.7 * P1X, 0, 0.3 * P1X, _Y);
		for (int i = 1; i < 9; i++) {
			double x = (i * (0.3 + 11.75)-8.75) * P1X;
			gc.fillRect(x, 0, 0.3 * P1X, _Y);
		}
													

		/*
		 * Lines Horizontal
		 */
			gc.fillRect(0, 0, _X, 0.3 * P1Y);
		for (int i = 1; i < 9; i++) {
			double y = (i * (0.3 + 11.75)-8.75) * P1Y;
			gc.fillRect(0, y, _X, 0.3 * P1Y);
		}
		// -----------------------------------------------------------------------------------------
		/**
		 * Tiles
		 */
		int i = 0;
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				if ((y & 1) == 0) { // Even Odd
					if ((x & 1) == 0) {
						gc.setFill(birchBrown);
					} else {
						gc.setFill(lightBrown);
					}
				} else {
					if ((x & 1) == 0) {
						gc.setFill(lightBrown);
					} else {
						gc.setFill(birchBrown);
					}
				}
				i++;
				double dX = ((x + 2) * 0.3 + (x + 1) * 11.75)-8.75;
				double dY = ((y + 2) * 0.3 + (y + 1) * 11.75)-8.75;
				Tile T = new Tile(dX, dY, 11.75, 11.75);
				Paint p = gc.getFill();
				
				T.setID(i);
				T.setXP(x);
				T.setYP(y);
				T.setColor((Color) p);
				TileList.add(T);
				
				if(T.getColor()==birchBrown){
					gc.setFill(getLinearGradient(birchBrownGrad, birchBrown, 0.3));
				}else{
					gc.setFill(getLinearGradient(lightBrownGrad, lightBrown, 0.3));
				}
				
				
				gc.fillRect(T.getX() * P1X, T.getY() * P1Y, T.getW() * P1X, T.getH() * P1Y);
				
				
				
				
			}
		}
		// -----------------------------------------------------------------------------------------------------
		/**
		 * images
		 */
		int x = 5;
		int y = 5;
		for (y = 0; y < 8; y++) {
			for (x = 0; x < 8; x++) {
				double XP, YP, dX, dY;
				XP = P1X * ((x + 2)*0.3 + (11.75 * (x + 1) - 6.7));
				YP = P1Y * ((y + 2)*0.3 + (11.75 * (y + 1) - 6.7));

				dX = (x + 1) * 10 + (x + 2);
				dY = (y + 1) * 10 + (y + 2);
				int iBGG;
				if (BGG[OMove.getIPosX()][OMove.getIPosY()] == BGG[x][y] && bDrag) {
					iBGG = 0;
				} else {
					iBGG = BGG[x][y];
				}

				if (OMove.getMoveList().size() > 0) {
					for (int ii = 0; ii < OMove.getMoveList().size(); ii++) {
						int[] JJ = new int[8];
						JJ = OMove.getMoveList().get(ii);
						int J = JJ[0];

						//HighlightField(J);
						
						newHighlightMoveField(J);
					}
				}
				if (OMove.getHitList().size() > 0) {
					for (int ii = 0; ii < OMove.getHitList().size(); ii++) {
						int[] JH = new int[8];
						JH = OMove.getHitList().get(ii);
						int J = JH[0];

						//HighlightSField(J);
						newHighlightStrikeField(J);
					}
				}

				if (LastMoveList.size() > 0) { // the AI Moves
					for (int ii = 0; ii < LastMoveList.size(); ii++) {

						int[] JL = new int[8];
						JL = LastMoveList.get(ii);

						int J = JL[0];
						// System.out.println(ii + "::" + J);
						HighlightLField(J);
					}
				}
				if (OMove.getLastMoveList().size() > 0) { // the human moves
					for (int ii = 0; ii < OMove.getLastMoveList().size(); ii++) {

						int[] JL = new int[8];
						JL = OMove.getLastMoveList().get(ii);

						int J = JL[0];
						// System.out.println(ii + "::" + J);
						HighlightLField(J);
					}
				}

				/*
				 * White team
				 */
				if (iBGG < 110 && iBGG >= 100) { // white pawn
					Image image = new Image("/Images/pawnWhite.png");
					gc.drawImage(image, XP, YP, 7.5 * P1X, 7.5 * P1Y);
				} else if (iBGG >= 110 && iBGG < 120) { // white Tower
					Image image = new Image("/Images/towerWhite.png");
					gc.drawImage(image, XP, YP, 7.5 * P1X, 7.5 * P1Y);
				} else if (iBGG >= 120 && iBGG < 130) { // white Rider
					Image image = new Image("/Images/jumperWhite.png");
					gc.drawImage(image, XP, YP, 7.5 * P1X, 7.5 * P1Y);
				} else if (iBGG >= 130 && iBGG < 140) { // white Runner
					Image image = new Image("/Images/runnerWhite.png");
					gc.drawImage(image, XP, YP, 7.5 * P1X, 7.5 * P1Y);
				} else if (iBGG >= 140 && iBGG < 150) { // white Queen
					Image image = new Image("/Images/queenWhite.png");
					gc.drawImage(image, XP, YP, 7.5 * P1X, 7.5 * P1Y);
				} else if (iBGG == 150) { // white king
					Image image = new Image("/Images/kingWhite.png");
					gc.drawImage(image, XP, YP, 7.5 * P1X, 7.5 * P1Y);
				}

				/*
				 * black team
				 */
				if (iBGG < 210 && iBGG >= 200) { // black pawn
					Image image = new Image("/Images/pawnBlack.png");
					gc.drawImage(image, XP, YP, 7.5 * P1X, 7.5 * P1Y);
				} else if (iBGG >= 210 && iBGG < 220) { // black Tower
					Image image = new Image("/Images/towerBlack.png");
					gc.drawImage(image, XP, YP, 7.5 * P1X, 7.5 * P1Y);
				} else if (iBGG >= 220 && iBGG < 230) { // black Rider
					Image image = new Image("/Images/jumperBlack.png");
					gc.drawImage(image, XP, YP, 7.5 * P1X, 7.5 * P1Y);
				} else if (iBGG >= 230 && iBGG < 240) { // black Runner
					Image image = new Image("/Images/runnerBlack.png");  									
					gc.drawImage(image, XP, YP, 7.5 * P1X, 7.5 * P1Y);
				} else if (iBGG >= 240 && iBGG < 250) { // black Queen
					Image image = new Image("/Images/queenBlack.png");
					gc.drawImage(image, XP, YP, 7.5 * P1X, 7.5 * P1Y);
				} else if (iBGG == 250) { // black king
					Image image = new Image("/Images/kingBlack.png");
					gc.drawImage(image, XP, YP, 7.5 * P1X, 7.5 * P1Y);
				}

			}
		}

		// ------------------------------------------------------------------------------------------------
		/**
		 * Here comes the signs
		 */
		for (y = 0; y < 8; y++) { 
			double X = 5.3;
			//double Y = 5 + y + 1 + (10 * y);    <-- Old formula
			double Y = (y*11.75) + (2*0.3) + 3 +(y*0.3);
			
			gc.setFill(darkbrown);
			gc.fillRect(0.3*P1X, Y*P1Y, 3*P1X, 11.75*P1Y);
			gc.setFill(Color.SILVER);
			gc.setTextAlign(TextAlignment.CENTER);
			gc.setTextBaseline(VPos.CENTER);
			
			
			
			String s = Objects.toString((y+1));
			gc.fillText(s, (X-3.6) * P1X, (Y+5.5) * P1Y);
		}

		for (x = 0; x < 8; x++) {
			double Y = 6;
			//double X = 5 + x + 1 + (10 * x);
			double X= (x*11.75)+(2*0.3)+3+(x*0.3);
			gc.setFill(darkbrown);
			gc.fillRect(X*P1X, 0.3*P1Y, 11.75*P1X,3*P1Y );
			//gc.fillRect((X - 5) * P1X, (Y - 5) * P1Y, 10 * P1X, 10 * P1Y);
			gc.setFill(Color.SILVER);
			gc.setTextAlign(TextAlignment.CENTER);
			gc.setTextBaseline(VPos.CENTER);

			String s = null;
			switch (x) {
			case 0:
				s = "A";
				break;
			case 1:
				s = "B";
				break;
			case 2:
				s = "C";
				break;
			case 3:
				s = "D";
				break;
			case 4:
				s = "E";
				break;
			case 5:
				s = "F";
				break;
			case 6:
				s = "G";
				break;
			case 7:
				s = "H";
				break;
			default:
				s = "";
				break;
			}

			gc.fillText(s, (X+5.75) * P1X, (Y-4.3) * P1Y);

		}

	}

	/**
	 * Redraws the Node First makes a Test (set the complete Background to
	 * green) then calls DrawGrid(_BGG); _BGG is the Backgroundgrid
	 */
	public void redraw() {
		
		Color oakBrown=new Color (0.247,0.145,0.062,1);
		
		if (!bThinking) {
			gc.setFill(oakBrown);
			gc.fillRect(0, 0, this.getWidth(), this.getHeight());
			try {
				_Lauch.setBGG(_BGG2);
				DrawGrid(_BGG);
			} catch (Exception e) {
			}
		}

	}

	public void drawBlurryMenu(hostingJob stopJob){
		
		P1X = (_X / 100);
		P1Y = (_Y / 100);
		
		BoxBlur frostEffect = new BoxBlur(10, 10, 1000);
		gc.setEffect(frostEffect);
		setBlurryButtonOn(true);
		bThinking=true;
		
		this.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				
				if(_blurryButtonOn){
				
				System.out.println("Mouse clicked" + "X:" + event.getX() + "  Y:" + event.getY() );
				System.out.println("Redrawing");
				bThinking=false;
				setHighlighting(true);
				setBlurryButtonOn(false);
				if(_BGG2.getLan().getIsConnectet()==true){
					
					_Gui.getMenu().menuFile.getItems().removeAll(_Gui.getMenu().Load, _Gui.getMenu().Save, _Gui.getMenu().newGame, _Gui.getMenu().refresh);
					_Gui.getMenu().menuGame.getItems().removeAll(_Gui.getMenu().GameMode0, _Gui.getMenu().GameMode1, _Gui.getMenu().GameMode2, _Gui.getMenu().GameMode3);
					
				}
				if(_BGG2.getLan().getIsConnectet() == false){
				System.out.println("socket stopped");
				stopJob.stopSocket();
				_Gui.setChoose(0);
				_BGG2.setChoose(0);
				_Gui.getMenu().setSelect(0);
				}
				
				_Gui.getStage().setResizable(true);
				
				
				
				
				
					try {
						DrawGrid(_BGG);
					} catch (Exception e) {
					
					}
				}
			}
		});
		
		try {
			DrawGrid(_BGG);
			_Gui.getStage().setResizable(false);
			gc.setFill(Color.ANTIQUEWHITE);
			gc.setEffect(new DropShadow(10, Color.BLACK));
			gc.fillRect(20*P1X, 35*P1Y, 60*P1X, 20*P1Y);
			gc.setEffect(null);
			gc.setFill(Color.BLACK);
			gc.setFont(new Font(2*P1X));
			gc.fillText("Waiting for Connections...", 50*P1X, 40*P1Y);
			gc.fillText("Click to abort and proceed in local mode!", 50*P1X, 48*P1Y);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		
		
	}
	
	/**
	 * Returns the current Game mode
	 * 
	 * @return _iChoose
	 */
	public int getChoose() {
		return _iChoose;
	}

	/**
	 * For Resizing the Node
	 * 
	 * @param x
	 *            - Width
	 * @param y
	 *            - Height
	 */
	public void setXY(double x, double y) {

		this.setWidth(x);
		this._X = x;
		this.setHeight(y);
		this._Y = y;
		redraw();

	}

	public double get_X(){
		return this._X;
	}
	
	public double get_Y(){
		return this._Y;
		
	}
	
	/**
	 * Sets the BackgroundGrid
	 * 
	 * @param BGG
	 */
	public void setBGG2(BackgroundGrid BGG) {
		_BGG2 = BGG;
	}

	/**
	 * 
	 * @return _BGG2 - A BackgroundGrid Object
	 */
	public BackgroundGrid getBGG2() {
		return _BGG2;
	}

	/**
	 * For the different Game Modes
	 * 
	 * @param Choose
	 *            - int - 0 is local - 1 is Lan - 2 is AI
	 */
	public void setChoose(int Choose) {
		_iChoose = Choose;
	}

	/**
	 * 
	 * @param b
	 *            - boolean - for setting which teams move is it
	 */
	public void setTeam(boolean b) {
		L.setTeam(b);
	}

	/**
	 * 
	 * @param b
	 *            - boolean - if the AI is doing its job
	 */
	public void setThinking(boolean b) {
		bThinking = b;
	}

	/**
	 * 
	 * @return - boolean - if AI is doing its job - true
	 */
	public boolean getThinking() {
		return bThinking;
	}

	/**
	 * 
	 * @return ArrayList<int[]> - last moves - contained in int[0] as ID of tile
	 */
	public ArrayList<int[]> getLastMoveList() {
		return LastMoveList;
	}

	/**
	 * 
	 * @param LastMoveL
	 *            - ArrayList<int[]> - last move - contained in int[0] as ID of
	 *            tile -> the more int[] in ArrayList -> the more fields are
	 *            marked
	 */
	public void setLastMoveList(ArrayList<int[]> LastMoveL) {
		LastMoveList = LastMoveL;
	}

/**
 * 
 * @param Color1
 * @param Color2
 * @return
 * 
 * 		Creates a beautyful linear color gradiation
 */
	public LinearGradient getLinearGradient(Color Color1, Color Color2, double repeatValue ){
		LinearGradient linGradient = new LinearGradient(0, 0, repeatValue, repeatValue, true, CycleMethod.REFLECT, new Stop(0.0, Color1), new Stop(1.0, Color2));
		return linGradient;
	}
	
/**
 * 
 * @param Color1
 * @param Color2
 * @return
 * 			
 * 		Creates a beautycul radial color gradiation
 */
	public RadialGradient getRadialGradient(Color Color1, Color Color2){
		
		RadialGradient radGradient = new RadialGradient(0, 0, 0.5, 0.5, 1, true, CycleMethod.REFLECT, new Stop(0.0, Color1), new Stop(1.0, Color2));
		return radGradient;
	}
/**
 * gets the highlighting
 * @return
 */
	public boolean getHighlighting() {
		return highlighting;
	}
	
	/**
	 * gets the _blurryButtonOn
	 * @return
	 */
		public boolean getBlurryButtonOn() {
			return _blurryButtonOn;
		}
		
		/**
		 * sets the highlighting
		 * @param highlighting
		 */
			public void setBlurryButtonOn(boolean _blurryButtonon) {
				this._blurryButtonOn = _blurryButtonon;
			}

		
		
/**
 * sets the highlighting
 * @param highlighting
 */
	public void setHighlighting(boolean highlighting) {
		this.highlighting = highlighting;
	}

	
	/**
	 * gets the highlightanimationstates
	 * @return
	 */
	public boolean getHighlightAnimations() {
		return highlightAnimations;
	}

	/**
	 * sets the highlightanimationstates
	 * @param highlightAnimations
	 */
	public void setHighlightAnimations(boolean highlightAnimations) {
		this.highlightAnimations = highlightAnimations;
	}

	public boolean isHighlightAnimationRunning() {
		return highlightAnimationRunning;
	}

	public void setHighlightAnimationRunning(boolean highlightAnimationRunning) {
		this.highlightAnimationRunning = highlightAnimationRunning;
	}
	
	public GUI getGui(){
		return _Gui;
	}
	
	public void setBthinking(boolean b){
		bThinking = b;
	}
	
	public boolean getBthinking(){
		return bThinking;
	}
	
	/**
	 * For Hold&Klotz
	 * @param Lauch - Object of interface_class
	 */
	public void setInterface_Class(interface_class Lauch) {
		_Lauch = Lauch;
	}
}
