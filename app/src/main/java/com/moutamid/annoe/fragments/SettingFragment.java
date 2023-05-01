package com.moutamid.annoe.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fxn.stash.Stash;
import com.moutamid.annoe.R;
import com.moutamid.annoe.constants.Constants;
import com.moutamid.annoe.databinding.FragmentSettingBinding;
import com.zhouyou.view.seekbar.SignSeekBar;

public class SettingFragment extends Fragment {
    FragmentSettingBinding binding;
    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSettingBinding.inflate(getLayoutInflater(), container, false);

        int cpu = Stash.getInt(Constants.ITR, 100);
        int mem = Stash.getInt(Constants.MAT, 100);
        binding.seekBarCPU.setProgress(cpu);
        binding.seekBarMemory.setProgress(mem);

        setView();

        binding.seekBarCPU.setOnProgressChangedListener(new SignSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(SignSeekBar signSeekBar, int progress, float progressFloat, boolean fromUser) {
                Stash.put(Constants.ITR, progress);
            }

            @Override
            public void getProgressOnActionUp(SignSeekBar signSeekBar, int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(SignSeekBar signSeekBar, int progress, float progressFloat, boolean fromUser) {

            }
        });

        binding.seekBarMemory.setOnProgressChangedListener(new SignSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(SignSeekBar signSeekBar, int progress, float progressFloat, boolean fromUser) {
                Stash.put(Constants.MAT, progress);
            }

            @Override
            public void getProgressOnActionUp(SignSeekBar signSeekBar, int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(SignSeekBar signSeekBar, int progress, float progressFloat, boolean fromUser) {

            }
        });

        binding.optBat.setOnClickListener(v -> {
            if (Stash.getBoolean(Constants.opt_bat, false)){
                Stash.put(Constants.opt_bat, false);
                binding.optBat.setImageResource(R.drawable.toggle_off);
            } else {
                Stash.put(Constants.opt_bat, true);
                binding.optBat.setImageResource(R.drawable.toggle_on);
            }
        });

        binding.optCpu.setOnClickListener(v -> {
            if (Stash.getBoolean(Constants.opt_cpu, false)){
                Stash.put(Constants.opt_cpu, false);
                binding.optCpu.setImageResource(R.drawable.toggle_off);
            } else {
                Stash.put(Constants.opt_cpu, true);
                binding.optCpu.setImageResource(R.drawable.toggle_on);
            }
        });

        binding.optMem.setOnClickListener(v -> {
            if (Stash.getBoolean(Constants.opt_mem, false)){
                Stash.put(Constants.opt_mem, false);
                binding.optMem.setImageResource(R.drawable.toggle_off);
            } else {
                Stash.put(Constants.opt_mem, true);
                binding.optMem.setImageResource(R.drawable.toggle_on);
            }
        });

        binding.optStor.setOnClickListener(v -> {
            if (Stash.getBoolean(Constants.opt_stor, true)){
                Stash.put(Constants.opt_stor, false);
                binding.optStor.setImageResource(R.drawable.toggle_off);
            } else {
                Stash.put(Constants.opt_stor, true);
                binding.optStor.setImageResource(R.drawable.toggle_on);
            }
        });

        binding.postTrain.setOnClickListener(v -> {
            if (Stash.getBoolean(Constants.post_train, true)){
                Stash.put(Constants.post_train, false);
                binding.postTrain.setImageResource(R.drawable.toggle_off);
            } else {
                Stash.put(Constants.post_train, true);
                binding.postTrain.setImageResource(R.drawable.toggle_on);
            }
        });

        return binding.getRoot();
    }

    private void setView() {
        if (Stash.getBoolean(Constants.opt_bat, false)){
            binding.optBat.setImageResource(R.drawable.toggle_on);
        } else {
            binding.optBat.setImageResource(R.drawable.toggle_off);
        }

        if (Stash.getBoolean(Constants.opt_cpu, false)){
            binding.optCpu.setImageResource(R.drawable.toggle_on);
        } else {
            binding.optCpu.setImageResource(R.drawable.toggle_off);
        }

        if (Stash.getBoolean(Constants.opt_mem, false)){
            binding.optMem.setImageResource(R.drawable.toggle_on);
        } else {
            binding.optMem.setImageResource(R.drawable.toggle_off);
        }

        if (Stash.getBoolean(Constants.post_train, true)){
            binding.postTrain.setImageResource(R.drawable.toggle_on);
        } else {
            binding.postTrain.setImageResource(R.drawable.toggle_off);
        }

        if (Stash.getBoolean(Constants.opt_stor, true)){
            binding.optStor.setImageResource(R.drawable.toggle_on);
        } else {
            binding.optStor.setImageResource(R.drawable.toggle_off);
        }
    }
}