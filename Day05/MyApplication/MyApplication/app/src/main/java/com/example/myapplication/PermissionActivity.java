package com.example.myapplication;

import android.Manifest;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import Adapter.ImageAdapter;

public class PermissionActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Uri> imageUris = new ArrayList<>();
    private ImageAdapter imageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        recyclerView = findViewById(R.id.recyclerViewPermission);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3)); // Hiển thị dạng lưới 3 cột
        imageAdapter = new ImageAdapter(this, imageUris);
        recyclerView.setAdapter(imageAdapter);

        xinQuyen();
    }
    private void xinQuyen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                docHinhDaTai();
            } else {
                requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 123);
            }
        } else {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                docHinhDaTai();
            } else {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            docHinhDaTai();
        }
    }

    private void docHinhDaTai() {
        Uri collection = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
                ? MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
                : MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME
        };

        String selection = MediaStore.Images.Media.RELATIVE_PATH + " LIKE ?";
        String[] selectionArgs = new String[]{"%Download/"};

        Cursor cursor = getContentResolver().query(
                collection,
                projection,
                selection,
                selectionArgs,
                MediaStore.Images.Media.DATE_ADDED + " DESC"
        );

        if (cursor != null) {
            imageUris.clear();
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);

            while (cursor.moveToNext()) {
                long id = cursor.getLong(idColumn);
                Uri uri = ContentUris.withAppendedId(collection, id);
                imageUris.add(uri);
            }
            cursor.close();
            imageAdapter.notifyDataSetChanged();
        }
    }

}