package javachess.backgroundmatrix;

import java.util.ArrayList;
import javachess.game.MovePos;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * @author alexl4123 - 2018
 * @version 2.0 - release
 * 
 *          A class for moving around the Meeples
 *          All move operations are checked here.
 *          
 */
public class Move {
	/**
	 * _BGG - int[][] - set due to need of it 
	 * 	TheMove - int[][] - returned to Gui - after the move
	 */
	private int[][] _BGG, TheMove;

	/**
	 * _bSelect - boolean - if sth. has been selected _Blackschach - boolean
	 * if the black king is checked (not mated) _Whiteschach - boolean - if the
	 * white king is checked (not mated) _Bauerntausch - boolean - if a
	 * _Bauerntausch has occured _Moved - boolean - if a Meeple has been moved
	 */
	private boolean _bSelect, _Blackschach, _Whiteschach, _Bauerntausch, _Moved;

	/**
	 * _iSelect - int - numeric representation of the previous selected Meeple
	 * _iPosX - int - X-Coord. of the selected Meeple _iPosY - int - Y-Coord. of
	 * the selected Meeple
	 */
	private int _iSelect, _iPosX, _iPosY;

	/**
	 * _BGG2 - BackgroundGrid - used for the Moving methods
	 */
	private BackgroundGrid _BGG2;

	/**
	 * MoveList - ArrayList<int[]> - used to designate possible move
	 */
	private ArrayList<int[]> _MoveList = new ArrayList<int[]>();

	/**
	 * HitList - ArrayList<int[]> - used to display possible hits
	 */
	private ArrayList<int[]> _HitList = new ArrayList<int[]>();

	/**
	 * LastMoveList - ArrayList<int[]> - used to display the last move
	 */
	private ArrayList<int[]> _LastMoveList = new ArrayList<int[]>();

	/**
	 * The Last move
	 */
	private ArrayList<MovePos> _LastMove = new ArrayList<MovePos>();


	/**
	 * for the launchpad
	 */
	private boolean _moveAllowed;



	/**
	 * default constructor - sets move selected to false
	 */
	public Move() {
		_bSelect = false;

	}

	/**
	 * This methode returns the backgroundGrid, which should be drawn
	 * Just uses one other method to identify the possible moves.
	 * These moves are returned via a MovePos object.
	 * 
	 * @param iPos
	 *            - the ,,Index'' of the Object
	 * @param iPosX
	 *            - x Position of the Click
	 * @param iPosY
	 *            - y Position of the Click
	 * @param BGG2
	 *            - BackgroundGrid (contains team, eg.)
	 * @return TheMove - a int[][] which is drawn
	 */
	public int[][] GetMove(int iPos, int iPosX, int iPosY, BackgroundGrid BGG2) {

		_moveAllowed = false;
		_BGG2 = BGG2;
		_BGG = _BGG2.iBackground;
		TheMove = _BGG2.iBackground;
		_Moved = false;
		int Check = Math.abs(iPos - _iSelect);
		double[][] BoardRep = new double[8][8]; //Fuck Java
		_MoveList.clear();
		_HitList.clear();

		if (_bSelect && Check >= 50) { // Moving Objects
			ArrayList<MovePos> MoveList = getMoveMeeple(_BGG, _BGG2.getTeam(), _iSelect, _iPosX, _iPosY);

			for (MovePos MP : MoveList) {
				if (iPosX == MP.PX && iPosY == MP.PY && AllowedMove(MP)) {
					_BGG2.Board = _BGG2.iBackground;


					//int[][] XYZ = TheMove;
					//MMP.setBoard(XYZ);
					_LastMove.clear();
					TheMove[MP.X][MP.Y] = 0;
					TheMove[MP.PX][MP.PY] = MP.ID;

					if (MP.ID3 > 0) {
						TheMove[MP.X3][MP.Y3] = 0;
					}

					if (MP.ID4 > 0) {
						TheMove[MP.X4][MP.Y4] = 0;
						if (MP.X3 > 0) {
							TheMove[MP.X3][MP.Y3] = 0;
						}
						TheMove[MP.X5][MP.Y5] = MP.ID4;
						_BGG2.setbRookMoved(MP.ID4, true);
						int[] iRoch1 = new int[1];
						int[] iRoch2 = new int[1];
						iRoch1[0] = MP.X5 + (8 * MP.Y5);
						iRoch2[0] = MP.X4 + (8 * MP.Y4);

						_LastMoveList.add(iRoch1);
						_LastMoveList.add(iRoch2);
					}

					if (MP.ID >= 100 && MP.ID < 110 && _BGG2.getTeam() && MP.PY == 7) {
						_Bauerntausch = false;
						Bauerntausch(_BGG2.getTeam(), MP.PX, MP.PY);

					} else if (MP.ID >= 200 && MP.ID < 210 && !_BGG2.getTeam() && MP.PY == 0) {
						_Bauerntausch = false;
						Bauerntausch(_BGG2.getTeam(), MP.PX, MP.PY);
					}

					_Moved = true;
					_bSelect = false;
					_LastMove.add(MP);
					_BGG2.setbKingMoved(MP.ID, true);
					_BGG2.setbRookMoved(MP.ID, true);
					int[] iH = new int[1];
					iH[0] = MP.PX + (8 * MP.PY);
					_LastMoveList.add(iH);
					int[] iHH = new int[1];
					iHH[0] = MP.X + (8 * MP.Y);
					_LastMoveList.add(iHH);

					for(int Y = 0; Y < 8; Y++){
						for(int X = 0; X < 8; X++){
							BoardRep[X][Y] = (double) _BGG2.iBackground[X][Y];

						}
					}
					MP.Board = BoardRep;
					//BGG2._TotalMoveList.add(MP);
					_moveAllowed = true;





				}

			}

			// TheMove = Moveing(iPos, iPosX, iPosY, BGG2);
			MoveList.clear(); // clear both lists, that no possible moves are
			// displayed (because nothing is selected)
			_HitList.clear();

		} else

		{
			/*
			 * All possible moves are written - displayed in BoardGui()
			 */
			if (iPos > 99) {
				_LastMoveList.clear();
				_iSelect = iPos;
				_iPosX = iPosX;
				_iPosY = iPosY;
				_bSelect = true;

				ArrayList<MovePos> MoveList = getMoveMeeple(_BGG, _BGG2.getTeam(), iPos, iPosX, iPosY);

				for (MovePos MP : MoveList) {
					if (MP.ID2 == 0) {
						if (AllowedMove(MP)) {
							int[] Hit = new int[5];
							int H = MP.PX + (MP.PY * 8);
							Hit[0] = H;
							if (MP.ID3 > 0) { // En Passant
								_HitList.add(Hit);
							} else {
								_MoveList.add(Hit);
							}
						}

					} else {
						if (AllowedMove(MP)) {
							int[] Hit = new int[5];
							int H = MP.PX + (MP.PY * 8);
							Hit[0] = H;
							_HitList.add(Hit);
						}

					}

				}

				if (MoveList.size() == 0) {
					_bSelect = false;
				}

			} else {
				_bSelect = false;
			}

		}

		if (_Moved) { // if a meeple has been moved change teams, higher the
			// turn round and check checked ;)

			getSchach2();
			BGG2.changeTeam();
			BGG2.higherTurnRound();




			//BG.getGui().getMenu().getPp().getLoadTurn().getItems().add("Turn:"+BGG2.getTurnRound());
			//add the board states
			//the complicity is needed, due to same pointer errors
			int[][] iBoard = new int[8][8];
			for(int iHY = 0; iHY < 8; iHY++){
				for(int iHX = 0; iHX < 8; iHX++){
					iBoard[iHX][iHY] = BGG2.iBackground[iHX][iHY];
				}

			}
			if(BGG2.getTurnRound()!=BGG2.getBoardList().size()){
				for(int iRM = BGG2.getBoardList().size()-1; iRM >= BGG2.getTurnRound(); iRM--){
					System.out.println("Override board state:"+iRM+"::BOARD::"+BGG2.getBoardList().size());
					BGG2.getBoardList().set(iRM,iBoard);
					boolean[] IB = new boolean[1];
					IB[0] = BGG2.getTeam();
					BGG2.getTeamList().set(iRM, IB);
				}
			}else{
				BGG2.addBoardState(iBoard);
				//add the team states
				BGG2.addTeamState(BGG2.getTeam());
			}

			System.out.println("Turn Round:"+BGG2.getTurnRound()+"::Size::"+BGG2.getBoardList().size());


			System.out.println("Moved");
			for(int[] ii :_LastMoveList){
				int i = ii[0];
				//BG.HighlightLField(i);
			}

			_Moved = false;
		} else if(_bSelect) {
			//only for Schach-Debug
			//getSchach2();

			//System.out.println("BoardListSize:" + BGG2.getBoardList().size() + "::" + BGG2.getTeamList().size());
			if(BGG2.getBoardList().size() > 0){
				int iH = 0;
				for(int[][] iBoard : BGG2.getBoardList()){

					//System.out.println("Team:" + BGG2.getTeamList().get(iH)[0]);
					for(int iHY = 0; iHY < 8; iHY++){
						for(int iHX = 0; iHX < 8; iHX++){
							//System.out.print(":" + iBoard[iHX][iHY] + ":");
						}
						//System.out.println(":");
					}
					iH++;
				}

			}
		}
		return TheMove;
	}

