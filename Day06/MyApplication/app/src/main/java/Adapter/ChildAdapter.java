package Adapter;

import static android.content.ContentValues.TAG;

import static Utils.MediaUtils.isGifExistsInAppStorage;
import static Utils.Permission.hasPermissionToRead;

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
import Utils.Permission;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ChildViewHolder> {

    public interface OnChildClickListener {
        void onClick(String staticUrl, String gifUrl, String name);
    }
    public interface PermissionCallback {
        void onRequestPermission();
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
    private PermissionCallback permissionCallback;
    public ChildAdapter(Context context, List<AnimationEntity> childList, Mode mode, OnChildClickListener listener, PermissionCallback permissionCallback) {
        this.context = context;
        this.mode = mode;
        this.listener = listener;
        this.childList = childList;
        this.permissionCallback = permissionCallback;

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

        //Sử dụng khi lưu vào bộ nhớ ngoài
//        if (Permission.hasPermissionToRead(context)) {
//            if (MediaUtils.checkExist(context, gifName)) {
//                Uri path = getAppSolutePath(context, gifName);
//                Glide.with(context)
//                        .asGif()
//                        .load(path)
//                        .skipMemoryCache(true)
//                        .diskCacheStrategy(DiskCacheStrategy.NONE)
//                        .into(imageView);
//            } else {
//                Glide.with(context).load(childModel.getStaticUrl()).into(imageView);
//                Log.d(TAG, "onBindViewHolder: " + childModel.getStaticUrl());
//            }
//        } else {
//            if (permissionCallback != null) {
//                permissionCallback.onRequestPermission(); // Gọi về Activity để xin quyền
//            }
//            Glide.with(context).load(childModel.getStaticUrl()).into(imageView); // fallback
//        }

        //Sử dụng khi lưu vào bộ nhớ trong (bộ nh ứng dụng)

        if (isGifExistsInAppStorage(context, childModel.getName())) {
            // Load file từ bộ nhớ ứng dụng
            File gifFile = new File(
                    context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/MyGifs",
                    childModel.getName() + ".gif"
            );

            Glide.with(context)
                    .asGif()
                    .placeholder(R.mipmap.loading)
                    .load(gifFile)
                    .into(imageView);
        } else {
            // Load static image từ URL
            Glide.with(context)
                    .load(childModel.getStaticUrl())
                    .placeholder(R.mipmap.loading)
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

    public static Uri getAppSolutePath(Context context, String fileNameWithoutExt) {
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