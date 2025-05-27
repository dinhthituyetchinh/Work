package Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.ThidActivity;
import com.google.gson.Gson;

import java.util.List;

import Model.Parent;

public class ParentAdapter extends RecyclerView.Adapter<ParentAdapter.ParentViewHolder> {
    private List<Parent> parentList;

    public ParentAdapter(List<Parent> parentList) {
        this.parentList = parentList;
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

        ChildAdapter adapter = new ChildAdapter(parent.getListItemChild(), ChildAdapter.Mode.STATIC, null);
        holder.rcChild.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        holder.rcChild.setAdapter(adapter);

        holder.seeAllTextView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, ThidActivity.class);
            intent.putExtra("title", parent.getTitle());
            intent.putExtra("childListJson", new Gson().toJson(parent.getListItemChild()));
            context.startActivity(intent);
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