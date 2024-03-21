package edu.fvtc.teams;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TAG = "DatabaseHelper";
    public static final int DATABASE_VERSION = 1;
    public DatabaseHelper(@Nullable Context context,
                          @Nullable String name,
                          @Nullable SQLiteDatabase.CursorFactory factory,
                          int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql;

        sql ="CREATE TABLE IF NOT EXISTS tblItem (Id integer primary key autoincrement, FirstName text, LastName text);";
        Log.d(TAG, "onCreate: " + sql);
        // Create the table
        db.execSQL(sql);

        // Insert an item.
        sql = "INSERT INTO tblItem VALUES (1, 'Jennifer', 'Aniston');";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade: ");
    }
}
