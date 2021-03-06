package javachess.backgroundmatrix;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Optional;

import javachess.game.AILogic;
import javachess.game.LAN;
import javachess.game.MovePos;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;


/**
 * @author alexl4123 - 2018
 * @version 2.0 - release
 * 
 * 
 *          The class BackgroundGrid is basically the ,,save-class'' So it
 *          implements Serializable (for save).
 * 
 *          It contains some important parts of the game logic.
 * 
 *          -On start the default position of the Meeples is added here -It
 *          contains the ,,iBackground[][]''. This is the representation of the
 *          position of the meeples. -Also the CheckMate Method (and also DrawCalc) is located here
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
	 * set hardcore AI
	 */
	private boolean HardCoreAI;

	/**
	 * How deep the AI should search
	 */
	private int _iAiDepth;
	//----------------------------------------------------------------------------	

	/**
	 * This list is needed for the forward/backward function (how does the board look like?)
	 */
	private ArrayList<int[][]> _AllBoardStatesList;

	/**
	 * This list is needed for the forward/backward function (which team plays at that round?)
	 */
	private ArrayList<boolean[]> _AllTeamStatesList;

	/**
	 * The indication, if you can move
	 */
	private boolean move;

	/**
	 * This measures the current turn round
	 */
	private short TurnRound;

	/**
	 * How many additional queens are in the game
	 */
	private int QueenNumber;

	/**
	 * Which Game-Mode is selected
	 */
	private int _Choose; 

	/**
	 * needed for LAN stuff
	 */
	public LAN _Lan;

	/**
	 * Which team the AI should play
	 */
	private boolean _bAITeam;

	/**
	 * Boolean if a special move occured
	 */
	private boolean[] bKingMoved, bTowerMoved;

	/**
	 * creates the default background grid
	 * 
	 * also sets the default "numbers" => which turn round it all starts; => how
	 * many additional meeples (threw "Bauerntausch") have been added.
	 * 
	 */
	public BackgroundGrid() {

		//some inits
		bKingMoved = new boolean[2];
		bTowerMoved = new boolean[4];
		HardCoreAI = false;


		QueenNumber = 0;
		TurnRound = 0;
		move = true;
		_iAiDepth = 3;
		_bAITeam = false;

		team = true;
		//_TotalMoveList = new ArrayList<MovePos>();
		_AllBoardStatesList = new ArrayList<int[][]>();
		_AllTeamStatesList = new ArrayList<boolean[]>();

		iBackground = new int[8][8];

		NewBoard();
		int[][] iBoard = new int[8][8];
		for(int iHY = 0; iHY < 8; iHY++){
			for(int iHX = 0; iHX < 8; iHX++){
				iBoard[iHX][iHY] = iBackground[iHX][iHY];
			}

		}
		_AllBoardStatesList.add(iBoard);
		boolean[] States = new boolean[1];
		States[0] = true;
		_AllTeamStatesList.add(States);

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

	public void setTurnRound(short TR){
		TurnRound = TR;
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
		int LocalTurn = TurnRound;

		//if there is a wrong calling
		if(KingX <0 || KingX >= 8 || KingY < 0 || KingY >= 8){
			return false;
		}

		//sets the ID of the king/meeple
		int iID;
		boolean Schach = false;
		if (KingX <= 7 && KingY <= 7) {
			iID = BGG.iBackground[KingX][KingY];
		} else {
			iID = 0;
		}

		//if a king should be ,,simulated'' at this tile
		if (bSimKingOnTile && team) {
			iID = 150;
		} else if (bSimKingOnTile && !team) {
			iID = 250;
		}

		//System.out.println("team:" + team + ":KingX:" + KingX + ":KingY:" + KingY + ":iID:" + iID);
		//System.out.println(iID + ":IID:");
		//checks if the King is in check
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

		//when there is no check, calculate a Draw
		if(!SchachMatt && !Schach && !_Draw){
			_Draw = CalcDraw(iID, BGG.iBackground, KingX, KingY, team, BGG);
		}

		//when there is a check, calculate if there is a Checkmate
		if (Schach && !SchachMatt) {

			SchachmattWhite = SchachMatt(iID, BGG.iBackground, KingX, KingY, team, BGG);
			System.out.println("Schach:" + Schach + ":SchachmattW" + SchachmattWhite);
			if(SchachmattWhite){
				//if there is a checkmate
				if (team) {

					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							try {
								Alert alert = new Alert(AlertType.INFORMATION);
								alert.setTitle("Check Mate");

								alert.setHeaderText("White lost the game! The game took " + LocalTurn + " turns.");

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
								alert.setHeaderText("Black lost the game! The game took " + LocalTurn + "turns.");

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
	 * Checks if a king is in check. Simply tries with every enemy meeple, to hit the king
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
	 * lets look if he is mated
	 * @param iID - ID of the king
	 * @param iBackground - current state of the board
	 * @param KingX - X-Location where the king is
	 * @param KingY - Y-Location where the king is
	 * @param Team - the current team 
	 * @return If a King is mated (true) or the game continues (false)
	 */
	private boolean SchachMatt(int iID, int[][] iBackground, int KingX, int KingY, boolean Team, BackgroundGrid BGG) {


		//let's see, if the king can get free on his own... (if he can attack the aggressor, or he can move away)
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

		//gets every possible move of the attacker and than tries to intercept it, or hit him
		Move Moves = new Move();
		Moves.setBGG(iBackground);
		Moves.setBGG2(BGG);
		//Moves.GetMove(iID, KingX, KingY, BGG);
		Moves.setBSelect(false);

		int iYA, iXA;

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
					//checks if a friend may intercept

					for(MovePos MPA : DefenseMoves){
						if(MP.PX == MPA.PX && MP.PY == MPA.PY){
							System.out.println(MP.PX + "::" + MPA.PX + "::" + MP.PY + "::" + MPA.PY+"::"+MPA.ID);
							//Make the possible defense move
							iBackground[MPA.PX][MPA.PY] = MPA.ID;
							iBackground[MPA.X][MPA.Y] = 0;

							//Give out the current state of the game for debug
							for(int itestY = 0; itestY < 8; itestY++) {
								for(int itestX = 0; itestX < 8; itestX++) {
									System.out.print(":"+iBackground[itestX][itestY]+":");
									if(Team && iBackground[itestX][itestY]==150){
										KingX=itestX;
										KingY=itestY;
									}else if(!Team && iBackground[itestX][itestY]==250){
										KingX=itestX;
										KingY=itestY;
									}
								}
								System.out.println("");
							}

							//get the king position


							System.out.println("KingX::"+KingX+"::KingY::"+KingY);
							System.out.println("SchachKing:" + Schach(iBackground, KingX, KingY, Team) + "::Team::" + Team + "::MPA.PX::" + MPA.PX + "::MPA.PY::" + MPA.PY);
							if(!SchachKing(Team, BGG, KingX, KingY, true, false) && !Schach(iBackground, KingX, KingY, Team)){
								//System.out.println(MPA.PX + "::" + MPA.PY + "::Funkt");
								//no checkmate
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

		//if the meeple is strikeable
		if(Schach(iBackground, _iX, _iY, !Team)){
			System.out.println("line 981");
			return false;
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
			ArrayList<MovePos> KingMoves = M.getMoveMeeple(iBackground, team, iID, KingX, KingY);

			boolean SchachKingMove = true;

			for(MovePos MP : KingMoves) {
				iBackground[MP.PX][MP.PY] = MP.ID;
				iBackground[MP.X][MP.Y] = 0;
				if(!Schach(iBackground, MP.PX, MP.PY, team)) {
					SchachKingMove = false;
					iBackground[MP.PX][MP.PY] = MP.ID2;
					iBackground[MP.X][MP.Y] = MP.ID;
					break;
				}
				iBackground[MP.PX][MP.PY] = MP.ID2;
				iBackground[MP.X][MP.Y] = MP.ID;
			}



			System.out.println("SchachKingMove:"+SchachKingMove+"::"+team+"::_iX::"+KingX+"::iY::"+KingY);
			if(SchachKingMove){
				Draw = true;
				System.out.println("Draw by no King move possible");
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
		} else if((iSum1 >= 270 && iSum1 < 290 && iSum2 == 250 && team) || (iSum2 >= 470 && iSum2 < 490 && iSum1 == 150 && !team)){
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setHeaderText("Draw");
					alert.setContentText("The game cannot be continued due to the impossible of check mate.");
					alert.setTitle("Draw");

					Optional<ButtonType> result = alert.showAndWait();
					if(result.get() == ButtonType.OK){
						setDraw(true);
					} else {
						setDraw(false);
					}
				}
			});
			return getDraw();
		}


		if(TurnRound >= 50){
			System.out.println(">= 50");
			int iCounter = 0;
			int[][] IArr = _AllBoardStatesList.get(_AllBoardStatesList.size()-1);
			AILogic AIL = new AILogic();
			int iBoardRep = (int) AIL.boardEvaluation(IArr,team);

			int iMeeples = 0;

			for(int iY = 0; iY < 8; iY ++){
				for(int iX = 0; iX < 8; iX++){
					if(IArr[iX][iY]>0){
						iMeeples++;
					}
					if(IArr[iX][iY]>=100 && IArr[iX][iY]<110){

					}
				}
			}

			for(int iHelp = 49; iHelp >= 0; iHelp -= 1){

				int iMeeplesRef = 0;
				int[][] IArrRef = _AllBoardStatesList.get(_AllBoardStatesList.size()-iHelp);
				boolean brck = false;
				for(int iY = 0; iY < 8; iY ++){
					for(int iX = 0; iX < 8; iX++){
						//no meeple killed
						if(IArrRef[iX][iY]>0){
							iMeeplesRef++;
						}

						//no Pawn moved
						if((IArrRef[iX][iY]>=100 && IArrRef[iX][iY]<110) || (IArrRef[iX][iY]>=200 && IArrRef[iX][iY] < 210)){
							if(IArr[iX][iY] != IArrRef[iX][iY]){
								brck = true;
							}
						}
					}
				}

				if(iMeeples != iMeeplesRef || brck){
					break;
				}else{
					iCounter++;
				}




			}

			//no meeple killed or no Pawn moved
			if(iCounter >= 49){
				return true;
			}
		}

		//claim draw by threefold repetition
		if(TurnRound >= 6){
			int iCount = 0;
			boolean BoardsEqual = true;

			for(int[][] iBoards : _AllBoardStatesList){
				iCount = 0;

				for(int[][] iBoards2 : _AllBoardStatesList){
					BoardsEqual = true;
					for(int iY = 0; iY < 8; iY++){
						for(int iX = 0; iX < 8; iX++){
							if(iBoards[iX][iY] != iBoards2[iX][iY])
								BoardsEqual = false;
						}
					}
					if(BoardsEqual){

						iCount++;
						//System.out.println("Equals..."+iCount+"::LIST_SIZE::"+_AllBoardStatesList.size());
					}


				}
				if(iCount >= 3 && (_Choose != 2 ||(_Choose == 2 && _bAITeam != this.team))){
					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							Alert alert = new Alert(AlertType.CONFIRMATION);
							alert.setHeaderText("Draw by threefold repetition");
							alert.setContentText("Do you want to claim a draw by threefold repetition?");
							alert.setTitle("Draw");
							alert.showAndWait();
						}
					});

					return true;
				}
			}
		}
		return Draw;
	}

	/**
	 * for new game
	 * reset all boart stats, like TotalMoveList and init new board
	 */
	public void ResetStats(){
		NewBoard();
		//_TotalMoveList.clear();
		TurnRound = 0;
		_AllBoardStatesList.clear();
		_AllTeamStatesList.clear();



		//add the team states

		setbKingMoved(150, false);
		setbKingMoved(250, false);
		setbRookMoved(110, false);
		setbRookMoved(111, false);
		setbRookMoved(210, false);
		setbRookMoved(211, false);

		team = true;
		setTeam(true);


		SchachmattBlack = false;
		SchachmattWhite = false;
		_Draw = false;

		int[][] iBoard = new int[8][8];
		for(int iHY = 0; iHY < 8; iHY++){
			for(int iHX = 0; iHX < 8; iHX++){
				iBoard[iHX][iHY] = iBackground[iHX][iHY];
			}

		}
		_AllBoardStatesList.add(iBoard);
		addTeamState(getTeam());


	}

	/**
	 * For a new game - reset the Board
	 */
	public void NewBoard(){
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

				} else if (Y == 6) {
					// team black
					iBackground[X][Y] = 200 + X;
				}
			}
		}
		// team white
		iBackground[0][0] = 110;// tower 1
		iBackground[7][0] = 111;// tower 2
		iBackground[1][0] = 120; // rider 1
		iBackground[6][0] = 121; // rider 2
		iBackground[2][0] = 130; // runner 1
		iBackground[5][0] = 131; // runner 2
		iBackground[3][0] = 140; // Queen
		iBackground[4][0] = 150; // King

		// team black
		// same for the other team except king and queen changed X-Pos+
		iBackground[0][7] = 210; // tower 1
		iBackground[7][7] = 211; // tower 2
		iBackground[1][7] = 220; // rider 1
		iBackground[6][7] = 221; // rider 2
		iBackground[2][7] = 230; // runner 1
		iBackground[5][7] = 231; // runner 2
		iBackground[3][7] = 240; // queen
		iBackground[4][7] = 250; // king

		_Lan=new LAN(iBackground, this);
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
	 * This Board is needed for the move class
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

	/**
	 * Sets the AI Team (true -> white ai)
	 * @param AITeam
	 */
	public void setAITeam(boolean AITeam) {
		_bAITeam = AITeam;
	}

	/**
	 * Returns, which team the AI should play
	 * @return boolean - AITeam
	 */
	public boolean getAITeam() {
		return _bAITeam;
	}

	/**
	 * Checks, if the HardCoreAi-Mode is selected
	 * @return
	 */
	public boolean getHardCoreAI() {
		return HardCoreAI;
	}

	/**
	 * Sets the HardcoreAI mode
	 * @param hardCoreAI - sets the HardCoreAI Mode
	 */
	public void setHardCoreAI(boolean hardCoreAI) {
		HardCoreAI = hardCoreAI;
	}

}
