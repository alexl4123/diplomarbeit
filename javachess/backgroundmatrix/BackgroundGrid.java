package javachess.backgroundmatrix;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import javachess.game.LAN;
import javachess.game.MovePos;
import javachess.launchpad.Launchpad;
import javachess.meeple.*;
import javachess.musik.SoundMachine;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

/**
 * @author alex - 2017
 * @version 1.1 - Draw
 * 
 * 
 *          The class BackgroundGrid is basically the ,,save-class'' So it
 *          implements Serializable (for save).
 * 
 *          It contains some important parts of the game logic.
 * 
 *          -On start the default position of the Meeples is added here -It
 *          contains the ,,iBackground[][]''. This is the representation of the
 *          position of the meeples. -Also the CheckMate Method (and also Draw calc) is located here
 * 
 *
 */
public class BackgroundGrid implements Serializable {

	/**
	 * The representation of the Meeples (contains the Numeric Value equivalent
	 * of the Meeples)
	 */
	public int[][] iBackground;

	/**
	 * A nother representation of the Meeples (contains the Meeple Objects)
	 */
	public ArrayList<Object> Objectives = new ArrayList<Object>(); // to save
																	// all the
																	// meeples
																	// (number
																	// of item =
	/**
	 * The current team // ID)
	 */
	boolean team; // to see what team is at the move

	/**
	 * if the white team is mated
	 */
	private boolean SchachmattWhite; // if the Whiteking is "schachmatt"
	
	/**
	 * if the black team is mated
	 */
	private boolean SchachmattBlack; //if the blackking is ''schachmatt''
	
	/**
	 * if a draw occurd (wether from Stalemate, agree, no check mate possible, 50. Moves without pawn move, capture or casteling or threefold position)
	 */
	private boolean _Draw;
	
	/**
	 * for the Draw evaluation
	 */
	
	/**
	 * How deep the AI should search
	 */
	private int _iAiDepth;
	
	

	
//----------------------------------------------------------------------------	
	
	public ArrayList<MovePos> _TotalMoveList;
	private ArrayList<int[][]> _AllBoardStatesList;
	private ArrayList<boolean[]> _AllTeamStatesList;
	
	boolean move; // when you can move
	short TurnRound; // to measure the turns of the current game
	int QueenNumber;// how many add. queens are
	int TowerNumber;// how many add. towers are
	int JumperNumber;// how many add. jumpers are
	int RunnerNumber;// how many add. runners are
	String name; // just for debugging
	int _Choose; // Which game mode is selected
	public LAN _Lan;
	private boolean _bAITeam;
	private boolean[] bPawnSpecMoved, bKingMoved, bTowerMoved;

