package com.connorhenke.mcts3000.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import com.connorhenke.mcts3000.BaseService;
import com.connorhenke.mcts3000.Prediction;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class PredictionsLoader extends AsyncTaskLoader<List<Prediction>> {

    private static final String ARG_ROUTE = "route";
    private static final String ARG_STOP = "stop";
    private String route;
    private String stop;

    public static Bundle newBundle(String route, String stop) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_ROUTE, route);
        bundle.putString(ARG_STOP, stop);
        return bundle;
    }

    public PredictionsLoader(Context context, Bundle args) {
        super(context);

        this.route = args.getString(ARG_ROUTE);
        this.stop = args.getString(ARG_STOP);
    }

    public PredictionsLoader(Context context) {
        super(context);
    }

    @Override
    public List<Prediction> loadInBackground() {
        try {
            List<Prediction> predictions = BaseService.getPredictions(route, stop);
            return predictions;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}