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


/**
 * 
 * @author mhub - 2018
 * @version 2.0
 * 
 * This class was introduced to work around the gamebreaking but at linux - systems,
 * where the fx-soundplayer broke the application.
 * It plays sounds with the help of a native soundplayer.
 *
 */
public class NativeAudio {
	
	/**
	 * Determins wether the player is muted.
	 */
	private boolean isMuted = false;
	
	/**
	 * The current Volume
	 */
	private double Volume;
	
	/**
	 * Size of the Buffer
	 */
	private final int BUFFER_SIZE = 128000;
	
	/**
	 * The soundFile
	 */
	private File soundFile;
	
	/**
	 * The inputStream for Audio
	 */
	private AudioInputStream audioStream;
	
	/**
	 * The format of the audio
	 */
	private AudioFormat audioFormat;
	
	/**
	 * the SourceDataLine
	 */
	private SourceDataLine sourceLine;
	
	/**
	 *location of a sound
	 */
	private final URL meeplesound = AudioSystem.class.getResource("/javachess/audio/MeepleClick.wav");
	
	/**
	 *location of a sound
	 */
	private final URL menusound = AudioSystem.class.getResource("/javachess/audio/MenuClick.wav");
	
	/**
	 *location of a sound
	 */
	private final URL startupsound = AudioSystem.class.getResource("/javachess/audio/Startup.wav");
	
	/**
	 * The Constructor. Sets the default volume. 
	 */
	public NativeAudio() {
		Volume = 0.5;
	}
	
	
	/**
	 * Method to play a native sound. 
	 * @param filename the sound to be played. 
	 */
	private void play(URL filename) {
		
		try {												//loading the File
			soundFile = new File(filename.getPath());
		} catch (Exception e) {
			System.out.println("no file");
			e.printStackTrace();
			System.exit(1);
		}

		try {												//Creating aUdioStream
			audioStream = AudioSystem.getAudioInputStream(filename);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		audioFormat = audioStream.getFormat();				//Setting the format

		DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);			//Creating the audioline
		try {
			sourceLine = (SourceDataLine) AudioSystem.getLine(info);						//some casting  - yey
			sourceLine.open(audioFormat);													//opening the audioformat
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

		sourceLine.drain();					//closing  the line now
		sourceLine.close();
	}

	
	/**
	 * Method to choose the sound to play. 
	 * @param choose the sound to play. 
	 */
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
	
	
	/**
	 * Sets the muting state of the soundplayer.
	 * @param b - wether the sound is muted
	 */
	public void setIsMuted(boolean b) {
		this.isMuted = b;
	}

	/**
	 * 
	 * @return the current mutet satet
	 */
	public boolean getIsMuted() {
		return this.isMuted;
	}

	/**
	 * Sets the current Volume
	 * @param d - the volume to be set
	 */
	public void setVolume(double d) {
		this.Volume = d;
	}

	/**
	 *
	 * @return the current volume
	 */
	public double getVolume() {
		return this.Volume;
	}

}
