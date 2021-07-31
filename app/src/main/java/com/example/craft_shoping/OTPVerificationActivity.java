package com.example.craft_shoping;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class OTPVerificationActivity extends AppCompatActivity {

    private TextView phoneNo;
    private EditText otp;
    private Button verifybtn;
    private String userNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p_verification);
        phoneNo = findViewById(R.id.phone_no);
        otp = findViewById(R.id.otp);
        verifybtn = findViewById(R.id.verify);
        userNo = getIntent().getStringExtra("mobileNo");
        phoneNo.setText("Verification code has been send to +92 " + userNo);
        Random random=new Random();
        int Otp_number=random.nextInt(999999-111111)+111111;
    }
}