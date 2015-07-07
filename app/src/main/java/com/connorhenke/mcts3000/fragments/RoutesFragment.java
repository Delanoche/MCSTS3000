package com.connorhenke.mcts3000.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.connorhenke.mcts.R;
import com.connorhenke.mcts3000.RoutesAdapter;
import com.connorhenke.mcts3000.activities.MapActivity;
import com.connorhenke.mcts3000.loaders.RoutesLoader;
import com.connorhenke.mcts3000.models.Route;

import java.util.List;

public class RoutesFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Route>> {

    private RoutesAdapter adapter;
    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.routes, null, false);

        listView = (ListView) view.findViewById(android.R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView number = (TextView) view.findViewById(R.id.number);
                TextView name = (TextView) view.findViewById(R.id.name);
                Intent intent = new Intent(getActivity(), MapActivity.class);
                intent.putExtra("NUMBER", number.getText());
                intent.putExtra("NAME", name.getText());
                startActivity(intent);
            }
        });

        if (adapter == null) {
            adapter = new RoutesAdapter(getActivity(), R.layout.route_item);
        }
        listView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLoaderManager().initLoader(0, null, this).forceLoad();
    }

    @Override
    public Loader<List<Route>> onCreateLoader(int id, Bundle args) {
        return new RoutesLoader(getActivity());
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
