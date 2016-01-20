package connection;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

/**
 *
 * @author Andrea Antonioni -
 * <a href="mailto:andreaantonioni97@gmail.com">andreaantonioni97@gmail.com</a>
 */
public class Proxy {
    
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
}
