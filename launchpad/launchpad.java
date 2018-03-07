package launchpad;

import BackgroundMatrix.BackgroundGrid;
import Gui.BoardGui;

/**
 * Class For Hold and Klotz
 * @Hold and @Klotz you have to do the setInterface_Class methode in BoardGui
 * @author alexl12
 *
 */
public class launchpad {
	
	private BoardGui _BG;
	private BackgroundGrid _BGG;
	private interface_class _Lauch;
	
	/**
	 * Init
	 */
	public launchpad() {
		System.out.println("Hello Hold and Klotz!");
	}
	
	/**
	 * 
	 * @param BGG
	 */
	public void setBGG(BackgroundGrid BGG) {
		_BGG = BGG;
	}
	
	/**
	 * Needed for redraw()
	 * @param BG
	 */
	public void setBG(BoardGui BG) {
		_BG = BG;
	}
	
	/**
	 * you have to do this, if redraw should work!!!
	 */
	public void updateInitRedraw() {
		_BG.setInterface_Class(_Lauch);
	}
	
	
}