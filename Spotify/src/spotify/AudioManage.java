
package spotify;

import com.google.code.mp3fenge.Mp3Fenge;
import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;


/**
 * Manage all reproduction and settings of audio files.
 * @author Antonioni Andrea & Zanelli Gabriele
 */

public class AudioManage {
    private MediaPlayer mediaPlayer;
    private String songPath;
    private int volume;
    
    public AudioManage() {
        this.songPath = null;
        this.volume=70;
    }
    
    public void newSong(String songPath) {
        this.songPath = songPath;
    }
    
    public void play() {
        playMediaFromPath(songPath);
    }
    
    public void playIndex(int millis) {
        
    }
    
    public void pause() {
        
    }
    
    public void changeVolume(int volume) {
        this.volume=volume;
    }
    
    public void playMediaFromPath(String path){
        Media media = new Media(new File(path).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        MediaView mediaView = new MediaView(mediaPlayer);
    }
    
    public void cutFileAudio() {
        Mp3Fenge origin = new Mp3Fenge(new File(Library.LOCAL_PATH+"Toxic.mp3"));
        origin.generateNewMp3ByTime(new File(Library.LOCAL_PATH+"Toxic-Cut.mp3"), 5000, 10000);
    }   
}