	public boolean AllowedMove(MovePos MP) {
		boolean allowed;

		_BGG[MP.X][MP.Y] = 0;
		_BGG[MP.PX][MP.PY] = MP.ID;
		if (MP.ID3 > 0) {
			_BGG[MP.X3][MP.Y3] = 0;
		}

		if (MP.ID4 > 0) {
			_BGG[MP.X4][MP.Y4] = 0;
			if (MP.X3 > 0) {
				_BGG[MP.X3][MP.Y3] = 0;
			}
			_BGG[MP.X5][MP.Y5] = MP.ID4;
		}

		int KingX, KingY;
		KingX = 10;
		KingY = 10;
		for (int Y = 0; Y < 8; Y++) {
			for (int X = 0; X < 8; X++) {
				if (_BGG2.iBackground[X][Y] == 150 && _BGG2.getTeam()) {
					KingX = X;
					KingY = Y;
				}
				if (_BGG2.iBackground[X][Y] == 250 && !_BGG2.getTeam()) {
					KingX = X;
					KingY = Y;
				}
			}
		}

		allowed = !_BGG2.SchachKing(_BGG2.getTeam(), _BGG2, KingX, KingY, true, false);

		_BGG[MP.X][MP.Y] = MP.ID;
		_BGG[MP.PX][MP.PY] = MP.ID2;
		if (MP.ID3 > 0) {
			_BGG[MP.X3][MP.Y3] = MP.ID3;
		}

		if (MP.ID4 > 0) {
			_BGG[MP.X4][MP.Y4] = MP.ID4;
			if (MP.X3 > 0) {
				_BGG[MP.X3][MP.Y3] = MP.ID3;
			}
			_BGG[MP.X5][MP.Y5] = MP.ID5;
		}

		return allowed;
	}

