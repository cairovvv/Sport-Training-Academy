package com.mycompany.sportstrainingacademy;

import java.sql.*;

public class EnrollAthlete {
    private final String url = "jdbc:sqlite:C:/Users/james paul/Documents/Athletes.db";

    // NEW: Admin Feature - Create a Training Session
    public boolean createSession(String name, String status, int maxCap) {
        String sql = "INSERT INTO tbl_Sessions (SessionName, Status, MaxCapacity, CurrentEnrollment) VALUES (?, ?, ?, 0)";
        
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, name);
            pstmt.setString(2, status); 
            pstmt.setInt(3, maxCap);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Session Creation Error: " + e.getMessage());
            return false;
        }
    }

    // Athlete Feature - Enroll in an existing session
    public String processEnrollment(int athleteId, int sessionId) {
        String checkAthlete = "SELECT * FROM tbl_Information WHERE AtheleteID = ?";
        String checkSession = "SELECT * FROM tbl_Sessions WHERE SessionID = ?";
        String enrollSql = "INSERT INTO tbl_Enrollments (AtheleteID, SessionID) VALUES (?, ?)";
        String updateCount = "UPDATE tbl_Sessions SET CurrentEnrollment = CurrentEnrollment + 1 WHERE SessionID = ?";

        try (Connection conn = DriverManager.getConnection(url)) {
            // 1. Requirement: IF athlete not found
            try (PreparedStatement ps = conn.prepareStatement(checkAthlete)) {
                ps.setInt(1, athleteId);
                if (!ps.executeQuery().next()) return "Athlete not found.";
            }

            // 2. Requirement: Session Checks
            try (PreparedStatement ps = conn.prepareStatement(checkSession)) {
                ps.setInt(1, sessionId);
                ResultSet rs = ps.executeQuery();
                
                if (!rs.next()) return "Session not found.";
                
                String status = rs.getString("Status");
                int max = rs.getInt("MaxCapacity");
                int current = rs.getInt("CurrentEnrollment");

                if (!"Scheduled".equalsIgnoreCase(status)) {
                    return "Session already completed or cancelled.";
                }

                if (current >= max) {
                    return "Session is already at full capacity.";
                }
            }

            // 3. Success: Perform Transaction
            conn.setAutoCommit(false);
            try (PreparedStatement psEnroll = conn.prepareStatement(enrollSql);
                 PreparedStatement psUpdate = conn.prepareStatement(updateCount)) {
                
                psEnroll.setInt(1, athleteId);
                psEnroll.setInt(2, sessionId);
                psEnroll.executeUpdate();

                psUpdate.setInt(1, sessionId);
                psUpdate.executeUpdate();

                conn.commit();
                return "Athlete enrolled in session successfully.";
            } catch (SQLException e) {
                conn.rollback();
                return "Enrollment failed: " + e.getMessage();
            }

        } catch (SQLException e) {
            return "Database Error: " + e.getMessage();
        }
    }
}