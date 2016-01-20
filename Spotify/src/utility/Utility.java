package utility;

import graphics.MyTranscoder;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.commons.lang3.time.DurationFormatUtils;
import spotify.GUIController;

/**
 * This class contains utility methods.
 *
 * @author Andrea Antonioni -
 * <a href="mailto:andreaantonioni97@gmail.com">andreaantonioni97@gmail.com</a>
 */
public class Utility {

    /**
     * Sets the proxy settings for the internet connection.
     *
     * @param server A String which represents the server ip.
     * @param porta A String which represents the number of the port.
     */
    public static void setProxy(String server, String porta) {
        System.setProperty("proxySet", "true");
        System.setProperty("http.proxyHost", server);
        System.setProperty("http.proxyPort", porta);
    }

    /**
     * Sets the proxy settings with authentication for the internet
     * connection.
     *
     * @param server A String which represents the server ip.
     * @param porta A String which represents the number of the port.
     * @param username A String which represents the username.
     * @param password A String which represents the password.
     */
    public static void setProxy(String server, String porta, String username, String password) {
        System.setProperty("proxySet", "true");
        System.setProperty("http.proxyHost", server);
        System.setProperty("http.proxyPort", porta);

        Authenticator.setDefault(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication(username, password.toCharArray());
            }
        });
    }
    
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
    
    public static Image loadSVGIcon(String pathIcon) {

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
    
    public static String getDurationAsString(Duration duration)
    {
        if(duration.isIndefinite() || duration.isUnknown())
            return "00:00";
       return ""+(int)duration.toMinutes()+":"+duration.toSeconds()%60;
    }
    
}