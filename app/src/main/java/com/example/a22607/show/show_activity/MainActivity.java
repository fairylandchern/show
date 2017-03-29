package com.example.a22607.show.show_activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import com.example.a22607.show.R;
import com.example.a22607.show.adapter.MainAdapter;
import com.example.a22607.show.httputils.FileDao;
import com.ipaulpro.afilechooser.FileChooserActivity;
import com.ipaulpro.afilechooser.utils.FileUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.fb)
    FloatingActionButton fb;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private MainAdapter mainAdapter;

    private TabLayout.Tab one;
    private TabLayout.Tab two;
    private TabLayout.Tab three;

    private static final int REQUEST_CHOOSER = 1234;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        //使用适配器将Viewpager与Fragment绑定到一起
        mViewPager = (ViewPager) findViewById(R.id.vp_view);
        mainAdapter = new MainAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mainAdapter);

        //设置viewpager无法左右滑动
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        //将tablayout与viewpager绑定到一起
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);
        one = mTabLayout.getTabAt(1);
        two = mTabLayout.getTabAt(2);
        three = mTabLayout.getTabAt(3);
    }

    @OnClick(R.id.fb)
    public void onViewClicked() {
        Intent getContentIntent = FileUtils.createGetContentIntent();
        Intent intent = Intent.createChooser(getContentIntent, "Select a file");
        startActivityForResult(intent, REQUEST_CHOOSER);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHOOSER:
                if (resultCode == RESULT_OK) {
                    final Uri uri = data.getData();
                    // Get the File path from the Uri
                    String path = FileUtils.getPath(this, uri);
                    // Alternatively, use FileUtils.getFile(Context, Uri)
                    if (path != null && FileUtils.isLocal(path)) {
                        final File file = new File(path);

                        Runnable runnable=new Runnable() {
                            @Override
                            public void run() {
                                Map<String,Integer> map=new HashMap<>();
                                map.put("userid",1);
                                FileDao.postfile(file,map);
                            }
                        };
                        Thread thread=new Thread(runnable);
                        thread.start();
                    }
                }
                break;
        }
    }
}
