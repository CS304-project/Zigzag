package Model;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

public class Sound {
    File file;
    AudioInputStream audioStream;
    Clip clip;

    public Sound(String path) throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        file = new File(path);
        audioStream = AudioSystem.getAudioInputStream(file);
        clip = AudioSystem.getClip();

        clip.open(audioStream);
    }

    public void start() {
        clip.start();
    }

    public void reset() {
        clip.setMicrosecondPosition(0);
    }
}
