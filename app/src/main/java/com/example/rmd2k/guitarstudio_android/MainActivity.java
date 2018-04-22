package com.example.rmd2k.guitarstudio_android;

import android.os.MessageQueue;
import android.support.v4.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.rmd2k.guitarstudio_android.Musiclibrary.MusicLibraryFragment;
import com.example.rmd2k.guitarstudio_android.MyZone.MyFragment;
import com.example.rmd2k.guitarstudio_android.MyZone.MyFragmentPagerAdapter;
import com.example.rmd2k.guitarstudio_android.Setting.SettingFragment;
import com.example.rmd2k.guitarstudio_android.Tools.ToolsFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;

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

        FragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments, new String[]{"我的", "乐库", "工具", "设置"});
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

}
