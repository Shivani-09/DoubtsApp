package com.example.doubtsApp.repository;

import com.example.doubtsApp.model.User;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource; 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; 

@Repository 
public class CustomUserRepositoryImpl implements CustomUserRepository {

    private final DataSource dataSource; 

//    @Autowired
    public CustomUserRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public User customSave(User user) {
        if (user.getId() == null) {
            return insertUser(user);
        } else {
            return updateUser(user);
        }
    }

    private User insertUser(User user) {
        final String SQL_INSERT = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;

        try {
            conn = dataSource.getConnection(); 
            pstmt = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword()); 

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getLong(1)); 
            } else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }
            return user;

        } catch (SQLException ex) {
            System.err.println("Error inserting user: " + ex.getMessage());
            
            throw new RuntimeException("Database error during user insert", ex);
        } finally {
        	
            try {
                if (generatedKeys != null) generatedKeys.close();
            } catch (SQLException e) { System.err.println("Error closing ResultSet: " + e.getMessage()); }
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) { System.err.println("Error closing PreparedStatement: " + e.getMessage()); }
            try {
                if (conn != null) conn.close(); 
            } catch (SQLException e) { System.err.println("Error closing Connection: " + e.getMessage()); }
        }
    }

    private User updateUser(User user) {
        final String SQL_UPDATE = "UPDATE users SET username = ?, email = ?, password = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement(SQL_UPDATE);

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.setLong(4, user.getId());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected == 0) {
                System.out.println("No user found with ID " + user.getId() + " to update.");
            }
            return user;

        } catch (SQLException ex) {
            System.err.println("Error updating user: " + ex.getMessage());
            throw new RuntimeException("Database error during user update", ex);
        } finally {
        	
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) { System.err.println("Error closing PreparedStatement: " + e.getMessage()); }
            try {
                if (conn != null) conn.close(); // Return connection to pool
            } catch (SQLException e) { System.err.println("Error closing Connection: " + e.getMessage()); }
        }
    }
}