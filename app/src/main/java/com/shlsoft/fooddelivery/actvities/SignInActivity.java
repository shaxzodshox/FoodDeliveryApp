package com.shlsoft.fooddelivery.actvities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.shlsoft.fooddelivery.R;
import com.shlsoft.fooddelivery.app.BaseActivity;
import com.shlsoft.fooddelivery.model.User;
import com.shlsoft.fooddelivery.util.Toasts;

import info.hoang8f.widget.FButton;

import static maes.tech.intentanim.CustomIntent.customType;

public class SignInActivity extends BaseActivity {

    private MaterialEditText edt_phone, edt_password;
    private TextView tv_error;
    private FButton btn_signIn;

    //Firebase
    private FirebaseDatabase database;
    private DatabaseReference table_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initViews();
        phoneEditTextSetup();

        //initializing the Firebase Database
        initFirebaseDatabase();

        btn_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_phone.getText().toString().isEmpty()) {
                    edt_phone.setError("Iltimos telefon raqam kiriting!");
                    edt_phone.requestFocus();
                } else if (edt_password.getText().toString().isEmpty()) {
                    edt_password.setError("Iltimos parolni kiriting");
                    edt_password.requestFocus();
                } else {
                    String phone = edt_phone.getText().toString();
                    String password = edt_password.getText().toString();

                    signIn(phone, password);
                }


            }
        });
    }

    private void signIn(final String phone, final String password) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Iltimos kuting...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        table_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.dismiss();

                //Check if user not exists in database
                if (dataSnapshot.child(phone).exists()) {
                    //Get user information
                    User user = dataSnapshot.child(phone).getValue(User.class);
                    if (user.getPassword().equals(password)) {
                        Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_SHORT).show();
                    } else {
                        //Password is wrong
                        edt_password.setText("");
                        edt_password.requestFocus();
                        tv_error.setVisibility(View.VISIBLE);
                        new CountDownTimer(2000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                            }

                            @Override
                            public void onFinish() {
                                tv_error.setVisibility(View.INVISIBLE);
                            }
                        }.start();
                    }
                } else {
                    //if the user does not exists
                    progressDialog.dismiss();
                    Toasts.showErrorToast(getString(R.string.foydalanuvchi_topilmadi));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void initFirebaseDatabase() {
        database = FirebaseDatabase.getInstance();
        table_user = database.getReference("User"); //Table name is User
    }

    private void phoneEditTextSetup() {
        edt_phone.setText("+998");
        edt_phone.requestFocus();
    }

    private void initViews() {
        edt_phone = findViewById(R.id.edt_phone);
        edt_password = findViewById(R.id.edt_password);
        btn_signIn = findViewById(R.id.btnSignIn);
        tv_error = findViewById(R.id.tv_error);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        customType(SignInActivity.this,"right-to-left");
    }
}
