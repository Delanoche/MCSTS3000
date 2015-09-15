package com.connorhenke.mcts3000.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.connorhenke.mcts3000.models.Favorite;
import com.connorhenke.mcts3000.persistence.SQLiteOpenHelperImpl;

import java.util.List;

public class FavoritesLoader extends AsyncTaskLoader<List<Favorite>> {

    public FavoritesLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<Favorite> loadInBackground() {
        List<Favorite> routes = SQLiteOpenHelperImpl.getInstance(getContext()).getFavorites();
        return routes;
    }
}
