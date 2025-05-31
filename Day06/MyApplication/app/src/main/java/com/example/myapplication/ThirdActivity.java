package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import Adapter.ChildAdapter;
import Model.AnimationEntity;

public class ThirdActivity extends AppCompatActivity {
    ChildAdapter adapter;
    List<AnimationEntity> childList;
    RecyclerView recyclerView;
    private TextView tvBack;

    private ActivityResultLauncher<String> requestPermissionLauncher;
    private boolean isPermissionGranted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        tvBack = findViewById(R.id.tvBack);
        tvBack.setOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.rcFullList);

        // 1. Khởi tạo launcher xin quyền
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    isPermissionGranted = isGranted;
                    if (isGranted) {
                        Toast.makeText(this, "Đã cấp quyền xem ảnh", Toast.LENGTH_SHORT).show();
                        adapter.notifyDataSetChanged(); // cập nhật lại hiển thị
                    } else {
                        Toast.makeText(this, "Bạn cần cấp quyền để xem ảnh", Toast.LENGTH_SHORT).show();
                    }
                });

        // 2. Load danh sách truyền vào
        String json = getIntent().getStringExtra("childListJson");
        if (json == null || json.isEmpty()) {
            finish();
            return;
        }

        Type listType = new TypeToken<List<AnimationEntity>>() {}.getType();
        childList = new Gson().fromJson(json, listType);

        // 3. Tạo adapter với PermissionCallback
        adapter = new ChildAdapter(
                this,
                childList,
                ChildAdapter.Mode.STATIC,
                (staticUrl, gifUrl, gifName) -> {
                    Intent intent = new Intent(this, DetailActivity.class);
                    intent.putExtra("gifUrl", gifUrl);
                    intent.putExtra("gifName", gifName);
                    startActivity(intent);
                },
                () -> checkPermissionAndLoadImages() // callback gọi xin quyền
        );

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(adapter);
    }

    private void checkPermissionAndLoadImages() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
            } else {
                isPermissionGranted = true;
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            } else {
                isPermissionGranted = true;
            }
        }
    }
}
