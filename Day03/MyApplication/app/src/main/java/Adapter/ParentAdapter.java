package Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.ThirdActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Model.AnimationEntity;
import Model.CategoryEntity;

public class ParentAdapter extends RecyclerView.Adapter<ParentAdapter.ParentViewHolder> {

    public interface OnChildClickListener {
        void onClick(String staticUrl, String gifUrl);
    }

    public enum Mode {
        STATIC, GIF
    }

    private Mode mode;
    private OnChildClickListener listener;
    private Map<String, CategoryEntity> parentMap;
    private List<String> parentKeys;

    public ParentAdapter(Map<String, CategoryEntity> parentMap, Mode mode, OnChildClickListener listener) {
        this.parentMap = parentMap != null ? parentMap : new java.util.HashMap<>();
        this.mode = mode;
        this.listener = listener;
        this.parentKeys = new ArrayList<>(this.parentMap.keySet());
    }

    @NonNull
    @Override
    public ParentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_parent, parent, false);
        return new ParentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParentViewHolder holder, int position) {
        if (position >= parentKeys.size()) return;

        String key = parentKeys.get(position);
        CategoryEntity categoryEntity = parentMap.get(key);

        if (categoryEntity == null) {
            holder.titleParent.setText("Unknown");
            holder.rcChild.setVisibility(View.GONE);
            return;
        }

        holder.titleParent.setText(key);
        List<AnimationEntity> childList = categoryEntity.getList();

        if (childList == null || childList.isEmpty()) {
            holder.rcChild.setVisibility(View.GONE);
        } else {
            holder.rcChild.setVisibility(View.VISIBLE);

            Context context = holder.itemView.getContext();
            ChildAdapter adapter = new ChildAdapter(
                    context,
                    childList,
                    mode == Mode.STATIC ? ChildAdapter.Mode.STATIC : ChildAdapter.Mode.GIF,
                    (staticUrl, gifUrl, name) -> {
                        if (listener != null) {
                            listener.onClick(staticUrl, gifUrl);
                        }
                    });

            holder.rcChild.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            holder.rcChild.setAdapter(adapter);
        }

        holder.seeAllTextView.setOnClickListener(v -> {
            Context context = holder.itemView.getContext();
            Intent intent = new Intent(context, ThirdActivity.class);
            intent.putExtra("title", key);
            intent.putExtra("childListJson", new Gson().toJson(categoryEntity.getList()));
            context.startActivity(intent);
        });

        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(holder.itemView.getContext(), "Click parent: " + key, Toast.LENGTH_SHORT).show();
        });

        // Debug log
        Log.d("ParentAdapter", "Binding key: " + key + ", children: " + (childList != null ? childList.size() : 0));
    }

    @Override
    public int getItemCount() {
        return parentKeys != null ? parentKeys.size() : 0;
    }

    public void updateData(Map<String, CategoryEntity> newParentMap) {
        if (newParentMap != null) {
            this.parentMap.clear();
            this.parentMap.putAll(newParentMap);
            this.parentKeys = new ArrayList<>(newParentMap.keySet());
            notifyDataSetChanged();
        }
    }

    public static class ParentViewHolder extends RecyclerView.ViewHolder {
        TextView titleParent, seeAllTextView;
        RecyclerView rcChild;

        public ParentViewHolder(@NonNull View itemView) {
            super(itemView);
            titleParent = itemView.findViewById(R.id.titleParent);
            seeAllTextView = itemView.findViewById(R.id.seeAll);
            rcChild = itemView.findViewById(R.id.rcChild);
        }
    }
}
