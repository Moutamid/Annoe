package com.moutamid.annoe.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.annoe.R;
import com.moutamid.annoe.models.Stats;

import java.util.ArrayList;

public class StatsAdapter extends RecyclerView.Adapter<StatsAdapter.StatVH> {
    Context context;
    ArrayList<Stats> list;

    public StatsAdapter(Context context, ArrayList<Stats> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public StatVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.stats_item, parent, false);
        return new StatVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatVH holder, int position) {
        Stats stats = list.get(holder.getAdapterPosition());

        if (holder.getAdapterPosition() == list.size()-1){
            holder.view.setVisibility(View.INVISIBLE);
        }

        holder.title.setText(stats.getTitle());
        holder.value.setText(stats.getValue());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class StatVH extends RecyclerView.ViewHolder{
        TextView title, value;
        View view;
        public StatVH(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            value = itemView.findViewById(R.id.value);
            view = itemView.findViewById(R.id.view);
        }
    }
}
