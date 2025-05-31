package Utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MediaUtils {

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
public List<String> getNameImg(Context context) {
    List<String> fileNames = new ArrayList<>();

    Uri collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);

    String[] projection = {
            MediaStore.Images.Media.DISPLAY_NAME // Chỉ lấy tên file
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

    public static boolean isGifExistsInAppStorage(Context context, String gifName) {
        File gifFile = new File(
                new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "MyGifs"),
                gifName + ".gif"
        );
        Log.d("DEBUG", "Checking file: " + gifFile.getAbsolutePath());
        Log.d("DEBUG", "File exists: " + gifFile.exists());
        return gifFile.exists();
    }

}
