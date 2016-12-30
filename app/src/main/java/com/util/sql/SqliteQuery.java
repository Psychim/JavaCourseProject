package com.util.sql;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorJoiner;
import android.database.sqlite.SQLiteDatabase;

import com.util.coursetable.Course;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by aiocac on 2016/12/23.
 */

/**
 * 处理sqlite的query
 */
public class SqliteQuery {
    protected Context context;
    protected SQLiteDatabase db;
    protected SqliteQuery(Context pContext){
        context=pContext;
        db=null;
    }

    /**
     * TODO 检查表是否存在
     */
    public boolean isTableExist(String tableName){
        boolean result=false;
        if(tableName==null||db==null||!db.isOpen())
            return false;
        String query="SELECT * FROM sqlite_master WHERE name=?";
        Cursor cursor=db.rawQuery(query,new String[]{tableName});
        if(cursor.getCount()>0)
            result=true;
        cursor.close();
        return result;
    }
}
