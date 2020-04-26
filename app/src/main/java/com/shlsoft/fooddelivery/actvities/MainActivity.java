package com.shlsoft.fooddelivery.actvities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.shlsoft.fooddelivery.R;
import com.shlsoft.fooddelivery.app.BaseActivity;

import info.hoang8f.widget.FButton;

import static maes.tech.intentanim.CustomIntent.customType;

public class MainActivity extends BaseActivity implements View.OnClickListener{
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
                startActivity(new Intent(getApplicationContext(),SignUpActivity.class));
                customType(MainActivity.this,"left-to-right");
                break;
            case R.id.btnSignIn:
                startActivity(new Intent(getApplicationContext(),SignInActivity.class));
                customType(MainActivity.this,"left-to-right");
                break;
        }
    }

    @Override
    public void onBackPressed() {
        showExitDialog();
    }

    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.chiqish)
                .setCancelable(false)
                .setMessage(R.string.chiqishni_xohlaysizmi)
                .setPositiveButton(R.string.xa, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                })
                .setNegativeButton(R.string.yoq, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
}
