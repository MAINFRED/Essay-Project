
package spotify;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Represents a set of songs.
 * Library object contains all the songs the user decides to save, 
 * @author Antonioni Andrea & Zanelli Gabriele
 */

public class Library {
    private HashMap<String,Song> library;
    private HashMap<String,Playlist> playlists;
    
    public Library() {
    this.library = new HashMap<>();
    this.playlists = new HashMap<>();
    }
    
    /**
     * Retrieve the song by name.
     * @param name The name of the song.
     * @return an instance of Song.
     */
    public Song getSongByName(String name) {
        return library.get(name);
    }
    
    /**
     * Gets the list of the playlists' names.
     * @return A List containing playlists' names.
     */
    public List getPlaylistsName() {
        return (List)playlists.keySet();
    }
    
    /**
     * Retrieve a List containing all songs' names in the playlist.
     * @param name Name of the playlist.
     * @return A List containing the playlist's songs' names.
     */
    public List getPlaylistSongsNames(String name) {
        return  playlists.get(name).getSongsNames();
    }
    
    /**
     * Retrieve a List containing all songs' names in the library.
     * @return A List containing the playlist's songs' names.
     */
    public List getAllSongsNames() {
        return (List)library.keySet();
    }
    
}
