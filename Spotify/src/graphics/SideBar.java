
package graphics;

import com.sun.prism.paint.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import spotify.Playlist;
import spotify.PlaylistLibrary;

/**
 *
 * @author Andrea Antonioni -
 * <a href="mailto:andreaantonioni97@gmail.com">andreaantonioni97@gmail.com</a>
 */
public class SideBar {

    private static final String MAIN_MENU = "YOUR SONG";
    private static final String PLAYLISTS_MENU = "PLAYLISTS";
    private static final String SELECTION_ITEM = "-fx-background-color: red;";
    private HashMap<ListItem, Playlist> hashmap = new HashMap<>();
    private PlaylistLibrary playlistLibrary;
    private VBox vboxPane;
    private ContextMenu popupMenu;
    
    public SideBar(VBox pane, PlaylistLibrary playlistLibrary) {
        this.vboxPane = pane;
        this.playlistLibrary = playlistLibrary;

        ListChangeListener<Playlist> listener = new ListChangeListener<Playlist>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends Playlist> c) {
                System.out.println("Changed event");
                while (c.next()) {
                    if (c.wasAdded()) {
                        List<Playlist> list = (List<Playlist>) c.getAddedSubList();
                        for (Playlist playlist : list) {
                            hashmap.put(new ListItem(playlist.getTitle(), ListItem.PLAYLIST_ICON), playlist);
                            System.out.println("Added new element: " + playlist.getTitle());
                        }
                    } else if (c.wasRemoved()) {

                    } else if (c.wasUpdated()) //Rename
                    {

                    }
                }

                refreshGraphics();
            }

        };

        playlistLibrary.addListener(listener);
        refreshGraphics();
    }

    public PlaylistLibrary getPlaylistLibrary() {
        return playlistLibrary;
    }

    public void refreshGraphics() {
        vboxPane.getChildren().clear();

        Label firstTitle = new Label(MAIN_MENU);
        firstTitle.setFont(new Font("Helvetica", 14));
        vboxPane.getChildren().add(firstTitle);

        Label secondTitle = new Label(PLAYLISTS_MENU);
        secondTitle.setFont(new Font("Helvetica", 14));
        vboxPane.getChildren().add(secondTitle);
        Set<ListItem> setCollection = hashmap.keySet();
        for (ListItem item : setCollection) {
            Pane pane = item.getGraphics();
            pane.setOnMouseClicked(new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent event) {
                    pane.setStyle(SELECTION_ITEM);
                    pane.requestLayout();
                }
                
            });
            
            pane.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>(){
                @Override
                public void handle(ContextMenuEvent event) {
                    Pane paneItem = (Pane) event.getSource();
                    paneItem.setStyle(SELECTION_ITEM);
                    popupMenu.show(vboxPane, event.getScreenX(), event.getScreenY());
                }
                
            });
            
            vboxPane.getChildren().add(pane);
        }

        vboxPane.requestLayout();
    }
    
    public void initPopupMenu() {
        popupMenu = new ContextMenu();
        
        MenuItem renameItem = new MenuItem("Rename");
        renameItem.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                Dialog dialog = new TextInputDialog();
                dialog.setHeaderText("Rename the selected playlist");
                dialog.setTitle("");

                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()) {
                    
                }
            }
            
        });
        popupMenu.getItems().add(renameItem);
        
        MenuItem deleteItem = new MenuItem("Delete");
        renameItem.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("");
                alert.setHeaderText("Do you want to delete the selected playlist?");
                alert.setContentText("");
                ButtonType buttonDelete = new ButtonType("Yes");
                ButtonType buttonNoDelete = new ButtonType("No");
                alert.getButtonTypes().setAll(buttonDelete, buttonNoDelete);

                Optional<ButtonType> result = alert.showAndWait();
                if(result.get() == buttonDelete)
                {
                    
                }
            }
            
        });
        popupMenu.getItems().add(deleteItem);
    }

}
