package javachess.audio;


public class AudioManager {
	

	
	private JavaFxAudio fxaudio;
	private NativeAudio nativesound;
	private boolean bfxaudio = false;
	
	public AudioManager(){
		try {
			this.fxaudio = new JavaFxAudio();
			this.bfxaudio = true;
			System.out.println("line 17");
			
		}catch (Exception e) {
			System.out.println("javafxaudio error! switching back to native audio");
			nativesound = new NativeAudio();
			this.bfxaudio = false;
			System.out.println("line 13");
		}
		
		
		
	}
	
	public void playSound(String choose){
		
		try {
			
		
		if(bfxaudio) {
			fxaudio.playSound(choose);
		}else {
			nativesound.playSound(choose);
		}
		
		}catch(Exception e) {
			System.out.println("ERROR can't play sound!");
			if(bfxaudio){
				nativesound = new NativeAudio();
				bfxaudio = false;
				System.out.println("javafxaudio error! switching back to native audio");
			}
			e.printStackTrace();
			
		}
		

		
	}
	
	public void setIsMuted(boolean b){
		if(bfxaudio) {
			fxaudio.setIsMuted(b);
		}else {
			//navtive 
			nativesound.setIsMuted(b);
		}
	}
	
	public boolean getIsMuted(){
		if(bfxaudio) {
			return fxaudio.getIsMuted();
		}else {
			//navtive 
			return nativesound.getIsMuted();
		}
	}
	
	public void setVolume(double d){
		if(bfxaudio) {
			fxaudio.setVolume(d);
		}else {
			//navtive
			nativesound.setVolume(d);
		}
	}
	
	public double getVolume(){
		if(bfxaudio) {
			return fxaudio.getVolume();
		}else {
			//navtive 
			return nativesound.getVolume();
		}
	}

}
