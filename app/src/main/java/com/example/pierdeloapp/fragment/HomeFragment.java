package com.example.pierdeloapp.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pierdeloapp.ItemsActivity;
import com.example.pierdeloapp.R;
import com.example.pierdeloapp.adapter.BestSellAdapter;
import com.example.pierdeloapp.adapter.CategoryAdapter;
import com.example.pierdeloapp.adapter.FeatureAdapter;
import com.example.pierdeloapp.domain.BestSell;
import com.example.pierdeloapp.domain.Category;
import com.example.pierdeloapp.domain.Feature;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    FirebaseFirestore mStore;
    private List<Category> mCategoryList;
    private CategoryAdapter mCategoryAdapter;
    RecyclerView mCatRecyclerView;

    private List<Feature> mFeatureList;
    private FeatureAdapter mFeatureAdapter;
    RecyclerView mFeatureRecyclerView;

    private List<BestSell> mBestSellList;
    private BestSellAdapter mBestSellAdapter;
    RecyclerView mBestSellRecyclerView;

    private TextView mSeeAll;
    private TextView mFeature;
    private TextView mBestSell;

    public HomeFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_home, container, false);

        mStore = FirebaseFirestore.getInstance();
        mSeeAll=view.findViewById(R.id.see_all);
        mFeature=view.findViewById(R.id.fea_see_all);
        mBestSell=view.findViewById(R.id.best_sell);

        mCatRecyclerView = view.findViewById(R.id.category_recycler);
        mFeatureRecyclerView = view.findViewById(R.id.feature_recycler); // Move this line up
        mBestSellRecyclerView = view.findViewById(R.id.bestsell_recycler);
        mCategoryList = new ArrayList<>();

        mCategoryAdapter = new CategoryAdapter(getContext(), mCategoryList);
        mCatRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        mCatRecyclerView.setAdapter(mCategoryAdapter);

        mFeatureList = new ArrayList<>();
        mFeatureAdapter = new FeatureAdapter(getContext(), mFeatureList);
        mFeatureRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        mFeatureRecyclerView.setAdapter(mFeatureAdapter);

        mBestSellList = new ArrayList<>();
        mBestSellAdapter = new BestSellAdapter(getContext(), mBestSellList);
        mBestSellRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2,RecyclerView.HORIZONTAL, false));
        mBestSellRecyclerView.setAdapter(mBestSellAdapter);



        mStore.collection("Category")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Category category = document.toObject(Category.class);
                            mCategoryList.add(category);
                            mCategoryAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Log.w("TAG", "Error getting documents.", task.getException());
                    }
                });

        mStore.collection("Feature")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Feature feature = document.toObject(Feature.class);
                            mFeatureList.add(feature);
                            mFeatureAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Log.w("TAG", "Error getting documents.", task.getException());
                    }
                });

        mStore.collection("BestSell")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            BestSell bestSell = document.toObject(BestSell.class);
                            mBestSellList.add(bestSell);
                            mBestSellAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Log.w("TAG", "Error getting documents.", task.getException());
                    }
                });
        mSeeAll.setOnClickListener(v -> {
            Intent intent=new Intent(getContext(), ItemsActivity.class);
            startActivity(intent);
        });
        mBestSell.setOnClickListener(v -> {
            Intent intent=new Intent(getContext(), ItemsActivity.class);
            startActivity(intent);
        });
        mFeature.setOnClickListener(v -> {
            Intent intent=new Intent(getContext(), ItemsActivity.class);
            startActivity(intent);
        });


        return view;
    }
}























