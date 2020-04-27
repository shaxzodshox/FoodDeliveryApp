package com.shlsoft.fooddelivery.actvities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shlsoft.fooddelivery.R;
import com.shlsoft.fooddelivery.app.BaseActivity;
import com.shlsoft.fooddelivery.common.Common;
import com.shlsoft.fooddelivery.model.User;
import com.shlsoft.fooddelivery.util.Toasts;
import com.swiftsynq.otpcustomview.CustomOtpView;

import java.util.concurrent.TimeUnit;

import info.hoang8f.widget.FButton;

import static maes.tech.intentanim.CustomIntent.customType;

public class PhoneVerificationActivity extends BaseActivity {

    private TextView tv_sms_error;

    private String verification_id;
    private FButton btnVerification;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference table_user;

    private CustomOtpView otpView;
    private TextView tv_verification_title;

    private String phone;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);

        firebaseAuth = FirebaseAuth.getInstance();

        initFirebase();

        tv_sms_error = findViewById(R.id.tv_sms_error);
        btnVerification = findViewById(R.id.btnVerify);
        tv_verification_title = findViewById(R.id.tv_verification_title);
        otpView = findViewById(R.id.otpView);

        phone = getIntent().getExtras().getString("phone");

        if (phone != null && !phone.isEmpty()) {
            tv_verification_title.setText(getString(R.string.tasdiqlash_uchun) + " " + phone + " " + getString(R.string.sms_yuborildi));
        }
        otpView.setAnimationEnable(true); //animation when the user type

        sendVerificationCode(phone);

        btnVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!otpView.getText().toString().isEmpty()) {
                    verifyCode(otpView.getText().toString());
                }else{
                    Toasts.showWarningToast(getString(R.string.sms_kod_kiriting));
                }
            }
        });


    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verification_id, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            saveUser();
                            Intent intent = new Intent(getApplicationContext(),MenuActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            PhoneVerificationActivity.this.finish();
                        }
                        else {
                            tv_sms_error.setVisibility(View.VISIBLE);

                            new CountDownTimer(2000, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {

                                }

                                @Override
                                public void onFinish() {
                                tv_sms_error.setVisibility(View.INVISIBLE);
                                }
                            }.start();

                            otpView.setText("");
                            Vibrator v = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                            v.vibrate(400);
                        }
                    }
                });
    }

    private void saveUser() {
        String phoneWithoutPlus = phone.replace("+","");
        String name = getIntent().getExtras().getString("name");
        String password = getIntent().getExtras().getString("password");


        User user = new User(name,password);
        table_user.child(phoneWithoutPlus).setValue(user);

        Common.current_user = user; //Keep this user as a current user
    }

    private void sendVerificationCode(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60, //resend after 60 seconds again if there is some mistake
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verification_id = s; //get the SMS code to check later with user's input
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(getApplicationContext(), R.string.tizimda_xatolik,Toast.LENGTH_LONG).show();
        }
    };

    private void initFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        table_user = firebaseDatabase.getReference("User");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        customType(PhoneVerificationActivity.this,"right-to-left");
    }
}
