package com.omer.final_project.Model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.widget.Button;

import java.time.ZonedDateTime;
import java.util.Date;

@Entity
public class Post {
    @PrimaryKey
    @NonNull
    public String postId;

  //  @Embedded
    //private User user;
    String userId;
    @Embedded
    private Item item;

    public String postName;


    public Post(String userId,Item item  ){
        this.item=item;
        this.userId=userId;
        this.postName=item.getName();
        this.postId=postName+ new Date().toString();
    }
    public Post(){

    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }


    //public User getUser() { return user;}

    //public void setUser(User user) {this.user = user;}

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
