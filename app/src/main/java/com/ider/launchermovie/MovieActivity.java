package com.ider.launchermovie;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.ider.launchermovie.util.FileControl;
import com.ider.launchermovie.util.MovieAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MovieActivity extends AppCompatActivity  {

    private GridView grid;
    private List<File> fileList=new ArrayList<>();
    private MovieAdapter adapter;
    private TextView notice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        if (ContextCompat.checkSelfPermission(MovieActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MovieActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }else {
            showMovie();
        }
//        Log.i("mainactivity",files.length+"");



    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){
        switch (requestCode){
            case 1:
                if (grantResults.length>0&& grantResults[0] ==PackageManager.PERMISSION_GRANTED){
                    showMovie();
                }else {
                    Toast.makeText(this,"You denied the permission",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
    private void showMovie(){
        notice = (TextView) findViewById(R.id.hav_no_movie);
        fileList = new ArrayList<File>();
        File file = new File("/storage"); //从SD的根目录开始
        File[] files = file.listFiles();     //本方法返回该文件夹展开后的所有文件的数组
        if (files != null) findXFormat(files);
        if (fileList .size()==0){
            notice.setVisibility(View.VISIBLE);
        }else {
            notice.setVisibility(View.GONE);
        }
        adapter = new MovieAdapter(MovieActivity.this, R.layout.movie_list_item, fileList);
        grid = (GridView) findViewById(R.id.movie_grid);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File f = fileList.get(position);
                openFile(f);
            }
        });
    }
    private void openFile(File file){
        Intent intent = new Intent();
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        String type = FileControl.getMIMEType(file);
        intent.setDataAndType(Uri.fromFile(file), type);
        startActivity(intent);

    }

    private void findXFormat(File[] files) {

        for(File f:files){//遍历展开后的文件夹的文件
            if(f.isDirectory()){//如果是文件夹，继续展开
                File[] filess = f.listFiles();
                if (filess!=null)findXFormat(filess);//用递归递归
            }else if(FileControl.isVideo(f)){
                fileList.add(f);//符合格式的添加入列
            }
        }

    }

//    private boolean isXFormat(File temp) {
//        int start = temp.getName().lastIndexOf(".");
//        int end = temp.getName().length();
//        if (start != -1) {
//            String indexName = temp.getName().substring(start + 1, end);
//            if (indexName.equals("pdf")){
//                return true;
//            }
//        }
//        return false;
//    }

    private List<String> getData() {
        List<String> str = new ArrayList<String>();

        for(int i = 0;i < fileList.size();i++){
            str.add(fileList.get(i).getName());
        }
        return str;
    }
}
