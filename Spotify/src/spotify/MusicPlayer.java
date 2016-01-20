
package spotify;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;

/**
 * MusicPlayer is the interface between user and application which convert user's input and manage
*  the library's song, played song, next songs and audio.
 * @author Antonioni Andrea & Zanelli Gabriele
 */

public class MusicPlayer {
    private Library library;
    private AudioManage audioManage;
    private RunnableFlintstone checkPlaybackFred;
    private LinkedList<Song> nextSongs;
    private Song currentSong;
    private ObservableList currentPlaylist;
    private int currentSongNumber;
    private repeatType repeat;
    private boolean reproduceShuffle;
    
    private GUIController controller;
    
    public enum sortType{Title,Album,Artist};
    public enum repeatType{SingleSongRepeat,PlaylistRepeat,NoRepeat}
    
    
    public MusicPlayer(GUIController controller) {
        loadState();
       
        checkPlaybackFred = new RunnableFlintstone(this,audioManage, controller);
        
        this.controller = controller;
    }
    
    /**
     * Returns a pointer to the Library.
     * @return a pointer to the Library.
     */
    public Library getLibrary() {
        return library;
    }
    
    /**
     * Starts playing the selected song.
     */
    public void play() {
        audioManage.play();
    }   
    
    /**
     * Starts playing the selected song from the selected point.
     * @param time A Duration indicating the desired start time.
     */
    public void skipTo(Duration time) {
        audioManage.playFromIndex(time);
    }
    
    /**
     * Pauses the selected song.
     */
    public void pause() {
        audioManage.pause();
    }
    
    public Duration getActualSongDuration() {
        return currentSong.getDuration();
    }
    /**
     * Skip forward to the next song.
     */
    public void nextSong() {
        if(repeat==repeatType.SingleSongRepeat)
            audioManage.playFromIndex(new Duration(0)); // If repeat single song is activated, just replay.
        else {
            currentSong = nextSongs.pollLast();
            // Check if there is actually a song to play
            if(currentSong!=null) {
                // If the song was the last one, restard from top.
                playNewSong(currentSong,currentPlaylist,currentSongNumber+1%currentPlaylist.size());     
                
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
        if(!currentPlaylist.isEmpty()) {
            if(audioManage.getCurrentTime().greaterThan(new Duration(10000)) || currentSongNumber <= 0)
                audioManage.playFromIndex(new Duration(0)); 
            else {
                nextSongs.addLast(currentSong);
                currentSong = (Song)currentPlaylist.get(currentSongNumber-1);
                playNewSong(currentSong,currentPlaylist,currentSongNumber-1);
            }
            // Modificare la lista di canzoni successive
        }
    }
    
    /**
     * Change repeat preference.
     * @param value A Static Value from MusicPlayer class indicating the repeat preferince.
     */
    public void repeatPreference(repeatType value) {
        this.repeat=value;
    }
    
    public repeatType getRepeatPreference() {
        return repeat;
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
     * Retrieve the status of the player.
     * @return A MediaPlayer.Status.
     */
    public Status getPlayerStatus() {
        return audioManage.getCurrentStatus();
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
     * @param playlist A Pointer to the playlist that the user is sorting.
     * @param sortMethod The name of sorting method.
     */
    public void changePreferredSort(Playlist playlist,String sortMethod){
        if("Title".equals(sortMethod) || "Artist".equals(sortMethod) || "Album".equals(sortMethod))
            playlist.orderBy(sortMethod);
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
        this.currentSong = newSong;
        this.currentPlaylist=currentPlaylist;
        this.currentSongNumber=songNumber;
        String path = newSong.getPath();
        audioManage.newSong(path);  
        play();
        controller.setGraphics(newSong);
//      Da fare in thread
        generateSongQueue();
    }
    
    /**
     * Shut down the MusicPlayer.
     */
    public void shutDown() {
        saveState();
    }
    
    public void saveState() {
        ObjectOutputStream out = null;
            try {
                out = new ObjectOutputStream(new FileOutputStream("state.sp"));
                out.writeObject(library);
                out.writeObject(currentSong);
                out.writeObject(currentPlaylist);
                out.writeObject(currentSongNumber);
                out.writeObject(repeat);
                out.writeObject(reproduceShuffle);
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(MusicPlayer.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    out.close();
                } catch (IOException ex) {
                    Logger.getLogger(MusicPlayer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
    }
    
    private void loadState() {
        ObjectInputStream in = null;
            try {
                in = new ObjectInputStream(new FileInputStream("state.sp"));
                library = (Library)in.readObject();
                currentSong = (Song)in.readObject();
                currentPlaylist = (ObservableList)in.readObject();
                currentSongNumber = (int)in.readObject();
                repeat = (repeatType)in.readObject();
                reproduceShuffle = (boolean)in.readObject();
                in.close();
            } catch (FileNotFoundException ex) {
                library = new Library();
                audioManage = new AudioManage();
                nextSongs=new LinkedList<>();
                currentPlaylist = FXCollections.observableArrayList();
                currentSongNumber=0;
                repeat=repeatType.NoRepeat;
                reproduceShuffle=false;
            } catch (ClassNotFoundException | IOException ex) {
            Logger.getLogger(MusicPlayer.class.getName()).log(Level.SEVERE, null, ex);
//        } finally {
//                try {
//                    in.close();
//                } catch (IOException ex) {
//                    Logger.getLogger(MusicPlayer.class.getName()).log(Level.SEVERE, null, ex);
//                }
            }
    }
    
    /**
     * Generate the next ten songs for the queue.
     */
    private void generateSongQueue() {
        nextSongs.clear();
        // Add 10 times the actual song
        if(repeat==repeatType.SingleSongRepeat) {
            for(int i=0;i<10;i++)
                nextSongs.add(currentSong);
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
                if(currentSongNumber+i>=currentPlaylist.size())
                    if(repeat==repeatType.PlaylistRepeat)
                        nextSongs.add((Song)currentPlaylist.get(currentSongNumber+i%currentPlaylist.size()));
                    else
                        return;
                else
                    nextSongs.add((Song)currentPlaylist.get(currentSongNumber+i));
            }
        }
    }
}
