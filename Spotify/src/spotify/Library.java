
package spotify;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents a set of songs.
 * Library object contains all the songs the user decides to save, 
 * @author Antonioni Andrea & Zanelli Gabriele
 */

public class Library {
    private HashMap <String,Song> library;
    private ArrayList<Playlist> playlists;
    
    public Library() {
    this.library = new HashMap<>();
    this.playlists = new ArrayList<>();
}
   
    public Song getSongByName(String name) {
        return library.get(name);
    }
    
}
