package Game;
import BackgroundMatrix.BackgroundGrid;
import Gui.*;
import javafx.stage.Stage;


/**
 * @author alexl12
 * @version 1.1 - Draw
 * 
 * Class for the local gamemode 
 * It simply switches teams
 * 
 *
 */
public class Local {
	
	private boolean _team;
	private int _count;
	
	/**
	 * Default constructor does NOTHING
	 */
	public Local(){
		
	}
	
	/**
	 * @return int _count - 
	 */
	public int getCount(){
		return _count;
	}
	
	/**
	 * 
	 * @return boolean _team - returns the current team 
	 */
	public boolean getTeam(){
		return _team;
	}
	
	/**
	 * For starting the Local Gamemode - White team begins
	 */
	public void startUpLocal(){
		_team = true;
	}
	
	/**
	 * @param i - why do i have this?
	 */
	public void setCount(int i ){
		_count = i;
	}
	
	/**
	 * @param boolean team - sets the teams - white is true, black is false
	 */
	public void setTeam(boolean team){
		_team = team;
	}
	
	/**
	 * simply switches the teams
	 */
	public void switchTeam(){
		if(_team){
			_team = false;
		}else{
			_team = true;
		}
	}
	
}
