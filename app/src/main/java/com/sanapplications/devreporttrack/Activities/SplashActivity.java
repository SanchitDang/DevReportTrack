package com.sanapplications.devreporttrack.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sanapplications.devreporttrack.Activities.MainActivity;
import com.sanapplications.devreporttrack.Activities.sendotpActivivity;
import com.sanapplications.devreporttrack.R;

public class SplashActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // Hide the action bar
        getSupportActionBar().hide();


        firebaseAuth = FirebaseAuth.getInstance();

        new Handler().postDelayed(() -> {
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser!=null){
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }else{
                startActivity(new Intent(this, sendotpActivivity.class));
                finish();
            }
        },2000);

    }

//    @Override
//    protected  void onStart(){
//        super.onStart();
//
//        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
//
//        if(firebaseUser!=null){
//            startActivity(new Intent(this, MainActivity.class));
//            finish();
//        }else{
//            startActivity(new Intent(this, sendotpActivivity.class));
//            finish();
//        }
//
//    }

}