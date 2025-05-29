package Adapter;

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

import Model.Child;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ChildViewHolder> {
    public interface OnChildClickListener {
        void onClick(String staticUrl, String gifUrl);
    }

    private List<String> staticUrls = new ArrayList<>();
    private List<String> gifUrls = new ArrayList<>();
    private Mode mode;
    private OnChildClickListener listener;

    public enum Mode {
        STATIC, GIF
    }

    public ChildAdapter(List<Child> childList, Mode mode, OnChildClickListener listener) {
        this.mode = mode;
        this.listener = listener;

        for (Child child : childList) {
            List<String> statics = child.getStaticImages();
            List<String> gifs = child.getGifImages();
            for (int i = 0; i < statics.size(); i++) {
                staticUrls.add(statics.get(i));
                gifUrls.add(gifs.get(i));
            }
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
        String url = (mode == Mode.STATIC) ? staticUrls.get(position) : gifUrls.get(position);
        Glide.with(holder.itemView.getContext()).load(url).into(holder.imgChild);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(staticUrls.get(position), gifUrls.get(position));
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
}