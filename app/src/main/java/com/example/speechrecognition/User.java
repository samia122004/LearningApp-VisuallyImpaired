package com.example.speechrecognition;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private String phoneNumber;
    private String email;
    private String password;

    // Constructor with default values
    public User() {
        this.name = "";
        this.phoneNumber = "";
        this.email = "";
        this.password = "";
    }

    // Constructor with parameters
    public User(String name, String phoneNumber, String email, String password) {
        this.name = name;
        this.phoneNumber = phoneNumber;
       this.email = email;
        this.password = password;
    }

    // Getter methods
    public String getName() {
        return name;
    }

   /* public String getDob() {
        return dob;
    }

    public String getGender() {
        return gender;
    }*/

    public String getPhone() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    // Setter methods (if needed)
    public void setName(String name) {
        this.name = name;
    }

   /* public void setDob(String dob) {
        this.dob = dob;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }*/

    public void setPhone(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public boolean isValidPhoneNumber() {
        // Add your phone number validation logic here
        return phoneNumber.matches("\\d{10}"); // Example: Validates a 10-digit number
    }

   public boolean isValidEmail(String email) {
        // Add your email validation logic here
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
