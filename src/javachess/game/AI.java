package javachess.game;

import java.util.ArrayList;

import javax.swing.JFrame;
import javachess.backgroundmatrix.BackgroundGrid;
import javachess.gui.BoardGui;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * @author alex - 2017
 * @version 1.1 - Draw
 * 
 *	Class AI extends Thread.
 *	If the thread AI is started,
 *	it starts the AI process.
 *	Calls in class AILogic the AlphaBeta part
 * 
 * 
 *
 */
public class AI extends Thread {

	/**
	 * Contains the position of the meeples
	 */
	static public BackgroundGrid _BGG2;

	/**
	 * BoardGui - for redrawing the Gui
	 */
	static public BoardGui _Gui;

	/**
	 * boolean _Blackschach - if the black king is checked
	 * boolean _Whiteschach - if the white king is checked
	 */
	static private boolean _Blackschach, _Whiteschach;

	/**
	 * another way to not make the AI do its shit
	 */
	boolean bStart = false;

	/**
	 * ArrayList<int[]> - to save the last move of the AI
	 */
	private ArrayList<int[]> LastMoveList = new ArrayList<int[]>();

	/**
	 * The team the AI plays
	 */
	private boolean _AiTeam;

	/**
	 * A boolean variable indicating whether the AICaller class is running. This
	 * prevents the possibility of two threads simultaneously manipulating the
	 * chess board. This should never happen, but one can never be too safe.
	 */
	private static boolean bRunning = false;

	private boolean _AIvsAI;

	private AIvsAI _AIFuckUp;

	private int _depth;

	/**
	 * The constructor
	 * @param BGG2 - BackgroundGrid - sets the location of the meeples
	 * @param Gui - Gui - for redrawing the Gui
	 */
	public AI(BackgroundGrid BGG2, BoardGui Gui, boolean AI_Team, boolean AIvsAI, int depth) {
		this._BGG2 = BGG2;
		this._Gui = Gui;
		this._AiTeam = AI_Team;
		_AIvsAI = AIvsAI;
		_depth = depth;
	}

