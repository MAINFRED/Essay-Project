
package spotify;

import java.util.LinkedList;
import javafx.collections.ObservableList;
import javafx.util.Duration;

/**
 * MusicPlayer is the interface between user and application which convert user's input and manage
*  the library's song, played song, next songs and audio.
 * @author Antonioni Andrea & Zanelli Gabriele
 */

public class MusicPlayer {
    private Library library;
    private AudioManage audioManage;
    private LinkedList<Song> listenedSongs; 
    private LinkedList<Song> nextSongs;
    private Song actualSong;
//    private enum sortType{Title,Album,Artist};
    private int playlistNumber;
    private int songNumber;
    private String preferredSort;
    private boolean repeatSong;
    private boolean repeatPlaylist;
    private boolean reproduceShuffle;
    
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
    
    /**
     * Returns a pointer to the Library.
     * @return a pointer to the Library.
     */
    public Library getLibrary() {
        return library;
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
        Song next = nextSongs.pollLast();
        if(next!=null) {
            playNewSong(next,playlistNumber,songNumber+1);
            listenedSongs.push(actualSong);
            actualSong=next;
            // Aggiungere la nuova canzone alla fine del vettore
        }
    }
    
    /**
    * Skip back to the previus song.
    */
    public void previusSong() { 
        //If there are no previus songs or the song has started for more than 10 seconds, reproduce the same 
        // song from the beginning else reproduce the previus song.
        if(audioManage.getCurrentTime().lessThan(new Duration(10000)) || listenedSongs.isEmpty())
            audioManage.playIndex(new Duration(0)); 
        else {
            Song previus = listenedSongs.pop();
            playNewSong(previus,playlistNumber,songNumber-1);
            nextSongs.addLast(actualSong);
            actualSong = previus;
        }
        // Modificare la lista di canzoni successive
    }
    
    /**
     * Change repeat song preference.
     * @param value A Boolean indicating activation of single song repeat.
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
     * Starts playing a new song. 
     * @param newSong An istance of Song containing the new song to play.
     * @param playlistNumber The number of the playlist cointaining the song, -1 for All Tracks.
     * @param songNumber The number of the song in the playlist once ordered.
     */
    public void playNewSong(Song newSong, int playlistNumber, int songNumber) {
        // Funzione searchSong che cerca la canzone in locale ed eventualmente la richiede al server E SETTA IL PATH LOCALE
        // Settare la variabile locale a true
        String path = newSong.getPath();
        audioManage.newSong(path);  
        audioManage.play();
        listenedSongs.clear(); 
          // Da fare in thread
        generateNextSongs();
    }
    
    private void generateNextSongs() {
        ObservableList currentPlaylist;
        if(playlistNumber==-1)
             currentPlaylist = library.getTracksPointer();
        else 
            currentPlaylist = (ObservableList)library.getPlaylistsPointer().get(playlistNumber);
        
        for(int i=1;i<=10;i++){
            nextSongs.add((Song)currentPlaylist.get(songNumber+i));
        }
    }
    
}
