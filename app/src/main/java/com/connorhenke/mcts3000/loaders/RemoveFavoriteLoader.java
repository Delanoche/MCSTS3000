package com.connorhenke.mcts3000.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import com.connorhenke.mcts3000.models.Favorite;
import com.connorhenke.mcts3000.persistence.SQLiteOpenHelperImpl;

import java.util.List;

public class RemoveFavoriteLoader extends AsyncTaskLoader<List<Favorite>> {

    private static final String ARG_FAVORITE = "favorite";
    private Favorite favorite;

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    public static Bundle newBundle(Favorite favorite) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_FAVORITE, favorite);
        return bundle;
    }

    public RemoveFavoriteLoader(Context context, Bundle args) {
        super(context);

        this.favorite = args.getParcelable(ARG_FAVORITE);
    }

    @Override
    public List<Favorite> loadInBackground() {
        SQLiteOpenHelperImpl.getInstance(getContext()).deleteFavorite(favorite);
        List<Favorite> routes = SQLiteOpenHelperImpl.getInstance(getContext()).getFavorites();
        return routes;
    }
}
