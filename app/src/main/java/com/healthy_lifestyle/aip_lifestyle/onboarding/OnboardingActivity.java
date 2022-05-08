package com.healthy_lifestyle.aip_lifestyle.onboarding;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthy_lifestyle.aip_lifestyle.R;
import com.healthy_lifestyle.aip_lifestyle.login.LoginActivity;

public class OnboardingActivity extends AppCompatActivity {
    private ViewPager vpOnboarding;
    private OnboardingAdapter onboardingAdapter;
    private LinearLayout llDots;
    private TextView[] tvDots;
    private int[] llOnboarding;
    private Button btnSkip, btnNext;
    private SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        sharedPrefManager = new SharedPrefManager(this);
//        if (!sharedPrefManager.isFirstTimeLaunch()) {
//            launchHomeScreen();
//            finish();
//        }
        changeVisibility();
        findById();
        addBottomDots(0);
        changeStatusBarColor();

        onboardingAdapter = new OnboardingAdapter();
        vpOnboarding.setAdapter(onboardingAdapter);
        vpOnboarding.addOnPageChangeListener(viewPagerPageChangeListener);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLayout();
            }
        });
    }

    private void changeLayout() {
        int current = getItem(+1);
        if (current < llOnboarding.length) {
            vpOnboarding.setCurrentItem(current);
        } else {
            launchHomeScreen();
        }
    }

    private void addBottomDots(int currentPage) {
        tvDots = new TextView[llOnboarding.length];

        int colorsActive = getResources().getColor(R.color.active_dot);
        int colorsInactive = getResources().getColor(R.color.inactive_dot);

        llDots.removeAllViews();
        for (int i = 0; i < tvDots.length; i++) {
            tvDots[i] = new TextView(this);
            tvDots[i].setText(Html.fromHtml("&#8226;"));
            tvDots[i].setTextSize(35);
            tvDots[i].setTextColor(colorsInactive);
            llDots.addView(tvDots[i]);
        }

        if (tvDots.length > 0)
            tvDots[currentPage].setTextColor(colorsActive);
    }

    private int getItem(int i) {
        return vpOnboarding.getCurrentItem() + i;
    }

    private void launchHomeScreen() {
        sharedPrefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(OnboardingActivity.this, LoginActivity.class));
        finish();
    }

    private void changeVisibility() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    private void findById () {
        vpOnboarding = (ViewPager) findViewById(R.id.vp_onboarding);
        llDots = (LinearLayout) findViewById(R.id.ll_onboarding_dots);
        btnSkip = (Button) findViewById(R.id.btn_skip);
        btnNext = (Button) findViewById(R.id.btn_next);
        llOnboarding = new int[]{
                R.layout.onboarding_slide_one,
                R.layout.onboarding_slide_two};
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
            if (position == llOnboarding.length - 1) {
                btnNext.setText(getString(R.string.got_it));
                btnSkip.setVisibility(View.GONE);
            } else {
                btnNext.setText(getString(R.string.next));
                btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public class OnboardingAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public OnboardingAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(llOnboarding[position], container, false);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return llOnboarding.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}

