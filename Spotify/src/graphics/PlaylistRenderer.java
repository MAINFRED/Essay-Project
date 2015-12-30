
package graphics;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import spotify.Playlist;

/**
 *
 * @author Andrea Antonioni -
 * <a href="mailto:andreaantonioni97@gmail.com">andreaantonioni97@gmail.com</a>
 */
public class PlaylistRenderer extends ListCell<Playlist>{
    private static final String urlIcon = "";
    
    @Override
    protected void updateItem(Playlist item, boolean empty) {
        super.updateItem(item, empty);
        setGraphic(createPane());
    }
    
    private Pane createPane() {
        Playlist item = getItem();
        if(item == null)
            return null;
        
        HBox hboxPane= new HBox();
        
        if(urlIcon != null && !urlIcon.equals(""))
        {
            ImageView icon = new ImageView(urlIcon);
            hboxPane.getChildren().add(icon);
        }

        Label description = new Label(item.getTitle());
        description.setFont(new Font("Helvetica", 14));
        description.setTextAlignment(TextAlignment.LEFT);
        hboxPane.getChildren().add(description);
        
        return hboxPane;
    }
}
