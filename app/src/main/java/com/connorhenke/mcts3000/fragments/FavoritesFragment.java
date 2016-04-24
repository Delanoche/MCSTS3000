package com.connorhenke.mcts3000.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.connorhenke.mcts.R;
import com.connorhenke.mcts3000.FavoritesAdapter;
import com.connorhenke.mcts3000.activities.FavoriteActivity;
import com.connorhenke.mcts3000.activities.PredictionListActivity;
import com.connorhenke.mcts3000.loaders.FavoritesLoader;
import com.connorhenke.mcts3000.loaders.RemoveFavoriteLoader;
import com.connorhenke.mcts3000.models.Favorite;
import com.connorhenke.mcts3000.persistence.SQLiteOpenHelperImpl;

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
        listView.setEmptyView(view.findViewById(android.R.id.empty));

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Favorite favorite = adapter.getItem(position);
                RemoveDialogFragment.newInstance(favorite).show(getFragmentManager(), "");
                return true;
            }
        });

        if (adapter == null) {
            adapter = new FavoritesAdapter(getActivity(), R.layout.item_favorite);
        }
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Favorite item = adapter.getItem(position);
//                startActivity(FavoriteActivity.newIntent(getActivity(), item));
                startActivity(PredictionListActivity.newIntent(getActivity(), item));
            }
        });

        return view;
    }

    public void restartLoader() {
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        restartLoader();
    }

    public void removedFavorite(Favorite favorite) {
        getLoaderManager().restartLoader(0, RemoveFavoriteLoader.newBundle(favorite), new RemoveFavoriteCallback());
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

    private class RemoveFavoriteCallback implements LoaderManager.LoaderCallbacks<List<Favorite>> {
        @Override
        public Loader<List<Favorite>> onCreateLoader(int id, Bundle args) {
            return new RemoveFavoriteLoader(getActivity(), args);
        }

        @Override
        public void onLoadFinished(Loader<List<Favorite>> loader, List<Favorite> data) {
            adapter.clear();
            adapter.addAll(data);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onLoaderReset(Loader<List<Favorite>> loader) {
            // No
        }
    }
}
