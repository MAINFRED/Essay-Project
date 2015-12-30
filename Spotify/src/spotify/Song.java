
package spotify;

import com.google.code.mp3fenge.Mp3Fenge;
import com.google.code.mp3fenge.Mp3Info;
import java.io.File;
import java.io.FileNotFoundException;
import org.apache.commons.lang3.time.DurationFormatUtils;

/**
 * Represent a single song with its information.
 * @author Antonioni Andrea & Zanelli Gabriele
 */

public class Song {
    private String title;
    private String artist; // ArrayList per successive canzoni ad autore multiplo
    private String album;
    private long durationMillis; //milliseconds
    private String key; // Percorso della canzone nella cache
    
    public Song(File file) throws FileNotFoundException
    {
        Metadata metadata = new Metadata(file);
        this.title = metadata.getTitle();
        this.artist = metadata.getArtist();
        this.album = metadata.getArtist();
        
        Mp3Fenge origin = new Mp3Fenge(file);
        Mp3Info info = origin.getMp3Info();
        this.durationMillis = info.getTrackLength()*1000; //moltiplicate to get milliseconds
        
        System.out.println(toString());
    }
    
    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getDuration() {
        return DurationFormatUtils.formatDuration(durationMillis, "mm:ss");
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "Song{" + "title=" + title + ", artist=" + artist + ", album=" + album + ", duration=" + this.getDuration() + '}';
    }
      
    
}
