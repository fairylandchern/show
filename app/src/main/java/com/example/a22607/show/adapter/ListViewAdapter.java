package com.example.a22607.show.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.a22607.show.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 22607 on 2017/3/4.
 */

public class ListViewAdapter extends BaseAdapter {
    private Context context;
    LayoutInflater layoutInflater;
    private List<Map<String,String>> data=getData();

    //获取到数据
    private List<Map<String,String>>  getData()
    {
        List<Map<String,String>> list=new ArrayList<>();
        Map<String,String> map;
        for(int i=0;i<20;i++){
            map=new HashMap<String,String>();
            map.put("path","http://olx26c04u.bkt.clouddn.com/baluob.mp4");
            map.put("title","视频拔萝卜");
            list.add(map);
        }
        return  list;
    }

    public ListViewAdapter(Context context){
        this.context=context;
      layoutInflater =LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return  data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View item=layoutInflater.inflate(R.layout.video_item,null);

        TextView textview=(TextView)item.findViewById(R.id.tv_video);
        textview.setText(data.get(position).get("title"));

        VideoView videoView=(VideoView)item.findViewById(R.id.video_item);
        videoView.setMediaController(new MediaController(context));
        videoView.setVideoPath(data.get(position).get("path"));
        videoView.start();
        videoView.requestFocus();
        return item;
    }
}

