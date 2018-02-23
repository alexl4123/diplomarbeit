package launchpad;

import javax.sound.midi.ShortMessage;

public class LaunchpadCommand {
	
	
	
	
	public static int getColor(int[][][] raster,int  note) {
		
		return raster[Convert.notetorow(note)][Convert.notetocol(note)][0];
	}
	
	public static int[][][] setupBoard(int[][][] raster) {
		for(int i = 0; i < LaunchpadController.rows; i ++) {
			for(int j = 0; j < LaunchpadController.cols; j++) {
				raster[i][j][0] = 0;
				if(j == 2 && i == 3) {
					raster[i][j][0] = 60;
					System.out.println("set");
				}
				System.out.println(Convert.rastertoled(i,j)+ "  i: " + i +  "  j: " + j);
				try {
					Thread.sleep(10); //check if this works
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Launchpad.entity.sendMIDI(1, Convert.rastertoled(i,j), raster[i][j][0]);
			}
		}
		
		return raster;
	}
	
	public static void move(ShortMessage selectmessage, ShortMessage destmessage, int[][][] raster) {
		Launchpad.entity.sendMIDI(1, Convert.notetoled(destmessage.getData1()), raster[Convert.notetorow(selectmessage.getData1())][Convert.notetocol(selectmessage.getData1())][0]);
		raster[Convert.notetorow(destmessage.getData1())][Convert.notetocol(destmessage.getData1())][0] = raster[Convert.notetorow(selectmessage.getData1())][Convert.notetocol(selectmessage.getData1())][0];
		raster[Convert.notetorow(selectmessage.getData1())][Convert.notetocol(selectmessage.getData1())][0] = 0;
		try {
			
			Thread.sleep(10); //may brick check it 
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Launchpad.entity.sendMIDI(1, Convert.notetoled(selectmessage.getData1()), 0);
	}
	
	
	public static void setLED(int note, int color) {
		Launchpad.entity.sendMIDI(1, Convert.notetoled(note), color);
	}
	
	public static void setLED(ShortMessage selectmessage, int[][][] raster) {
		Launchpad.entity.sendMIDI(1, Convert.notetoled(selectmessage.getData1()), raster[Convert.notetorow(selectmessage.getData1())][Convert.notetocol(selectmessage.getData1())][0]);
	}

}
