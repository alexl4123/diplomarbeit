package javachess.gui;

import java.util.ArrayList;
import java.util.Objects;
import java.io.IOException;
import java.net.InetAddress;

import javachess.audio.AudioManager;
import javachess.backgroundmatrix.BackgroundGrid;
import javachess.backgroundmatrix.Move;
import javachess.game.*;
import javachess.launchpad.*;
import javachess.network.ReadingJob;
import javachess.network.hostingJob;
import javafx.event.EventHandler;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * @author mhub - 2018
 * @version 2.0 
 * 
 *          BoardGui extends Canvas BoardGui contains a GraphicsContext. The borad is displayed by this class.
 *          Also contains listeners for actions. 
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
	private boolean bDrag, bThinking, rectMode;

	/**
	 * @return the rectMode
	 */
	public boolean isRectMode() {
		return rectMode;
	}

	/**
	 * @param rectMode the rectMode to set
	 */
	public void setRectMode(boolean rectMode) {
		this.rectMode = rectMode;
	}

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
	private boolean _blurryButtonOn, startupbuttonOn, heartbeatMenu, onlineHighlight;

	/**
	 * Audiomanager for playing Audio
	 */
	public AudioManager soundPlayer;

	private ArrayList<int[]> LastMoveList = new ArrayList<int[]>();

	/**
	 * if a Launchpad is connected
	 */
	private boolean _bLauch;

	/**
	 * For Holds Launchpad
	 * BGG ist updated in redraw methode
	 */
	private Launchpad _Lauch;


	/**
	 * Integerproperties to trigger certain actions 
	 */
	public IntegerProperty BGGChange, Heartbeat, turnProp, conProp, teamProp;
	public javachess.network.Heartbeat heartBeatJob;




	/**
	 * Initial Setup for the GUI Contains the Listeners: .setOnMousePressed:
	 * normal Click - Click game .setOnDragDetected: Start the Drag
	 * .setOnMouseDragged: How the Meeple moves with the Mouse ;)
	 * .setOnMouseReleased: Writes and Draws the Gui after Drag
	 * @param <T>
	 */
	public <T> BoardGui(GUI Gui) {
		bThinking = false;
		_Gui = Gui;
		bDrag = false;
		DGX = 0;
		DGY = 0;
		this.rectMode = false;
		BGGChange = new SimpleIntegerProperty(0);
		Heartbeat = new SimpleIntegerProperty(0);
		setOnlineHighlight(false);
		soundPlayer = new AudioManager();
		L = new Local();
		L.startUpLocal();
		gc = this.getGraphicsContext2D();
		OMove = new Move();
		this.widthProperty().addListener(observable -> redraw());
		this.heightProperty().addListener(observable -> redraw());
		this._X = this.getWidth();
		this._Y = this.getHeight();
		_bLauch = false;
		turnProp = new SimpleIntegerProperty();
		conProp = new SimpleIntegerProperty();
		teamProp = new SimpleIntegerProperty();

		turnProp.setValue(0);
		conProp.setValue(0);
		teamProp.setValue(0);

		//this code is triggerd after the Heartbeat property is changed --> if connection loss - do this
		this.Heartbeat.addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {

				if(_Gui.getBGG2().getLan().getIsConnectet()==true){
					System.out.println("Heartbeat Timeout");
					_Gui.getBGG2().getLan().setIsConnectet(false);
					heartbeatMenu = true;
					_Gui.getBoardGui().drawBlurryMenu(Gui.getMenu().hostJob);

				}else{
					System.out.println("here");
				}


			}
		});

		//This code is exectued (in an other Trhead) when receiving the new Matrix in LAN
		this.BGGChange.addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {



				System.out.println("TRIGGERED");
				try {

					_BGG =  (int[][]) Gui.getBGG2().getLan().netReadStream.readObject();
					_BGG2.higherTurnRound();

					if (_BGG2.getLan().getFirstturn() == true){
						L.setTeam(false);
						_BGG2.setTeam(false);
					} else if (_BGG2.getLan().getFirstturn() == false){
						L.setTeam(true);
						_BGG2.setTeam(true);
					}


				} catch (ClassNotFoundException e) {
					_blurryButtonOn = true;
					heartbeatMenu = true;
					drawBlurryMenu(null);
					e.printStackTrace();
				} catch (IOException e) {
					_blurryButtonOn = true;
					heartbeatMenu = true;
					drawBlurryMenu(null);
					e.printStackTrace();
				}

				_BGG2.iBackground = _BGG;
				_BGG2.Board = _BGG;
				_Gui.setBGG2(_BGG2);
				bThinking = false;
				System.out.println("switches bThinking to off");
				redraw();
				System.out.println("hab neu gezeichnet");

				if(_bLauch){
					//For @Hold and @Klotz
					_Lauch.setBG(_Gui.getBoardGui());
					_Lauch.setBGG(_BGG2);
				}

				System.out.println("line 227");
				int iWKingX,iBKingX, iWKingY, iBKingY;
				iWKingX = 0;
				iWKingY = 0;
				iBKingX = 0;
				iBKingY = 0;
				for(int iY = 0; iY < 8; iY++){
					for(int iX = 0; iX < 8; iX++){
						if(_BGG2.iBackground[iX][iY]==150){
							iWKingX=iX;
							iWKingY=iY;
						}else if(_BGG2.iBackground[iX][iY]==250){
							iBKingX = iX;
							iBKingY = iY;
						}
					}
				}

				System.out.println("Schach for Team True");
				//	_BGG2.SchachKing(true, _BGG2, iWKingX, iWKingY, false, false);
				System.out.println("Schach for Team false");
				//_BGG2.SchachKing(false, _BGG2, iBKingX, iBKingY, false, false);

				//---------------------------------------------------------------------------------

				boolean Blackschach = _BGG2.SchachKing(false, _BGG2, iBKingX, iBKingY, false, false);
				if (Blackschach == true && !_BGG2.getSchachmattBlack()) {							
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("check");
					alert.setHeaderText("Blackking is in check!");
					alert.setContentText("Blackking is in check!");
					alert.showAndWait();

				}
				boolean Whiteschach = _BGG2.SchachKing(true, _BGG2, iWKingX, iWKingY, false, false);
				if (Whiteschach == true && !_BGG2.getSchachmattWhite()) {		
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("check");
					alert.setHeaderText("Whiteking is in check!");
					alert.setContentText("Whiteking is in check!");
					alert.showAndWait();

				}


				//---------------------------------------------------------------------------------

			}

		});



		//setting listeners for various occasions and doing what is necessary when the event happens
		this.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {



				L.setTeam(_BGG2.getTeam());
				int KingX = 0;
				int KingY = 0;
				for(int iY = 0; iY<8; iY++) {
					for(int iX = 0; iX<8; iX++) {
						if(_BGG2.iBackground[iX][iY]==150) {
							KingX = iX;
							KingY = iY;
						}
					}
				}

				System.out.println("Schachmatt black::"+_BGG2.getSchachmattBlack());

				if (!bThinking && !_BGG2.getSchachmattWhite() && !_BGG2.getSchachmattBlack() && !_BGG2.getDraw()) {
					System.out.println("Pressed");
					bDrag = false;
					ButtonReleased(event, true);
				}

				event.consume();
			}
		});

		//setting listener for a drag
		this.setOnDragDetected(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				L.setTeam(_BGG2.getTeam());
				if (!bThinking && !_BGG2.getSchachmattWhite() && !_BGG2.getSchachmattBlack()  && !_BGG2.getDraw()) {

					bDrag = true;

					ButtonDragE(event);
				}
			}
		});

		//also a listener for a drag
		this.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (!bThinking && !_BGG2.getSchachmattWhite() && !_BGG2.getSchachmattBlack()  && !_BGG2.getDraw()) {
					ButtonDragD(event);
				}
			}
		});

		//what happens on mouse relesased
		this.setOnMouseReleased(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (!bThinking && !_BGG2.getSchachmattWhite() && !_BGG2.getSchachmattBlack()  && !_BGG2.getDraw()) {
					System.out.println("Mouse Released");
					bDrag = false;
					ButtonReleased(event, false);
				}

			}
		});

	}

	/**
	 * Method to handle drag starts
	 * @param e - MouseEvenet
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

	ArrayList<int[]> SaveMoveList = new ArrayList<int[]>();

	/**
	 * This Method is used for drags and button presses.
	 * Everything happens here. LAN, Local or AI Mode.
	 * 
	 * @param e - Event (Mouse Event)
	 */
	private void ButtonReleased(MouseEvent e, boolean ButtonPressed) {
		try {

			LastMoveList.clear();
			System.out.println("BoardGui-TileList:" +TileList.size());

			for (Tile T : TileList) {
				if (T.Hit(e.getX() / P1X, e.getY() / P1Y) && !OMove.getBauer()) {			//getting hits

					int iMatrix = _BGG[T.getXP()][T.getYP()];
					_BGG2.setTeam(L.getTeam());
					_BGG = _BGG2.iBackground;
					int iPos = OMove.getISelect();

					//System.out.println("::DEBUG::"+(ButtonPressed && ((_BGG2.getTeam() && (iMatrix > 0 && iMatrix < 160)) || (!_BGG2.getTeam() && (iMatrix > 160 && iMatrix < 260)))) + "::BUTTON::"+ButtonPressed+"::IPOS::"+(iMatrix > 0 && iMatrix < 160)+"::IPOS::"+iMatrix+"::SELECT::"+OMove.getBSelect()+"::iSelect::"+OMove.getISelect());
					if (((!ButtonPressed && (OMove.getISelect() > 0) && (iPos != iMatrix)) || (ButtonPressed && ((_BGG2.getTeam() && (iMatrix > 0 && iMatrix < 160)) || (!_BGG2.getTeam() && (iMatrix > 160 && iMatrix < 260))))) && (_iChoose == 1 || (_iChoose == 2 && _BGG2.getAITeam() && !L.getTeam()) || (_iChoose == 2 && !_BGG2.getAITeam() && L.getTeam()) || _iChoose == 0)) { // if move is possible
						System.out.println("Init player move");
						soundPlayer.playSound("move");
						int[][] XY = OMove.GetMove(iMatrix, T.getXP(), T.getYP(), _BGG2);					//setting nessecary stuff for local
						_BGG = XY;
						_BGG2 = OMove.getBGG2();
						L.setTeam(_BGG2.getTeam());
						_Gui.setBGG2(_BGG2);
						turnProp.setValue(_BGG2.getTurnRound());
						System.out.println("Player move finished::MatedWhite"+_BGG2.getSchachmattWhite()+"::MATEDBLACK::"+_BGG2.getSchachmattBlack());
					}



					if (_iChoose == 1 && !OMove.getBauer() && ((!L.getTeam() && !_BGG2.getLan().getFirstturn()) || (L.getTeam() && _BGG2.getLan().getFirstturn()))){ // if move has happend,
						//LAN


						System.out.println("schreib jetzt1");
						_BGG2.getLan().netWriteStream.writeObject(_BGG);
						_BGG2.getLan().netWriteStream.flush();
						//_Gui.getStage().setResizable(false);												//stuff for LAN aka. sending
						for(int y = 0; y<8;y++){
							for(int x = 0; x<8;x++){
								System.out.print(":"+_BGG[x][y]+":");
							}
							System.out.println(" ");
						}
						System.out.println("Hab gschrieben1");

						_BGG2.iBackground = _BGG;
						_Gui.setBGG2(_BGG2);
						//Josi was here  //welch ehre
						redraw();

						turnProp.setValue(_BGG2.getTurnRound());
						_BGG2.higherTurnRound();
						bThinking = true;
						_Gui.getMenu().rj = new ReadingJob(_Gui);
						Thread rt = new Thread(_Gui.getMenu().rj);
						rt.start();
						System.out.println("Habs gezeichnet");

					} else if (!ButtonPressed && !_BGG2.getSchachmattWhite() && _iChoose == 2 && _BGG2.getTeam() && !OMove.getBauer() && _BGG2.getAITeam()) {
						//White AI
						System.out.println("White AI init");
						AI _AI = new AI(_BGG2, this,true,false,_BGG2.getAiDepth());
						_AI.start();															//Code for the AIs
						LastMoveList = _AI.getLMoveList();
						bThinking = true;
						redraw();
						_BGG2.setTeam(false);
					} else if (!ButtonPressed && !_BGG2.getSchachmattBlack() && _iChoose == 2 && !_BGG2.getTeam() && !OMove.getBauer() && !_BGG2.getAITeam()) {
						//Black AI
						System.out.println("Black AI init");
						AI _AI = new AI(_BGG2, this,false,false,_BGG2.getAiDepth());

						_AI.start();
						LastMoveList = _AI.getLMoveList();
						bThinking = true;
						redraw();
						_BGG2.setTeam(true);
					} else if(_iChoose == 4) {
						AIvsAI AIFuckUp = new AIvsAI(_BGG2, this);
						AIFuckUp.start();
						bThinking = true;
					}

					System.out.println("LINE 541");
					if(_BGG2.getSchachmattBlack() || _BGG2.getSchachmattWhite()){
						System.out.println("LINE 543");
						_Gui.newBG();
					}

				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if(_bLauch){
			//For @Hold and @Klotz
			_Lauch.setBG(_Gui.getBoardGui());				//launchpad - stuff
			_Lauch.setBGG(_BGG2);
		}
		redraw();
	}

	/**
	 * Is called while Dragging Simply redraws the Node while dragging it around
	 * 
	 * @param e- MouseEvent
	 */
	private void ButtonDragD(MouseEvent e) {
		int iBGG = _BGG[OMove.getIPosX()][OMove.getIPosY()];
		double X, Y;
		Y = e.getY();
		X = e.getX() - (3.75 * P1X);

		redraw();

		// System.out.println(X + "::" + Y + "::");

		if (iBGG < 110 && iBGG >= 100) { // white pawn
			Image image = new Image("/javachess/images/pawnWhite.png");
			gc.drawImage(image, X, Y, 7.5 * P1X, 7.5 * P1Y);
		} else if (iBGG >= 110 && iBGG < 120) { // white Tower
			Image image = new Image("/javachess/images/towerWhite.png");
			gc.drawImage(image, X, Y, 7.5 * P1X, 7.5 * P1Y);
		} else if (iBGG >= 120 && iBGG < 130) { // white Rider
			Image image = new Image("/javachess/images/jumperWhite.png");
			gc.drawImage(image, X, Y, 7.5 * P1X, 7.5 * P1Y);
		} else if (iBGG >= 130 && iBGG < 140) { // white Runner
			Image image = new Image("/javachess/images/runnerWhite.png");
			gc.drawImage(image, X, Y, 7.5 * P1X, 7.5 * P1Y);
		} else if (iBGG >= 140 && iBGG < 150) { // white Queen
			Image image = new Image("/javachess/images/queenWhite.png");
			gc.drawImage(image, X, Y, 7.5 * P1X, 7.5 * P1Y);
		} else if (iBGG == 150) { // white king
			Image image = new Image("/javachess/images/kingWhite.png");
			gc.drawImage(image, X, Y, 7.5 * P1X, 7.5 * P1Y);
		}

		/*
		 * black team
		 */
		if (iBGG < 210 && iBGG >= 200) { // black pawn
			Image image = new Image("/javachess/images/pawnBlack.png");
			gc.drawImage(image, X, Y, 7.5 * P1X, 7.5 * P1Y);
		} else if (iBGG >= 210 && iBGG < 220) { // black Tower
			Image image = new Image("/javachess/images/towerBlack.png");
			gc.drawImage(image, X, Y, 7.5 * P1X, 7.5 * P1Y);
		} else if (iBGG >= 220 && iBGG < 230) { // black Rider
			Image image = new Image("/javachess/images/jumperBlack.png");
			gc.drawImage(image, X, Y, 7.5 * P1X, 7.5 * P1Y);
		} else if (iBGG >= 230 && iBGG < 240) { // black Runner
			Image image = new Image("/javachess/images/runnerBlack.png");
			gc.drawImage(image, X, Y, 7.5 * P1X, 7.5 * P1Y);
		} else if (iBGG >= 240 && iBGG < 250) { // black Queen
			Image image = new Image("/javachess/images/queenBlack.png");
			gc.drawImage(image, X, Y, 7.5 * P1X, 7.5 * P1Y);
		} else if (iBGG == 250) { // black king
			Image image = new Image("/javachess/images/kingBlack.png");
			gc.drawImage(image, X, Y, 7.5 * P1X, 7.5 * P1Y);
		}

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

		if(getOnlineHighlight() == false){
			gc.fillRect(T.getX() * P1X, T.getY() * P1Y, T.getW() * P1X, T.getH() * P1Y);}
		gc.setGlobalAlpha(1);

	}
	//-----------------------------------------------------------------------------------------------------------

	/**
	 * New method to highlight a moveable field
	 * @param tileNumber
	 */
	public void newHighlightMoveField(int tileNumber){

		Tile T= TileList.get(tileNumber);
		Color moveBlue = Color.rgb(164, 228, 234);
		Color secondMoveBlue= Color.rgb(27, 112, 118);

		if(getHighlighting() == true){

			gc.setStroke(getLinearGradient(secondMoveBlue, moveBlue, 1));
			gc.setLineWidth(2);
			gc.strokeRect(T.getX()*P1X, T.getY()*P1Y, T.getH()*P1X, T.getW()*P1Y);

		}

	}

	/**
	 * New Method for highlighting strike fields
	 * @param tileNumber
	 */
	public void newHighlightStrikeField(int tileNumber){
		Tile T= TileList.get(tileNumber);
		Color agressorRed = Color.rgb(187, 6, 6);
		Color secondAgressorRed = Color.rgb(249, 53, 53);

		if(getHighlighting() == true){

			if(getHighlightAnimations()==true){

				//insert Animatet codes here

			}else{

				gc.setStroke(getLinearGradient(agressorRed, secondAgressorRed, 1));
				gc.setLineWidth(2);
				gc.strokeRect(T.getX()*P1X, T.getY()*P1Y, T.getH()*P1X, T.getW()*P1Y);

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

					gc.setFill(getLinearGradient(birchBrownGrad, birchBrown, 0.9));				//org. 0.3
					//gc.setFill(birchBrown);													//use for DEMO w/o Effect
				}else{
					gc.setFill(getLinearGradient(lightBrownGrad, lightBrown, 0.9));
					//gc.setFill(lightBrown);													//use for DEMO w/o Effect
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
				/*
				try{
					if (BGG[OMove.getIPosX()][OMove.getIPosY()] == BGG[x][y] && bDrag) {
						iBGG = 0;
					} else {
						iBGG = BGG[x][y];
					}
				} catch(Exception e){
					iBGG = 0;
				}
				 */
				try {
					if (BGG[OMove.getIPosX()][OMove.getIPosY()] == BGG[x][y] && bDrag) {
						iBGG = 0;
					} else {
						iBGG = BGG[x][y];
					}

				}catch(Exception e) {
					iBGG = 0;
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
					Image image = new Image("/javachess/images/pawnWhite.png");
					gc.drawImage(image, XP, YP, 7.5 * P1X, 7.5 * P1Y);
				} else if (iBGG >= 110 && iBGG < 120) { // white Tower
					Image image = new Image("/javachess/images/towerWhite.png");
					gc.drawImage(image, XP, YP, 7.5 * P1X, 7.5 * P1Y);
				} else if (iBGG >= 120 && iBGG < 130) { // white Rider
					Image image = new Image("/javachess/images/jumperWhite.png");
					gc.drawImage(image, XP, YP, 7.5 * P1X, 7.5 * P1Y);
				} else if (iBGG >= 130 && iBGG < 140) { // white Runner
					Image image = new Image("/javachess/images/runnerWhite.png");
					gc.drawImage(image, XP, YP, 7.5 * P1X, 7.5 * P1Y);
				} else if (iBGG >= 140 && iBGG < 150) { // white Queen
					Image image = new Image("/javachess/images/queenWhite.png");
					gc.drawImage(image, XP, YP, 7.5 * P1X, 7.5 * P1Y);
				} else if (iBGG == 150) { // white king
					Image image = new Image("/javachess/images/kingWhite.png");
					gc.drawImage(image, XP, YP, 7.5 * P1X, 7.5 * P1Y);
				}

				/*
				 * black team
				 */
				if (iBGG < 210 && iBGG >= 200) { // black pawn
					Image image = new Image("/javachess/images/pawnBlack.png");
					gc.drawImage(image, XP, YP, 7.5 * P1X, 7.5 * P1Y);
				} else if (iBGG >= 210 && iBGG < 220) { // black Tower
					Image image = new Image("/javachess/images/towerBlack.png");
					gc.drawImage(image, XP, YP, 7.5 * P1X, 7.5 * P1Y);
				} else if (iBGG >= 220 && iBGG < 230) { // black Rider
					Image image = new Image("/javachess/images/jumperBlack.png");
					gc.drawImage(image, XP, YP, 7.5 * P1X, 7.5 * P1Y);
				} else if (iBGG >= 230 && iBGG < 240) { // black Runner
					Image image = new Image("/javachess/images/runnerBlack.png");  									
					gc.drawImage(image, XP, YP, 7.5 * P1X, 7.5 * P1Y);
				} else if (iBGG >= 240 && iBGG < 250) { // black Queen
					Image image = new Image("/javachess/images/queenBlack.png");
					gc.drawImage(image, XP, YP, 7.5 * P1X, 7.5 * P1Y);
				} else if (iBGG == 250) { // black king
					Image image = new Image("/javachess/images/kingBlack.png");
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
				DrawGrid(_BGG);
			} catch (Exception e) {
				e.printStackTrace();			}
		}

	}


	/**
	 * Method which  draws an infoscreen for hosting or disconnecting
	 * @param stopJob - threadjob to stop the connection
	 */
	public void drawBlurryMenu(hostingJob stopJob){

		P1X = (_X / 100);
		P1Y = (_Y / 100);


		System.out.println("drawing blurry menu");
		BoxBlur frostEffect = new BoxBlur(10, 10, 1000);
		gc.setEffect(frostEffect);									//cool effekts
		setBlurryButtonOn(true);
		bThinking=true;


		this.setOnMouseClicked(new EventHandler<MouseEvent>() {		//making the screen clickable

			@Override
			public void handle(MouseEvent event) {

				if(_blurryButtonOn == true && heartbeatMenu == true){		//determin wether disconnect or hosting

					bThinking=false;

					//---------------------------------------------------------------------

					//----------------------------------------------------------------------
					setHighlighting(true);
					setOnlineHighlight(false);
					setBlurryButtonOn(false);
					System.out.println("Restore Menus");
					_Gui.setChoose(0);											//doing nessacery stuff for disconnecting 
					_BGG2.setChoose(0);
					_Gui.getMenu().setSelect(0);
					_Gui.getMenu().menuFile.getItems().addAll(_Gui.getMenu().Load, _Gui.getMenu().Save, _Gui.getMenu().newGame);
					_Gui.getMenu().menuGame.getItems().addAll(_Gui.getMenu().GameMode0, _Gui.getMenu().GameMode1, _Gui.getMenu().GameMode2);
					_Gui.getMenu().menuOther.getItems().addAll(_Gui.getMenu().Draw);
					_Gui.getMenu().menuGame.getItems().removeAll(_Gui.getMenu().disconnect);
					heartBeatJob.setDisconnectInitiation(false);
					heartbeatMenu = false;

					if(_BGG2.getLan().getTeam() == true){
						try{
							System.out.println("killing");
							heartBeatJob.stopServSocket();
							stopJob.stopSocket();}
						catch(Exception e){
							System.out.println("STUPID EXCEPTION");
						}
					}
					_Gui.getStage().setResizable(true);


				}

				if(_blurryButtonOn == true && heartbeatMenu == false){

					//System.out.println("Mouse clicked" + "X:" + event.getX() + "  Y:" + event.getY() );
					System.out.println("Redrawing");
					bThinking=false;
					setHighlighting(true);
					setBlurryButtonOn(false);


					if(_BGG2.getLan().getIsConnectet()==true){					//stuff for hosting

						_Gui.getMenu().menuFile.getItems().removeAll(_Gui.getMenu().Load, _Gui.getMenu().Save, _Gui.getMenu().newGame, _Gui.getMenu().Draw);
						_Gui.getMenu().menuGame.getItems().removeAll(_Gui.getMenu().GameMode0, _Gui.getMenu().GameMode1, _Gui.getMenu().GameMode2);
						_Gui.getMenu().menuGame.getItems().addAll(_Gui.getMenu().disconnect);
					}
					if(_BGG2.getLan().getIsConnectet() == false){
						System.out.println("socket stopped");


						if(_BGG2.getLan().getTeam() == true){
							try{
								System.out.println("killing2");
								heartBeatJob.stopServSocket();
								stopJob.stopSocket();}
							catch(Exception e){

							}
						}
						setOnlineHighlight(false);
						_Gui.setChoose(0);
						_BGG2.setChoose(0);
						_Gui.getMenu().setSelect(0);
					}
				}

				_Gui.getStage().setResizable(true);


				try {
					DrawGrid(_BGG);
				} catch (Exception e) {

					System.out.println("EROOR DURING REDRAWING");

				}

			}
		});

		try {
			DrawGrid(_BGG);

			gc.setFill(Color.ANTIQUEWHITE);
			gc.setEffect(new DropShadow(10, Color.BLACK));			//drawing the menu finaly
			gc.fillRect(20*P1X, 35*P1Y, 60*P1X, 20*P1Y);
			gc.setEffect(null);
			gc.setFill(Color.BLACK);
			gc.setFont(new Font(2*P1X));

			if(!heartbeatMenu){
				InetAddress iadr = InetAddress.getLocalHost();					//text for hostingscreen
				String[] inetString = iadr.toString().split("/");
				gc.fillText("Waiting for connections on IP " + inetString[1], 50*P1X, 40*P1Y);
				gc.fillText("Click to abort and proceed in local mode!", 50*P1X, 48*P1Y);
				_Gui.getStage().setResizable(false);
			}
			else if(heartbeatMenu){

				if(heartBeatJob.getDisconnectInitiation()){						//text for heartbeatscreen
					gc.fillText("You disconnected!", 50*P1X, 40*P1Y);

				}else if(!heartBeatJob.getDisconnectInitiation()){
					gc.fillText("The other Player disconnected!", 50*P1X, 40*P1Y);

				}
				gc.fillText("Click to abort and proceed in local mode!",50*P1X, 48*P1Y);
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						_Gui.getStage().setResizable(false);

					}
				});

			}


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	/**
	 * This method draws the welcome screen at the beginning of the game
	 */
	public void drawStartMenu(){

		P1X = (_X / 100);
		P1Y = (_Y / 100);

		Color darkbrown=Color.web("#4D3322");
		Color lightBrown= Color.web("#8C603C");
		Color lightBrownGrad= Color.web("B78357");			//define colors
		Color birchBrown=Color.web("#D4AC7B");
		Color birchBrownGrad= Color.web("#E8D1B7");

		Image Icon = new Image("/javachess/images/JavaChess.png");

		//gc.drawImage(image, X, Y, 7.5 * P1X, 7.5 * P1Y);

		BoxBlur frostEffect = new BoxBlur(10, 10, 1000);
		gc.setEffect(frostEffect);


		bThinking=true;
		try{
			soundPlayer.playSound("startup");
		}catch(Exception ex){
			System.out.println("Startup Sound Failed");
		}


		//making the screen clickable
		this.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if(_Gui.getBoardGui().getStartupbuttonOn() == true){
					setHighlighting(true);

					setStartupbuttonOn(false);

					try {
						//soundPlayer.playSound("menu");
						DrawGrid(_BGG);									//hiding the menu again
						bThinking=false;
						_Gui.getStage().setResizable(true);
						_Gui.getRoot().setTop(_Gui.getMenu());

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});

		//StartUpScreen

		try {
			DrawGrid(_BGG);
		} catch (Exception e) {
			e.printStackTrace();
		}

		gc.setFill(Color.ANTIQUEWHITE);
		//gc.setEffect(null);
		gc.setEffect(new DropShadow(25, Color.BLACK));					//setting the startup Dropshadow
		gc.fillRect(10*P1X, 22*P1Y, 80*P1X, 56*P1Y);
		gc.setStroke(Color.BLACK);
		gc.setLineWidth(1.5);
		gc.setEffect(null);
		gc.strokeRect(10*P1X, 22*P1Y, 80*P1X, 56*P1Y);
		gc.setLineWidth(1);
		gc.setFill(getLinearGradient(lightBrown, lightBrownGrad, 0.9));
		gc.setEffect(null);
		gc.setFont(new Font(2.5*P1X*P1Y));
		gc.fillText("JavaChess", 65*P1X, 40*P1Y);
		gc.setFill(Color.BLACK);										//drawing this pretty pretty thing
		gc.strokeText("JavaChess", 65*P1X, 40*P1Y);
		gc.setFont(new Font(2.5*P1X));
		gc.setFill(Color.BLACK);
		gc.fillText("Click to start the Game", 63.5*P1X, 60*P1Y);
		gc.setLineWidth(1.2);
		gc.strokeRect(48.5*P1X, 56.5*P1Y, 30*P1X, 8*P1Y);
		gc.setLineWidth(1);
		gc.drawImage(Icon, 30, 100 ,50*P1X, P1Y*60);
		_Gui.getStage().setResizable(false);
		System.out.println("Startup finished");

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
	 * For the hold and klotz initiative
	 * @param Lauch
	 */
	public void setLaunchpad(Launchpad Lauch){
		_Lauch = Lauch;
	}


	/**
	 * if a Launchpad connects
	 * @param State - if Launchpad connects - true
	 */
	public void setBLauch(boolean State){
		_bLauch = State;
	}

	/**
	 * get if a Launchpad is connected
	 * @return
	 */
	public boolean getBLauch(){
		return _bLauch;

	}
	public boolean getStartupbuttonOn() {
		return startupbuttonOn;
	}

	public void setStartupbuttonOn(boolean startupbuttonOn) {
		this.startupbuttonOn = startupbuttonOn;
	}

	public boolean getOnlineHighlight() {
		return onlineHighlight;
	}

	public boolean setOnlineHighlight(boolean onlineHighlight) {
		this.onlineHighlight = onlineHighlight;
		return onlineHighlight;
	}

}
