package com.shlsoft.fooddelivery.actvities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shlsoft.fooddelivery.R;
import com.shlsoft.fooddelivery.databinding.ActivitySignUpBinding;
import com.shlsoft.fooddelivery.util.Toasts;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

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

        binding.edtPhone.setText("+");
        binding.edtPhone.requestFocus();


        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(
                        binding.edtPhone.getText().toString().isEmpty() ||
                        binding.edtName.getText().toString().isEmpty() ||
                        binding.edtPassword.getText().toString().isEmpty() ||
                        binding.edtConfirmPassword.getText().toString().isEmpty()
                ){
                    Toasts.showWarningToast(getString(R.string.barcha_maydoni_toldiring));
                }
                else if(!binding.edtPhone.getText().toString().startsWith("+")){
                    binding.edtPhone.setError(getString(R.string.plyus_belgisi));
                    binding.edtPhone.requestFocus();
                }
                else if(!binding.edtPhone.getText().toString().contains("998")){
                    binding.edtPhone.setError(getString(R.string.ozbekiston_raqami_emas));
                }
                else if(binding.edtName.getText().toString().length() < 3){
                    Toasts.showWarningToast(getString(R.string.ism_tekshirish));
                    binding.edtName.requestFocus();
                }
                else if(binding.edtPassword.getText().toString().length() < 5){
                    binding.edtPassword.setError(getString(R.string.parol_kamida_besh_harf));
                }
                else if(!binding.edtConfirmPassword.getText().toString()
                        .equals(binding.edtPassword.getText().toString())){
                    //Password does not match
                    binding.edtPassword.setText("");
                    binding.edtConfirmPassword.setText("");
                    binding.edtPassword.requestFocus();

                    Toasts.showErrorToast(getString(R.string.tasdiqlash_xatosi));
                }



                //Check if the name contains only the letters
                Pattern p = Pattern.compile("[^\\p{L}+]");
                Matcher m = p.matcher(binding.edtName.getText().toString());
                if(m.find()){
                    binding.edtName.setError(getString(R.string.ism_faqat_harf));
                    binding.edtName.setSelectAllOnFocus(true);
                    binding.edtName.requestFocus();
                }
            }
        });
    }

    private void initFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        table_user = firebaseDatabase.getReference("User");
    }
}
