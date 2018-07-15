package com.omer.final_project.Model;

import com.omer.final_project.Model.Post;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import java.util.List;


@Dao
public interface PostDao {

    @Query("select * from Post")
    List<Post> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Post... students);

    @Delete
    void delete(Post student);

}
