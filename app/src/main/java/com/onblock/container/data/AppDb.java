package com.onblock.container.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.onblock.container.data.dao.AppInfoDao;
import com.onblock.container.data.model.AppInfo;

@Database(entities = {AppInfo.class}, version = 2)
public abstract class AppDb extends RoomDatabase {
    private static AppDb instance;

    public abstract AppInfoDao appInfoDao();


    // Get a database instance &&     Create the database
    public static synchronized AppDb getDatabaseInstance(final Context context) {
        if (instance == null) {
            synchronized (AppDb.class) {
                if (instance == null) {
                    instance =
                            Room.databaseBuilder(context.getApplicationContext(),
                                    AppDb.class, "app_database")
                                    .allowMainThreadQueries()
                                    .fallbackToDestructiveMigration()
                                    .build();
                }
            }
        }
        return instance;
    }

}


