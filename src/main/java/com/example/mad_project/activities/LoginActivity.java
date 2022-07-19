package com.example.mad_project.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.mad_project.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    //view binding
    private ActivityLoginBinding binding;

    //firebase auth
    private FirebaseAuth firebaseAuth;

    //progress dialog
    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();


        //setup progress dialog
        progressDialog = new Dialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);


        //handle click , go to register screen
        binding.noAccountTv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                startActivity(new Intent(LoginActivity.this , RegisterActivity.class));
            }
        });
        //handle click , begin login
        binding.logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
        //handle click , open forgot password activity
        binding.forgotTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this , ForgotPasswordActivity.class));
            }
        });
    }
    private String email="",password="";
    private void validateData() {
        /*Before logging , lets do some data validation*/

        //get data
        email = binding.emailEt.getText().toString().trim();
        password =binding.passwordEt.getText().toString().trim();

        //validate data
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this,"Invalid Email pattern..!",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Enter Password..!",Toast.LENGTH_SHORT).show();
        }else{
            //data is validated, begin login
            loginUser();
        }
    }

    private void loginUser() {
        //show progress
        progressDialog.setTitle("Logging in...");
        progressDialog.show();

        //logging user
        firebaseAuth.signInWithEmailAndPassword(email , password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //login success,check if user is user or admin
                        checkUser();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        //login fail
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void checkUser() {
        progressDialog.setTitle("Checking User..");
        //check if user is user or admin from realtime database
        //get current user
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        //check in db
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        progressDialog.dismiss();

                        //get user type
                        String userType = ""+snapshot.child("userType").getValue();
                        //check user type
                        if(userType.equals("user")){

                            //this is simple user , open user dashboard
                            startActivity(new Intent(LoginActivity.this, DashboardUserActivity.class));
                            finish();

                        }else if(userType.equals("admin")){
                            //this is  admin , open admin  dashboard
                            startActivity(new Intent(LoginActivity.this, DashboardAdminActivity.class));
                            finish();

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });

    }
}