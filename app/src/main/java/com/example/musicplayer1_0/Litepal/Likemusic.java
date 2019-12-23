package com.example.musicplayer1_0.Litepal;

import org.litepal.crud.DataSupport;

/**
 * Created by 王子琛 on 2019/12/15.
 */

public class Likemusic extends DataSupport {
    private String name;//歌曲名
    private String singer;//歌手
    private String album;//专辑
    private int duration;//时长
    private long imageId;//图片
    private long size;//歌曲大小
    private String path;//歌曲路径

    public Likemusic(){

    }

    public Likemusic(String name, String singer, String album, int duration, long imageId, long size, String path){
        this.name = name;
        this.singer = singer;
        this.album = album;
        this.duration = duration;
        this.imageId = imageId;
        this.size = size;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getImageId() {
        return imageId;
    }

    public void setImageId(long imageId) {
        this.imageId = imageId;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
