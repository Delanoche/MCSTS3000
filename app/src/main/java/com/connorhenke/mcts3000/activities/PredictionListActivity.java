package com.connorhenke.mcts3000.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.connorhenke.mcts.R;
import com.connorhenke.mcts3000.NewPredictionAdapter;
import com.connorhenke.mcts3000.loaders.PredictionsLoader;
import com.connorhenke.mcts3000.models.Favorite;
import com.connorhenke.mcts3000.models.Prediction;
import com.connorhenke.mcts3000.persistence.SQLiteOpenHelperImpl;

import java.util.List;

public class PredictionListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Prediction>> {

    private static final String ARG_FAVORITE = "favorite";

    private RecyclerView recyclerView;
    private NewPredictionAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private Favorite favorite;

    public static Intent newIntent(Context context, Favorite favorite) {
        Intent intent = new Intent(context, PredictionListActivity.class);
        intent.putExtra(ARG_FAVORITE, favorite);
        return intent;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_prediction_list);
        recyclerView = (RecyclerView) findViewById(R.id.prediction_recycler_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_favorite);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        adapter = new NewPredictionAdapter();
        recyclerView.setAdapter(adapter);

        favorite = getIntent().getParcelableExtra(ARG_FAVORITE);
        getSupportLoaderManager().restartLoader(0, PredictionsLoader.newBundle(favorite.getStopId()), this);
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
        return new PredictionsLoader(PredictionListActivity.this, args);
    }

    @Override
    public void onLoadFinished(Loader<List<Prediction>> loader, List<Prediction> data) {
        adapter.setData(data);
        if (data != null) {
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "Could not load predictions, check network connection", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Prediction>> loader) {

    }
}
