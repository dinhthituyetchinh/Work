package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toNextActivity();
    }
    public void toNextActivity()
    {
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        MainActivity.this.startActivity(intent);
        MainActivity.this.finish();

    }
}