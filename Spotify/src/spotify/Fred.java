
package spotify;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Duration;

/**
 * @author Antonioni Andrea & Zanelli Gabriele
 */

public class Fred extends Thread {
    MusicPlayer musicPlayer;
    AudioManage audioManage;
    
    public Fred(MusicPlayer musicPlayerPointer,AudioManage audioManagePointer) {
        musicPlayer=musicPlayerPointer;
        audioManage=audioManagePointer;
    }
   
    @Override
    public void run(){
        try {
        while(audioManage.getTimeLeft().equals(new Duration(0)))
            wait(500);
        
        musicPlayer.nextSong();
        } catch (InterruptedException ex) {
            Logger.getLogger(Fred.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
