package com.example.mymoviesapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.mymoviesapp.model.Movie;

@Database(entities = {Movie.class},version = 1,exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {
    public abstract MovieDAO movieDAO();
    public static MovieDatabase INSTANCE;
    public static Context context;

    public static MovieDatabase getDatabase(Context ctx){
        context = ctx;
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context,MovieDatabase.class,"myDb")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance(){
        INSTANCE = null;
    }
}
