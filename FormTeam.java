package com.mycompany.sportstrainingacademy;

import java.sql.*;

public class FormTeam {
    private final String url = "jdbc:sqlite:C:/Users/james paul/Documents/Athletes.db";

    // Requirement: Create Team (Admin Only)
    public boolean createTeam(String name, int captainId, String sport, int maxSize) {
        String sql = "INSERT INTO tbl_Teams (TeamName, CaptainID, Sport, MaxTeamSize) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, name);
            pstmt.setInt(2, captainId);
            pstmt.setString(3, sport);
            pstmt.setInt(4, maxSize);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Team Creation Error: " + e.getMessage());
            return false;
        }
    }

    // Requirement: View Team Records (Admin Only)
    public void displayTeamRecords() {
        // This query combines tbl_Teams with tbl_Information to get the Full Name
        String sql = "SELECT t.TeamID, t.TeamName, i.FirstName || ' ' || i.LastName AS CaptainName, t.Sport, t.MaxTeamSize " +
                     "FROM tbl_Teams t " +
                     "JOIN tbl_Information i ON t.CaptainID = i.AtheleteID";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n==================== TEAM RECORDS (ADMIN ONLY) ====================");
            System.out.printf("%-5s %-15s %-20s %-12s %-8s%n", "ID", "TEAM NAME", "CAPTAIN", "SPORT", "MAX SIZE");
            System.out.println("-------------------------------------------------------------------");

            while (rs.next()) {
                System.out.printf("%-5d %-15s %-20s %-12s %-8d%n",
                    rs.getInt("TeamID"),
                    rs.getString("TeamName"),
                    rs.getString("CaptainName"),
                    rs.getString("Sport"),
                    rs.getInt("MaxTeamSize"));
            }
            System.out.println("===================================================================\n");

        } catch (SQLException e) {
            System.err.println("Error retrieving teams: " + e.getMessage());
        }
    }
}