	/**
	 * Is the move method for AI and Player. This method follows the DRY
	 * principle (=Do not repeat yourself) This methode gives back all possible
	 * moves for one Meeple, as a MovePos ArrayList 
	 * -> one MovePos object is one possible move
	 * 
	 * @param BGG
	 *            - Board representation
	 * @param team
	 *            - which team
	 * @param iID
	 *            - for which meeple
	 * @param iX
	 *            - Meeple Pos-X
	 * @param iY
	 *            - Meeple Pos-Y
	 * @return MovePos ArrayList - All possible moves for one meeple
	 */
	public ArrayList<MovePos> getMoveMeeple(int[][] BGG, boolean team, int iID, int iX, int iY) {
		ArrayList<MovePos> MP = new ArrayList<MovePos>();
		if ((iID >= 100 && iID < 110 && team) || (iID >= 200 && iID < 210 && !team)) { // pawn
			if ((BGG[iX][iY + 1] == 0 && team) || (BGG[iX][iY - 1] == 0 && !team)) {
				MovePos MPN = new MovePos();
				MPN.ID = iID;
				MPN.ID2 = 0;
				MPN.PX = iX;
				if (team) {
					MPN.PY = iY + 1;
				} else {
					MPN.PY = iY - 1;
				}
				MPN.X = iX;
				MPN.Y = iY;
				MP.add(MPN);
			}
			if ((iY == 1 && BGG[iX][iY + 2] == 0 && BGG[iX][iY + 1] == 0 && team)
					|| (iY == 6 && BGG[iX][iY - 2] == 0 && BGG[iX][iY - 1] == 0 && !team)) {
				MovePos MPN = new MovePos();
				MPN.ID = iID;
				MPN.ID2 = 0;
				MPN.PX = iX;
				if (team) {
					MPN.PY = iY + 2;
				} else {
					MPN.PY = iY - 2;
				}
				MPN.X = iX;
				MPN.Y = iY;
				MP.add(MPN);
			}

			if (team) {
				int dBGrid = 0;
				int dBGrid2 = 0;
				if (iX - 1 >= 0 && iY + 1 <= 7) {
					dBGrid = Math.abs(BGG[iX][iY] - BGG[iX - 1][iY + 1]);
				}
				if (iX + 1 <= 7 && iY + 1 <= 7) {
					dBGrid2 = Math.abs(BGG[iX][iY] - BGG[iX + 1][iY + 1]);
				}

				if (dBGrid > 50 && BGG[iX - 1][iY + 1] > 160) {
					MovePos MPN = new MovePos();
					MPN.ID = iID;
					MPN.ID2 = BGG[iX - 1][iY + 1];
					MPN.PX = iX - 1;
					MPN.PY = iY + 1;
					MPN.X = iX;
					MPN.Y = iY;
					MP.add(MPN);
				}
				if (dBGrid2 > 50 && BGG[iX + 1][iY + 1] > 160) {
					MovePos MPN = new MovePos();
					MPN.ID = iID;
					MPN.ID2 = BGG[iX + 1][iY + 1];
					MPN.PX = iX + 1;
					MPN.PY = iY + 1;
					MPN.X = iX;
					MPN.Y = iY;
					MP.add(MPN);
				}
			} else {
				int dBGrid = 0;
				int dBGrid2 = 0;
				if (iX - 1 >= 0 && iY - 1 >= 0) {

					dBGrid = Math.abs(BGG[iX][iY] - BGG[iX - 1][iY - 1]);

				}
				if (iX + 1 <= 7 && iY - 1 >= 0) {
					dBGrid2 = Math.abs(BGG[iX][iY] - BGG[iX + 1][iY - 1]);
				}
				if (dBGrid > 50 && BGG[iX - 1][iY - 1] > 90 && BGG[iX - 1][iY - 1] < 200) {

					MovePos MPN = new MovePos();
					MPN.ID = iID;
					MPN.ID2 = BGG[iX - 1][iY - 1];
					MPN.PX = iX - 1;
					MPN.PY = iY - 1;
					MPN.X = iX;
					MPN.Y = iY;
					MP.add(MPN);
				}
				if (dBGrid2 > 50 && BGG[iX + 1][iY - 1] > 90 && BGG[iX + 1][iY - 1] < 200) {
					MovePos MPN = new MovePos();
					MPN.ID = iID;
					MPN.ID2 = BGG[iX + 1][iY - 1];
					MPN.PX = iX + 1;
					MPN.PY = iY - 1;
					MPN.X = iX;
					MPN.Y = iY;
					MP.add(MPN);
				}
			}
			if (_LastMove.size() > 0) { // En Passant
				int iIDLM = _LastMove.get(0).ID;
				int iDifM = Math.abs(_LastMove.get(0).PY - _LastMove.get(0).Y);
				if (iX + 1 <= 7) {
					int iX1 = BGG[iX + 1][iY];
					if ((iX1 >= 200 && iX1 < 210 && team)) {
						if ((iIDLM == iX1 && iDifM == 2)) {
							MovePos MPN = new MovePos();
							MPN.ID = iID;
							MPN.ID2 = BGG[iX + 1][iY + 1];
							MPN.ID3 = BGG[iX + 1][iY];
							MPN.PX = iX + 1;
							MPN.PY = iY + 1;
							MPN.X = iX;
							MPN.Y = iY;
							MPN.X3 = iX + 1;
							MPN.Y3 = iY;
							MP.add(MPN);
						}
					} else if ((iX1 >= 100 && iX1 < 110 && !team)) {
						if ((iIDLM == iX1 && iDifM == 2)) {
							MovePos MPN = new MovePos();
							MPN.ID = iID;
							MPN.ID2 = BGG[iX + 1][iY - 1];
							MPN.ID3 = BGG[iX + 1][iY];
							MPN.PX = iX + 1;
							MPN.PY = iY - 1;
							MPN.X = iX;
							MPN.Y = iY;
							MPN.X3 = iX + 1;
							MPN.Y3 = iY;
							MP.add(MPN);
						}
					}
				}
				if (iX - 1 >= 0) {
					int iX1 = BGG[iX - 1][iY];
					if ((iX1 >= 200 && iX1 < 210 && team)) {
						if ((iIDLM == iX1 && iDifM == 2)) {
							MovePos MPN = new MovePos();
							MPN.ID = iID;
							MPN.ID2 = BGG[iX - 1][iY + 1];
							MPN.ID3 = BGG[iX - 1][iY];
							MPN.PX = iX - 1;
							MPN.PY = iY + 1;
							MPN.X = iX;
							MPN.Y = iY;
							MPN.X3 = iX - 1;
							MPN.Y3 = iY;
							MP.add(MPN);
						}
					} else if ((iX1 >= 100 && iX1 < 110 && !team)) {
						if ((iIDLM == iX1 && iDifM == 2)) {
							MovePos MPN = new MovePos();
							MPN.ID = iID;
							MPN.ID2 = BGG[iX - 1][iY - 1];
							MPN.ID3 = BGG[iX - 1][iY];
							MPN.PX = iX - 1;
							MPN.PY = iY - 1;
							MPN.X = iX;
							MPN.Y = iY;
							MPN.X3 = iX - 1;
							MPN.Y3 = iY;
							MP.add(MPN);
						}
					}
				}
			}

		} else if ((iID >= 110 && iID < 120 && team) || (iID >= 210 && iID < 220 && !team)) { // rook
			boolean X1, X2, Y1, Y2;
			X1 = false;
			X2 = false;
			Y1 = false;
			Y2 = false;
			for (int iXHelp = 1; iXHelp < 8; iXHelp++) {

				if (iX + iXHelp <= 7 && !X1) {
					if ((((BGG[iX + iXHelp][iY] > 190 || BGG[iX + iXHelp][iY] == 0) && team)
							|| (BGG[iX + iXHelp][iY] < 200 && !team))) {
						MovePos MPN = new MovePos();
						MPN.ID = iID;
						MPN.ID2 = BGG[iX + iXHelp][iY];
						MPN.PX = iX + iXHelp;
						MPN.PY = iY;
						MPN.X = iX;
						MPN.Y = iY;
						MP.add(MPN);
						if (BGG[iX + iXHelp][iY] > 0) {
							X1 = true;
						}
					} else {
						X1 = true;
					}
				}

				if (iX - iXHelp >= 0 && !X2) {
					if ((((BGG[iX - iXHelp][iY] > 190 || BGG[iX - iXHelp][iY] == 0) && team)
							|| (BGG[iX - iXHelp][iY] < 200 && !team))) {
						MovePos MPN = new MovePos();
						MPN.ID = iID;
						MPN.ID2 = BGG[iX - iXHelp][iY];
						MPN.PX = iX - iXHelp;
						MPN.PY = iY;
						MPN.X = iX;
						MPN.Y = iY;
						MP.add(MPN);

						if (BGG[iX - iXHelp][iY] > 0) {
							X2 = true;
						}
					} else {
						X2 = true;
					}
				}
				if (iY - iXHelp >= 0 && !Y1) {
					if ((((BGG[iX][iY - iXHelp] == 0 || BGG[iX][iY - iXHelp] > 190) && team)
							|| (BGG[iX][iY - iXHelp] < 200 && !team))) {

						MovePos MPN = new MovePos();
						MPN.ID = iID;
						MPN.ID2 = BGG[iX][iY - iXHelp];
						MPN.PX = iX;
						MPN.PY = iY - iXHelp;
						MPN.X = iX;
						MPN.Y = iY;
						MP.add(MPN);
						if (BGG[iX][iY - iXHelp] > 0) {
							Y1 = true;
						}
					} else {
						Y1 = true;
					}
				}

				if (iY + iXHelp <= 7 && !Y2) {
					if ((((BGG[iX][iY + iXHelp] == 0 || BGG[iX][iY + iXHelp] > 190) && team)
							|| (BGG[iX][iY + iXHelp] < 200 && !team))) {

						MovePos MPN = new MovePos();
						MPN.ID = iID;
						MPN.ID2 = BGG[iX][iY + iXHelp];
						MPN.PX = iX;
						MPN.PY = iY + iXHelp;
						MPN.X = iX;
						MPN.Y = iY;
						MP.add(MPN);

						if (BGG[iX][iY + iXHelp] > 0) {
							Y2 = true;
						}
					} else {
						Y2 = true;
					}
				}

			}
		} else if ((iID >= 120 && iID < 130 && team) || (iID >= 220 && iID < 230 && !team)) {

			if (iX + 2 < 8 && iY + 1 < 8) {
				int iDif = Math.abs(BGG[iX + 2][iY + 1] - BGG[iX][iY]);
				if (iDif > 50) {
					MovePos MPN = new MovePos();
					MPN.ID = iID;
					MPN.ID2 = BGG[iX + 2][iY + 1];
					MPN.PX = iX + 2;
					MPN.PY = iY + 1;
					MPN.X = iX;
					MPN.Y = iY;
					MP.add(MPN);
				}
			}

			if (iX + 2 < 8 && iY - 1 >= 0) {
				int iDif = Math.abs(BGG[iX + 2][iY - 1] - BGG[iX][iY]);
				if (iDif > 50) {
					MovePos MPN = new MovePos();
					MPN.ID = iID;
					MPN.ID2 = BGG[iX + 2][iY - 1];
					MPN.PX = iX + 2;
					MPN.PY = iY - 1;
					MPN.X = iX;
					MPN.Y = iY;
					MP.add(MPN);
				}
			}

			if (iX + 1 < 8 && iY + 2 < 8) {
				int iDif = Math.abs(BGG[iX + 1][iY + 2] - BGG[iX][iY]);
				if (iDif > 50) {
					MovePos MPN = new MovePos();
					MPN.ID = iID;
					MPN.ID2 = BGG[iX + 1][iY + 2];
					MPN.PX = iX + 1;
					MPN.PY = iY + 2;
					MPN.X = iX;
					MPN.Y = iY;
					MP.add(MPN);
				}
			}

			if (iX + 1 < 8 && iY - 2 >= 0) {
				int iDif = Math.abs(BGG[iX + 1][iY - 2] - BGG[iX][iY]);
				if (iDif > 50) {
					MovePos MPN = new MovePos();
					MPN.ID = iID;
					MPN.ID2 = BGG[iX + 1][iY - 2];
					MPN.PX = iX + 1;
					MPN.PY = iY - 2;
					MPN.X = iX;
					MPN.Y = iY;
					MP.add(MPN);
				}
			}

			if (iX - 1 >= 0 && iY + 2 < 8) {
				int iDif = Math.abs(BGG[iX - 1][iY + 2] - BGG[iX][iY]);
				if (iDif > 50) {
					MovePos MPN = new MovePos();
					MPN.ID = iID;
					MPN.ID2 = BGG[iX - 1][iY + 2];
					MPN.PX = iX - 1;
					MPN.PY = iY + 2;
					MPN.X = iX;
					MPN.Y = iY;
					MP.add(MPN);
				}
			}

			if (iX - 1 >= 0 && iY - 2 >= 0) {
				int iDif = Math.abs(BGG[iX - 1][iY - 2] - BGG[iX][iY]);
				if (iDif > 50) {
					MovePos MPN = new MovePos();
					MPN.ID = iID;
					MPN.ID2 = BGG[iX - 1][iY - 2];
					MPN.PX = iX - 1;
					MPN.PY = iY - 2;
					MPN.X = iX;
					MPN.Y = iY;
					MP.add(MPN);
				}
			}

			if (iX - 2 >= 0 && iY + 1 < 8) {
				int iDif = Math.abs(BGG[iX - 2][iY + 1] - BGG[iX][iY]);
				if (iDif > 50) {
					MovePos MPN = new MovePos();
					MPN.ID = iID;
					MPN.ID2 = BGG[iX - 2][iY + 1];
					MPN.PX = iX - 2;
					MPN.PY = iY + 1;
					MPN.X = iX;
					MPN.Y = iY;
					MP.add(MPN);
				}
			}

			if (iX - 2 >= 0 && iY - 1 >= 0) {
				int iDif = Math.abs(BGG[iX - 2][iY - 1] - BGG[iX][iY]);
				if (iDif > 50) {
					MovePos MPN = new MovePos();
					MPN.ID = iID;
					MPN.ID2 = BGG[iX - 2][iY - 1];
					MPN.PX = iX - 2;
					MPN.PY = iY - 1;
					MPN.X = iX;
					MPN.Y = iY;
					MP.add(MPN);
				}
			}

		} else if ((iID >= 130 && iID < 140 && team) || (iID >= 230 && iID < 240 && !team)) { // bishop
			boolean D1, D2, D3, D4;
			D1 = false;
			D2 = false;
			D3 = false;
			D4 = false;
			for (int iHelp = 1; iHelp < 8; iHelp++) {

				if (iX + iHelp <= 7 && iY + iHelp <= 7 && !D1) {
					if (((BGG[iX + iHelp][iY + iHelp] == 0 || BGG[iX + iHelp][iY + iHelp] > 190) && team)
							|| (BGG[iX + iHelp][iY + iHelp] < 200 && !team)) {
						MovePos MPN = new MovePos();
						MPN.ID = iID;
						MPN.ID2 = BGG[iX + iHelp][iY + iHelp];
						MPN.PX = iX + iHelp;
						MPN.PY = iY + iHelp;
						MPN.X = iX;
						MPN.Y = iY;
						MP.add(MPN);

						if (BGG[iX + iHelp][iY + iHelp] > 0) {
							D1 = true;
						}
					} else {
						D1 = true;
					}
				}

				if (iX + iHelp <= 7 && iY - iHelp >= 0 && !D2) {
					if (((BGG[iX + iHelp][iY - iHelp] == 0 || BGG[iX + iHelp][iY - iHelp] > 190) && team)
							|| (BGG[iX + iHelp][iY - iHelp] < 200 && !team)) {
						MovePos MPN = new MovePos();
						MPN.ID = iID;
						MPN.ID2 = BGG[iX + iHelp][iY - iHelp];
						MPN.PX = iX + iHelp;
						MPN.PY = iY - iHelp;
						MPN.X = iX;
						MPN.Y = iY;
						MP.add(MPN);

						if (BGG[iX + iHelp][iY - iHelp] > 0) {
							D2 = true;
						}
					} else {
						D2 = true;
					}
				}

				if (iX - iHelp >= 0 && iY - iHelp >= 0 && !D3) {
					if (((BGG[iX - iHelp][iY - iHelp] == 0 || BGG[iX - iHelp][iY - iHelp] > 190) && team)
							|| (BGG[iX - iHelp][iY - iHelp] < 200 && !team)) {
						MovePos MPN = new MovePos();
						MPN.ID = iID;
						MPN.ID2 = BGG[iX - iHelp][iY - iHelp];
						MPN.PX = iX - iHelp;
						MPN.PY = iY - iHelp;
						MPN.X = iX;
						MPN.Y = iY;
						MP.add(MPN);

						if (BGG[iX - iHelp][iY - iHelp] > 0) {
							D3 = true;
						}
					} else {
						D3 = true;
					}
				}

				if (iX - iHelp >= 0 && iY + iHelp <= 7 && !D4) {
					if (((BGG[iX - iHelp][iY + iHelp] == 0 || BGG[iX - iHelp][iY + iHelp] > 190) && team)
							|| (BGG[iX - iHelp][iY + iHelp] < 200 && !team)) {
						MovePos MPN = new MovePos();
						MPN.ID = iID;
						MPN.ID2 = BGG[iX - iHelp][iY + iHelp];
						MPN.PX = iX - iHelp;
						MPN.PY = iY + iHelp;
						MPN.X = iX;
						MPN.Y = iY;
						MP.add(MPN);

						if (BGG[iX - iHelp][iY + iHelp] > 0) {
							D4 = true;
						}
					} else {
						D4 = true;
					}
				}

			}
		} else if ((iID >= 140 && iID < 150 && team) || (iID >= 240 && iID < 250 && !team)) {

			boolean X1, X2, Y1, Y2, D1, D2, D3, D4;
			X1 = false;
			X2 = false;
			Y1 = false;
			Y2 = false;
			D1 = false;
			D2 = false;
			D3 = false;
			D4 = false;
			for (int iHelp = 1; iHelp < 8; iHelp++) {

				if (iX + iHelp <= 7 && !X1) {
					if ((((BGG[iX + iHelp][iY] > 190 || BGG[iX + iHelp][iY] == 0) && team)
							|| (BGG[iX + iHelp][iY] < 200 && !team))) {

						MovePos MPN = new MovePos();
						MPN.ID = iID;
						MPN.ID2 = BGG[iX + iHelp][iY];
						MPN.PX = iX + iHelp;
						MPN.PY = iY;
						MPN.X = iX;
						MPN.Y = iY;
						MP.add(MPN);
						if (BGG[iX + iHelp][iY] > 0) {
							X1 = true;
						}
					} else {
						X1 = true;
					}
				}

				if (iX - iHelp >= 0 && !X2) {
					if ((((BGG[iX - iHelp][iY] > 190 || BGG[iX - iHelp][iY] == 0) && team)
							|| (BGG[iX - iHelp][iY] < 200 && !team))) {
						MovePos MPN = new MovePos();
						MPN.ID = iID;
						MPN.ID2 = BGG[iX - iHelp][iY];
						MPN.PX = iX - iHelp;
						MPN.PY = iY;
						MPN.X = iX;
						MPN.Y = iY;
						MP.add(MPN);

						if (BGG[iX - iHelp][iY] > 0) {
							X2 = true;
						}
					} else {
						X2 = true;
					}
				}
				if (iY - iHelp >= 0 && !Y1) {
					if ((((BGG[iX][iY - iHelp] == 0 || BGG[iX][iY - iHelp] > 190) && team)
							|| (BGG[iX][iY - iHelp] < 200 && !team))) {

						MovePos MPN = new MovePos();
						MPN.ID = iID;
						MPN.ID2 = BGG[iX][iY - iHelp];
						MPN.PX = iX;
						MPN.PY = iY - iHelp;
						MPN.X = iX;
						MPN.Y = iY;
						MP.add(MPN);
						if (BGG[iX][iY - iHelp] > 0) {
							Y1 = true;
						}
					} else {
						Y1 = true;
					}
				}

				if (iY + iHelp <= 7 && !Y2) {
					if ((((BGG[iX][iY + iHelp] == 0 || BGG[iX][iY + iHelp] > 190) && team)
							|| (BGG[iX][iY + iHelp] < 200 && !team))) {

						MovePos MPN = new MovePos();
						MPN.ID = iID;
						MPN.ID2 = BGG[iX][iY + iHelp];
						MPN.PX = iX;
						MPN.PY = iY + iHelp;
						MPN.X = iX;
						MPN.Y = iY;
						MP.add(MPN);

						if (BGG[iX][iY + iHelp] > 0) {
							Y2 = true;
						}
					} else {
						Y2 = true;
					}
				}

				if (iX + iHelp <= 7 && iY + iHelp <= 7 && !D1) {
					if (((BGG[iX + iHelp][iY + iHelp] == 0 || BGG[iX + iHelp][iY + iHelp] > 190) && team)
							|| (BGG[iX + iHelp][iY + iHelp] < 200 && !team)) {
						MovePos MPN = new MovePos();
						MPN.ID = iID;
						MPN.ID2 = BGG[iX + iHelp][iY + iHelp];
						MPN.PX = iX + iHelp;
						MPN.PY = iY + iHelp;
						MPN.X = iX;
						MPN.Y = iY;
						MP.add(MPN);

						if (BGG[iX + iHelp][iY + iHelp] > 0) {
							D1 = true;
						}
					} else {
						D1 = true;
					}
				}

				if (iX + iHelp <= 7 && iY - iHelp >= 0 && !D2) {
					if (((BGG[iX + iHelp][iY - iHelp] == 0 || BGG[iX + iHelp][iY - iHelp] > 190) && team)
							|| (BGG[iX + iHelp][iY - iHelp] < 200 && !team)) {
						MovePos MPN = new MovePos();
						MPN.ID = iID;
						MPN.ID2 = BGG[iX + iHelp][iY - iHelp];
						MPN.PX = iX + iHelp;
						MPN.PY = iY - iHelp;
						MPN.X = iX;
						MPN.Y = iY;
						MP.add(MPN);

						if (BGG[iX + iHelp][iY - iHelp] > 0) {
							D2 = true;
						}
					} else {
						D2 = true;
					}
				}

				if (iX - iHelp >= 0 && iY - iHelp >= 0 && !D3) {
					if (((BGG[iX - iHelp][iY - iHelp] == 0 || BGG[iX - iHelp][iY - iHelp] > 190) && team)
							|| (BGG[iX - iHelp][iY - iHelp] < 200 && !team)) {
						MovePos MPN = new MovePos();
						MPN.ID = iID;
						MPN.ID2 = BGG[iX - iHelp][iY - iHelp];
						MPN.PX = iX - iHelp;
						MPN.PY = iY - iHelp;
						MPN.X = iX;
						MPN.Y = iY;
						MP.add(MPN);

						if (BGG[iX - iHelp][iY - iHelp] > 0) {
							D3 = true;
						}
					} else {
						D3 = true;
					}
				}

				if (iX - iHelp >= 0 && iY + iHelp <= 7 && !D4) {
					if (((BGG[iX - iHelp][iY + iHelp] == 0 || BGG[iX - iHelp][iY + iHelp] > 190) && team)
							|| (BGG[iX - iHelp][iY + iHelp] < 200 && !team)) {
						MovePos MPN = new MovePos();
						MPN.ID = iID;
						MPN.ID2 = BGG[iX - iHelp][iY + iHelp];
						MPN.PX = iX - iHelp;
						MPN.PY = iY + iHelp;
						MPN.X = iX;
						MPN.Y = iY;
						MP.add(MPN);

						if (BGG[iX - iHelp][iY + iHelp] > 0) {
							D4 = true;
						}
					} else {
						D4 = true;
					}
				}

			}
		} else if ((iID == 150 && team) || (iID == 250 && !team)) {
			for (int Y = -1; Y <= 1; Y++) {
				for (int X = -1; X <= 1; X++) {
					if (iX + X >= 0 && iX + X < 8 && iY + Y < 8 && iY + Y >= 0) {
						if (((BGG[iX + X][iY + Y] == 0 || BGG[iX + X][iY + Y] > 160) && team)
								|| (BGG[iX + X][iY + Y] < 190 && !team)) {
							MovePos MPN = new MovePos();
							MPN.ID = iID;
							MPN.ID2 = BGG[iX + X][iY + Y];
							MPN.PX = iX + X;
							MPN.PY = iY + Y;
							MPN.X = iX;
							MPN.Y = iY;

							MP.add(MPN);
						}
					}
				}
			}
			if (iX - 4 >= 0) { // rochade left (4 wide)
				if (BGG[iX - 1][iY] == 0 && BGG[iX - 2][iY] == 0 && BGG[iX - 3][iY] == 0) {
					boolean Schach = false;
					for (int iXHelp = 0; iXHelp <= 4; iXHelp++) {
						if (_BGG2.SchachKing(team, _BGG2, iX - iXHelp, iY, true, true)) {
							Schach = true;
							break;
						}
					}

					if (!Schach) {
						int iIDRook = BGG[iX - 4][iY];
						if (!_BGG2.getbKingMoved(iID) && !_BGG2.getbRookMoved(iIDRook)) {
							MovePos MPN = new MovePos();
							MPN.ID = iID;
							MPN.ID2 = BGG[iX - 2][iY];
							MPN.ID3 = BGG[iX - 3][iY];
							MPN.ID4 = BGG[iX - 4][iY];
							MPN.ID5 = BGG[iX - 1][iY];
							MPN.PX = iX - 2;
							MPN.PY = iY;
							MPN.X = iX;
							MPN.Y = iY;
							MPN.X3 = iX - 3;
							MPN.Y3 = iY;
							MPN.X4 = iX - 4;
							MPN.Y4 = iY;
							MPN.X5 = iX - 1;
							MPN.Y5 = iY;
							MP.add(MPN);
						}
					}

				}
			}

			if (iX + 3 <= 7) { // rochade right (3 wide)
				if (BGG[iX + 1][iY] == 0 && BGG[iX + 2][iY] == 0) {
					boolean Schach = false;
					for (int iXHelp = 0; iXHelp <= 3; iXHelp++) {
						if (_BGG2.SchachKing(team, _BGG2, iX + iXHelp, iY, true, true)) {
							break;
						}
					}

					if (!Schach) {
						int iIDRook = BGG[iX + 3][iY];
						if (!_BGG2.getbKingMoved(iID) && !_BGG2.getbRookMoved(iIDRook)) {
							MovePos MPN = new MovePos();
							MPN.ID = iID;
							MPN.ID2 = BGG[iX + 2][iY];
							MPN.ID4 = BGG[iX + 3][iY];
							MPN.ID5 = BGG[iX + 1][iY];
							MPN.PX = iX + 2;
							MPN.PY = iY;
							MPN.X = iX;
							MPN.Y = iY;
							MPN.X4 = iX + 3;
							MPN.Y4 = iY;
							MPN.X5 = iX + 1;
							MPN.Y5 = iY;
							MP.add(MPN);
						}
					}

				}
			}
		}

		return MP;
	}

