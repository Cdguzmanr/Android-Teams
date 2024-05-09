package edu.fvtc.teams;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TeamsListActivity extends AppCompatActivity {
    public static final String TAG = "TeamsListActivity";
    public static final String FILENAME = "teams.txt";
    ArrayList<Team> teams;
    RecyclerView teamList;
    TeamsAdapter teamsAdapter;

    Comparator<Team> nameComparator = (c1, c2) -> c1.getName().compareTo(c2.getName());
    Comparator<Team> cityComparator = (c1, c2) -> c1.getCity().compareTo(c2.getCity());
    Comparator<Team> isFavoriteComparator = (c1, c2) -> (String.valueOf(c1.getIsFavorite()).compareTo(String.valueOf(c2.getIsFavorite())));

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

        //initDatabase();
        readFromAPI();

        //teams = readTeams(this);
        //if(teams.size() == 0) {
        //    createTeams();
        //}

        initDeleteSwitch();
        initAddTeamButton();
        //RebindTeams();

        // Get the battery life
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                double batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                double levelScale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
                int batteryPercent = (int)Math.floor(batteryLevel / levelScale * 100);

                TextView txtBatteryLevel = findViewById(R.id.txtBatteryLevel);
                txtBatteryLevel.setText(batteryPercent + "%");
            }
        };

        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(broadcastReceiver, filter);


    }

    private void initDatabase() {
        TeamsDataSource ds = new TeamsDataSource(this);
        ds.open(false);
        String sortBy = getSharedPreferences("teamspreferences",
                Context.MODE_PRIVATE)
                .getString("sortby", "name");
        String sortOrder = getSharedPreferences("teamspreferences",
                Context.MODE_PRIVATE)
                .getString("sortorder", "ASC");
        teams = ds.get(sortBy, sortOrder);
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
        teams = ds.get("Name", "ASC");

        Log.d(TAG, "createTeams: End: " + teams.size());
    }

    private void readFromAPI()
    {
        try{
            Log.d(TAG, "readFromAPI: Start");
            RestClient.execGetRequest(getString(R.string.api_url),
                    this,
                    new VolleyCallback() {
                        @Override
                        public void onSuccess(ArrayList<Team> result) {
                            Log.d(TAG, "onSuccess: Got Here!");
                            teams = result;
                            RebindTeams();
                        }
                    });
        }
        catch(Exception e){
            Log.e(TAG, "readFromAPI: Error: " + e.getMessage());
        }
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

    private void sortTeams(ArrayList<Team> teams)
    {
        String sortBy = getSharedPreferences("teamspreferences",
                Context.MODE_PRIVATE)
                .getString("sortfield", "name");
        String sortOrder = getSharedPreferences("teamspreferences",
                Context.MODE_PRIVATE)
                .getString("sortorder", "ASC");
        Log.d(TAG, "sortTeams: " + sortBy + ":" + sortOrder);
        if(sortOrder =="ASC") {
            if (sortBy == "name") teams.sort(nameComparator);
            if (sortBy == "city") teams.sort(cityComparator);
            if (sortBy == "isfavorite") teams.sort(isFavoriteComparator);
        }
        else {
            if (sortBy == "name") teams.sort(Collections.reverseOrder(nameComparator));
            if (sortBy == "city") teams.sort(Collections.reverseOrder(cityComparator));
            if (sortBy == "isfavorite") teams.sort(Collections.reverseOrder(isFavoriteComparator));
        }
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