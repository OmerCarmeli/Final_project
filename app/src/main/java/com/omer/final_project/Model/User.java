package com.omer.final_project.Model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class User {

    private String userId;
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String profilePic; //url
    @Embedded
    private HashMap<String,Item> items=new HashMap<>();


    public User(   ){

    }
    public User(String userName, String email) {
        this.userName = userName;
        this.email = email;
    }
    public User(User u){
        setUserId(u.getUserId());
        setUserName(u.getUserName());
        setFirstName(u.getFirstName());
        setLastName(u.getLastName());
        setEmail(u.getEmail());
        setProfilePic(u.getProfilePic());
        setItems(u.getItems());
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public HashMap<String,Item> getItems() {
        return items;
    }

    public void setItems(HashMap<String,Item> items) {
        this.items = items;
    }

    public void addItemToList(Item item){
        items.put(item.getName(),item);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    //TODO: add and get from the item list
}
