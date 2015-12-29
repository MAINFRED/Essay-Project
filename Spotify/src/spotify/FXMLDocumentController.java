package spotify;

import graphics.ListItem;
import graphics.ListItemRenderer;
import graphics.MusicMenu;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

/**
 * FXML Controller class of "FXMLDocument.fxml"
 * 
 * @author Antonioni Andrea & Zanelli Gabriele
 */

public class FXMLDocumentController implements Initializable {
    
    private MusicPlayer musicPlayer = new MusicPlayer();
    
    @FXML
    private Label username, countUP, countDown, artist, titleSong;
    @FXML
    private ImageView previousButton, playButton, nextButton, shuffle, replay,
            artwork;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Slider volumeControl;
    @FXML
    private HBox newPlaylistButton;
    @FXML
    private ListView<ListItem> musicMenu, playlistsMenu;
    @FXML
    private MenuItem addSongItem, newPlaylistItem, exitItem, playItem, 
            nextSongItem, previousSongItem, volumeUpItem, volumeDownItem;
    @FXML
    private CheckMenuItem shuffleItem, RepeatItem;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        initLeftSideBar();
        
    }    
    
    private void initLeftSideBar()
    {
        musicMenu.setItems(MusicMenu.createMusicMenu());
        
        //Says to musicMenu how to render elements
        musicMenu.setCellFactory(new Callback<ListView<ListItem>, ListCell<ListItem>>() {

            @Override
            public ListCell<ListItem> call(ListView<ListItem> param) {
                return new ListItemRenderer();
            }
        });
        
        
        //Says to playlistMenu how to render elements
        playlistsMenu.setCellFactory(new Callback<ListView<ListItem>, ListCell<ListItem>>() {

            @Override
            public ListCell<ListItem> call(ListView<ListItem> param) {
                return new ListItemRenderer();
            }
        });
        
        newPlaylistButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                
                Dialog dialog = new TextInputDialog("New playlist");
                dialog.setTitle("Add a new playlist");
                
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()) {
                    
                }
                

            }
        });
    }
}
