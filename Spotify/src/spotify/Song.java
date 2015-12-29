
package spotify;

import java.time.Duration;

/**
 * Represent a single song with its information.
 * @author Antonioni Andrea & Zanelli Gabriele
 */

public class Song {
    private String title;
    private Artist artist; // ArrayList per successive canzoni ad autore multiplo
    private Duration duration;
    private String key; // Percorso della canzone nella cache
    
    public Song(String title, Artist artist, Duration duration) {
        this.title=title;
        this.artist=artist;
        this.duration=duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
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
