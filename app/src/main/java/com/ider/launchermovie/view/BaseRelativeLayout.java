package com.ider.launchermovie.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ider.launchermovie.R;
import com.ider.launchermovie.util.EntryAnimation;

/**
 * Created by Eric on 2017/3/16.
 */

public class BaseRelativeLayout extends RelativeLayout {
    private ObjectAnimator animator = null;
    private TextView title;

    public BaseRelativeLayout(Context context) {
        this(context, null);
    }
    public BaseRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.base_rela, this);
        title = (TextView) findViewById(R.id.title);

    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (((String)getTag()).equals("1")) {
            title.setText(getResources().getString(R.string.movie));
        }
        if (((String)getTag()).equals("2")) {
            title.setText(getResources().getString(R.string.music));
        }
        if (((String)getTag()).equals("3")) {
            title.setText(getResources().getString(R.string.picture));
        }
        if (((String)getTag()).equals("4")) {
            title.setText(getResources().getString(R.string.file));
        }
        if (((String)getTag()).equals("5")) {
            title.setText(getResources().getString(R.string.setting));
        }
    }
    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {

        if(gainFocus) {
            animator = EntryAnimation.createFocusAnimator(this);
        } else {

            if(animator != null && animator.isRunning()) {
                animator.cancel();
            }
            animator = EntryAnimation.createLoseFocusAnimator(this);

        }
        animator.start();
    }
}
