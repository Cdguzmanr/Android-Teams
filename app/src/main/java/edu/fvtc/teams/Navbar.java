package edu.fvtc.teams;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;

public class Navbar {
    public static void initSettingsButton(Activity activity)
    {
        ImageButton ibSettings = activity.findViewById(R.id.imageButtonSettings);
        setupListenerEvent(ibSettings, activity, TeamsSettingsActivity.class);
    }
    public static void initMapButton(Activity activity)
    {
        ImageButton ibMap = activity.findViewById(R.id.imageButtonMap);
        setupListenerEvent(ibMap, activity, TeamsMapActivity.class);
    }

    public static void initListButton(Activity activity)
    {
        ImageButton ibList = activity.findViewById(R.id.imageButtonList);
        setupListenerEvent(ibList, activity, TeamsListActivity.class);
    }

    private static void setupListenerEvent(ImageButton imageButton,
                                           Activity fromActivity,
                                           Class<?> destinationActivityClass) {

        // disable one of them
        imageButton.setEnabled(fromActivity.getClass() != destinationActivityClass);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(fromActivity, destinationActivityClass);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                fromActivity.startActivity(intent);
            }
        });
    }
}
