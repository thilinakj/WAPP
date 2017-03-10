package com.thilinas.twallpapers.activities;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.thilinas.twallpapers.R;

public class MyProfileActivity extends AppCompatActivity {

    String TAG = "RegisterFragmentFIRE" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();
            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = user.getUid();

            Log.w(TAG, "User Details: "
                    +"\n\tname: "+name
                    +"\n\temail: "+email
                    +"\n\tphotoUrl: "+photoUrl
                    +"\n\tuid: "+uid
            );
        }

    }
}
