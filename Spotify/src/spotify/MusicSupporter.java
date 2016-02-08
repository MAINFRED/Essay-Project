package spotify;

import graphics.GUIController;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;
import utility.Utility;

/**
 * MusicSupporter is the interface between user and application which convert
 * user's input and manage the library's song, played song, next songs and
 * audio.
 *
 * @author Antonioni Andrea & Zanelli Gabriele
 */
public class MusicSupporter {

    private Library library;
    private AudioManage audioManage;
    private LinkedList<Song> nextSongs;
    private Song currentSong;
    private Playlist currentPlaylist;
    private int currentSongNumber;
    private replayType replay;
    private boolean reproduceShuffle;

    private GUIController controller;

    private static final String STATE_FILE = "state.sp";

    public enum sortType {
        Title, Album, Artist
    };

    public enum replayType {
        SingleSongReplay, PlaylistReplay, NoReplay
    }

    // MARK: Contructor
    /**
     * Creates a new MusicSupporter object
     *
     * @param controller A GUIController object
     */
    public MusicSupporter(GUIController controller) {
        this.controller = controller;
        loadState();
    }

    // MARK: Player methods
    /**
     * Starts playing a new song.
     *
     * @param newSong A Song object which represents the new song to play.
     * @param playlistNumber An int value indicating the playlist that contains
     * the song, negative for All Tracks, positive for the playlist number.
     */
    public void playNewSong(Song newSong, int playlistNumber) {
        // Funzione searchSong che cerca la canzone in locale ed eventualmente la richiede al server 
        // E SETTA IL PATH LOCALE, Settare la variabile locale a true
        this.currentSong = newSong;
        this.currentPlaylist = library.retrievePlaylist(playlistNumber);
        this.currentSongNumber = currentPlaylist.indexOfSong(newSong);
        audioManage.newSong(newSong.getPath());
        play();
        controller.setPlayerGraphics(newSong);
//      Da fare in thread
        generateSongQueue();
    }

    private void playNewSong(Song newSong, Playlist currentPlaylist, int songNumber) {
        // Equivalente della sopra, da utilizzare privatamente con riferimento ad una playlist
        this.currentSong = newSong;
        this.currentPlaylist = currentPlaylist; // Non so manco se serva
        this.currentSongNumber = songNumber;
        String path = newSong.getPath();
        audioManage.newSong(path);
        play();
        controller.setPlayerGraphics(newSong);
//      Da fare in thread
        generateSongQueue();
    }

    /**
     * Starts playing the selected song.
     */
    public void play() {
        audioManage.play();
    }

    /**
     * Pauses the selected song.
     */
    public void pause() {
        audioManage.pause();
    }

    /**
     * Skips forward to the next song.
     */
    public void nextSong() {
        if (replay == replayType.SingleSongReplay) {
            audioManage.playFromIndex(new Duration(0)); // If replay single song is activated, just replay.
        } else {
            currentSong = nextSongs.pollLast();
            // Check if there is actually a song to play
            if (currentSong != null) {
                // If the song was the last one, restard from top.
                playNewSong(currentSong, currentPlaylist, currentSongNumber + 1 % currentPlaylist.size());

                // Aggiungere la nuova canzone alla fine del vettore
            }
        }
    }

    /**
     * Skips back to the previus song. If there are no previus songs or the song
     * has started for more than 10 seconds, reproduce the same + song from the
     * beginning else reproduce the previus song.
     */
    public void previusSong() {
        if (!currentPlaylist.isEmpty()) {
            if (audioManage.getCurrentTime().greaterThan(new Duration(10000)) || currentSongNumber <= 0) {
                audioManage.playFromIndex(new Duration(0));
            } else {
                nextSongs.addLast(currentSong);
                currentSong = (Song) currentPlaylist.getSong(currentSongNumber - 1);
                playNewSong(currentSong, currentPlaylist, currentSongNumber - 1);
            }
            // Modificare la lista di canzoni successive
        }
    }

    /**
     * Starts playing the selected song from the selected point.
     *
     * @param time A Duration indicating the desired start time.
     */
    public void skipTo(Duration time) {
        audioManage.playFromIndex(time);
    }

    /**
     * Changes replay preference.
     *
     * @param value A static value from MusicSupporter class indicating the
     * replay preference.
     */
    public void setReplayPreference(replayType value) {
        this.replay = value;
    }

    /**
     * Returns the replay precerence selected.
     *
     * @return A static value from MusicSupporter class indicating the replay
     * preference.
     */
    public replayType getReplayPreference() {
        return replay;
    }

    /**
     * Changes reproduce setShuffle preference.
     *
     * @param value A Boolean indicating activation of random song playing.
     */
    public void setShuffle(boolean value) {
        this.reproduceShuffle = value;
        generateSongQueue();
    }

    /**
     * Retrieve the setShuffle value.
     *
     * @return A Boolean indicating if random song playing is activated.
     */
    public boolean getShuffle() {
        return reproduceShuffle;
    }

    /**
     * Retrieve the status of the player.
     *
     * @return A MediaPlayer.Status.
     */
    public Status getPlayerStatus() {
        return audioManage.getCurrentStatus();
    }

