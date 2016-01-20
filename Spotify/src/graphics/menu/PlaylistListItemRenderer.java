
package graphics.menu;

import graphics.svg.SVGImage;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import spotify.GUIController;
import spotify.Playlist;
import utility.Utility;

/**
 *
 * @author Andrea Antonioni -
 * <a href="mailto:andreaantonioni97@gmail.com">andreaantonioni97@gmail.com</a>
 */
public class PlaylistListItemRenderer extends ListCell<Playlist>{
    private static final String urlIcon = GUIController.ICON_PATH + "playlist.svg";
    
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
            ImageView icon = new ImageView();
            icon.setFitHeight(20);
            icon.setFitWidth(20);
            icon.setImage(SVGImage.loadIcon(urlIcon));
            hboxPane.getChildren().add(icon);
            hboxPane.setSpacing(10); //space between items in a HBox
        }

        Label description = new Label(item.getTitle());
        description.setFont(new Font("Helvetica", 14));
        description.setTextAlignment(TextAlignment.LEFT);
        hboxPane.getChildren().add(description);
        
        return hboxPane;
    }
}
