package edu.fvtc.teams;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class  TeamsAdapter extends RecyclerView.Adapter {
    private ArrayList<Team> teamData;
    private View.OnClickListener onItemClickListener;
    private CompoundButton.OnCheckedChangeListener onItemCheckedChangeListener;

    public static final String TAG = "TeamAdapter";

    private Context parentContext;

    private boolean isDeleting;

    public void setDelete(boolean b)
    {
        isDeleting = b;
    }
    public class TeamViewHolder extends RecyclerView.ViewHolder{
        public TextView tvName;
        public TextView tvCity;

        private Button btnDelete;
        private CheckBox chkFavorite;

        public ImageButton imageButtonPhoto;
        private View.OnClickListener onClickListener;
        private CompoundButton.OnCheckedChangeListener onCheckedChangeListener;

        public TeamViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvCity = itemView.findViewById(R.id.tvCity);
            imageButtonPhoto = itemView.findViewById(R.id.imgPhoto);
            chkFavorite = itemView.findViewById(R.id.chkFavorite);
            btnDelete = itemView.findViewById(R.id.btnDelete);

            // Code involving with click an item in the list.
            itemView.setTag(this);
            itemView.setOnClickListener(onItemClickListener);
            chkFavorite.setTag(this);
            chkFavorite.setOnCheckedChangeListener(onCheckedChangeListener);
        }

        public TextView getTvName()
        {
            return tvName;
        }
        public TextView getTvCity()
        {
            return tvCity;
        }
        public ImageButton getImageButtonPhoto() {return imageButtonPhoto;}

        public CheckBox getChkFavorite() { return chkFavorite; }
        public Button getBtnDelete() {return btnDelete; }
    }

    public TeamsAdapter(ArrayList<Team> data, Context context)
    {
        teamData = data;
        Log.d(TAG, "TeamAdapter: " + data.size());
        parentContext = context;
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener)
    {
        Log.d(TAG, "setOnItemClickListener: ");
        onItemClickListener = itemClickListener;
    }

    public void setOnItemCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener)
    {
        Log.d(TAG, "setOnItemCheckedChangeListener: ");
        onItemCheckedChangeListener = listener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new TeamViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: " + teamData.get(position));
        TeamViewHolder teamViewHolder = (TeamViewHolder) holder;
        teamViewHolder.getTvName().setText(teamData.get(position).getName());
        teamViewHolder.getTvCity().setText(teamData.get(position).getCity());
        teamViewHolder.getChkFavorite().setChecked(teamData.get(position).getIsFavorite());
        teamViewHolder.getImageButtonPhoto().setImageResource(teamData.get(position).getImgId());

        if(isDeleting)
            teamViewHolder.getBtnDelete().setVisibility(View.VISIBLE);
        else
            teamViewHolder.getBtnDelete().setVisibility(View.INVISIBLE);

        teamViewHolder.chkFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "onCheckedChanged: " + isChecked);
                onItemCheckedChangeListener.onCheckedChanged(buttonView, isChecked);

            }
        });

        // Add a click listener for the btnDelete
        teamViewHolder.getBtnDelete().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Delete");
                deleteItem(position);
            }
        });
    }

    private void favoriteChanged(boolean isChecked) {
        notifyDataSetChanged();
    }

    private void deleteItem(int position) {
        Log.d(TAG, "deleteItem: " + position);
        Team team = teamData.get(position);
        teamData.remove(position);

        //FileIO.writeFile(TeamsListActivity.FILENAME,
        //                (AppCompatActivity) parentContext.getApplicationContext(),
        //                TeamsListActivity.createDataArray(teamData));
        //                TeamsListActivity.createDataArray(teamData));
        //                notifyDataSetChanged();

        Log.d(TAG, "deleteItem: parentContext: " + parentContext);
        TeamsDataSource ds = new TeamsDataSource(parentContext);
        Log.d(TAG, "deleteItem: " + team.toString());
        boolean didDelete = ds.delete(team) > 0;
        Log.d(TAG, "deleteItem: " + didDelete);
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return teamData.size();
    }
}
