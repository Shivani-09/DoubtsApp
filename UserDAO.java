package com.example.updatesql.dao;

import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

@Repository
public class UserDAO {

    private String url = "jdbc:mysql://localhost:3306/DoubtsApp";
    private String username = "root";
    private String password = "Tsat@9392350848";

    public boolean updateFieldById(int id, String field, String value) {
        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            String sql = "UPDATE user SET " + field + " = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, value);
            stmt.setInt(2, id);
            int rows = stmt.executeUpdate();
            stmt.close();
            conn.close();
            return rows > 0;
        } catch (Exception e) {
            System.out.println("Update Error: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteUser(int id) {
        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            String sql = "DELETE FROM user WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            stmt.close();
            conn.close();
            return rows > 0;
        } catch (Exception e) {
            System.out.println("Delete Error: " + e.getMessage());
            return false;
        }
    }
}
