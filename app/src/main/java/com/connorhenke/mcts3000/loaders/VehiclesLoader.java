package com.connorhenke.mcts3000.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import com.connorhenke.mcts3000.BaseService;
import com.connorhenke.mcts3000.Bus;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class VehiclesLoader extends AsyncTaskLoader<List<Bus>> {

    public static final String ARG_ROUTE = "route";
    private String route;

    public static Bundle newBundle(String route) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_ROUTE, route);
        return bundle;
    }

    public VehiclesLoader(Context context, Bundle args) {
        super(context);

        this.route = args.getString(ARG_ROUTE);
    }

    @Override
    public List<Bus> loadInBackground() {
        try {
            List<Bus> buses = BaseService.getVehicles(route);
            return buses;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
