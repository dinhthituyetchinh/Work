package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


import Adapter.ParentAdapter;
import Database.FirebaseHelper;
import Model.Child;
import Model.Parent;

public class SecondActivity extends AppCompatActivity {
    RecyclerView rcParent;
    ParentAdapter parentAdapter;
    List<Parent> parentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        rcParent = findViewById(R.id.rcParent);
        // Khởi tạo adapter với listener để mở DetailActivity
        parentAdapter = new ParentAdapter(parentList, ParentAdapter.Mode.STATIC, (staticUrl, gifUrl) -> {
            Intent intent = new Intent(SecondActivity.this, DetailActivity.class);
            intent.putExtra("staticUrl", staticUrl);
            intent.putExtra("gifUrl", gifUrl);
            startActivity(intent);
        });

        rcParent.setLayoutManager(new LinearLayoutManager(this));
        rcParent.setAdapter(parentAdapter);

        FirebaseHelper.fetchAllParents(new FirebaseHelper.OnDataFetchedListener() {
            @Override
            public void onDataFetched(List<Parent> data) {
                parentList.clear();
                parentList.addAll(data);
                parentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(SecondActivity.this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });


    }
}