package com.example.mad_project.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mad_project.databinding.ActivityMain2Binding;

public class MainActivity2 extends AppCompatActivity {
    //view binding
    private ActivityMain2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //handle login click ,start login screen
        binding.logBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                 startActivity(new Intent(MainActivity2.this, LoginActivity.class));
            }
        });

        //handle skipBtn click ,start continue without login screen
        binding.skipBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(MainActivity2.this, DashboardUserActivity.class));
            }
        });
    }
}