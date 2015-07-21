package com.connorhenke.mcts3000.fragments;

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
import android.widget.Toast;

import com.connorhenke.mcts.R;
import com.connorhenke.mcts3000.FavoritesAdapter;
import com.connorhenke.mcts3000.loaders.FavoritesLoader;
import com.connorhenke.mcts3000.models.Favorite;

import java.util.List;

public class FavoritesFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Favorite>> {

    private FavoritesAdapter adapter;
    private ListView listView;

    public static FavoritesFragment newInstance() {
        FavoritesFragment fragment = new FavoritesFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_favorites, null, false);

        listView = (ListView) view.findViewById(android.R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "Woo", Toast.LENGTH_SHORT).show();
            }
        });

        if (adapter == null) {
            adapter = new FavoritesAdapter(getActivity(), R.layout.item_favorite);
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
    public Loader<List<Favorite>> onCreateLoader(int id, Bundle args) {
        return new FavoritesLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<Favorite>> loader, List<Favorite> data) {
        adapter.clear();
        adapter.addAll(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<Favorite>> loader) {
        // Unused
    }
}
