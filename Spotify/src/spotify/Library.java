package spotify;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import spotify.MusicPlayer.sortType;

/**
 * Represents the set of all songs and playlist added and created by the user.
 *
 * @author Antonioni Andrea & Zanelli Gabriele
 */
public class Library implements Serializable {

    private Playlist allTracks;
    private ObservableList<Playlist> playlists;

    private static Library instance = null;

    /**
     * Represents the path in which are located songs imported by the user.
     */
    public static final String LOCAL_PATH = "resources/local/";

    private Library() {
        allTracks = new Playlist("All Tracks");
        this.playlists = FXCollections.observableArrayList();
    }
    
    private Library(ObjectInputStream stream) throws IOException, ClassNotFoundException{
        allTracks = new Playlist(stream);
        this.playlists = FXCollections.observableArrayList((ArrayList)stream.readObject());
    }
    /**
     * Returns a reference of the Library object created by default method
     * @return A reference of the Library object created by default method
     */
    public static Library getInstance() {
        if (instance == null) {
            instance = new Library();
        }
        return instance;
    }
    
    public static Library getInstance(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        if (instance == null) {
            instance = new Library(stream);
        }
        return instance;
    }

    /**
     * Returns a reference of the tracks list which is unmodifiable.
     * @return a reference of the tracks list which is unmodifiable.
     */
    public ObservableList getAllTracks() {
        return FXCollections.unmodifiableObservableList((ObservableList<Song>) allTracks.getSongsPointer());
    }

    /**
     * Returns a reference of the playlists list which is unmodifiable.
     * @return a reference of the playlists list which is unmodifiable.
     */
    public ObservableList getPlaylistsPointer() {
        return FXCollections.unmodifiableObservableList(playlists);
    }

    /**
     * Adds a new song to the library using a File object.
     * @param file A File object which represents the song to add to the library
     * @throws java.io.FileNotFoundException
     */
    public void addSong(File file) throws FileNotFoundException {
        allTracks.addSong(new Song(file));
    }

    /**
     * Add a new playlist.
     * @param name Name of the new playlist.
     */
    public void addPlaylist(String name) {
        if (name.equals("")) {
            name = "Unknown";
        }

        int i = 0, k = 0;

        for (Playlist playlist : playlists) {
            if (playlist.getTitle().equals(name)) {
                i++;
            }
        }
        if (i != k) {
            do {
                k = i;
                for (Playlist playlist : playlists) {
                    if (playlist.getTitle().equals(name + i)) {
                        i++;
                    }
                }
            } while (i == k);
            playlists.add(new Playlist(name + i));
        } else {
            playlists.add(new Playlist(name));
        }
    }

    /**
     * Remove a playlist maintaining the songs copied in All Tracks/Songs.
     * @param playlist The pointer of the playlist to remove.
     */
    public void removePlaylist(Playlist playlist) {
        playlists.remove(playlist);
    }

    /**
     * Rename a playlist.
     * @param selectedPlaylist A pointer to the playlist whose name you want to
     * change.
     * @param newName The new name of the playlist.
     */
    public void renamePlaylist(Playlist selectedPlaylist, String newName) {
        if (newName.equals("")) {
            newName = "Senza titolo";
        }

        int i = 0, k = 0;
        for (Playlist playlist : playlists) {
            if (playlist.getTitle().equals(newName)) {
                i++;
            }
        }
        if (i != k) {
            do {
                k = i;
                for (Playlist playlist : playlists) {
                    if (playlist.getTitle().equals(newName + i)) {
                        i++;
                    }
                }
            } while (i == k);
            this.getPlaylist(selectedPlaylist.getTitle()).setTitle(newName + i);
        } else {
            this.getPlaylist(selectedPlaylist.getTitle()).setTitle(newName);
        }
    }

    /**
     * Retrieve a playlist from its name.
     * @param title The name of the playlist.
     * @return A Pointer to the playlist.
     */
    public Playlist getPlaylist(String title) {
        for (Playlist p : playlists) {
            if (p.getTitle().equals(title)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Remove a song from a playlist.
     * @param song A pointer to the song to remove.
     * @param playlist The pointer to the playlist which countains the song.
     */
    public void removeSongFromPlaylist(Song song, Playlist playlist) {
        playlist.removeSong(song);
        if(playlist==allTracks) {
            for(Playlist current:playlists){
               current.removeSong(song);
            }
        }
        else {
            boolean otherInstance = false;
            for(Playlist current:playlists){
                if(current.contains(song))
                    otherInstance=true;
            }
            if(!otherInstance)
                allTracks.removeSong(song);
        }
    }
    
     // Utilizzabile solo da music player
    public Playlist retrievePlaylist(int playlistNumber){
        if(playlistNumber<0)
            return allTracks;
        else if(playlistNumber<playlists.size())
            return playlists.get(playlistNumber);
        else {
            System.out.println("Invalid playlist number");
            return null;
        }  
    }

    public void writeObject(ObjectOutputStream stream) throws IOException {
        allTracks.writeObject(stream);
        stream.writeObject(new ArrayList(playlists));
    }
    
    public void orderPlaylistBy(int playlistNumber,sortType sortMethod) {
        if(retrievePlaylist(playlistNumber)==null)
            System.out.println("Invalid playlist");
        else
            retrievePlaylist(playlistNumber).orderBy(sortMethod);
    }

}
