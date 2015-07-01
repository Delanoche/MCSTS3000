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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.connorhenke.mcts.R;

import java.util.List;

public class RoutesAdapter extends ArrayAdapter<Route> {

    Context context;
    int layoutResourceId;

    public RoutesAdapter(Context context, int layoutResourceId) {
        super(context, layoutResourceId);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(R.layout.route_item, parent, false);
        }

        FrameLayout list = (FrameLayout) view.findViewById(R.id.routeslist);
//        if(position % 2 == 0) {
//            list.setBackgroundColor(Color.argb(0, 0, 0, 0));
//        } else {
//            list.setBackgroundColor(Color.argb(0, 0, 0, 0));
//        }

        TextView number = (TextView) view.findViewById(R.id.number);
        TextView name = (TextView) view.findViewById(R.id.name);

        number.setText(getItem(position).getNumber());
        name.setText(getItem(position).getName());

        View color = view.findViewById(R.id.color_block);
        int bg = Color.parseColor(getItem(position).getColor());
        Log.d("COLOR", "" + bg);
        color.setBackgroundColor(bg);

        return view;
    }

    private static int parseColor(String color) {
        return Color.rgb(Integer.valueOf(color.substring(1, 3), 16).intValue(), Integer.valueOf(color.substring(3, 5), 16).intValue(), Integer.valueOf(color.substring(5, 7), 16).intValue());
    }
}
