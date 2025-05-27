package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


import Adapter.ParentAdapter;
import Database.FirebaseHelper;
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
        rcParent.setLayoutManager(new LinearLayoutManager(this));

        parentAdapter = new ParentAdapter(parentList);
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