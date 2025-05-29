package Adapter;

import static android.content.ContentValues.TAG;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.myapplication.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import Model.AnimationEntity;
import Utils.MediaUtils;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ChildViewHolder> {

    public interface OnChildClickListener {
        void onClick(String staticUrl, String gifUrl, String name);
    }

    private static final String RELATIVE_PATH = Environment.DIRECTORY_PICTURES + "/MyGifs/";

    private List<String> staticUrls = new ArrayList<>();
    private List<String> gifUrls = new ArrayList<>();
    private List<String> nameUrls = new ArrayList<>();
    private Mode mode;
    private OnChildClickListener listener;
    private List<AnimationEntity> childList;

    private Context context;

    public enum Mode {
        STATIC, GIF
    }

    public ChildAdapter(Context context, List<AnimationEntity> childList, Mode mode, OnChildClickListener listener) {
        this.context = context;
        this.mode = mode;
        this.listener = listener;
        this.childList = childList;

        // Load dữ liệu từ childList
        for (AnimationEntity entity : childList) {
            String staticUrl = entity.getStaticUrl(); // đã decode trong model
            String gifUrl = entity.getGif();          // đã decode trong model
            String name = entity.getName();

            staticUrls.add(staticUrl);
            gifUrls.add(gifUrl);
            nameUrls.add(name);
        }
    }

    @NonNull
    @Override
    public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_child, parent, false);
        return new ChildViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChildViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
//
        String staticUrl = staticUrls.get(position);
        String gifUrl = gifUrls.get(position);
        String name = nameUrls.get(position);
        ImageView imageView = holder.imgChild;
        AnimationEntity childModel = childList.get(position);
        String gifName = childModel.getName();

        if (MediaUtils.checkExist(context, gifName)) {
            Uri path = getImageUriByName(context, gifName);
            Glide.with(context)
                    .asGif()
                    .load(path)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(imageView);
        } else {
            Glide.with(context)
                    .load(childModel.getStaticUrl())
                    .into(imageView);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(staticUrl, gifUrl, name);
            }
        });
    }

    @Override
    public int getItemCount() {
        return childList != null ? childList.size() : 0;
    }

    public static class ChildViewHolder extends RecyclerView.ViewHolder {
        ImageView imgChild;

        public ChildViewHolder(@NonNull View itemView) {
            super(itemView);
            imgChild = itemView.findViewById(R.id.imgItemChild);
        }
    }

//    public Uri getAppSolutePath(String name) {
//        if (name == null) return null;
//
//        String fileName = name + ".gif";
//
//        String[] projection = {
//                MediaStore.Images.Media._ID
//        };
//
//        String selection = MediaStore.Images.Media.DISPLAY_NAME + "=? AND " +
//                MediaStore.Images.Media.MIME_TYPE + "=?";
//        Log.d(TAG, "getAppSolutePath: " + fileName);
//        String[] selectionArgs = {fileName, "image/gif"};
//        Log.d(TAG, "getAppSolutePath: " + selectionArgs);
//        try (Cursor cursor = context.getContentResolver().query(
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                projection,
//                selection,
//                selectionArgs,
//                null
//        )) {
//            if (cursor != null && cursor.moveToFirst()) {
//                long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
//                Uri uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
//                Log.d(TAG, "Found URI: " + uri.toString());
//                return uri;
//            }
//        }
//        return null;
//    }

    public static Uri getImageUriByName(Context context, String fileNameWithoutExt) {
        if (fileNameWithoutExt == null) return null;

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
            if (cursor != null && cursor.moveToFirst()) {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
                return ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
            }
        }
        return null;
    }

}