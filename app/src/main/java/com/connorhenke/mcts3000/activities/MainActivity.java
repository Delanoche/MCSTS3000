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
import com.connorhenke.mcts3000.fragments.RemoveDialogFragment;
import com.connorhenke.mcts3000.fragments.RoutesFragment;
import com.connorhenke.mcts3000.models.Favorite;
import com.connorhenke.mcts3000.persistence.SQLiteOpenHelperImpl;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements RemoveDialogFragment.FavoriteRemovedListener {

    private ViewPager viewPager;
    private RoutesFragment routesFragment;
    private FavoritesFragment favoritesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
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

    @Override
    public void favoriteRemoved(Favorite favorite) {
        favoritesFragment.removedFavorite(favorite);
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch(position) {
                case 0: return getString(R.string.title_favorites);
                case 1: return getString(R.string.title_routes);
                default: return getString(R.string.title_routes);
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
