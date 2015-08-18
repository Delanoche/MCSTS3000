package com.connorhenke.mcts3000.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.connorhenke.mcts.R;
import com.connorhenke.mcts3000.PredictionAdapter;
import com.connorhenke.mcts3000.loaders.PredictionsLoader;
import com.connorhenke.mcts3000.models.Favorite;
import com.connorhenke.mcts3000.models.Prediction;
import com.connorhenke.mcts3000.persistence.SQLiteOpenHelperImpl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Prediction>> {

    private static final String ARG_FAVORITE = "favorite";

    private Favorite favorite;
    private List<Prediction> times;
    private LinearLayout layout;

    public static Intent newIntent(Context context, Favorite favorite) {
        Intent intent = new Intent(context, FavoriteActivity.class);
        intent.putExtra(ARG_FAVORITE, favorite);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        favorite = getIntent().getParcelableExtra(ARG_FAVORITE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Arrival Times");

        ((TextView) findViewById(R.id.route)).setText(favorite.getRouteId() + " " + favorite.getDirection());
        ((TextView) findViewById(R.id.name)).setText(favorite.getStopName());

        layout = (LinearLayout) findViewById(R.id.times);
        times = new LinkedList<>();

        getSupportLoaderManager().restartLoader(0, PredictionsLoader.newBundle(favorite.getRouteId(), favorite.getStopId()), this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_favorite, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_favorite:
                SQLiteOpenHelperImpl.getInstance(this).addFavorite(favorite);
                Toast.makeText(this, "Favorite added", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<List<Prediction>> onCreateLoader(int id, Bundle args) {
        return new PredictionsLoader(FavoriteActivity.this, args);
    }

    @Override
    public void onLoadFinished(Loader<List<Prediction>> loader, List<Prediction> data) {
        layout.removeAllViews();
        times.clear();
        times.addAll(data);
        LayoutInflater inflater = LayoutInflater.from(this);

        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(250);
        LayoutAnimationController animationController = new LayoutAnimationController(animation, 0.5f);
        layout.setLayoutAnimation(animationController);
        if (times.size() > 0) {
            for (Prediction prediction : times) {
                View view = inflater.inflate(R.layout.item_prediction, null);
                view.setAnimation(animation);
                TextView time = (TextView) view.findViewById(R.id.time);
                if (prediction.isDelay()) {
                    time.setText("Delayed");
                } else if (prediction.getPrdctdn().equals("DUE")) {
                    time.setText("Due");
                } else {
                    time.setText(prediction.getPrdctdn() + " minutes");
                }
                view.animate();
                layout.addView(view);
            }
        } else {
            View view = inflater.inflate(R.layout.item_prediction, null);
            view.setAnimation(animation);
            TextView time = (TextView) view.findViewById(R.id.time);
            time.setText("No buses currently en route");
            view.animate();
            layout.addView(view);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Prediction>> loader) {

    }
}
