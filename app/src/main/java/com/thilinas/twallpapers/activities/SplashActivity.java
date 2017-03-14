package com.thilinas.twallpapers.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.thilinas.twallpapers.R;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MenuActivity.class));
            finish();
        }else {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }

        setContentView(R.layout.activity_splash);
    }
}