    /**
     * Change the value of volume.
     *
     * @param volume The value of volume.
     */
    public void changeVolume(int volume) {
        if (volume >= 0 & volume <= 100) {
            audioManage.changeVolume(volume);
        } else {
            System.out.println("Invalid volume range");
        }
    }

    // MARK: Attributes methods
    /**
     * Returns a reference of the tracks list which is unmodifiable.
     *
     * @return a reference of the tracks list which is unmodifiable.
     */
    public ObservableList getAllTracks() {
        return library.getAllTracks();
    }

    /**
     * Returns a reference of the playlists list which is unmodifiable.
     *
     * @return a reference of the playlists list which is unmodifiable.
     */
    public ObservableList getPlaylistsPointer() {
        return library.getPlaylistsPointer();
    }

    // MARK: Saving methods
    
    /**
     * Saves the current state of the player in a file.
     */
    public void saveState() {
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(new FileOutputStream(STATE_FILE));
            library.writeObject(out);
            out.writeObject(currentSong);
            currentPlaylist.writeObject(out);
            out.writeObject(currentSongNumber);
            out.writeObject(replay);
            out.writeObject(reproduceShuffle);
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(MusicSupporter.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(MusicSupporter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Resume the last state con the player from a file.
     */
    private void loadState() {
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream("state.sp"));
            library = Library.getInstance(in);
            currentSong = (Song) in.readObject();
            currentPlaylist = new Playlist(in);
            currentSongNumber = (int) in.readObject();
            replay = (replayType) in.readObject();
            reproduceShuffle = (boolean) in.readObject();
            audioManage = new AudioManage(controller.getSlideTimeManager());
            nextSongs = new LinkedList<>();
            in.close();

            checkLocalSongs(true);
        } catch (FileNotFoundException ex) {
            library = Library.getInstance();
            audioManage = new AudioManage(controller.getSlideTimeManager());
            nextSongs = new LinkedList<>();
            currentPlaylist = library.retrievePlaylist(-1);
            currentSongNumber = 0;
            replay = replayType.NoReplay;
            reproduceShuffle = false;

            checkLocalSongs(false);
        } catch (ClassNotFoundException | IOException ex) {
            Logger.getLogger(MusicSupporter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void checkLocalSongs(boolean loaded) {
        if (loaded) {
            for (int i = 0; i < library.getAllTracks().size(); i++) {
                if (!(new File(library.getAllTracks().get(i).getPath()).isFile())) {
                    library.removeSong(library.getAllTracks().get(i));
                }
            }
        } else {
            File folder = new File(Library.LOCAL_PATH);
            File[] listOfFiles = folder.listFiles();
            
            for (File file : listOfFiles) {
                File dest = new File(Library.LOCAL_PATH + file.getName());
                if (file.isFile() && !file.isHidden()) {
                    try {
                        Utility.copyFile(file, dest);
                        addSong(dest);
                    } catch (IOException ex) {
                        Logger.getLogger(MusicSupporter.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    // MARK: Library methods
    
    public void addSong(File file) throws FileNotFoundException {

        library.addSong(file);

    }

    public void addPlaylist(String name) {
        library.addPlaylist(name);
    }

    public void removePlaylist(Playlist playlist) {
        library.removePlaylist(playlist);
    }

    public void removeSongFromPlaylist(Song song, Playlist playlist) {
        library.removeSongFromPlaylist(song, playlist);
    }
    
    public void renamePlaylist(Playlist playlist, String newName) {
        library.renamePlaylist(playlist, newName);
    }

    /**
     * Change the preferred sorting method between Title, Arist or Album.
     *
     * @param playlistNumber An int indicating the playlist, negative for All
     * Tracks, positive for the playlist number.
     * @param sortMethod The name of sorting method.
     */
    public void changePreferredSort(int playlistNumber, sortType sortMethod) {
        library.orderPlaylistBy(playlistNumber, sortMethod);
    }
    
    // MARK: Utilities methods
    
    public Duration getActualSongDuration() {
        return currentSong.getDuration();
    }
    
    /**
     * Generate the next ten songs for the queue.
     */
    private void generateSongQueue() {
        nextSongs.clear();
        // Add 10 times the actual song
        if (replay == replayType.SingleSongReplay) {
            for (int i = 0; i < 10; i++) {
                nextSongs.add(currentSong);
            }
        } // Add 10 random songs.
        else if (reproduceShuffle) {
            for (int i = 0; i < 10; i++) {
                nextSongs.add(currentPlaylist.getSong(new Random().nextInt(currentPlaylist.size())));
            }
        } // Add the next 10 songs 
        else {
            for (int i = 1; i <= 10; i++) {
                // If it reaches the end and replay playlist is on, goes to the top.
                if (currentSongNumber + i >= currentPlaylist.size()) {
                    if (replay == replayType.PlaylistReplay) {
                        nextSongs.add(currentPlaylist.getSong(currentSongNumber + i % currentPlaylist.size()));
                    } else {
                        return;
                    }
                } else {
                    nextSongs.add(currentPlaylist.getSong(currentSongNumber + i));
                }
            }
        }
    }

}
