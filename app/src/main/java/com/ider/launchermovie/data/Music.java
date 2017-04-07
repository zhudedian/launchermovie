package com.ider.launchermovie.data;

/**
 * Created by Eric on 2017/3/31.
 */

public class Music {
    private String musicName;
    private String musicAlbum;
    private String musicArtist;
    private String musicPath;
    private int musicDuration;
    private int musicSize;
    public Music(String musicName, String musicAlbum,String musicArtist,String musicPath,int musicDuration, int musicSize){
        this.musicName = musicName;
        this.musicAlbum = musicAlbum;
        this.musicArtist = musicArtist;
        this.musicPath = musicPath;
        this.musicDuration = musicDuration;
        this.musicSize = musicSize;
    }
    public String getMusicName(){
        return musicName;
    }
    public void setMusicName(String musicName){
        this.musicName= musicName;
    }
    public String getMusicAlbum(){
        return musicAlbum;
    }
    public void setMusicAlbum(String musicAlbum){
        this.musicAlbum= musicAlbum;
    }
    public String getMusicArtist(){
        return musicArtist;
    }
    public void setMusicArtist(String musicArtist){
        this.musicArtist= musicArtist;
    }
    public String getMusicPath(){
        return musicPath;
    }
    public void setMusicPath(String musicPath){
        this.musicPath= musicPath;
    }
    public int getMusicDuration(){
        return musicDuration;
    }
    public void setMusicDuration(int musicDuration){
        this.musicDuration= musicDuration;
    }
    public int getMusicSize(){
        return musicSize;
    }
    public void setMusicSize(int musicSize){
        this.musicSize= musicSize;
    }
}
