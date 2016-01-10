
package spotify;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Duration;

/**
 * @author Antonioni Andrea & Zanelli Gabriele
 */

public class RunnableFlintstone extends Thread {
    MusicPlayer musicPlayer;
    AudioManage audioManage;
    
    public RunnableFlintstone(MusicPlayer musicPlayerPointer,AudioManage audioManagePointer) {
        musicPlayer=musicPlayerPointer;
        audioManage=audioManagePointer;
    }
   
    @Override
    public void run(){
        try {
        while(audioManage.getTimeLeft().equals(new Duration(0)))
//            GUIController.refreshPlayer(audioManage.getCurrentTime());
            wait(500);
        
        musicPlayer.nextSong();
        } catch (InterruptedException ex) {
            Logger.getLogger(RunnableFlintstone.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
