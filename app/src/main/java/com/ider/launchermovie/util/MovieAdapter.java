package com.ider.launchermovie.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ider.launchermovie.R;

import java.io.File;
import java.util.List;

import static android.R.attr.data;
import static com.ider.launchermovie.util.MyApplication.getContext;

/**
 * Created by Eric on 2017/3/31.
 */

public class MovieAdapter extends BaseAdapter {

    private static final String TAG = "MovieAdapter";

    private Context mContext;
    private int layoutId ;
    private List<File> data;

    public MovieAdapter(Context mContext, List<File> data) {
        this.mContext = mContext;
        this.data = data;
    }

    public MovieAdapter(Context mContext, int layoutId, List<File> data) {
        this(mContext, data);
        this.layoutId = layoutId;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        File file = data.get(position);
        ViewHolder viewHolder;
        if(view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.movie_image);
            viewHolder.textView = (TextView) view.findViewById(R.id.movie_text);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.imageView.setImageBitmap(getVideoThumb(file.getPath()));
        viewHolder.textView.setText(file.getName());
        return view;
    }

    public Bitmap getVideoThumb(String path) {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(path);
        return media.getFrameAtTime();
    }
    class ViewHolder{
        ImageView imageView;
        TextView textView;
    }

}
