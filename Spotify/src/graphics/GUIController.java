package graphics;

import graphics.menu.ListItem;
import graphics.menu.ListItemRenderer;
import graphics.menu.MainMenu;
import graphics.menu.PlaylistListItemRenderer;
import graphics.svg.SVGImage;
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
import javafx.application.Platform;
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
import javafx.scene.control.Slider;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.Duration;
import spotify.Library;
import spotify.MusicPlayer;
import spotify.Playlist;
import spotify.Song;
import utility.Utility;

/**
 * FXML Controller class of "FXMLDocument.fxml"
 *
 * @author Antonioni Andrea & Zanelli Gabriele
 */
public class GUIController implements Initializable {

    public static final String ICON_PATH = "resources/icon/";

    private MusicPlayer musicPlayer = new MusicPlayer(this);
    private Library library = Library.getInstance();

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
    private ListView<ListItem> mainMenu;
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

    //MARK: init zone
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initSideBar();
        initPopupMenu();
        initTables();
        initIcon();
        initPlayer();
        initMenuBar();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ((Stage) songPane.getScene().getWindow()).setOnCloseRequest(new EventHandler<WindowEvent>() {
                    public void handle(WindowEvent we) {
                        musicPlayer.saveState();
                        System.exit(0);
                    }
                });
            }
        });
    }

    private void initSideBar() {
        initMainMenu();
        initPlaylistsMenu();
    }

    private void initMainMenu() {
        //Set items of mainMenu
        mainMenu.setItems(MainMenu.get());

        //Says to mainMenu how to render elements
        mainMenu.setCellFactory(new Callback<ListView<ListItem>, ListCell<ListItem>>() {
            @Override
            public ListCell<ListItem> call(ListView<ListItem> param) {
                return new ListItemRenderer();
            }
        });

        //When user select an item of mainMenu
        mainMenu.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ListItem>() {
            @Override
            public void changed(ObservableValue<? extends ListItem> observable, ListItem oldValue, ListItem newValue) {
                if (newValue == null) //bugfix
                {
                    return;
                }

                songPane.setVisible(false);
                artistsPane.setVisible(false);
                albumsPane.setVisible(false);
                playlistSongPane.setVisible(false);

                if (newValue == MainMenu.songItem) {
                    songPane.setVisible(true);
                } else if (newValue == MainMenu.artistsItem) {
                    artistsPane.setVisible(true);
                } else if (newValue == MainMenu.albumItem) {
                    albumsPane.setVisible(true);
                }
            }
        });

        //Deselect item in others menus
        mainMenu.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                playlistsMenu.getSelectionModel().select(null);
            }
        });
    }

    private void initPlaylistsMenu() {
        //Set items of playlistsMenu
        playlistsMenu.setItems(library.getPlaylistsPointer());

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

        //Deselect item in others menus
        playlistsMenu.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mainMenu.getSelectionModel().select(null);
            }
        });

        //Added contextMenu
        playlistsMenu.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent event) {
                popupMenuPlaylist.show(playlistsMenu, event.getScreenX(), event.getScreenY());
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
                    library.removeSongFromPlaylist(playlistTable.getSelectionModel().getSelectedItem(),
                            library.getPlaylist(titlePlaylist.getText()));
                }
            }

        });
        popupMenuPlaylistTable.getItems().add(removeItem);
    }

    private void initTables() {
        songsTable = new SongTable(library.getAllTracks());
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
                for (Playlist playlist : (ObservableList<Playlist>) library.getPlaylistsPointer()) {
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
                            Library.ALL_TRACKS_NUMBER, library.getAllTracks().indexOf(songsTable.getSelectionModel().getSelectedItem()));
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
        previousButton.setImage(SVGImage.loadIcon(ICON_PATH + "previous.svg"));
        playButton.setImage(SVGImage.loadIcon(ICON_PATH + "play.svg"));
        nextButton.setImage(SVGImage.loadIcon(ICON_PATH + "next.svg"));
        shuffle.setImage(SVGImage.loadIcon(ICON_PATH + "shuffle.svg"));
        replay.setImage(SVGImage.loadIcon(ICON_PATH + "repeatPlaylist.svg"));
        volumeDownIcon.setImage(SVGImage.loadIcon(ICON_PATH + "volumeDown.svg"));
        volumeUpIcon.setImage(SVGImage.loadIcon(ICON_PATH + "volumeUp.svg"));
        playlistIcon.setImage(SVGImage.loadIcon(ICON_PATH + "playlist.svg"));
    }

    private void initPlayer() {

        previousButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                previousSongButton();
            }

        });

        nextButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                nextSongButton();
            }

        });

        playButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                playPauseButton();
            }

        });

        shuffle.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                shuffledButton();
            }

        });

        replay.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                replayButton();
            }

        });

        volumeControl.valueProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
                musicPlayer.changeVolume((int) volumeControl.getValue());
            }
        });

        sliderTime.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                musicPlayer.skipTo(new Duration(sliderTime.getValue() * musicPlayer.getActualSongDuration().toMillis() / sliderTime.getMax()));
            }

        });

        sliderTime.setOnDragDetected(new EventHandler() {
            @Override
            public void handle(Event event) {
                System.out.println("Drag detected");
                playPauseButton();
            }
        });

        sliderTime.setOnDragDropped(new EventHandler() {
            @Override
            public void handle(Event event) {
                System.out.println("Drop detected");
                playPauseButton();
            }
        });
    }

    private void initMenuBar() {

        //FILE
        addSongItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
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

                            library.addSong(dest);
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

        });

        newPlaylistItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handleNewPlaylistButton(event);
            }

        });

        exitItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                musicPlayer.saveState();
                System.exit(0);
            }

        });

        //PLAYBACK
        playItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handlePlayPause(event);
            }

        });

        nextSongItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handleNextSong(event);
            }

        });

        previousSongItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handlePreviousSong(event);
            }

        });

        volumeUpItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                volumeControl.setValue(volumeControl.getValue() + 10);
                musicPlayer.changeVolume((int) volumeControl.getValue());
            }

        });

        volumeDownItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                volumeControl.setValue(volumeControl.getValue() - 10);
                musicPlayer.changeVolume((int) volumeControl.getValue());
            }

        });

    }

    //MARK: playlist manage
    @FXML
    private void handleNewPlaylistButton(Event event) {
        Dialog dialog = new TextInputDialog("");
        dialog.setHeaderText("Add a new playlist");
        dialog.setTitle("");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            library.addPlaylist(result.get());
        }

    }

    @FXML
    private void handleRenamePlaylist(ActionEvent event) {
        Dialog dialog = new TextInputDialog("New playlist");
        dialog.setHeaderText("Rename the selected playlist");
        dialog.setTitle("");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            Playlist renamed = playlistsMenu.getSelectionModel().getSelectedItem();
            library.renamePlaylist(renamed, result.get());
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
            library.removePlaylist(playlistsMenu.getSelectionModel().getSelectedItem());
            playlistSongPane.setVisible(false);
        }
    }

    private void refreshPlaylistsMenu() {
        playlistsMenu.setItems(null);
        playlistsMenu.setItems(library.getPlaylistsPointer());
    }

    //MARK: player manage
    private void previousSongButton() {
        musicPlayer.previusSong();
    }

    private void playPauseButton() {
        if (musicPlayer.getPlayerStatus() == MediaPlayer.Status.UNKNOWN) {
            return;
        } else if (musicPlayer.getPlayerStatus() == MediaPlayer.Status.PLAYING) {
            musicPlayer.pause();
            playButton.setImage(SVGImage.loadIcon(ICON_PATH + "play.svg"));
        } else if (musicPlayer.getPlayerStatus() == MediaPlayer.Status.PAUSED) {
            musicPlayer.play();
            playButton.setImage(SVGImage.loadIcon(ICON_PATH + "pause.svg"));
        }
    }

    private void nextSongButton() {
        musicPlayer.nextSong();
    }

    private void shuffledButton() {
        musicPlayer.shuffle(!musicPlayer.getShuffle());
        System.out.println("Shuffle: " + musicPlayer.getShuffle());
    }

    private void replayButton() {
        if (musicPlayer.getRepeatPreference() == MusicPlayer.repeatType.NoRepeat) {
            musicPlayer.repeatPreference(MusicPlayer.repeatType.PlaylistRepeat);
        } else if (musicPlayer.getRepeatPreference() == MusicPlayer.repeatType.PlaylistRepeat) {
            musicPlayer.repeatPreference(MusicPlayer.repeatType.SingleSongRepeat);
        } else if (musicPlayer.getRepeatPreference() == MusicPlayer.repeatType.SingleSongRepeat) {
            musicPlayer.repeatPreference(MusicPlayer.repeatType.NoRepeat);
        }
    }

    @FXML
    private void handlePreviousSong(Event event) {
        musicPlayer.previusSong();
    }

    @FXML
    private void handlePlayPause(Event event) {
        if (musicPlayer.getPlayerStatus() == MediaPlayer.Status.UNKNOWN) {
            return;
        } else if (musicPlayer.getPlayerStatus() == MediaPlayer.Status.PLAYING) {
            musicPlayer.pause();
            playButton.setImage(SVGImage.loadIcon(ICON_PATH + "play.svg"));
        } else if (musicPlayer.getPlayerStatus() == MediaPlayer.Status.PAUSED) {
            musicPlayer.play();
            playButton.setImage(SVGImage.loadIcon(ICON_PATH + "pause.svg"));
        }
    }

    @FXML
    private void handleNextSong(Event event) {
        musicPlayer.nextSong();
    }

    @FXML
    private void handleShuffled(MouseEvent event) {
        musicPlayer.shuffle(!musicPlayer.getShuffle());
        System.out.println("Shuffle: " + musicPlayer.getShuffle());
    }

    @FXML
    private void handleReplay(MouseEvent event) {
        if (musicPlayer.getRepeatPreference() == MusicPlayer.repeatType.NoRepeat) {
            musicPlayer.repeatPreference(MusicPlayer.repeatType.PlaylistRepeat);
        } else if (musicPlayer.getRepeatPreference() == MusicPlayer.repeatType.PlaylistRepeat) {
            musicPlayer.repeatPreference(MusicPlayer.repeatType.SingleSongRepeat);
        } else if (musicPlayer.getRepeatPreference() == MusicPlayer.repeatType.SingleSongRepeat) {
            musicPlayer.repeatPreference(MusicPlayer.repeatType.NoRepeat);
        }
    }

    public void setPlayerGraphics(Song song) {
        playButton.setImage(SVGImage.loadIcon(ICON_PATH + "pause.svg"));

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

    public ChangeListener<Duration> getSlideTimeManager() {
        return new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observableValue, Duration oldValue, Duration newValue) {
                countUP.setText(Utility.getDurationAsString(newValue));
                int valueSlider = (int) (newValue.toMillis() * sliderTime.getMax() / musicPlayer.getActualSongDuration().toMillis());
                sliderTime.setValue(valueSlider);
            }
        };
    }

    //MARK: exit zone
    public void closePlayer() {
        musicPlayer.saveState();
    }
}
