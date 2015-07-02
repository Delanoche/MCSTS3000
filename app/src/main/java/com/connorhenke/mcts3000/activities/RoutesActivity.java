package com.connorhenke.mcts3000.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
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

    protected List<Route> routes = new ArrayList<Route>();
    protected RoutesAdapter adapter;
    ListView listView;
    SwipeRefreshLayout swipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                startActivity(intent);
                overridePendingTransition(R.anim.lefttoright, R.anim.righttoleft);

            }
        });

        swipeLayout = (SwipeRefreshLayout) this.findViewById(R.id.swipe_container);
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSupportLoaderManager().restartLoader(0, null, RoutesActivity.this).forceLoad();
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
            swipeLayout.setRefreshing(false);
            adapter.notifyDataSetChanged();
        } catch (Exception e) {

        }
    }

    @Override
    public void onLoaderReset(Loader<List<Route>> loader) {
        // Unused
    }
}
