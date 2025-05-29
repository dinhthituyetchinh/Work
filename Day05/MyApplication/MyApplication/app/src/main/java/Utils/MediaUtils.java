package Utils;

import static android.content.ContentValues.TAG;

import android.content.ContentUris;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MediaUtils {

    //    public static boolean checkExist(Context context, String name) {
//        if (name == null) return false;
//
//        String fileName = name + ".gif";
//        String[] projection = {MediaStore.Images.Media._ID};
//
//        String selection = MediaStore.Images.Media.DISPLAY_NAME + "=? AND " +
//                MediaStore.Images.Media.MIME_TYPE + "=?";
//        String[] selectionArgs = {fileName, "image/gif"};
//
//        try (Cursor cursor = context.getContentResolver().query(
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                projection,
//                selection,
//                selectionArgs,
//                null
//        )) {
//            return cursor != null && cursor.moveToFirst();
//        }
//    }
    public static boolean checkExist(Context context, String fileNameWithoutExt) {
        if (fileNameWithoutExt == null) return false;
        String fileName = fileNameWithoutExt + ".gif";
        String[] projection = {MediaStore.Images.Media._ID};
        String selection = MediaStore.Images.Media.DISPLAY_NAME + "=? AND " +
                MediaStore.Images.Media.MIME_TYPE + "=?";
        String[] selectionArgs = {fileName, "image/gif"};

        try (Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null
        )) {
            return cursor != null && cursor.moveToFirst();
        }
    }

//    public List<Uri> getNameImg(Context context) {
//        List<Uri> imageUris = new ArrayList<>();
//
//        Uri collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
//
//        String[] projection = {
//                MediaStore.Images.Media._ID
//        };
//
//        try (Cursor cursor = context.getContentResolver().query(
//                collection,
//                projection,
//                null,
//                null,
//                MediaStore.Images.Media.DATE_ADDED + " DESC"
//        )) {
//            if (cursor != null) {
//                int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
//
//                while (cursor.moveToNext()) {
//                    long id = cursor.getLong(idColumn);
//                    Uri contentUri = ContentUris.withAppendedId(
//                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
//                    imageUris.add(contentUri);
//                }
//            }
//        }
//
//        return imageUris;
//    }
public List<String> getNameImg(Context context) {
    List<String> fileNames = new ArrayList<>();

    Uri collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);

    String[] projection = {
            MediaStore.Images.Media.DISPLAY_NAME // ✅ Chỉ lấy tên file
    };

    try (Cursor cursor = context.getContentResolver().query(
            collection,
            projection,
            null,
            null,
            MediaStore.Images.Media.DATE_ADDED + " DESC"
    )) {
        if (cursor != null) {
            int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);

            while (cursor.moveToNext()) {
                String name = cursor.getString(nameColumn);
                fileNames.add(name);
            }
        }
    }

    return fileNames;
}

    private ContentObserver mediaObserver;

    public void registerMediaObserver(Context context) {
        mediaObserver = new ContentObserver(new Handler(Looper.getMainLooper())) {
            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                Log.d("MediaObserver", "MediaStore changed, reloading images...");

                // Gọi lại hàm lấy dữ liệu ảnh
                List<String> updatedImages = getNameImg(context);

//                // Ví dụ: Cập nhật UI
//                for (Uri name : updatedImages) {
//                    Log.d("Observer", "onChange: "+name);
//                }
            }
        };

        context.getContentResolver().registerContentObserver(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                true,
                mediaObserver
        );
    }

}
