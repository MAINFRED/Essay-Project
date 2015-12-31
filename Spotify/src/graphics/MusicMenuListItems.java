package graphics;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 * Creates the menu on the left side called "YOUR MUSIC".
 * @author Andrea Antonioni -
 * <a href="mailto:andreaantonioni97@gmail.com">andreaantonioni97@gmail.com</a>
 */
public class MusicMenuListItems {
    private static ObservableList<ListItem> listItems = null;
    
    private static ObservableList<ListItem> createMenu()
    {
        String[] urlIcons = {"", "", "", ""};
        ArrayList<ListItem> items = new ArrayList<>();
        items.add(new ListItem("Songs", urlIcons[0]));
        items.add(new ListItem("Album", urlIcons[1]));
        items.add(new ListItem("Artists", urlIcons[2]));
        items.add(new ListItem("Local files", urlIcons[3]));
        
        ObservableList<ListItem> observableList = FXCollections.observableArrayList();
        observableList.addAll(items);
        
        return observableList;
    }
    
    public static ObservableList<ListItem> get()
    {
        if(listItems == null)
            listItems = createMenu();
        return listItems;
    }
}
