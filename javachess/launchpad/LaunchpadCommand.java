package javachess.launchpad;

import javax.sound.midi.ShortMessage;

public class LaunchpadCommand {

	public static int getColor(int[][][] raster, int note) {

		return raster[Convert.notetorow(note)][Convert.notetocol(note)][0];
	}

	public static int[][][] setupBoard(int[][][] raster) {
		for (int i = 0; i < LaunchpadController.rows; i++) {
			for (int j = 0; j < LaunchpadController.cols; j++) {
				raster[i][j][0] = 0;
				if (i == 1) {
					raster[i][j][0] = 60;
					System.out.println("set");
				}
				System.out.println(Convert.rastertoled(i, j) + "  i: " + i + "  j: " + j);
				try {
					Thread.sleep(10); // check if this works
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Launchpad.entity.sendMIDI(1, Convert.rastertoled(i, j), raster[i][j][0]);
			}
		}

		return raster;
	}

	public static void move(ShortMessage selectmessage, ShortMessage destmessage, int[][][] raster) {

		System.out.println("move par   " + "X: " + (Convert.notetoX(selectmessage.getData1())) + "Y: "
				+ (Convert.notetoY(selectmessage.getData1())) + "  X2: " + Convert.notetoX(destmessage.getData1())
				+ "   Y2: " + Convert.notetoY(destmessage.getData1()));

		boolean mymove = Launchpad.chessinterface.moveFig(Convert.notetoX(selectmessage.getData1()),
				Convert.notetoY(selectmessage.getData1()), Convert.notetoX(destmessage.getData1()),
				Convert.notetoY(destmessage.getData1()));
		System.out.println("move" + mymove);

		if (mymove) {

			Launchpad.entity.sendMIDI(1, Convert.notetoled(destmessage.getData1()),
					raster[Convert.notetorow(selectmessage.getData1())][Convert
							.notetocol(selectmessage.getData1())][0]);
			raster[Convert.notetorow(destmessage.getData1())][Convert
					.notetocol(destmessage.getData1())][0] = raster[Convert.notetorow(selectmessage.getData1())][Convert
							.notetocol(selectmessage.getData1())][0];
			raster[Convert.notetorow(selectmessage.getData1())][Convert.notetocol(selectmessage.getData1())][0] = 0;
			try {

				Thread.sleep(10); // may brick check it
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Launchpad.entity.sendMIDI(1, Convert.notetoled(selectmessage.getData1()), 0);

		}else {
			
			//indicate wrong move 
			setLED(selectmessage.getData1(), 1);
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			setLED(destmessage.getData1(), 1);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			setLED(selectmessage, raster);
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			setLED(destmessage, raster);
		}

		// just for test
		// System.out.println("is my turn (white)"+
		// Launchpad.chessinterface.isWhiteTurn());
		// System.out.println(Launchpad.chessinterface.moveFig(hjk, 1, hjk, 2));

	}

	// make update raster function bgg = raster

	public static void setLED(int note, int color) {
		Launchpad.entity.sendMIDI(1, Convert.notetoled(note), color);
	}

	public static void setLED(ShortMessage selectmessage, int[][][] raster) {
		Launchpad.entity.sendMIDI(1, Convert.notetoled(selectmessage.getData1()),
				raster[Convert.notetorow(selectmessage.getData1())][Convert.notetocol(selectmessage.getData1())][0]);
	}

}
