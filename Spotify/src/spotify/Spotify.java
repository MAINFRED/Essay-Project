
package spotify;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

/**
 * @author Antonioni Andrea & Zanelli Gabriele
 */

public class Spotify extends Application {
    private static MediaPlayer mediaPlayer;
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setTitle("Unknown");
        stage.setScene(scene);
        stage.show();
        
//        byte[] ciaone = AudioGesture.mp3toByteArray(new FileInputStream(new File("hello.mp3")));
//        AudioGesture.newSong(ciaone);
        AudioGesture AG = new AudioGesture();
//        AG.play();
        AG.cutFileAudio();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
