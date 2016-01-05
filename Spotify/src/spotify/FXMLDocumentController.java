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
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
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
    private Label newPlaylistButton;
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
    private ContextMenu popupMenuPlaylist;
    private ContextMenu popupMenuSongsTable, popupMenuPlaylistTable;
    private Menu menuItemPlaylist;
    @FXML
    private BorderPane songPane;
    @FXML
    private BorderPane playlistSongPane;
    @FXML
    private Label titlePlaylist;

    private SongTable songsTable, playlistTable;
    @FXML
    private TilePane artistsPane, albumsPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initSideBar();
        initPopupMenu();

        songsTable = new SongTable(musicPlayer.getLibrary().getTracksPointer());
        songPane.setCenter(songsTable);
        songsTable.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent event) {
                showPopupMenuSongsTable(event);
            }

        });
        songsTable.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                //Double click --> play a song
                if(event.getClickCount() == 2)
                {
                    playSong(songsTable.getSelectionModel().getSelectedItem());
                }
            }
            
        });

        playlistTable = new SongTable();
        playlistSongPane.setCenter(playlistTable);
        playlistTable.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent event) {
                showPopupMenuPlaylistTable(event);
            }

            private void showPopupMenuPlaylistTable(ContextMenuEvent event) {
                popupMenuPlaylistTable.show(playlistTable, event.getScreenX(), event.getScreenY());
            }

        });
    }

    private void initSideBar() {
        musicListMenu.setItems(MusicMenuListItems.get());

        //Says to musicListMenu how to render elements
        musicListMenu.setCellFactory(new Callback<ListView<ListItem>, ListCell<ListItem>>() {
            @Override
            public ListCell<ListItem> call(ListView<ListItem> param) {
                return new ListItemRenderer();
            }
        });

        //When user select an item of musicListMenu
        musicListMenu.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ListItem>() {
            @Override
            public void changed(ObservableValue<? extends ListItem> observable, ListItem oldValue, ListItem newValue) {
                if(newValue == null)    //bugfix
                    return;
                
                songPane.setVisible(false);
                artistsPane.setVisible(false);
                albumsPane.setVisible(false);
                playlistSongPane.setVisible(false);
                    
                if (newValue == MusicMenuListItems.songItem) {
                    songPane.setVisible(true);
                }
                else if(newValue == MusicMenuListItems.artistsItem) {
                    artistsPane.setVisible(true);
                }
                else if(newValue == MusicMenuListItems.albumItem) {
                    albumsPane.setVisible(true);
                }
            }
        });
        
        musicListMenu.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                playlistsMenu.getSelectionModel().select(null);
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

        //When user select an item of playlistsMenu
        playlistsMenu.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Playlist>() {
            @Override
            public void changed(ObservableValue<? extends Playlist> observable, Playlist oldValue, Playlist newValue) {
                
                if(newValue == null)    //bugfix
                    return;
                
                songPane.setVisible(false);
                playlistSongPane.setVisible(true);

                titlePlaylist.setText(newValue.getTitle());
                playlistTable.setItems(FXCollections.observableList(newValue.getSongsPointer()));

            }
        });
        
        playlistsMenu.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                musicListMenu.getSelectionModel().select(null);
            }
            
        });
    }

    private void initPopupMenu() {
        popupMenuSongsTable = new ContextMenu();
        menuItemPlaylist = new Menu("Add to playlist");
        popupMenuSongsTable.getItems().add(menuItemPlaylist);

        popupMenuPlaylistTable = new ContextMenu();

        MenuItem removeItem = new MenuItem("Remove");
        removeItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("");
                alert.setHeaderText("Do you want to remove the song from this playlist?");
                alert.setContentText("");
                ButtonType buttonYes = new ButtonType("Yes");
                ButtonType buttonNo = new ButtonType("No");
                alert.getButtonTypes().setAll(buttonYes, buttonNo);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == buttonYes) {
                    musicPlayer.getLibrary().removeSongFromPlaylist(playlistTable.getSelectionModel().getSelectedItem(),
                            musicPlayer.getLibrary().getPlaylist(titlePlaylist.getText()));
                }
            }

        });
        popupMenuPlaylistTable.getItems().add(removeItem);

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

    private void showPopupMenuSongsTable(ContextMenuEvent event) {
        //Removes the old menu
        popupMenuSongsTable.getItems().remove(menuItemPlaylist);

        //Creates the new menu
        menuItemPlaylist = new Menu("Add to playlist");
        for (Playlist playlist : (ObservableList<Playlist>) musicPlayer.getLibrary().getPlaylistsPointer()) {
            MenuItem item = new MenuItem(playlist.getTitle());
            item.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    Song songSelected = songsTable.getSelectionModel().getSelectedItem();
                    playlist.addSong(songSelected);
                }
            });
            menuItemPlaylist.getItems().add(item);
        }

        //Adds the new menu
        popupMenuSongsTable.getItems().add(menuItemPlaylist);
        popupMenuSongsTable.show(songsTable, event.getScreenX(), event.getScreenY());

    }

    @FXML
    private void handleRenamePlaylist(ActionEvent event) {
        Dialog dialog = new TextInputDialog("New playlist");
        dialog.setHeaderText("Rename the selected playlist");
        dialog.setTitle("");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            Playlist renamed = playlistsMenu.getSelectionModel().getSelectedItem();
            musicPlayer.getLibrary().renamePlaylist(renamed, result.get());
            refreshPlaylistsMenu();
            titlePlaylist.setText(renamed.getTitle());
            playlistsMenu.getSelectionModel().select(renamed);
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
        if (result.get() == buttonDelete) {
            musicPlayer.getLibrary().removePlaylist(playlistsMenu.getSelectionModel().getSelectedItem());
            playlistSongPane.setVisible(false);
        }
    }

    private void refreshPlaylistsMenu() {
        playlistsMenu.setItems(null);
        playlistsMenu.setItems(musicPlayer.getLibrary().getPlaylistsPointer());
    }
    
    private void playSong(Song song)
    {
        musicPlayer.playNewSong(song);
    }

}
