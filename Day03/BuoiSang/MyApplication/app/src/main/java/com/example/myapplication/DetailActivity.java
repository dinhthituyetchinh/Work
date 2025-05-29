package com.example.myapplication;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;

public class DetailActivity extends AppCompatActivity {
    TextView tvBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
         tvBack = findViewById(R.id.tvBackDetail);
         tvBack.setOnClickListener(v -> finish());

        ImageView imageView = findViewById(R.id.imgGifDetail);
        String gifUrl = getIntent().getStringExtra("gifUrl");

        Glide.with(this).asGif().load(gifUrl).into(imageView);
    }
}