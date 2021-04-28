package com.zlock.zlock.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.zlock.zlock.DataModel.Credentials;

import java.util.ArrayList;
import java.util.List;

public class CreHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "credentials.db";
    private static final int SCHEMA = 1;
    private static final String TABLE_NAME = "credential";


    public static final String _ID = "_id";
    public static final String COL_ACCOUNT = "account_name";
    public static final String COL_USERNAME = "username";
    public static final String COL_PASSWORD = "password";
    public static final String COL_USER_ID = "user_id";


    public CreHelper(@Nullable Context context) {

        super(context, DATABASE_NAME, null, SCHEMA);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_ACCOUNT + " TEXT NOT NULL, " +
                COL_USERNAME + " TEXT NOT NULL, " +
                COL_PASSWORD + " TEXT NOT NULL, " +
                COL_USER_ID + " INTEGER NOT NULL " +
                ");";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insert(Credentials credentials){

        final ContentValues user1 = new ContentValues();

        user1.put(COL_ACCOUNT, credentials.getAccount());
        user1.put(COL_USERNAME, credentials.getUsername());
        user1.put(COL_PASSWORD, credentials.getPassword());
        user1.put(COL_USER_ID, credentials.getUser_id());
        try {
            this.getWritableDatabase().insert(TABLE_NAME, null, user1);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    public List<Credentials> ReadData() {

        List<Credentials> data = new ArrayList<>();
        Cursor c = null;
        c = getReadableDatabase().query(TABLE_NAME, null, null, null, null, null, null);
        if (c.moveToFirst()){
            do {
                Credentials credentials = new Credentials();
                credentials.setId(c.getInt(c.getColumnIndex(_ID)));
                credentials.setAccount(c.getString(c.getColumnIndex(COL_ACCOUNT)));
                credentials.setUsername(c.getString(c.getColumnIndex(COL_USERNAME)));
                credentials.setPassword(c.getString(c.getColumnIndex(COL_PASSWORD)));
                credentials.setUser_id(c.getInt(c.getColumnIndex(COL_USER_ID)));

                data.add(credentials);
            }while(c.moveToNext());
        }
        c.close();
        return  data;
    }


    public void UpdateData(Credentials credentials, int id){

        ContentValues cv = new ContentValues();
        cv.put(COL_ACCOUNT, credentials.getAccount());
        cv.put(COL_USERNAME, credentials.getUsername());
        cv.put(COL_PASSWORD, credentials.getPassword());
        cv.put(COL_USER_ID, credentials.getUser_id());
        this.getWritableDatabase().update(TABLE_NAME, cv, _ID + "=" + id, null);
    }

    public void DeleteData(int id){
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "+ _ID + "=\"" + id + "\";";
        this.getWritableDatabase().execSQL(query);
        this.getWritableDatabase().close();
    }

}
