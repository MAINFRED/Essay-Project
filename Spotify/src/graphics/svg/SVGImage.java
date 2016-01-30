package graphics.svg;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import graphics.GUIController;

/**
 * Loads images with ".svg" extention
 * @see graphics.svg.MyTranscoder
 * @author Antonioni Andrea & Zanelli Gabriele
 */
public class SVGImage {
    
    public static Image loadIcon(String pathIcon) {

        MyTranscoder imageTranscoder = new MyTranscoder();

        TranscoderInput transIn = new TranscoderInput(pathIcon);
        try {
            imageTranscoder.transcode(transIn, null);
        } catch (TranscoderException ex) {
            Logger.getLogger(GUIController.class.getName()).log(Level.SEVERE, null, ex);
        }

        Image img = SwingFXUtils.toFXImage(imageTranscoder.getImage(), null);
        
        return img;
    }
}
