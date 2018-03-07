package audio;

import java.net.URL;

import javafx.scene.media.AudioClip;

public class AudioManager {
	
	/*
	private  AudioClip meepleClick, menuClick, startup;
	private boolean isMuted = false;
	private double Volume;
	*/
	
	private JavaFxAudio fxaudio;
	private boolean bfxaudio = false;
	
	public AudioManager(){
		
		/*
		final URL rsc1 = getClass().getResource("/audio/MeepleClick.mp3");
		final URL rsc2 = getClass().getResource("/audio/menuClick.mp3");
		final URL rsc3 = getClass().getResource("/audio/Startup.mp3");
		
		Volume = 0.5;
		
		meepleClick = new AudioClip(rsc1.toString()); 
		menuClick = new AudioClip(rsc2.toString());
		startup = new AudioClip(rsc3.toString());
		*/
		
		
		
		try {
			System.out.println("heyy");
			this.fxaudio = new JavaFxAudio();
			this.bfxaudio = true;
			
		}catch (Exception e) {
			System.out.println("switching back to native audio --> todo");
		}
		
		
		
	}
	
	public void playSound(String choose){
		
		try {
			
		
		if(bfxaudio) {
			fxaudio.playSound(choose);
		}else {
			//add navtive 
		}
		
		}catch(Exception e) {
			System.out.println("error");
		}
		
		/*
		
		if(!isMuted){
			
			switch (choose) {
			
			case "move": 
				meepleClick.setVolume(Volume);
				System.out.println("move sound played");
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
		
		*/
		
	}
	
	public void setIsMuted(boolean b){
		//this.isMuted = b;
		if(bfxaudio) {
			fxaudio.setIsMuted(b);
		}else {
			//add navtive 
		}
	}
	
	public boolean getIsMuted(){
		//return this.isMuted;
		if(bfxaudio) {
			return fxaudio.getIsMuted();
		}else {
			//add navtive 
			return false;
		}
	}
	
	public void setVolume(double d){
		//this.Volume = d;
		if(bfxaudio) {
			fxaudio.setVolume(d);
		}else {
			//add navtive 
		}
	}
	
	public double getVolume(){
		//return this.Volume;
		if(bfxaudio) {
			return fxaudio.getVolume();
		}else {
			//add navtive 
			return 0.5;
		}
	}

}
