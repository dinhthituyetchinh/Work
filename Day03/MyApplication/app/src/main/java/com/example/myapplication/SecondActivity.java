package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.Map;

import Adapter.ParentAdapter;
import Database.FirebaseHelper;
import Model.CategoryEntity;

public class SecondActivity extends AppCompatActivity {
    private RecyclerView rcParent;
    private ParentAdapter parentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        rcParent = findViewById(R.id.rcParent);

        parentAdapter = new ParentAdapter(new HashMap<>(), ParentAdapter.Mode.STATIC, (staticUrl, gifUrl) -> {
            Intent intent = new Intent(SecondActivity.this, DetailActivity.class);
            intent.putExtra("staticUrl", staticUrl);
            intent.putExtra("gifUrl", gifUrl);
            startActivity(intent);
        });

        rcParent.setLayoutManager(new LinearLayoutManager(this));
        rcParent.setAdapter(parentAdapter);

        // Fetch data từ Firebase và cập nhật adapter
        FirebaseHelper.fetchAllParents(new FirebaseHelper.OnDataFetchedListener() {
            @Override
            public void onDataFetched(Map<String, CategoryEntity> data) {
                parentAdapter.updateData(data); // ✅ Sử dụng hàm updateData() từ adapter
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(SecondActivity.this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
