package com.example.a22607.show.mainfragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.a22607.show.R;
import com.example.a22607.show.show_activity.FullVideoActivity;
import com.example.a22607.show.widget.MediaHelp;
import com.example.a22607.show.widget.VideoBean;
import com.example.a22607.show.widget.VideoSuperPlayer;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 22607 on 2017/2/25.
 */

public class MainFragment extends Fragment {
    private String url = "http://olx26c04u.bkt.clouddn.com/huluwa.mp4";
    private List<VideoBean> mList;
    private ListView mListView;
    private boolean isPlaying;
    private int indexPostion = -1;
    private MAdapter mAdapter;

    @Override
    public void onDestroy() {
        MediaHelp.release();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        MediaHelp.resume();
        super.onResume();
    }

    @Override
   public void onPause() {
        MediaHelp.pause();
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MediaHelp.getInstance().seekTo(data.getIntExtra("position", 0));
    }
    @Nullable
    @Override
    public View  onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.fragment_main,container,false);
        mListView = (ListView)view.findViewById(R.id.list);
        mList = new ArrayList<VideoBean>();
        for (int i = 0; i < 100; i++) {
            mList.add(new VideoBean(url));
        }
        mAdapter = new MAdapter(getContext());
        mListView.setAdapter(mAdapter);
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if ((indexPostion < mListView.getFirstVisiblePosition() || indexPostion > mListView
                        .getLastVisiblePosition()) && isPlaying) {
                    indexPostion = -1;
                    isPlaying = false;
                    mAdapter.notifyDataSetChanged();
                    MediaHelp.release();
                }
            }
        });
        return  view;
    }
    class MAdapter extends BaseAdapter {
        private Context context;
        LayoutInflater inflater;

        public MAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public VideoBean getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public View getView(int position, View v, ViewGroup parent) {
            GameVideoViewHolder holder = null;
            if (v == null) {
                holder = new GameVideoViewHolder();
                v = inflater.inflate(R.layout.list_video_item, parent, false);
                holder.mVideoViewLayout = (VideoSuperPlayer) v.findViewById(R.id.video);
                holder.mPlayBtnView = (ImageView) v.findViewById(R.id.play_btn);
                v.setTag(holder);
            } else {
                holder = (GameVideoViewHolder) v.getTag();
            }
            holder.mPlayBtnView.setOnClickListener(new MyOnclick(
                    holder.mPlayBtnView, holder.mVideoViewLayout, position));
            if (indexPostion == position) {
                holder.mVideoViewLayout.setVisibility(View.VISIBLE);
            } else {
                holder.mVideoViewLayout.setVisibility(View.GONE);
                holder.mVideoViewLayout.close();
            }
            return v;
        }

        class MyOnclick implements View.OnClickListener {
            VideoSuperPlayer mSuperVideoPlayer;
            ImageView mPlayBtnView;
            int position;

            public MyOnclick(ImageView mPlayBtnView,
                             VideoSuperPlayer mSuperVideoPlayer, int position) {
                this.position = position;
                this.mSuperVideoPlayer = mSuperVideoPlayer;
                this.mPlayBtnView = mPlayBtnView;
            }

            @Override
            public void onClick(View v) {
                MediaHelp.release();
                indexPostion = position;
                isPlaying = true;
                mSuperVideoPlayer.setVisibility(View.VISIBLE);
                mSuperVideoPlayer.loadAndPlay(MediaHelp.getInstance(), mList
                        .get(position).getUrl(), 0, false);
                mSuperVideoPlayer.setVideoPlayCallback(new MyVideoPlayCallback(
                        mPlayBtnView, mSuperVideoPlayer, mList.get(position)));
                notifyDataSetChanged();
            }
        }

        class MyVideoPlayCallback implements VideoSuperPlayer.VideoPlayCallbackImpl {
            ImageView mPlayBtnView;
            VideoSuperPlayer mSuperVideoPlayer;
            VideoBean info;

            public MyVideoPlayCallback(ImageView mPlayBtnView,
                                       VideoSuperPlayer mSuperVideoPlayer, VideoBean info) {
                this.mPlayBtnView = mPlayBtnView;
                this.info = info;
                this.mSuperVideoPlayer = mSuperVideoPlayer;
            }

            @Override
            public void onCloseVideo() {
                closeVideo();
            }

            @Override
            public void onSwitchPageType() {
                if (((Activity) context).getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                    Intent intent = new Intent(new Intent(context,
                            FullVideoActivity.class));
                    intent.putExtra("video", info);
                    intent.putExtra("position",
                            mSuperVideoPlayer.getCurrentPosition());
                    ((Activity) context).startActivityForResult(intent, 1);
                }
            }

            @Override
            public void onPlayFinish() {
                closeVideo();
            }

            private void closeVideo() {
                isPlaying = false;
                indexPostion = -1;
                mSuperVideoPlayer.close();
                MediaHelp.release();
                mPlayBtnView.setVisibility(View.VISIBLE);
                mSuperVideoPlayer.setVisibility(View.GONE);
            }

        }

        class GameVideoViewHolder {

            private VideoSuperPlayer mVideoViewLayout;
            private ImageView mPlayBtnView;

        }

    }


}
