package edu.fvtc.teams;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

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

    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            Log.d(TAG, "onCheckedChanged: " + isChecked);
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) buttonView.getTag();
            int position = viewHolder.getAdapterPosition();
            teams.get(position).setIsFavorite(isChecked);
            TeamsDataSource ds = new TeamsDataSource(TeamsListActivity.this);
            ds.update(teams.get(position));

            //FileIO.writeFile(TeamsListActivity.FILENAME,
            //        TeamsListActivity.this,
            //        createDataArray(teams));
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

        initDatabase();

        //teams = readTeams(this);
        if(teams.size() == 0) {
            createTeams();
        }

        initDeleteSwitch();
        initAddTeamButton();
        RebindTeams();
    }

    private void initDatabase() {
        TeamsDataSource ds = new TeamsDataSource(this);
        ds.open(false);
        teams = ds.get();
        Log.d(TAG, "initDatabase: Teams: " + teams.size());
    }


    private void initAddTeamButton() {
        Button btnAddTeam = findViewById(R.id.btnAddTeam);
        btnAddTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeamsListActivity.this, TeamsEditActivity.class);
                intent.putExtra("teamid", -1);
                Log.d(TAG, "onClick: ");
                startActivity(intent);
            }
        });
    }

    private void initDeleteSwitch() {
        SwitchCompat switchDelete = findViewById(R.id.switchDelete);
        switchDelete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "onCheckedChanged: " + isChecked);
                teamsAdapter.setDelete(isChecked);
                teamsAdapter.notifyDataSetChanged();
            }
        });
    }

    private void RebindTeams() {
        // Rebind the RecyclerView
        Log.d(TAG, "RebindTeams: Start");
        teamList = findViewById(R.id.rvTeams);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        teamList.setLayoutManager(layoutManager);
        teamsAdapter = new TeamsAdapter(teams, this);
        teamsAdapter.setOnItemClickListener(onClickListener);
        teamsAdapter.setOnItemCheckedChangeListener(onCheckedChangeListener);
        teamList.setAdapter(teamsAdapter);

    }


    private void createTeams() {
        Log.d(TAG, "createTeams: Start");
        teams = new ArrayList<Team>();

        //teams.add(new Team(1, "Packers", "Green Bay","9205551234", 1, true, R.drawable.packers ));
        //teams.add(new Team(2, "Lions", "Detroit","9204441234", 2, false, R.drawable.lions ));
        //teams.add(new Team(3, "Vikings", "Minneapolis","9203331234", 4, false, R.drawable.vikings ));
        //teams.add(new Team(4, "Bears", "Chicago","9202221234", 4, false, R.drawable.bears ));

        //FileIO.writeFile(FILENAME, this, createDataArray(teams));
        //teams = readTeams(this);

        TeamsDataSource ds = new TeamsDataSource(TeamsListActivity.this);
        ds.open(true);
        teams = ds.get();

        Log.d(TAG, "createTeams: End: " + teams.size());
    }

    public static ArrayList<Team> readTeams(AppCompatActivity activity) {
        ArrayList<String> strData = FileIO.readFile(FILENAME, activity);
        ArrayList<Team> teams = new ArrayList<Team>();

        for(String s : strData)
        {
            Log.d(TAG, "readTeams: " + s);
            String[] data = s.split("\\|");
            teams.add(new Team(
                    Integer.parseInt(data[0]),
                    data[1],
                    data[2],
                    data[3],
                    Float.parseFloat(data[4]),
                    Boolean.parseBoolean(data[5]),
                    Integer.parseInt(data[6])
            ));
        }
        Log.d(TAG, "readTeams: " + teams.size());
        return teams;
    }

    public static String[] createDataArray(ArrayList<Team> teams)
    {
        String[] teamData = new String[teams.size()];
        for (int count = 0; count < teams.size(); count++)
        {
            teamData[count] = teams.get(count).toString();
        }
        return teamData;
    }
}