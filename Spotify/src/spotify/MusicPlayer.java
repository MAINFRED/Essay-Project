
package spotify;

import java.util.LinkedList;

/**
 * @author Antonioni Andrea & Zanelli Gabriele
 */

/**
* MusicPlayer is the interface between user and application which convert user's input and manage
* the songs' library, played song, next songs and audio.
*/
public class MusicPlayer {
    Library library;
    AudioManage audioManage;
    private LinkedList<String> listenedSongs; 
    private LinkedList<String> nextSongs;
    private String actualSong;
    private String preferredSort;
    private boolean repeatSong;
    private boolean repeatPlaylist;
    private boolean reproduceShuffle;
    
    /** Initialize
    * listenedSongs: Stack of listened songs;
    * nextSong: Queue of next songs;
    * prefferedSort: Type of sorting method between Title, Artist, Date;
    * repeatSong: Indicate activation of single song repeat;
    * repeatPlaylist: Indicate activation of playlist repeat once terminated;
    * reproduceShuffle: Indicate activation of random song playing.
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
    
    public void play() {
        audioManage.play();
    }
    
    public void pause() {
        audioManage.pause();
    }
    
    public void nextSong() {
        String next = nextSongs.pollLast();
        if(next!=null) {
            playNewSong(next);
            listenedSongs.push(actualSong);
            actualSong=next;
        }
    }
    
    /**
    * If there are no previus songs or the song has started for more than 10 seconds, reproduce the same song from 
    * the beginning else reproduce the previus song.
    */
    public void previusSong() { 
        if(/*Secondi Riprodotti >10 or*/listenedSongs.isEmpty())
            audioManage.playIndex(0); 
        else {
            String previus = listenedSongs.pop();
            playNewSong(previus);
            nextSongs.addLast(actualSong);
            actualSong = previus;
        }
    }
    
    public void repeatSong(boolean value) {
        this.repeatSong=value;
    }
    
    public void repeatPlaylist(boolean value) {
        this.repeatPlaylist=value;
    }
    
    public void reproduceShuffle(boolean value) {
        this.reproduceShuffle=value;
    }
    
    public void changePreferredSort(String value){
        this.preferredSort=value;
    }
    
    /**
     * Gets the name of the song and retrieve the byteArray/mp3 from the library in order to play it 
     * @param newSong Name/Key of the song.
     */
    private void playNewSong(String name) {
        Song newSong = library.getSongByName(name);
        byte[] byteSong = new byte[10]; 
        audioManage.newSong(byteSong);  
        audioManage.play();
    }
}
