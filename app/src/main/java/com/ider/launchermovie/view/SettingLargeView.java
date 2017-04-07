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
 * Created by Eric on 2017/3/13.
 */

public class SettingLargeView extends RelativeLayout {
    private static final String TAG = "SettingLargeView";

    private ObjectAnimator animator = null;

    public Context mContext;
    private ImageView thumbnailGrid;
    private TextView title;


    public SettingLargeView(Context context) {
        this(context, null);
    }

    public SettingLargeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        LayoutInflater.from(context).inflate(R.layout.setting_large_view, this);
        thumbnailGrid = (ImageView) findViewById(R.id.setting_large_image);
        title = (TextView) findViewById(R.id.setting_large_text);

    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (((String)getTag()).equals("21")) {
            thumbnailGrid.setImageBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.set_display_ico));
            title.setText(getResources().getString(R.string.screen));
        }
        if (((String)getTag()).equals("22")) {
            thumbnailGrid.setImageBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.set_soun_ico));
            title.setText(getResources().getString(R.string.sound));
        }
        if (((String)getTag()).equals("23")) {
            thumbnailGrid.setImageBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.set_lang_ico));
            title.setText(getResources().getString(R.string.language));
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
