package Adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import Model.AnimationEntity;

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
    private Context context;

    public enum Mode {
        STATIC, GIF
    }

    public ChildAdapter(Context context, List<AnimationEntity> childList, Mode mode, OnChildClickListener listener) {
        this.context = context;
        this.mode = mode;
        this.listener = listener;

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

        String staticUrl = staticUrls.get(position);
        String gifUrl = gifUrls.get(position);
        String name = nameUrls.get(position);
        // So sánh tên tệp đã lưu với name + ".gif"
        Uri gifUri = getDownloadedGifUriByName(context, name);

        if (gifUri != null) {
            // Load local GIF
            Glide.with(context)
                    .asGif()
                    .load(gifUri)
                    .into(holder.imgChild);
        } else {
            // Load ảnh tĩnh từ URL
            Glide.with(context)
                    .load(staticUrl)
                    .into(holder.imgChild);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(staticUrl, gifUrl, name);
            }
        });
    }

    @Override
    public int getItemCount() {
        return staticUrls.size();
    }

    public static class ChildViewHolder extends RecyclerView.ViewHolder {
        ImageView imgChild;

        public ChildViewHolder(@NonNull View itemView) {
            super(itemView);
            imgChild = itemView.findViewById(R.id.imgItemChild);
        }
    }

    private Uri getDownloadedGifUriByName(Context context, String name) {
        if (name == null) return null;

        String fileName = name + ".gif";

        String[] projection = {MediaStore.Images.Media._ID};
        String selection = MediaStore.Images.Media.RELATIVE_PATH + "=? AND " +
                MediaStore.Images.Media.DISPLAY_NAME + "=?";
        String[] selectionArgs = {RELATIVE_PATH, fileName};

        try (android.database.Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null)) {

            if (cursor != null && cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
                return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, String.valueOf(id));
            }
        }

        return null;
    }

}