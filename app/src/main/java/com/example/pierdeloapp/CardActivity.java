package com.example.pierdeloapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.pierdeloapp.adapter.CartItemAdapter;
import com.example.pierdeloapp.domain.Items;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;
import io.github.muddz.styleabletoast.StyleableToast;

public class CardActivity extends AppCompatActivity implements CartItemAdapter.ItemRemoved {

    FirebaseFirestore mStore;
    FirebaseAuth mAuth;
    List<Items> itemList;
    RecyclerView cartRecyclerView;
    CartItemAdapter cartItemAdapter;
    TextView totalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        ImageView backActivity = findViewById(R.id.toolbar_activityBack);
        TextView activityHeader = findViewById(R.id.toolbar_activityHeader);
        activityHeader.setText(getString(R.string.cart_title));
        backActivity.setOnClickListener(v -> finish());
        totalAmount = findViewById(R.id.total_amount);
        mStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        itemList = new ArrayList<>();
        cartRecyclerView = findViewById(R.id.cart_item_container);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartRecyclerView.setHasFixedSize(true);
        cartItemAdapter = new CartItemAdapter(this,itemList, this);
        cartRecyclerView.setAdapter(cartItemAdapter);
        mStore.collection("Users").document(mAuth.getCurrentUser().getUid())
                .collection("Cart").get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        if(task.getResult()!=null){
                            for(DocumentChange doc : task.getResult().getDocumentChanges()){
                                String documentId = doc.getDocument().getId();
                                Items item = doc.getDocument().toObject(Items.class);
                                item.setDocId(documentId);
                                itemList.add(item);
                            }
                            calculateAmount(itemList);
                            cartItemAdapter.notifyDataSetChanged();
                        }
                    }else  {
                        StyleableToast.makeText(CardActivity.this,task.getException().getMessage(), Toast.LENGTH_SHORT,R.style.errorToast).show();
                    }
                });
    }
    private void calculateAmount(List<Items> itemList) {
        double totalAmountInDouble = 0.0;
        for (Items item : itemList) {
            totalAmountInDouble += item.getPrice() * item.getQuantity();
        }
        totalAmount.setText(String.format("%.2f ₺", totalAmountInDouble));
    }
    @Override
    public void OnItemRemoved(List<Items> itemsList) {
        calculateAmount(itemsList);
        cartItemAdapter.notifyDataSetChanged();
    }

}