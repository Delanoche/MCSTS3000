package com.connorhenke.mcts3000.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.connorhenke.mcts.R;
import com.connorhenke.mcts3000.fragments.FavoritesFragment;
import com.connorhenke.mcts3000.fragments.RoutesFragment;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private RoutesFragment routesFragment;
    private FavoritesFragment favoritesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        routesFragment = new RoutesFragment();
        favoritesFragment = FavoritesFragment.newInstance();
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setBackgroundColor(getResources().getColor(R.color.mcts_blue));
        tabLayout.setTabTextColors(getResources().getColorStateList(R.color.tab_text));
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch(position) {
                case 0: return "Favorites";
                case 1: return "Routes";
                default: return "Routes";
            }
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0: return favoritesFragment;
                case 1: return routesFragment;
                default: return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
