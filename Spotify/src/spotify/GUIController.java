 package spotify;

import graphics.ListItem;
import graphics.ListItemRenderer;
import graphics.MusicMenuListItems;
import graphics.MyTranscoder;
import graphics.PlaylistListItemRenderer;
import graphics.SongTable;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import utility.Utility;

/**
 * FXML Controller class of "FXMLDocument.fxml"
 *
 * @author Antonioni Andrea & Zanelli Gabriele
 */
public class GUIController implements Initializable {

    public static final String ICON_PATH = "resources/icon/";

    private MusicPlayer musicPlayer = new MusicPlayer();

    @FXML
    private Label username, countUP, artist, titleSong;
    @FXML
    private ImageView previousButton, playButton, nextButton, shuffle, replay,
            volumeDownIcon, volumeUpIcon, playlistIcon, searchIcon, artwork;
    @FXML
    private Slider volumeControl, sliderTime;
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
        initTables();
        initIcon();
        initPlayer();
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
                if (newValue == null) //bugfix
                    return;
                
                songPane.setVisible(false);
                artistsPane.setVisible(false);
                albumsPane.setVisible(false);
                playlistSongPane.setVisible(false);

                if (newValue == MusicMenuListItems.songItem) {
                    songPane.setVisible(true);
                } else if (newValue == MusicMenuListItems.artistsItem) {
                    artistsPane.setVisible(true);
                } else if (newValue == MusicMenuListItems.albumItem) {
                    albumsPane.setVisible(true);
                }
            }
        });

        musicListMenu.setOnMouseClicked(new EventHandler<MouseEvent>() {
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
                return new PlaylistListItemRenderer();
            }
        });

        //When user select an item of playlistsMenu
        playlistsMenu.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Playlist>() {
            @Override
            public void changed(ObservableValue<? extends Playlist> observable, Playlist oldValue, Playlist newValue) {

                if (newValue == null) //bugfix
                {
                    return;
                }

                songPane.setVisible(false);
                playlistSongPane.setVisible(true);

                titlePlaylist.setText(newValue.getTitle());
                playlistTable.setItems(FXCollections.observableList(newValue.getSongsPointer()));
            }
        });

        playlistsMenu.setOnMouseClicked(new EventHandler<MouseEvent>() {
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
    
    private void initTables() {
        songsTable = new SongTable(musicPlayer.getLibrary().getAllTracksPointer());
        songPane.setCenter(songsTable);
        songsTable.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent event) {
                showPopupMenuSongsTable(event);
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

        });
        songsTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //Double click --> play a song
                if (event.getClickCount() == 2) {
                    musicPlayer.playNewSong(songsTable.getSelectionModel().getSelectedItem(), 
                           musicPlayer.getLibrary().getAllTracksPointer() , musicPlayer.getLibrary().getAllTracksPointer().indexOf(songsTable.getSelectionModel().getSelectedItem()));
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
    
    private void initIcon() {
       previousButton.setImage(Utility.loadSVGIcon(ICON_PATH + "previous.svg"));
       playButton.setImage(Utility.loadSVGIcon(ICON_PATH + "play.svg"));
       nextButton.setImage(Utility.loadSVGIcon(ICON_PATH + "next.svg"));
       shuffle.setImage(Utility.loadSVGIcon(ICON_PATH + "shuffle.svg"));
       replay.setImage(Utility.loadSVGIcon(ICON_PATH + "repeatPlaylist.svg"));
       volumeDownIcon.setImage(Utility.loadSVGIcon(ICON_PATH + "volumeDown.svg"));
       volumeUpIcon.setImage(Utility.loadSVGIcon(ICON_PATH + "volumeUp.svg"));
       playlistIcon.setImage(Utility.loadSVGIcon(ICON_PATH + "playlist.svg"));
    }
    
    private void initPlayer() {
        volumeControl.valueProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
                musicPlayer.changeVolume((int) volumeControl.getValue());
            }
        });
        
        sliderTime.valueProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
                musicPlayer.skipTo(new Duration(sliderTime.getValue()));
            }
        });
    }
    
    @FXML
    private void handleAddSongItem(ActionEvent event) {
        Stage stage = new Stage();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Add a song");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("MP3 file", "*.mp3"));

        List<File> src = fileChooser.showOpenMultipleDialog(stage);
        if (src != null) {
            for (File file : src) {
                File dest = new File(Library.LOCAL_PATH + file.getName());
                try {
                    Utility.copyFile(file, dest);

                    musicPlayer.getLibrary().addSong(dest);
                } catch (FileAlreadyExistsException ex) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Dialog");
                    alert.setHeaderText("Something went wrong :(");
                    alert.setContentText("The file already exists in " + Library.LOCAL_PATH + file.getName());
                    alert.showAndWait();
                } catch (FileNotFoundException ex) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Dialog");
                    alert.setHeaderText("Something went wrong :(");
                    alert.setContentText("There's no file here:" + dest.getAbsolutePath());
                    alert.showAndWait();
                } catch (IOException ex) {
                    Logger.getLogger(GUIController.class.getName()).log(Level.SEVERE, null, ex);
                }
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

    private void loadIcon(String pathIcon, ImageView imageView) {

        MyTranscoder imageTranscoder = new MyTranscoder();

        TranscoderInput transIn = new TranscoderInput(pathIcon);
        try {
            imageTranscoder.transcode(transIn, null);
        } catch (TranscoderException ex) {
            Logger.getLogger(GUIController.class.getName()).log(Level.SEVERE, null, ex);
        }

        Image img = SwingFXUtils.toFXImage(imageTranscoder.getImage(), null);
        imageView.setImage(img);
    }

    @FXML
    private void handlePreviousSong(MouseEvent event) {
        musicPlayer.previusSong();
    }

    @FXML
    private void handlePlayPause(MouseEvent event) {
        
    }

    @FXML
    private void handleNextSong(MouseEvent event) {
        musicPlayer.nextSong();
    }

    @FXML
    private void handleShuffled(MouseEvent event) {
        musicPlayer.shuffle(!musicPlayer.getShuffle());
        System.out.println("Shuffle: " + musicPlayer.getShuffle());
    }

    @FXML
    private void handleReplay(MouseEvent event) {
        if(musicPlayer.getRepeatPreference()==MusicPlayer.repeatType.NoRepeat)
            musicPlayer.repeatPreference(MusicPlayer.repeatType.PlaylistRepeat);
        else if (musicPlayer.getRepeatPreference()==MusicPlayer.repeatType.PlaylistRepeat)
            musicPlayer.repeatPreference(MusicPlayer.repeatType.SingleSongRepeat);
        else if (musicPlayer.getRepeatPreference()==MusicPlayer.repeatType.SingleSongRepeat)
            musicPlayer.repeatPreference(MusicPlayer.repeatType.NoRepeat);
    }

    public void setGraphics(Song song) {
        titleSong.setText(song.getTitle());
        artist.setText(song.getArtist()); 
       
        try {
            BufferedImage bufferedImage = song.getArtwork();
            if (bufferedImage != null) {
                BufferedImage resizedImage = Utility.resize(bufferedImage, (int) artwork.getFitWidth(), (int) artwork.getFitWidth());
                Image image = SwingFXUtils.toFXImage(resizedImage, null);
                artwork.setImage(image);
            }
        } catch (IOException ex) {
            Logger.getLogger(GUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        sliderTime.setValue(0);
        countUP.setText("00:00");
    }
    
    public void refreshPlayer(Duration currentTime)
    {
        
    }

}
