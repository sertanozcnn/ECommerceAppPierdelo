package com.example.pierdeloapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.pierdeloapp.R;
import com.example.pierdeloapp.domain.Items;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;
import io.github.muddz.styleabletoast.StyleableToast;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.ViewHolder> {

    private Context context;
    List<Items> itemsList;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    ItemRemoved itemRemoved;
    public CartItemAdapter(Context context,List<Items> itemsList ,  ItemRemoved itemRemoved) {
        this.context = context;
        this.itemsList = itemsList;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        this.itemRemoved = itemRemoved;
    }

    @NonNull
    @Override
    public CartItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_cart_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemAdapter.ViewHolder holder, int position) {
        holder.value.setText(String.valueOf(itemsList.get(position).getQuantity()));
        holder.cartAllPrice.setText(String.valueOf(itemsList.get(position).getPrice()*itemsList.get(position).getQuantity()));
        holder.cartName.setText(itemsList.get(position).getName());
        holder.cartPrice.setText(itemsList.get(position).getPrice() + " ₺");
        holder.cartDesc.setText(itemsList.get(position).getDescription());
        holder.removeItem.setOnClickListener(v -> firebaseFirestore.collection("Users")
                .document(firebaseAuth.getCurrentUser().getUid())
                .collection("Cart")
                .document(itemsList.get(position).getDocId())
                .delete().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        itemsList.remove(itemsList.get(position));
                        notifyDataSetChanged();
                        itemRemoved.OnItemRemoved(itemsList);
                        StyleableToast.makeText(holder.itemView.getContext(),context.getString(R.string.toast_cart_deleted), Toast.LENGTH_SHORT,R.style.infoToast).show();
                    }else{
                        StyleableToast.makeText(holder.itemView.getContext(),task.getException().getMessage(), Toast.LENGTH_SHORT,R.style.infoToast).show();
                    }
                }));
        Glide.with(holder.itemView.getContext()).load(itemsList.get(position).getImg_url()).into(holder.cartImage);
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cartImage;
        TextView cartName;
        TextView cartDesc;
        TextView cartPrice;
        TextView cartAllPrice;
        ImageView removeItem;
        Button decreaseButton;
        Button increaseButton;
        Button value;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cartImage = itemView.findViewById(R.id.cart_image);
            cartName = itemView.findViewById(R.id.card_name);
            cartDesc = itemView.findViewById(R.id.card_desc);
            cartPrice = itemView.findViewById(R.id.card_price);
            cartAllPrice = itemView.findViewById(R.id.card_all_price);
            decreaseButton = itemView.findViewById(R.id.buttonDecrease);
            removeItem = itemView.findViewById(R.id.remove_item);
            increaseButton = itemView.findViewById(R.id.buttonIncrease);
            value = itemView.findViewById(R.id.buttonValue);
            decreaseButton.setOnClickListener(v -> decrement());
            increaseButton.setOnClickListener(v -> increment());
        }

        public void increment() {
            int position = getAdapterPosition();
            int newQuantity = itemsList.get(position).getQuantity() + 1;
            itemsList.get(position).setQuantity(newQuantity);
            value.setText(String.valueOf(newQuantity));
            updateQuantityInFirestore(position, newQuantity);
            notifyItemChanged(position);
            itemRemoved.OnItemRemoved(itemsList);
        }

        public void decrement() {
            int position = getAdapterPosition();
            int currentQuantity = itemsList.get(position).getQuantity();
            if (currentQuantity > 1) {
                int newQuantity = currentQuantity - 1;
                itemsList.get(position).setQuantity(newQuantity);
                value.setText(String.valueOf(newQuantity));
                updateQuantityInFirestore(position, newQuantity);
                notifyItemChanged(position);
                itemRemoved.OnItemRemoved(itemsList);
            }
        }
        private void updateQuantityInFirestore(int position, int newQuantity) {
            firebaseFirestore.collection("Users")
                    .document(firebaseAuth.getCurrentUser().getUid())
                    .collection("Cart")
                    .document(itemsList.get(position).getDocId())
                    .update("quantity", newQuantity)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("CartItemAdapter", "Quantity updated successfully");
                            notifyItemChanged(position);
                        } else {
                            Log.e("CartItemAdapter", "Error updating quantity", task.getException());
                        }
                    });
        }
    }

    public interface ItemRemoved{
        public void OnItemRemoved(List<Items> itemsList);
    }
}


