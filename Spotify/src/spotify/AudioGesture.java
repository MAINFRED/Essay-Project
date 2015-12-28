
package spotify;

import com.google.code.mp3fenge.Mp3Fenge;
import com.google.code.mp3fenge.Mp3Info;
import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;


/**
 * @author Zanelli Gabriele
 */

public class AudioGesture {
    private byte[] song;
    private String resPath = "/Users/Gabriele/Desktop/Essay-Project/Spotify/src/resources/";
    
    public AudioGesture() {
        song = null;
    }
    
    public void newSong(byte[] song) {
        this.song = song;
    }
    
    public void play() {
        playMediaFromPath(resPath+"Toxic.mp3");
    }
    
    public void playIndex(/*Con l'indice di partenza*/) {
        
    }
    
    public void pause() {
        
    }
    
    public void changeVolume() {
        
    }
    
    public void playMediaFromPath(String path){
        Media media = new Media(new File(path).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        MediaView mediaView = new MediaView(mediaPlayer);
    }
    
    public void cutFileAudio() {
        Mp3Fenge origin = new Mp3Fenge(new File(resPath+"Toxic.mp3"));
//        origin.generateNewMp3ByTime(new File(resPath+"Toxic-Cut.mp3"), 5000, 10000);
        Mp3Info ciao = origin.getMp3Info();
        if(ciao==null)
            System.out.println("porcodio");
        else {
        int min = ciao.getTrackLength();
        int sec = ciao.getTrackLength() - (min*60);
        System.out.println(min+":"+sec);
        }
    }   
}
