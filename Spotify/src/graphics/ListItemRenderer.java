package graphics;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * Contains the way to represents graphically ListItem objects inside the ListView.
 * @see spotify.FXMLDocumentController#musicMenu
 * @see spotify.FXMLDocumentController#playlistsMenu
 * @author Andrea Antonioni -
 * <a href="mailto:andreaantonioni97@gmail.com">andreaantonioni97@gmail.com</a>
 */
public class ListItemRenderer extends ListCell<ListItem>{
    
    @Override
    protected void updateItem(ListItem item, boolean empty) {
        super.updateItem(item, empty);
        setGraphic(createPane());
    }
    
    private Pane createPane() {
        ListItem item = getItem();
        if(item == null)
            return null;
        
        HBox hboxPane= new HBox();
        
        if(item.getUrlIcon() != null && !item.getUrlIcon().equals(""))
        {
            ImageView icon = new ImageView(item.getUrlIcon());
            hboxPane.getChildren().add(icon);
        }

        Label description = new Label(item.getDescription());
        description.setFont(new Font("Helvetica", 14));
        description.setTextAlignment(TextAlignment.LEFT);
        hboxPane.getChildren().add(description);
        
        return hboxPane;
    }

}