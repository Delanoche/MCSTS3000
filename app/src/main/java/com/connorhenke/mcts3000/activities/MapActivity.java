package com.connorhenke.mcts3000.activities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.connorhenke.mcts.R;
import com.connorhenke.mcts3000.loaders.DirectionsLoader;
import com.connorhenke.mcts3000.loaders.PredictionsLoader;
import com.connorhenke.mcts3000.loaders.StopsLoader;
import com.connorhenke.mcts3000.loaders.VehiclesLoader;
import com.connorhenke.mcts3000.models.Bus;
import com.connorhenke.mcts3000.models.Direction;
import com.connorhenke.mcts3000.models.Prediction;
import com.connorhenke.mcts3000.models.Stop;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MapActivity extends AppCompatActivity {

    private GoogleMap map;
    private String route;
    private List<String> directions;
    private List<Bus> vehicles;
    private List<Stop> stops;

    private static final int DIRECTIONS = 0;
    private DirectionsLoaderListener directionsLoaderListener = new DirectionsLoaderListener();
    private static final int PREDICTIONS = 1;
    private PredictionsLoaderListener predictionsLoaderListener = new PredictionsLoaderListener();
    private static final int STOPS = 2;
    private StopsLoaderListener stopsLoaderListener = new StopsLoaderListener();
    private static final int VEHICLES = 3;
    private VehiclesLoaderListener vehiclesLoaderListener = new VehiclesLoaderListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        route = getIntent().getStringExtra("NUMBER");
        setTitle(route);
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        map.setOnMarkerClickListener(new OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                getSupportLoaderManager().restartLoader(PREDICTIONS, PredictionsLoader.newBundle(route, marker.getTitle()), predictionsLoaderListener).forceLoad();
                return true;
            }
        });

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(43.038940, -87.906448), 12.0f));
        vehicles = new ArrayList<Bus>();
        stops = new ArrayList<Stop>();
        directions = new ArrayList<String>();

        setTitle("MCTS3000 - " + route);

        getSupportLoaderManager().initLoader(DIRECTIONS, DirectionsLoader.newBundle(route), directionsLoaderListener).forceLoad();
        getSupportLoaderManager().initLoader(VEHICLES, VehiclesLoader.newBundle(route), vehiclesLoaderListener).forceLoad();
    }

    private void refresh() {
        getSupportLoaderManager().restartLoader(DIRECTIONS, DirectionsLoader.newBundle(route), directionsLoaderListener).forceLoad();
        getSupportLoaderManager().restartLoader(VEHICLES, VehiclesLoader.newBundle(route), vehiclesLoaderListener).forceLoad();
    }

    private void displayBuses() {
        for (Bus bus : vehicles) {
            map.addMarker(new MarkerOptions()
                    .flat(true)
                    .position(bus.getLocation())
                    .rotation(bus.getHeading())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow))
                    .title("" + bus.getVid()));
        }
    }

    private void displayStops(List<Stop> stopList) {
        map.clear();
        displayBuses();
        for (Stop stop : stopList) {
            map.addMarker(new MarkerOptions()
                    .flat(true)
                    .position(new LatLng(stop.getLatitude(), stop.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.circledot))
                    .title(stop.getStopId() != null ? stop.getStopId() : ""));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                refresh();
                return true;
            case R.id.show_stops:
                AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
                String[] options = Arrays.copyOf(directions.toArray(), directions.toArray().length, String[].class);
                builder.setTitle(R.string.show_stops)
                        .setItems(options, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        getSupportLoaderManager().restartLoader(STOPS, StopsLoader.newBundle(route, directions.get(0)), stopsLoaderListener).forceLoad();
//                                        displayStops(firstSet);
                                        break;
                                    case 1:
                                        getSupportLoaderManager().restartLoader(STOPS, StopsLoader.newBundle(route, directions.get(1)), stopsLoaderListener).forceLoad();
//                                        displayStops(secondSet);
                                        break;
                                }
                                dialog.dismiss();
                            }
                        });
                Dialog dialog = builder.create();
                dialog.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class StopsLoaderListener implements LoaderManager.LoaderCallbacks<List<Stop>> {

        @Override
        public Loader<List<Stop>> onCreateLoader(int id, Bundle args) {
            return new StopsLoader(MapActivity.this, args);
        }

        @Override
        public void onLoadFinished(Loader<List<Stop>> loader, List<Stop> data) {
            displayStops(data);
        }

        @Override
        public void onLoaderReset(Loader<List<Stop>> loader) {

        }
    }

    private class VehiclesLoaderListener implements LoaderManager.LoaderCallbacks<List<Bus>> {

        @Override
        public Loader<List<Bus>> onCreateLoader(int id, Bundle args) {
            return new VehiclesLoader(MapActivity.this, args);
        }

        @Override
        public void onLoadFinished(Loader<List<Bus>> loader, List<Bus> data) {
            try {
                map.clear();
                vehicles.clear();
                vehicles.addAll(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
            displayBuses();
        }

        @Override
        public void onLoaderReset(Loader<List<Bus>> loader) {

        }
    }

    private class DirectionsLoaderListener implements LoaderManager.LoaderCallbacks<List<Direction>> {

        @Override
        public Loader<List<Direction>> onCreateLoader(int id, Bundle args) {
            return new DirectionsLoader(MapActivity.this, args);
        }

        @Override
        public void onLoadFinished(Loader<List<Direction>> loader, List<Direction> data) {
            directions.clear();
            for(Direction direction : data) {
                directions.add(direction.getDir());
            }
        }

        @Override
        public void onLoaderReset(Loader<List<Direction>> loader) {

        }
    }

    private class PredictionsLoaderListener implements LoaderManager.LoaderCallbacks<List<Prediction>> {

        @Override
        public Loader<List<Prediction>> onCreateLoader(int id, Bundle args) {
            return new PredictionsLoader(MapActivity.this, args);
        }

        @Override
        public void onLoadFinished(Loader<List<Prediction>> loader, List<Prediction> data) {
            for(Prediction prediction : data) {
                Toast.makeText(getApplicationContext(), prediction.getPrdctdn(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onLoaderReset(Loader<List<Prediction>> loader) {

        }
    }
}
