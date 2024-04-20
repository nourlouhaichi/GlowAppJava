package Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDatabase {
    private final String URL = "jdbc:mysql://localhost:3306/glowpi";
    private final String USER = "root";
    private final String PASSWORD = "";
    private Connection connection;
    private static MyDatabase instance;

    private MyDatabase() {
        try {
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected");
        } catch (SQLException var2) {
            System.err.println(var2.getMessage());
        }

    }

    public static MyDatabase getInstance() {
        if (instance == null) {
            instance = new MyDatabase();
        }

        return instance;
    }

    public Connection getConnection() {
        return this.connection;
    }
}
