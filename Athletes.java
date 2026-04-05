package com.mycompany.sportstrainingacademy;

/**
 * @author james paul
 */
public class Athletes {

    // Private attributes (Fields)
    private int athleteID;
    private String firstName;
    private String lastName;
    private int age;
    private String sport;
    private double bmi;

    // Constructor
    public Athletes(int athleteID, String firstName, String lastName, int age, String sport, double bmi) {
        this.athleteID = athleteID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.sport = sport;
        this.bmi = bmi;
    }

    // Getters
    public int getAthleteID() { return athleteID; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public int getAge() { return age; }
    public String getSport() { return sport; }
    public double getBmi() { return bmi; }

    // Setters
    public void setAthleteID(int athleteID) { this.athleteID = athleteID; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setAge(int age) { this.age = age; }
    public void setSport(String sport) { this.sport = sport; }
    public void setBmi(double bmi) { this.bmi = bmi; }
    
    // Helper method to get the full name easily
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