	/**
	 * creates the default background grid
	 * 
	 * also sets the default "numbers" => which turn round it all starts; => how
	 * many additional meeples (threw "Bauerntausch") have been added.
	 * 
	 */
	public BackgroundGrid() {
		
		//some inits
		bPawnSpecMoved = new boolean[20];
		bKingMoved = new boolean[2];
		bTowerMoved = new boolean[4];
		
		
		
		QueenNumber = 0;
		TowerNumber = 0;
		JumperNumber = 0;
		RunnerNumber = 0;
		TurnRound = 0;
		move = true;
		_iAiDepth = 5;
		_bAITeam = false;
		
		team = true;
		_TotalMoveList = new ArrayList<MovePos>();
		_AllBoardStatesList = new ArrayList<int[][]>();
		_AllTeamStatesList = new ArrayList<boolean[]>();
		
		iBackground = new int[8][8];

		for (int i = 0; i < 300; i++) {
			Objectives.add(i);
		}
		
		//make the default board state
		
		for (int Y = 0; Y < 8; Y++) {
			for (int X = 0; X < 8; X++) {

				// Default start:

				// empty
				if ((Y >= 2) && (Y <= 5)) {
					iBackground[X][Y] = 0;

				} else if (Y == 1) {
					// team white
					iBackground[X][Y] = 101 + X; // 101 due to Move
					Farmer TheFarmer = new Farmer(true, iBackground[X][Y], X, Y);
					TheFarmer.setMeepleXPos(X);
					TheFarmer.setMeepleYPos(Y);
					Objectives.set(iBackground[X][Y], TheFarmer);

				} else if (Y == 6) {
					// team black
					iBackground[X][Y] = 200 + X;

					Farmer TheFarmer = new Farmer(false, iBackground[X][Y], X, Y);
					TheFarmer.setMeepleXPos(X);
					TheFarmer.setMeepleYPos(Y);
					Objectives.set(iBackground[X][Y], TheFarmer);

				}
			}
		}
		// team white
		iBackground[0][0] = 110;// tower 1
		Tower TheTower = new Tower(true, iBackground[0][0], 0, 0);
		TheTower.setMeepleXPos(0);
		TheTower.setMeepleYPos(0);
		Objectives.set(iBackground[0][0], TheTower);
		iBackground[7][0] = 111;// tower 2
		TheTower = new Tower(true, iBackground[7][0], 7, 0);
		TheTower.setMeepleXPos(7);
		TheTower.setMeepleYPos(0);
		Objectives.set(iBackground[7][0], TheTower);

		iBackground[1][0] = 120; // rider 1
		Jumper Drogo = new Jumper(true, iBackground[1][0], 1, 0);
		Drogo.setMeepleXPos(1);
		Drogo.setMeepleYPos(0);
		Objectives.set(iBackground[1][0], Drogo);

		iBackground[6][0] = 121; // rider 2
		Drogo = new Jumper(true, iBackground[6][0], 6, 0);
		Drogo.setMeepleXPos(6);
		Drogo.setMeepleYPos(0);
		Objectives.set(iBackground[6][0], Drogo);

		iBackground[2][0] = 130; // runner 1
		Runner TheRunner = new Runner(true, iBackground[2][0], 2, 0);
		TheRunner.setMeepleXPos(2);
		TheRunner.setMeepleYPos(0);
		Objectives.set(iBackground[2][0], TheRunner);
		iBackground[5][0] = 131; // runner 2
		TheRunner = new Runner(true, iBackground[5][0], 5, 0);
		TheRunner.setMeepleXPos(5);
		TheRunner.setMeepleYPos(0);
		Objectives.set(iBackground[5][0], TheRunner);

		iBackground[3][0] = 140; // Queen
		Queen khaleesi = new Queen(true, iBackground[3][0], 3, 0);
		khaleesi.setMeepleXPos(3);
		khaleesi.setMeepleYPos(0);
		Objectives.set(iBackground[3][0], khaleesi);

		iBackground[4][0] = 150; // King
		King TheKingWhite = new King(true, 150, 4, 0);
		TheKingWhite.setMeepleXPos(4);
		TheKingWhite.setMeepleYPos(0); // only for the Kings
		Objectives.set(150, TheKingWhite);

		// team black
		// same for the other team except king and queen changed X-Pos+
		iBackground[0][7] = 210; // tower 1
		TheTower = new Tower(false, iBackground[0][7], 0, 7);
		TheTower.setMeepleXPos(0);
		TheTower.setMeepleYPos(7);
		Objectives.set(iBackground[0][7], TheTower);
		iBackground[7][7] = 211; // tower 2
		TheTower = new Tower(false, iBackground[7][7], 7, 7);
		TheTower.setMeepleXPos(7);
		TheTower.setMeepleYPos(7);
		Objectives.set(iBackground[7][7], TheTower);

		iBackground[1][7] = 220; // rider 1
		Drogo = new Jumper(false, iBackground[1][7], 1, 7);
		Drogo.setMeepleXPos(1);
		Drogo.setMeepleYPos(7);
		Objectives.set(iBackground[1][7], Drogo);

		iBackground[6][7] = 221; // rider 2
		Drogo = new Jumper(false, iBackground[6][7], 6, 7);
		Drogo.setMeepleXPos(6);
		Drogo.setMeepleYPos(7);
		Objectives.set(iBackground[6][7], Drogo);

		iBackground[2][7] = 230; // runner 1
		TheRunner = new Runner(false, iBackground[2][7], 2, 7);
		TheRunner.setMeepleXPos(2);
		TheRunner.setMeepleYPos(7);
		Objectives.set(iBackground[2][7], TheRunner);
		iBackground[5][7] = 231; // runner 2
		TheRunner = new Runner(false, iBackground[5][7], 5, 7);
		TheRunner.setMeepleXPos(5);
		TheRunner.setMeepleYPos(7);
		Objectives.set(iBackground[5][7], TheRunner);

		iBackground[3][7] = 240; // queen
		khaleesi = new Queen(false, iBackground[3][7], 3, 7);
		khaleesi.setMeepleXPos(3);
		khaleesi.setMeepleYPos(7);
		Objectives.set(iBackground[3][7], khaleesi);

		iBackground[4][7] = 250; // king
		King TheKingBlack = new King(false, iBackground[4][7], 4, 7);
		TheKingBlack.setMeepleXPos(4);
		TheKingBlack.setMeepleYPos(7); // only for the Kings
		Objectives.set(iBackground[4][7], TheKingBlack);
		
		_Lan=new LAN(iBackground, this);

	}

	/**
	 * Returns the current Turn
	 * 
	 * @return TurnRound - short - current turn
	 */
	public short getTurnRound() {
		return TurnRound;
	}

	/**
	 * Highers the current turn by 1
	 */
	public void higherTurnRound() {
		TurnRound++;
	}

	/**
	 * Returns the Object, that you pressed on the button
	 * 
	 * @param Back
	 *            - int number for returning the Object at the destination
	 * @return Object - returns the object of the ArrayList at the given
	 *         destination
	 */
	public Object Objects(int Back) {
		return Objectives.get(Back);
	}

	/**
	 * if move is valid or not
	 * 
	 * @return this.move - boolean - true if a move is valid (no more in use)
	 */
	public boolean getMove() {
		return this.move;
	}

	

	/**
	 * set if the player can move or not
	 * 
	 * @param temp
	 *            - boolean - to set if a player can move
	 */
	public void setMove(boolean temp) {
		this.move = temp;
	}

	

