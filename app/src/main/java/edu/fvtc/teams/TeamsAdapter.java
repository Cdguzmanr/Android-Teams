package edu.fvtc.teams;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class  TeamsAdapter extends RecyclerView.Adapter {
    private ArrayList<Team> teamData;
    private View.OnClickListener onItemClickListener;
    public static final String TAG = "TeamAdapter";

    private Context parentContext;

    public class TeamViewHolder extends RecyclerView.ViewHolder{
        public TextView tvName;
        public TextView tvCity;

        public ImageButton imageButtonPhoto;
        private View.OnClickListener onClickListener;
        public TeamViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvCity = itemView.findViewById(R.id.tvCity);
            imageButtonPhoto = itemView.findViewById(R.id.imgPhoto);
            // Code involving with click an item in the list.
            itemView.setTag(this);
            itemView.setOnClickListener(onItemClickListener);
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

        teamViewHolder.getImageButtonPhoto().setImageResource(teamData.get(position).getImgId());

    }

    @Override
    public int getItemCount() {
        return teamData.size();
    }
}
