package musik;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;











public class SoundMachine
  extends Thread
{
  private File sf;
  private final int EXTERNAL_BUFFER_SIZE = 524288;
  private SourceDataLine line = null;
  private AudioInputStream ais = null;
  



  private FloatControl volume;
  


  private String pathFinder;
  


  private int choose;
  


  private int eastern;
  



  public SoundMachine(String path, int i) {}
  



  public void killSound()
  {
    super.interrupt();
    line.close();
  }
  
  private void otherMusic(String path) {}
  
  public void init() {}
  
  public void run() {}
  
  public void redurceVOl() {}
  
  public void increaseVOL() {}
  
  public void muteVOL() {}
}
