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
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
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
    
    @FXML
    private Label username;
    @FXML
    private ImageView previousButton;
    @FXML
    private ImageView playButton;
    @FXML
    private ImageView nextButton;
    @FXML
    private Label countUP;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label countDown;
    @FXML
    private ImageView shuffle;
    @FXML
    private ImageView replay;
    @FXML
    private Slider volumeControl;
    @FXML
    private HBox newPlaylistButton;
    @FXML
    private ImageView artwork;
    @FXML
    private Label artist;
    @FXML
    private Label titleSong;
    @FXML
    private ListView<ListItem> musicMenu, playlistsMenu;
    
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
