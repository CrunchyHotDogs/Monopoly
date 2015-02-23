package monopoly;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author Kyle Crossman
 */
public class MusicPlayer {
    private Clip soundClip = null;
    private InputStream audioSrc, bufferedIn;
    private AudioInputStream AS1;
    private AudioFormat AF;
    private DataLine.Info info;
    private Line line;
    
    public MusicPlayer() {
        this.chooseSong("/monopoly/Music/Summertime.wav");
    }
    
    public void chooseSong(String filePath) {
        if (soundClip != null && soundClip.isOpen()) {
            soundClip.stop();
        }
        
        try {
            audioSrc = getClass().getResourceAsStream(filePath);
            bufferedIn = new BufferedInputStream(audioSrc);
            AS1 = AudioSystem.getAudioInputStream(bufferedIn);

            AF = AS1.getFormat();
            soundClip = AudioSystem.getClip();
            info = new DataLine.Info(Clip.class, AF);

            line = AudioSystem.getLine(info);
            
            if (!line.isOpen()){
                soundClip.open(AS1);
                soundClip.loop(Clip.LOOP_CONTINUOUSLY);
            }
        }
        catch(UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("Error : " + e);
        }
    }
	
    public void startMusic() {
        soundClip.start();
    }
    
    public void stopMusic() {
        soundClip.stop();
    }
}
