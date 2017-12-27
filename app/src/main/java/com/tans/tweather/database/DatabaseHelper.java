package com.tans.tweather.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.tans.tweather.database.bean.LocationBean;

import java.io.File;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mine on 2017/12/26.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    public DatabaseHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion) {
        super(context, databaseName, factory, databaseVersion);
    }

    public DatabaseHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion, int configFileId) {
        super(context, databaseName, factory, databaseVersion, configFileId);
    }

    public DatabaseHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion, File configFile) {
        super(context, databaseName, factory, databaseVersion, configFile);
    }

    public DatabaseHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion, InputStream stream) {
        super(context, databaseName, factory, databaseVersion, stream);
    }

    private static int VERSION = 1;
    private static String FILE_NAME = "tWeather.db";
    private static List<Class> TABLES = null;
    private static DatabaseHelper helper = null;

    static {
        if (TABLES == null)
            TABLES = new ArrayList<Class>();
        TABLES.add(LocationBean.class);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            if(TABLES != null) {
                for(Class table:TABLES)
                    TableUtils.createTableIfNotExists(connectionSource,table);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            if(TABLES != null) {
                for(Class table:TABLES)
                    TableUtils.dropTable(connectionSource,table,true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        onCreate(database,connectionSource);
    }

    public static DatabaseHelper getHelper(Context context) {
        if(helper != null)
            return helper;
        else
            helper = new DatabaseHelper(context,FILE_NAME,null,VERSION);

        return helper;
    }

}
