package javachess.launchpad;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

public class MidiInputReceiver implements Receiver {
	public String name;
	public Thread procmus;

	public MidiInputReceiver(String name) {
		this.name = name;
	}

	public void send(MidiMessage msg, long timeStamp) {
		if (msg instanceof ShortMessage) {
			ShortMessage shortMessage = (ShortMessage) msg;

			if (shortMessage.getCommand() == ShortMessage.NOTE_ON) {
				int channel = shortMessage.getChannel();
				int note = shortMessage.getData1();
				int value = shortMessage.getData2();
				
				System.out.println("Channel: " + channel + "  note: " + note + "   value: " + value);
				
				Launchpad.controller.receive(shortMessage.getCommand(), channel, note, value);
				

			} else if (shortMessage.getCommand() == ShortMessage.CONTROL_CHANGE) {
				int cont = shortMessage.getData1();
				int val = shortMessage.getData2();

				//not supported yet 
				System.out.println("ID: " + cont + "  Value: " + val);
				
				

			} else if (shortMessage.getCommand() == ShortMessage.NOTE_OFF) {

				int channel = shortMessage.getChannel();
				int note = shortMessage.getData1();
				int value = shortMessage.getData2();
				
				Launchpad.controller.receive(shortMessage.getCommand(), channel, note, value);

			}

		} else {
			System.out.println("No ShortMessage");
		}

	}

	public void close() {
	}
}
