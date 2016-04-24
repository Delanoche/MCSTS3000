package com.connorhenke.mcts3000.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import com.connorhenke.mcts3000.BaseService;
import com.connorhenke.mcts3000.models.Prediction;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PredictionsLoader extends AsyncTaskLoader<List<Prediction>> {

    private static final String ARG_ROUTE = "route";
    private static final String ARG_STOP = "stop";
    private String route;
    private String stop;

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    public static Bundle newBundle(String route, String stop) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_ROUTE, route);
        bundle.putString(ARG_STOP, stop);
        return bundle;
    }

    public static Bundle newBundle(String stop) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_ROUTE, null);
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
        List<Prediction> predictions = new ArrayList<>();
        try {
            predictions = BaseService.getPredictions(route, stop);
            return predictions;
        } catch (JSONException e) {
            return predictions;
        } catch (IOException e) {
            return null;
        }
    }
}
