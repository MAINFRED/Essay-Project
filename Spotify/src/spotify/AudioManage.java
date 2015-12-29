
package spotify;

import com.google.code.mp3fenge.Mp3Fenge;
import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;


/**
 * @author Zanelli Gabriele
 */

public class AudioManage {
    private byte[] song;
    private String resPath = "resources/local/";
    private int volume;
    
    public AudioManage() {
        this.song = null;
        this.volume=70;
    }
    
    public void newSong(byte[] song) {
        this.song = song;
    }
    
    public void play() {
        playMediaFromPath(resPath+"Toxic.mp3");
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
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        MediaView mediaView = new MediaView(mediaPlayer);
    }
    
    public void cutFileAudio() {
        Mp3Fenge origin = new Mp3Fenge(new File(resPath+"Toxic.mp3"));
        origin.generateNewMp3ByTime(new File(resPath+"Toxic-Cut.mp3"), 5000, 10000);
    }   
}
