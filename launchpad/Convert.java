package launchpad;

public class Convert {

	public static int notetoled(int note) {
		return (note - 35) / 4 + 16;
	}

	public static int notetorow(int note) {
		return (int) ((note - 35) / 4 / 4);
	}

	public static int notetocol(int note) {
		return ((note - 35) / 4) - 4 * ((int) (((note - 35) / 4) / 4));
	}

	public static int notetoX(int note) {
		return Convert.notetocol(note);
	}
	
	public static int notetoY(int note) {
		return Convert.notetorow(note);
	}

	public static int rastertoled(int row, int col) {
		return ((int) (row * 4)) + (col + 1) + 15;
	}

}
