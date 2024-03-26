package edu.fvtc.teams;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.util.ArrayList;

public class TeamsListActivity extends AppCompatActivity {
    public static final String TAG = "TeamsListActivity";
    public static final String FILENAME = "teams.txt";
    ArrayList<Team> teams;
    RecyclerView teamList;
    TeamsAdapter teamsAdapter;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) v.getTag();
            int position = viewHolder.getAdapterPosition();
            Team team = teams.get(position);
            Log.d(TAG, "onClick: " + team.getName());
            Intent intent = new Intent(TeamsListActivity.this, TeamsEditActivity.class);
            intent.putExtra("teamid", team.getId());
            Log.d(TAG, "onClick: " + team.getId());
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teams_list);

        Navbar.initListButton(this);
        Navbar.initSettingsButton(this);
        Navbar.initMapButton(this);
        this.setTitle("List");
        teams = new ArrayList<Team>();

        teams = readTeams(this);
        if(teams.size() == 0)
            createTeams();

        RebindTeams();
    }

    private void RebindTeams() {
        // Rebind the RecyclerView
        Log.d(TAG, "RebindTeams: Start");
        teamList = findViewById(R.id.rvTeams);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        teamList.setLayoutManager(layoutManager);
        teamsAdapter = new TeamsAdapter(teams, this);
        teamsAdapter.setOnItemClickListener(onClickListener);
        teamList.setAdapter(teamsAdapter);

    }

    private void createTeams() {
        teams = new ArrayList<Team>();

        teams.add(new Team(1, "Packers", "Green Bay","9205551234", 1, true, R.drawable.packers ));
        teams.add(new Team(2, "Lions", "Detroit","9204441234", 2, false, R.drawable.lions ));
        teams.add(new Team(3, "Vikings", "Minneapolis","9203331234", 4, false, R.drawable.vikings ));
        teams.add(new Team(4, "Bears", "Chicago","9202221234", 4, false, R.drawable.bears ));

        FileIO.writeFile(FILENAME, this, createDataArray(teams));
        teams = readTeams(this);
    }

    public static ArrayList<Team> readTeams(AppCompatActivity activity) {
        ArrayList<String> strData = FileIO.readFile(FILENAME, activity);
        ArrayList<Team> teams1 = new ArrayList<Team>();

        for (String s : strData){
            Log.d(TAG, "readTeams: " + s);
            String[] data = s.split("\\|");
            teams1.add(new Team(
               Integer.parseInt(data[0]),
                data[1],
                data[2],
                data[3],
                Float.parseFloat(data[4]),
                Boolean.parseBoolean(data[5]),
                Integer.parseInt(data[6])
            ));
        }
        Log.d(TAG, "readTeams: " + teams1.size());
        return teams1;
    }

    public static String[] createDataArray(ArrayList<Team> teams){
        String[] teamData = new String[teams.size()];

        for (int count=0; count < teams.size(); count++){
            teamData[count] = teams.get(count).toString();
        }
        return teamData;
    }
}