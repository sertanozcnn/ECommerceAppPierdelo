package com.example.pierdeloapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.pierdeloapp.domain.BestSell;
import com.example.pierdeloapp.domain.Feature;
import com.example.pierdeloapp.domain.Items;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import io.github.muddz.styleabletoast.StyleableToast;

public class DetailActivity extends AppCompatActivity {

    ImageView mImage;
    TextView mItemName;
    TextView mPrice;
    RatingBar mItemRating;
    TextView mItemRatDesc;
    TextView mItemDesc;
    Button mAddToCart;
    Button mBuyBtn;
    Feature feature = null;
    BestSell bestSell = null;
    Items items = null;
    FirebaseFirestore mStore;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView backActivity = findViewById(R.id.toolbar_activityBack);
        TextView activityHeader = findViewById(R.id.toolbar_activityHeader);
        activityHeader.setText(getString(R.string.detail_product_title));
        backActivity.setOnClickListener(v -> finish());
        mImage=findViewById(R.id.item_img);
        mItemName=findViewById(R.id.item_name);
        mPrice=findViewById(R.id.item_price);
        mItemRating=findViewById(R.id.item_rating);
        mItemRatDesc=findViewById(R.id.item_rat_des);
        mItemDesc=findViewById(R.id.item_des);
        mAddToCart=findViewById(R.id.item_add_cart);
        mBuyBtn=findViewById(R.id.item_buy_now);
        mStore =FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        final Object obj=  getIntent().getSerializableExtra("detail");
        if(obj instanceof Feature){
            feature= (Feature) obj;
        }else if(obj instanceof BestSell){
            bestSell= (BestSell) obj;
        }
        else if(obj instanceof Items){
            items= (Items) obj;
        }
        if(feature!=null){
            Glide.with(getApplicationContext()).load(feature.getImg_url()).into(mImage);
            mItemName.setText(feature.getName());
            mPrice.setText(feature.getPrice()+"₺");
            mItemRating.setRating(feature.getRating());
            mItemRatDesc.setText(String.valueOf(feature.getRating()));
            mItemDesc.setText(feature.getDescription());
        }
        if(bestSell!=null){
            Glide.with(getApplicationContext()).load(bestSell.getImg_url()).into(mImage);
            mItemName.setText(bestSell.getName());
            mPrice.setText(bestSell.getPrice()+"₺");
            mItemRating.setRating(bestSell.getRating());
            mItemRatDesc.setText(String.valueOf(bestSell.getRating()));
            mItemDesc.setText(bestSell.getDescription());
        }
        if(items!=null){
            Glide.with(getApplicationContext()).load(items.getImg_url()).into(mImage);
            mItemName.setText(items.getName());
            mPrice.setText(items.getPrice()+"₺");
            mItemRating.setRating(items.getRating());
            mItemRatDesc.setText(String.valueOf(items.getRating()));
            mItemDesc.setText(items.getDescription());
        }
        mBuyBtn.setOnClickListener(v -> {
            Intent intent=new Intent(DetailActivity.this,AddressActivity.class);
            if(feature!=null){
                intent.putExtra("item", feature);
            }
            if(bestSell!=null){
                intent.putExtra("item", bestSell);
            }
            if(items!=null){
                intent.putExtra("item", items);
            }
            startActivity(intent);
        });

        mAddToCart.setOnClickListener(v -> {
            if(feature!=null){
                mStore.collection("Users").document(mAuth.getCurrentUser().getUid())
                        .collection("Cart").add(feature).addOnCompleteListener(task -> {
                            StyleableToast.makeText(DetailActivity.this,getString(R.string.detail_activity_add_cart), Toast.LENGTH_SHORT,R.style.successToast).show();
                            Intent intent = new Intent(DetailActivity.this,CardActivity.class);
                            startActivity(intent);
                        });
            }
            if(bestSell!=null){
                mStore.collection("Users").document(mAuth.getCurrentUser().getUid())
                        .collection("Cart").add(bestSell).addOnCompleteListener(task -> {
                            StyleableToast.makeText(DetailActivity.this,getString(R.string.detail_activity_add_cart), Toast.LENGTH_SHORT,R.style.successToast).show();
                            Intent intent = new Intent(DetailActivity.this,CardActivity.class);
                            startActivity(intent);
                        });
            }
            if(items!=null){
                mStore.collection("Users").document(mAuth.getCurrentUser().getUid())
                        .collection("Cart").add(items).addOnCompleteListener(task -> {
                            StyleableToast.makeText(DetailActivity.this,getString(R.string.detail_activity_add_cart), Toast.LENGTH_SHORT,R.style.successToast).show();
                            Intent intent = new Intent(DetailActivity.this,CardActivity.class);
                            startActivity(intent);
                        });
            }
        });
    }
}
