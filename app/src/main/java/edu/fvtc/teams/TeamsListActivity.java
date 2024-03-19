package edu.fvtc.teams;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class TeamsListActivity extends AppCompatActivity {

    public static final String TAG = "TeamsListActivity";
    public static final String FILENAME = "teams.txt";
    ArrayList<Team> teams;
    RecyclerView teamList;
    TeamsAdapter teamsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teams_list);

        Navbar.initListButton(this);
        Navbar.initSettingsButton(this);
        Navbar.initMapButton(this);
        this.setTitle("List");

        if (teams.size() == 0){
            createTeams();
        }

        RebindTeams();

    }

    private void RebindTeams() {
        // Rebind the RecyclerView

        teamList = findViewById(R.id.rvTeams);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        teamList.setLayoutManager(layoutManager);
        teamsAdapter = new TeamsAdapter(teams, this);
        teamList.setAdapter(teamsAdapter);
    }

    private void createTeams() {
        teams = new ArrayList<Team>();

        teams.add(new Team(1, "Packers", "Green Bay","9205551234", 1, true, R.drawable.packers ));
        teams.add(new Team(2, "Lions", "Detroit","9204441234", 2, false, R.drawable.lions ));
        teams.add(new Team(3, "Vikings", "Minneapolis","9203331234", 4, false, R.drawable.vikings ));
        teams.add(new Team(4, "Bears", "Chicago","9202221234", 4, false, R.drawable.bears ));
    }
}