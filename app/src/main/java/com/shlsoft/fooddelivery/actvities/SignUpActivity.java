package com.shlsoft.fooddelivery.actvities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shlsoft.fooddelivery.R;
import com.shlsoft.fooddelivery.app.BaseActivity;
import com.shlsoft.fooddelivery.databinding.ActivitySignUpBinding;
import com.shlsoft.fooddelivery.model.User;
import com.shlsoft.fooddelivery.util.Toasts;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends BaseActivity {

    private ActivitySignUpBinding binding;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference table_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Binding the view using ViewBinding
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        initFirebase();

        //Set +998 for the Phone input
        phoneSetup();

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (
                        binding.edtPhone.getText().toString().isEmpty() ||
                                binding.edtName.getText().toString().isEmpty() ||
                                binding.edtPassword.getText().toString().isEmpty() ||
                                binding.edtConfirmPassword.getText().toString().isEmpty()
                ) {
                    Toasts.showWarningToast(getString(R.string.barcha_maydoni_toldiring));
                }else if (binding.edtPhone.getText().toString().length() != 13) {
                    Toasts.showWarningToast(getString(R.string.raqamni_tekshiring));
                    binding.edtPhone.requestFocus();
                }
                else if (binding.edtName.getText().toString().length() < 3) {
                    Toasts.showWarningToast(getString(R.string.ism_tekshirish));
                    binding.edtName.requestFocus();
                } else if (binding.edtPassword.getText().toString().length() < 5) {
                    binding.edtPassword.setError(getString(R.string.parol_kamida_besh_harf));
                } else if (!binding.edtConfirmPassword.getText().toString()
                        .equals(binding.edtPassword.getText().toString())) {
                    //Password does not match
                    binding.edtPassword.setText("");
                    binding.edtConfirmPassword.setText("");
                    binding.edtPassword.requestFocus();

                    Toasts.showErrorToast(getString(R.string.tasdiqlash_xatosi));
                } else {
                    //Check if the name contains only the letters
                    Pattern p = Pattern.compile("[^\\p{L}+]");
                    Matcher m = p.matcher(binding.edtName.getText().toString());
                    if (m.find()) {
                        binding.edtName.setError(getString(R.string.ism_faqat_harf));
                        binding.edtName.setSelectAllOnFocus(true);
                        binding.edtName.requestFocus();
                    } else {
                        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this);
                        progressDialog.setMessage(getString(R.string.iltimos_kuting));
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        //Validation is correct...Insert user into firebase database
                        table_user.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                progressDialog.dismiss();

                                String phone = binding.edtPhone.getText().toString().replace("+", ""); //removing plus symbol before adding it into firebase

                                //Check if user already registered
                                if (dataSnapshot.child(phone).exists()) {
                                    binding.edtPhone.setSelectAllOnFocus(true);
                                    binding.edtPhone.setError(getString(R.string.royxatdan_otmagan_raqam_kiriting));
                                    binding.edtPhone.requestFocus();
                                    Toasts.showErrorToast(getString(R.string.oldin_royxatdan_otgan));
                                } else {
                                    User user = new User
                                            (binding.edtName.getText().toString(), binding.edtPassword.getText().toString());
                                    table_user.child(phone).setValue(user);
                                    Toasts.showSuccessToast("Muvaffaqiyatli ro'yxatdan o'tdingiz");
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }

            }
        });
    }

    private void phoneSetup() {
        binding.edtPhone.setText("+998");
        binding.edtPhone.requestFocus();
        binding.edtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().startsWith("+998")) {
                    binding.edtPhone.setText("+998");
                    Selection.setSelection(binding.edtPhone.getText(), binding.edtPhone.getText().toString().length());
                }

            }
        });
    }
    private void initFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        table_user = firebaseDatabase.getReference("User");
    }
}
