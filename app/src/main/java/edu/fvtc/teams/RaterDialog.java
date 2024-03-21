package edu.fvtc.teams;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;

import androidx.fragment.app.DialogFragment;

public class RaterDialog extends DialogFragment {
    Float rating;

    public interface SaveRatingListener{
        void didFinishTeamRaterDialog(float rating);
    }

    public RaterDialog()
    {

    }

    public RaterDialog(float rating)
    {
        this.rating = rating;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.dialog_teamrater, container);
        getDialog().setTitle("Rate Team");

        RatingBar ratingBar = view.findViewById(R.id.ratingBar);
        ratingBar.setRating(rating);

        Button btnSave = view.findViewById(R.id.btnSaveRating);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float rating = ratingBar.getRating();
                saveRating(rating);
            }
        });

        return view;
    }

    private void saveRating(float rating) {
        // Get the parent activity
        SaveRatingListener activity = (SaveRatingListener) getActivity();

        // Call the interface method
        activity.didFinishTeamRaterDialog(rating);

        // Close the dialog
        getDialog().dismiss();
    }
}
