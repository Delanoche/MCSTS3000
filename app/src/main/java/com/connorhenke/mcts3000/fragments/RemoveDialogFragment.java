package com.connorhenke.mcts3000.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.connorhenke.mcts3000.models.Favorite;
import com.connorhenke.mcts3000.persistence.SQLiteOpenHelperImpl;

public class RemoveDialogFragment extends DialogFragment {

    private static final String ARG_FAVORITE = "favorite";

    public static RemoveDialogFragment newInstance(Favorite favorite) {

        Bundle args = new Bundle();
        args.putParcelable(ARG_FAVORITE, favorite);

        RemoveDialogFragment fragment = new RemoveDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Favorite favorite = getArguments().getParcelable(ARG_FAVORITE);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        AlertDialog dialog = builder.setTitle("Remove favorite")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((FavoriteRemovedListener)getActivity()).favoriteRemoved(favorite);
                    }
                })
                .setNegativeButton("No", null)
                .create();
        return dialog;
    }

    public interface FavoriteRemovedListener {
        void favoriteRemoved(Favorite favorite);
    }
}
