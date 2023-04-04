package com.moutamid.annoe;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.moutamid.annoe.constants.Constants;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (Constants.isNetworkConnected(SplashScreenActivity.this)){
            startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
            finish();
        } else {
            new AlertDialog.Builder(SplashScreenActivity.this)
                    .setTitle("Internet is Disconnected!")
                    .setMessage("Please connect to the internet")
                    .setPositiveButton("Ok", (dialog, which) -> {
                        dialog.dismiss();
                        finish();
                    })
                    .setCancelable(false)
                    .show();
        }

    }
}