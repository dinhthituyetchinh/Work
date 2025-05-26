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
import com.example.myapplication.ThirdActivity;

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

        // Setup child adapter
        ChildAdapter childAdapter = new ChildAdapter(parent.getListItemChild());
        holder.rcChild.setAdapter(childAdapter);
        holder.rcChild.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    public int getItemCount() {
        return parentList.size();
    }

    public static class ParentViewHolder extends RecyclerView.ViewHolder {
        TextView titleParent;
        RecyclerView rcChild;
        TextView seeAllTextView;


        public ParentViewHolder(@NonNull View itemView) {
            super(itemView);
            titleParent = itemView.findViewById(R.id.titleParent);
            rcChild = itemView.findViewById(R.id.rcChild);
            seeAllTextView = itemView.findViewById(R.id.seeAll);

            seeAllTextView.setOnClickListener(v -> {
                Context context = v.getContext();
                Intent intent = new Intent(context, ThirdActivity.class);
                context.startActivity(intent);
            });
        }
    }
}
