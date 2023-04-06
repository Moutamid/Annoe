package com.moutamid.annoe.fragments;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.fxn.stash.Stash;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.moutamid.annoe.R;
import com.moutamid.annoe.adapters.RepoAdapter;
import com.moutamid.annoe.constants.ClickListner;
import com.moutamid.annoe.constants.Constants;
import com.moutamid.annoe.databinding.FragmentListBinding;
import com.moutamid.annoe.models.Model;

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
import java.util.Comparator;
import java.util.UUID;

public class ListFragment extends Fragment {
    FragmentListBinding binding;
    ArrayList<Model> list;
    RepoAdapter adapter;
    ProgressDialog progressDialog;

    BottomNavigationView bottomNavigationView;
    FrameLayout frame;

    public ListFragment() {
        // Required empty public constructor
    }

    public ListFragment(BottomNavigationView bottomNavigationView) {
        this.bottomNavigationView = bottomNavigationView;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentListBinding.inflate(getLayoutInflater(), container, false);
        list = new ArrayList<>();
        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        binding.recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recycler.setHasFixedSize(false);

        getRepo();

        return binding.getRoot();
    }

    private void getRepo() {
        Log.d("testingRepo", "Enter");
        progressDialog.show();
        new Thread(() -> {
            URL google = null;
            try {
                google = new URL(Constants.REPO_ALL);
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
                            list.add(model);
                        }

                        Collections.reverse(list);

                        adapter = new RepoAdapter(requireContext(), list, clickListner);
                        adapter.notifyDataSetChanged();
                        binding.recycler.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
            }
        }).start();
    }

    ClickListner clickListner = new ClickListner() {
        @Override
        public void onClick(int pos, String version) {
            Stash.put(Constants.VERSION, version);
            bottomNavigationView.setSelectedItemId(R.id.nav_stat);
        }
    };

}