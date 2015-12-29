
package spotify;

import java.util.LinkedList;
import java.util.List;
import javafx.collections.ObservableMap;

/**
 * MusicPlayer is the interface between user and application which convert user's input and manage
*  the library's song, played song, next songs and audio.
 * @author Antonioni Andrea & Zanelli Gabriele
 */

public class MusicPlayer {
    private Library library;
    private AudioManage audioManage;
    private LinkedList<String> listenedSongs; 
    private LinkedList<String> nextSongs;
    private String actualSong;
//    private enum sortType{Title,Album,Artist};
    private String preferredSort;
    private boolean repeatSong;
    private boolean repeatPlaylist;
    private boolean reproduceShuffle;
    
    /** Initialize
    * listenedSongs: Stack of listened songs;
    * nextSong: Queue of next songs;
    * prefferedSort: 
    */
    
    public MusicPlayer() {
        library = new Library();
        audioManage = new AudioManage();
        listenedSongs=new LinkedList<>();
        nextSongs=new LinkedList<>();
        preferredSort="Title";
        repeatSong=false;
        repeatPlaylist=false;
        reproduceShuffle=false;
    }
    
    public ObservableMap getLibraryPointer() {
        return library.getLibraryPointer();
    }
    
    public ObservableMap getPlaylistsPointer() {
        return library.getPlaylistsPointer();
    }
    
    /**
     * Start playing the selected song.
     */
    public void play() {
        audioManage.play();
    }
    
    /**
     * Pause the selected song.
     */
    public void pause() {
        audioManage.pause();
    }
    
    /**
     * Skip forward to the next song.
     */
    public void nextSong() {
        String next = nextSongs.pollLast();
        if(next!=null) {
            playNewSong(next);
            listenedSongs.push(actualSong);
            actualSong=next;
        }
    }
    
    /**
    * Skip back to the previus song.
    */
    public void previusSong() { 
        //If there are no previus songs or the song has started for more than 10 seconds, reproduce the same 
        // song from the beginning else reproduce the previus song.
        if(/*Secondi Riprodotti >10 or*/listenedSongs.isEmpty())
            audioManage.playIndex(0); 
        else {
            String previus = listenedSongs.pop();
            playNewSong(previus);
            nextSongs.addLast(actualSong);
            actualSong = previus;
        }
    }
    
    /**
     * Change repeat song preference.
     * @param value Indicating activation of single song repeat.
     */
    public void repeatSong(boolean value) {
        this.repeatSong=value;
        
        // Decidere se mettere la stessa canzone in coda 10 volte o se
        // Semplicemente ripetere la riproduzione
    }
    
    /**
     * Change repeat playlist preference.
     * @param value Indicating activation of playlist repeat once terminated.
     */
    public void repeatPlaylist(boolean value) {
        this.repeatPlaylist=value;
    }
    
    /**
     * Change reproduce shuffle preference.
     * @param value Indicating activation of random song playing.
     */
    public void reproduceShuffle(boolean value) {
        this.reproduceShuffle=value;
        
        // Aggiornamento canzoni in coda
    }
    
    /**
     * Change the value of volume.
     * @param volume The value of volume.
     */
    public void changeVolume(int volume) {
        if(volume>=0 & volume <=100)
            audioManage.changeVolume(volume);
    }
    
    /**
     * Change the preferred sorting method between Title, Arist or Album.
     * @param sortMethod The name of sorting method.
     */
    public void changePreferredSort(String sortMethod){
        if("Title".equals(sortMethod) || "Artist".equals(sortMethod) || "Album".equals(sortMethod))
            this.preferredSort=sortMethod;
        
       //Aggiornamento ordine playlist da fare o qua o in un thread separato di continuo
    }
    
    /**
     * Retrieve a List of the playlist's names.
     * @return A List containing playlists' names.
     */
    public List getPlaylistsName(){
        return library.getPlaylistsName();
    }
    
    /**
     * Retrieve a List containing all songs' names in the playlist.
     * @param name Name of the playlist.
     * @return A List containing the playlist's songs' names.
     */
    public List getPlaylistSongsNames(String name) {
        return library.getPlaylistSongsNames(name);
    }
    
    /**
     * Retrieve a List containing all songs' names in the library.
     * @return A List containing the playlist's songs' names.
     */
    public List getAllSongsNames() {
        return library.getAllSongsNames();
    }
    
    /**
     * Gets the name of the song and retrieve the mp3 from the library in order to play it 
     * @param newSong Name/Key of the song.
     */
    private void playNewSong(String name) {
        Song newSong = library.getSongByName(name);
        byte[] byteSong = new byte[10]; 
        audioManage.newSong(byteSong);  
        audioManage.play();
    }
    
}
