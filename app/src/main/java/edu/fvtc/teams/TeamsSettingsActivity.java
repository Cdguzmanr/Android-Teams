package edu.fvtc.teams;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class TeamsSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teams_settings);
        Navbar.initListButton(this);
        Navbar.initSettingsButton(this);
        Navbar.initMapButton(this);

        this.setTitle("Settings");
    }
}