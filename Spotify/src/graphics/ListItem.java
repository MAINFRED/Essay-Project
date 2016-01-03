package graphics;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * Represents items of menus on the left side.
 * @author Andrea Antonioni -
 * <a href="mailto:andreaantonioni97@gmail.com">andreaantonioni97@gmail.com</a>
 */
public class ListItem {
    private String urlIcon;
    private String description;
    
    public static final String PLAYLIST_ICON = "";

    /**
     * Creates a new ListItem obejct which contains a description of the item and and icon which represents the item.
     * @param description A String object which contains the description of the item.
     * @param urlIcon A String which represents the url of the icon which represents the item.
     */
    public ListItem(String description, String urlIcon) {
        this.urlIcon = urlIcon;
        this.description = description;
    }

    /**
     * Returns a String object which contains the url of the icon.
     * @return A String object which contains the url of the icon.
     */
    public String getUrlIcon() {
        return urlIcon;
    }

    /**
     * Returns a String object which contains a description of the item.
     * @return A String object which contains a description of the item.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the item.
     * @param description A String object which contains a description of the item.
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Pane getGraphics()
    {
        HBox hboxPane= new HBox();
        
        if(this.getUrlIcon() != null && !this.getUrlIcon().equals(""))
        {
            ImageView icon = new ImageView(this.getUrlIcon());
            hboxPane.getChildren().add(icon);
        }

        Label description = new Label(this.getDescription());
        description.setFont(new Font("Helvetica", 14));
        description.setTextAlignment(TextAlignment.LEFT);
        hboxPane.getChildren().add(description);
        
        return hboxPane;
    }

    @Override
    public String toString() {
        return "ListItem{" + "description=" + description + '}';
    }
       
}
