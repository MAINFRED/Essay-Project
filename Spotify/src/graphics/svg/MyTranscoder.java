package graphics.svg;

import java.awt.image.BufferedImage;
import org.apache.batik.transcoder.*;
import org.apache.batik.transcoder.image.ImageTranscoder;

/**
 *
 * @author Andrea Antonioni -
 * <a href="mailto:andreaantonioni97@gmail.com">andreaantonioni97@gmail.com</a>
 */
public class MyTranscoder extends ImageTranscoder {
 
    private BufferedImage image = null;
 
    @Override
    public BufferedImage createImage(int w, int h) {
        image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        return image;
    }
 
   public BufferedImage getImage() {
        return image;
    }

    @Override
    public void writeImage(BufferedImage bi, TranscoderOutput to) throws TranscoderException {
        
    }
}