package com.onblock.myapp.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.onblock.myapp.data.model.AppInfo;

import java.util.List;

@Dao
public interface AppInfoDao {
    /**
     * The DAO implements methods to insert, find and delete records from the App database.
     * The insertion method is passed an appInfo entity object containing the data to be stored while
     * the methods to find and delete records are passed a string containing the name of the app on which to perform the operation.
     * The getAllApps() method returns a LiveData object containing all of the records within the database.
     * This method will be used to keep the RecyclerView appInfo list in the user interface layout synchronized with the database.
     **/

    @Insert
    void insert(AppInfo appInfo);

    @Update
    void update(AppInfo appInfo);

    @Delete
    void delete(AppInfo appInfo);

    @Query("DELETE FROM appInfo_table")
    void deleteAllApps();

    @Query("SELECT * FROM appInfo_table WHERE( itCanBeOpned = 0 AND isTheContainer = 0) ORDER BY name DESC ")
    LiveData<List<AppInfo>> getAllASystempps();

    @Query("SELECT * FROM appInfo_table Where (itCanBeOpned = 1 AND isTheContainer = 0) ORDER BY name DESC")
    LiveData<List<AppInfo>> getInstalledApps();

    @Query("SELECT * FROM appInfo_table WHERE (isNormalUserAllowed = 1 AND isTheContainer = 0) ORDER BY name ")
    LiveData<List<AppInfo>> getAllGrantedApp();

    @Query("DELETE FROM appInfo_table WHERE packageName = :pn")
    void deletePackage(String pn);

    @Query("SELECT * FROM appInfo_table WHERE packageName = :pn")
    AppInfo getFromPackage(String pn);

    @Query("SELECT packageName FROM appInfo_table ")
    List<String> getAllPackages();

    @Query("SELECT * FROM appInfo_table WHERE (name LIKE  :str OR  packageName LIKE  :str) AND isTheContainer = 0 AND itCanBeOpned = 1 ")
    LiveData<List<AppInfo>> getSearchResults(String str);

    @Query("UPDATE appInfo_table SET isNormalUserAllowed = 0")
    void deniedAllApps();

    @Query("SELECT * FROM appInfo_table WHERE (name LIKE  :str OR  packageName LIKE  :str)  AND isTheContainer = 0 AND itCanBeOpned = 0 ")
    LiveData<List<AppInfo>> getSearchResultsSystemAPP(String str);

    @Query("SELECT * FROM appInfo_table WHERE name LIKE  :str  AND isNormalUserAllowed = 1  AND isTheContainer = 0")
    LiveData<List<AppInfo>> getSearchResultsForUser(String str);

    /*

     @Query("UPDATE appInfo_table SET isNormalUserAllowed = :itIs WHERE packageName = :pn ")
     void updateApp(boolean itIs , String pn);

     @Query("SELECT * FROM products WHERE productName = :name")
     List<Product> findProduct(String name);

     @Query("DELETE FROM products WHERE productName = :name")
     void deleteProduct(String name);

     @Query("SELECT * FROM products")
     LiveData<List<Product>> getAllProducts();
     */
}
