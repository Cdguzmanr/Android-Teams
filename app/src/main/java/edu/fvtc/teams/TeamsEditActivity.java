package edu.fvtc.teams;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class TeamsEditActivity extends AppCompatActivity implements RaterDialog.SaveRatingListener {
    public static final String TAG = TeamsEditActivity.class.toString();
    public static final int PERMISSION_REQUEST_PHONE = 102;
    public static final int PERMISSION_REQUEST_CAMERA = 103;
    public static final int CAMERA_REQUEST = 1888;
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
            initTeam(teamId);
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

        // Get the teams
        //teams = TeamsListActivity.readTeams(this);


        setForEditting(false);
        initImageButton();
        Log.d(TAG, "onCreate: End");

    }
    protected  void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA_REQUEST)
        {
            if(resultCode == RESULT_OK)
            {
                Log.d(TAG, "onActivityResult: Here");
                Bitmap photo= (Bitmap)data.getExtras().get("data");
                Bitmap scaledPhoto = Bitmap.createScaledBitmap(photo, 144, 144, true);
                ImageButton imageButton = findViewById(R.id.imageTeam);
                imageButton.setImageBitmap(scaledPhoto);
                team.setPhoto(scaledPhoto);
            }
        }
    }

    private void initImageButton() {
        ImageButton imageTeam = findViewById(R.id.imageTeam);

        imageTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT >= 23)
                {
                    // Check for the manifest permission
                    if(ContextCompat.checkSelfPermission(TeamsEditActivity.this, Manifest.permission.CAMERA) != PERMISSION_GRANTED){
                        if(ActivityCompat.shouldShowRequestPermissionRationale(TeamsEditActivity.this, Manifest.permission.CAMERA)){
                            Snackbar.make(findViewById(R.id.activity_teams_edit), "Teams requires this permission to take a photo.",
                                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Log.d(TAG, "onClick: snackBar");
                                    ActivityCompat.requestPermissions(TeamsEditActivity.this,
                                            new String[] {Manifest.permission.CAMERA},PERMISSION_REQUEST_PHONE);
                                }
                            }).show();
                        }
                        else {
                            Log.d(TAG, "onClick: ");
                            ActivityCompat.requestPermissions(TeamsEditActivity.this,
                                    new String[] {Manifest.permission.CAMERA},PERMISSION_REQUEST_PHONE);
                            takePhoto();
                        }
                    }
                    else{
                        Log.d(TAG, "onClick: ");
                        takePhoto();
                    }
                }
                else {
                    // Only rely on the previous permissions
                    takePhoto();
                }
            }
        });

    }

    private void takePhoto() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    private void readFromAPI(int teamId)
    {
        try{
            Log.d(TAG, "readFromAPI: Start");
            RestClient.execGetOneRequest(getString(R.string.api_url) + teamId,
                    this,
                    new VolleyCallback() {
                        @Override
                        public void onSuccess(ArrayList<Team> result) {
                            Log.d(TAG, "onSuccess: Got Here!");
                            team = result.get(0);
                            rebindTeam();
                        }
                    });
        }
        catch(Exception e){
            Log.e(TAG, "readFromAPI: Error: " + e.getMessage());
        }
    }
    private void initSaveButton() {
        Button btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TeamsDataSource ds = new TeamsDataSource(TeamsEditActivity.this);
                //ds.open();
                if(teamId == -1)
                {
                    Log.d(TAG, "onClick: " + team.toString());
                    //team.setId(ds.getNewId());
                    //teams.add(team);
                    //ds.insert(team);

                    RestClient.execPostRequest(team, getString(R.string.api_url),
                            TeamsEditActivity.this,
                            new VolleyCallback() {
                                @Override
                                public void onSuccess(ArrayList<Team> result) {
                                    team.setId(result.get(0).getId());
                                    Log.d(TAG, "onSuccess: Post" + team.getId());
                                }
                            });
                }
                else {
                    //teams.set(teamId, team);
                    //ds.update(team);
                    RestClient.execPutRequest(team, getString(R.string.api_url) + teamId,
                            TeamsEditActivity.this,
                            new VolleyCallback() {
                                @Override
                                public void onSuccess(ArrayList<Team> result) {
                                    Log.d(TAG, "onSuccess: Put" + team.getId());
                                }
                            });
                }
                //FileIO.writeFile(TeamsListActivity.FILENAME,
                //          TeamsEditActivity.this,
                //                 TeamsListActivity.createDataArray(teams));
            }
        });
    }

    private void initTextChanged(int controlId)
    {
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
        Button btnRating = findViewById(R.id.btnRating);

        editName.setEnabled(checked);
        editCity.setEnabled(checked);
        editCellPhone.setEnabled(checked);
        btnRating.setEnabled(checked);

        if(checked) {
            // Set Focus to the editName
            editName.requestFocus();
        }
        else {
            ScrollView scrollView = findViewById(R.id.scrollView);
            scrollView.fullScroll(ScrollView.FOCUS_UP);
        }
    }

    private void initTeam(int teamId) {

        // Get the teams
        //teams = TeamsListActivity.readTeams(this);
        // Get the team
        //team = teams.get(teamId);
        Log.d(TAG, "initTeam: " + teamId);

        //TeamsDataSource ds = new TeamsDataSource(TeamsEditActivity.this);
        //teams = ds.get();
        //team = ds.get(teamId);
        //rebindTeam();

        readFromAPI(teamId);
    }

    private void rebindTeam() {
        EditText editName = findViewById(R.id.etName);
        EditText editCity = findViewById(R.id.etCity);
        EditText editCellPhone = findViewById(R.id.editCell);
        TextView editRating = findViewById(R.id.txtRating);
        ImageButton imageButtonPhoto = findViewById(R.id.imageTeam);

        editName.setText(team.getName());
        editCity.setText(team.getCity());
        editCellPhone.setText(team.getCellPhone());
        editRating.setText(String.valueOf(team.getRating()));

        if(team.getPhoto() == null)
        {
            Log.d(TAG, "rebindTeam: Null photo");
            team.setPhoto(BitmapFactory.decodeResource(this.getResources(), R.drawable.photoicon));
        }
        imageButtonPhoto.setImageBitmap(team.getPhoto());
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