	/**
	 * only for debugging
	 * 
	 * @return name - String - for debugging only
	 */
	public String getName() {
		return name;
	}

	/**
	 * only for debugging
	 * 
	 * @param n
	 *            - String - for debugging only
	 */
	public void setName(String n) {
		this.name = n;
	}

	/**
	 * if a "bauerntausch" occurs and a queen is selected this will be increased
	 */
	public void higherQueenNumber() {
		this.QueenNumber++;
	}

	/**
	 * for "bauerntausch"
	 * 
	 * @return QueenNumber - int - returns the current number of queens
	 */
	public int getQueenNumber() {
		return QueenNumber;
	}

	/**
	 * if a "bauerntausch" occurs and a jumper is selected this will be
	 * increased
	 */
	public void higherJumperNumber() {
		this.JumperNumber++;
	}

	/**
	 * for "bauerntausch"
	 * 
	 * @return JumperNumber - int - returns the current number of Jumpers
	 *         (Knights)
	 */
	public int getJumperNumber() {
		return JumperNumber;
	}

	/**
	 * if a "bauerntausch" occurs and a runnner (bishop) is selected this will
	 * be increased
	 */
	public void higherRunnerNumber() {
		this.RunnerNumber++;
	}

	/**
	 * for "bauerntausch"
	 * 
	 * @return RunnerNumber - int - returns the current number of Runners
	 *         (bishops)
	 */
	public int getRunnerNumber() {
		return RunnerNumber;
	}

	/**
	 * if a "bauerntausch" occurs and a tower (rook) is selected this will be
	 * increased
	 */
	public void higherTowerNumber() {
		this.TowerNumber++;
	}

	/**
	 * for "bauerntausch"
	 * 
	 * @return TowerNumber - int - returns the current number of Towers (rooks)
	 */
	public int getTowerNumber() {
		return TowerNumber;
	}

	/**
	 * returns the int value of the BackgroundGrid at a given location
	 * 
	 * @param X-XPos
	 *            - int - 0-7
	 * @param Y-YPos
	 *            - int - 0-7
	 * @return iBackground-int - Numeric representation of the position
	 */
	public int getBackgroundGrid(int X, int Y) {
		return iBackground[X][Y];
	}

	/**
	 * Sets a position on the background grid for instance: if a stike occurs,
	 * the striked field will be overwritten
	 * 
	 * @param X-XPos
	 *            - int - 0 - 7
	 * @param Y-YPos
	 *            - int - 0 - 8
	 * @param iBG-new
	 *            - int - what number Number of BackgroundGrid
	 */
	public void setBackgroundGrid(int X, int Y, int iBG) {

		iBackground[X][Y] = iBG;
	}

	int X;
	int Y;
	Object OBJ;

	/**
	 * sets the X and Y pos from the previous button press (currently not used)
	 * 
	 * @param X1
	 *            - int - prev. button press
	 * @param Y1
	 *            - int - prev. button press
	 */
	public void setXY(int X1, int Y1) {

		X = X1;
		Y = Y1;
	}

	/**
	 * Sets the selected Meeple For instance if you click on a knight, this sets
	 * the knight (currently not used)
	 * 
	 * @param Ob
	 *            - Object - which object to set as selected (for moving)
	 */
	public void setObject(Object Ob) {
		OBJ = Ob;
	}

	/**
	 * returns the X of the previous turn (currently not used)
	 * 
	 * @return X - int - selected X Position
	 */
	public int getX() {

		return X;
	}

	/**
	 * returns the Y of the previous turn (currently not used)
	 * 
	 * @return Y - int - returns the selected Y pos
	 */
	public int getY() {
		return Y;
	}

	/**
	 * returns the object of the previous selected item (currently not used)
	 * 
	 * @return OBJ - Object - the selected Meeple
	 */
	public Object getObject() {
		return OBJ;
	}

	/**
	 * gets the current team
	 * 
	 * @return this.team - boolean - the current team
	 */
	public boolean getTeam() {
		return this.team;
	}

	/**
	 * changes the current team True to false false to true
	 */
	public void changeTeam() {

		if (team == true) {
			team = false;
		} else {
			team = true;
		}

	}

	/**
	 * Only used for SchachKing (= checkking) The ID of the King attacker and the location of the attacker
	 */
	private int _ID, _iX, _iY;
	
	

	/**
	 * Only used for SchachKing (= checkking) The number of attackers
	 */
	private int iDanger;

