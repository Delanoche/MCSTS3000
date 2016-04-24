package com.connorhenke.mcts3000.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.connorhenke.mcts.R;
import com.connorhenke.mcts3000.RoutesAdapter;
import com.connorhenke.mcts3000.activities.MapActivity;
import com.connorhenke.mcts3000.loaders.RoutesLoader;
import com.connorhenke.mcts3000.models.Route;

import java.util.List;

public class RoutesFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Route>> {

    private RoutesAdapter adapter;
    private ListView listView;
    private View progress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.routes, null, false);

        progress = view.findViewById(R.id.routes_progress_bar);
        listView = (ListView) view.findViewById(android.R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Route item = adapter.getItem(position);
                Intent intent = MapActivity.newIntent(getActivity(), item.getNumber(), item.getName(), item.getColor());
                View viewStart = view.findViewById(R.id.color_block);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), viewStart, getString(R.string.route_transition));
                ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
            }
        });

        if (adapter == null) {
            adapter = new RoutesAdapter(getActivity(), R.layout.route_item);
        }
        listView.setAdapter(adapter);

        return view;
    }

    private void hideProgressBar() {
        progress.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public Loader<List<Route>> onCreateLoader(int id, Bundle args) {
        return new RoutesLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<Route>> loader, List<Route> data) {
        adapter.clear();
        hideProgressBar();
        if (data == null) {
            Toast.makeText(getActivity(), "Could not load routes. Check network connection", Toast.LENGTH_LONG).show();
        } else {
            adapter.addAll(data);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Route>> loader) {
        // Unused
    }
}
