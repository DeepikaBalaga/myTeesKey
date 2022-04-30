package com.tees.mad.w9538620;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SliderPagerAdapter extends PagerAdapter {
    private final boolean verifyOwner;
    Activity activity;
    ArrayList<String> image_arraylist;
    private LayoutInflater layoutInflater;

    public SliderPagerAdapter(Activity activity, ArrayList<String> image_arraylist, boolean verifyOwner) {
        this.activity = activity;
        this.image_arraylist = image_arraylist;
        this.verifyOwner = verifyOwner;
    }

    @Override
    public @NotNull Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.layout_slider, container, false);
        ImageView im_slider = view.findViewById(R.id.im_slider);

        switch (position) {
            case 0:
                if (verifyOwner) {
                    im_slider.setImageResource(R.drawable.help_one);
                } else {
                    im_slider.setImageResource(R.drawable.help_three);
                }
                break;
            case 1:
                if (verifyOwner) {
                    im_slider.setImageResource(R.drawable.help_two);
                } else {
                    im_slider.setImageResource(R.drawable.help_four);
                }
                break;
            case 2:
                im_slider.setImageResource(R.drawable.help_five);
                break;
            default:
                im_slider.setImageResource(R.drawable.help_one);
                break;
        }

        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return image_arraylist.size();
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
