 
package spotify;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.tika.exception.TikaException;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.xml.sax.SAXException;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Antonioni Andrea & Zanelli Gabriele
 */
public class Metadata {
    private org.apache.tika.metadata.Metadata metadata;
    private static final String TITLE = "title";
    private static final String ARTIST = "xmpDM:artist";
    private static final String ALBUM = "xmpDM:album";
    private static final String DURATION = "xmpDM:duration";
    private static final String GENRE = "xmpDM:composer";
    private static final String COMPOSER = "xmpDM:composer";
    
    public Metadata(File file) throws FileNotFoundException
    {
        try {
            InputStream input = new FileInputStream(file);
            ContentHandler handler = new DefaultHandler();
            metadata = new org.apache.tika.metadata.Metadata();
            Parser parser = new Mp3Parser();
            ParseContext parseCtx = new ParseContext();
            parser.parse(input, (org.xml.sax.ContentHandler) handler, metadata, parseCtx);
            input.close();
            
        } catch (IOException ex) {
            Logger.getLogger(Metadata.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(Metadata.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TikaException ex) {
            Logger.getLogger(Metadata.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public String getTitle()
    {
        return metadata.get(TITLE);
    }
    
    public String getArtist()
    {
        return metadata.get(ARTIST);
    }
    
    public String getAlbum()
    {
        return metadata.get(ALBUM);
    }
    
    public int getDuration()
    {
        if(metadata.get(DURATION) == null)
            return 0;
        return Integer.parseInt(metadata.get(DURATION).substring(0, metadata.get(DURATION).indexOf(".")));
    }
    
    public String getGenre()
    {
        return metadata.get(GENRE);
    }
    
    public String getComposer()
    {
        return metadata.get(COMPOSER);
    }
    
    
}
