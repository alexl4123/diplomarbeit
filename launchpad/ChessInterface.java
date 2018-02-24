package launchpad;

public class ChessInterface {
	
	private Interface_class _interface_Class;
	// move + redraw
	public boolean moveFig(int x, int y, int x2, int y2) {
		_interface_Class.moveFig(x, y, x2, y2);
		return true;
	}

	public int getFig(int x, int y) {
		return _interface_Class.getMeepleID(x, y);
	}

	public int[][] getBoard() {
		return _interface_Class.getboard();
	}

	public boolean isOnlineGame() {
		//currently not possible
		return false;
	}

	// online
	public boolean ismyTeamColorWhite() {
		return false;
	}
	
	//online
	public boolean ismyTeamColorBlack() {
		return false;
	}

	// return if this team has its turn
	public boolean ismyTurn(int x, int y) {
		return _interface_Class.isMyTurn(x, y);
	}

	public boolean isWhiteTurn() {
		return _interface_Class.getBGG2().getTeam();
	}

	public boolean isBlackTurn() {
		if(_interface_Class.getBGG2().getTeam()){
			return false;
		}
		return true;
	}
	
	
	public void setupinterfaceclass(Interface_class interface_class){
		_interface_Class = interface_class;
		//do some setup
	}
	

}
