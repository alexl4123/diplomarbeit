package javachess.audio;

/**
 * 
 * @author mhub - 2018
 * @version 2.0
 * 
 * This Class manages, which soundplayer is used to play sounds. 
 * If the initializing of the fx - player fails, the native one is used.
 * The Audio - Manager was introduced after a critical bug on Linux systems. 
 *
 */

public class AudioManager {


	/**
	 * The FX-Soundplayer
	 */
	private JavaFxAudio fxaudio;

	/**
	 * The Native-Soundplayer
	 */
	private NativeAudio nativesound;

	/**
	 * Determins which soundplayer is used
	 */
	private boolean bfxaudio = false;



	/**
	 * The Constructor
	 * Trys to use the javaFx soundplayer. If this fails, the native one kicks in. 
	 */
	public AudioManager(){
		try {

			this.fxaudio = new JavaFxAudio();			//Initialize JFX
			this.bfxaudio = true;


		}catch (Exception e) {							//If this fails, use native
			System.out.println("javafxaudio error! switching back to native audio");
			nativesound = new NativeAudio();
			this.bfxaudio = false;
		}



	}

	
	/**
	 * A method to play a previously loaded sound. 
	 * No worries about the active Soundplayer!
	 * @param choose - A string, which chooses which sound will be played. 
	 */
	public void playSound(String choose){

		try {


			if(bfxaudio) {
				fxaudio.playSound(choose);
			}else {
				nativesound.playSound(choose);
			}

		}catch(Exception e) {
			
			//Safte first - better initalize again when it fails
			System.out.println("ERROR can't play sound!");
			if(bfxaudio){
				nativesound = new NativeAudio();
				bfxaudio = false;
			}
		}
	}

	/**
	 * Mutes or demutes the Sound
	 * @param b - Used to mute the sound
	 */
	public void setIsMuted(boolean b){
		if(bfxaudio) {
			fxaudio.setIsMuted(b);
		}else {
			nativesound.setIsMuted(b);
		}
	}

	/**
	 * 
	 * @return the mutet state of the Soundplayer
	 */
	public boolean getIsMuted(){
		if(bfxaudio) {
			return fxaudio.getIsMuted();
		}else {
			return nativesound.getIsMuted();
		}
	}
	
	/**
	 * Sets the Volume of the Soundplayer
	 * @param d - The Number of the volume. 
	 */
	public void setVolume(double d){
		if(bfxaudio) {
			fxaudio.setVolume(d);
		}else {
			nativesound.setVolume(d);
		}
	}

	/**
	 * 
	 * @return the current volume
	 */
	public double getVolume(){
		if(bfxaudio) {
			return fxaudio.getVolume();
		}else {
			return nativesound.getVolume();
		}
	}

}