	/**
	 * checks if the king is in "Schach". If so, then it checks if the king is
	 * "SchachMatt".
	 * 
	 * @param team-for
	 *            which team the check
	 * @param BGG-BackgroundGrid
	 *            of the BoardGui
	 * @param KingX-on
	 *            which position the check should be made
	 * @param KingY-on
	 *            which position the check should be made
	 * @param SchachMatt-if
	 *            the "SchachMatt"-check should be made
	 * @param bSimKingOnTile
	 *            - if there should be a King simulated at KingX & KingY (used
	 *            for the moves)
	 * @return Schach-returns if king is in check
	 */
	public boolean SchachKing(boolean team, BackgroundGrid BGG, int KingX, int KingY, boolean SchachMatt,
			boolean bSimKingOnTile) {
		
		
		SchachmattWhite= false;
		SchachmattBlack= false;
		
		if(KingX <0 || KingX >= 8 || KingY < 0 || KingY >= 8){
			return false;
		}
		
		
		int iID;
		boolean Schach = false;
		if (KingX <= 7 && KingY <= 7) {
			iID = BGG.iBackground[KingX][KingY];
		} else {
			iID = 0;
		}

		if (bSimKingOnTile && team) {
			iID = 150;
		} else if (bSimKingOnTile && !team) {
			iID = 250;
		}
		
		//System.out.println("team:" + team + ":KingX:" + KingX + ":KingY:" + KingY + ":iID:" + iID);
		//System.out.println(iID + ":IID:");
		switch (iID) {
		case 150: {
			Schach = Schach(BGG.iBackground, KingX, KingY, team);
		}
			break;
		case 250: {
			Schach = Schach(BGG.iBackground, KingX, KingY, team);
		}
			break;
		default: {
			Schach = false;
		}
			break;
		}
		
		if(!SchachMatt && !Schach){
			_Draw = CalcDraw(iID, BGG.iBackground, KingX, KingY, team, BGG);
		}

		if (Schach && !SchachMatt) {
			
			SchachmattWhite = SchachMatt(iID, BGG.iBackground, KingX, KingY, team, BGG);
			System.out.println("Schach:" + Schach + ":SchachmattW" + SchachmattWhite);
			if(SchachmattWhite){
				
				if (team) {
					
					 Platform.runLater(new Runnable() {

							@Override
							public void run() {
								try {
									 Alert alert = new Alert(AlertType.INFORMATION);
									  alert.setTitle("Check Mate");
									  alert.setHeaderText("White has lost the game, black takes it all!");
									  alert.showAndWait();

								} catch (Exception ex) {
									ex.printStackTrace();
								}

							}
						});
					
					  SchachmattBlack =false;
					  SchachmattWhite = true;
					  
					  } else {
					  SchachmattBlack = true;
					  SchachmattWhite = false;
					  
					  Platform.runLater(new Runnable() {

							@Override
							public void run() {
								try {
									Alert alert = new Alert(AlertType.INFORMATION);
									  alert.setTitle("Check Mate");
									  alert.setHeaderText("Black has lost the game, white takes it all!");
									  alert.showAndWait();

								} catch (Exception ex) {
									ex.printStackTrace();
								}

							}
						});
					  
					  
					  
					  } 
				
			}
		}
		
		

		return Schach;
	}