	/**
	 * This function is called when a player moves a piece and it is the AI's
	 * turn to make a move. It simply processes the search in a separate thread
	 * so the application is not locked up. The thread will exit if
	 * chess.bThinking is falsified.
	 */
	public void run() {
		if (bRunning)
			return;
		bRunning = true;
		bStart = true;
		while (bRunning) {
			if (bStart) {
				bStart = false;

				AILogic AIL = new AILogic();
				BackgroundGrid BGGX = _BGG2;

				//here the real AI is called
				float fx = AIL.alphaBeta(_depth, _BGG2, _AiTeam);


				//System.out.println("THE COMPUTER:"+fx);
				//if((fx > -5000) && (fx < 5000)){
					try {
						int i = AIL.BestMove.size();

						//gets the last ,,best'' move, because this one is ever the best
						MovePos A = AIL.BestMove.get(i - 1);
						_BGG2.iBackground[A.PX][A.PY] = A.ID;
						_BGG2.iBackground[A.X][A.Y] = 0; // makes the move
						if (A.ID3 > 0) {
							_BGG2.iBackground[A.X3][A.Y3] = 0;
						}

						if (A.ID4 > 0) {
							_BGG2.iBackground[A.X4][A.Y4] = 0;
							if (A.X3 > 0) {
								_BGG2.iBackground[A.X3][A.Y3] = 0;
							}
							_BGG2.iBackground[A.X5][A.Y5] = A.ID4;
							_BGG2.setbRookMoved(A.ID, true);
							_BGG2.setbKingMoved(A.ID, true);
							int[] iRoch1 = new int[1];
							int[] iRoch2 = new int[1];
							iRoch1[0] = A.X5 + (8*A.Y5);
							iRoch2[0] = A.X4 + (8*A.Y4);

							LastMoveList.add(iRoch1);
							LastMoveList.add(iRoch2);
						}

						if(A.ID >= 100 && A.ID < 110 && _BGG2.getTeam() && A.PY == 7){
							_BGG2.higherQueenNumber();
							_BGG2.iBackground[A.PX][A.PY] = 140+ _BGG2.getQueenNumber();
						} else if(A.ID >= 200 && A.ID < 210 && !_BGG2.getTeam() && A.PY == 0){
							_BGG2.higherQueenNumber();
							_BGG2.iBackground[A.PX][A.PY] = 240+ _BGG2.getQueenNumber();
						}





						_BGG2.higherTurnRound();

						//add the board states
						//the complicity is needed, due to same pointer errors
						int[][] iBoard = new int[8][8];
						for(int iHY = 0; iHY < 8; iHY++){
							for(int iHX = 0; iHX < 8; iHX++){
								iBoard[iHX][iHY] = _BGG2.iBackground[iHX][iHY];
							}

						}
						_BGG2.addBoardState(iBoard);
						//add the team states

						_BGG2.addTeamState(!_AiTeam);




						int[] LML = new int[8];
						LML[0] = A.X +  (A.Y * 8);
						LastMoveList.add(LML);
						int[] LML1 = new int[8];
						LML1[0] = A.PX  + (A.PY * 8);

						LastMoveList.add(LML1);
						LastMoveList.add(LML);

					}catch(Exception ex) {
						ex.printStackTrace();
					}
				/*}else if(_AiTeam){ //if somebody is mated

					System.out.println("_AiTeam");
					if(fx>5000) {
						_BGG2.setSchachmattBlack(true);
					}else if(fx<-5000) {
						_BGG2.setSchachmattWhite(true);
					}
				}else if(!_AiTeam) {
					System.out.println("!_AiTeam");
					if(fx>5000) {
						_BGG2.setSchachmattWhite(true);
					}else if(fx<-5000) {
						_BGG2.setSchachmattBlack(true);
					}
				}*/

			}
			bRunning = false;
		}
		//that the player may move
		_Gui.setThinking(false);
		_Gui.redraw();
		//when the two AIs play against each other, no check -> improves performance drastically
		if(!_AIvsAI) {
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					getSchach();

				}

			});
		}

		/*
		 * In AIvsAI mode it is necessary to know, when the other AI is thinking
		 * so the other AI wont try to move
		 */
		if(_AIvsAI && _AiTeam) {
			_AIFuckUp.bAI_Thinking_White = false;
		} else if(_AIvsAI && !_AiTeam) {
			_AIFuckUp.bAI_Thinking_Black = false;
		}


		bRunning = false;

	}

	/**
	 * to check if a king is in "Schach". Different output for each King Not for
	 * SCHACHMATT!!! Schachmatt is detected automatically, but you get it via
	 * BackgroundGrid.getSchachmatt()
	 */
	public void getSchach() {
		JFrame Frame1 = new JFrame();

		int XKing2, YKing2, XKing1, YKing1;
		XKing2 = 10;
		XKing1 = 10;
		YKing1 = 10;
		YKing2 = 10;
		for(int Y = 0; Y < 8; Y++){
			for(int X = 0; X < 8; X++){
				if(_BGG2.iBackground[X][Y] == 150){
					XKing1 = X;
					YKing1 = Y;
				}
				if(_BGG2.iBackground[X][Y] == 250){
					XKing2 = X;
					YKing2 = Y;
				}
			}
		}


		_Blackschach = _BGG2.SchachKing(false, _BGG2, XKing2, YKing2, false, false);
		if (_Blackschach && !_BGG2.getSchachmattBlack()) {
			System.out.println("BlackCheck");
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("check");
			alert.setHeaderText("Blackking is in check!");
			alert.setContentText("Blackking is in check!");
			alert.showAndWait();
		}
		_Whiteschach = _BGG2.SchachKing(true, _BGG2, XKing1, YKing1, false, false);
		if (_Whiteschach == true  && !_BGG2.getSchachmattWhite()) {
			System.out.println("WhiteCHeck");
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("check");
			alert.setHeaderText("Whiteking is in check!");
			alert.setContentText("Whiteking is in check!");
			alert.showAndWait();
		}

	}



	/**
	 * 
	 * @return - BackgroundGrid - returns the changed BackgroundGrid
	 */
	public BackgroundGrid getBGG() {
		return _BGG2;
	}

	public void setAIvsAI(AIvsAI AIFuckup) {
		_AIFuckUp = AIFuckup;
	}

	/**
	 * 
	 * @return - ArrayList<int[]> - returns the last move of the AI
	 */
	public ArrayList<int[]> getLMoveList(){
		return LastMoveList;
	}
}
