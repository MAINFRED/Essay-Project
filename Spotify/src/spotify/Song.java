
package spotify;

import java.time.Duration;

/**
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
}
