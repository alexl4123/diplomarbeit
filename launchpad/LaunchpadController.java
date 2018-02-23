package launchpad;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;

public class LaunchpadController {

	
	final int rows = 4;
	final int cols = 4;
	final int option = 1;
	
	private int[][][] raster = new int[rows][cols][option];
	private boolean select = false;
	private ShortMessage selectmessage = new ShortMessage();
	private ShortMessage destmessage = new ShortMessage();
	
	
	private int notetoled(int note) {
		return (note - 35) / 4 + 16;
	}
	
	private int notetorow(int note) {
		return (int) ((note - 35) / 4 / 4);
	}
	
	private int notetocol(int note) {
		return ((note - 35) / 4) - 4 * ((int) (((note - 35) / 4) / 4));
	}
	
	private int rastertoled(int row, int col) {
		return ( (int) (row * 4)) + (col + 1) + 15;
	}
	
	public LaunchpadController() {
		
		
		for(int i = 0; i < rows; i ++) {
			for(int j = 0; j < cols; j++) {
				raster[i][j][0] = 0;
				if(j == 2 && i == 3) {
					raster[i][j][0] = 60;
					System.out.println("set");
				}
				System.out.println(rastertoled(i,j)+ "  i: " + i +  "  j: " + j);
				try {
					Thread.currentThread().sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Launchpad.entity.sendMIDI(1, rastertoled(i,j), raster[i][j][0]);
			}
		}
	}
	
	//new class for led controlle und convert
	
	private void move() {
		Launchpad.entity.sendMIDI(1, notetoled(destmessage.getData1()), raster[notetorow(selectmessage.getData1())][notetocol(selectmessage.getData1())][0]);
		raster[notetorow(destmessage.getData1())][notetocol(destmessage.getData1())][0] = raster[notetorow(selectmessage.getData1())][notetocol(selectmessage.getData1())][0];
		raster[notetorow(selectmessage.getData1())][notetocol(selectmessage.getData1())][0] = 0;
		try {
			Thread.currentThread().sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Launchpad.entity.sendMIDI(1, notetoled(selectmessage.getData1()), 0);
	}
	
	

	public void receive(int command, int channel, int note, int velocity) {
		 System.out.println("heyy Channel: " + channel + " note: " + note + " value: "
		 + velocity);

		if (command == ShortMessage.NOTE_ON) {
			if (select == false) {
				try {
					if(raster[notetorow(note)][notetocol(note)][0] == 0) {
						return;
					}
					selectmessage.setMessage(command, channel, note, velocity);
					Launchpad.entity.sendMIDI(1, notetoled(note), 127);
					select = true;
				} catch (InvalidMidiDataException e) {
					e.printStackTrace();
				}
			}else {
				try {
					destmessage.setMessage(command, channel, note, velocity);
				} catch (InvalidMidiDataException e) {
					e.printStackTrace();
				}
				if(selectmessage.getData1() == destmessage.getData1()) {
					select = false;
					Launchpad.entity.sendMIDI(1, notetoled(selectmessage.getData1()), raster[notetorow(selectmessage.getData1())][notetocol(selectmessage.getData1())][0]);
					destmessage = new ShortMessage();
					selectmessage = new ShortMessage();
				}else {
					move();
					System.out.println("moll");
					select = false;
					destmessage = new ShortMessage();
					selectmessage = new ShortMessage();
				}
			}
		}
	}

}
