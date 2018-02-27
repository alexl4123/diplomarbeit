package launchpad;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;

public class LaunchpadController {

	
	final static int rows = 4;
	final static int cols = 4;
	final static int option = 1;
	
	private int[][][] raster = new int[rows][cols][option];
	private boolean select = false;
	private ShortMessage selectmessage = new ShortMessage();
	private ShortMessage destmessage = new ShortMessage();
	
	
	
	
	public LaunchpadController() {
		
		LaunchpadCommand.setupBoard(raster);
	}
	
	//new class for led controlle und convert
	
	private void move() {
		LaunchpadCommand.move(selectmessage, destmessage, raster);
	}
	
	

	public void receive(int command, int channel, int note, int velocity) {
		 System.out.println("heyy Channel: " + channel + " note: " + note + " value: "
		 + velocity);

		if (command == ShortMessage.NOTE_ON) {
			if (select == false) {
				try {
					if(LaunchpadCommand.getColor(raster, note) == 0) {
						return;
					}
					
					
					if(!Launchpad.chessinterface.ismyTurn(Convert.notetoX(selectmessage.getData1()), Convert.notetoY(selectmessage.getData1()))) {
						return;
					}
					
					
					//make check if you can use this (is it on your team? and are you allowed to move (your turn ))
					
					selectmessage.setMessage(command, channel, note, velocity);
					//Launchpad.entity.sendMIDI(1, notetoled(note), 127);
					
					
					LaunchpadCommand.setLED(note, 127);
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
					//Launchpad.entity.sendMIDI(1, notetoled(selectmessage.getData1()), raster[notetorow(selectmessage.getData1())][notetocol(selectmessage.getData1())][0]);
					LaunchpadCommand.setLED(selectmessage, raster);
					
					destmessage = new ShortMessage();
					selectmessage = new ShortMessage();
				}else {
					move();
					select = false;
					destmessage = new ShortMessage();
					selectmessage = new ShortMessage();
				}
			}
		}
		
		
	}

}
