package com.connorhenke.mcts3000.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.connorhenke.mcts.R;
import com.connorhenke.mcts3000.BaseService;
import com.connorhenke.mcts3000.RoutesAdapter;
import com.connorhenke.mcts3000.activities.MapActivity;
import com.connorhenke.mcts3000.models.Route;

import java.util.List;

import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;

public class RoutesFragment extends Fragment {

    private RoutesAdapter adapter;
    private ListView listView;
    private View progress;

    private SingleSubscriber<List<Route>> subscriber;

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
                Intent intent = MapActivity.newIntent(getActivity(), item.getNumber(), item.getName());
                startActivity(intent);
            }
        });

        if (adapter == null) {
            adapter = new RoutesAdapter(getActivity(), R.layout.route_item);
        }
        listView.setAdapter(adapter);

        subscriber = subscribe();
        BaseService.getRoutes()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        subscriber.unsubscribe();
    }

    private SingleSubscriber<List<Route>> subscribe() {
        return new SingleSubscriber<List<Route>>() {
            @Override
            public void onSuccess(List<Route> data) {
                adapter.clear();
                hideProgressBar();
                if (data == null) {
                    Toast.makeText(getActivity(), R.string.could_not_load_routes, Toast.LENGTH_LONG).show();
                } else {
                    adapter.addAll(data);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Throwable error) {
                Toast.makeText(getActivity(), R.string.could_not_load_routes, Toast.LENGTH_LONG).show();
            }
        };
    }

    private void hideProgressBar() {
        progress.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
    }
}
