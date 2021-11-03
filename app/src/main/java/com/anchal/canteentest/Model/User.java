package com.anchal.canteentest.Model;

// We will store our user's information using this class.

public class User {
    private String Name;
    private String Password;
    private String Phone;
    private String IsStaff;
    private String Mail;
    public User() {

    }

    public String getIsStaff() {
        return IsStaff;
    }

    public void setIsStaff(String isStaff) {
        IsStaff = isStaff;
    }

    public User(String name, String password, String mail) {
        Name = name;
        Password = password;
        Mail = mail;
        IsStaff = "false";
    }

    public String getMail() {
        return Mail;
    }

    public void setMail(String mail) {
        Mail = mail;
    }

    public String getPhoneNo() {
        return Phone;
    }

    public void setPhoneNo(String phone) {
        Phone = phone;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPassword() {
        return Password;
    }
}
