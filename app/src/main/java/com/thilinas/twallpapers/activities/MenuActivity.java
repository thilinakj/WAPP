package com.thilinas.twallpapers.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.thilinas.twallpapers.R;

public class MenuActivity extends NetworkChangeActivity {

    private FirebaseAuth mAuth;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        setAds();
    }

    public void onClick(View view){
        switch(view.getId()) {
            case R.id.tile1 :
                startActivity(new Intent(getApplicationContext(),PhotoViewActivity.class));
                break;
            case R.id.tile2 :
                startActivity(new Intent(getApplicationContext(),MyFavouritesActivity.class));
                break;
            case R.id.tile3 :
                startActivity(new Intent(getApplicationContext(),MyProfileActivity.class));
                break;
            case R.id.tile4 :
                Toast.makeText(getApplicationContext(), "This feature will be coming soon.", Toast.LENGTH_SHORT).show();
                break;
            default : break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            finish();
        }
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    private void setAds(){
        mAdView = (AdView) findViewById(R.id.adView);
      //  AdRequest adRequestBanner = new AdRequest.Builder().addTestDevice("04314D770769A618742A42632138DD3E").build();
        AdRequest adRequestBanner = new AdRequest.Builder().build();
        mAdView.loadAd(adRequestBanner);
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
}
