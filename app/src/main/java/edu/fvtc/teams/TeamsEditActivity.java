package edu.fvtc.teams;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class TeamsEditActivity extends AppCompatActivity implements RaterDialog.SaveRatingListener {
    public static final String TAG = TeamsEditActivity.class.toString();
    Team team;
    boolean loading = true;
    int teamId = -1;
    ArrayList<Team> teams;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teams_edit);

        Log.d(TAG, "onCreate: Start");

        Bundle extras = getIntent().getExtras();
        teamId = extras.getInt("teamid");

        this.setTitle("Team: " + teamId);

        if(teamId != -1)
        {
            // Get the team
            initTeam(teamId-1);

        }
        else {
            team = new Team();
        }
        Navbar.initListButton(this);
        Navbar.initSettingsButton(this);
        Navbar.initMapButton(this);

        initRatingButton();
        initToggleButton();
        initSaveButton();
        initTextChanged(R.id.etName);
        initTextChanged(R.id.etCity);
        initTextChanged(R.id.editCell);

        setForEditting(false);
        Log.d(TAG, "onCreate: End");
    }

    private void initSaveButton() {
        Button btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (teamId == -1){
                    teams.add(team);
                } else {
                    teams.set(teamId-1,team);
                }
                FileIO.writeFile(TeamsListActivity.FILENAME,
                        TeamsEditActivity.this,
                        TeamsListActivity.createDataArray(teams));
            }
        });
    }

    private void initTextChanged(int controlId){
        EditText editText = findViewById(controlId);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                team.setControlText(controlId, s.toString());
            }
        });
    }

    private void initToggleButton() {
        ToggleButton toggleButton = findViewById(R.id.toggleButtonEdit);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setForEditting(toggleButton.isChecked());
            }


        });
    }

    private void setForEditting(boolean checked) {
        EditText editName = findViewById(R.id.etName);
        EditText editCity = findViewById(R.id.etCity);
        EditText editCellPhone = findViewById(R.id.editCell);
        TextView editRating = findViewById(R.id.btnRating);


        editName.setEnabled(checked);
        editCity.setEnabled(checked);
        editCellPhone.setEnabled(checked);
        editRating.setEnabled(checked);

        if (checked){
            // Set focus to the edit name
            editName.requestFocus();
        } else {
            ScrollView scrollView = findViewById(R.id.scrollView);
            scrollView.fullScroll(ScrollView.FOCUS_UP);
        }
    }

    private void initTeam(int teamId) {
        // Get the teams
        teams = TeamsListActivity.readTeams(this);

        // Get the team
        team = teams.get(teamId);
        rebindTeam();
    }

    private void rebindTeam() {
        EditText editName = findViewById(R.id.etName);
        EditText editCity = findViewById(R.id.etCity);
        EditText editCellPhone = findViewById(R.id.editCell);
        TextView editRating = findViewById(R.id.txtRating);

        editName.setText(team.getName());
        editCity.setText(team.getCity());
        editCellPhone.setText(team.getCellPhone());
        editRating.setText(String.valueOf(R.id.ratingBar)); // <<-----------
    }

    private void initRatingButton()
    {
        Button btnRating = findViewById(R.id.btnRating);

        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                RaterDialog raterDialog = new RaterDialog(team.getRating());
                raterDialog.show(fragmentManager, "Rate Team");
            }
        });
    }

    @Override
    public void didFinishTeamRaterDialog(float rating) {
        Log.d(TAG, "didFinishTeamRaterDialog: " + rating);
        TextView txtRating = findViewById(R.id.txtRating);
        txtRating.setText(String.valueOf(rating));
        team.setRating(rating);
    }
}