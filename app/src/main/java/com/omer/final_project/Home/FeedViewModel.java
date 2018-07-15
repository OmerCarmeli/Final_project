package com.omer.final_project.Home;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.omer.final_project.Model.Model;
import com.omer.final_project.Model.Post;

import java.util.List;

public class FeedViewModel extends ViewModel {

    LiveData<List<Post>> data;

    public LiveData<List<Post>> getData(){
        data = Model.instance.getAllPosts();
        return data;
    }
}
