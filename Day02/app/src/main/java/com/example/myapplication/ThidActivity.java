package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import Adapter.ChildAdapter;
import Model.Child;

public class ThidActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thid);

        RecyclerView recyclerView = findViewById(R.id.rcFullList);
        String json = getIntent().getStringExtra("childListJson");

        Type listType = new TypeToken<List<Child>>(){}.getType();
        List<Child> childList = new Gson().fromJson(json, listType);

        ChildAdapter adapter = new ChildAdapter(childList, ChildAdapter.Mode.STATIC, (staticUrl, gifUrl) -> {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("gifUrl", gifUrl);
            startActivity(intent);
        });

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);
    }
}