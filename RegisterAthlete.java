package com.mycompany.sportstrainingacademy;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegisterAthlete {

    private final String url = "jdbc:sqlite:C:/Users/james paul/Documents/Athletes.db";

    public String loginValidation(String username, String password) {
        String sql = "SELECT Password, Roles FROM tbl_Users WHERE Username = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) {
                return "NOT_FOUND";
            }

            String storedPassword = rs.getString("Password");
            String role = rs.getString("Roles"); 

            if (storedPassword.equals(password)) {
                return role; 
            } else {
                return "WRONG_PASS";
            }

        } catch (SQLException e) {
            System.err.println("Login error: " + e.getMessage());
            return "ERROR";
        }
    }

    public boolean registerFullProfile(String user, String pass, String fName, String lName, int age, String sport, double bmi) {
        String userSql = "INSERT INTO tbl_Users (Username, Password, Roles) VALUES(?,?,?)";
        String infoSql = "INSERT INTO tbl_Information (FirstName, LastName, Age, Sport, BMI) VALUES(?,?,?,?,?)";

        try (Connection conn = DriverManager.getConnection(url)) {
            conn.setAutoCommit(false); 

            try (PreparedStatement pstmt1 = conn.prepareStatement(userSql);
                 PreparedStatement pstmt2 = conn.prepareStatement(infoSql)) {

                pstmt1.setString(1, user);
                pstmt1.setString(2, pass);
                pstmt1.setString(3, "Athlete"); 
                pstmt1.executeUpdate();

                pstmt2.setString(1, fName);
                pstmt2.setString(2, lName);
                pstmt2.setInt(3, age);
                pstmt2.setString(4, sport);
                pstmt2.setDouble(5, bmi);
                pstmt2.executeUpdate();

                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                System.err.println("Database error: " + e.getMessage());
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Connection error: " + e.getMessage());
            return false;
        }
    }

    public List<Athletes> getAllAthletes() {
        List<Athletes> list = new ArrayList<>();
        String sql = "SELECT * FROM tbl_Information";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Athletes(
                        rs.getInt("AtheleteID"), // MATCHED: Database spelling
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getInt("Age"),
                        rs.getString("Sport"),
                        rs.getDouble("BMI")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching list: " + e.getMessage());
        }
        return list;
    }
}
