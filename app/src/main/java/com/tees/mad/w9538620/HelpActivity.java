package com.tees.mad.w9538620;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.Objects;

public class HelpActivity extends AppCompatActivity {

    SliderPagerAdapter sliderPagerAdapter;
    ArrayList<String> slider_image_list;
    int page_position = 0;
    private ViewPager vp_slider;
    private LinearLayout ll_dots;
    private TextView[] dots;
    private boolean verifyOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_help);

        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setTitle("Help");

        verifyOwner = getIntent().getBooleanExtra("verifyOwner", false);

        init();
        addBottomDots(0);
    }

    private void init() {
        vp_slider = findViewById(R.id.vp_slider);
        ll_dots = findViewById(R.id.ll_dots);
        slider_image_list = new ArrayList<>();

        if(verifyOwner) {
            slider_image_list.add("one");
            slider_image_list.add("two");
        }else {
            slider_image_list.add("one");
            slider_image_list.add("two");
            slider_image_list.add("three");
        }
        sliderPagerAdapter = new SliderPagerAdapter(HelpActivity.this, slider_image_list, verifyOwner);
        vp_slider.setAdapter(sliderPagerAdapter);

        vp_slider.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_help, menu);

        // return true so that the menu pop up is opened
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[slider_image_list.size()];

        ll_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(Color.parseColor("#BAC2D2"));
            ll_dots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(Color.parseColor("#1B4298"));
    }

}