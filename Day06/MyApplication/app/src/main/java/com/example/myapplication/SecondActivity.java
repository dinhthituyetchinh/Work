package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.ChildAdapter;
import Adapter.ParentAdapter;
import Database.FirebaseHelper;
import Model.CategoryEntity;
import Utils.MediaUtils;

public class SecondActivity extends AppCompatActivity {
    private RecyclerView rcParent;
    private ParentAdapter parentAdapter;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        rcParent = findViewById(R.id.rcParent);

        // Đăng ký xin quyền
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        Toast.makeText(this, "Đã cấp quyền", Toast.LENGTH_SHORT).show();
                        // Gọi lại adapter.notifyDataSetChanged() nếu cần
                    } else {
                        Toast.makeText(this, "Từ chối quyền", Toast.LENGTH_SHORT).show();
                    }
                }
        );


        parentAdapter = new ParentAdapter(this, new ArrayList<>(), ParentAdapter.Mode.STATIC, (staticUrl, gifUrl, name) -> {
            Intent intent = new Intent(SecondActivity.this, DetailActivity.class);
            intent.putExtra("staticUrl", staticUrl);
            intent.putExtra("gifUrl", gifUrl);
            intent.putExtra("gifName", name);
            startActivity(intent);
        });


        rcParent.setLayoutManager(new LinearLayoutManager(this));
        rcParent.setAdapter(parentAdapter);

        FirebaseHelper.fetchAllParents(new FirebaseHelper.OnListDataFetchedListener() {
            @Override
            public void onDataFetched(List<CategoryEntity> data) {
                parentAdapter.updateData(data);
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(SecondActivity.this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            if (parentAdapter != null) {
                parentAdapter.notifyDataSetChanged(); // gọi để load lại hình động nếu đã tải
            }
        }
    }



}