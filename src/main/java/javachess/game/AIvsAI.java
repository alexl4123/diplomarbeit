package javachess.game;

import javachess.backgroundmatrix.BackgroundGrid;
import javachess.gui.BoardGui;

/**
 * @author alexl4123 - 2018
 * @version 2.0 - release
 * 
 * This class is used for the ,,secret'' GameMode (type SkyNet into the IP-Addresse field).
 * Here the AI plays against the AI. 
 * This class extends Thread, so i won't affect the other game.
 */
public class AIvsAI extends Thread {
	
	/**
	 * Object of BackgroundGrid - used for Board states
	 */
	private BackgroundGrid _BGG2;
	
	/**
	 * Object of the BoardGui - used for updating GUI
	 */
	private BoardGui _BG;
	
	/**
	 * Is set to true, when the white AI calculates a turn, so the black AI won't fire
	 */
	public boolean bAI_Thinking_White;
	
	/**
	 * Is set to true, when the black AI calculates a turn
	 */
	public boolean bAI_Thinking_Black;
	
	/**
	 * This is the constructor for the AIvsAI mode - sets the _BG and _BGG2
	 * @param BGG2 sets _BGG2
	 * @param BG sets _BG
	 */
	public AIvsAI(BackgroundGrid BGG2, BoardGui BG) {
		bAI_Thinking_White = false;
		bAI_Thinking_Black = false;
		_BGG2 = BGG2;
		_BG = BG;
	}
	
	/**
	 * when the new thread is fired,
	 * this is executed. 
	 * So here the AI plays truely against itself
	 * At first the white AI is called, then the black and so on
	 */
	public void run() {
		_BG.setThinking(true);
		boolean isRunning = true;
		AILogic AIL = new AILogic();
		boolean bSchachMattW = false;
		boolean bSchachMattB = false;
		
		do {
			
				//init white AI
				if(!bAI_Thinking_Black) {
					bAI_Thinking_White = true;
					AI _AI_White = new AI(_BGG2, _BG,true,true,_BGG2.getAiDepth());
					_AI_White.setAIvsAI(this);
					_AI_White.start();
				}
				
				//wait while thinking
				while(bAI_Thinking_White) {
					try {
						Thread.currentThread().sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				int KingX;
				int KingY;
				for(int iY = 0; iY < 8; iY++){
					for(int iX = 0; iX < 8; iX++){
						if(_BGG2.iBackground[iX][iY] == 150){
							KingX = iX;
							KingY = iY;
							bSchachMattW = _BGG2.SchachKing(true, _BGG2, KingX, KingY, false, false);
						}else if(_BGG2.iBackground[iX][iY] == 250){
							KingX = iX;
							KingY = iY;
							bSchachMattB = _BGG2.SchachKing(false, _BGG2, KingX, KingY, false, false);
						}
					}
				}
				if(_BGG2.getSchachmattBlack() || _BGG2.getSchachmattWhite()){
					break;
				}
				
				if(!bAI_Thinking_White) {
					bAI_Thinking_Black = true;
					AI _AI_Black = new AI(_BGG2, _BG,false,true,_BGG2.getAiDepth());
					_AI_Black.setAIvsAI(this);
					_AI_Black.start();
				}
				
				while(bAI_Thinking_Black) {
					try {
						Thread.currentThread().sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				for(int iY = 0; iY < 8; iY++){
					for(int iX = 0; iX < 8; iX++){
						if(_BGG2.iBackground[iX][iY] == 150){
							KingX = iX;
							KingY = iY;
							bSchachMattW = _BGG2.SchachKing(true, _BGG2, KingX, KingY, false, false);
						}else if(_BGG2.iBackground[iX][iY] == 250){
							KingX = iX;
							KingY = iY;
							bSchachMattB = _BGG2.SchachKing(false, _BGG2, KingX, KingY, false, false);
						}
					}
				}
				
				
				
			
		}while(15000 > AIL.boardEvaluation(_BGG2.iBackground, true) && (-15000) < AIL.boardEvaluation(_BGG2.iBackground, true) && !_BGG2.getSchachmattBlack() && !_BGG2.getSchachmattWhite() && _BGG2.getTurnRound()<250);
		System.out.println("SkyNet halted:BOARD:"+AIL.boardEvaluation(_BGG2.iBackground, true)+"::"+bSchachMattB+"::"+bSchachMattW);
		
	}
}
