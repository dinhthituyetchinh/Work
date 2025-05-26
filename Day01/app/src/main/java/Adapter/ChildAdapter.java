package Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;

import java.util.List;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ChildViewHolder> {
    private List<String> list;

    public ChildAdapter(List<String> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_child, parent, false);
        return new ChildViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChildViewHolder holder, int position) {
        String url = list.get(position);
        // Load ảnh từ URL nếu có, hoặc set hình mặc định
        Glide.with(holder.itemView.getContext()).load(url).into(holder.imgChild);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ChildViewHolder extends RecyclerView.ViewHolder {
        ImageView imgChild;

        public ChildViewHolder(@NonNull View itemView) {
            super(itemView);
            imgChild = itemView.findViewById(R.id.imgItemChild);
        }
    }
}
