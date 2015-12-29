
package spotify;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a list of songs with a title.
 * @author Antonioni Andrea & Zanelli Gabriele
 */

public class Playlist {
    private List<String> keys;
    private String title;
    
    /**
     * Creates an empty Playlist object with a title
     * @param title A String object which represents the title of the playlist
     */
    public Playlist(String title){
        this.title = title;
        this.keys = new ArrayList<>();
    }
    
    /**
     * Creates a Playlist object with a title using a List object. 
     * It doesn't create a copy of the List object, it uses just a pointer.
     * @param title A String object which represents the title of the playlist
     * @param keys A List object which containts a list of String representing names of the songs already exist in the Library
     * @see spotify.Library#library
     */
    public Playlist(String title, List<String> keys){
        this.title = title;
        this.keys = keys;
    }

    /**
     * Returns a String object which represents the title of the playlist
     * @return A String object which represents the title of the playlist
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the playlist.
     * @param title A String object which represents the title of the playlist
     */
    public void setTitle(String title) {
        this.title = title;
    }
    
    /**
     * Retrieve the names of the songs.
     * @return A List containing the names of the songs.
     */
    public List getSongsNames() {
        return keys;
    }
    
    
}
