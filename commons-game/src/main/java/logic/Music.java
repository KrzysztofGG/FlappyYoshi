package logic;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Music {
    File musicFile;
    AudioInputStream audioStream;
    Clip clip;

    public void playMusic(String file, float volume, boolean isLoop){
        try{
            musicFile = new File(file);
            audioStream = AudioSystem.getAudioInputStream(musicFile);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            FloatControl gainControl =
                    (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(volume);
            clip.start();
            if(isLoop)
                clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }


    }
}
