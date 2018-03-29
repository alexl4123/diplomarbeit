package javachess.game;

import java.util.ArrayList;

import javachess.backgroundmatrix.BackgroundGrid;
import javachess.backgroundmatrix.Move;
/**
 * 
 * @author alexl12 - 2018
 * @category AI
 * @version 2.0 - Release
 * 
 *          Class for the AI Logic Here has the AI its brain Here is the AI The
 *          AI is basically a MinMax AI with a EvaluationMethod. But MinMax has
 *          certain improvements like AlphaBeta, iterative deepening and
 *          ,,better moves''
 * 
 *          The improvements of MinMax are there for efficiency, so at a depth
 *          of 5, there must not be searched 36^(5), but less (cut offs)
 * 
 *          The evaluation basic principle is Material Balance (what is on the
 *          field). But it also has PawnFormations and a table for each Meeple
 *          what position is best.
 * 
 *          The class also has a own MoveMethod
 * 
 *
 */
public class AILogic {

	/**
	 * Both are not used
	 */
	private BackgroundGrid _WorkPos, _BestPos;

	/**
	 * to what depth should we search?
	 */
	public int MaxDepth;

	/**
	 * contains the number of best move The best Move is saved in ArrayList
	 * BestMove
	 */
	public int _BestMove;

	/**
	 * Contains all the good moves on depth 0
	 */
	public ArrayList<MovePos> BestMove = new ArrayList<MovePos>();

	/**
	 * used to evaluate the best move
	 */
	public int loop;

	/**
	 * not currently used
	 */
	private int count;

	// ----------------------------------------------------------

	/**
	 * The default constructor sets loop = 0.
	 */
	public AILogic() {
		loop = 0;
	}

	/**
	 * should be at first a caller for AlphaBeta method. Currently not used!
	 * 
	 * @param BGG2
	 *            - BackgroundGrid - used for moves,...
	 * @param Team
	 *            - the current team
	 * @return BackgroundGrid - what has changed
	 */
	public BackgroundGrid playGame(BackgroundGrid BGG2, boolean Team) {

		_WorkPos = BGG2;
		_BestPos = null;

		Float x = alphaBeta(0, _WorkPos, Team);

		return BGG2;
	}

	/**
	 * This Method is for IterativeDeepening. Iterative Deepening searches
	 * increasingly, beginning at depth 0 to MaxDepth
	 * 
	 * 
	 * 
	 * @param depth
	 *            - int - to what depth should be searched
	 * @param BGG2
	 *            - BackgroundGrid - contains the position of the meeples
	 * @param Team
	 *            - for which team should the AI work?
	 * @return float - value of best move, represented in a float
	 */
	public float alphaBeta(int depth, BackgroundGrid BGG2, boolean Team) {
		MaxDepth = depth;
		count = 0;
		float beta = -10000.0f;

		beta = alphaBetaHelper(0, BGG2, Team, -beta, beta);
		System.out.println(beta);

		/*
		for (int i = 1; i <= depth; i++) {
			MaxDepth = i;
			float fNewBeta = -10000;
			fNewBeta = alphaBetaHelper(0, BGG2, Team, -beta, beta);
			if(fNewBeta > beta) {
				beta = fNewBeta;
			}
			System.out.println(beta);
		}
		 */

		return beta;
	}

