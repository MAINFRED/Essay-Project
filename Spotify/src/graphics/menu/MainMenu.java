package graphics.menu;

import java.util.Arrays;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import graphics.GUIController;

/**
 * Creates the menu on the left side called "YOUR MUSIC".
 * @author Antonioni Andrea & Zanelli Gabriele
 */
public class MainMenu {
    
    private static final String[] urlIcons = {GUIController.ICON_PATH + "song.svg", 
        GUIController.ICON_PATH + "album.svg", GUIController.ICON_PATH + "artist.svg", 
        GUIController.ICON_PATH + "localFiles.svg"};
    private static ObservableList<ListItem> listItems = null;
    
    public static final ListItem songItem = new ListItem("Songs", urlIcons[0]);
    public static final ListItem albumItem = new ListItem("Album", urlIcons[1]);
    public static final ListItem artistsItem = new ListItem("Artists", urlIcons[2]);
    public static final ListItem localFilesItem = new ListItem("Local files", urlIcons[3]);
    
    private static ObservableList<ListItem> createMenu()
    {
        List<ListItem> items = Arrays.asList(songItem, albumItem, artistsItem, localFilesItem);
        
        ObservableList<ListItem> observableList = FXCollections.observableArrayList();
        observableList.addAll(items);
        
        return observableList;
    }
    
    /**
     * Returns a reference of a pre-set ObservableList object which represents the items of the MainMenu
     * @return An ObservableList object which represents the items of the MainMenu
     */
    public static ObservableList<ListItem> get()
    {
        if(listItems == null)
            listItems = createMenu();
        return listItems;
    }
}
