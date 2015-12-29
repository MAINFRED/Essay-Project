
package spotify;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

/**
 * Represents a set of songs.
 * Library object contains all the songs the user decides to save, 
 * @author Antonioni Andrea & Zanelli Gabriele
 */

public class Library {
    private ObservableMap<String,Song> tracks;
    private ObservableMap<String,Playlist> playlists;
    
    /**
     * Represents the path in which are located songs imported by the user.
     */
    public static String LOCAL_PATH = "resources/local/";
    
    public Library() {
        this.tracks = FXCollections.observableHashMap();
        this.playlists = FXCollections.observableHashMap();
    }
    
    public ObservableMap getLibraryPointer() {
        return tracks;
    }
    
    public ObservableMap getPlaylistsPointer() {
        return playlists;
    }

    /**
     * Retrieve the song by name.
     * @param name The name of the song.
     * @return an instance of Song.
     */
    public Song getSongByName(String name) {
        return tracks.get(name);
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
        return (List)tracks.keySet();
    }
    
}
