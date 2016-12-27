package com.util.sql;

import android.content.Context;
import android.database.Cursor;

import com.util.coursetable.Arrangement;
import com.util.coursetable.Course;
import com.util.coursetable.CourseInstance;
import com.util.coursetable.CourseTable;
import com.util.sql.SqliteManager;
import com.util.sql.SqliteQuery;
import com.util.Constant.*;

/**
 * Created by aiocac on 2016/12/23.
 */

public class StudentQuery extends SqliteQuery{
    final private static String DBName="student.db";
    /**
     * COLUMN NAMES
     */
    final private static String COURSE_NAME="COURSE_NAME";
    final private static String TEACHER="TEACHER";
    final private static String COURSE_ID="COURSE_ID";
    final private static String CREDIT="CREDIT";
    final private static String WEEK_START="WEEK_START";
    final private static String WEEK_END="WEEK_END";
    final private static String STUCARD_ID="STUCARD_ID";
    final private static String WEEKDAY="WEEKDAY";
    final private static String SEG_START="SEG_START";
    final private static String SEG_END="SEG_END";
    final private static String CLASSROOM="CLASSROOM";
    final private static String ACADEMICYEAR="ACADEMIC_YEAR";
    public StudentQuery(Context pContext) {
        super(pContext);
        db= SqliteManager.getInstance().getConnection(context.getFilesDir().getAbsolutePath()+"/"+DBName);
        TryCreateTable();
    }
    private void TryCreateTable(){
        /*if(isTableExist("COURSEINSTANCE")){
            db.execSQL("DROP TABLE COURSEINSTANCE");
        }*/
        if (!isTableExist("COURSEINSTANCE")) {
            String query = "CREATE TABLE COURSEINSTANCE" +
                    "("+COURSE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COURSE_NAME+" TEXT NOT NULL," +
                    TEACHER+" TEXT NOT NULL," +
                    CREDIT+" REAL NOT NULL," +
                    WEEK_START+" INTEGER NOT NULL," +
                    WEEK_END+" INTEGER NOT NULL,"+
                    WEEKDAY+" VARCHAR(10) NOT NULL,"+
                    SEG_START+" INTEGER NOT NULL,"+
                    SEG_END+" INTEGER NOT NULL,"+
                    CLASSROOM+" TEXT NOT NULL,"+
                    STUCARD_ID+" CHARACTER(9) NOT NULL," +
                    ACADEMICYEAR+" CHARCATER(7) NOT NULL)";
            db.execSQL(query);
        }
    }
    public boolean isCourseInstanceExist(CourseInstance ci,String stuID,String acaYear){
        boolean result=false;
        String query = "SELECT * FROM COURSEINSTANCE WHERE "+COURSE_NAME+"=?"
                + " AND "+WEEKDAY+"=?"+
                " AND "+SEG_START+"=?"+" AND "+STUCARD_ID+"=?"+" AND "+
                ACADEMICYEAR +"=?";
        Cursor cursor = db.rawQuery(query,new String[]{ci.C.getCoursename(),ci.A.getWeekday().name(),
                Integer.toString(ci.A.getSegStart()),stuID,acaYear});
        if(cursor.getCount()>0)
            result=true;
        return result;
    }
    public void DeleteCourseInstance(CourseInstance ci,String stuID,String acaYear){
        String query = "DELETE FROM COURSEINSTANCE WHERE "+COURSE_NAME+"=?"
                + " AND "+WEEKDAY+"=?"+
                " AND "+SEG_START+"=?"+" AND "+STUCARD_ID+"=?"+" AND "
                +ACADEMICYEAR +"=?";
        db.execSQL(query,new String[]{ci.C.getCoursename(),ci.A.getWeekday().name(),
        Integer.toString(ci.A.getSegStart()),stuID,acaYear});
    }
    public void InsertCourseInstance(CourseInstance ci,String stuID,String acaYear){
        String query = "INSERT INTO COURSEINSTANCE "+
                "("+COURSE_NAME+','+TEACHER+','+CREDIT+','+WEEK_START+','+
                WEEK_END+','+WEEKDAY+','+SEG_START+','+SEG_END+','+CLASSROOM+
                ','+STUCARD_ID+','+ACADEMICYEAR+")"+
                "VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        db.execSQL(query,new String[]{ci.C.getCoursename(),ci.C.getTeacher(),Double.toString(ci.C.getCredit()),
            Integer.toString(ci.C.getWeekStart()),Integer.toString(ci.C.getWeekEnd()),
            ci.A.getWeekday().name(),Integer.toString(ci.A.getSegStart()),Integer.toString(ci.A.getSegEnd()),
            ci.A.getClassroom(),stuID,acaYear});
    }
    public CourseTable getCourseTable(String stuID,String acaYear){
        CourseTable ct=null;
        Course co=null;
        Arrangement ar=null;
        String query="SELECT * FROM COURSEINSTANCE WHERE "+STUCARD_ID+"=?"+" AND "+ACADEMICYEAR+"=?";
        Cursor cursor=db.rawQuery(query,new String[]{stuID,acaYear});
        if(cursor.getCount()>0){
            ct=new CourseTable();
            cursor.moveToFirst();
            do{
                co=new Course();
                ar=new Arrangement();
                co.setCoursename(cursor.getString(cursor.getColumnIndex(COURSE_NAME)));
                co.setTeacher(cursor.getString(cursor.getColumnIndex(TEACHER)));
                co.setWeek(cursor.getInt(cursor.getColumnIndex(WEEK_START)),
                        cursor.getInt(cursor.getColumnIndex(WEEK_END)));
                co.setCredit(cursor.getDouble(cursor.getColumnIndex(CREDIT)));
                ar.setClassroom(cursor.getString(cursor.getColumnIndex(CLASSROOM)));
                ar.setSegment(cursor.getInt(cursor.getColumnIndex(SEG_START)),
                        cursor.getInt(cursor.getColumnIndex(SEG_END)));
                ar.setWeekday(_WEEKDAY.valueOf(cursor.getString(cursor.getColumnIndex(WEEKDAY))));
                ct.add(new CourseInstance(co,ar));
            }while(cursor.moveToNext());
            ct.setStuID(stuID);
            ct.setAcaYear(acaYear);
        }
        return ct;
    }
    public void SaveCourseTable(CourseTable ct){
        String stuID=ct.getStuID();
        String acaYear=ct.getAcaYear();
        String query="DELETE FROM COURSEINSTANCE WHERE "+STUCARD_ID+"=?"+" AND "+ACADEMICYEAR+"=?";
        db.execSQL(query,new String[]{stuID,acaYear});
        for (CourseInstance ci : ct) {
            InsertCourseInstance(ci, stuID,acaYear);
        }
    }
}
