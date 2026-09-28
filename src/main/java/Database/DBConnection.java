package Database;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    public static Connection getConnection() {
        Connection con = null;

        try {
            // 1. load and register driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. read credentials from db.properties (password secure rakhna)
            Properties props = new Properties();
            FileInputStream input = new FileInputStream("db.properties");
            props.load(input);
            input.close();

            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");

            // 3. establish connection
            con = DriverManager.getConnection(url, user, password);


        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL Driver not found", e);
        } catch (IOException e) {
            throw new RuntimeException("Could not read db.properties file", e);
        } catch (SQLException e) {
            throw new RuntimeException("Database connection failed", e);
        }

        return con;
    }
}