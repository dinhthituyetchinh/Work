package Adapter;

import android.app.Activity;
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

import com.example.myapplication.DetailActivity;
import com.example.myapplication.R;
import com.example.myapplication.ThirdActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


import Model.AnimationEntity;
import Model.CategoryEntity;


public class ParentAdapter extends RecyclerView.Adapter<ParentAdapter.ParentViewHolder> {

    public interface OnChildClickListener {
        void onClick(String staticUrl, String gifUrl, String name);
    }

    public enum Mode {
        STATIC, GIF
    }

    private Mode mode;
    private OnChildClickListener listener;
    private List<CategoryEntity> parentList;
    private Activity activity;

    public ParentAdapter(Activity activity, List<CategoryEntity> parentList, Mode mode, OnChildClickListener listener) {
        this.activity = activity;
        this.parentList = parentList != null ? parentList : new ArrayList<>();
        this.mode = mode;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ParentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_parent, parent, false);
        return new ParentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParentViewHolder holder, int position) {
        if (position >= parentList.size()) return;

        CategoryEntity categoryEntity = parentList.get(position);
        String categoryName = categoryEntity.getCategoryName();
        List<AnimationEntity> childList = categoryEntity.getList();

        holder.titleParent.setText(categoryName);

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
                        Intent intent = new Intent(activity, DetailActivity.class);
                        intent.putExtra("gifUrl", gifUrl);
                        intent.putExtra("gifName", name);
                        activity.startActivityForResult(intent, 1001);
                    },
                    () -> {
                        if (activity instanceof ChildAdapter.PermissionCallback) {
                            ((ChildAdapter.PermissionCallback) activity).onRequestPermission();
                        }
                    }
            );

            holder.rcChild.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            holder.rcChild.setAdapter(adapter);
        }

        holder.seeAllTextView.setOnClickListener(v -> {
            Context context = holder.itemView.getContext();
            Intent intent = new Intent(context, ThirdActivity.class);
            intent.putExtra("title", categoryName);
            intent.putExtra("childListJson", new Gson().toJson(categoryEntity.getList()));
            context.startActivity(intent);
        });

        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(holder.itemView.getContext(), "Click parent: " + categoryName, Toast.LENGTH_SHORT).show();
        });


        Log.d("ParentAdapter", "Binding: " + categoryName + ", children: " + (childList != null ? childList.size() : 0));
    }

    @Override
    public int getItemCount() {
        return parentList != null ? parentList.size() : 0;
    }

    public void updateData(List<CategoryEntity> newList) {
        if (newList != null) {
            this.parentList.clear();
            this.parentList.addAll(newList);
            notifyDataSetChanged();
        }
    }

    public static class ParentViewHolder extends RecyclerView.ViewHolder {
        TextView titleParent, seeAllTextView ;
        RecyclerView rcChild;

        public ParentViewHolder(@NonNull View itemView) {
            super(itemView);
            titleParent = itemView.findViewById(R.id.titleParent);
            seeAllTextView = itemView.findViewById(R.id.seeAll);
            rcChild = itemView.findViewById(R.id.rcChild);

        }
    }
}
