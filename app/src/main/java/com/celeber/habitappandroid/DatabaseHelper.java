package com.celeber.habitappandroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by alinemokfa on 20/11/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "bans.db";
    public static final String TABLE_NAME = "bans_table";
    public static final int DATABASE_VERSION = 1;
    public static final String COL_ID = "ID";
    public static final String COL_TITLE = "TITLE";
    public static final String COL_SUBMIT = "SUBMIT";
    public static final String COL_RESIST = "RESIST";

    //when this constructor is called db will be created
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +
                " ( " + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_TITLE + " VARCHAR, " +
                COL_SUBMIT + " VARCHAR, " +
                COL_RESIST + " VARCHAR);");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean save(Ban ban) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_TITLE, ban.getTitle());
        contentValues.put(COL_SUBMIT, ban.getSubmit());
        contentValues.put(COL_RESIST, ban.getResist());

        //1 true / 0false
        long result = db.insert(TABLE_NAME, null, contentValues);

        return result != -1; //-1 is eq. to error
    }

    public ArrayList<Ban> getAllBans() {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        ArrayList<Ban> bans = new ArrayList<>();
        Ban ban;
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();

                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String submit = cursor.getString(2);
                String resist = cursor.getString(3);

                ban = new Ban(id, title, submit, resist);
                bans.add(ban);
            }
        }
        cursor.close();
        database.close();
        return bans;
    }

    public boolean updateBan(Ban ban) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID, ban.getID());
        contentValues.put(COL_TITLE, ban.getTitle());
        contentValues.put(COL_SUBMIT, ban.getSubmit());
        contentValues.put(COL_RESIST, ban.getResist());
        int val = db.update(TABLE_NAME, contentValues, "id = " + ban.getID(), null);
        return val != -1;
    }


    public void deleteBan(String ID) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_NAME, COL_ID + " = ?", new String[]{ID});
        database.close();
    }

    public void deleteAllBans() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DELETE FROM " + TABLE_NAME);
        database.close();
    }

    public Ban getBanByID(int id) {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE ID = " + id, null );
        cursor.moveToNext();

        Ban ban = new Ban();
        ban.setId(cursor.getInt(0));
        ban.setTitle(cursor.getString(1));
        ban.setSubmit(cursor.getString(2));
        ban.setResist(cursor.getString(3));

        cursor.close();
        database.close();
        return ban;
    }

}
