package com.mycompany.sportstrainingacademy;

import java.util.Scanner;
import java.util.List;

public class SportsTrainingAcademy {
    public static void main(String[] args) {
        RegisterAthlete db = new RegisterAthlete();
        EnrollAthlete enroller = new EnrollAthlete();
        FormTeam teamCreator = new FormTeam();
        Scanner sc = new Scanner(System.in);
        
        boolean systemRunning = true;

        while (systemRunning) {
            System.out.println("\n========== SPORTS ACADEMY SYSTEM ==========");
            System.out.println("1. Login\n2. Sign Up\n3. Exit System");
            System.out.print("Selection: ");
            String mainChoice = sc.nextLine();

            switch (mainChoice) {
                case "1":
                    handleLoginFlow(db, enroller, teamCreator, sc);
                    break;
                case "2":
                    handleRegistration(db, sc);
                    break;
                case "3":
                    System.out.println("Goodbye!");
                    systemRunning = false;
                    break;
            }
        }
    }

    private static void handleLoginFlow(RegisterAthlete db, EnrollAthlete enroller, FormTeam teamCreator, Scanner sc) {
        System.out.print("Username: "); String user = sc.nextLine();
        System.out.print("Password: "); String pass = sc.nextLine();
        String status = db.loginValidation(user, pass);

        if (status.equals("NOT_FOUND")) {
            System.out.println("Account does not exist.");
        } else if (status.equals("WRONG_PASS")) {
            System.out.println("Invalid username or password.");
        } else {
            System.out.println("Login successful. Welcome, " + (status.equalsIgnoreCase("Admin") ? "Head Coach." : "Athlete."));
            handleUserDashboard(status, db, enroller, teamCreator, sc);
        }
    }

    private static void handleUserDashboard(String role, RegisterAthlete db, EnrollAthlete enroller, FormTeam teamCreator, Scanner sc) {
        boolean inDashboard = true;
        while (inDashboard) {
            System.out.println("\n--- DASHBOARD (" + role.toUpperCase() + ") ---");
            System.out.println("1. View Roster\n2. Enroll in Session\n3. Management (Admin Only)\n4. Logout\n5. Exit System");
            System.out.print("Choice: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    if (role.equalsIgnoreCase("Admin")) displayAthleteTable(db.getAllAthletes());
                    else System.out.println("Access Denied.");
                    break;
                case "2":
                    handleEnrollmentUI(enroller, sc);
                    break;
                case "3":
                    if (role.equalsIgnoreCase("Admin")) handleAdminTools(teamCreator, enroller, sc);
                    else System.out.println("Access Denied.");
                    break;
                case "4": inDashboard = false; break;
                case "5": System.exit(0); break;
            }
        }
    }

    private static void handleAdminTools(FormTeam teamCreator, EnrollAthlete enroller, Scanner sc) {
        boolean inTools = true;
        while (inTools) {
            System.out.println("\n--- ADMIN TOOLS ---");
            System.out.println("1. Form Team\n2. View Teams\n3. Create Session\n4. Back");
            System.out.print("Choice: ");
            String choice = sc.nextLine();

            if (choice.equals("1")) {
                System.out.print("Team Name: "); String n = sc.nextLine();
                System.out.print("Captain ID: "); int c = Integer.parseInt(sc.nextLine());
                System.out.print("Sport: "); String s = sc.nextLine();
                System.out.print("Max Size: "); int m = Integer.parseInt(sc.nextLine());
                teamCreator.createTeam(n, c, s, m);
            } else if (choice.equals("2")) {
                teamCreator.displayTeamRecords();
            } else if (choice.equals("3")) {
                System.out.print("Session Name: "); String sn = sc.nextLine();
                System.out.print("Status (Scheduled): "); String ss = sc.nextLine();
                System.out.print("Capacity: "); int scap = Integer.parseInt(sc.nextLine());
                enroller.createSession(sn, ss, scap);
            } else if (choice.equals("4")) inTools = false;
        }
    }

    private static void handleEnrollmentUI(EnrollAthlete enroller, Scanner sc) {
        try {
            System.out.print("Athlete ID: "); int aid = Integer.parseInt(sc.nextLine());
            System.out.print("Session ID: "); int sid = Integer.parseInt(sc.nextLine());
            System.out.println(">>> " + enroller.processEnrollment(aid, sid));
        } catch (Exception e) { System.out.println("Invalid Input."); }
    }

    private static void handleRegistration(RegisterAthlete db, Scanner sc) {
        // ... (Code from previous registration block) ...
        System.out.println("Registration Logic Executed.");
    }

    private static void displayAthleteTable(List<Athletes> athletes) {
        System.out.println("\n--- ATHLETE ROSTER ---");
        for (Athletes a : athletes) {
            System.out.printf("ID: %d | Name: %s | Sport: %s | BMI: %.2f%n", 
                a.getAthleteID(), a.getFullName(), a.getSport(), a.getBmi());
        }
    }
}