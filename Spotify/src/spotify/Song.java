package spotify;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import org.apache.commons.lang3.time.DurationFormatUtils;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Duration;
import javax.imageio.ImageIO;
import spotify.MusicPlayer.sortType;

/**
 * Represent a single song with its information.
 * @author Antonioni Andrea & Zanelli Gabriele
 */
public class Song implements Comparable<Song>, Serializable{

    private String title = "";
    private String artist = ""; // ArrayList per successive canzoni ad autore multiplo
    private String album = "";
    private final Duration duration; //milliseconds
    private byte[] cover;
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
        
        this.cover = null;
        this.duration = new Duration(metadata.getDuration());
       
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

    public Duration getDuration() {
        return duration;
    }
    
    public String getDurationAsString() {
        return DurationFormatUtils.formatDuration((long)duration.toMillis(), "mm:ss");
    }

    public String getPath() {
        return pathFile;
    }

    public void setPath(String key) {
        this.pathFile = key;
    }

    public BufferedImage getArtwork() throws IOException {
        try {
            /*Leggo l'Album Artwork dai tag del file .mp3*/
            Mp3File song = new Mp3File(getPath());
            if (song.hasId3v2Tag()) {
                ID3v2 id3v2tag = song.getId3v2Tag();
                byte[] imageData = id3v2tag.getAlbumImage();
                //converting the bytes to an image
                BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageData));
                return img;
            }
            else return null;
        } catch (UnsupportedTagException ex) {
            Logger.getLogger(Song.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidDataException ex) {
            Logger.getLogger(Song.class.getName()).log(Level.SEVERE, null, ex);
        } catch(NullPointerException ex){
            return  null;
        }
        return null;
    }

    @Override
    public String toString() {
        return "Song{" + "title=" + title + ", artist=" + artist + ", album=" + album + ", duration=" + this.getDurationAsString() + ", pathFile: " + pathFile + '}';
    }

    @Override
    public int compareTo(Song secondSong) {
        // Questa variabile dovrà essere gettata dalla playlist che si sta ordinando!
        sortType sortMethod = sortType.Title; 
        
        if(sortMethod == sortType.Artist)
            return this.artist.compareTo(secondSong.artist);
        else if(sortMethod == sortType.Album)
            return this.album.compareTo(secondSong.album);
        else if(sortMethod == sortType.Title)
            return this.title.compareTo(secondSong.title);
        else
            System.out.println("Something Exploded when Sorting Besos");
        return 1;
    }

}
