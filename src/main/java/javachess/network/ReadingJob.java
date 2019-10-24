package javachess.network;

import javachess.gui.GUI;

/**
 * 
 * @author mhub - 2018
 * @version 2.0
 * 
 * This class is used to parallely read from sockets, so u can use the gui meanwhile reading. 
 * Maybe looks strange, but if the 'Trigger' is changed, Code from gui will be executed in this thread, not in gui - thread. 
 * Implements Runable - so it is a trheadjob. 
 *
 */
public class ReadingJob implements Runnable {
	
	
	/**
	 * GUI for Triggering
	 */
	private GUI theGui;

	/**
	 * Thre Constructor.
	 * @param newGui - The current GUI - instance
	 */
	public ReadingJob(GUI newGui){
		
		this.theGui = newGui;
	}

	@Override
	public void run() {
		
		System.out.println("reading Job running");
				
			try {
				
				if (theGui.getBoardGui().getBthinking()== true){
				System.out.println("read in reader thread");
				theGui.getBoardGui().BGGChange.set(theGui.getBoardGui().BGGChange.get()+1);		//Chaning the trigger so the code from gui is executed here.
				System.out.println("Trigger Value changed");
				
				}				
			} catch (Exception e) {

			}	

		}

}
