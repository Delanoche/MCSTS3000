package com.connorhenke.mcts3000;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.connorhenke.mcts.R;
import com.connorhenke.mcts3000.models.Prediction;

public class PredictionAdapter extends ArrayAdapter<Prediction> {

    private LayoutInflater inflater;

    public PredictionAdapter(Context context, int layoutResourceId) {
        super(context, layoutResourceId);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.item_prediction, parent, false);
            holder = new ViewHolder();
            view.setTag(holder);
            holder.time = (TextView) view.findViewById(R.id.favorite_name);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Prediction prediction = getItem(position);
        if (prediction.isDelay()) {
            holder.time.setText(R.string.delayed);
        } else if (prediction.getPrdctdn().equals("DUE")) {
            holder.time.setText(R.string.due);
        } else {
            holder.time.setText(prediction.getPrdctdn());
        }

        return view;
    }

    private class ViewHolder {

        private TextView time;
    }
}

