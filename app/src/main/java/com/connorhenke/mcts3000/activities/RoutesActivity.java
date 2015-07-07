package com.connorhenke.mcts3000.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.connorhenke.mcts.R;
import com.connorhenke.mcts3000.RoutesAdapter;
import com.connorhenke.mcts3000.loaders.RoutesLoader;
import com.connorhenke.mcts3000.models.Route;

import java.util.ArrayList;
import java.util.List;

public class RoutesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Route>> {

    protected RoutesAdapter adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setEnterTransition(new Explode());
            getWindow().setExitTransition(new Explode());
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.routes);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) this.findViewById(android.R.id.list);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView number = (TextView) view.findViewById(R.id.number);
                TextView name = (TextView) view.findViewById(R.id.name);
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                intent.putExtra("NUMBER", number.getText());
                intent.putExtra("NAME", name.getText());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(RoutesActivity.this).toBundle());
                } else {
                    startActivity(intent);
                }
            }
        });

        if (adapter == null) {
            adapter = new RoutesAdapter(this, R.layout.route_item);
        }
        listView.setAdapter(adapter);

        getSupportLoaderManager().initLoader(0, null, this).forceLoad();
    }

    @Override
    public Loader<List<Route>> onCreateLoader(int id, Bundle args) {
        return new RoutesLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<Route>> loader, List<Route> data) {
        try {
            adapter.clear();
            adapter.addAll(data);
            adapter.notifyDataSetChanged();
        } catch (Exception e) {

        }
    }

    @Override
    public void onLoaderReset(Loader<List<Route>> loader) {
        // Unused
    }
}
