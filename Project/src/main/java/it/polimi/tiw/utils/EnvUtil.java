package it.polimi.tiw.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * This class provides an easy and clean way to parse the .env file for DB access info and other settings.
 * It automatically locates the .env file, provided it is located inside the WEB-INF folder, and reads its content.
 *
 * It provides:
 * - A method to get a configured Database Connection
 * - A method to get the path to the folder where User-uploaded images are stored
 */
public class EnvUtil {
    private static final Dotenv dotenv;

    static {
        String rawPath = EnvUtil.class.getClassLoader()
            .getResource("../../WEB-INF/.env")
            .getPath()
            .replace("%20", " ") // handle spaces
            .replace("/.env", "");

        // ✅ Fix for Windows-style paths like /C:/...
        if (rawPath.matches("^/[A-Z]:/.*")) {
            rawPath = rawPath.substring(1);
        }

        dotenv = Dotenv.configure()
                .directory(rawPath)
                .filename(".env")
                .load();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("❌ MySQL JDBC Driver not found", e);
        }
    }

    private static final String DB_URL = dotenv.get("DB_URL");
    private static final String DB_USER = dotenv.get("DB_USER");
    private static final String DB_PASS = dotenv.get("DB_PASS");
    private static final String UPLOAD_DIR = dotenv.get("UPLOAD_DIR");

    // Returns a configured connection to the Database
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    // Returns the path to the folder where uploaded images are stored
    public static String getUploadDir() {
        return UPLOAD_DIR;
    }
}