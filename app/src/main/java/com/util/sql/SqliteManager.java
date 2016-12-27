package com.util.sql;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by aiocac on 2016/12/23.
 */

public class SqliteManager {
    private static SqliteManager instance=null;
    private Map<String,SQLiteDatabase> dbs;
    public SQLiteDatabase getConnection(String databasename){
        SQLiteDatabase db;
        if(databasename==null)
            return null;
        else if(dbs.containsKey(databasename)){
            return dbs.get(databasename);
        }
        db=SQLiteDatabase.openOrCreateDatabase(databasename, null);
        dbs.put(databasename,db);
        System.out.println("Opened database "+databasename+" successfullly");
        return db;
    }
    public static SqliteManager getInstance(){
        synchronized (SqliteManager.class){
            if(instance==null){
                instance=new SqliteManager();
            }
        }
        return instance;
    }
    private SqliteManager(){
        dbs = new HashMap<String, SQLiteDatabase>();
    }
    @Override
    protected void finalize(){
        try {
            for(String databasename: dbs.keySet()){
                dbs.get(databasename).close();
            }
            super.finalize();
        }catch(Throwable e){
            System.out.println(e.getClass().getName()+": "+e.getMessage());
            e.printStackTrace();
        }

    }
}
