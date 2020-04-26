package com.shlsoft.fooddelivery.actvities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.shlsoft.fooddelivery.R;

import info.hoang8f.widget.FButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private FButton btnSignUp, btnSignIn;
    private TextView tv_slogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initListeners();

        setTypeFace(tv_slogan); //changing the font of the slogan
    }

    private void initListeners() {
        btnSignUp.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
    }

    private void initViews() {
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignIn = findViewById(R.id.btnSignIn);
        tv_slogan = findViewById(R.id.textSlogan);
    }

    private void setTypeFace(TextView textView){
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/bee_leave.ttf");
        textView.setTypeface(typeface);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSignUp:
                // TODO: 26.04.2020 Implement sign up here
                break;
            case R.id.btnSignIn:
                startActivity(new Intent(getApplicationContext(),SignInActivity.class));
                break;
        }

    }
}
