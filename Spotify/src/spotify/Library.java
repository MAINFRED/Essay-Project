
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
//    private HashMap<String,String> files;
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
    
    /**
     * Adds a new song to the library using a File object.
     * @param file A File object which represents the song to add to the library
     */
    public void addSong(File file) throws FileNotFoundException
    {
        tracks.add(new Song(file));
    }
    
//    /**
//     * Retrieve the song by name.
//     * @param name The name of the song.
//     * @return an instance of Song.
//     */
//    public Song getSongByName(String name) {
//        return tracks.get(name);
//    }
    
    
//    /**
//     * Gets the list of the playlists' names.
//     * @return A List containing playlists' names.
//     */
//    public List getPlaylistsName() {
//        return (List)playlists.keySet();
//    }
//    
//    /**
//     * Retrieve a List containing all songs' names in the playlist.
//     * @param name Name of the playlist.
//     * @return A List containing the playlist's songs' names.
//     */
//    public List getPlaylistSongsNames(String name) {
//        return  playlists.get(name).getSongsNames();
//    }
//    
//    /**
//     * Retrieve a List containing all songs' names in the library.
//     * @return A List containing the playlist's songs' names.
//     */
//    public List getAllSongsNames() {
//        return (List)tracks.keySet();
//    }
    
}
