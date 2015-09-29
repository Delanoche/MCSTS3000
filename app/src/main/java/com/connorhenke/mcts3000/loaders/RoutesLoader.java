package com.connorhenke.mcts3000.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.connorhenke.mcts3000.BaseService;
import com.connorhenke.mcts3000.models.Route;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RoutesLoader extends AsyncTaskLoader<List<Route>> {

    public RoutesLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<Route> loadInBackground() {
        List<Route> routes = new ArrayList<>();
        try {
            routes = BaseService.getRoutes();
            return routes;
        } catch (JSONException e) {
            return routes;
        } catch (IOException e) {
            return null;
        }
    }
}
