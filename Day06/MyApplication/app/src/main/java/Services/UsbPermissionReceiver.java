package Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;

public class UsbPermissionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("com.example.myapplication.USB_PERMISSION".equals(intent.getAction())) {
            synchronized (this) {
                UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                boolean granted = intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false);

                if (device != null && granted) {
                    Log.d("UsbPermissionReceiver", "Permission granted for device " + device.getDeviceName());
                    // Khởi chạy lại service để tiếp tục
                    Intent serviceIntent = new Intent(context, ChargingForegroundService.class);
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        context.startForegroundService(serviceIntent);
                    } else {
                        context.startService(serviceIntent);
                    }
                } else {
                    Log.w("UsbPermissionReceiver", "Permission denied for USB device.");
                }
            }
        }
    }
}
