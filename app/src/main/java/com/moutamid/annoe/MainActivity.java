package com.moutamid.annoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.fxn.stash.Stash;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.moutamid.annoe.adapters.RepoAdapter;
import com.moutamid.annoe.constants.Constants;
import com.moutamid.annoe.databinding.ActivityMainBinding;
import com.moutamid.annoe.fragments.HomeFragment;
import com.moutamid.annoe.fragments.ListFragment;
import com.moutamid.annoe.fragments.StatFragment;
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
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    ProgressDialog progressDialog;
    ArrayList<Model> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.checkApp(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame, new HomeFragment()).commit();
        binding.navigation.setSelectedItemId(R.id.nav_home);
        list = new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        getRepo();


        binding.navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.nav_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame, new HomeFragment()).commit();
                        break;
                    case R.id.nav_list:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame, new ListFragment(binding.navigation)).commit();
                        break;
                    case R.id.nav_stat:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame, new StatFragment()).commit();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

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

                runOnUiThread(() -> {
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

                        String version = list.get(0).getVersion();
                        Stash.put(Constants.CURRENT, version);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
        }).start();
    }
}