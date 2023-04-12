package com.moutamid.annoe.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.moutamid.annoe.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderVH> {
    Context context;
    ArrayList<String> list;

    public SliderAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }


    @Override
    public SliderVH onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.slider_layout, null);
        return new SliderVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderVH holder, int position) {
        String s = list.get(position);
        Glide.with(context).load(s).into(holder.imageView);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public class SliderVH extends SliderViewAdapter.ViewHolder{
        ImageView imageView;
        public SliderVH(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }
    }
}
