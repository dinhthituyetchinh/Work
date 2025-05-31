package com.example.myapplication;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.File;

public class ChargingActivity extends AppCompatActivity {


    ImageView imgFullScreen;
    private TextView textBelowImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ChargingActivity", "onCreate: ChargingActivity được gọi");

        // Đánh thức màn hình nếu đang tắt
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        );

        setContentView(R.layout.activity_charging);

        imgFullScreen = findViewById(R.id.imgFullScreen);

        // Lấy ảnh đang được Apply từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String appliedPath = sharedPreferences.getString("applied_path", null);

        Log.d("ChargingActivity", "applied_path từ SharedPreferences: " + appliedPath);

        if (appliedPath != null) {
            File gifFile = new File(appliedPath);
            Log.d("ChargingActivity", "File tồn tại: " + gifFile.exists() + ", Đường dẫn: " + gifFile.getAbsolutePath());

            if (gifFile.exists()) {
                Log.d("ChargingActivity", "Bắt đầu load ảnh GIF với Glide");
                Glide.with(this)
                        .asGif()
                        .load(gifFile)
                        .into(imgFullScreen);
            } else {
                Toast.makeText(this, "Ảnh đã áp dụng không còn tồn tại!", Toast.LENGTH_SHORT).show();
                Log.e("ChargingActivity", "GIF file không tồn tại tại đường dẫn: " + gifFile.getAbsolutePath());
            }
        } else {
            Toast.makeText(this, "Chưa có ảnh nào được áp dụng!", Toast.LENGTH_SHORT).show();
            Log.e("ChargingActivity", "applied_path rỗng hoặc null trong SharedPreferences");
        }

        textBelowImage = findViewById(R.id.textBelowImage);

        updateChargingType();
    }

    private void updateChargingType() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = registerReceiver(null, ifilter);

        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);

        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
        boolean wirelessCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_WIRELESS;

        String chargingType;
        if (usbCharge) {
            chargingType = "Đang sạc qua USB";
        } else if (acCharge) {
            chargingType = "Đang sạc nhanh (ổ cắm AC)";
        } else if (wirelessCharge) {
            chargingType = "Đang sạc không dây";
        } else {
            chargingType = "Không sạc";
        }

        textBelowImage.setText(chargingType);
    }
}