	/**
	 * Bauerntausch: if a pawn gets to the other side of the fied, than the
	 * player can change him to a Queen
	 * 
	 * TODO -Upgrade to fx
	 * 
	 * @param team-for
	 *            which team
	 * @param XX-on
	 *            which pos.
	 * @param YY-on
	 *            which pos.
	 */
	public void Bauerntausch(boolean team, int XX, int YY) {
		int Number;
		if (_BGG2.getTeam() == true) {
			_BGG2.higherQueenNumber();
			Number = _BGG2.getQueenNumber();
			_BGG2.setBackgroundGrid(XX, YY, Number + 140);
			_BGG2.iBackground[XX][YY] = 140 + Number;
		} else {
			_BGG2.higherQueenNumber();
			Number = _BGG2.getQueenNumber();
			_BGG2.setBackgroundGrid(XX, YY, Number + 240);
			TheMove[XX][YY] = 240 + Number;
		}
		_BGG2.setMove(true);


	}

	/**
	 * getSchach() simply returns a true _Blackschach when the Black king is in
	 * check and a _Whiteschach when the White king is in check. This method is
	 * use mainly for Moveing & Viewing
	 */
	public void getSchach() {

		int XKing2, YKing2, XKing1, YKing1;
		XKing2 = 10;
		XKing1 = 10;
		YKing1 = 10;
		YKing2 = 10;
		for (int Y = 0; Y < 8; Y++) {
			for (int X = 0; X < 8; X++) {
				if (TheMove[X][Y] == 150) {
					XKing1 = X;
					YKing1 = Y;
				}
				if (TheMove[X][Y] == 250) {
					XKing2 = X;
					YKing2 = Y;
				}
			}
		}

		_Blackschach = _BGG2.SchachKing(false, _BGG2, XKing2, YKing2, true, false);
		if (_Blackschach) {

		}
		_Whiteschach = _BGG2.SchachKing(true, _BGG2, XKing1, YKing1, true, false);
		if (_Whiteschach) {
		}

	}

