package com.ider.launchermovie;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ider.launchermovie.data.Music;
import com.ider.launchermovie.util.FileControl;
import com.ider.launchermovie.util.MusicAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MusicActivity extends AppCompatActivity {

    private List<Music> musicList = new ArrayList<>();;
    private TextView notice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        if (ContextCompat.checkSelfPermission(MusicActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MusicActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }else {
            initList();
        }



    }
    private void initList(){
        notice = (TextView) findViewById(R.id.hav_no_music);
        ContentResolver resolver = getContentResolver();
        Cursor c = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null,null, null,null);
        if (c!=null) {
            c.moveToFirst();
            if (c.moveToNext()){
                notice.setVisibility(View.GONE);
            }else {
                notice.setVisibility(View.VISIBLE);
            }
            c.moveToFirst();
            while (c.moveToNext()) {
                //名字
                String name = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                //专辑名
                String album = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
                //歌手名
                String artist = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                //URI 歌曲文件存放路径
                String path = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                //歌曲文件播放时间长度
                int duration = c.getInt(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                //音乐文件大小
                int size = c.getInt(c.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                musicList.add(new Music(name, album, artist, path, duration, size));
            }
        }
        c.close();
        MusicAdapter adapter = new MusicAdapter(MusicActivity.this,R.layout.music_list_item,musicList);
        ListView listView = (ListView) findViewById(R.id.music_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position,long id){
                Music music = musicList.get(position);
                String path = music.getMusicPath();
                File file = new File(path);
                Intent intent = new Intent(MusicActivity.this,MusicPlayActivity.class);
                intent.putExtra("path",path);
                startActivity(intent);

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

    public  String formatTime(int time) {
        if (time / 1000 % 60 < 10) {
            return time / 1000 / 60 + ":0" + time / 1000 % 60;

        } else {
            return time / 1000 / 60 + ":" + time / 1000 % 60;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){
        switch (requestCode){
            case 1:
                if (grantResults.length>0&& grantResults[0] ==PackageManager.PERMISSION_GRANTED){
                    initList();
                }else {
                    Toast.makeText(this,"You denied the permission",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }


}
