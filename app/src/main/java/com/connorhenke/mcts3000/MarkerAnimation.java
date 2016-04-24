package com.connorhenke.mcts3000;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.DrawableRes;
import android.util.Log;
import android.util.Property;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.connorhenke.mcts.R;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class MarkerAnimation {

    public static void animateMarkerIn(final Marker marker, Context context, @DrawableRes int resource) {
        final LatLng startPosition = marker.getPosition();
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final Interpolator interpolator = new LinearInterpolator();
        final float durationInMs = 250;

        Drawable myDrawable = context.getResources().getDrawable(resource);
        final Bitmap bm = ((BitmapDrawable) myDrawable).getBitmap();



        handler.post(new Runnable() {
            long elapsed;
            float t;
            float v;

            @Override
            public void run() {
                // Calculate progress using interpolator
                elapsed = SystemClock.uptimeMillis() - start;
                t = elapsed / durationInMs;
                v = interpolator.getInterpolation(t);

                Bitmap bhalfsize;
                if (1.0f - v < 0.02f) {
                    bhalfsize=bm;
                } else {
                    bhalfsize=Bitmap.createScaledBitmap(bm, (int) (bm.getWidth()*v) + 1, (int) (bm.getHeight()*v) + 1, false);
                }
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(bhalfsize));

                // Repeat till progress is complete.
                if (t < 1) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                }
            }
        });
    }
}

