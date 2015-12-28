
package spotify;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Antonioni Andrea & Zanelli Gabriele
 */

public class Library {
    HashMap <String,Song> library;
    ArrayList<Playlist> playlists;
    
    public Library() {
    this.library = new HashMap<>();
    this.playlists = new ArrayList<>();
}
   
    public Song getSongByName(String name) {
        return library.get(name);
    }
    
}
