package com.example.audibook.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.audibook.Adapter.OnBoardingAdapter;
import com.example.audibook.Models.OnBoardingModel;
import com.example.audibook.R;

import java.util.ArrayList;
import java.util.List;

public class OnBoardingActivity extends AppCompatActivity {

    LinearLayout onBoardingIndicators;
    Button nextBtn, skipBtn;
    ViewPager2 onBoardingViewPager;
    OnBoardingAdapter onBoardingAdapter;
    String prevStarted = "yes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_on_boarding);

        onBoardingIndicators = findViewById(R.id.onBoardingIndicators);
        nextBtn = findViewById(R.id.nextBtn);
        skipBtn = findViewById(R.id.skipBtn);
        onBoardingViewPager = findViewById(R.id.onBoardingViewPager);

        setOnboardingAdapter();
        onBoardingViewPager.setAdapter(onBoardingAdapter);
        setLayoutOnBoardingIndicators();
        setCurrentOnboardingIndicator(0);

        onBoardingViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentOnboardingIndicator(position);
            }
        });

        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBoardingViewPager.setCurrentItem(onBoardingAdapter.getItemCount());
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onBoardingViewPager.getCurrentItem() + 1 < onBoardingAdapter.getItemCount()){
                    onBoardingViewPager.setCurrentItem(onBoardingViewPager.getCurrentItem() + 1);
                } else {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//                    finish();
                }
            }
        });

    }

    private void setOnboardingAdapter(){
        List<OnBoardingModel> onboardingItems = new ArrayList<>();

        OnBoardingModel itemPayOnline = new OnBoardingModel();
        itemPayOnline.setTitle("Life is short and the world is wide");
        itemPayOnline.setDescription("At Friends tours and travel, we customize reliable and trustworthy educational tours to destinations all over the world.");
        itemPayOnline.setImage(R.drawable.people);
        itemPayOnline.setDarkImage(R.drawable.people_dark);

        OnBoardingModel itemPayOnline2 = new OnBoardingModel();
        itemPayOnline2.setTitle("It's a big world out there go explore");
        itemPayOnline2.setDescription("To get the best of your adventure you just need to leave and go where to like. We are waiting for you.");
        itemPayOnline2.setImage(R.drawable.knowledge);
        itemPayOnline2.setDarkImage(R.drawable.knowledge_dark);

        OnBoardingModel itemPayOnline3 = new OnBoardingModel();
        itemPayOnline3.setTitle("People don't take trips, trips take people");
        itemPayOnline3.setDescription("To get the best of your adventure you just need to leave and go where to like. We are waiting for you.");
        itemPayOnline3.setImage(R.drawable.comunity);
        itemPayOnline3.setDarkImage(R.drawable.comunity_dark);

        onboardingItems.add(itemPayOnline);
        onboardingItems.add(itemPayOnline2);
        onboardingItems.add(itemPayOnline3);

        onBoardingAdapter = new OnBoardingAdapter(onboardingItems);
    }

    private void setLayoutOnBoardingIndicators(){
        ImageView[] indicators = new ImageView[onBoardingAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(10,0,10,0);
        for(int i = 0; i<indicators.length; i++){
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.onboarding_indicator_inactive));
            indicators[i].setLayoutParams(layoutParams);
            onBoardingIndicators.addView(indicators[i]);
        }
    }

    private void setCurrentOnboardingIndicator(int index){
        int childCount = onBoardingIndicators.getChildCount();
        for(int i = 0; i < childCount; i++){
            ImageView imageView = (ImageView) onBoardingIndicators.getChildAt(i);
            if(i == index){
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.onboarding_indicator_active));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.onboarding_indicator_inactive));
            }
        }
        if(index == childCount-1){
            nextBtn.setText("Get Started");
            skipBtn.setVisibility(View.GONE);
        } else {
            nextBtn.setText("Next");
            skipBtn.setVisibility(View.VISIBLE);
        }
    }
}