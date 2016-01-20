package graphics;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import spotify.Song;

/**
 *
 * @author Andrea Antonioni -
 * <a href="mailto:andreaantonioni97@gmail.com">andreaantonioni97@gmail.com</a>
 */
public class SongTable extends TableView<Song>{
    private TableColumn<Song, String> songColumn = new TableColumn<>("Song");
    private TableColumn<Song, String> artistColumn = new TableColumn<>("Artist");
    private TableColumn<Song, String> albumColumn = new TableColumn<>("Album");
    private TableColumn<Song, String> durationColumn = new TableColumn<>("Duration");
    
    public SongTable(ObservableList<Song> list)
    {
        initSongTable();
        this.setItems(list);
    }
    
    public SongTable()
    {
        initSongTable();
    }
    
    private void initSongTable()
    {
        this.getColumns().addAll(songColumn, artistColumn, albumColumn, durationColumn);
        this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        songColumn.setCellValueFactory(new PropertyValueFactory<Song, String>("title"));
        artistColumn.setCellValueFactory(new PropertyValueFactory<Song, String>("artist"));
        albumColumn.setCellValueFactory(new PropertyValueFactory<Song, String>("album"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<Song, String>("durationAsString"));
    }
    
}
