package javachess.game;

/**
 * @author alexl4123 - 2018
 * @version 2.0 - release
 * 
 * Class for the local gamemode 
 * It simply switches teams
 *
 */
public class Local {
	
	/**
	 * boolean for the team in the local gamemode
	 * Attention: The BackgroundGrid team must be updated, if this one changes
	 */
	private boolean _team;
	
	/**
	 * a debug turn round number
	 */
	private int _count;
	
	/**
	 * @return int _count - gets the debug turn round
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
	 * @param i - for debbuging
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