	/**
	 * Checks if a king is in check.
	 * @param i - ID of the King
	 * @param iBackground - current state of the board
	 * @param KingX - X-Location where the king is
	 * @param KingY - Y-Location where the king is
	 * @param Team - the current team
	 * @return If a king is checked - return true
	 */
	private boolean Schach(int[][] iBackground, int KingX, int KingY, boolean Team) {

		for (int Y = 0; Y < 8; Y++) {
			for (int X = 0; X < 8; X++) {
				
				int iBack = iBackground[X][Y];
				
				if ((iBack >= 200 && iBack < 210 && Team) || (iBack >= 100 && iBack < 110 && !Team)) {
					if (KingY == Y - 1 && Team) {
						if (KingX == X - 1 || KingX == X + 1) {
							_ID = iBack; _iX = X; _iY = Y;
							return true;
						}
					} else if (KingY == Y + 1 && !Team) {
						if (KingX == X - 1 || KingX == X + 1) {
							_ID = iBack; _iX = X; _iY = Y;
							return true;
						}
					}
				} else if ((iBack >= 210 && iBack < 220 && Team) || (iBack >= 110 && iBack < 120 && !Team)) {
					// ROOK-----------------------------------------------------------
					//
					if (KingY == Y) {
						int dX = Math.abs(KingX - X);
						int SumOfField = 0;
						if (dX == 1) {
							_ID = iBack; _iX = X; _iY = Y;
							return true;
						}
						for (int x = 1; x < dX; x++) {
							if (KingX < x && X - x >= 0) {
								SumOfField += iBackground[X - x][Y];
							} else if (KingX > x && X + x < 8) {
								SumOfField += iBackground[x + X][Y];
							}

						}

						if (SumOfField == 0) {
							_ID = iBack; _iX = X; _iY = Y;
							return true;
						}

					} else if (KingX == X) {
						int dY = Math.abs(KingY - Y);
						int SumOfField = 0;

						if (dY == 1) {
							_ID = iBack; _iX = X; _iY = Y;
							return true;
						}
						for (int y = 1; y < dY; y++) {
							if (KingY < Y && Y - y >= 0) {
								SumOfField += iBackground[X][Y - y];
							} else if (KingY > Y && Y + y < 8) {
								SumOfField += iBackground[X][Y + y];
							}

						}

						if (SumOfField == 0) {
							_ID = iBack; _iX = X; _iY = Y;
							return true;
						}

					}
				} else if ((iBack >= 220 && iBack < 230 && Team) || (iBack >= 120 && iBack < 130 && !Team)) {
					if (((KingX == X - 2) || (KingX == X + 2)) && ((KingY == Y - 1) || (KingY == Y + 1))) {
						_ID = iBack; _iX = X; _iY = Y;
						return true;
					} else if (((KingX == X - 1) || (KingX == X + 1)) && ((KingY == Y - 2) || (KingY == Y + 2))) {
						_ID = iBack; _iX = X; _iY = Y;
						return true;
					}

				} else if ((iBack >= 230 && iBack < 240 && Team) || (iBack >= 130 && iBack < 140 && !Team)) {
					// bishop/runner-------------------------------------------------------------
					float dX, dY, dD;
					dX = Math.abs(X - KingX);
					dY = Math.abs(Y - KingY); 
					dD = dY / dX;
					int SumOfField = 0;

					if (dD == 1) {
						if (dX == 1) {
							_ID = iBack; _iX = X; _iY = Y;
							return true;
						}

						for (int iHelp = 1; iHelp < dX; iHelp++) {

							if (X < KingX && Y > KingY && X + iHelp < 8 && Y - iHelp >= 0) {
								SumOfField += iBackground[X + iHelp][Y - iHelp];
							} else if (X < KingX && Y < KingY && X + iHelp < 8 && Y + iHelp < 8) {
								SumOfField += iBackground[X + iHelp][Y + iHelp];
							} else if (X > KingX && Y > KingY && X - iHelp >= 0 && Y - iHelp >= 0) {
								SumOfField += iBackground[X - iHelp][Y - iHelp];
							} else if (X > KingX && Y < KingY && X - iHelp >= 0 && Y + iHelp < 8) {
								SumOfField += iBackground[X - iHelp][Y + iHelp];
							}

						}
						if (SumOfField == 0) {
							_ID = iBack; _iX = X; _iY = Y;
							return true;
						}
					}
				} else if ((iBack >= 240 && iBack < 250 && Team) || (iBack >= 140 && iBack < 150 && !Team)) {
					// ----------------------------------------------------------------------------
					float dX, dY, dD;
					dX = Math.abs(X - KingX);
					dY = Math.abs(Y - KingY);
					dD = dY / dX;
					int SumOfField = 0;
					//System.out.println("733:" + iBack + ":dD:" + dD + ":dX:" + dX + ":dY:" + dY);
					if(dD == Float.POSITIVE_INFINITY){
						
						dD = 2;
					}
					//System.out.println("731:" + Team + ":dD:" + dD + ":dX:" + dX + ":dY:" + dY + ":X:" + X + ":Y:" + Y + ":KingX:" + KingX + ":KingY:" + KingY);
					if (dD == 1) {
						
						if (dX == 1) {
							_ID = iBack; _iX = X; _iY = Y;
							return true;
						}
						for (int iHelp = 1; iHelp < dX; iHelp++) {

							if (X < KingX && Y > KingY && X + iHelp < 8 && Y - iHelp >= 0) {
								SumOfField += iBackground[X + iHelp][Y - iHelp];
							} else if (X < KingX && Y < KingY && X + iHelp < 8 && Y + iHelp < 8) {
								SumOfField += iBackground[X + iHelp][Y + iHelp];
							} else if (X > KingX && Y > KingY && X - iHelp >= 0 && Y - iHelp >= 0) {
								SumOfField += iBackground[X - iHelp][Y - iHelp];
							} else if (X > KingX && Y < KingY && X - iHelp >= 0 && Y + iHelp < 8) {
								SumOfField += iBackground[X - iHelp][Y + iHelp];
							}

						}

						if (SumOfField == 0) {
							_ID = iBack; _iX = X; _iY = Y;
							return true;
						}
					} else if (KingY == Y) {
						dX = Math.abs(KingX - X);
						SumOfField = 0;
						
						if (dX == 1) {
							_ID = iBack; _iX = X; _iY = Y;
							
							return true;
						}
						for (int x = 1; x < dX; x++) {
							if (KingX < X && (X - x) >= 0) {
								SumOfField += iBackground[X - x][Y];

							} else if (KingX > X && (x + X < 8)) {
								SumOfField += iBackground[x + X][Y];
							}

							
						}

						if (SumOfField == 0) {
							_ID = iBack; _iX = X; _iY = Y;
							return true;
						}

					} else if (KingX == X) {
						dY = Math.abs(KingY - Y);
						SumOfField = 0;

						if (dY == 1) {
							_ID = iBack; _iX = X; _iY = Y;
							return true;
						}
						for (int y = 1; y < dY; y++) {
							if (KingY < Y && (Y - y) >= 0) {
								SumOfField += iBackground[X][Y - y];
							} else if (KingY > Y && Y + y < 8) {
								SumOfField += iBackground[X][Y + y];
							}

						
						}

						if (SumOfField == 0) {
							_ID = iBack; _iX = X; _iY = Y;
							return true;
						}

					}
				} else if ((iBack == 250 && Team) || (iBack == 150 && !Team)) {
					int dX = Math.abs(KingX - X);
					int dY = Math.abs(KingY - Y);

					if (dX < 2 && dY < 2) {
						_ID = iBack; _iX = X; _iY = Y;
						return true;
					}
				}

			}
		}

		return false;
	}

