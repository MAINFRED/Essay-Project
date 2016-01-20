package graphics.menu;

import graphics.svg.SVGImage;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * Contains the way to represents graphically ListItem objects inside the ListView.
 * @see spotify.FXMLDocumentController#musicMenu
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
            ImageView icon = new ImageView();
            icon.setFitHeight(20);
            icon.setFitWidth(20);
            icon.setImage(SVGImage.loadIcon(item.getUrlIcon()));
            hboxPane.getChildren().add(icon);
            hboxPane.setSpacing(10); //space between items in a HBox
        }

        Label description = new Label(item.getDescription());
        description.setFont(new Font("Helvetica", 14));
        description.setTextAlignment(TextAlignment.LEFT);
        hboxPane.getChildren().add(description);
        
        return hboxPane;
    }

}