package com.ider.launchermovie;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.ider.launchermovie.data.Music;
import com.ider.launchermovie.util.FullscreenActivity;
import com.ider.launchermovie.view.BaseRelativeLayout;

import java.io.File;

import static com.ider.launchermovie.R.drawable.sd;

public class MainActivity extends FullscreenActivity implements View.OnClickListener{

    private BaseRelativeLayout movie,music,picture,folder,setting;
    ImageView usb,sd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movie = (BaseRelativeLayout) findViewById(R.id.movie);
        music = (BaseRelativeLayout) findViewById(R.id.music);
        picture = (BaseRelativeLayout) findViewById(R.id.picture);
        folder = (BaseRelativeLayout) findViewById(R.id.folder);
        setting = (BaseRelativeLayout) findViewById(R.id.setting);

        usb = (ImageView)findViewById(R.id.usb) ;
        sd = (ImageView) findViewById(R.id.sd_card);

        movie.setOnClickListener(this);
        music.setOnClickListener(this);
        picture.setOnClickListener(this);
        folder.setOnClickListener(this);
        setting.setOnClickListener(this);

        registReceivers();
    }

    public void registReceivers() {
        IntentFilter filter;

        // 外接u盘广播
        filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_EJECT);
        filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        filter.addAction(Intent.ACTION_MEDIA_REMOVED);
        filter.addAction(Intent.ACTION_MEDIA_CHECKING);
        filter.addDataScheme("file");
        registerReceiver(mediaReciever, filter);

    }

    BroadcastReceiver mediaReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i("tag", action);
            String data = intent.getDataString();
            String path = data.substring(7);
            Log.i("tag", "onReceive: " + path);

            if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {

                if (path.contains("USB_DISK0") || path.contains("udisk0")) {
                    usb.setVisibility(View.VISIBLE);
                } else if (path.contains("USB_DISK1") || path.contains("udisk1")) {
                    usb.setVisibility(View.VISIBLE);
                } else if (path.contains("sdcard1")) {
                    sd.setVisibility(View.VISIBLE);
                }
            } else if (action.equals(Intent.ACTION_MEDIA_REMOVED) || action.equals(Intent.ACTION_MEDIA_UNMOUNTED)) {
                if (path.contains("USB_DISK0") || path.contains("udisk0")) {
                    usb.setVisibility(View.GONE);
                } else if (path.contains("USB_DISK1") || path.contains("udisk1")) {
                    usb.setVisibility(View.GONE);
                } else if (path.contains("sdcard1")) {
                    sd.setVisibility(View.GONE);
                }
            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mediaReciever);
    }

    @Override
    public void onClick(View view){
        Intent intent = new Intent();
        switch(view.getId()){
            case R.id.movie:
//                intent = getPackageManager().getLaunchIntentForPackage("android.rk.RockVideoPlayer");
                intent = new Intent(MainActivity.this,MovieActivity.class);
                startActivity(intent);
                break;
            case R.id.music:
//                intent = getPackageManager().getLaunchIntentForPackage("com.android.music");
                intent = new Intent(MainActivity.this, MusicActivity.class);
                startActivity(intent);
                break;
            case R.id.picture:
                intent = getPackageManager().getLaunchIntentForPackage("com.android.gallery3d");
                startActivity(intent);
                break;
            case R.id.folder:
                intent = new Intent(MainActivity.this,FileActivity.class);
                startActivity(intent);
                break;
            case R.id.setting:
                intent = new Intent(MainActivity.this,SettingActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
    @Override
    public void onBackPressed(){

    }
}
