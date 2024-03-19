package edu.fvtc.teams;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.fvtc.teams.Team;

public class  TeamsAdapter extends RecyclerView.Adapter {
    private ArrayList<Team> teamData;
    private View.OnClickListener onItemClickListener;
    public static final String TAG = "TeamAdapter";
    private Context parentContext;

    public class TeamViewHolder extends RecyclerView.ViewHolder{
        public TextView tvFirstName;
        public TextView tvLastName;


        //private View.OnClickListener onClickListener;

        public TeamViewHolder(@NonNull View itemView) {
            super(itemView);
//            tvFirstName = itemView.findViewById(R.id.tvFirstName);
//            tvLastName = itemView.findViewById(R.id.tvLastName);


            // Code involving in click an item in the list
            itemView.setTag(this);
            itemView.setOnClickListener(onItemClickListener);
        }
        public TextView getTvFirstName()
        {
            return tvFirstName;
        }

        public TextView getTvLastName(){ return tvLastName; }
    }

    public TeamsAdapter(ArrayList<Team> data, Context context)
    {
        teamData = data;
        Log.d(TAG, "TeamAdapter: " + data.size());
        parentContext = context;
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener){
        Log.d(TAG, "setOnItemClickListener: ");
        onItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.complex_item_view, parent, false);
//        return new TeamViewHolder(v);
        return null;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: " + teamData.get(position));
        TeamViewHolder teamViewHolder = (TeamViewHolder) holder;
//        teamViewHolder.getTvFirstName().setText(teamData.get(position).getFirstName());
//        teamViewHolder.getTvLastName().setText(teamData.get(position).getLastName());

    }

    @Override
    public int getItemCount() {
        return teamData.size();
    }
}
