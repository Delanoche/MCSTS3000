package com.connorhenke.mcts3000.activities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.connorhenke.mcts.R;
import com.connorhenke.mcts3000.loaders.DirectionsLoader;
import com.connorhenke.mcts3000.loaders.PredictionsLoader;
import com.connorhenke.mcts3000.loaders.StopsLoader;
import com.connorhenke.mcts3000.loaders.VehiclesLoader;
import com.connorhenke.mcts3000.models.Bus;
import com.connorhenke.mcts3000.models.Direction;
import com.connorhenke.mcts3000.models.Favorite;
import com.connorhenke.mcts3000.models.Prediction;
import com.connorhenke.mcts3000.models.Stop;
import com.connorhenke.mcts3000.persistence.SQLiteOpenHelperImpl;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.Explode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

public class MapActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap map;
    private String route;
    private List<String> directions;
    private String currentDirection;
    private List<Bus> vehicles;
    private HashMap<String, Stop> stops;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Marker person;

    private static final int DIRECTIONS = 0;
    private DirectionsLoaderListener directionsLoaderListener = new DirectionsLoaderListener();
    private static final int PREDICTIONS = 1;
    private static final int STOPS = 2;
    private StopsLoaderListener stopsLoaderListener = new StopsLoaderListener();
    private static final int VEHICLES = 3;
    private VehiclesLoaderListener vehiclesLoaderListener = new VehiclesLoaderListener();

    private static final String ARG_NUMBER = "number";
    private static final String ARG_NAME = "name";

    public static Intent newIntent(Context context, String number, String name) {
        Intent intent = new Intent(context, MapActivity.class);
        intent.putExtra(ARG_NUMBER, number);
        intent.putExtra(ARG_NAME, name);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        locationRequest = createLocationRequest();
        buildGoogleApiClient();
        googleApiClient.connect();

        route = getIntent().getStringExtra(ARG_NUMBER);
        setTitle(route);
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        map.setOnMarkerClickListener(new OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Stop stop = stops.get(marker.getTitle());
                if (stop != null) {
                    startActivity(FavoriteActivity.newIntent(MapActivity.this, new Favorite(stop.getStopId(), stop.getStopName(), route, currentDirection)));
                    return true;
                }
                return false;
            }
        });

        vehicles = new ArrayList<>();
        stops = new HashMap<>();
        directions = new ArrayList<>();

        setTitle(route);

        getSupportLoaderManager().initLoader(DIRECTIONS, DirectionsLoader.newBundle(route), directionsLoaderListener).forceLoad();
        getSupportLoaderManager().initLoader(VEHICLES, VehiclesLoader.newBundle(route), vehiclesLoaderListener).forceLoad();
    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void refresh() {
        getSupportLoaderManager().restartLoader(DIRECTIONS, DirectionsLoader.newBundle(route), directionsLoaderListener).forceLoad();
        getSupportLoaderManager().restartLoader(VEHICLES, VehiclesLoader.newBundle(route), vehiclesLoaderListener).forceLoad();
    }

    private void displayBuses() {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(43.038940, -87.906448), 12.0f));
        for (Bus bus : vehicles) {
            map.addMarker(new MarkerOptions()
                    .flat(true)
                    .position(bus.getLocation())
                    .rotation(bus.getHeading())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow))
                    .title(""));
        }
        MarkerOptions markerOptions = new MarkerOptions();
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            markerOptions.position(new LatLng(location.getLatitude(), location.getLongitude()));
            person = map.addMarker(markerOptions);
        }
    }

    private void displayStops(List<Stop> stopList) {
        map.clear();
        displayBuses();
        for (Stop stop : stopList) {
            stops.put(stop.getStopId(), stop);
            map.addMarker(new MarkerOptions()
                    .flat(true)
                    .position(new LatLng(stop.getLatitude(), stop.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.circledot))
                    .title(stop.getStopId()));
        }
    }

    private void findClosestStop() {
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null && stops.size() > 0) {
            Stop closest = null;
            double closestDistance = Double.POSITIVE_INFINITY;
            for(Stop stop : stops.values()) {
                double temp = calculateDistance(location.getLatitude(), location.getLongitude(), stop.getLatitude(), stop.getLongitude());
                if (closest == null || temp < closestDistance) {
                    closest = stop;
                    closestDistance = temp;
                }
            }
            startActivity(FavoriteActivity.newIntent(MapActivity.this, new Favorite(closest.getStopId(), closest.getStopName(), route, currentDirection)));
        } else if (stops.size() == 0) {
            Toast.makeText(this, "Please select a direction", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Please turn on location services", Toast.LENGTH_SHORT).show();
        }
    }

    private double calculateDistance(double latitude, double longitude, double otherLatitude, double otherLongitude) {
        double deltaLongitude = otherLongitude - longitude;
        double deltaLatitude = otherLatitude - latitude;
        double a = Math.pow(Math.sin(deltaLatitude/2), 2) + Math.cos(latitude) * Math.cos(otherLatitude) * Math.pow(Math.sin(deltaLongitude), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        // 3691 is the radius of Earth in miles
        return 3961 * c;
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
                                        currentDirection = directions.get(0);
                                        break;
                                    case 1:
                                        currentDirection = directions.get(1);
                                        break;
                                }
                                getSupportLoaderManager().restartLoader(STOPS, StopsLoader.newBundle(route, currentDirection), stopsLoaderListener).forceLoad();
                                dialog.dismiss();
                            }
                        });
                Dialog dialog = builder.create();
                dialog.show();

                return true;
            case R.id.get_closest:
                findClosestStop();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }

    protected LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (googleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            if (person == null) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(new LatLng(location.getLatitude(), location.getLongitude()));
                person = map.addMarker(markerOptions);
            } else {
                person.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
            }
        }
    }

    private class StopsLoaderListener implements LoaderManager.LoaderCallbacks<List<Stop>> {

        @Override
        public Loader<List<Stop>> onCreateLoader(int id, Bundle args) {
            return new StopsLoader(MapActivity.this, args);
        }

        @Override
        public void onLoadFinished(Loader<List<Stop>> loader, List<Stop> data) {
            if (data == null) {
                Toast.makeText(MapActivity.this, "Could not load stops. Check network connection", Toast.LENGTH_LONG).show();
            } else {
                displayStops(data);
            }
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
                map.clear();
                vehicles.clear();
                if (data == null) {
                    Toast.makeText(MapActivity.this, "Could not load locations. Check network connection", Toast.LENGTH_LONG).show();
                } else {
                    if (data.size() > 0) {
                        vehicles.addAll(data);
                    } else {
                        Toast.makeText(MapActivity.this, "No buses are running for this route", Toast.LENGTH_LONG).show();
                    }
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
            if (data != null) {
                for (Direction direction : data) {
                    directions.add(direction.getDir());
                }
            }
        }

        @Override
        public void onLoaderReset(Loader<List<Direction>> loader) {

        }
    }
}
