package com.thilinas.twallpapers.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.thilinas.twallpapers.R;

public class FinishActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        finish();
    }
}
