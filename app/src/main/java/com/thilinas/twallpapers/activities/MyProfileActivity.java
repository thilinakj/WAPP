package com.thilinas.twallpapers.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.makeramen.roundedimageview.RoundedImageView;
import com.thilinas.twallpapers.R;
import com.thilinas.twallpapers.customs.CustomTextView;

public class MyProfileActivity extends AppCompatActivity {

    String TAG = "RegisterFragmentFIRE" ;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        final ImageView img = (ImageView) findViewById(R.id.imgB);
        final Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.w2);
           try {
               Bitmap blurred = blurRenderScript(getApplicationContext(),largeIcon, 25);
               img.setImageBitmap(blurred);
           }catch (Exception e){
               img.setImageBitmap(largeIcon);
           }
        setFire();
        setDetails();

    }

    private void setFire(){
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }
            }
        };
    }

    private void setDetails(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();
            String uid = user.getUid();
            final RoundedImageView proPic = (RoundedImageView) findViewById(R.id.pro_pic);
            final CustomTextView txtMail = (CustomTextView) findViewById(R.id.txt_mail);
            final CustomTextView txtName = (CustomTextView) findViewById(R.id.txt_name);
            if(name == null || name.isEmpty()){
                txtName.setText("Hi ! ");
            }else{
                txtName.setText("Hi ! " + name);
            }
            if(email.trim().isEmpty()){
                txtName.setText("N/A");
            }else{
                txtMail.setText(email);
            }
            if(photoUrl == null){
                proPic.setImageResource(R.drawable.ic_user);
            }else{
                try {
                    Glide
                            .with(getApplicationContext())
                            .load(photoUrl)
                            .asBitmap()
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .into(new SimpleTarget<Bitmap>(110,110) {
                                @Override
                                public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                                    proPic.setImageBitmap(bitmap);
                                }
                            });
                }catch (Exception e){     }
            }
        }else{
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }
    }

    public void onClick(View view){
        if(view.getId() == R.id.btn_c_pass){
            Toast.makeText(getApplicationContext(), "This feature will be coming soon.", Toast.LENGTH_SHORT).show();
            /*user.updatePassword(newPassword.getText().toString().trim())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Password is updated!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Failed to update password!", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });*/
        }
        if(view.getId() == R.id.btn_r_acc){
            if (user != null) {
                user.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Your profile was deleted.", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Failed to delete your account!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }
        if(view.getId() == R.id.btn_s_out){
            mAuth.signOut();
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            finish();
        }
        if(view.getId() == R.id.btn_ads){
            startActivity(new Intent(getApplicationContext(),MenuActivity.class));
        }
    }

    @SuppressLint("NewApi")
    public static Bitmap blurRenderScript(Context context, Bitmap smallBitmap, int radius) {
        try {
            smallBitmap = RGB565toARGB888(smallBitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bitmap bitmap = Bitmap.createBitmap(
                smallBitmap.getWidth(), smallBitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        RenderScript renderScript = RenderScript.create(context);
        Allocation blurInput = Allocation.createFromBitmap(renderScript, smallBitmap);
        Allocation blurOutput = Allocation.createFromBitmap(renderScript, bitmap);
        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(renderScript,
                Element.U8_4(renderScript));
        blur.setInput(blurInput);
        blur.setRadius(radius); // radius must be 0 < r <= 25
        blur.forEach(blurOutput);
        blurOutput.copyTo(bitmap);
        renderScript.destroy();
        return bitmap;
    }

    private static Bitmap RGB565toARGB888(Bitmap img) throws Exception {
        int numPixels = img.getWidth() * img.getHeight();
        int[] pixels = new int[numPixels];
        //Get JPEG pixels.  Each int is the color values for one pixel.
        img.getPixels(pixels, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());
        //Create a Bitmap of the appropriate format.
        Bitmap result = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Bitmap.Config.ARGB_8888);
        //Set RGB pixels.
        result.setPixels(pixels, 0, result.getWidth(), 0, 0, result.getWidth(), result.getHeight());
        return result;
    }
}
