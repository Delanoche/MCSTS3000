package com.connorhenke.mcts3000;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.connorhenke.mcts.R;
import com.connorhenke.mcts.databinding.PredictionCardBinding;
import com.connorhenke.mcts3000.models.Prediction;

import java.util.ArrayList;
import java.util.List;

public class NewPredictionAdapter extends RecyclerView.Adapter<NewPredictionAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public PredictionCardBinding binding;
        public ViewHolder(PredictionCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private List<Prediction> predictions;

    // Provide a suitable constructor (depends on the kind of dataset)
    public NewPredictionAdapter() {
        predictions = new ArrayList<>();
    }

    public void setData(List<Prediction> predictions) {
        this.predictions = predictions;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public NewPredictionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        PredictionCardBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.prediction_card, parent, false);
        return new ViewHolder(binding);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.binding.setPrediction(predictions.get(position));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return predictions.size();
    }

}
