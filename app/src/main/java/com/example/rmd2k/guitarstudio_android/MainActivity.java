package com.example.rmd2k.guitarstudio_android;

import android.os.MessageQueue;
import android.support.v4.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.rmd2k.guitarstudio_android.Musiclibrary.MusicLibraryFragment;
import com.example.rmd2k.guitarstudio_android.MyZone.MyFragment;
import com.example.rmd2k.guitarstudio_android.MyZone.MyFragmentPagerAdapter;
import com.example.rmd2k.guitarstudio_android.Setting.SettingFragment;
import com.example.rmd2k.guitarstudio_android.Tools.ToolsFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    TabLayout tabLayout;
    MyViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initViewPagerWithFragments();
    }

    private void initView() {
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
    }

    private void initViewPagerWithFragments() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new MyFragment());
        fragments.add(new MusicLibraryFragment());
        fragments.add(new MyFragment());
        fragments.add(new MyFragment());

        String myZone = getText(R.string.navi_myZone).toString();
        String musicLibrary = getText(R.string.navi_musicLibrary).toString();
        String tool = getText(R.string.navi_tool).toString();
        String setting = getText(R.string.navi_setting).toString();

        FragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments, new String[]{myZone, musicLibrary, tool, setting});
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
    
}
