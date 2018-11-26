package site.zido.elise.proxy;

/**
 * http proxy
 *
 * @author zido
 */
public class Proxy {

    private String host;
    private int port;
    private String username;
    private String password;

    private Proxy() {

    }

    /**
     * Instantiates a new Proxy.
     *
     * @param host the host
     * @param port the port
     */
    public Proxy(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Instantiates a new Proxy.
     *
     * @param host     the host
     * @param port     the port
     * @param username the username
     * @param password the password
     */
    public Proxy(String host, int port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    /**
     * Gets host.
     *
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * Gets port.
     *
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Proxy proxy = (Proxy) o;

        if (port != proxy.port) {
            return false;
        }
        if (host != null ? !host.equals(proxy.host) : proxy.host != null) {
            return false;
        }
        if (username != null ? !username.equals(proxy.username) : proxy.username != null) {
            return false;
        }
        return password != null ? password.equals(proxy.password) : proxy.password == null;
    }

    @Override
    public int hashCode() {
        int result = host != null ? host.hashCode() : 0;
        result = 31 * result + port;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Proxy{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
