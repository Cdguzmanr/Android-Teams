package edu.fvtc.teams;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class TeamsMapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teams_map);
        Navbar.initListButton(this);
        Navbar.initSettingsButton(this);
        Navbar.initMapButton(this);

        this.setTitle("Maps");
    }
}