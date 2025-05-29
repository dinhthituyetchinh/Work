package com.example.myapplication;

import static Utils.MediaUtils.checkExist;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.ByteArrayOutputStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import Utils.MediaUtils;

public class DetailActivity extends AppCompatActivity {
    TextView tvBack;
    String gifUrl;
    Button btnDownload;
    View loadingLayout;
    private static final int REQUEST_PERMISSION_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
         tvBack = findViewById(R.id.tvBackDetail);
         tvBack.setOnClickListener(v -> finish());

        ImageView imgLoad = findViewById(R.id.my_image_view);
        ImageView imageView = findViewById(R.id.imgGifDetail);

        btnDownload = findViewById(R.id.btnDownloadGif);
        loadingLayout = findViewById(R.id.loading_layout);
        btnDownload.setVisibility(View.GONE);



        Animation rotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
        imgLoad.startAnimation(rotate);

        gifUrl = getIntent().getStringExtra("gifUrl");

        // Load GIF bằng Glide + lắng nghe trạng thái

        Glide.with(this)
                .asGif()
                .load(gifUrl)
                .listener(new RequestListener<GifDrawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e,
                                                Object model,
                                                Target<GifDrawable> target,
                                                boolean isFirstResource) {
                        // Tắt animation nếu lỗi
                        imgLoad.clearAnimation();
                        imgLoad.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GifDrawable resource,
                                                   Object model,
                                                   Target<GifDrawable> target,
                                                   DataSource dataSource,
                                                   boolean isFirstResource) {
                        // Khi ảnh load xong thành công -> tắt animation loading
                        imgLoad.clearAnimation();
                        imgLoad.setVisibility(View.GONE);
                        btnDownload.setVisibility(View.VISIBLE);
                        return false; // tiếp tục hiển thị GIF như bình thường
                    }
                })
                .into(imageView);

        String gifName = getIntent().getStringExtra("gifName");

        btnDownload.setOnClickListener(v -> {
            if (checkExist(this, gifName)) {
                btnDownload.setVisibility(View.GONE);
                Toast.makeText(this, "GIF đã tồn tại", Toast.LENGTH_SHORT).show();

                return;
            }
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
                } else {
                    downloadGif(gifUrl);
                }
            } else {
                downloadGif(gifUrl); // Android 10+ không cần xin WRITE_EXTERNAL_STORAGE
            }
        });



    }

    private void downloadGif(String gifUrl) {
        new Thread(() -> {
            try {
                URL url = new URL(gifUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream input = connection.getInputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;
                ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

                while ((bytesRead = input.read(buffer)) != -1) {
                    byteBuffer.write(buffer, 0, bytesRead);
                }

                byte[] gifBytes = byteBuffer.toByteArray();
                input.close();
                byteBuffer.close();

                // Lưu vào Gallery
                runOnUiThread(() -> saveGifToGallery(gifBytes));
            } catch (Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(this, "Tải thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadGif(gifUrl);
            } else {
                Toast.makeText(this, "Không có quyền ghi bộ nhớ!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveGifToGallery(byte[] gifBytes) {
        String gifName = getIntent().getStringExtra("gifName");
        Log.d("DetailActivity", "gifName from Intent: " + gifName);
        String fileName = gifName + ".gif";

        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
        values.put(MediaStore.MediaColumns.MIME_TYPE, "image/gif");
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/MyGifs");

        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        try {
            OutputStream outputStream = getContentResolver().openOutputStream(uri);
            outputStream.write(gifBytes);
            outputStream.flush();
            outputStream.close();

            Toast.makeText(this, "Đã lưu vào Thư viện", Toast.LENGTH_SHORT).show();
            btnDownload.setVisibility(View.GONE);
            // Gửi kết quả về
            setResult(RESULT_OK);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi lưu GIF", Toast.LENGTH_SHORT).show();
        }
    }





}