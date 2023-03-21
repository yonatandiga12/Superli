package Domain.DAL;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionHandler implements Closeable {
    private final static String dbName= "Superly.db"; // need to be change!
    private static String url = String.format("jdbc:sqlite:%s\\%s",System.getProperty("user.dir"),dbName);
    // String url = String.format("jdbc:sqlite:%s/%s",System.getProperty("user.dir"),dbName); the url for the jar
    private static Connection connection;
    private static int counter =0;

    public static void setUrl(String url1) {url=url1;}
    public static String getUrl() {return url;}
    public ConnectionHandler(){
        synchronized (ConnectionHandler.class) {
            counter++;
            if (connection == null) {
                try {
                    connection = DriverManager.getConnection(url);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }

    }
    @Override
    public void close() {
        synchronized (ConnectionHandler.class) {
            counter = counter - 1;
            if (counter == 0) {
                try {
                    connection.close();
                    connection = null;
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }

    public Connection get() throws SQLException {
        return connection;
    }
}