	/**
	 * to check if a king is in real ''Schach''. Different output for each King Not for
	 * SCHACHMATT!!! Schachmatt is detected automatically, but you get it via
	 * BackgroundGrid.getSchachmatt()
	 */
	public void getSchach2() {

		boolean Blackschach, Whiteschach;
		int XKing2, YKing2, XKing1, YKing1;
		XKing2 = 10;
		XKing1 = 10;
		YKing1 = 10;
		YKing2 = 10;
		for (int Y = 0; Y < 8; Y++) {
			for (int X = 0; X < 8; X++) {
				if (TheMove[X][Y] == 150) {
					XKing1 = X;
					YKing1 = Y;
				}
				if (TheMove[X][Y] == 250) {
					XKing2 = X;
					YKing2 = Y;
				}
			}
		}
		_BGG2.iBackground = TheMove;

		Blackschach = _BGG2.SchachKing(false, _BGG2, XKing2, YKing2, false, false);
		if (Blackschach == true && !_BGG2.getSchachmattBlack()) {
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					try {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("check");
						alert.setHeaderText("Blackking is in check!");
						alert.setContentText("Blackking is in check!");
						alert.showAndWait();

					} catch (Exception ex) {
						ex.printStackTrace();
					}

				}
			});

		}
		System.out.println("1-Mated Black::"+_BGG2.getSchachmattBlack()+"::Mated White::"+_BGG2.getSchachmattWhite());
		Whiteschach = _BGG2.SchachKing(true, _BGG2, XKing1, YKing1, false, false);
		System.out.println("2-Mated Black::"+_BGG2.getSchachmattBlack()+"::Mated White::"+_BGG2.getSchachmattWhite());
		if (Whiteschach == true && !_BGG2.getSchachmattWhite()) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					try {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("check");
						alert.setHeaderText("Whiteking is in check!");
						alert.setContentText("Whiteking is in check!");
						alert.showAndWait();

					} catch (Exception ex) {
						ex.printStackTrace();
					}

				}
			});

		}

		System.out.println("Mated Black::"+_BGG2.getSchachmattBlack()+"::Mated White::"+_BGG2.getSchachmattWhite());

	}

	/**
	 * @param int[][]
	 *            BGG - is a int[][] - to set the Background Array
	 */
	public void setBGG(int[][] BGG) {
		_BGG = BGG;
	}

	/**
	 * @param boolean
	 *            sel - set boolean _bSelect (if a Meeple has been selected)
	 */
	public void setBSelect(boolean sel) {
		_bSelect = sel;
	}

	/**
	 * @return int[][] _BGG - returns the BackgroundGrid,Grid
	 */
	public int[][] getBGG() {
		return _BGG;
	}

	/**
	 * @return int _iSelect - Returns the number of the previous selected Meeple
	 */
	public int getISelect() {
		return _iSelect;
	}

	/**
	 * @return boolean _beSelect - returns if a Meeple has been selected
	 */
	public boolean getBSelect() {
		return _bSelect;
	}

	/**
	 * @return boolean _Bauerntausch - returns true if a Bauerntausch is
	 *         currently happening
	 */
	public boolean getBauer() {
		return _Bauerntausch;
	}

	/**
	 * @return ArrayList<int[]> MoveList - The List for the possible Moves
	 */
	public ArrayList<int[]> getMoveList() {
		return _MoveList;
	}

	/**
	 * Sets the move list
	 * @param newMovelist
	 */
	public void setMoveList(ArrayList<int[]> newMovelist){
		_MoveList = newMovelist;
	}

	/**
	 * @return ArrayList<int[]> HitList - The List for the possible strikes
	 */
	public ArrayList<int[]> getHitList() {
		return _HitList;
	}

	/**
	 * 
	 * @return ArrayList<int[]> - LastMoveList - List where you have last moved
	 */
	public ArrayList<int[]> getLastMoveList() {
		return _LastMoveList;
	}

	/**
	 * Sets the last move list
	 * @param newLastMoveList - ArrayList<int[]>
	 */
	public void setLastMoveList(ArrayList<int[]> newLastMoveList){
		_LastMoveList = newLastMoveList;
	}

	/**
	 * @return int _iPosX - Returns the previous selected Meeples X Position in
	 *         8x8
	 */
	public int getIPosX() {
		return _iPosX;
	}

	/**
	 * overrides the last x pos
	 * @param x - the x value of the meeple
	 */
	public void setIPosX(int x) {
		_iPosX = x;
	}

	/**
	 * overrides the last y pos
	 * @param y - the y value of the meeple
	 */
	public void setIPosY(int y) {
		_iPosY = y;
	}

	/**
	 * @return int _iPosY - Returns the previous selected Meeples Y Position in
	 *         8x8
	 */
	public int getIPosY() {
		return _iPosY;
	}

	/**
	 * 
	 * @return _BGG2 - backgroundGrid - returns the BackgroundGrid object - used
	 *         for drawing
	 */
	public BackgroundGrid getBGG2() {
		return _BGG2;
	}

	/**
	 * Set the Backgroundgrid
	 * 
	 * @param BGG
	 *            - object of class BackgroundGrid
	 */
	public void setBGG2(BackgroundGrid BGG) {
		_BGG2 = BGG;
	}



	/**
	 * 
	 * @param ID - int - overrides the last meeple
	 */
	public void setLastID(int ID) {
		_iSelect = ID;
	}

	/**
	 * for the launchpad - if move has happenend
	 * @return boolean - if move has happenend
	 */
	public boolean getMoveAlowed() {
		return _moveAllowed;
	}
}
