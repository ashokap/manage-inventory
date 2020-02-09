package com.example.manageinventory.models;

import javax.persistence.*;

/**
 * Created by pana on 24/01/20.
 */
@Entity(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "type")
    private Enum<UserType> type;
    @Column(name = "name")
    private String name;
    @Column(name = "email")
    private String email;
    @Column(name = "user_code")
    private String userCode;
    @Column(name = "is_active")
    private Boolean isActive;
    @Column(name = "contact_number")
    private String contactNumber;
    @Column(name = "display_name")
    private String displayName;

    public User() {
    }

    public User(Enum<UserType> type, String name, String email, String userCode, Boolean isActive, String contactNumber, String displayName) {
        this.type = type;
        this.name = name;
        this.email = email;
        this.userCode = userCode;
        this.isActive = isActive;
        this.contactNumber = contactNumber;
        this.displayName = displayName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Enum<UserType> getType() {
        return type;
    }

    public void setType(Enum<UserType> type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", userCode='" + userCode + '\'' +
                ", isActive=" + isActive +
                ", contactNumber='" + contactNumber + '\'' +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
