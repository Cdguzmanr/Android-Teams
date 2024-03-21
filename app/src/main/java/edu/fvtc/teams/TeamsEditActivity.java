package edu.fvtc.teams;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class TeamsEditActivity extends AppCompatActivity {
    public static final String TAG = TeamsEditActivity.class.toString();
    Team team;
    boolean loading = true;
    int teamId = -1;

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

        }
        else {
            team = new Team();
        }

        initRatingButton();

        Log.d(TAG, "onCreate: End");
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
}