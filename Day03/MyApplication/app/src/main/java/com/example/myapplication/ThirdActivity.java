package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import Adapter.ChildAdapter;
import Model.AnimationEntity;

public class ThirdActivity extends AppCompatActivity {
    private TextView tvBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        tvBack = findViewById(R.id.tvBack);
        tvBack.setOnClickListener(v -> finish());

        RecyclerView recyclerView = findViewById(R.id.rcFullList);
        String json = getIntent().getStringExtra("childListJson");

        if (json == null || json.isEmpty()) {
            finish(); // Không có dữ liệu thì thoát
            return;
        }

        Type listType = new TypeToken<List<AnimationEntity>>() {}.getType();
        List<AnimationEntity> childList = new Gson().fromJson(json, listType);

        ChildAdapter adapter = new ChildAdapter(
                this,
                childList,
                ChildAdapter.Mode.STATIC,
                (staticUrl, gifUrl, gifName) -> {
                    Intent intent = new Intent(this, DetailActivity.class);
                    intent.putExtra("gifUrl", gifUrl);
                    intent.putExtra("gifName", gifName);
                    startActivity(intent);
                });

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(adapter);
    }
}
