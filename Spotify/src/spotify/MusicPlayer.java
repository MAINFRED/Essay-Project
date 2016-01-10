
package spotify;

import java.util.LinkedList;
import java.util.Random;
import javafx.collections.FXCollections;
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
    private Fred checkPlaybackFred;
    private LinkedList<Song> nextSongs;
    private Song actualSong;
//    private enum sortType{Title,Album,Artist};
    private ObservableList currentPlaylist;
    private int songNumber;
    private String preferredSort;
    private int repeat;
    private boolean reproduceShuffle;
    
    public static final int REPEAT_SINGLE_SONG=1;
    public static final int REPEAT_PLAYLIST=0;
    public static final int NO_REPEAT=-1;
    
    
    public MusicPlayer() {
        library = new Library();
        audioManage = new AudioManage();
        checkPlaybackFred = new Fred(this,audioManage);
        nextSongs=new LinkedList<>();
        currentPlaylist = FXCollections.observableArrayList();
        preferredSort="Title";
        repeat=NO_REPEAT;
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
        if(repeat==REPEAT_SINGLE_SONG)
            audioManage.playIndex(0); // If repeat single song is activated, just replay.
        else {
            actualSong = nextSongs.pollLast();
            // Check if there is actually a song to play
            if(actualSong!=null) {
                // If the song was the last one, restard from top.
                playNewSong(actualSong,currentPlaylist,songNumber+1%currentPlaylist.size());     
                
                // Aggiungere la nuova canzone alla fine del vettore
            }
        }
    }
    
    /**
    * Skip back to the previus song.
    * If there are no previus songs or the song has started for more than 10 seconds, reproduce the same 
    + song from the beginning else reproduce the previus song.
    */
    public void previusSong() {
        if(audioManage.getCurrentTime().lessThan(new Duration(10000)) || songNumber <= 0)
            audioManage.playIndex(0); 
        else {
            nextSongs.addLast(actualSong);
            actualSong = (Song)currentPlaylist.get(songNumber-1);
            playNewSong(actualSong,currentPlaylist,songNumber-1);
        }
        // Modificare la lista di canzoni successive
    }
    
    /**
     * Change repeat preference.
     * @param value A Static Value from MusicPlayer class indicating the repeat preferince.
     */
    public void repeatPreference(int value) {
        this.repeat=value;
    }
    
    /**
     * Change reproduce shuffle preference.
     * @param value A Booleran indicating activation of random song playing.
     */
    public void shuffle(boolean value) {
        this.reproduceShuffle=value;
        generateSongQueue();
    }
    
    /**
     * Retrieve the shuffle value.
     * @return A Boolean indicating if random song playing is activated.
     */
    public boolean getShuffle() {
        return reproduceShuffle;
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
     * @param currentPlaylist A pointer to the playlist in which the song is.
     * @param songNumber The number of the song in the playlist once ordered.
     */
    public void playNewSong(Song newSong, ObservableList currentPlaylist, int songNumber) {
        // Funzione searchSong che cerca la canzone in locale ed eventualmente la richiede al server E SETTA IL PATH LOCALE
        // Settare la variabile locale a true
        this.currentPlaylist=currentPlaylist;
        this.songNumber=songNumber;
        String path = newSong.getPath();
        audioManage.newSong(path);  
        audioManage.play();
          // Da fare in thread
        generateSongQueue();
    }
    
    /**
     * Generate the next ten songs for the queue.
     */
    private void generateSongQueue() {
        nextSongs.clear();
        // Add 10 times the actual song
        if(repeat==REPEAT_SINGLE_SONG) {
            for(int i=0;i<10;i++)
                nextSongs.add(actualSong);
        }
        // Add 10 random songs.
        else if(reproduceShuffle) {
            for(int i=0;i<10;i++)
                nextSongs.add((Song)currentPlaylist.get(new Random().nextInt(currentPlaylist.size())));
        }
        // Add the next 10 songs 
        else {
            for(int i=1;i<=10;i++){
                // If it reaches the end and repeat playlist is on, goes to the top.
                if(songNumber>=currentPlaylist.size()-1)
                    if(repeat==REPEAT_PLAYLIST)
                        nextSongs.add((Song)currentPlaylist.get(songNumber+i%currentPlaylist.size()));
                    else
                        return;
                else
                    nextSongs.add((Song)currentPlaylist.get(songNumber+i));
            }
        }
    }
}
