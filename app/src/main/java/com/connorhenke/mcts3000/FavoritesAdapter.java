package com.connorhenke.mcts3000;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.connorhenke.mcts.R;
import com.connorhenke.mcts3000.models.Favorite;

public class FavoritesAdapter extends ArrayAdapter<Favorite> {

    private LayoutInflater inflater;

    public FavoritesAdapter(Context context, int layoutResourceId) {
        super(context, layoutResourceId);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.item_favorite, parent, false);
            holder = new ViewHolder();
            view.setTag(holder);
            holder.name = (TextView) view.findViewById(R.id.favorite_name);
            holder.route = (TextView) view.findViewById(R.id.route);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.name.setText(getItem(position).getDirection());
        holder.route.setText(getItem(position).getStopName());

        return view;
    }

    private static int parseColor(String color) {
        return Color.rgb(Integer.valueOf(color.substring(1, 3), 16).intValue(), Integer.valueOf(color.substring(3, 5), 16).intValue(), Integer.valueOf(color.substring(5, 7), 16).intValue());
    }

    private class ViewHolder {

        private TextView id;
        private TextView name;
        private TextView route;

    }
}
