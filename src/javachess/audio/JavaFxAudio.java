package javachess.audio;

import java.net.URL;


import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class JavaFxAudio {

	private MediaPlayer meepleClick, menuClick, startup;
	private Media meepleClicksound, menuClicksound, startupsound;
	private boolean isMuted = false;
	private double Volume;

	public JavaFxAudio() {
		final URL rsc1 = getClass().getResource("/javachess/audio/MeepleClick.mp3");
		final URL rsc2 = getClass().getResource("/javachess/audio/MenuClick.mp3");
		final URL rsc3 = getClass().getResource("/javachess/audio/Startup.mp3");

		Volume = 0.5;


		meepleClicksound = new Media(rsc1.toString());
		//meepleClick = new MediaPlayer(meepleClicksound);
		
		menuClicksound = new Media(rsc2.toString());
		//menuClick = new MediaPlayer(menuClicksound);
		
		startupsound = new Media(rsc3.toString());
		//startup = new MediaPlayer(startupsound);
		
		
	}

	public void playSound(String choose) {

		if (!isMuted) {

			switch (choose) {

			case "move":
				meepleClick = new MediaPlayer(meepleClicksound);
				meepleClick.setVolume(Volume);
				System.out.println("move sound played");
				meepleClick.play();
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
	
	public void setIsMuted(boolean b){
		this.isMuted = b;
	}
	
	public boolean getIsMuted(){
		return this.isMuted;
	}
	
	public void setVolume(double d){
		this.Volume = d;
	}
	
	public double getVolume(){
		return this.Volume;
	}
}
