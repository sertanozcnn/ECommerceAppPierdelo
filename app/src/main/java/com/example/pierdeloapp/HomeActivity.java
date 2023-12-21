package com.example.pierdeloapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import com.example.pierdeloapp.adapter.ItemsRecyclerAdapter;
import com.example.pierdeloapp.domain.Items;
import com.example.pierdeloapp.fragment.HomeFragment;
import com.example.pierdeloapp.utility.NetworkChangeListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    private FirebaseFirestore mStore;
    private List<Items> mItemsList;
    private RecyclerView mItemRecyclerView;
    private AppBarConfiguration mAppBarConfiguration;
    private ItemsRecyclerAdapter itemsRecyclerAdapter;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    Fragment homeFragment;
    FirebaseAuth mAuth;
    EditText mSearchtext;
    ImageView mCardButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_profile) {
                startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
                overridePendingTransition(0,0);
                return true;
            } else if (item.getItemId() == R.id.bottom_home) {
                return true;
            }else if(item.getItemId() == R.id.bottom_discover){
                startActivity(new Intent(getApplicationContext(), ItemsActivity.class));
                overridePendingTransition(0,0);
                return true;
            }
            return false;
        });

        homeFragment=new HomeFragment();
        loadFragment(homeFragment);
        mAuth=FirebaseAuth.getInstance();
        mSearchtext=findViewById(R.id.search_text);
        mCardButton = findViewById(R.id.goToCard);
        mStore=FirebaseFirestore.getInstance();
        mItemsList=new ArrayList<>();
        mItemRecyclerView=findViewById(R.id.search_recycler);
        mItemRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        itemsRecyclerAdapter=new ItemsRecyclerAdapter(this,mItemsList);
        mItemRecyclerView.setAdapter(itemsRecyclerAdapter);
        mSearchtext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchItem(s.toString());

            }
        });
        mCardButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this,CardActivity.class);
            startActivity(intent);
        });


    }


    private void searchItem(String text) {
        if(!text.isEmpty()){
            mStore.collection("All").whereGreaterThanOrEqualTo("name",text).get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful() && task.getResult()!=null){
                            mItemsList.clear();
                            for(DocumentSnapshot doc:task.getResult().getDocuments()){
                                Items items=doc.toObject(Items.class);
                                if(!mItemsList.contains(items)){
                                    mItemsList.add(items);
                                }
                            }
                            itemsRecyclerAdapter=new ItemsRecyclerAdapter(getApplicationContext(),mItemsList);
                            mItemRecyclerView.setAdapter(itemsRecyclerAdapter);
                            itemsRecyclerAdapter.notifyDataSetChanged();
                        }
                    });
        }else{
            mItemsList.clear();
            itemsRecyclerAdapter=new ItemsRecyclerAdapter(getApplicationContext(),new ArrayList<>());
            mItemRecyclerView.setAdapter(itemsRecyclerAdapter);
            itemsRecyclerAdapter.notifyDataSetChanged();
        }
    }
    private void loadFragment(Fragment homeFragment) {
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.home_container,homeFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}