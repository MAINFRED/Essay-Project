
package spotify;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a list of songs with a title.
 * @author Antonioni Andrea & Zanelli Gabriele
 */

public class Playlist {
    private String title;
    private List<Song> songs;
    
    /**
     * Creates an empty Playlist object with a title
     * @param title A String object which represents the title of the playlist
     */
    public Playlist(String title){
        this.title = title;
        this.songs = new ArrayList<>();
    }
    
    /**
     * Creates a Playlist object with a title using a List object. 
     * It doesn't create a copy of the List object, it uses just a pointer.
     * @param title A String object which represents the title of the playlist
     * @param songs A List object which containts a list of Song representing names of the songs already exist in the Library
     * @see spotify.Library#library
     */
    public Playlist(String title, List<Song> songs){
        // MODIFICA PER COPIARE LE CANZONI MA NON LA STESSA PLAYLIST
        this.title = title;
        this.songs = songs;
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
        List names = new ArrayList();
        for(Song song : songs){
            names.add(song.getTitle());
        }
        return names;
    }
    
    public void addSong(Song song){
        songs.add(song);
    }     
}
