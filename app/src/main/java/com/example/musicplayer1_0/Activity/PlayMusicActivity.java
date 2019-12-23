package com.example.musicplayer1_0.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicplayer1_0.Adapter.Music;
import com.example.musicplayer1_0.R;

import java.util.ArrayList;
import java.util.List;

public class PlayMusicActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView musicName;
    private TextView musicSinger;
    private ImageView play;
    private ImageView pause;
    private ImageView last;
    private ImageView next;
    private ImageView back;

    private int position;
    private List<Music> musicList = new ArrayList<>();
    private MediaPlayer mediaPlayer = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playmusic_layout);
        /*得到从MainActivity中传来的数据*/
        position = getIntent().getIntExtra("position", 0);//音乐在列表中的位置，0表示默认值
        musicList = (List<Music>)getIntent().getSerializableExtra("musicList");//得到音乐列表
        musicName = (TextView)findViewById(R.id.music_name);
        musicSinger = (TextView)findViewById(R.id.music_singer);
        musicName.setText(musicList.get(position).getName());
        musicSinger.setText(musicList.get(position).getSinger());
        play = (ImageView)findViewById(R.id.play_music);
        pause = (ImageView)findViewById(R.id.pause_music);
        last = (ImageView)findViewById(R.id.last_music);
        next = (ImageView)findViewById(R.id.next_music);
        back = (ImageView)findViewById(R.id.back);
        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        last.setOnClickListener(this);
        next.setOnClickListener(this);
        back.setOnClickListener(this);

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // 装载完毕回调
                mediaPlayer.start();
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
            @Override
            public void onCompletion(MediaPlayer mp) {
                if ((position + 1) < musicList.size()) {
                    position += 1;
                    try {
                        mediaPlayer.reset();
                        mediaPlayer.setDataSource(musicList.get(position).getPath());
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                        musicName.setText(musicList.get(position).getName());
                        musicSinger.setText(musicList.get(position).getSinger());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(PlayMusicActivity.this, "已经到达最后一首！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (ContextCompat.checkSelfPermission(PlayMusicActivity.this, android.Manifest.permission.
                WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PlayMusicActivity.this, new String[]{
               android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        initMediaPlayer();

    }

    private void initMediaPlayer() {
        try {
            Music music = musicList.get(position);
            mediaPlayer.setDataSource(music.getPath());
            mediaPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initMediaPlayer();
                } else {
                    Toast.makeText(this, "拒绝权限将无法使用程序", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                Intent intent = new Intent(PlayMusicActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.play_music:
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                }
                break;
            case R.id.pause_music:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
                break;
            case R.id.last_music:
                if ((position + 1) < musicList.size()) {
                    position += 1;
                    try {
                        mediaPlayer.reset();
                        mediaPlayer.setDataSource(musicList.get(position).getPath());
                        mediaPlayer.prepare();
                        musicName.setText(musicList.get(position).getName());
                        musicSinger.setText(musicList.get(position).getSinger());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(PlayMusicActivity.this, "已经到达最后一首！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.next_music:
                if ((position - 1) > 0) {
                    position += 1;
                    try {
                        mediaPlayer.reset();
                        mediaPlayer.setDataSource(musicList.get(position).getPath());
                        mediaPlayer.prepare();
                        musicName.setText(musicList.get(position).getName());
                        musicSinger.setText(musicList.get(position).getSinger());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(PlayMusicActivity.this, "已经到达第一首！", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}
