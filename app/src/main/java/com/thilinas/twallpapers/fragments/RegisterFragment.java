package com.thilinas.twallpapers.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import com.google.firebase.auth.GoogleAuthProvider;
import com.thilinas.twallpapers.R;
import com.thilinas.twallpapers.activities.MyProfileActivity;
import com.thilinas.twallpapers.customs.CustomButton;
import com.thilinas.twallpapers.customs.CustomEditText;
import com.thilinas.twallpapers.customs.CustomTextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executor;

/**
 * Created by Thilina on 02-Feb-17.
 */

public class RegisterFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private Vibrator vib;
    private Animation animShake;
    private CustomEditText editTextEmail,editTextPass;
    private TextInputLayout textInputLayoutEmail,textInputLayoutPass;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private int  RC_SIGN_IN = 101;
    private GoogleApiClient mGoogleApiClient;
    String TAG = "RegisterFragmentFIRE" ;
    private String email,pass;

    public static RegisterFragment newInstance(int page, String title) {
        RegisterFragment registerFragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        registerFragment.setArguments(args);
        return registerFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        setElements(view);
        setFire();
        return view;
    }

    private void setFire(){
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged: signed_in:" + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged: signed_out");
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        if(mGoogleApiClient == null){
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .enableAutoManage(getActivity(), this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API).build();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }else {
                            getCurrentUser();
                        }
                        // ...
                    }
                });
    }

    public void createAccount(){
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getActivity(), "Successfully login via google", Toast.LENGTH_SHORT).show();
                            getCurrentUser();

                        }
                    }
                });
    }

    public void getCurrentUser(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();
            boolean emailVerified = user.isEmailVerified();
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
        startActivity(new Intent(getActivity(), MyProfileActivity.class));
    }

    private void setElements(View view){
        animShake = AnimationUtils.loadAnimation(getActivity(),R.anim.shake);
        vib = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        textInputLayoutEmail = (TextInputLayout) view.findViewById(R.id.mailRow);
        textInputLayoutPass = (TextInputLayout) view.findViewById(R.id.passRow);
        editTextEmail = (CustomEditText) view.findViewById(R.id.emailInput);
        editTextPass = (CustomEditText) view.findViewById(R.id.passInput);
        editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                checkEmail();
            }
        });
        editTextPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
               checkPassword();
            }
        });
        CustomButton btnLogin = (CustomButton) view.findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override    public void run() {
                        ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.vpPager);
                        viewPager.setCurrentItem(0);
                    }
                }, 200);

            }
        });

        CustomButton btnReg = (CustomButton) view.findViewById(R.id.btn_reg);
        btnReg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });

        CustomButton btnGPlus = (CustomButton) view.findViewById(R.id.btn_gplus);
        btnGPlus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                buildGoogleApiClient();
            }
        });
    }

    private void submitForm() {
        if (!checkEmail()) {
            textInputLayoutEmail.setAnimation(animShake);
            editTextEmail.startAnimation(animShake);
            vib.vibrate(120);
            return;
        }
        if (!checkPassword()) {
            textInputLayoutPass.setAnimation(animShake);
            editTextEmail.startAnimation(animShake);
            vib.vibrate(120);
            return;
        }
        textInputLayoutPass.setErrorEnabled(false);
        textInputLayoutEmail.setErrorEnabled(false);
        createAccount();
    }

    private boolean checkEmail() {
        email = editTextEmail.getText().toString().trim();
        if (email.isEmpty() || !isValidEmail(email)) {
            textInputLayoutEmail.setErrorEnabled(true);
            textInputLayoutEmail.setError(getString(R.string.err_msg_email));
            editTextEmail.setError(getString(R.string.err_msg_required));
            requestFocus(editTextEmail);
            return false;
        }
        textInputLayoutEmail.setErrorEnabled(false);
        return true;
    }

    private boolean checkPassword() {
        pass = editTextPass.getText().toString().trim();
        if (pass.isEmpty()) {
            textInputLayoutPass.setErrorEnabled(true);
            textInputLayoutPass.setError(getString(R.string.err_msg_password));
            editTextPass.setError(getString(R.string.err_msg_required));
            requestFocus(editTextPass);
            return false;
        }
        if (pass.length() < 6) {
            textInputLayoutPass.setErrorEnabled(true);
            textInputLayoutPass.setError(getString(R.string.minimum_password));
            editTextPass.setError(getString(R.string.err_msg_required));
            requestFocus(editTextPass);
            return false;
        }
        textInputLayoutPass.setErrorEnabled(false);
        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