	/**
	 * Method consists of MinMax and AlphaBeta (due to ALphaBeta is an
	 * improvement to MinMax)
	 * 
	 * @param depth
	 *            - int - which depth the AlphaBeta currently is
	 * @param BGG2
	 *            - BackgroundGrid - where the meeples are stored
	 * @param Team
	 *            - boolean - for what team should we do this
	 * @param alpha
	 *            - float - basically the upper boundary for the Maxer
	 * @param beta
	 *            - float - basically the lower boundary for the Maxer
	 * @return float - the value of the best move
	 */
	public float alphaBetaHelper(int depth, BackgroundGrid BGG2, boolean Team, float alpha, float beta) {

		float Sum = boardEvaluation(BGG2.iBackground, Team); // the value of the Board
		if(Sum > 50000){
			return 200000;
		}else if(Sum < (-50000)) {
			return (-200000);
		}

		if (depth >= MaxDepth) { // if the max depth has been reached, return

			return Sum;
		}

		for (int y = 0; y < 8; y++) { // loops threw all objects
			for (int x = 0; x < 8; x++) {
				int iPos = BGG2.iBackground[x][y];
				if (iPos > 0) {
					Move M = new Move();
					M.setBGG(BGG2.iBackground);
					
					M.setBGG2(BGG2);

					/* no comment */

					ArrayList<MovePos> AIM = M.getMoveMeeple(BGG2.iBackground, Team, iPos, x, y); 
					for (MovePos A : AIM) { // loops threw all possible moves
						if(M.AllowedMove(A)){

							BGG2.iBackground[A.PX][A.PY] = A.ID;
							BGG2.iBackground[A.X][A.Y] = 0; // makes the move
							if (A.ID3 > 0) {
								BGG2.iBackground[A.X3][A.Y3] = 0;
							}

							//Rochade
							if (A.ID4 > 0) {
								BGG2.iBackground[A.X4][A.Y4] = 0;
								if (A.X3 > 0) {
									BGG2.iBackground[A.X3][A.Y3] = 0;
								}
								BGG2.iBackground[A.X5][A.Y5] = A.ID4;
							}

							//Bauerntausch
							if(A.ID >= 100 && A.ID < 110 && Team && A.PY == 7){
								BGG2.iBackground[A.PX][A.PY] = 140+ BGG2.getQueenNumber();
							} else if(A.ID >= 200 && A.ID < 210 && !Team && A.PY == 0){
								BGG2.iBackground[A.PX][A.PY] = 240+ BGG2.getQueenNumber();
							}


							BGG2.changeTeam();
							float Sum1 = -alphaBetaHelper(depth + 1, BGG2, !Team, -beta, -alpha);
							/*
							 * recursively calls itself with changed alphaBeta,
							 * increased depth and changed team
							 */
							BGG2.iBackground[A.PX][A.PY] = A.ID2;
							BGG2.iBackground[A.X][A.Y] = A.ID; 
							if (A.ID3 > 0) {
								BGG2.iBackground[A.X3][A.Y3] = A.ID3;
							}

							//Rochade rueckgaengig
							if (A.ID4 > 0) {
								BGG2.iBackground[A.X4][A.Y4] = A.ID4;
								if (A.X3 > 0) {
									BGG2.iBackground[A.X3][A.Y3] = A.ID3;
								}
								BGG2.iBackground[A.X5][A.Y5] = A.ID5;
							}
							//Bauerntausch rueckgaengig
							if(A.ID >= 100 && A.ID < 110 && Team && A.PY == 7){
								BGG2.iBackground[A.PX][A.PY] = A.ID2;
							} else if(A.ID >= 200 && A.ID < 210 && !Team && A.PY == 0){
								BGG2.iBackground[A.PX][A.PY] = A.ID2;
							}

							// returns the move
							// to its previous
							// position
							BGG2.changeTeam();

							if(BGG2.getHardCoreAI() && Sum1 > beta){
								beta = Sum1;

								if (depth == 0) { // if at depth 0 and a good move
									System.out.println("Sum1:"+Sum1 + "::beta::"+beta+"::loop::"+loop+"::ID::"+A.ID+"::max_depth::"+MaxDepth);
									loop++;
									A.Beta = (int) Sum1;
									BestMove.add(A);

									_BestMove = loop;
								}

								if (beta >= alpha) { // if the move was so good, the
									// opposing team would never
									// take it
									return beta;
								}

							}
							if (!BGG2.getHardCoreAI() && Sum1 >= beta) { // if it was a good move

								beta = Sum1;
								//---------------------------------------------------------------------------------------
								if(depth==1 && A.ID==240 && A.PX == 5 && beta==-365) {
									System.out.println("Sum1:"+Sum1 + "::beta::"+beta+"::ID::"+A.ID+":X:"+A.PX+"::Y::"+A.PY+"::max_depth::"+depth);

									BGG2.iBackground[A.PX][A.PY] = A.ID;
									BGG2.iBackground[A.X][A.Y] = 0; // makes the move
									if (A.ID3 > 0) {
										BGG2.iBackground[A.X3][A.Y3] = 0;
									}

									if (A.ID4 > 0) {
										BGG2.iBackground[A.X4][A.Y4] = 0;
										if (A.X3 > 0) {
											BGG2.iBackground[A.X3][A.Y3] = 0;
										}
										BGG2.iBackground[A.X5][A.Y5] = A.ID4;
									}

									if(A.ID >= 100 && A.ID < 110 && Team && A.PY == 7){
										BGG2.iBackground[A.PX][A.PY] = 140+ BGG2.getQueenNumber();
									} else if(A.ID >= 200 && A.ID < 210 && !Team && A.PY == 0){
										BGG2.iBackground[A.PX][A.PY] = 240+ BGG2.getQueenNumber();
									}

									for (int Y = 0; Y < 8; Y++) {
										for (int X = 0; X < 8; X++) {
											System.out.print(":"+BGG2.iBackground[X][Y]+":");
										}
										System.out.println("");
									}

									BGG2.iBackground[A.PX][A.PY] = A.ID2;
									BGG2.iBackground[A.X][A.Y] = A.ID; 
									if (A.ID3 > 0) {
										BGG2.iBackground[A.X3][A.Y3] = A.ID3;
									}

									if (A.ID4 > 0) {
										BGG2.iBackground[A.X4][A.Y4] = A.ID4;
										if (A.X3 > 0) {
											BGG2.iBackground[A.X3][A.Y3] = A.ID3;
										}
										BGG2.iBackground[A.X5][A.Y5] = A.ID5;
									}

									if(A.ID >= 100 && A.ID < 110 && Team && A.PY == 7){
										BGG2.iBackground[A.PX][A.PY] = A.ID2;
									} else if(A.ID >= 200 && A.ID < 210 && !Team && A.PY == 0){
										BGG2.iBackground[A.PX][A.PY] = A.ID2;
									}


								}	//---------------------------------------------------------------------------------------


								if (depth == 0) { // if at depth 0 and a good move
									System.out.println("Sum1:"+Sum1 + "::beta::"+beta+"::loop::"+loop+"::ID::"+A.ID+"::max_depth::"+MaxDepth);
									loop++;
									A.Beta = (int) Sum1;
									BestMove.add(A);

									_BestMove = loop;
								}

								if (beta >= alpha) { // if the move was so good, the
									// opposing team would never
									// take it
									return beta;
								}


							}

						} // foreach end
					}//Check AllowedMove end
				} // if a Meeple has been selected

			}

		}
		return beta;
	}

