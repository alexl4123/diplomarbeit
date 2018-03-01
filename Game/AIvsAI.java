package Game;

import java.util.concurrent.TimeUnit;

import BackgroundMatrix.BackgroundGrid;
import Gui.BoardGui;

public class AIvsAI extends Thread {
	
	private BackgroundGrid _BGG2;
	
	private BoardGui _BG;
	
	public boolean bAI_Thinking_White;
	public boolean bAI_Thinking_Black;
	
	public AIvsAI(BackgroundGrid BGG2, BoardGui BG) {
		bAI_Thinking_White = false;
		bAI_Thinking_Black = false;
		_BGG2 = BGG2;
		_BG = BG;
	}
	
	public void run() {
		boolean isRunning = true;
		AILogic AIL = new AILogic();
		
		do {
			
				
				if(!bAI_Thinking_Black) {
					bAI_Thinking_White = true;
					_BG.setThinking(true);
					AI _AI_White = new AI(_BGG2, _BG,true,true);
					_AI_White.setAIvsAI(this);
					_AI_White.start();
				}
				
				while(bAI_Thinking_White) {
					try {
						Thread.currentThread().sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				if(!bAI_Thinking_White) {
					bAI_Thinking_Black = true;
					AI _AI_Black = new AI(_BGG2, _BG,false,true);
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
				
				
			
		}while(7000 > AIL.boardEvaluation(_BGG2, true) && (-7000) < AIL.boardEvaluation(_BGG2, true));
		System.out.println("SkyNet halted");
		
	}
}
