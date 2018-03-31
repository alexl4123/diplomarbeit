package javachess.audio;

import java.net.URL;


import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * 
 * @author mhub - 2018
 * @version 2.0
 * 
 * This Class is the original Audioplayer. It loads and plays sounds with the help of the JavaFX - Framework.
 *
 */
public class JavaFxAudio {

	/**
	 * Three different MediaPlayers for different occasions.
	 */
	private MediaPlayer meepleClick, menuClick, startup;
	
	/**
	 * The different sounds to Play
	 */
	private Media meepleClicksound, menuClicksound, startupsound;
	
	/**
	 * Boolean wether the sound is mutet
	 */
	private boolean isMuted = false;
	
	/**
	 * Represents the current Volume
	 */
	private double Volume;

	
	/**
	 * The constructor. Loads the Sounds and sets the volume to default = 50%. 
	 */
	public JavaFxAudio() {
		
		//getting Resource - Lines
		final URL rsc1 = getClass().getResource("/javachess/audio/MeepleClick.mp3");
		final URL rsc2 = getClass().getResource("/javachess/audio/MenuClick.mp3");
		final URL rsc3 = getClass().getResource("/javachess/audio/Startup.mp3");

		Volume = 0.5;		//Setting default Volume

		//Creating the Media to play
		meepleClicksound = new Media(rsc1.toString());
		menuClicksound = new Media(rsc2.toString());
		startupsound = new Media(rsc3.toString());


	}

	/**
	 * A method to play a a sound. 
	 * @param choose - A String to choose which sound to play. 
	 */
	public void playSound(String choose) {
		
		//if the sound is mutet, we should´t play sounds, should we?
		if (!isMuted) {

			switch (choose) {					//choosing sound

			case "move":
				meepleClick = new MediaPlayer(meepleClicksound);		//creating a mediaPlayer
				meepleClick.setVolume(Volume);							//setting the volume of the Mediaplayer
				System.out.println("move sound played");
				meepleClick.play();										//play the sound
				break;

			case "menu":
				menuClick = new MediaPlayer(menuClicksound);
				menuClick.setVolume(Volume);
				menuClick.play();
				break;

			case "startup":
				startup = new MediaPlayer(startupsound);
				startup.setVolume(Volume);
				startup.play();
				break;

			default:
				break;
			}

		}

	}

	/**
	 * Sets the mutete sate of the soundplayer. 
	 * @param b - Wether the sound is mutet or not
	 */
	public void setIsMuted(boolean b){
		this.isMuted = b;
	}

	/**
	 * @return the mutet state
	 */
	public boolean getIsMuted(){
		return this.isMuted;
	}

	/**
	 * Sets the Volume of the Soundplayer
	 * @param d - the Volume
	 */
	public void setVolume(double d){
		this.Volume = d;
	}

	/**
	 * 
	 * @return the current Volume
	 */
	public double getVolume(){
		return this.Volume;
	}
}
