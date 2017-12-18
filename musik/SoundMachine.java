package musik;

import java.io.*;
import java.net.URL;

import javax.net.*;
import javax.sound.sampled.*;

import GuiStuff.BoardGui;
 
/**
 * @author alexl12
 * @version 1.1 - Draw
 * 
 * This one is not needed in 1.1
 * It is needed for the sound and music...
 * It is a thread
 *
 */
public class SoundMachine extends Thread{  
    private File sf;//only for finding out, if the file is there
    private final int EXTERNAL_BUFFER_SIZE = 524288; //for reading
    private SourceDataLine line = null;
    private AudioInputStream ais = null;
    private FloatControl volume; 
    private String pathFinder; //the path of the music
    private int choose; //background music (loop) or not background music (button click)
    private int eastern;
    
    /**
     * 
     * @param path-path of the music
     * @param i-determining if it is background music (loop) or just sounds (attack sound)
     */
    public SoundMachine(String path, int i) {
    	/*
    	eastern = 0;
    	choose = i;
        try {
        	pathFinder = path;
            sf = new File(path);
        } catch (Exception e) {
            System.out.println("No Wave file found at :" + path);
        }
        init();
        */
    }
    
    /**
     * kills the thread
     */
    public void killSound(){
    	super.interrupt();
    	line.close();
    
    }
    
   
    /**
     * only for background music
     * @param path-path of the music
     */
    private void otherMusic(String path){
    	/*
    	eastern++;
    	pathFinder = path;
    	 System.out.println("otherMusic");
    	 try {
             sf = new File(path);
         } catch (Exception e) {
             System.out.println("No Wave file found at :" + path);
         }
         init();
         line.start();
         volume = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);  
         int nBytesRead = 0;
         byte[] abData = new byte[EXTERNAL_BUFFER_SIZE];
         try {
             while(nBytesRead != -1) {
                 nBytesRead = ais.read(abData, 0, abData.length);
                 if (nBytesRead >= 0) {
                     line.write(abData,  0, nBytesRead);
                 }
             }
         } catch (Exception e) {
             e.printStackTrace();
             return;
         } finally {
             line.drain();
             line.close();
             System.out.println(eastern);
             if((eastern==15)||(eastern==33)||(eastern==66)){
            	 System.out.println("Easter EGG");
            	 otherMusic("/musik/SW1.wav");
             }else{
            	 //randomly looping threw the background tracks
            	 int rand = (int) (Math.random() * 7);
            	 switch(rand){
            	 case 0:{System.out.println("0");otherMusic("/musik/music7.wav");}break;
            	 case 1:{System.out.println("1");otherMusic("/musik/musik1.wav");}break;
            	 case 2:{System.out.println("2");otherMusic("/musik/music3.wav");}break;
            	 case 3:{System.out.println("3");otherMusic("/musik/music4.wav");}break;
            	 case 4:{System.out.println("4");otherMusic("/musik/music5.wav");}break;
            	 case 5:{System.out.println("5");otherMusic("/musik/music6.wav");}break;
            	 case 6:{System.out.println("6");otherMusic("/musik/music2.wav");}break;
            	 default:{System.out.println("Default");otherMusic("/musik/SW1.wav");}break;

            	 }
             }
             
         }
         */
    }
    
    /**
     * for initialising the inputstreams,...
     */
    public void init() {
    	/*
        long now = System.currentTimeMillis();
        
        
        
       
        try {
        	URL url = SoundMachine.class.getResource(pathFinder);
            ais = AudioSystem.getAudioInputStream(url);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
       
        AudioFormat format = ais.getFormat();
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
       
        try {
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(format);
          
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        FloatControl gain = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
        gain.setValue(gain.getMaximum());
        */
    }
   
   /**
    * if the thread is started, it starts the music
    */
    public void run() {
       /*
    	if(choose == 2){
    		int rand = (int) (Math.random() * 7);
            switch(rand){
            	case 0:{System.out.println("0");otherMusic("/musik/music7.wav");}break;
            	case 1:{System.out.println("1");otherMusic("/musik/musik1.wav");}break;
            	case 2:{System.out.println("2");otherMusic("/musik/music3.wav");}break;
            	case 3:{System.out.println("3");otherMusic("/musik/music4.wav");}break;
            	case 4:{System.out.println("4");otherMusic("/musik/music5.wav");}break;
            	case 5:{System.out.println("5");otherMusic("/musik/music2.wav");}break;
            	case 6:{System.out.println("5");otherMusic("/musik/music6.wav");}break;
            	default:{System.out.println("Default");otherMusic("/musik/SW1.wav");}break;
           
            }
    	}{
    		line.start();
        volume = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);  
        int nBytesRead = 0;
        byte[] abData = new byte[EXTERNAL_BUFFER_SIZE];
        try {
            while(nBytesRead != -1) {
                nBytesRead = ais.read(abData, 0, abData.length);
                if (nBytesRead >= 0) {
                    line.write(abData,  0, nBytesRead);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        } finally {
            line.drain();
            line.close();
            
            if(choose == 1){
            	//start the background loop
            	otherMusic("/musik/SW1.wav");
            }
            
        }
    	}
    	*/
    	
    }
    
    /**
     * reducing the volume
     */
    public void redurceVOl(){
    	/*
    	float control = volume.getValue();
    	volume.setValue(control-10f);
    	*/
    }
    
    /**
     * increasing the volume
     */
    public void increaseVOL(){
    	/*
    	float control = volume.getValue();
    	volume.setValue(control+10f);
    	*/
    	
    }
    
    
    public void muteVOL(){
    	/*
    	float control = volume.getValue();
    	System.out.println(control);
    	volume.setValue(-50f);
    	*/
    }
}