
package spotify;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import spotify.MusicPlayer.sortType;

/**
 * Represents a list of songs with a title.
 * @author Antonioni Andrea & Zanelli Gabriele
 */

public class Playlist implements Serializable{
    private String title;
    private sortType orderedBy;
    private ObservableList<Song> songs;
    
    /**
     * Creates an empty Playlist object with a title
     * @param title A String object which represents the title of the playlist
     */
    public Playlist(String title){
        this.title = title;
        this.orderedBy = sortType.Title;
        this.songs = FXCollections.observableArrayList();
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
    
    public List getSongsPointer(){
        return songs;
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
    
    public void removeSong(Song song){
        songs.remove(song);
    }
    
    public boolean isEmpty() {
        return songs.isEmpty();
    }
    
    public void orderBy(sortType orderType) {
        orderedBy = orderType;
        FXCollections.sort(songs);
    }
}
