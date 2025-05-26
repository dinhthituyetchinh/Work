package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;

import java.util.List;

import Model.Child2;


public class ChildAdapter2 extends RecyclerView.Adapter<ChildAdapter2.ChildViewHolder> {

    private Context context;
    private List<Child2> childList;

    public ChildAdapter2(Context context, List<Child2> childList) {
        this.context = context;
        this.childList = childList;
    }

    @NonNull
    @Override
    public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_child_2, parent, false);
        return new ChildViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChildViewHolder holder, int position) {
        Child2 child = childList.get(position);

        Glide.with(context)
                .load(child.getImgChild())
                .into(holder.imgItemChild);

        holder.txtItemName.setText(child.getName());
    }

    @Override
    public int getItemCount() {
        return childList.size();
    }

    public static class ChildViewHolder extends RecyclerView.ViewHolder {
        ImageView imgItemChild;
        TextView txtItemName;

        public ChildViewHolder(@NonNull View itemView) {
            super(itemView);
            imgItemChild = itemView.findViewById(R.id.imgItemChild);
            txtItemName = itemView.findViewById(R.id.txtItemName);
        }
    }
}
