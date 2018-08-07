package com.omer.final_project.Model;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import java.util.List;

@Dao
public interface ItemDao {
    @Query("select * from Item")
    List<Item> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Item... items);

    @Delete
    void delete(Item items);


}
