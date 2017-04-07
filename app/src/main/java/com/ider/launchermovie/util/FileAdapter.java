package com.ider.launchermovie.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ider.launchermovie.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Eric on 2017/3/30.
 */

public class FileAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Drawable directory,file;
    private Drawable dir,fil;
    private Context mContext;
    //存储文件名称
    private ArrayList<String> names = null;
    //存储文件路径
    private ArrayList<String> paths = null;

    //参数初始化
    public FileAdapter(Context context, ArrayList<String> na, ArrayList<String> pa){
        this.mContext = context;
        names = na;
        paths = pa;
        Resources resources = context.getResources();
        directory = resources.getDrawable(R.drawable.icon_folder);
        //缩小图片
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return names.size();
    }
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return names.get(position);
    }
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        if (null == convertView){
            convertView = inflater.inflate(R.layout.file, null);
            holder = new ViewHolder();
            holder.text = (TextView)convertView.findViewById(R.id.textView);
            holder.image = (ImageView)convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }
        File f = new File(paths.get(position).toString());
        FileControl fileControl = new FileControl(MyApplication.getContext(),paths.get(position),null);
        String file_typ = fileControl.getMIMEType(f);
        if (names.get(position).equals("@1")){
            holder.text.setText("/");
            holder.image.setImageDrawable(directory);
        }
        else if (names.get(position).equals("@2")){
            holder.text.setText("..");
            holder.image.setImageDrawable(directory);
        }
        else{
            holder.text.setText(f.getName());
            if (f.isDirectory()){
                holder.image.setImageDrawable(directory);
            }
            else if (f.isFile()){
//                Drawable d = fileControl.getDrawable(file_typ);

                holder.image.setImageDrawable(FileControl.getDrawable(mContext,f));
            }
            else{
                System.out.println(f.getName());
            }
        }
        return convertView;
    }
    private class ViewHolder{
        private TextView text;
        private ImageView image;
    }
}
