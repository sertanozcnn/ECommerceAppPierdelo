package com.example.pierdeloapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pierdeloapp.adapter.ItemsRecyclerAdapter;
import com.example.pierdeloapp.domain.Items;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ItemsActivity extends AppCompatActivity {
    private FirebaseFirestore mStore;
    private ItemsRecyclerAdapter itemsRecyclerAdapter;
    List<Items> mItemsList;
    RecyclerView itemRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        ImageView backActivity = findViewById(R.id.toolbar_activityBack);
        TextView activityHeader = findViewById(R.id.toolbar_activityHeader);
        activityHeader.setText(getString(R.string.items_title));
        backActivity.setOnClickListener(v -> finish());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_discover);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_profile) {
                startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
                overridePendingTransition(0,0);
                return true;
            } else if (item.getItemId() == R.id.bottom_home) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                overridePendingTransition(0,0);
                return true;
            }else if(item.getItemId() == R.id.bottom_discover){
                return true;
            }
            return false;
        });


        String type=getIntent().getStringExtra("type");
        mStore=FirebaseFirestore.getInstance();
        mItemsList=new ArrayList<>();
        itemRecyclerView=findViewById(R.id.items_recycler);
        itemRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        itemsRecyclerAdapter=new ItemsRecyclerAdapter(this,mItemsList);
        itemRecyclerView.setAdapter(itemsRecyclerAdapter);

        if(type==null || type.isEmpty()){
            mStore.collection("All").get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    for(DocumentSnapshot doc:task.getResult().getDocuments()){
                        Log.i("TAG", "onComplete: "+doc.toString());
                        Items items=doc.toObject(Items.class);
                        mItemsList.add(items);
                        itemsRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
        if(type!=null && type.equalsIgnoreCase("vest")){
            mStore.collection("All").whereEqualTo("type","vest").get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    for(DocumentSnapshot doc:task.getResult().getDocuments()){
                        Log.i("TAG", "onComplete: "+doc.toString());
                        Items items=doc.toObject(Items.class);
                        mItemsList.add(items);
                        itemsRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
        if(type!=null && type.equalsIgnoreCase("tshirt")){
            mStore.collection("All").whereEqualTo("type","tshirt").get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    for(DocumentSnapshot doc:task.getResult().getDocuments()){
                        Log.i("TAG", "onComplete: "+doc.toString());
                        Items items=doc.toObject(Items.class);
                        mItemsList.add(items);
                        itemsRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
        if(type!=null && type.equalsIgnoreCase("sweater")){
            mStore.collection("All").whereEqualTo("type","sweater").get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    for(DocumentSnapshot doc:task.getResult().getDocuments()){
                        Log.i("TAG", "onComplete: "+doc.toString());
                        Items items=doc.toObject(Items.class);
                        mItemsList.add(items);
                        itemsRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            });
        }

        if(type!=null && type.equalsIgnoreCase("tracksuit")){
            mStore.collection("All").whereEqualTo("type","tracksuit").get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    for(DocumentSnapshot doc:task.getResult().getDocuments()){
                        Log.i("TAG", "onComplete: "+doc.toString());
                        Items items=doc.toObject(Items.class);
                        mItemsList.add(items);
                        itemsRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            });
        }

        if(type!=null && type.equalsIgnoreCase("cardigan")){
            mStore.collection("All").whereEqualTo("type","cardigan").get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    for(DocumentSnapshot doc:task.getResult().getDocuments()){
                        Log.i("TAG", "onComplete: "+doc.toString());
                        Items items=doc.toObject(Items.class);
                        mItemsList.add(items);
                        itemsRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            });
        }

        if(type!=null && type.equalsIgnoreCase("jacket")){
            mStore.collection("All").whereEqualTo("type","jacket").get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    for(DocumentSnapshot doc:task.getResult().getDocuments()){
                        Log.i("TAG", "onComplete: "+doc.toString());
                        Items items=doc.toObject(Items.class);
                        mItemsList.add(items);
                        itemsRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            });
        }

        if(type!=null && type.equalsIgnoreCase("coat")){
            mStore.collection("All").whereEqualTo("type","coat").get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    for(DocumentSnapshot doc:task.getResult().getDocuments()){
                        Log.i("TAG", "onComplete: "+doc.toString());
                        Items items=doc.toObject(Items.class);
                        mItemsList.add(items);
                        itemsRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu,menu);
        MenuItem item= menu.findItem(R.id.search_it);
        SearchView searchView= (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchItem(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    private void searchItem(String query) {
        mItemsList.clear();
        mStore.collection("All").whereGreaterThanOrEqualTo("name",query).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                for(DocumentSnapshot doc:task.getResult().getDocuments()){
                    Log.i("TAG", "onComplete: "+doc.toString());
                    Items items=doc.toObject(Items.class);
                    mItemsList.add(items);
                    itemsRecyclerAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}