package com.example.manageinventory.models;

import javax.persistence.*;

/**
 * Created by pana on 24/01/20.
 */
@Entity(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @Column(name = "type")
    private UserType type;
    @Column(name = "name")
    private String name;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "user_code")
    private String userCode;
    @Column(name = "is_active")
    private Boolean active;
    @Column(name = "contact_number")
    private String contactNumber;
    @Column(name = "display_name")
    private String displayName;

    public User() {
    }

    public User(UserType type, String name, String email, String userCode, Boolean active, String contactNumber, String displayName) {
        this.type = type;
        this.name = name;
        this.email = email;
        this.userCode = userCode;
        this.active = active;
        this.contactNumber = contactNumber;
        this.displayName = displayName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
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
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", userCode='" + userCode + '\'' +
                ", isActive=" + active +
                ", contactNumber='" + contactNumber + '\'' +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
