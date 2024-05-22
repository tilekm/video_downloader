package kz.tilek.downloader.database;

import io.github.cdimascio.dotenv.Dotenv;
import kz.tilek.downloader.utils.PasswordManager;
import kz.tilek.downloader.utils.custom.CustomException;

import java.sql.*;

public class DatabaseManager {

    private static Connection connection;
    private static final Dotenv dotenv = Dotenv.load();
    private static final String DB_URL = "jdbc:postgresql://" + dotenv.get("HOST") + ":" + dotenv.get("PORT") + "/" + dotenv.get("DBNAME");

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(DB_URL, dotenv.get("USER"), dotenv.get("PASSWORD"));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void login(String email, String password) throws CustomException {
        String query = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                if (PasswordManager.checkPassword(password, rs.getString("password"))) {
                    User user = User.getInstance();
                    user.setId(rs.getInt("id"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                    user.setDownloadLimit(rs.getInt("download_limit"));
                    user.setTrys(rs.getInt("trys"));
                    user.setReloadTime(rs.getDate("reload_time"));
                } else {
                    throw new CustomException("Incorrect password");
                }
            }
        } catch (SQLException e) {
            throw new CustomException("User not found");
        }
    }

    public static void register(User user) throws CustomException {
        String query = "INSERT INTO users (email, password) VALUES (?, ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user.getEmail());
            stmt.setString(2, PasswordManager.hashPassword(user.getPassword()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new CustomException("User already exists");
        }
    }

    public static void update(String email, String oldPass, String newPass) throws CustomException {
        PasswordManager.checkPassword(oldPass, User.getInstance().getPassword());
        String query = "UPDATE users SET password = ? WHERE email = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, PasswordManager.hashPassword(newPass));
            stmt.setString(2, email);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new CustomException("User not found");
        }
    }

    public static void loadUser(int id) {
        String query = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = User.getInstance();
                user.setId(rs.getInt("id"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setDownloadLimit(rs.getInt("download_limit"));
                user.setTrys(rs.getInt("trys"));
                user.setReloadTime(rs.getDate("reload_time"));
            }
        } catch (SQLException e) {
            System.out.println("User not found");
        }
    }

    public static void logout(User instance) {
        new Thread(() -> {
            try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("UPDATE users SET trys = " + instance.getTrys() + " WHERE id = " + instance.getId());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
    }
    public static void setTrys(User instance) {
        new Thread(() -> {
            try {
                Connection conn = getConnection();
                Statement stmt = conn.createStatement();
                stmt.executeUpdate("UPDATE users SET trys = " + instance.getTrys() + " WHERE id = " + instance.getId());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void setReloadTime(User instance) {
        new Thread(() -> {
            try {
                Connection conn = getConnection();
                Statement stmt = conn.createStatement();
                stmt.executeUpdate("UPDATE users SET reload_time = '" + new Date(instance.getReloadTime().getTime()) + "' WHERE id = " + instance.getId());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
    }
}