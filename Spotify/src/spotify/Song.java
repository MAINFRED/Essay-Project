
package spotify;

import java.time.Duration;

/**
 * Represent a single song with its information.
 * @author Antonioni Andrea & Zanelli Gabriele
 */

public class Song {
    private String title;
    private String artist; // ArrayList per successive canzoni ad autore multiplo
    private String album;
    private Duration duration;
    private String key; // Percorso della canzone nella cache
    
    public Song(String title, String artist, String album, Duration duration) {
        this.title=title;
        this.artist=artist;
        this.album = album;
        this.duration=duration;
    }

    public Song(String title, Duration duration) {
        this.title = title;
        this.duration=duration;
    }

    public Song(String title, String artist, Duration duration) {
        this.title = title;
        this.artist = artist;
        this.duration=duration;
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

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    
    
}
