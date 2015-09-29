package com.connorhenke.mcts3000.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import com.connorhenke.mcts3000.BaseService;
import com.connorhenke.mcts3000.models.Direction;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DirectionsLoader extends AsyncTaskLoader<List<Direction>> {

    private static final String ARG_ROUTE = "route";
    private String route;

    public static Bundle newBundle(String route) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_ROUTE, route);
        return bundle;
    }

    public DirectionsLoader(Context context, Bundle args) {
        super(context);

        this.route = args.getString(ARG_ROUTE);
    }

    @Override
    public List<Direction> loadInBackground() {
        List<Direction> directions = new ArrayList<>();
        try {
            directions = BaseService.getDirections(route);
            return directions;
        } catch (JSONException e) {
            return directions;
        } catch (IOException e) {
            return null;
        }
    }
}
