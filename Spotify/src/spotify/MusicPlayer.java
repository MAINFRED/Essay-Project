
package spotify;

import java.util.LinkedList;
import java.util.Random;
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
    private LinkedList<Song> listenedSongs; 
    private LinkedList<Song> nextSongs;
    private Song actualSong;
//    private enum sortType{Title,Album,Artist};
    private int playlistNumber;
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
        listenedSongs=new LinkedList<>();
        nextSongs=new LinkedList<>();
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
            audioManage.playIndex(0); // Replaya la stessa canzone
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
            audioManage.playIndex(0); 
        else {
            Song previus = listenedSongs.pop();
            playNewSong(previus,playlistNumber,songNumber-1);
            nextSongs.addLast(actualSong);
            actualSong = previus;
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
        generateNextSongs();
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
     * @param playlistNumber The number of the playlist cointaining the song, -1 for All Tracks.
     * @param songNumber The number of the song in the playlist once ordered.
     */
    public void playNewSong(Song newSong, int playlistNumber, int songNumber) {
        // Funzione searchSong che cerca la canzone in locale ed eventualmente la richiede al server E SETTA IL PATH LOCALE
        // Settare la variabile locale a true
        String path = newSong.getPath();
        audioManage.newSong(path);  
        audioManage.play();
          // Da fare in thread
        generateNextSongs();
    }
    
    /**
     * Generate the next ten songs for the queue.
     */
    private void generateNextSongs() {
        listenedSongs.clear(); 
        ObservableList currentPlaylist;
        if(playlistNumber==-1 || reproduceShuffle)
             currentPlaylist = library.getTracksPointer();
        else 
            currentPlaylist = (ObservableList)library.getPlaylistsPointer().get(playlistNumber);
        
        for(int i=1;i<=10;i++){
            if(reproduceShuffle) {
                nextSongs.add((Song)currentPlaylist.get(new Random().nextInt(currentPlaylist.size())));
            }
            else
                nextSongs.add((Song)currentPlaylist.get(songNumber+i));
        }
    }
}
