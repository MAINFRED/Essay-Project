
package spotify;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/**
 *
 * @author Andrea Antonioni -
 * <a href="mailto:andreaantonioni97@gmail.com">andreaantonioni97@gmail.com</a>
 */
public class PlaylistLibrary {
    private ObservableList<Playlist> playlists;
    
    public PlaylistLibrary()
    {
        playlists = FXCollections.observableArrayList();
        
    }
    
    public void addListener(ListChangeListener<? super Playlist> listener)
    {
        playlists.addListener(listener);
    }
    
    public void addPlaylist(Playlist playlist)
    {
        playlists.add(playlist);
    }
}
