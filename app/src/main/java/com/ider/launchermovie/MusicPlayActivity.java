package com.ider.launchermovie;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MusicPlayActivity extends Activity implements View.OnClickListener{
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private String path;
    private SeekBar seekBar;
    private Timer timer;
    private boolean shortPress = false;
    private ScheduledExecutorService scheduledExecutor;
    private PressThread pressThread;
    private boolean isSetProgress=true;
    private boolean isLongTouch;
    private int newPosition=0,lastPosition;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        path = intent.getStringExtra("path");
        setContentView(R.layout.activity_music_play);
        Button play = (Button) findViewById(R.id.play);
        Button pause = (Button) findViewById(R.id.pause);
        Button stop = (Button) findViewById(R.id.stop);
        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        stop.setOnClickListener(this);
        initMediaPlayer();
    }

    private void initMediaPlayer(){
        try{
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            seekBar = (SeekBar)findViewById(R.id.playSeekBar);
            seekBar.setMax(mediaPlayer.getDuration());
//            timer = new Timer();
//            timer.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                        seekBar.setProgress(mediaPlayer.getCurrentPosition());
//                }
//            },0,50);



            //后台线程发送消息进行更新进度条
            new Thread() {
                public void run() {
                    while (isSetProgress) {
                        try {
                            sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mHandler.sendEmptyMessage(0);
                    }
                }
            }.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //处理进度条更新
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 0:
                    try {
                        //更新进度
                        if (isSetProgress) {
                            int position = mediaPlayer.getCurrentPosition();
//                    int time = mediaPlayer.getDuration();
//                    int max = seekBar.getMax();
                            seekBar.setProgress(position);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    try {
                        //更新进度
                        if (isLongTouch) {
                            if (lastPosition < mediaPlayer.getDuration()) {
                                newPosition++;
                            }
                            int position = mediaPlayer.getCurrentPosition();
                            int time = mediaPlayer.getDuration();
//                    int max = seekBar.getMax();
                            lastPosition = position + (newPosition * time / 100);
                            seekBar.setProgress(lastPosition);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }

        }
    };

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.play:
                if (!mediaPlayer.isPlaying()){
                    mediaPlayer.start();
                }
                break;
            case R.id.pause:
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                }
                break;
            case R.id.stop:
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.reset();
                    initMediaPlayer();
                }
                break;
            default:
                break;
        }
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            isSetProgress = false;

        }
    }
    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            shortPress = false;
            isSetProgress=false;
            isLongTouch=true;
            newPosition=0;
            new Thread() {
                public void run() {
                    while (isLongTouch) {
                        try {
                            sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mHandler.sendEmptyMessage(1);
                    }
                }
            }.start();
//            Toast.makeText(this, "longPress", Toast.LENGTH_LONG).show();
            return true;
        }
        //Just return false because the super call does always the same (returning false)
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            if(event.getAction() == KeyEvent.ACTION_DOWN){
                event.startTracking();
                if(event.getRepeatCount() == 0){
                    shortPress = true;
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            if(shortPress){
//                Toast.makeText(this, "shortPress", Toast.LENGTH_LONG).show();
            } else {
                isLongTouch=false;
                isSetProgress= true;
                mediaPlayer.seekTo(lastPosition);
                new Thread() {
                    public void run() {
                        while (isSetProgress) {
                            try {
                                sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            mHandler.sendEmptyMessage(0);
                        }
                    }
                }.start();
                //Don't handle longpress here, because the user will have to get his finger back up first
            }
            shortPress = false;
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }
    private void updateAddOrSubtract(int viewId) {
        final int vid = viewId;
        scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = vid;
                handler.sendMessage(msg);
            }
        }, 0, 100, TimeUnit.MILLISECONDS);    //每间隔100ms发送Message
    }

    private void stopAddOrSubtract() {
        if (scheduledExecutor != null) {
            scheduledExecutor.shutdownNow();
            scheduledExecutor = null;
        }
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int viewId = msg.what;
            switch (viewId){

            }
        }
    };

    class PressThread extends Thread{

        public void run(){

            while(isSetProgress){
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                mHandler.sendEmptyMessage(0);
            }
        }

    }
    public static Bitmap setArtwork(Context context, String url, ImageView ivPic) {
        Uri selectedAudio = Uri.parse(url);
        MediaMetadataRetriever myRetriever = new MediaMetadataRetriever();
        myRetriever.setDataSource(context, selectedAudio); // the URI of audio file
        byte[] artwork;
        artwork = myRetriever.getEmbeddedPicture();
        if (artwork != null) {
            Bitmap bMap = BitmapFactory.decodeByteArray(artwork, 0, artwork.length);
            ivPic.setImageBitmap(bMap);

            return bMap;
        }
        return null;
//        else {
//            ivPic.setImageResource(R.drawable.defult_music);
//            return BitmapFactory.decodeResource(context.getResources(), R.drawable.defult_music);
//        }
    }


//    public class MySeekBar implements SeekBar.OnSeekBarChangeListener {
//
//        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//            //更新进度
//            int position = mediaPlayer.getCurrentPosition();
//            int time = mediaPlayer.getDuration();
//            int max = seekBar.getMax();
//            seekBar.setProgress(position*max/time);
//        }
//
//        /*滚动时,应当暂停后台定时器*/
//        public void onStartTrackingTouch(SeekBar seekBar) {
//
//        }
//        /*滑动结束后，重新设置值*/
//        public void onStopTrackingTouch(SeekBar seekBar) {
//            mediaPlayer.seekTo(seekBar.getProgress());
//        }
//    }
}
