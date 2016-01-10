
package spotify;

import com.google.code.mp3fenge.Mp3Fenge;
import java.io.File;
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
    private Duration timeIndex;
    private int volume;
    
    public AudioManage() {
        this.mediaPlayer=null;
        this.songPath = null;
        this.timeIndex = new Duration(0);
        this.volume=70;
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
     * @param time A Duration indicating the desired start time.
     */
    public void playFromIndex(Duration time) {
        if(mediaPlayer.getStatus()==MediaPlayer.Status.PLAYING)
            mediaPlayer.stop();
        mediaPlayer.setStartTime(time);
        mediaPlayer.play();
    }
    
    /**
     * Pauses the song in playback.
     */
    public void pause() {
        mediaPlayer.pause();
        timeIndex=mediaPlayer.getCurrentTime();
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
        return mediaPlayer.getStopTime().subtract(mediaPlayer.getCurrentTime());
    }
    
    /**
     * Retrieve the status of the player.
     * @return A MediaPlayer.Status.
     */
    public Status getCurrentStatus() {
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
