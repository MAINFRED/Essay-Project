package spotify;

import com.google.code.mp3fenge.Mp3Fenge;
import com.google.code.mp3fenge.Mp3Info;
import java.io.File;
import java.io.FileNotFoundException;
import org.apache.commons.lang3.time.DurationFormatUtils;

/**
 * Represent a single song with its information.
 *
 * @author Antonioni Andrea & Zanelli Gabriele
 */
public class Song {

    private String title = "";
    private String artist = ""; // ArrayList per successive canzoni ad autore multiplo
    private String album = "";
    private long durationMillis; //milliseconds
    private String pathFile; // Percorso della canzone nella cache

    public Song(File file) throws FileNotFoundException {

        Metadata metadata = new Metadata(file);

        if (metadata.getTitle() == null) {
            this.title = file.getName();
        } else {
            this.title = metadata.getTitle();
        }

        if (metadata.getArtist() == null) {
            this.artist = "";
        } else {
            this.artist = metadata.getArtist();
        }

        if (metadata.getAlbum() == null) {
            this.album = "";
        } else {
            this.album = metadata.getAlbum();
        }

        Mp3Fenge origin = new Mp3Fenge(file);
        Mp3Info info = origin.getMp3Info();
        
        if(info == null)
            this.durationMillis = 0;
        else this.durationMillis = info.getTrackLength() * 1000; //moltiplicate to get milliseconds

        this.pathFile = file.getPath();
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

    public String getPath() {
        return pathFile;
    }

    public void setPath(String key) {
        this.pathFile = key;
    }

    @Override
    public String toString() {
        return "Song{" + "title=" + title + ", artist=" + artist + ", album=" + album + ", duration=" + this.getDuration() + ", pathFile: " + pathFile + '}';
    }

}
