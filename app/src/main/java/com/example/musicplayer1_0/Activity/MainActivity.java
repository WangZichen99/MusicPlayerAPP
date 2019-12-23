package com.example.musicplayer1_0.Activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.support.design.widget.NavigationView;

import com.example.musicplayer1_0.Adapter.MusicAdapter;
import com.example.musicplayer1_0.Adapter.Music;
import com.example.musicplayer1_0.Litepal.Likemusic;
import com.example.musicplayer1_0.R;


public class MainActivity extends AppCompatActivity {

    private String name;//歌曲名
    private String singer;//歌手
    private String album;//专辑
    private int duration;//时长
    private long imageId;//图片
    private long size;//歌曲大小
    private String path;//歌曲路径
    private String ismusic;//是不是歌曲

    private DrawerLayout mDrawerLayout;

    List<Music> musicList = new ArrayList<>();
    MusicAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        //toolbar
        Toolbar toobar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toobar);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView)findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu);
        }
        //设置侧边栏menu监听(我的消息、我的好友、我的喜欢、听歌识曲、设置)
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_mail:
                        break;
                    case R.id.nav_friend:
                        break;
                    case R.id.nav_like:
                        break;
                    case R.id.nav_recognition:
                        break;
                    case R.id.nav_setting:
                        break;
                    default:
                }
                return true;
            }
        });
        //读取音乐
        ListView musicView = (ListView)findViewById(R.id.music_view);
        //给ListView设置menu
        musicView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                contextMenu.add(0,1,0,"收藏");
                contextMenu.add(0,2,0,"重命名");
                contextMenu.add(0,3,0,"删除");
            }
        });


        adapter = new MusicAdapter(this, R.layout.music_item, musicList);//设置适配器
        musicView.setAdapter(adapter);
        //判断是否有权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            readMusic();
        }
        //设置监听器
        musicView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Music music = musicList.get(position);
//                String information = "name: "+music.getName()+"\nalbum:"+music.getAlbum()+
//                        "\nsinger:"+music.getSinger()+"\nmusicPath:"+music.getPath();
//                Toast.makeText(MainActivity.this, information, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, PlayMusicActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("musicList", (Serializable)musicList);
                startActivity(intent);
            }
        });
    }

    //ListView的菜单选择
    @Override
    public boolean onContextItemSelected(MenuItem menuItem) {
        ContextMenu.ContextMenuInfo info = menuItem.getMenuInfo();
        AdapterView.AdapterContextMenuInfo contextMenuinfo = (AdapterView.AdapterContextMenuInfo)info;
        final int position = contextMenuinfo.position;
        switch (menuItem.getItemId()) {
            //将音乐添加到Likemusic表中
            case 1:
                Music music = musicList.get(position);
                Likemusic likemusic = new Likemusic();
                likemusic.setName(music.getName());
                likemusic.setSinger(music.getSinger());
                likemusic.setAlbum(music.getAlbum());
                likemusic.setDuration(music.getDuration());
                likemusic.setImageId(music.getImageId());
                likemusic.setSize(music.getSize());
                likemusic.setPath(music.getPath());
//                likemusic.save();//向数据库中插入数据
                break;
            case 2:
                break;
            case 3:
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("提示：");
                dialog.setMessage("是否确定删除此音乐？");
                dialog.setCancelable(true);
                dialog.setPositiveButton("取消", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){}
                });
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        musicList.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });
                dialog.show();
                break;
            default:
        }

        return super.onContextItemSelected(menuItem);
    }

    //内容提供器，读取SD卡中的音乐
    private void readMusic(){
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    //获取音乐的相关信息
                    name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                    singer = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                    album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM));
                    duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                    imageId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                    size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                    path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                    ismusic = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.IS_MUSIC));
                    name = name.replace(" - " + singer, "");
                    name = name.replace(".mp3", "");
                    if (ismusic.equals("1")) {
                        Music music = new Music(name, singer, album, duration, imageId, size, path);
//                    music.setName(name);
//                    music.setSinger(singer);
//                    music.setAlbum(album);
//                    music.setDuration(duration);
//                    music.setImageId(imageId);
//                    music.setSize(size);
//                    music.setPath(path);
                        musicList.add(music);
//                        music.save();//向数据库中添加音乐数据
                    }
//                    Log.d("MainActivity", "name: "+name+"\nalbum:"+album+
//                            "\nsinger:"+singer+"\nalbum_id:"+album_id+
//                            "\nalbum_key:"+album_key+"\nismusic:"+ismusic);
                }
                adapter.notifyDataSetChanged();//数据改变后刷新的作用
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    readMusic();
                } else {
                    Toast.makeText(this, "您缺少权限！", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){//滑动页面方法(侧边栏)
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }


}
