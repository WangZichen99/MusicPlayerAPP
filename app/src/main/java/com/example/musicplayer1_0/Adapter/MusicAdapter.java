package com.example.musicplayer1_0.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.musicplayer1_0.R;

import java.util.List;

/**
 * Created by 王子琛 on 2019/12/16.
 */

public class MusicAdapter extends ArrayAdapter<Music> {

    private int resourceId;

    public MusicAdapter(Context context, int textViewResourceId, List<Music> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Music music = getItem(position);//获取当前项的Music实例
        View view;
        ViewHolder viewHolder;
        //如果convertView为空，就加载布局，否则直接重用convertView缓存
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.number = (TextView)view.findViewById(R.id.number);
            viewHolder.musicName = (TextView)view.findViewById(R.id.music_name);
            viewHolder.musicSingerAlbum = (TextView)view.findViewById(R.id.music_singer_album);
            view.setTag(viewHolder);

        } else {
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.number.setText(String.valueOf(position + 1));
        viewHolder.musicName.setText(music.getName());
        viewHolder.musicSingerAlbum.setText(music.getSinger() + " — " + music.getAlbum());
        return view;
    }

    class ViewHolder {

        TextView number;
        TextView musicName;
        TextView musicSingerAlbum;
    }

}
