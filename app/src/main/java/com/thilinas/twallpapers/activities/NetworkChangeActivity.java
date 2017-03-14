package com.thilinas.twallpapers.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.thilinas.twallpapers.R;


public class NetworkChangeActivity extends AppCompatActivity {

    private AlertDialog dialogError;
    private AlertDialog.Builder  builder;

    private LinearLayout detailBox;
    private LinearLayout searchingBox;
    private NetworkChangeReceiver networkChangeReceiver = new NetworkChangeReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNetworkAlert(this);
        registerNetworkBroadcastReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isConnectingToInternet(getApplicationContext())){
            showNetworkAlert();
        }else{
            if(dialogError!=null){
                if(dialogError.isShowing()){
                    dialogError.dismiss();
                }
            }
            dialogError=null;
        }
    }

    private void setNetworkAlert(Context context){
        builder = new AlertDialog.Builder(new ContextThemeWrapper(this,android.R.style.Theme_DeviceDefault_Light_Dialog));
        LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.popup_no_network, null);
        builder.setView(view);
        builder.setCancelable(false);
        CardView buttonRequestRetry = (CardView) view.findViewById(R.id.buttonTry);
        CardView buttonRequestExit = (CardView) view.findViewById(R.id.buttonExit);
        detailBox = (LinearLayout) view.findViewById(R.id.detailBox);
        searchingBox = (LinearLayout) view.findViewById(R.id.searchingBox);
        buttonRequestRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailBox.setVisibility(View.GONE);
                searchingBox.setVisibility(View.VISIBLE);
                try {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            searchingBox.setVisibility(View.GONE);
                            detailBox.setVisibility(View.VISIBLE);
                            if(!isConnectingToInternet(getApplicationContext())){
                                showNetworkAlert();
                            }else{
                                dialogError.dismiss();
                                ((ViewGroup) view.getParent()).removeView(view);
                                dialogError=null;
                            }
                        }
                    }, 5000);
                }catch (Exception e){
                    searchingBox.setVisibility(View.GONE);
                    detailBox.setVisibility(View.VISIBLE);
                }
            }
        });
        buttonRequestExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitApplication();
                searchingBox.setVisibility(View.GONE);
                detailBox.setVisibility(View.VISIBLE);
                dialogError.dismiss();
                ((ViewGroup) view.getParent()).removeView(view);
                dialogError=null;
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterNetworkBroadcastReceiver();
    }

    private void registerNetworkBroadcastReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        this.registerReceiver(networkChangeReceiver, filter);
    }

    private void unregisterNetworkBroadcastReceiver() {
        this.unregisterReceiver(networkChangeReceiver);
    }

    private void showNetworkAlert(){
        if(dialogError==null){
            searchingBox.setVisibility(View.GONE);
            detailBox.setVisibility(View.VISIBLE);
            dialogError = builder.create();
            dialogError.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogError.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialogError.show();
        }else {
            if(!dialogError.isShowing()){
                searchingBox.setVisibility(View.GONE);
                detailBox.setVisibility(View.VISIBLE);
                dialogError.show();
            }
        }
    }

    protected boolean isConnectingToInternet(Context context){
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED){
                        return true;
                    }
        }
        return false;
    }

    private class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, Intent intent) {
            if(!isConnectingToInternet(context)){
                showNetworkAlert();
            }
        }
    }

    private void exitApplication(){
        Intent intent = new Intent(this, FinishActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
