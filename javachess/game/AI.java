package javachess.game;

import java.util.ArrayList;

import javax.swing.JFrame;
import javachess.backgroundmatrix.BackgroundGrid;
import javachess.gui.BoardGui;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.util.concurrent.ThreadLocalRandom;

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
		if (bStart) {
			bStart = false;

			AILogic AIL = new AILogic();
			BackgroundGrid BGGX = _BGG2;

			//here the real AI is called
			float fx = AIL.alphaBeta(_depth, _BGG2, _AiTeam);
			int iBeta;

			//System.out.println("THE COMPUTER:"+fx);

			try {


				for(int i = AIL.BestMove.size()-1; i>=0; i--){
					//to be sure that nothing fails
					if(i < 0){
						i=0;
					}
					iBeta = AIL.BestMove.get(i).Beta;
					//If some equivalent moves possible - do random

					ArrayList<MovePos> BestMoves = new ArrayList<MovePos>();
					MovePos A;
					if(AIL.BestMove.size()>1 && i==AIL.BestMove.size()-1 && !_BGG2.getHardCoreAI()){
						for(int iRand = AIL.BestMove.size()-1; iRand>=0; iRand--){
							if(iBeta == AIL.BestMove.get(iRand).Beta){
								BestMoves.add(AIL.BestMove.get(iRand));
							}
						}
						if(BestMoves.size() > 1){
							int randomNum = ThreadLocalRandom.current().nextInt(0, BestMoves.size());
							A = BestMoves.get(randomNum);
							System.out.println("Equivalent AI Moves, so one has been picked randomly - Rand Num -"+randomNum);
						}else{
							A = AIL.BestMove.get(i);
							System.out.println("No equivalent AI Moves but first try");
						}

					}else{
						//No random equivalent moves
						A = AIL.BestMove.get(i);
						System.out.println("Default AI Move");
					}

					//save the curr. board
					//the complicity is needed, due to some pointer errors
					int[][] iBoardSave = new int[8][8];
					for(int iSY = 0; iSY < 8; iSY++){
						for(int iSX = 0; iSX < 8; iSX++){
							iBoardSave[iSX][iSY] = _BGG2.iBackground[iSX][iSY];
						}

					}


					//--------------------------------------------------------------
					//gets the last ,,best'' move, because this one is ever the best


					_BGG2.iBackground[A.PX][A.PY] = A.ID;
					_BGG2.iBackground[A.X][A.Y] = 0; // makes the move
					
					if(A.ID==150||A.ID==250){
						_BGG2.setbKingMoved(A.ID, true);
					}
					
					if((110 < A.ID && A.ID <= 119) || (210 < A.ID && A.ID <=219)){
						_BGG2.setbRookMoved(A.ID,true);
					}
					
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
					//-------------------------------------------------------
					//Check if it was a legal move

					int XKing, YKing;
					XKing = 10;
					YKing = 10;

					for(int Y = 0; Y < 8; Y++){
						for(int X = 0; X < 8; X++){
							if(_BGG2.iBackground[X][Y] == 150 && _AiTeam){
								XKing = X;
								YKing = Y;
							}
							if(_BGG2.iBackground[X][Y] == 250 && !_AiTeam){
								XKing = X;
								YKing = Y;
							}
						}
					}

					if(!_BGG2.SchachKing(_AiTeam, _BGG2, XKing, YKing, true, false)){
						//if good AI Move then end AI Move


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
						_BGG2.higherTurnRound();
						_BGG2.addTeamState(!_AiTeam);


						int[] LML = new int[8];
						LML[0] = A.X +  (A.Y * 8);
						LastMoveList.add(LML);
						int[] LML1 = new int[8];
						LML1[0] = A.PX  + (A.PY * 8);

						//TODO -check if needed
						LastMoveList.add(LML1);
						LastMoveList.add(LML);

						bRunning = false;
						System.out.println("Break - line 211 - AI.java");
						break;





					}
					System.out.println("Line 219 - AI.java");

					//------------------------------------------------
					//if it was no legal move, reverse the fields
					//add the board states
					//the complicity is needed, due to same pointer errors
					for(int iSSY = 0; iSSY < 8; iSSY++){
						for(int iSSX = 0; iSSX < 8; iSSX++){
							_BGG2.iBackground[iSSX][iSSY] = iBoardSave[iSSX][iSSY];
						}

					}
					System.out.println("No legal move - AI.java - line 231 - MoveID -"+A.ID+"-MovePosX-"+A.PX+"-MovePosY-"+A.PY+"-i-"+i);



					if(i==0){
						//if the AI has no moves left
							Platform.runLater(new Runnable() {

								@Override
								public void run() {
									getSchach();

								}

							});
						if(!_BGG2.getSchachmattWhite() && !_BGG2.getSchachmattBlack()){
							Platform.runLater(new Runnable() {

								@Override
								public void run() {
									try {
										Alert alert = new Alert(AlertType.INFORMATION);
										alert.setTitle("Check Mate");

										alert.setHeaderText("The AI has lost the game! It has no legal moves left... The game took " + _BGG2.getTurnRound() + " turns.");
										alert.showAndWait();

									} catch (Exception ex) {
										ex.printStackTrace();
									}

								}
							});
						}


						if(_AiTeam){
							_BGG2.setSchachmattWhite(true);
						}else{
							_BGG2.setSchachmattBlack(true);
						}


						break;
					}
				}







			}catch(Exception ex) {
				ex.printStackTrace();

				bRunning = false;
			}


		}


		//that the player may move
		_Gui.setThinking(false);
		_Gui.redraw();
		//when the two AIs play against each other, no check -> improves performance drastically
		if(!_AIvsAI && !_BGG2.getSchachmattBlack() && !_BGG2.getSchachmattWhite()) {
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
