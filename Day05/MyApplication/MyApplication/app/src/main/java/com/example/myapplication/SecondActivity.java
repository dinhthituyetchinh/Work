package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
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
    @Override
    protected void onResume() {
        super.onResume();

        String restoredFileName = "a1.gif";
        File restoredFile = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera/" + restoredFileName);

        if (restoredFile.exists()) {
            MediaScannerConnection.scanFile(
                    this,
                    new String[]{ restoredFile.getAbsolutePath() },
                    null,
                    (path, uri) -> {
                        Log.d("ScanSingleFile", "Restored image scanned: " + uri);

                        // Gọi lại danh sách ảnh nếu muốn cập nhật UI
                        MediaUtils mediaUtils = new MediaUtils();
                        List<String> updatedUris = mediaUtils.getNameImg(this);
                        Log.d("ScanSingleFile", "list"+updatedUris);
                        // Cập nhật RecyclerView adapter nếu cần
                    }
            );
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        rcParent = findViewById(R.id.rcParent);


        parentAdapter = new ParentAdapter(this, new HashMap<>(), ParentAdapter.Mode.STATIC, (staticUrl, gifUrl, name) -> {
            Intent intent = new Intent(SecondActivity.this, DetailActivity.class);
            intent.putExtra("staticUrl", staticUrl);
            intent.putExtra("gifUrl", gifUrl);
            intent.putExtra("gifName", name);
            startActivity(intent);
        });

        rcParent.setLayoutManager(new LinearLayoutManager(this));
        rcParent.setAdapter(parentAdapter);

        // Fetch data từ Firebase và cập nhật adapter
        FirebaseHelper.fetchAllParents(new FirebaseHelper.OnDataFetchedListener() {
            @Override
            public void onDataFetched(Map<String, CategoryEntity> data) {
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