package com.example.pierdeloapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pierdeloapp.DetailActivity;
import com.example.pierdeloapp.HomeActivity;
import com.example.pierdeloapp.R;
import com.example.pierdeloapp.domain.Items;

import java.util.List;

public class ItemsRecyclerAdapter extends RecyclerView.Adapter<ItemsRecyclerAdapter.ViewHolder>  {
    Context applicationContext;
    List<Items> mItemsList;
    public ItemsRecyclerAdapter(Context applicationContext, List<Items> mItemsList) {
        this.applicationContext =applicationContext;
        this.mItemsList=mItemsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(applicationContext ).inflate(R.layout.single_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.mCost.setText("₺ "+mItemsList.get(position).getPrice());
        holder.mName.setText(mItemsList.get(position).getName());
        holder.mItemRating.setRating(mItemsList.get(position).getRating());
        holder.mItemRatingText.setText(String.valueOf(mItemsList.get(position).getRating()));
        if(!(applicationContext instanceof HomeActivity)){
            Glide.with(applicationContext).load(mItemsList.get(position).getImg_url()).into(holder.mItemImage);

        }else
        {
            holder.mItemImage.setVisibility(View.GONE);
        }
        holder.mItemImage.setOnClickListener(v -> {
            Intent intent=new Intent(applicationContext, DetailActivity.class);
            intent.putExtra("detail",mItemsList.get(position));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            applicationContext.startActivity(intent);
        });
        holder.mName.setOnClickListener(v -> {
            Intent intent=new Intent(applicationContext, DetailActivity.class);
            intent.putExtra("detail",mItemsList.get(position));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            applicationContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mItemsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView mItemImage;
        private TextView mCost;
        private TextView mName;
        private RatingBar mItemRating;
        private TextView mItemRatingText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mItemImage=itemView.findViewById(R.id.item_image);
            mCost=itemView.findViewById(R.id.item_cost);
            mName=itemView.findViewById(R.id.item_nam);
            mItemRating = itemView.findViewById(R.id.item_rating);
            mItemRatingText = itemView.findViewById(R.id.item_rating_text);
        }
    }

}