package com.example.craft_shoping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {
private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        firebaseAuth=FirebaseAuth.getInstance();
        SystemClock.sleep(2000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentuser=firebaseAuth.getCurrentUser();
        if (currentuser == null){
            Intent regesterIntent = new Intent(SplashActivity.this,RegisterActivity.class);
            startActivity(regesterIntent);
            finish();
        }else {
            Intent mainIntent = new Intent(SplashActivity.this,RegisterActivity.class);
            startActivity(mainIntent);
            finish();
        }
    }
}