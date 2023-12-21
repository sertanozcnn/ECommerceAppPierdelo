package com.example.pierdeloapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.pierdeloapp.adapter.ViewPagerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class NavigationActivity extends AppCompatActivity {
    ViewPager slideViewPager;
    LinearLayout dotIndicator;
    ViewPagerAdapter viewPagerAdapter;
    Button skipButton ,  nextButton;
    TextView[] dots = new TextView[3];

    ViewPager.OnPageChangeListener viewPagerListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {

            setDotIndicator(position);

            if (position == 2) {
                nextButton.setText(getString(R.string.start_shopping_title));
            } else {
                nextButton.setText(getString(R.string.next_title));
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        FirebaseAuth.getInstance().addAuthStateListener(firebaseAuth -> checkAuthenticationAndNavigate());

        nextButton = findViewById(R.id.skipNext);
        skipButton = findViewById(R.id.skipButton);
        nextButton.setOnClickListener(v -> {
            if(getItem(0)<2){
                slideViewPager.setCurrentItem(getItem(1),true);
            } else{
                checkAuthenticationAndNavigate();

            }
        });
        skipButton.setOnClickListener(v -> checkAuthenticationAndNavigate());
        slideViewPager = (ViewPager) findViewById(R.id.slideViewPager);
        dotIndicator = (LinearLayout) findViewById(R.id.dotIndicator);
        viewPagerAdapter = new ViewPagerAdapter(this);
        slideViewPager.setAdapter(viewPagerAdapter);
        setDotIndicator(0);
        slideViewPager.addOnPageChangeListener(viewPagerListener);

    }
    private void checkAuthenticationAndNavigate() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null ) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        } else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    public void setDotIndicator(int position) {
        dotIndicator.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            ImageView dot = new ImageView(this);
            dot.setImageResource(R.drawable.dot_two);
            if (i == position) {
                dot.setImageResource(R.drawable.dot_one);
                dot.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_up));
            } else {
                dot.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_down));
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(8, 0, 8, 0);
            dot.setLayoutParams(params);
            dotIndicator.addView(dot);
        }

    }
    private int getItem(int i){
        return slideViewPager.getCurrentItem() + i;
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAuthenticationAndNavigate();
    }
}