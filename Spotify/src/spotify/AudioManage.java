
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
    
    /**
     * Change the song to be played.
     * @param songPath A String containing the path of the song in local.
     */
    public void newSong(String songPath) {
        this.songPath = songPath;
        Media media = new Media(new File(songPath).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);
    }
    
    /**
     * Starts playing the song previusly selected from the last index of time.
     */
    public void play() {
        mediaPlayer.play();
    }
    
    /**
     * Play a song from a certain second.
     * @param millis An Int indicating the desired start time in milliseconds.
     */
    public void playIndex(int millis) {
        mediaPlayer.setStartTime(new Duration(millis));
        mediaPlayer.play();
    }
    
    /**
     * Pauses the song in playback.
     */
    public void pause() {
        mediaPlayer.pause();
        // Salvare l'indice di tempo attuale
    }
    
    /**
     * Retrieve the actual index of time of the song in playback.
     * @return A Duration indicating the index of time in milliseconds.
     */
    public Duration getCurrentTime() {
        return mediaPlayer.getCurrentTime();
    }
    
    /**
     * Retrieve the time left to the end of the song.
     * @return A Duration indicating the time left in milliseconds.
     */
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
