package com.example.boardqactivity.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.boardqactivity.R;
import com.example.boardqactivity.adapter.FragmentAdapter;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    TabLayout tabMainTab;
    ViewPager2 vpMainVp;
    FragmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabMainTab = (TabLayout) findViewById(R.id.tab_mainTab);
        vpMainVp = (ViewPager2) findViewById(R.id.vp_mainVp);

        FragmentManager fm = getSupportFragmentManager();
        adapter = new FragmentAdapter(fm, getLifecycle());
        vpMainVp.setAdapter(adapter);

        tabMainTab.addTab(tabMainTab.newTab().setText("게시판"));
        tabMainTab.addTab(tabMainTab.newTab().setText("문의하기"));

        tabMainTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vpMainVp.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        vpMainVp.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabMainTab.selectTab(tabMainTab.getTabAt(position));
            }
        });

    }
}