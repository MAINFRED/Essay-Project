package spotify;

import graphics.ListItem;
import graphics.ListItemRenderer;
import graphics.MusicMenu;
import graphics.PlaylistRenderer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Callback;
import utility.Utility;

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
    private ListView<ListItem> musicMenu;
    @FXML
    private ListView<Playlist> playlistsMenu;
    @FXML
    private MenuItem addSongItem, newPlaylistItem, exitItem, playItem,
            nextSongItem, previousSongItem, volumeUpItem, volumeDownItem;
    @FXML
    private CheckMenuItem shuffleItem, RepeatItem;
    @FXML
    private TableView<Song> allSongsTable;
    @FXML
    private TableColumn<Song, String> songColumn, artistColumn, albumColumn, durationColumn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initLeftSideBar();
        initSongsTable();
    }

    private void initLeftSideBar() {
        musicMenu.setItems(MusicMenu.createMusicMenu());

        //Says to musicMenu how to render elements
        musicMenu.setCellFactory(new Callback<ListView<ListItem>, ListCell<ListItem>>() {

            @Override
            public ListCell<ListItem> call(ListView<ListItem> param) {
                return new ListItemRenderer();
            }
        });

        playlistsMenu.setItems(musicPlayer.getLibrary().getPlaylistsPointer());

        //Says to playlistMenu how to render elements
        playlistsMenu.setCellFactory(new Callback<ListView<Playlist>, ListCell<Playlist>>() {

            @Override
            public ListCell<Playlist> call(ListView<Playlist> param) {
                return new PlaylistRenderer();
            }
        });

    }

    private void initSongsTable() {
        allSongsTable.setItems(musicPlayer.getLibrary().getTracksPointer());

        allSongsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        songColumn.setCellValueFactory(new PropertyValueFactory<Song, String>("title"));
        artistColumn.setCellValueFactory(new PropertyValueFactory<Song, String>("artist"));
        albumColumn.setCellValueFactory(new PropertyValueFactory<Song, String>("album"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<Song, String>("duration"));
    }

    @FXML
    private void handleAddSongItem(ActionEvent event) {
        Stage stage = new Stage();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Add a song");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("MP3 file", "*.mp3"));

        File src = fileChooser.showOpenDialog(stage);
        if (src != null) {
            File dest = new File(Library.LOCAL_PATH + src.getName());
            try {
                Utility.copyFile(src, dest);

                musicPlayer.getLibrary().addSong(dest);
            } catch (FileAlreadyExistsException ex) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Something went wrong :(");
                alert.setContentText("The file already exists in " + Library.LOCAL_PATH + src.getName());
                alert.showAndWait();
            } catch (FileNotFoundException ex) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Something went wrong :(");
                alert.setContentText("There's no file here:" + dest.getAbsolutePath());
                alert.showAndWait();
            } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    @FXML
    private void handleNewPlaylistItem(Event event) {
        handleNewPlaylistButton(event);
    }

    @FXML
    private void handleNewPlaylistButton(Event event) {
        Dialog dialog = new TextInputDialog("New playlist");
        dialog.setTitle("Add a new playlist");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            musicPlayer.getLibrary().addPlaylist(result.get());
        }
    }

}
