package com.connorhenke.mcts3000.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.connorhenke.mcts3000.BaseService;
import com.connorhenke.mcts3000.Route;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class RoutesLoader extends AsyncTaskLoader<List<Route>> {

    public RoutesLoader(Context context) {
        super(context);
    }

    @Override
    public List<Route> loadInBackground() {
        try {
            List<Route> routes = BaseService.getRoutes();
            return routes;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
