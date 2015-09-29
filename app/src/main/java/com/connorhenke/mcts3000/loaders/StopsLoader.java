package com.connorhenke.mcts3000.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import com.connorhenke.mcts3000.BaseService;
import com.connorhenke.mcts3000.models.Stop;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StopsLoader extends AsyncTaskLoader<List<Stop>> {

    private static final String ARG_ROUTE = "route";
    private static final String ARG_DIR = "dir";
    private String route;
    private String dir;

    public static Bundle newBundle(String route, String dir) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_ROUTE, route);
        bundle.putString(ARG_DIR, dir);
        return bundle;
    }

    public StopsLoader(Context context, Bundle args) {
        super(context);

        this.route = args.getString(ARG_ROUTE);
        this.dir = args.getString(ARG_DIR);
    }

    public StopsLoader(Context context) {
        super(context);
    }

    @Override
    public List<Stop> loadInBackground() {
        List<Stop> stops = new ArrayList<>();
        try {
            stops = BaseService.getStops(route, dir);
            return stops;
        } catch (JSONException e) {
            return stops;
        } catch (IOException e) {
            return null;
        }
    }
}
