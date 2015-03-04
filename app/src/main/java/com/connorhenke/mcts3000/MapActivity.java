package com.connorhenke.mcts3000;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.connorhenke.mcts.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MapActivity extends Activity {

    private GoogleMap map;
    private String route;
    private List<String> directions;
    private List<Bus> vehicles;
    private List<Stop> firstSet;
    private List<Stop> secondSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);

        route = getIntent().getStringExtra("NUMBER");
        setTitle(route);
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        map.setOnMarkerClickListener(new OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                new PredictionsRequester(marker.getTitle(), route).execute();
                return true;
            }
        });

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(43.038940, -87.906448), 12.0f));
        vehicles = new ArrayList<Bus>();
        firstSet = new ArrayList<Stop>();
        secondSet = new ArrayList<Stop>();
        directions = new ArrayList<String>();

        setTitle("MCTS3000 - " + route);

        refresh();
    }

    private void refresh() {
        new VehiclesRequester(route).execute();
        new DirectionsRequester(route).execute();

    }

    private void displayBuses() {
        for (Bus bus : vehicles) {
            map.addMarker(new MarkerOptions()
                    .flat(true)
                    .position(bus.getLocation())
                    .rotation(bus.getHeading())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow))
                    .title(bus.getVid()));
        }
    }

    private void displayStops(List<Stop> stopList) {
        map.clear();
        displayBuses();
        for (Stop stop : stopList) {
            map.addMarker(new MarkerOptions()
                    .flat(true)
                    .position(new LatLng(Double.parseDouble(stop.getLatitude()), Double.parseDouble(stop.getLongitude())))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.circledot))
                    .title(stop.getStopId() != null ? stop.getStopId() : ""));
        }

    }

    class StopsRequester extends AsyncTask<String, String, JSONObject> {
        String route;
        String dir;
        List<Stop> list;

        public StopsRequester(String route, String dir, List<Stop> list) {
            this.route = route;
            this.dir = dir;
            this.list = list;
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject jsonStops = Constants.getStops(route, dir);
            Log.i(WIFI_SERVICE, "Request completed");
            return jsonStops;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            try {
                JSONArray stopList = result.getJSONObject("bustime-response").getJSONArray("stops");
                for (int i = 0; i < stopList.length(); i++) {
                    JSONObject stop = (JSONObject) stopList.get(i);
                    String stopId = stop.getString("stpid");
                    String stopName = stop.getString("stpnm");
                    String latitude = stop.getString("lat");
                    String longitude = stop.getString("lon");
                    Stop stopObj = new Stop("", "", latitude, longitude, stopId, stopName, "");
                    list.add(stopObj);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    class VehiclesRequester extends AsyncTask<String, String, JSONObject> {

        String route;
        Exception exception;

        public VehiclesRequester(String route) {
            this.route = route;
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject vehicles;
            try {
                vehicles = Constants.getVehicles(route);
            } catch (RouteException e) {
                exception = e;
                vehicles = null;
            }
            Log.i(WIFI_SERVICE, "Request completed");
            return vehicles;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            if (exception == null) {
                try {
                    JSONArray vehicleList = result.getJSONObject("bustime-response").getJSONArray("vehicle");
                    map.clear();
                    for (int i = 0; i < vehicleList.length(); i++) {
                        JSONObject vehicle = (JSONObject) vehicleList.get(i);
                        String vid = vehicle.getString("vid");
                        String time = vehicle.getString("tmstmp");
                        float latitude = Float.parseFloat(vehicle.getString("lat"));
                        float longitude = Float.parseFloat(vehicle.getString("lon"));
                        int heading = Integer.parseInt(vehicle.getString("hdg"));
                        vehicles.add(new Bus(new LatLng(latitude, longitude), heading, vid));
                        map.addMarker(new MarkerOptions()
                                .flat(true)
                                .position(new LatLng(latitude, longitude))
                                .rotation(heading)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow))
                                .title(vid));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                displayBuses();
            } else {
                Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                MapActivity.this.finish();
            }
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
            case R.id.show_stops:
                AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
                String[] options = Arrays.copyOf(directions.toArray(), directions.toArray().length, String[].class);
                builder.setTitle(R.string.show_stops)
                        .setItems(options, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        displayStops(firstSet);
                                        break;
                                    case 1:
                                        displayStops(secondSet);
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

    class DirectionsRequester extends AsyncTask<String, String, JSONObject> {

        String route;
        Exception exception;

        public DirectionsRequester(String route) {
            this.route = route;
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject directions;
            try {
                directions = Constants.getDirections(route);
            } catch (RouteException e) {
                exception = e;
                directions = null;
            }
            Log.i(WIFI_SERVICE, "Request completed");
            return directions;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            if (exception == null) {
                try {
                    JSONArray directionList = result.getJSONObject("bustime-response").getJSONArray("directions");
                    directions.clear();
                    for (int i = 0; i < directionList.length(); i++) {
                        JSONObject direction = (JSONObject) directionList.get(i);
                        String dir = direction.getString("dir");
                        directions.add(dir);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new StopsRequester(route, directions.get(0), firstSet).execute();
                new StopsRequester(route, directions.get(1), secondSet).execute();
            } else {
                Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                MapActivity.this.finish();
            }
        }

    }

    class PredictionsRequester extends AsyncTask<String, String, JSONObject> {

        String stop;
        String route;
        Exception exception;

        public PredictionsRequester(String stop, String route) {
            this.stop = stop;
            this.route = route;
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject prediction;
            prediction = Constants.getPredictions(stop, route);
            Log.i(WIFI_SERVICE, "Request completed");
            return prediction;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            if (exception == null) {
                try {
                    JSONObject response = result.getJSONObject("bustime-response");
                    JSONArray prd = response.getJSONArray("prd");
                    for (int i = 0; i < prd.length(); i++) {
                        JSONObject obj = prd.getJSONObject(i);
                        Prediction pred = new Prediction();
                        pred.loadJSON(obj);
                        Toast.makeText(getApplicationContext(), pred.getPrdctdn(), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                MapActivity.this.finish();
            }
        }

    }
}
