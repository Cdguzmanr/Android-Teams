package edu.fvtc.teams;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TAG = "DatabaseHelper";
    public static final String DATABASE_NAME = "teams.db";

    public static final String CREATE_TEAM_SQL=
            "create table tblTeam (id integer primary key autoincrement, "
                    + "name text not null, "
                    + "city text not null, "
                    + "imgId integer not null, "
                    + "isFavorite bit, "
                    + "rating float, "
                    + "phone text not null)";
    public static final int DATABASE_VERSION = 1;
    public DatabaseHelper(@Nullable Context context,
                          @Nullable String name,
                          @Nullable SQLiteDatabase.CursorFactory factory,
                          int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the table
        Log.d(TAG, "onCreate: ");
        db.execSQL(CREATE_TEAM_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade: ");
        db.execSQL("DROP TABLE IF EXISTS tblTeam");
        onCreate(db);
    }
}
