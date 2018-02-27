package audio;

import java.net.URL;

import javafx.scene.media.AudioClip;

public class AudioManager {
	
	private  AudioClip meepleClick, menuClick, startup;
	private boolean isMuted = false;
	private double Volume;
	
	public AudioManager(){
		
		final URL rsc1 = getClass().getResource("/audio/MeepleClick.mp3");
		final URL rsc2 = getClass().getResource("/audio/menuClick.mp3");
		final URL rsc3 = getClass().getResource("/audio/Startup.mp3");
		
		Volume = 0.5;
		
		meepleClick = new AudioClip(rsc1.toString()); 
		menuClick = new AudioClip(rsc2.toString());
		startup = new AudioClip(rsc3.toString());
		
	}
	
	public void playSound(String choose){
		
		if(!isMuted){
			
			switch (choose) {
			
			case "move": 
				meepleClick.setVolume(Volume);
				meepleClick.play(); break;
				
			case "menu": 
				menuClick.setVolume(Volume);
				menuClick.play(); break;
				
			case "startup": 
				startup.setVolume(Volume);
				startup.play(); break;

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
