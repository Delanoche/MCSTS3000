package com.connorhenke.mcts3000;

import android.util.Log;

import com.connorhenke.mcts3000.models.Bus;
import com.connorhenke.mcts3000.models.Direction;
import com.connorhenke.mcts3000.models.Prediction;
import com.connorhenke.mcts3000.models.Route;
import com.connorhenke.mcts3000.models.Stop;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Single;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class BaseService {

    private static final OkHttpClient client = new OkHttpClient();

    public static final String API_KEY = "wHRcbttmWX6FFh9t25u8Ea6K9";
    public static final String API = "http://realtime.ridemcts.com/bustime/api/v2/";
    public static final String GOOGLE_DIRECTIONS = "maps.google.com/maps/api/directions/json";

    public static final String ROUTES_ENDPOINT = "getroutes";
    public static final String ROUTES_OBJECT = "routes";

    public static final String VEHICLES_ENDPOINT = "getvehicles";
    public static final String VEHICLES_OBJECT = "vehicle";

    public static final String STOPS_ENDPOINT = "getstops";
    public static final String STOPS_OBJECT = "stops";

    public static final String DIRECTIONS_ENDPOINT = "getdirections";
    public static final String DIRECTIONS_OBJECT = "directions";

    public static final String PREDICTIONS_ENDPOINT = "getpredictions";
    public static final String PREDICTIONS_OBJECT = "prd";


    public static final String BUSTIME_RESPONSE = "bustime-response";

    public static Response request(String endpoint, String params) throws IOException {
        Request request = new Request.Builder()
                .get()
                .url(API + endpoint + "?key=" + API_KEY + "&format=json" + params)
                .build();
        return request(request);
    }

    public static Response request(Request request) throws IOException {
        Log.d("Endpoint", request.urlString());
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            Log.d("Error", "Response Code: " + response.code());
            throw new IOException("Unexpected code " + response.body());
        }
        return response;
    }

    public static Single<List<Route>> getRoutes() {
        return Single.create(new Single.OnSubscribe<List<Route>>() {
            @Override
            public void call(SingleSubscriber<? super List<Route>> singleSubscriber) {
                try {
                    Response response = request(ROUTES_ENDPOINT, "");
                    JSONObject object = new JSONObject(response.body().string());
                    JSONArray routes = object.getJSONObject(BUSTIME_RESPONSE).getJSONArray(ROUTES_OBJECT);
                    List<Route> result = new ArrayList<>();
                    for (int i = 0; i < routes.length(); i++) {
                        JSONObject route = routes.getJSONObject(i);
                        result.add(new Route(route.getString(Route.NUMBER), route.getString(Route.NAME), route.getString(Route.COLOR)));
                    }
                    singleSubscriber.onSuccess(result);
                } catch (IOException | JSONException e) {
                    singleSubscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    public static List<Bus> getVehicles(String route) throws IOException, JSONException {
        Response response = request(VEHICLES_ENDPOINT, "&rt=" + route);
        JSONObject object = new JSONObject(response.body().string());
        JSONArray vehicles = object.getJSONObject(BUSTIME_RESPONSE).getJSONArray(VEHICLES_OBJECT);
        List<Bus> result = new ArrayList<>();
        for(int i = 0; i < vehicles.length(); i++) {
            JSONObject vehicle = vehicles.getJSONObject(i);
            double lat = Double.parseDouble(vehicle.getString(Bus.LAT));
            double lon = Double.parseDouble(vehicle.getString(Bus.LON));
            int heading = Integer.parseInt(vehicle.getString(Bus.HEADING));

            result.add(new Bus(new LatLng(lat, lon), heading, vehicle.getInt(Bus.VID)));
        }
        return result;
    }

    public static List<Stop> getStops(String route, String dir) throws IOException, JSONException {
        Response response = request(STOPS_ENDPOINT, "&rt=" + route + "&dir=" + dir);
        JSONObject object = new JSONObject(response.body().string());
        JSONArray stops = object.getJSONObject(BUSTIME_RESPONSE).getJSONArray(STOPS_OBJECT);
        List<Stop> result = new ArrayList<>();
        for(int i = 0; i < stops.length(); i++) {
            JSONObject stop = stops.getJSONObject(i);
            result.add(new Stop(stop.getDouble(Stop.LAT), stop.getDouble(Stop.LON), stop.getString(Stop.ID), stop.getString(Stop.NAME)));
        }
        return result;
    }

    public static List<Direction> getDirections(String route) throws IOException, JSONException {
        Response response = request(DIRECTIONS_ENDPOINT, "&rt=" + route);
        JSONObject object = new JSONObject(response.body().string());
        JSONArray directions = object.getJSONObject(BUSTIME_RESPONSE).getJSONArray(DIRECTIONS_OBJECT);
        List<Direction> result = new ArrayList<>();
        for(int i = 0; i < directions.length(); i++) {
            JSONObject direction = directions.getJSONObject(i);
            result.add(new Direction(direction.getString(Direction.DIRECTION)));
        }
        return result;
    }

    public static List<Prediction> getPredictions(String route, String stop) throws IOException, JSONException {
        Response response = request(PREDICTIONS_ENDPOINT, "&rt=" + route + "&stpid=" + stop);
        JSONObject object = new JSONObject(response.body().string());
        JSONArray predictions = object.getJSONObject(BUSTIME_RESPONSE).getJSONArray(PREDICTIONS_OBJECT);
        List<Prediction> result = new ArrayList<>();
        for(int i = 0; i < predictions.length(); i++) {
            JSONObject prediction = predictions.getJSONObject(i);
            Prediction pred = new Prediction();
            pred.loadJSON(prediction);
            result.add(pred);
        }
        return result;
    }

}

