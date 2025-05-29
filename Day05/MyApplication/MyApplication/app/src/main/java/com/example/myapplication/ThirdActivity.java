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
import androidx.annotation.Nullable;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        tvBack = findViewById(R.id.tvBack);
        tvBack.setOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.rcFullList);
        // Đăng ký ActivityResultLauncher
        requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        // Quyền đã được cấp

                    } else {
                        // Quyền bị từ chối
                        Toast.makeText(this, "Bạn cần cấp quyền để xem ảnh", Toast.LENGTH_SHORT).show();
                    }
                });

        String json = getIntent().getStringExtra("childListJson");

        if (json == null || json.isEmpty()) {
            finish();
            return;
        }

        Type listType = new TypeToken<List<AnimationEntity>>() {}.getType();
         childList = new Gson().fromJson(json, listType);

         adapter = new ChildAdapter(
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
    private void checkPermissionAndLoadImages() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                    == PackageManager.PERMISSION_GRANTED) {
                // Quyền đã được cấp

            } else {
                // Yêu cầu quyền
                requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
            }
        } else {
            // Android < 13: xử lý theo READ_EXTERNAL_STORAGE hoặc logic khác
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001 && resultCode == RESULT_OK) {
            // Load lại adapter để hiển thị ảnh động nếu đã tải
            refreshChildAdapter();
        }
    }
    private void refreshChildAdapter() {
        if (adapter != null && childList != null) {
            adapter = new ChildAdapter(this, childList, ChildAdapter.Mode.STATIC, (staticUrl, gifUrl, name) -> {
                // Khi click item trong danh sách
                Intent intent = new Intent(this, DetailActivity.class);
                intent.putExtra("gifUrl", gifUrl);
                intent.putExtra("gifName", name);
                startActivityForResult(intent, 1001);
            });
            recyclerView.setAdapter(adapter);
        }
    }

}