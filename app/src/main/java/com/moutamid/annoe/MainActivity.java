package com.moutamid.annoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.moutamid.annoe.constants.Constants;
import com.moutamid.annoe.databinding.ActivityMainBinding;
import com.moutamid.annoe.fragments.HomeFragment;
import com.moutamid.annoe.fragments.ListFragment;
import com.moutamid.annoe.fragments.StatFragment;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.checkApp(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame, new HomeFragment()).commit();
        binding.navigation.setSelectedItemId(R.id.nav_home);

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
}