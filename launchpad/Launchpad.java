package launchpad;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Transmitter;
import javax.swing.JOptionPane;

import BackgroundMatrix.BackgroundGrid;
import Gui.BoardGui;
import Gui.GUI;




/**
 * Class For Hold and Klotz
 * 
 * @Hold and @Klotz you have to do the setInterface_Class methode in BoardGui
 * @author alexl12
 *
 */
public class Launchpad implements Runnable {

	public static Launchpad entity;
	public static LaunchpadController controller;
	public static ChessInterface chessinterface;
	public static Thread thread;
	
	private static Interface_class interface_class;

	private GUI gui;
	private BoardGui _BG;
	private BackgroundGrid _BGG;
	
	public boolean setup = false;
	public boolean finsetup = false;

	private static MidiDevice transdevice;
	private static MidiDevice recdevice;

	private String selectlaunchpad(MidiDevice.Info[] infos) {

		int length = infos.length;
		boolean newentry = true;

		String[] prechoices = new String[infos.length];

		for (int i = 0; infos.length > i; i++) {
			prechoices[i] = "";
		}

		for (int i = 0; infos.length > i; i++) {

			newentry = true;

			for (int j = 0; infos.length > j; j++) {

				if (prechoices[j].equals(infos[i].getDescription())) {
					length--;
					newentry = false;
				}

			}

			if (newentry) {
				prechoices[i - (infos.length - length)] = infos[i].getDescription();
			}
		}

		String[] choices = new String[length];

		for (int i = 0; i < length; i++) {
			choices[i] = prechoices[i];
		}

		String input = (String) JOptionPane.showInputDialog(null, "Launchpad: ", "Launchpad Selector",
				JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);

		
		return input;
	}

	private void setup() {
		setup = false;

		MidiDevice device;
		MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();

		String selection = selectlaunchpad(infos);
		
		if(selection == null) {
			System.out.println("No Launchpad was selected!");
			setup = false;
			return;
		}

		for (int i = 0; i < infos.length; i++) {

			if (infos[i].getDescription().equals(selection)) {

				try {

					device = MidiSystem.getMidiDevice(infos[i]);
					System.out.println(device.getMaxReceivers());
					System.out.println(device.getMaxTransmitters());

					if (device.getMaxTransmitters() != 0) {
						// and for each transmitter

						Transmitter trans = device.getTransmitter();
						trans.setReceiver(new MidiInputReceiver(device.getDeviceInfo().toString()));

						transdevice = device;
						// open each device
						transdevice.open();
						// if code gets this far without throwing an exception
						// print a success message
						System.out.println(device.getDeviceInfo().getDescription() + " was opened sucessfully for sending MIDI-Messages (Launchpad -> PC)");
					}
					
					if(device.getMaxReceivers() != 0) {
						recdevice = device;
						recdevice.open();
						
						System.out.println(device.getDeviceInfo().getDescription() + " was opened sucessfully for receiving MIDI-Messages (PC -> Launchpad)!");
						
					}

				} catch (MidiUnavailableException e) {
				}
			}
		}
		
		setup = true;
	}

	public void sendMIDI(int channel, int note, int velocity) {

		ShortMessage myMsg = new ShortMessage();
		// Start playing the note Middle C (60),
		// moderately loud (velocity = 93).
		try {
			myMsg.setMessage(ShortMessage.NOTE_ON, channel, note, velocity);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
		long timeStamp = -1;
		try {
			recdevice.getReceiver().send(myMsg, timeStamp);
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
	}
	

	public void run() {
		finsetup = false;
		setup();
		finsetup = true;
		System.out.println("hey");
		if(!setup) {
			System.out.println("Exit Launchpad-mode!");
			this.gui.getMenu().setLaunchpadMode(false);
			return;
		}
		this.gui.initLaunchpad2(this);
		Launchpad.entity = this;
		controller = new LaunchpadController();
		chessinterface = new ChessInterface();
		interface_class = new Interface_class();
		interface_class.setBGG(_BGG);
		interface_class.setBG(_BG);
		
		//do more setup
		
		chessinterface.setupinterfaceclass(interface_class);
		System.out.println("Setup Interface_class successful!");
		
		
	}
	

	//TODO Implement Stop class (cleanup usw.)
	public static void stop() {
		if(transdevice != null) {
		transdevice.close();
		}
		if(recdevice != null) {
		recdevice.close();
		}
		Launchpad.thread.interrupt();
		System.out.println("Killing Launchpad!");
	}
	
	
	/**
	 * Init
	 */
	public Launchpad(GUI gui) {
		this.gui = gui;
		finsetup = false;
	}
	
	public void start(BackgroundGrid BGG, BoardGui BG){
		_BGG = BGG;
		_BG = BG;
		Thread t = new Thread(this);
		t.start();
		Launchpad.thread = t;
	}

	/**
	 * 
	 * @param BGG
	 */
	public void setBGG(BackgroundGrid BGG) {
		_BGG = BGG;
		interface_class.setBGG(_BGG);
	}

	/**
	 * Needed for redraw()
	 * 
	 * @param BG
	 */
	public void setBG(BoardGui BG) {
		_BG = BG;
		interface_class.setBG(_BG);
	}

	

}