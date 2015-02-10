/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    private Line Line1;
    
    public MusicPlayer() {
        this.ChooseSong("/monopoly/Music/Summertime.wav");
    }
    
    public void ChooseSong(String FilePath) {
        if (soundClip != null && soundClip.isOpen()) {
            soundClip.stop();
        }
        
        try {
            audioSrc = getClass().getResourceAsStream(FilePath);
            bufferedIn = new BufferedInputStream(audioSrc);
            AS1 = AudioSystem.getAudioInputStream(bufferedIn);

            AF = AS1.getFormat();
            soundClip = AudioSystem.getClip();
            info = new DataLine.Info(Clip.class, AF);

            Line1 = AudioSystem.getLine(info);
            
            if (!Line1.isOpen()){
                soundClip.open(AS1);
                soundClip.loop(Clip.LOOP_CONTINUOUSLY);
            }
        }
        catch(UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("Error" + e);
        }
    }
	
    public void StartMusic() {
        soundClip.start();
    }
    
    public void StopMusic() {
        soundClip.stop();
    }
}
