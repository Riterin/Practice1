package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DatabaseConnection {

    private static final String URL = "jdbc:postgresql://localhost:5432/caliber";
    private static final String USER = "postgres";
    private static final String PASSWORD = "all";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // ---------- Регистрация ----------

    public boolean registerUser(User user) {
        String sql = "INSERT INTO users (login, password, role) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getLogin());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole() != null ? user.getRole() : "caliber_client");
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                System.err.println("Пользователь с таким логином уже существует");
            } else {
                e.printStackTrace();
            }
            return false;
        }
    }

    // ---------- Все пользователи ----------

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, login, password, role FROM users ORDER BY id";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("login"),
                        rs.getString("password"),
                        rs.getString("role")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    // ---------- Роль по id пользователя ----------

    public Optional<String> getRoleByUserId(int userId) {
        String sql = "SELECT role FROM users WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(rs.getString("role"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    // ---------- Роль по логину ----------

    public Optional<String> getRoleByLogin(String login) {
        String sql = "SELECT role FROM users WHERE login = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, login);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(rs.getString("role"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    // ---------- Смена роли (только для админа, проверка — на уровне приложения) ----------

    public boolean updateUserRole(int userId, String newRole) {
        String sql = "UPDATE users SET role = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newRole);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            if ("23514".equals(e.getSQLState())) {
                System.err.println("Недопустимая роль: " + newRole);
            } else {
                e.printStackTrace();
            }
            return false;
        }
    }

    // ---------- Товары ----------

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT id, name, quality FROM products ORDER BY id";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("quality")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    // ---------- Аутентификация ----------

    public Optional<User> authenticate(String login, String password) {
        String sql = "SELECT id, login, password, role FROM users WHERE login = ? AND password = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, login);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new User(
                            rs.getInt("id"),
                            rs.getString("login"),
                            rs.getString("password"),
                            rs.getString("role")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    // ---------- Сохранение товаров (батч + транзакция) ----------

    public int saveProducts(List<Product> products) {
        String sql = "UPDATE products SET name = ?, quality = ? WHERE id = ?";
        int updated = 0;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);

            for (Product p : products) {
                stmt.setString(1, p.getName());
                stmt.setInt(2, p.getQuality());
                stmt.setInt(3, p.getId());
                stmt.addBatch();
            }

            int[] results = stmt.executeBatch();
            conn.commit();

            for (int r : results) {
                if (r >= 0 || r == Statement.SUCCESS_NO_INFO) updated++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updated;
    }

    public static String roleToRussian(String role) {
        if (role == null) return "Неизвестно";
        return switch (role) {
            case "caliber_admin"   -> "Администратор";
            case "caliber_barbers" -> "Барбер";
            case "caliber_client"  -> "Клиент";
            default -> "Неизвестно";
        };
    }

}
