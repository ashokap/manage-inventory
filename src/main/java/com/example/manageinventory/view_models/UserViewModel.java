package com.example.manageinventory.view_models;

import com.example.manageinventory.models.UserType;

/**
 * Created by pana on 24/01/20.
 */

public class UserViewModel {
    private String id;
    private UserType type;
    private String name;
    private String email;
    private String password;
    private String userCode;
    private Boolean active;
    private String contactNumber;
    private String displayName;

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
        return "UserViewModel{" +
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