	/**
	 * At current stage of development, this evaluation method contains Material
	 * Balance Pawn formations And piece square tables Evaluates a float number
	 * for the given BackgroundGrid.
	 * 
	 * @param BGG2
	 *            - BackgroundGrid for getting the current Board
	 * @param Team
	 *            - for which team the eval. should be made
	 * @return S (float) - returns the current eval. number of the board
	 */
	public float boardEvaluation(int[][] Board, boolean Team) {
		float S1 = 0;
		float S2 = 0;
		float S = 0;
		//int[][] Board = BGG2.iBackground;

		// Material Balance
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				int SB = Board[x][y];

				if (SB >= 100 && SB < 110) {
					try {
						S1 += 100; // if just a pawn exists
						S1 += WhitePawnSquareTable[x][y]; // were the pawn
						// should be!
						if ((y - 1) >= 0 && (y + 1) < 8) {
							if (Board[x][y - 1] > 100 && Board[x][y - 1] < 110) {
								S1 -= 30; // If two Pawns are in the same row
							} else if (Board[x][y + 1] > 200 && Board[x][y + 1] < 210) {
								S1 -= 20; // If two opposite Pawns are standing
								// face
								// to face
							} else {

							}
						}

						if ((x - 1) >= 0 && (y + 1) < 8) {
							if (Board[x - 1][y + 1] > 100 && Board[x - 1][y + 1] < 110) {
								S1 += 40; // a pawn formation
							}
						}
						if ((x + 1) < 8 && (y + 1) < 8) {
							if (Board[x + 1][y + 1] > 100 && Board[x + 1][y + 1] < 110) {
								S1 += 40;
							}
						}

					} catch (Exception ex) {
						ex.printStackTrace();
					}

				} else if (SB >= 110 && SB < 120) {
					S1 += 500; // the rook (tower)
					S1 += WhiteRookSquareTable[x][y];
				} else if (SB >= 120 && SB < 130) {
					S1 += 325; // the knight (jumper)
					S1 += WhiteKnightSquareTable[x][y];
				} else if (SB >= 130 && SB < 140) {
					S1 += 300; // the bishop (runner)
					S1 += WhiteBishopSquareTable[x][y];
				} else if (SB >= 140 && SB < 150) {
					S1 += 900; // the queen
					S1 += WhiteQueenSquareTable[x][y];
				} else if (SB == 150) {
					S1 += 100000;
					if (S1 > 110000) {
						S1 += WhiteKingMiddleSquareTable[x][y];
					} else {
						S1 += WhiteKingEndSquareTable[x][y];
					}

				}

				if (SB >= 200 && SB < 210) {
					try {
						S2 += 100; // if just a pawn exists
						S2 += BlackPawnSquareTable[x][y]; // were the pawn
						// should be!
						if ((y + 1) < 8 && (y - 1) >= 0) {
							if (Board[x][y + 1] >= 200 && Board[x][y + 1] < 210) {
								S2 -= 30; // If two Pawns are in the same row
							} else if (Board[x][y - 1] > 100 && Board[x][y - 1] < 110) {
								S2 -= 20; // If two opposite Pawns are standing
								// face
								// to face
							} else {

							}
						}

						if ((x - 1) >= 0 && (y - 1) >= 0) {
							if (Board[x - 1][y - 1] > 200 && Board[x - 1][y - 1] < 210) {
								S2 += 40; // a pawn formation
							}
						}
						if ((x + 1) < 8 && (y - 1) >= 0) {
							if (Board[x + 1][y - 1] > 200 && Board[x + 1][y - 1] < 210) {
								S2 += 40;
							}
						}

					} catch (Exception ex) {
						ex.printStackTrace();
					}

				} else if (SB >= 210 && SB < 220) {
					S2 += 500; // the rook (tower)
					S2 += BlackRookSquareTable[x][y];
				} else if (SB >= 220 && SB < 230) {
					S2 += 325; // the knight (jumper)
					S2 += BlackKnightSquareTable[x][y];
				} else if (SB >= 230 && SB < 240) {
					S2 += 300; // the bishop (runner)
					S2 += BlackKnightSquareTable[x][y];
				} else if (SB >= 240 && SB < 250) {
					S2 += 900; // the queen
					S2 += BlackQueenSquareTable[x][y];
				} else if (SB == 250) {
					S2 += 100000;
					if (S2 > 110000) {
						S2 += BlackKingMiddleSquareTable[x][y];
					} else {
						S2 += BlackKingEndSquareTable[x][y];
					}

				}

			}
		}

		// Mobility
		// Tropism

		if (Team) {
			S = S1 - S2;
		} else
			S = S2 - S1;
		return S;
	}

	/**
	 * Sets the white pawn Square Table. A move square table ist basically the
	 * location where a meeple should move to.
	 * 
	 * @return int[][] - what number on what position
	 */
	private int[][] WhitePawnTable() {
		int[][] table;
		int[] tableHelper;
		table = new int[8][8];
		tableHelper = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 50, 50, 50, 50, 50, 50, 50, 50, 10, 10, 20, 30, 30, 20, 10,
				10, 5, 5, 10, 25, 25, 10, 5, 5, 0, 0, 0, 20, 20, 0, 0, 0, 5, -5, -10, 0, 0, -10, -5, 5, 5, 10, 10, -20,
				-20, 10, 10, 5, 0, 0, 0, 0, 0, 0, 0, 0 };
		// these numbers are from the www

		int i = 0;
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				table[x][y] = tableHelper[i];
				i++;
			}
		}

		return table;
	}

	/**
	 * int[][] - just for performance purpose
	 */
	public int[][] WhitePawnSquareTable = WhitePawnTable();

	// ----------------------------------------------------------------------------------------------------
	/**
	 * Sets the pawn Square Table
	 * 
	 * @return int[][] - what number on what position
	 */
	private int[][] BlackPawnTable() {
		int[][] table;
		int[] tableHelper;
		table = new int[8][8];
		tableHelper = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 5, 10, 10, -20, -20, 10, 10, 5, 5, -5, -10, 0, 0, -10, -5, 5,
				0, 0, 0, 20, 20, 0, 0, 0, 5, 5, 10, 25, 25, 10, 5, 5, 10, 10, 20, 30, 30, 20, 10, 10, 50, 50, 50, 50,
				50, 50, 50, 50, 0, 0, 0, 0, 0, 0, 0, 0 };

		int i = 0;
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				table[x][y] = tableHelper[i];
				i++;
			}
		}

		return table;
	}

	/**
	 * int[][] just for performance purpose
	 */
	public int[][] BlackPawnSquareTable = BlackPawnTable();

	// ---------------------------------------------------------------------------------------
	/**
	 * The Move Square table for the WhiteKnight
	 * 
	 * @return - int[][] - what number on what position
	 */
	private int[][] WhiteKnightTable() {
		int[][] table;
		int[] tableHelper;
		table = new int[8][8];
		tableHelper = new int[] { -50, -40, -30, -30, -30, -30, -40, -50, -40, -20, 0, 0, 0, 0, -20, -40, -30, 0, 10,
				15, 15, 10, 0, -30, -30, 5, 15, 20, 20, 15, 5, -30, -30, 0, 15, 20, 20, 15, 0, -30, -30, 5, 10, 15, 15,
				10, 5, -30, -40, -20, 0, 5, 5, 0, -20, -40, -50, -40, -30, -30, -30, -30, -40, -50 };

		int i = 0;
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				table[x][y] = tableHelper[i];
				i++;
			}
		}

		return table;
	}

	/**
	 * Performance
	 */
	public int[][] WhiteKnightSquareTable = WhiteKnightTable();

	// -----------------------------------------------------------------------------------------
	/**
	 * The black knight move square table
	 * 
	 * @return - int[][] - what number on what position
	 */
	private int[][] BlackKnightTable() {
		int[][] table;
		int[] tableHelper;
		table = new int[8][8];
		tableHelper = new int[] { -50, -40, -30, -30, -30, -30, -40, -50, -40, -20, 0, 5, 5, 0, -20, -40, -30, 5, 10,
				15, 15, 10, 5, -30, -30, 0, 15, 20, 20, 15, 0, -30, -30, 5, 15, 20, 20, 15, 5, -30, -30, 0, 10, 15, 15,
				10, 0, -30, -40, -20, 0, 0, 0, 0, -20, -40, -50, -40, -30, -30, -30, -30, -40, -50 };

		int i = 0;
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				table[x][y] = tableHelper[i];
				i++;
			}
		}

		return table;
	}

	/**
	 * for performance
	 */
	public int[][] BlackKnightSquareTable = BlackKnightTable();

	// ----------------------------------------------------------------------------------------
	/**
	 * The white bishop move square table
	 * 
	 * @return - int[][] - what number on what position
	 */
	private int[][] WhiteBishopTable() {
		int[][] table;
		int[] tableHelper;
		table = new int[8][8];
		tableHelper = new int[] { -20, -10, -10, -10, -10, -10, -10, -20, -10, 0, 0, 0, 0, 0, 0, -10, -10, 0, 5, 10, 10,
				5, 0, -10, -10, 5, 5, 10, 10, 5, 5, -10, -10, 0, 10, 10, 10, 10, 0, -10, -10, 10, 10, 10, 10, 10, 10,
				-10, -10, 5, 0, 0, 0, 0, 5, -10, -20, -10, -10, -10, -10, -10, -10, -20 };

		int i = 0;
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				table[x][y] = tableHelper[i];
				i++;
			}
		}

		return table;
	}

	/**
	 * just for performance
	 */
	public int[][] WhiteBishopSquareTable = WhiteBishopTable();

	// ----------------------------------------------------------------------------------------
	/**
	 * The black bishop move square table
	 * 
	 * @return - int[][] - what number on what position
	 */
	private int[][] BlackBishopTable() {
		int[][] table;
		int[] tableHelper;
		table = new int[8][8];
		tableHelper = new int[] { -20, -10, -10, -10, -10, -10, -10, -20, -10, 5, 0, 0, 0, 0, 5, -10, -10, 10, 10, 10,
				10, 10, 10, -10, -10, 0, 10, 10, 10, 10, 0, -10, -10, 5, 5, 10, 10, 5, 5, -10, -10, 0, 5, 10, 10, 5, 0,
				-10, -10, 0, 0, 0, 0, 0, 0, -10, -20, -10, -10, -10, -10, -10, -10, -20 };

		int i = 0;
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				table[x][y] = tableHelper[i];
				i++;
			}
		}

		return table;
	}

	/**
	 * just for performance
	 */
	public int[][] BlackBishopSquareTable = BlackBishopTable();

	// ----------------------------------------------------------------------------------------

	/**
	 * The white rook move square table
	 * 
	 * @return - int[][] - what number on what position
	 */
	private int[][] WhiteRookTable() {
		int[][] table;
		int[] tableHelper;
		table = new int[8][8];
		tableHelper = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 5, 10, 10, 10, 10, 10, 10, 5, -5, 0, 0, 0, 0, 0, 0, -5, -5, 0,
				0, 0, 0, 0, 0, -5, -5, 0, 0, 0, 0, 0, 0, -5, -5, 0, 0, 0, 0, 0, 0, -5, -5, 0, 0, 0, 0, 0, 0, -5, 0, 0,
				0, 5, 5, 0, 0, 0 };

		int i = 0;
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				table[x][y] = tableHelper[i];
				i++;
			}
		}

		return table;
	}

	/**
	 * just for performance
	 */
	public int[][] WhiteRookSquareTable = WhiteRookTable();

	// ----------------------------------------------------------------------------------------
	/**
	 * The black rook move square table
	 * 
	 * @return -int[][] - what number on what position
	 */
	private int[][] BlackRookTable() {
		int[][] table;
		int[] tableHelper;
		table = new int[8][8];
		tableHelper = new int[] { 0, 0, 0, 5, 5, 0, 0, 0, -5, 0, 0, 0, 0, 0, 0, -5, -5, 0, 0, 0, 0, 0, 0, -5, -5, 0, 0,
				0, 0, 0, 0, -5, -5, 0, 0, 0, 0, 0, 0, -5, -5, 0, 0, 0, 0, 0, 0, -5, 5, 10, 10, 10, 10, 10, 10, 5, 0, 0,
				0, 0, 0, 0, 0, 0 };

		int i = 0;
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				table[x][y] = tableHelper[i];
				i++;
			}
		}

		return table;
	}

	/**
	 * just for performance
	 */
	public int[][] BlackRookSquareTable = BlackRookTable();

	// ----------------------------------------------------------------------------------------

	/**
	 * The white queen move square table
	 * 
	 * @return - int[][] - what number on what position
	 */
	private int[][] WhiteQueenTable() {
		int[][] table;
		int[] tableHelper;
		table = new int[8][8];
		tableHelper = new int[] { -20, -10, -10, -5, -5, -10, -10, -20, -10, 0, 0, 0, 0, 0, 0, -10, -10, 0, 5, 5, 5, 5,
				0, -10, -5, 0, 5, 5, 5, 5, 0, -5, 0, 0, 5, 5, 5, 5, 0, -5, -10, 5, 5, 5, 5, 5, 0, -10, -10, 0, 5, 0, 0,
				0, 0, -10, -20, -10, -10, -5, -5, -10, -10, -20 };

		int i = 0;
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				table[x][y] = tableHelper[i];
				i++;
			}
		}

		return table;
	}

	/**
	 * just for performance
	 */
	public int[][] WhiteQueenSquareTable = WhiteQueenTable();

	// ----------------------------------------------------------------------------------------

	/**
	 * The black queen move square table
	 * 
	 * @return - int[][] - what number on what position
	 */
	private int[][] BlackQueenTable() {
		int[][] table;
		int[] tableHelper;
		table = new int[8][8];
		tableHelper = new int[] { -20, -10, -10, -5, -5, -10, -10, -20, -10, 0, 5, 0, 0, 0, 0, -10, -10, 5, 5, 5, 5, 5,
				0, -10, 0, 0, 5, 5, 5, 5, 0, -5, -5, 0, 5, 5, 5, 5, 0, -5, -10, 0, 5, 5, 5, 5, 0, -10, -10, 0, 0, 0, 0,
				0, 0, -10, -20, -10, -10, -5, -5, -10, -10, -20 };

		int i = 0;
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				table[x][y] = tableHelper[i];
				i++;
			}
		}

		return table;
	}

	/**
	 * Just for performance
	 */
	public int[][] BlackQueenSquareTable = BlackQueenTable();

	// ----------------------------------------------------------------------------------------

	/**
	 * the white king middle move square table For the mid game
	 * 
	 * @return - int[][] - what number on what position
	 */
	private int[][] WhiteKingMiddleTable() {
		int[][] table;
		int[] tableHelper;
		table = new int[8][8];
		tableHelper = new int[] { 20, 30, 10, 0, 0, 10, 30, 20, 20, 20, 0, 0, 0, 0, 20, 20, -10, -20, -20, -20, -20,
				-20, -20, -10, -20, -30, -30, -40, -40, -30, -30, -20, -30, -40, -40, -50, -50, -40, -40, -30, -30, -40,
				-40, -50, -50, -40, -40, -30, -30, -40, -40, -50, -50, -40, -40, -30, -30, -40, -40, -50, -50, -40, -40,
				-30 };

		int i = 0;
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				table[x][y] = tableHelper[i];
				i++;
			}
		}

		return table;
	}

	/**
	 * just for performance
	 */
	public int[][] WhiteKingMiddleSquareTable = WhiteKingMiddleTable();

	// ----------------------------------------------------------------------------------------

	/**
	 * the black king move square table for the middle game
	 * 
	 * @return - int[][] - what number on what position
	 */
	private int[][] BlackKingMiddleTable() {
		int[][] table;
		int[] tableHelper;
		table = new int[8][8];
		tableHelper = new int[] { -30, -40, -40, -50, -50, -40, -40, -30, -30, -40, -40, -50, -50, -40, -40, -30, -30,
				-40, -40, -50, -50, -40, -40, -30, -30, -40, -40, -50, -50, -40, -40, -30, -20, -30, -30, -40, -40, -30,
				-30, -20, -10, -20, -20, -20, -20, -20, -20, -10, 20, 20, 0, 0, 0, 0, 20, 20, 20, 30, 10, 0, 0, 10, 30,
				20 };

		int i = 0;
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				table[x][y] = tableHelper[i];
				i++;
			}
		}

		return table;
	}

	/**
	 * just for performance
	 */
	public int[][] BlackKingMiddleSquareTable = BlackKingMiddleTable();

	// ----------------------------------------------------------------------------------------

	/**
	 * the white king end move square table. For the end game
	 * 
	 * @return - int[][] - what number on what position
	 */
	private int[][] WhiteKingEndTable() {
		int[][] table;
		int[] tableHelper;
		table = new int[8][8];
		tableHelper = new int[] { -50, -30, -30, -30, -30, -30, -30, -50, -30, -30, 0, 0, 0, 0, -30, -30, -30, -10, 20,
				30, 30, 20, -10, -30, -30, -10, 30, 40, 40, 30, -10, -30, -30, -10, 30, 40, 40, 30, -10, -30, -30, -10,
				20, 30, 30, 20, -10, -30, -30, -20, -10, 0, 0, -10, -20, -30, -50, -40, -30, -20, -20, -30, -40, -50 };

		int i = 0;
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				table[x][y] = tableHelper[i];
				i++;
			}
		}

		return table;
	}

	/**
	 * just for performance
	 */
	public int[][] WhiteKingEndSquareTable = WhiteKingEndTable();

	// ----------------------------------------------------------------------------------------

	/**
	 * the black king end move square table for the end game
	 * 
	 * @return - int[][] - what number on what position
	 */
	private int[][] BlackKingEndTable() {
		int[][] table;
		int[] tableHelper;
		table = new int[8][8];
		tableHelper = new int[] { -50, -40, -30, -20, -20, -30, -40, -50, -30, -20, -10, 0, 0, -10, -20, -30, -30, -10,
				20, 30, 30, 20, -10, -30, -30, -10, 30, 40, 40, 30, -10, -30, -30, -10, 30, 40, 40, 30, -10, -30, -30,
				-10, 20, 30, 30, 20, -10, -30, -30, -30, 0, 0, 0, 0, -30, -30, -50, -30, -30, -30, -30, -30, -30, -50 };

		int i = 0;
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				table[x][y] = tableHelper[i];
				i++;
			}
		}

		return table;
	}

	/**
	 * just for performance
	 */
	public int[][] BlackKingEndSquareTable = BlackKingEndTable();


}
