package com.moutamid.annoe.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.fxn.stash.Stash;
import com.moutamid.annoe.R;
import com.moutamid.annoe.adapters.RepoAdapter;
import com.moutamid.annoe.adapters.SliderAdapter;
import com.moutamid.annoe.adapters.StatsAdapter;
import com.moutamid.annoe.constants.Constants;
import com.moutamid.annoe.databinding.FragmentStatBinding;
import com.moutamid.annoe.models.Model;
import com.moutamid.annoe.models.Stats;
import com.smarteist.autoimageslider.SliderViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.UUID;


public class StatFragment extends Fragment {
    FragmentStatBinding binding;
    ArrayList<Stats> list;
    ArrayList<String> setImageList = new ArrayList<>();
    Stats stats;
    StatsAdapter adapter;
    ProgressDialog progressDialog;
    String version;
    ArrayList<SlideModel> imageList = new ArrayList<>();

    public StatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentStatBinding.inflate(getLayoutInflater(), container, false);

        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        list = new ArrayList<>();

        String v = Stash.getString(Constants.VERSION, "");
        if (v.isEmpty()) {
            String c = Stash.getString(Constants.CURRENT);
            version = Constants.MODEL_API + c;
            binding.versionNumb.setText("v" + c);
        } else {
            version = Constants.MODEL_API + v;
        }
        Stash.clear(Constants.VERSION);

        imageList.add(new SlideModel("http://128.199.84.194:8000/image1/test_img1.png", null, ScaleTypes.FIT));
        imageList.add(new SlideModel("http://128.199.84.194:8000/image1/test_img2.png", null, ScaleTypes.FIT));

        setImageList.add("http://128.199.84.194:8000/image1/test_img1.png");
        setImageList.add("http://128.199.84.194:8000/image1/test_img2.png");

        SliderAdapter adapter1 = new SliderAdapter(requireContext(), setImageList);
        binding.imageSlider.setSliderAdapter(adapter1);

        LinearLayoutManager lm = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        binding.recyler.setLayoutManager(lm);
        binding.recyler.setHasFixedSize(false);

        getRepo();

        return binding.getRoot();
    }

    private void getRepo() {
        Log.d("testingRepo", "Enter");
        progressDialog.show();
        new Thread(() -> {
            URL google = null;
            try {
                google = new URL(version);
            } catch (final MalformedURLException e) {
                e.printStackTrace();
            }
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(google != null ? google.openStream() : null));
            } catch (final IOException e) {
                Log.d("testingRepo", "compress: ERROR: " + e.toString());
                e.printStackTrace();
            }
            String input = null;
            StringBuffer stringBuffer = new StringBuffer();
            while (true) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        if ((input = in != null ? in.readLine() : null) == null) break;
                    }
                } catch (final IOException e) {
                    e.printStackTrace();
                }
                stringBuffer.append(input);
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
            String htmlData = stringBuffer.toString();

            Log.d("TAG", "data: " + htmlData);

            if (isAdded()) {
                requireActivity().runOnUiThread(() -> {
                    progressDialog.dismiss();
                    try {
                        JSONArray json = new JSONArray(htmlData);
                        list.clear();
                        for (int i = 0; i < json.length(); i++) {
                            JSONObject object = json.getJSONObject(i);
                            Model model = new Model(
                                    UUID.randomUUID().toString(),
                                    object.getString("model"),
                                    object.getString("version"),
                                    object.getInt("elapsed_time"),
                                    object.getDouble("accuracy"),
                                    object.getDouble("loss"),
                                    object.getString("created_at"),
                                    object.getInt("epochs"),
                                    object.getString("prediction_ratio"),
                                    object.getInt("model_size"),
                                    object.getString("aware_optimizer"),
                                    object.getString("post_optimizer")
                            );
                            binding.versionNumb.setText("v" + model.getVersion());
                            binding.postOpt.setText(model.getPost_optimizer());
                            binding.optim.setText(model.getAware_optimizer());
                            for (String s : iterate(object.keys())) {
                                if (s.equals("elapsed_time")) {
                                    stats = new Stats("Elapsed time", model.getElapsed_time() + "s");
                                    list.add(stats);
                                } else if (s.equals("accuracy")) {
                                    stats = new Stats("Accuracy", "" + model.getAccuracy());
                                    list.add(stats);
                                } else if (s.equals("loss")) {
                                    stats = new Stats("Loss", "" + model.getLoss());
                                    list.add(stats);
                                } else if (s.equals("epochs")) {
                                    stats = new Stats("Epochs", "" + model.getEpochs());
                                    list.add(stats);
                                } else if (s.equals("prediction_ratio")) {
                                    stats = new Stats("Prediction Ratio", "" + model.getPrediction_ratio());
                                    list.add(stats);
                                } else if (s.equals("model_size")) {
                                    stats = new Stats("Model Size", model.getModel_size() + "MB");
                                    list.add(stats);
                                }

                            }
                        }

                        adapter = new StatsAdapter(requireContext(), list);
                        binding.recyler.setAdapter(adapter);
                        progressDialog.dismiss();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show());
                    }
                });
            }
        }).start();
    }

    private <T> Iterable<T> iterate(final Iterator<T> i) {
        return () -> i;
    }

}