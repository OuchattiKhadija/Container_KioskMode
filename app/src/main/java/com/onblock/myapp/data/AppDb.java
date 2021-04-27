package com.onblock.myapp.data;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.onblock.myapp.data.model.AppInfo;
import com.onblock.myapp.data.model.AppInfoDao;

@Database(entities = {AppInfo.class}, version = 1)
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


