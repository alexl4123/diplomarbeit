 package threadJobs;

import java.awt.Color;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import GuiStuff.BoardGui;
import javachess.backgroundmatrix.BackgroundGrid;

/**
 * @author alexl12
 * @version 1.1 - Draw
 * 
 * Not needed in 1.1
 * 
 * A thread that only waits for the move of the other player
 *  for reading until the other player has finished his turn.
 *
 */
public class ThreadForReading implements Runnable {

	BackgroundGrid BGG; //BackgroundGrid of the Gui
	BoardGui BG;//Board Gui

	/**
	 * Def. const. 
	 * sets the BackgroundGrid and BoardGui
	 * @param BGG1-BackgroundGrid
	 * @param BG1-BoardGui
	 */
	public ThreadForReading(BackgroundGrid BGG1,BoardGui BG1) {
		BGG = BGG1;
		BG = BG1;
	
	}

	@Override
	/**
	 * run for Thread
	 */
	public void run() {


		//nearly endless loop
		while (true){

			
			
			try {
				Object o=BG.getOIS().readObject();
				
				//if the client leaves the game
				if(BG.servSock!=null&&o.equals("end")){
					JFrame Frame1 = new JFrame("a frame");
					JOptionPane.showMessageDialog(Frame1, "The client connection has been interrupted!", "CONNECTION INTERUPTION", 0);
					System.out.println("Connection interrupting!!!");
					BG.getNetworkMenu().remove(BG.closeServ);
					System.out.println("connection interrupted!!");
					BG.getOIS().close();
					BG.getOOS().close();
					BG.servSock.close();
					BG.servSock=null;
					BGG.getLan().setIsConnectet(false);
					BGG.setMove(true);
					BG.Frame.setVisible(false);
					BG.getTextArea().setText("");
					break;
					
				//if the host leaves the game	
				}else if(BG.servSock==null&&o.equals("end")){
					JFrame Frame1 = new JFrame("a frame");
					JOptionPane.showMessageDialog(Frame1, "The host connection has been interrupted!", "CONNECTION INTERUPTION", 0);
					BG.getOIS().close();
					BG.getOOS().close();
					BG.clientSock2.close();
					BG.clientSock2=null;
					BGG.getLan().setIsConnectet(false);
					BGG.setMove(true);
					break;
					
					
					
					
					
				}
				
				
				
				System.out.println("I AM LISTENING");
				BGG =(BackgroundGrid) o;
			//	BG.consolefield.append(o.toString());
				
				//if opponent has moved => get overwritted panel
				if(BGG.getMove()==false){
					BGG.setName("NEWNAME");
					BG.setBackgroundGridRead(BGG);
					BG.ChessPanel.renewPanel(0, 0,false);
					BG.ChessPanel.getSchach();
					if(BGG.getLan().getIsConnectet()==true){
						BG.con2.setBackground(Color.green);
					}else{
						BG.con2.setBackground(Color.red);
					}
					if(BGG.getTeam()==true){
						BG.player2.setBackground(Color.white);
					}else{
						BG.player2.setBackground(Color.black);
					}
					break;
				}
				
			} catch (ClassNotFoundException e) {
				System.out.println("fail1");
				break;
				
			} catch (IOException e) {
				System.out.println("fail2");
	
			break;
			}

		}
		

	}
}
