package com.moutamid.annoe.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.moutamid.annoe.R;
import com.moutamid.annoe.constants.ClickListner;
import com.moutamid.annoe.models.Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.RepoVH> {
    Context context;
    ArrayList<Model> list;
    ClickListner clickListner;

    public RepoAdapter(Context context, ArrayList<Model> list, ClickListner clickListner) {
        this.context = context;
        this.list = list;
        this.clickListner = clickListner;
    }

    @NonNull
    @Override
    public RepoVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.repo_card, parent, false);
        return new RepoVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RepoVH holder, int position) {
        Model model = list.get(holder.getAdapterPosition());

        if (holder.getAdapterPosition() == 0){
            holder.versionNum.setTextColor(context.getResources().getColor(R.color.background));
            holder.versionCard.setCardBackgroundColor(context.getResources().getColor(R.color.yellow));
        }

        holder.itemView.setOnClickListener(v -> {
            clickListner.onClick(holder.getAdapterPosition());
        });

        holder.accuracy.setText("Accuracy - " + model.getAccuracy());
        holder.elapsedTime.setText("Elapsed time - " + model.getElapsed_time() + "s");
        holder.loss.setText("Loss - " + model.getLoss());
        holder.versionNum.setText("v"+model.getVersion());


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM, yyyy");
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa");
        Date date;
        try {
            date = format.parse(model.getCreated_at());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        String day = dayFormat.format(date);
        String d = dateFormat.format(date);
        String time = timeFormat.format(date);

        String created_at = "Created on " + day + " of " + d + " @ " + time;
        holder.time.setText(created_at);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class RepoVH extends RecyclerView.ViewHolder{
        TextView elapsedTime, accuracy, loss, time, versionNum;
        MaterialCardView versionCard;

        public RepoVH(@NonNull View itemView) {
            super(itemView);
            elapsedTime = itemView.findViewById(R.id.elapsedTime);
            accuracy = itemView.findViewById(R.id.accuracy);
            loss = itemView.findViewById(R.id.loss);
            time = itemView.findViewById(R.id.time);
            versionNum = itemView.findViewById(R.id.versionNumb);
            versionCard = itemView.findViewById(R.id.versionCard);
        }
    }

}
