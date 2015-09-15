package com.connorhenke.mcts3000;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.connorhenke.mcts.R;
import com.connorhenke.mcts3000.models.Route;

public class RoutesAdapter extends ArrayAdapter<Route> {

    private LayoutInflater inflater;

    public RoutesAdapter(Context context, int layoutResourceId) {
        super(context, layoutResourceId);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.route_item, parent, false);
            holder = new ViewHolder();
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.number = (TextView) view.findViewById(R.id.number);
        holder.name = (TextView) view.findViewById(R.id.name);

        holder.number.setText(getItem(position).getNumber());
        holder.name.setText(getItem(position).getName());

        holder.color = view.findViewById(R.id.color_block);
        int bg = Color.parseColor(getItem(position).getColor());
        holder.color.setBackgroundColor(bg);

        return view;
    }

    private static int parseColor(String color) {
        return Color.rgb(Integer.valueOf(color.substring(1, 3), 16).intValue(), Integer.valueOf(color.substring(3, 5), 16).intValue(), Integer.valueOf(color.substring(5, 7), 16).intValue());
    }

    private class ViewHolder {

        private TextView number;
        private TextView name;
        private View color;

    }
}
