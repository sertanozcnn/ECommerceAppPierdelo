package com.example.pierdeloapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.example.pierdeloapp.R;

public class ViewPagerAdapter extends PagerAdapter {

    Context context;
    int sliderAllImages[] = {R.drawable.slider_image_one , R.drawable.slider_image_two , R.drawable.slider_image_three};
    int sliderAllTitle[] = {R.string.screen1Title,R.string.screenNone,R.string.screenNone};
    int sliderAllDesc[] = {R.string.screen1Desc , R.string.screenNone , R.string.screenNone};
    public ViewPagerAdapter(Context context){
        this.context = context;
    }
    @Override
    public int getCount() {
        return sliderAllTitle.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout) object;
    }
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_screen, container, false);
        ImageView sliderImage = view.findViewById(R.id.sliderImage);
        TextView sliderTitle = view.findViewById(R.id.sliderTitle);
        TextView sliderDesc = view.findViewById(R.id.sliderDesc);
        Log.d("ViewPagerAdapter", "Position: " + position + ", Resource ID: " + sliderAllImages[position]);
        if (sliderAllImages[position] != 0) {
            sliderImage.setImageResource(sliderAllImages[position]);
        }
        sliderTitle.setText(sliderAllTitle[position]);
        sliderDesc.setText(sliderAllDesc[position]);
        container.addView(view);
        return view;
    }
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }
}
