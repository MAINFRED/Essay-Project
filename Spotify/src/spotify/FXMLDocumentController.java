
package spotify;

import graphics.ListItem;
import graphics.ListItemRenderer;
import graphics.MusicMenuListItems;
import graphics.PlaylistRenderer;
import graphics.SongTable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    private ListView<ListItem> musicListMenu;
    @FXML
    private ListView<Playlist> playlistsMenu;
    @FXML
    private MenuItem addSongItem, newPlaylistItem, exitItem, playItem,
            nextSongItem, previousSongItem, volumeUpItem, volumeDownItem,
            renamePlaylistItem, deletePlaylistItem;
    @FXML
    private CheckMenuItem shuffleItem, RepeatItem;
    @FXML
    private AnchorPane mainPanel;
    @FXML
    private ContextMenu popupMenuPlaylist;
    private ContextMenu popupMenuSongs;
    private Menu menuItemPlaylist;
    @FXML
    private VBox allSongPane;
    @FXML
    private VBox playlistSongPane;
    @FXML
    private Label titlePlaylist;
    
    private SongTable allSongTable, playlistTable;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initLeftSideBar();
        initPopupMenu();
        
        allSongTable = new SongTable(musicPlayer.getLibrary().getTracksPointer());
        allSongPane.getChildren().add(allSongTable);
        allSongTable.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>(){
            @Override
            public void handle(ContextMenuEvent event) {
                showPopupMenuSongs(event);
            }
            
        });
        
        playlistTable = new SongTable();
        playlistSongPane.getChildren().add(playlistTable);
    }

    private void initLeftSideBar() {
        musicListMenu.setItems(MusicMenuListItems.get());

        //Says to musicListMenu how to render elements
        musicListMenu.setCellFactory(new Callback<ListView<ListItem>, ListCell<ListItem>>() {

            @Override
            public ListCell<ListItem> call(ListView<ListItem> param) {
                return new ListItemRenderer();
            }
        });
        
        musicListMenu.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ListItem>() {
            @Override
            public void changed(ObservableValue<? extends ListItem> observable, ListItem oldValue, ListItem newValue) {
                if(newValue == MusicMenuListItems.songItem)
                {
                    allSongPane.setVisible(true);
                    playlistSongPane.setVisible(false);
                }
                
                
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
        
        playlistsMenu.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Playlist>() {
            @Override
            public void changed(ObservableValue<? extends Playlist> observable, Playlist oldValue, Playlist newValue) {
                
                allSongPane.setVisible(false);
                playlistSongPane.setVisible(true);
                
                titlePlaylist.setText(newValue.getTitle());
                playlistTable.setItems(FXCollections.observableList(newValue.getSongsPointer()));
                
            }
        });

    }

    private void initPopupMenu()
    {
        popupMenuSongs = new ContextMenu();
        menuItemPlaylist = new Menu("Add to playlist");
        popupMenuSongs.getItems().add(menuItemPlaylist);
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
        Dialog dialog = new TextInputDialog("");
        dialog.setHeaderText("Add a new playlist");
        dialog.setTitle("");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            musicPlayer.getLibrary().addPlaylist(result.get());
        }
    }

    @FXML
    private void showPopupMenuPlaylist(ContextMenuEvent event) {
        popupMenuPlaylist.show(playlistsMenu, event.getScreenX(), event.getScreenY());
    }
    
    private void showPopupMenuSongs(ContextMenuEvent event) {
        
        menuItemPlaylist = new Menu("Add to playlist");
        for(Playlist playlist : (ObservableList<Playlist>)musicPlayer.getLibrary().getPlaylistsPointer())
        {
            MenuItem item = new MenuItem(playlist.getTitle());
            item.setOnAction(new EventHandler<ActionEvent>() {
                @Override 
                public void handle(ActionEvent e) {
                    Song songSelected = allSongTable.getSelectionModel().getSelectedItem();
                    playlist.addSong(songSelected);
                }
            });
            menuItemPlaylist.getItems().add(item);
        }
        
        popupMenuSongs.show(allSongTable, event.getScreenX(), event.getScreenY());
            
    }
    
    @FXML
    private void handleRenamePlaylist(ActionEvent event) {
        Dialog dialog = new TextInputDialog("New playlist");
        dialog.setHeaderText("Rename the selected playlist");
        dialog.setTitle("");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            musicPlayer.getLibrary().renamePlaylist(playlistsMenu.getSelectionModel().getSelectedItem(), 
                    result.get());
            refreshPlaylistsMenu();
        }
    }
    
    @FXML
    private void handleDeletePlaylist(ActionEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("");
        alert.setHeaderText("Do you want to delete the selected playlist?");
        alert.setContentText("");
        ButtonType buttonDelete = new ButtonType("Yes");
        ButtonType buttonNoDelete = new ButtonType("No");
        alert.getButtonTypes().setAll(buttonDelete, buttonNoDelete);
        
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == buttonDelete)
            musicPlayer.getLibrary().removePlaylist(playlistsMenu.getSelectionModel().getSelectedItem());
    }
    
    private void refreshPlaylistsMenu() {
        playlistsMenu.setItems(null);
        playlistsMenu.setItems(musicPlayer.getLibrary().getPlaylistsPointer());
    }

    

}
