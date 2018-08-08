package com.omer.final_project.Model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import com.omer.final_project.Home.HomeActivity;


@Database(entities = {Post.class,Item.class}, version = 3)
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract PostDao postDao();
    public abstract ItemDao itemDao();
}


public class AppLocalDb {
    static public AppLocalDbRepository db = Room.databaseBuilder(HomeActivity.context,
            AppLocalDbRepository.class,
            "dbFileName.db").fallbackToDestructiveMigration().build();
}