	/**
	 * If a King is in check, 
	 * let�s look if he is mated
	 * @param iID - ID of the king
	 * @param iBackground - current state of the board
	 * @param KingX - X-Location where the king is
	 * @param KingY - Y-Location where the king is
	 * @param Team - the current team 
	 * @return If a King is mated (true) or the game continues (false)
	 */
	private boolean SchachMatt(int iID, int[][] iBackground, int KingX, int KingY, boolean Team, BackgroundGrid BGG) {
		
		
		
		for (int Y = -1; Y <= 1; Y++) {
			for (int X = -1; X <= 1; X++) {
				if(KingX+X >= 0 && KingX+X <8 && KingY+Y >= 0 && KingY+Y < 8){ //Due to bugs
					BackgroundGrid BGG2 = new BackgroundGrid();
					BGG2.iBackground = iBackground;
					int dBack = Math.abs(iBackground[KingX+X][KingY+Y] - iID); //If its friend or foe or field
					int iDBackup = iBackground[KingX+X][KingY+Y];
					BGG2.iBackground[KingX][KingY] = 0;
					BGG2.iBackground[KingX+X][KingY + Y] = iID;
					if(!BGG2.SchachKing(Team, BGG, KingX+X, KingY+Y, true, false) && dBack > 50){ // when not Schach and it is a Field or foe - not mated
						BGG2.iBackground[KingX][KingY] = iID;
						BGG2.iBackground[KingX+X][KingY + Y] =iDBackup;
						return false;
					}
					BGG2.iBackground[KingX][KingY] = iID;
					BGG2.iBackground[KingX+X][KingY + Y] =iDBackup;
				}
				
			}

		}
		
		Move Moves = new Move();
		Moves.setBGG(iBackground);
		Moves.setBGG2(BGG);
		//Moves.GetMove(iID, KingX, KingY, BGG);
		Moves.setBSelect(false);
		System.out.println("line 908:");
		
		int iYA, iXA;
		
		/*if(Schach(iBackground, _iX, _iY, !Team)){
			System.out.println("line 913");
			return false;
		}*/
		int iBB = iBackground[_iX][_iY];
		ArrayList<MovePos> AttackMoves = Moves.getMoveMeeple(iBackground, !Team, iBB, _iX, _iY);
		MovePos MPSelf = new MovePos();
		MPSelf.ID = iBB;
		MPSelf.X = _iX;
		MPSelf.Y = _iY;
		MPSelf.PX = _iX;
		MPSelf.PY = _iY;
		AttackMoves.add(MPSelf);
		for(MovePos MP : AttackMoves){
			System.out.println(AttackMoves.size() + "::Attackmoves::" + iBB + ":MPA.ID:" + MP.ID + "::X::"+MP.X+"::XP::"+MP.PX + ":Y:" + MP.Y + "::PY::" + MP.PY);
			for(iYA = 0; iYA < 8; iYA ++){
				for(iXA = 0; iXA < 8; iXA ++){
					int iBack = iBackground[iXA][iYA];
					ArrayList<MovePos> DefenseMoves = Moves.getMoveMeeple(iBackground, Team, iBack, iXA, iYA);
					
				
					for(MovePos MPA : DefenseMoves){
						if(MP.PX == MPA.PX && MP.PY == MPA.PY){
							System.out.println(MP.PX + "::" + MPA.PX + "::" + MP.PY + "::" + MPA.PY+"::"+MPA.ID);
							//Make the possible defense move
							iBackground[MPA.PX][MPA.PY] = MPA.ID;
							iBackground[MPA.X][MPA.Y] = 0;
							
							//Give out the current state of the game for debug
							for(int itestY = 0; itestY < 8; itestY++) {
								for(int itestX = 0; itestX < 8; itestX++) {
									System.out.print(":"+Board[itestX][itestY]+":");
								}
								System.out.println("");
							}
							
							//get the king positition
							
							
							System.out.println("KingX::"+KingX+"::KingY::"+KingY);
							System.out.println("SchachKing:" + SchachKing(Team, BGG, KingX, KingY, true, false) + "::Team::" + Team + "::MPA.PX::" + MPA.PX + "::MPA.PY::" + MPA.PY);
							if(!SchachKing(Team, BGG, KingX, KingY, true, false) && !Schach(Board, KingX, KingY, Team)){
								//System.out.println(MPA.PX + "::" + MPA.PY + "::Funkt");
								iBackground[MPA.PX][MPA.PY] = MPA.ID2;
								iBackground[MPA.X][MPA.Y] = MPA.ID;
								System.out.println("line 941");
								return false;
							}
							iBackground[MPA.PX][MPA.PY] = MPA.ID2;
							iBackground[MPA.X][MPA.Y] = MPA.ID;
							
						}
					}
				}
			}
		}
		
		for(int iHelp = 0; iHelp < Moves.getMoveList().size(); iHelp++){
			int[] IDAR = Moves.getMoveList().get(iHelp);
			int IDA = IDAR[0];
			
			if(IDA < 8)
			{
				iYA = 0;
			} else if(IDA >= 8 && IDA < 16){
				iYA = 1;
			} else if(IDA >= 16 && IDA < 24){
				iYA = 2;
			}else if(IDA >= 24 && IDA < 32){
				iYA = 3;
			}else if(IDA >= 32 && IDA < 40){
				iYA = 4;
			}else if(IDA >= 40 && IDA < 48){
				iYA = 5;
			} else if(IDA >= 48 && IDA <56){
				iYA = 6;
			}else{
				iYA = 7;
			}
			
			iXA = IDA - (iYA * 8);
			
			//System.out.println(iXA + ":: " + iYA);
			
			if(Schach(iBackground, _iX, _iY, !Team)){
				System.out.println("line 981");
				return false;
			}
			
			
		}
		

		return true;
	}
	
