package com.newbiechen.classscheduledemo.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.newbiechen.classscheduledemo.database.DbHelper;
import com.newbiechen.classscheduledemo.entity.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 2016/9/26.
 * 算作工具类，所以应该使用单例模式
 */
public class StuCourseDao {
    private SQLiteDatabase mDataBase;
    private static StuCourseDao sStuCourseDao;

    private StuCourseDao(Context context){
        mDataBase = new DbHelper(context).getWritableDatabase();
    }

    public static StuCourseDao getInstance (Context context){
        synchronized (StuCourseDao.class){
            if (sStuCourseDao == null){
                sStuCourseDao = new StuCourseDao(context);
            }
        }
        return sStuCourseDao;
    }

    public void saveStuCls(Course course){
        ContentValues values = new ContentValues();
        values.put(DbHelper.STUCLS_NAME_COL, course.getClsName());
        values.put(DbHelper.STUCLS_DAY_COL, course.getDay());
        values.put(DbHelper.STUCLS_NUM_COL, course.getClsNum());
        values.put(DbHelper.STUCLS_COUNT_COL, course.getClsCount());
        values.put(DbHelper.STUCLS_COLOR_COL, course.getColor());
        //当相同的时候替换。
        mDataBase.insertWithOnConflict(DbHelper.TABLE_STUCLS,null,values,SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void saveStuClsList(List<Course> courses){
        for (Course course : courses){
            saveStuCls(course);
        }
    }

    public List<Course> getStuClsList(){
        List<Course> courses = new ArrayList<>();
        Cursor cursor = mDataBase.rawQuery("select * from "+DbHelper.TABLE_STUCLS,null);
        while (cursor.moveToNext()){
            Course course = new Course();
            course.setClsName(cursor.getString(
                    cursor.getColumnIndex(DbHelper.STUCLS_NAME_COL))
            );
            course.setClsNum(cursor.getInt(
                    cursor.getColumnIndex(DbHelper.STUCLS_NUM_COL)
            ));
            course.setDay(cursor.getInt(
                    cursor.getColumnIndex(DbHelper.STUCLS_DAY_COL)
            ));
            course.setClsCount(cursor.getInt(
                    cursor.getColumnIndex(DbHelper.STUCLS_COUNT_COL)
            ));
            course.setColor(cursor.getInt(
                    cursor.getColumnIndex(DbHelper.STUCLS_COLOR_COL)
            ));
            courses.add(course);
        }
        cursor.close();
        return courses;
    }

    public void removeAll(){
        //删除全部记录
        mDataBase.delete(DbHelper.TABLE_STUCLS,null,null);
    }
}
