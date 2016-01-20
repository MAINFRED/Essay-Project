package utility;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import javafx.util.Duration;
import org.apache.commons.lang3.time.DurationFormatUtils;

/**
 * This class contains utility methods.
 *
 * @author Andrea Antonioni -
 * <a href="mailto:andreaantonioni97@gmail.com">andreaantonioni97@gmail.com</a>
 */
public class Utility {

    /**
     * Copies a source File object in a directory specified in destination File object.
     * @param source A File object which represents the file to be copied.
     * @param dest A File object which represents the new file copied in the new directory.
     * @throws IOException If the source path or destination path don't exist.
     * @throws java.nio.file.FileAlreadyExistsException If the file already exists in the destination path.
     */
    public static void copyFile(File source, File dest) throws FileAlreadyExistsException, IOException
    {
        Files.copy(source.toPath(), dest.toPath()); 
    }
    
    public static BufferedImage resize(BufferedImage image, int width, int height) 
    {
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
        Graphics2D g2d = (Graphics2D) bi.createGraphics();
        g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        g2d.drawImage(image, 0, 0, width, height, null);
        g2d.dispose();
        return bi;
    }
    
    public static String getDurationAsString(Duration duration)
    {
        if(duration.isUnknown())
            return "00:00";
        return DurationFormatUtils.formatDuration((long)duration.toMillis(), "mm:ss");
    }
    
}