	/**
	 * Calculates if a Draw has occurd,
	 * actually calcs:
	 * -Stalemate
	 * -One side has no chance anymore
	 * -50 moves without a castling, pawn move or hit
	 * -3 times the same position
	 * @param iID - ID of the king
	 * @param iBackground - the Board representation
	 * @param KingX - X-Pos of the King
	 * @param KingY - Y-Pos of the King
	 * @param team - current team (true - white | false - black)
	 * @param BGG - BackgroundgGrid
	 * @return boolean - if true then it is a Draw
	 */
	private boolean CalcDraw(int iID, int[][] iBackground, int KingX, int KingY, boolean team, BackgroundGrid BGG){
		
		
		boolean Draw = false;
		int iSum1 = 0; //Stalemate
		int iSum2 = 0;
		for(int Y = 0; Y < 8; Y++){
			for(int X = 0; X < 8; X++){
				if((iBackground[X][Y] >= 100 && iBackground[X][Y] < 160 && team) || (iBackground[X][Y] >= 200 && iBackground[X][Y] < 260 && !team)){
					iSum1 += iBackground[X][Y];
				}
				
				if((iBackground[X][Y] >= 100 && iBackground[X][Y] < 160 && !team) || (iBackground[X][Y] >= 200 && iBackground[X][Y] < 260 && team)){
					iSum2 += iBackground[X][Y];
				}
				
				
			}
		}
		
		if((iSum1 == 150 && team) || (iSum1 == 250 && !team)){
			Move M = new Move();
			M.setBGG(iBackground);
			M.setBGG2(BGG);
			M.GetMove(iID, KingX, KingY, BGG);
			if(M.getMoveList().size() == 0){
				Draw = true;
				Platform.runLater(new Runnable() {
					
					@Override
					public void run() {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setHeaderText("Draw");
						alert.setContentText("A stalemate has caused a draw, no one won!");
						alert.setTitle("Draw");
						alert.showAndWait();
					}
				});
			}
		} else if((iSum1 >= 270 && iSum1 < 290 && iSum2 == 250 && team) || (iSum1 >= 470 && iSum1 < 490 && iSum2 == 150 && !team)){
			Platform.runLater(new Runnable() {
				
				@Override
				public void run() {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setHeaderText("Draw");
					alert.setContentText("The game cannot be continued due to the impossible of check mate.");
					alert.setTitle("Draw");
					alert.showAndWait();
				}
			});
		}
		
		
		if(_TotalMoveList.size()>=50){
			System.out.println(">= 50");
			int iCounter = 0;
			for(int iHelp = 49; iHelp >= 0; iHelp -= 1){
				MovePos MP = _TotalMoveList.get(_TotalMoveList.size()-iHelp);
				
				if((MP.ID >= 100 && MP.ID < 110) || (MP.ID >= 200 && MP.ID < 210) || MP.ID2 != 0 || MP.ID4 != 0){
					iCounter = 0;
					break;
				}else {
					iCounter++;
				}
			}
			
			if(iCounter >= 50){
				Draw = true;
			}
		}
		
		if(_TotalMoveList.size() >=6){
			int icount = 0;
			for(MovePos MP1 : _TotalMoveList){
				icount = 0;
				for(MovePos MP2 : _TotalMoveList){
					if(java.util.Arrays.deepEquals(MP1.Board, MP2.Board)){
						System.out.println(MP1.ID + "::" + MP2.ID);
						icount++;
						if(icount > 3){
							return true;
						}
					}
					
					
				}
			}
		}
		
		return Draw;
	}

	/**
	 * 
	 * @param Schachmatt-to
	 *            set SchachMatt for the white team
	 */
	public void setSchachmattWhite(boolean Schachmatt) {
		this.SchachmattWhite = Schachmatt;
	}

	/**
	 * 
	 * @return-get SchachMatt for white team
	 */
	public boolean getSchachmattWhite() {
		return SchachmattWhite;
	}

	/**
	 * 
	 * @param Schachmatt - set Schachmatt for the black team
	 */
	public void setSchachmattBlack(boolean Schachmatt){
		this.SchachmattBlack = Schachmatt;
	}
	
	/**
	 * @return get Schachmatt for Black team
	 */
	public boolean getSchachmattBlack(){
		return SchachmattBlack;
	}

