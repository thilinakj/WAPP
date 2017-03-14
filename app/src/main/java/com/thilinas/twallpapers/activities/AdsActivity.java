package com.thilinas.twallpapers.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.thilinas.twallpapers.R;

public class AdsActivity extends AppCompatActivity {

    private AdView mAdView;
    InterstitialAd mInterstitialAd;
    Button btnFullscreenAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads);

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequestBanner = new AdRequest.Builder().addTestDevice("04314D770769A618742A42632138DD3E").build();
        mAdView.loadAd(adRequestBanner);


        mInterstitialAd = new InterstitialAd(this);
        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
        final AdRequest adRequestFull = new AdRequest.Builder().addTestDevice("04314D770769A618742A42632138DD3E").build();
        // Load ads into Interstitial Ads

        // full  ca-app-pub-9469961636590299/5782641169
        // baner ca-app-pub-9469961636590299/7715202760
      /*  mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }

        });*/
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Toast.makeText(getApplicationContext(), "Ad is loaded!", Toast.LENGTH_SHORT).show();
                showInterstitial();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Toast.makeText(getApplicationContext(), "Ad failed to load! error code: " + errorCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdClosed() {
                Toast.makeText(getApplicationContext(), "Ad is closed!", Toast.LENGTH_SHORT).show();
            }
        });

        btnFullscreenAd = (Button) findViewById(R.id.btn_fullscreen_ad);
        btnFullscreenAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInterstitialAd.loadAd(adRequestFull);
            }
        });


        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Toast.makeText(getApplicationContext(), "Ad is loaded!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdClosed() {
                Toast.makeText(getApplicationContext(), "Ad is closed!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Toast.makeText(getApplicationContext(), "Ad failed to load! error code: " + errorCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLeftApplication() {
                Toast.makeText(getApplicationContext(), "Ad left application!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdOpened() {
                Toast.makeText(getApplicationContext(), "Ad is opened!", Toast.LENGTH_SHORT).show();
            }
        });



    }


    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

}
