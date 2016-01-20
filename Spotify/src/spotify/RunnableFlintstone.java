
package spotify;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Duration;

/**
 * @author Antonioni Andrea & Zanelli Gabriele
 */

public class RunnableFlintstone extends Thread {
    private MusicPlayer musicPlayer;
    private AudioManage audioManage;
    private GUIController controller;
    
    public RunnableFlintstone(MusicPlayer musicPlayerPointer,AudioManage audioManagePointer, GUIController controller) {
        musicPlayer=musicPlayerPointer;
        audioManage=audioManagePointer;
        this.controller = controller;
        start();
    }
    
    @Override
    public void run(){
        try {
            System.out.println("Thread avviato");
            while(true) {
                while(audioManage.getTimeLeft()!=null)
                {
                    if(audioManage.getTimeLeft().equals(new Duration(0))) {
                        System.out.println("La canzone è terminata");
                        musicPlayer.nextSong();
                    }
                    else
                        controller.refreshPlayer(audioManage.getCurrentTime());
                    sleep(1500);
                }
                sleep(1500);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(RunnableFlintstone.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
