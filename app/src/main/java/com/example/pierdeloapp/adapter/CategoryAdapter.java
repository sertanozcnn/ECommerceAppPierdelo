package com.example.pierdeloapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pierdeloapp.ItemsActivity;
import com.example.pierdeloapp.R;
import com.example.pierdeloapp.domain.Category;


import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private Context context;
    private List<Category> mCategoryList;
    public CategoryAdapter(Context context, List<Category> mCategoryList) {
        this.context = context;
        this.mCategoryList = mCategoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_category_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Glide.with(context).load(mCategoryList.get(holder.getAdapterPosition()).getImg_url()).into(holder.mTypeImg);
        holder.mText.setText(mCategoryList.get(position).getText());
        holder.mTypeImg.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                Intent intent = new Intent(context, ItemsActivity.class);
                intent.putExtra("type", mCategoryList.get(adapterPosition).getType());
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mCategoryList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView mTypeImg;
        private TextView mText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTypeImg = itemView.findViewById(R.id.category_img);
            mText = itemView.findViewById(R.id.category_text);
        }
    }
}
