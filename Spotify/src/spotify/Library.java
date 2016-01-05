
package spotify;

import java.io.File;
import java.io.FileNotFoundException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Represents the set of all songs and playlist added and created by the user.
 * @author Antonioni Andrea & Zanelli Gabriele
 */

public class Library {
    private ObservableList<Song> tracks;
    private ObservableList<Playlist> playlists;
    
    /**
     * Represents the path in which are located songs imported by the user.
     */
    public static String LOCAL_PATH = "resources/local/";
    
    public Library() {
        this.tracks = FXCollections.observableArrayList();
        this.playlists = FXCollections.observableArrayList();
    }
    
    /**
     * Returns a pointer to the tracks list.
     * @return a pointer to the tracks list.
     */
    public ObservableList getTracksPointer() {
        return tracks;
    }
    
    /**
     * Returns a pointer to the playlist list.
     * @return a pointer to the playlist list.
     */
    public ObservableList getPlaylistsPointer() {
        return playlists;
    }
    
    /**
     * Add a new playlist.
     * @param name Name of the new playlist.
     */
    public void addPlaylist(String name) {
        if(name.equals("")) 
            name = "Senza titolo";
        
        int i=0, k=0;
        for(Playlist playlist : playlists) {
            if(playlist.getTitle().equals(name))
                i++;
        }
        if(i!=k) {
            do {
                k=i;
                for(Playlist playlist : playlists) {
                    if(playlist.getTitle().equals(name+i))
                       i++;
                }
            } while(i==k);
            playlists.add(new Playlist(name+i));
        }
        else
            playlists.add(new Playlist(name));
    }
    
    /**
     * Remove a playlist maintaining the songs copied in All Tracks/Songs.
     * @param playlist The pointer of the playlist to remove.
     */
    public void removePlaylist(Playlist playlist) {
        playlists.remove(playlist);
    }
    
    public void renamePlaylist(Playlist playlist, String newName){
        this.getPlaylist(playlist.getTitle()).setTitle(newName);
    }
    
    public Playlist getPlaylist(String title)
    {
        for(Playlist p : playlists)
            if(p.getTitle().equals(title))
                return p;
        return null;
    }
    
    public void removeSongFromPlaylist(Song song, Playlist playlist)
    {
        playlist.removeSong(song);
    }
    
    /**
     * Adds a new song to the library using a File object.
     * @param file A File object which represents the song to add to the library
     */
    public void addSong(File file) throws FileNotFoundException
    {
        tracks.add(new Song(file));
    }
    
}
