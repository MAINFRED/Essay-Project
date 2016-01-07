
package spotify;

import com.google.code.mp3fenge.Mp3Fenge;
import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;


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
        Media media = new Media(new File(songPath).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);
    }
    
    public void play() {
        mediaPlayer.play();
    }
    
    /**
     * Play a song from a certain second.
     * @param time A Duration indicating the desired start time.
     */
    public void playIndex(Duration time) {
        mediaPlayer.setStartTime(time);
        mediaPlayer.play();
    }
    
    public void pause() {
        mediaPlayer.pause();
    }
    
    public Duration getCurrentTime() {
        return mediaPlayer.getCurrentTime();
    }
    
    public Duration getTimeLeft() {
        return mediaPlayer.getStopTime().subtract(mediaPlayer.getCurrentTime());
    }
    
    /**
     * Change the speakers volume.
     * @param volume An Integer indicating the value of volume from 0 to 100.
     */
    public void changeVolume(int volume) {
        this.volume=volume;
        mediaPlayer.setVolume(volume*0.01);
    }
    
    public void cutFileAudio() {
        Mp3Fenge origin = new Mp3Fenge(new File(Library.LOCAL_PATH+"Toxic.mp3"));
        origin.generateNewMp3ByTime(new File(Library.LOCAL_PATH+"Toxic-Cut.mp3"), 5000, 10000);
    }   
}
