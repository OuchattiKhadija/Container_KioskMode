package com.onblock.myapp.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.onblock.myapp.data.dao.AppInfoDao;
import com.onblock.myapp.data.dao.GrpUserDao;
import com.onblock.myapp.data.dao.UserGrpDao;
import com.onblock.myapp.data.model.AppInfo;
import com.onblock.myapp.data.model.Group;
import com.onblock.myapp.data.model.User;
import com.onblock.myapp.data.model.relations.AppGroupCrossRef;

import org.jetbrains.annotations.NotNull;

//@Database(entities = {AppInfo.class, GroupModel.class, AppGroupCrossRef.class}, version = 2)
@Database(
        entities = {AppInfo.class, Group.class, User.class, AppGroupCrossRef.class},
        version = 3)
public abstract class AppDb extends RoomDatabase {

    private static AppDb instance;

    public abstract AppInfoDao appInfoDao();
    public abstract UserGrpDao userGrpDao();
    public abstract GrpUserDao grpUserDao();


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


