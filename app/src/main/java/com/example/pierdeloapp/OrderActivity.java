package com.example.pierdeloapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pierdeloapp.adapter.OrderDeliveryAdapter;
import com.example.pierdeloapp.fragment.DeliveryFragment;
import com.example.pierdeloapp.fragment.OrderFragment;
import com.google.android.material.tabs.TabLayout;

public class OrderActivity extends AppCompatActivity {

     TabLayout tabLayout;
     ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        ImageView backActivity = findViewById(R.id.toolbar_activityBack);
        TextView activityHeader = findViewById(R.id.toolbar_activityHeader);
        activityHeader.setText(getString(R.string.order_title));
        backActivity.setOnClickListener(v -> finish());
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        tabLayout.setupWithViewPager(viewPager);
        OrderDeliveryAdapter adapter = new OrderDeliveryAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(new OrderFragment(),getString(R.string.order_title));
        adapter.addFragment(new DeliveryFragment(),getString(R.string.order_title_two));
        viewPager.setAdapter(adapter);

    }
}