package com.zlock.zlock.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.zlock.zlock.DataModel.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class UserHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "user.db";
    private static final int SCHEMA = 1;
    private static final String TABLE_NAME = "users";


    public static final String _ID = "_id";
    public static final String COL_NAME = "name";
    public static final String COL_EMAIL = "email";
    public static final String COL_PASSWORD = "password";
    public static final String COL_QUES = "question";
    public static final String COL_ANS = "answer";

    public UserHelper(@Nullable Context context) {

        super(context, DATABASE_NAME, null, SCHEMA);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT NOT NULL, " +
                COL_EMAIL + " TEXT NOT NULL, " +
                COL_PASSWORD + " TEXT NOT NULL, " +
                COL_QUES + " TEXT NOT NULL, " +
                COL_ANS + " TEXT NOT NULL " +
                ");";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insert(User user){

        final ContentValues user1 = new ContentValues();

        user1.put(COL_NAME, user.getName());
        user1.put(COL_EMAIL, user.getEmail());
        user1.put(COL_PASSWORD, user.getPassword());
        user1.put(COL_QUES, user.getQues());
        user1.put(COL_ANS, user.getAns());
    try {
        this.getWritableDatabase().insert(TABLE_NAME, null, user1);
    }catch (Exception e){
        return false;
    }
        return true;
    }

    //here is the readData method which will simply get all the user data from sqlite data and return it as list
    public List<User> ReadData() {

        List<User> data = new ArrayList<>();
        Cursor c = null;
        c = getReadableDatabase().query(TABLE_NAME, null, null, null, null, null, null);
        if (c.moveToFirst()){
            do {
                User user = new User();
                user.setId(c.getInt(c.getColumnIndex(_ID)));
                user.setName(c.getString(c.getColumnIndex(COL_NAME)));
                user.setEmail(c.getString(c.getColumnIndex(COL_EMAIL)));
                user.setPassword(c.getString(c.getColumnIndex(COL_PASSWORD)));
                user.setQues(c.getString(c.getColumnIndex(COL_QUES)));
                user.setAns(c.getString(c.getColumnIndex(COL_ANS)));

                data.add(user);
            }while(c.moveToNext());
        }
        c.close();
        return  data;
    }

    public void UpdateData(User user, int id){

        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, user.getName());
        cv.put(COL_EMAIL, user.getEmail());
        cv.put(COL_PASSWORD, user.getPassword());
        cv.put(COL_QUES, user.getQues());
        cv.put(COL_ANS, user.getAns());

        this.getWritableDatabase().update(TABLE_NAME, cv, _ID + "=?", new String[]{String.valueOf(id)});
    }

    public void DeleteData(int id){
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "+ _ID + "=\"" + id + "\";";
        this.getWritableDatabase().execSQL(query);
        this.getWritableDatabase().close();
    }

}
