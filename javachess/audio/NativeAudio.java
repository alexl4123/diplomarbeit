package javachess.audio;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;


public class NativeAudio {
	
	
	private boolean isMuted = false;
	private double Volume;
	private final int BUFFER_SIZE = 128000;
	private File soundFile;
	private AudioInputStream audioStream;
	private AudioFormat audioFormat;
	private SourceDataLine sourceLine;
	private final URL meeplesound = getClass().getResource("/javachess/audio/MeepleClick.wav");
	private final URL menusound = getClass().getResource("/javachess/audio/MenuClick.wav");
	private final URL startupsound = getClass().getResource("/javachess/audio/Startup.wav");

	public NativeAudio() {
		
		Volume = 0.5;

	}
	

	private void play(URL filename) {
		
		


		try {
			soundFile = new File(filename.getPath());
		} catch (Exception e) {
			System.out.println("no file");
			e.printStackTrace();
			System.exit(1);
		}

		try {
			audioStream = AudioSystem.getAudioInputStream(soundFile);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		audioFormat = audioStream.getFormat();

		DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
		try {
			sourceLine = (SourceDataLine) AudioSystem.getLine(info);
			sourceLine.open(audioFormat);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		sourceLine.start();

		int nBytesRead = 0;
		byte[] abData = new byte[BUFFER_SIZE];
		while (nBytesRead != -1) {
			try {
				nBytesRead = audioStream.read(abData, 0, abData.length);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (nBytesRead >= 0) {
				@SuppressWarnings("unused")
				int nBytesWritten = sourceLine.write(abData, 0, nBytesRead);
			}
		}

		sourceLine.drain();
		sourceLine.close();
	}


	public void playSound(String choose) {

		if (!isMuted) {

			switch (choose) {

			case "move":
				//meeplesound
				play(meeplesound);
				break;

			case "menu":
				//menusound
				play(menusound);
				break;

			case "startup":
				//startupsound
				play(startupsound);
				break;

			default:
				break;
			}

		}

	}
	
	

	public void setIsMuted(boolean b) {
		this.isMuted = b;
	}

	public boolean getIsMuted() {
		return this.isMuted;
	}

	public void setVolume(double d) {
		this.Volume = d;
	}

	public double getVolume() {
		return this.Volume;
	}

}
