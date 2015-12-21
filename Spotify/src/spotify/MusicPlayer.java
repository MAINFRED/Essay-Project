package spotify;

import java.util.LinkedList;

/**
 * @author Zanelli Gabriele
 */

public class MusicPlayer {
    private LinkedList previusList; // Pila push() e pop()
    private LinkedList nextList;
    
    public MusicPlayer() {
        previusList=new LinkedList();
        nextList=new LinkedList();
    }
            
}
