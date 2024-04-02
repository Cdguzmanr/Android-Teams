package edu.fvtc.teams;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class TeamsDataSource {
    SQLiteDatabase database;
    DatabaseHelper dbHelper;
    public static final String TAG = "TeamsDataSource";

    public TeamsDataSource(Context context)
    {
        dbHelper = new DatabaseHelper(context,
                DatabaseHelper.DATABASE_NAME,
                null,
                DatabaseHelper.DATABASE_VERSION);
    }

    public void open() throws SQLException{
        open(false);
    }

    public void open(boolean refresh)  throws SQLException{

        database = dbHelper.getWritableDatabase();
        Log.d(TAG, "open: " + database.isOpen());
        if(refresh) refreshData();
    }

    public void close()
    {
        dbHelper.close();
    }

    public void refreshData()
    {
        Log.d(TAG, "refreshData: Start");
        ArrayList<Team> teams = new ArrayList<Team>();

        teams.add(new Team(1, "Packers", "Green Bay","9205551234", 1, true, R.drawable.packers ));
        teams.add(new Team(2, "Lions", "Detroit","9204441234", 2, false, R.drawable.lions ));
        teams.add(new Team(3, "Vikings", "Minneapolis","9203331234", 4, false, R.drawable.vikings ));
        teams.add(new Team(4, "Bears", "Chicago","9202221234", 4, false, R.drawable.bears ));

        // Delete and reinsert all the teams
        int results = 0;
        for(Team team : teams){
            results += insert(team);
        }
        Log.d(TAG, "refreshData: End: " + results + " rows...");
    }

    public Team get(int id)
    {
        return new Team();
    }

    public ArrayList<Team> get()
    {
        Log.d(TAG, "get: Start");
        ArrayList<Team> teams = new ArrayList<Team>();

        try {
            String sql = "SELECT * from tblTeam";
            Cursor cursor = database.rawQuery(sql, null);
            Team team;
            cursor.moveToFirst();

            while(!cursor.isAfterLast())
            {
                team = new Team();
                team.setId(cursor.getInt(0));
                team.setName(cursor.getString(1));
                team.setCity(cursor.getString(2));
                team.setImgId(cursor.getInt(3));
                Boolean fav = cursor.getInt(4) == 1;
                team.setIsFavorite(fav);
                team.setRating(cursor.getFloat(5));
                team.setCellPhone(cursor.getString(6));

                if(team.getImgId() == 0)
                    team.setImgId(R.drawable.photoicon);

                teams.add(team);
                Log.d(TAG, "get: " + team.toString());
                cursor.moveToNext();
            }

        }
        catch(Exception e)
        {
            Log.d(TAG, "get: " + e.getMessage());
            e.printStackTrace();
        }

        return teams;
    }

    public int deleteAll()
    {
        return 0;
    }

    public int delete(int id)
    {
        return 0;
    }

    public int getNewId()
    {
        int newId =-1;
        try{
            // Get the highest id in the table and add 1
            String sql = "SELECT max(id) from tblTeam";
            Cursor cursor = database.rawQuery(sql, null);
            cursor.moveToFirst();
            newId = cursor.getInt(0) + 1;
            cursor.close();
        }
        catch(Exception e)
        {

        }
        return newId;
    }

    public int insert(Team team)
    {
        return 0;
    }
    public int update(Team team)
    {
        return 0;
    }
}

