package com.omer.final_project.Profile;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import com.omer.final_project.Model.Item;
import com.omer.final_project.Model.Model;
import java.util.List;

public class ItemsViewModel extends ViewModel {
    LiveData<List<Item>> data;

    public LiveData<List<Item>> getData(){
        data = Model.instance.getMyItems();
        return data;
    }
}
