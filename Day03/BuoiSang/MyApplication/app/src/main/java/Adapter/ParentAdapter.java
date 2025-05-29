package Adapter;

import android.content.Context;
import android.content.Intent;
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

import java.util.List;

import Model.Parent;

public class ParentAdapter extends RecyclerView.Adapter<ParentAdapter.ParentViewHolder> {

    public interface OnChildClickListener {
        void onClick(String staticUrl, String gifUrl);
    }

    private Mode mode;
    private OnChildClickListener listener;

    public enum Mode {
        STATIC, GIF
    }
    private List<Parent> parentList;

//    public ParentAdapter(List<Parent> parentList) {
//        this.parentList = parentList;
//    }

    public ParentAdapter(List<Parent> parentList, Mode mode, OnChildClickListener listener) {
        this.parentList = parentList;
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
        Parent parent = parentList.get(position);
        holder.titleParent.setText(parent.getTitle());

       // ChildAdapter adapter = new ChildAdapter(parent.getListItemChild(), ChildAdapter.Mode.STATIC, null);
        // Tạo ChildAdapter với listener callback
        ChildAdapter adapter = new ChildAdapter(
                parent.getListItemChild(),
                mode == Mode.STATIC ? ChildAdapter.Mode.STATIC : ChildAdapter.Mode.GIF,
                (staticUrl, gifUrl) -> {
                    if (listener != null) {
                        listener.onClick(staticUrl, gifUrl);
                    }
                });
        holder.rcChild.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        holder.rcChild.setAdapter(adapter);

        holder.seeAllTextView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, ThirdActivity.class);
            intent.putExtra("title", parent.getTitle());
            intent.putExtra("childListJson", new Gson().toJson(parent.getListItemChild()));
            context.startActivity(intent);
        });
        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "Click parent: " + parent.getTitle(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return parentList.size();
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