package com.example.a22607.show.show_activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.a22607.show.R;
import com.example.a22607.show.widget.MediaHelp;
import com.example.a22607.show.widget.VideoBean;
import com.example.a22607.show.widget.VideoMediaController;
import com.example.a22607.show.widget.VideoSuperPlayer;

public class FullVideoActivity extends Activity {
	private VideoSuperPlayer mVideo;
	private VideoBean info;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);// 横屏
		this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_full);
		mVideo = (VideoSuperPlayer) findViewById(R.id.video);
		info = (VideoBean) getIntent().getExtras().getSerializable("video");
		mVideo.loadAndPlay(MediaHelp.getInstance(), info.getUrl(), getIntent()
				.getExtras().getInt("position"), true);
		mVideo.setPageType(VideoMediaController.PageType.EXPAND);
		mVideo.setVideoPlayCallback(new VideoSuperPlayer.VideoPlayCallbackImpl() {


			public void onSwitchPageType() {
				if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
					finish();
				}
			}


			public void onPlayFinish() {
				finish();
			}


			public void onCloseVideo() {
				finish();
			}
		});
	}

	@Override
	public void finish() {
		Intent intent = new Intent();
		intent.putExtra("position", mVideo.getCurrentPosition());
		setResult(RESULT_OK, intent);
		super.finish();
	}

	@Override
	protected void onPause() {
		super.onPause();
		MediaHelp.pause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MediaHelp.resume();
	}
}