	/**
	 * @param team
	 *            to set
	 */
	public void setTeam(boolean team2) {
		team = team2;
	}

	/**
	 * Sets the current game mode
	 * 
	 * @param i
	 *            - Game modes
	 */
	public void setChoose(int i) {
		_Choose = i;
	}

	/**
	 * Gets the current game mode
	 * 
	 * @return _Choose - The current game mode
	 */
	public int getChoose() {
		return _Choose;
	}
	
	/**
	 * Set if the king has been moved
	 * @param iID - to identify which king
	 */
	public void setbKingMoved(int iID, boolean Moved){
		switch(iID){
		case 150: bKingMoved[0] = Moved; break;
		case 250: bKingMoved[1] = Moved; break;
		}
	}
	
	/**
	 * Get if a King has been moved
	 * @param iID - to identify the king
	 * @return boolean - if a king has been moved
	 */
	public boolean getbKingMoved(int iID){
		switch(iID){
		case 150: return bKingMoved[0];
		case 250: return bKingMoved[1];
		}
		return true;
	}
	
	/**
	 * Set if a Rook (tower) has been moved
	 * @param iID - to identify the rook / tower
	 */
	public void setbRookMoved(int iID, boolean Moved){
		switch(iID){
		case 110: bTowerMoved[0] = Moved; break;
		case 111: bTowerMoved[1] = Moved; break;
		case 210: bTowerMoved[2] = Moved; break;
		case 211: bTowerMoved[3] = Moved; break;
		}
	}
	
	/**
	 * Get if a tower has been moved
	 * @param iID - to identify the rook / tower
	 * @return boolean - if a rook has been moved
	 */
	public boolean getbRookMoved(int iID){
		switch(iID){
		case 110: return bTowerMoved[0];
		case 111: return bTowerMoved[1];
		case 210: return bTowerMoved[2];
		case 211: return bTowerMoved[3];
		}
		
		return true;
	}
	
	/**
	 * To lower queen number (for AI)
	 */
	public void lowerQueenNumber(){
		QueenNumber -= 1;
	}
	
	/**
	 * 
	 * @return Returns true if the white king is mated (end of game)
	 */
	public boolean getSchachMattWhite(){
		return SchachmattWhite;
	}
	
	/**
	 * 
	 * @return Returns true if the black king is mated (end of game)
	 */
	public boolean getSchachMattBlack(){
		return SchachmattBlack;
	}
	
	/**
	 * 
	 * @return returns true if a Draw has occurd (end of game)
	 */
	public boolean getDraw(){
		return _Draw;
	}
	
	/**
	 * 
	 * @param Draw - to set if a Draw has occurd
	 */
	public void setDraw(boolean Draw){
		_Draw = Draw;
	}
	
	/**
	 * add a new move to the MoveList
	 * @param MP - MovePos object - will be added to the MoveList
	 */
	public void addMoveListItem(MovePos MP){
		_TotalMoveList.add(MP);
	}
	
	/**
	 * Remove last Item in the Move List
	 */
	public void removeLastMoveListItem(){
		if(_TotalMoveList.size()-1 > 0){
			_TotalMoveList.remove((_TotalMoveList.size()-1));
		}
		
	}
	
	/**
	 * get a item from the Move List
	 * @param i - which item
	 * @return - MovePos object
	 */
	public MovePos getMovelistItem(int i){
		if(i >= 0 && i < _TotalMoveList.size()){
			return _TotalMoveList.get(i);
		}
		
		return null;
		
	}
	
	

	/**
	 * I think i need this for the Move class...
	 */
	public int[][] Board;
	
	
	public LAN getLan(){
		return _Lan;
	}
	
	/**
	 * 
	 * @param iAiDepth-To which depth the AI calculation should be made
	 */
	public void setAiDepth(int iAiDepth){
		_iAiDepth = iAiDepth;
	}
	
	/**
	 * returns the current value for the AI Depth calculation
	 * @return - int - AiDepth value
	 */
	public int getAiDepth(){
		return _iAiDepth;
	}
	
	/**
	 * add a board state to the list
	 * @param Board - int[][]
	 */
	public void addBoardState(int[][] Board){
		int[][] iBoard = Board;
		_AllBoardStatesList.add(iBoard);
	}
	
	/**
	 * returns the list with all the possible moves
	 * @return - ArrayList[][]
	 */
	public ArrayList<int[][]> getBoardList(){
		return _AllBoardStatesList;
	}
	
	/**
	 * Adds a team state
	 * @param team - boolean
	 */
	public void addTeamState(boolean team){
		boolean[] teamL  = new boolean[1];
		teamL[0] = team;
		_AllTeamStatesList.add(teamL);
	}
	
	/**
	 * return the list of all the team states
	 * @return - ArrayList<boolean[]> - boolean[0] = team
	 */
	public ArrayList<boolean[]> getTeamList(){
		return _AllTeamStatesList;
	}
	
	public void setAITeam(boolean AITeam) {
		_bAITeam = AITeam;
	}
	
	public boolean getAITeam() {
		return _bAITeam;
	}
	
}
