
package spotify;

import com.google.code.mp3fenge.Mp3Fenge;
import graphics.GUIController;
import java.io.File;
import javafx.beans.value.ChangeListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.media.MediaView;
import javafx.util.Duration;


/**
 * Manage all reproduction and settings of audio files.
 * @author Antonioni Andrea & Zanelli Gabriele
 */

public class AudioManage {
    private MediaPlayer mediaPlayer;
    private String songPath;
    private Duration timePauseIndex;
    private int volume;
    
    private ChangeListener<Duration> slideTimeChangeListener;
    
    public AudioManage(ChangeListener<Duration> slideTimeChangeListener) {
        this.mediaPlayer=null;
        this.songPath = null;
        this.timePauseIndex = new Duration(0);
        this.volume=70;
        
        this.slideTimeChangeListener = slideTimeChangeListener;
    }
    
    /**
     * Change the song to be played.
     * @param songPath A String containing the path of the song in local.
     */
    public void newSong(String songPath) {
        if(mediaPlayer!=null && mediaPlayer.getStatus()==MediaPlayer.Status.PLAYING)
            mediaPlayer.stop();
        this.songPath = songPath;
        Media media = new Media(new File(songPath).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.currentTimeProperty().addListener(slideTimeChangeListener);
        MediaView mediaView = new MediaView(mediaPlayer);
    }
    
    /**
     * Starts playing the song previusly selected from the last index of time.
     */
    public void play() {
        mediaPlayer.setVolume(volume*0.01);
        if(mediaPlayer!=null)
            mediaPlayer.play();
    }
    
    /**
     * Play a song from a certain second.
     * @param time A Duration indicating the desired start time.
     */
    public void playFromIndex(Duration time) {
        if(mediaPlayer.getStatus()==MediaPlayer.Status.STOPPED) {
            timePauseIndex=time;
            mediaPlayer.setStartTime(time);
        }
        else
            mediaPlayer.seek(time);
    }
    
    /**
     * Pauses the song in playback.
     */
    public void pause() {
        mediaPlayer.pause();
        timePauseIndex=mediaPlayer.getCurrentTime();
    }
    
    /**
     * Retrieve the current index of time of the song in playback.
     * @return A Duration indicating the index of time.
     */
    public Duration getCurrentTime() {
        return mediaPlayer.getCurrentTime();
    }
    
    /**
     * Retrieve the time left to the end of the song.
     * @return A Duration indicating the time left in milliseconds.
     */
    public Duration getTimeLeft() {
        if(mediaPlayer==null)
            return null;
        return mediaPlayer.getStopTime().subtract(mediaPlayer.getCurrentTime());
    }
    
    /**
     * Retrieve the status of the player.
     * @return A MediaPlayer.Status.
     */
    public Status getCurrentStatus() {
        if(mediaPlayer==null)
            return MediaPlayer.Status.UNKNOWN;
        return mediaPlayer.getStatus();
    }
    
    /**
     * Change the speakers volume.
     * @param volume An Integer indicating the value of volume from 0 to 100.
     */
    public void changeVolume(int volume) {
        this.volume=volume;
        mediaPlayer.setVolume(volume*0.01);
    }
    
    /**
     * Retrieve the current volume of the speakers.
     * @return An Integer indicating the value of volume from 0 to 100.
     */
    public int getVolume() {
        return volume;
    }
    
    public void cutFileAudio() {
        Mp3Fenge origin = new Mp3Fenge(new File(Library.LOCAL_PATH+"Toxic.mp3"));
        origin.generateNewMp3ByTime(new File(Library.LOCAL_PATH+"Toxic-Cut.mp3"), 5000, 10000);
    }   
}
