package com.connorhenke.mcts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
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
	private List<Stop> stops;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		
		route = getIntent().getStringExtra("NUMBER");
		setTitle(route);
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		new VehiclesRequester(route).execute();

		map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(43.038940, -87.906448), 12.0f));
		vehicles = new ArrayList<Bus>();
		stops = new ArrayList<Stop>();
		
		setTitle("MCTS3000 - " + route);
		
		new StopsRequester(route).execute();

	}

	class StopsRequester extends AsyncTask<String, String, JSONObject> {
		String route;

		public StopsRequester(String route) {
			this.route = route;
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			JSONObject stops = Constants.getStops(route, "SOUTH");
			Log.i(WIFI_SERVICE, "Request completed");
			return stops;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			try {
				JSONArray stopList = result.getJSONObject("bustime-response").getJSONArray("stops");
				for(int i = 0; i < stopList.length(); i++) {
					JSONObject stop = (JSONObject)stopList.get(i);				
					String stopId = stop.getString("stpid");
					String stopName = stop.getString("stpnm");
					String latitude = stop.getString("lat");
					String longitude = stop.getString("lon");
					Stop stopObj = new Stop("", "", latitude, longitude, stopId, stopName, "");
					stops.add(stopObj);
					map.addMarker(new MarkerOptions()
					.flat(true)
					.position(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude)))
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.circledot))
					.title(stopId != null ? stopId : ""));
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
			
		}

	}

	class VehiclesRequester extends AsyncTask<String, String, Document> {

		String route;
		Exception exception;

		public VehiclesRequester(String route) {
			this.route = route;
		}

		@Override
		protected Document doInBackground(String... params) {
			Document vehicles;
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
		protected void onPostExecute(Document result) {
			if(exception == null) {
				Element response = (Element) result.getElementsByTagName("bustime-response").item(0);
				NodeList vehicleList = response.getChildNodes();
				map.clear();
				for(int i = 0; i < vehicleList.getLength(); i++) {
					Element vehicle = (Element) vehicleList.item(i);
					Node vid = vehicle.getFirstChild();
					Node time = vid.getNextSibling();
					Node lat = time.getNextSibling();
					float latitude = Float.parseFloat(lat.getTextContent());
					Node lon = lat.getNextSibling();
					float longitude = Float.parseFloat(lon.getTextContent());
					Node hdg = lon.getNextSibling();
					int heading = Integer.parseInt(hdg.getTextContent());
					vehicles.add(new Bus(new LatLng(latitude, longitude), heading, vid.getTextContent()));
					map.addMarker(new MarkerOptions()
					.flat(true)
					.position(new LatLng(latitude, longitude))
					.rotation(heading)
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow))
					.title(vid.getTextContent()));
				}
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
//	                    	   dialog.dismiss();
	                       }
	            });
	            Dialog dialog = builder.create();
	            dialog.show();

	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	class DirectionsRequester extends AsyncTask<String, String, Document> {

		String route;
		Exception exception;

		public DirectionsRequester(String route) {
			this.route = route;
		}

		@Override
		protected Document doInBackground(String... params) {
			Document directions;
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
		protected void onPostExecute(Document result) {
			if(exception == null) {
				Element response = (Element) result.getElementsByTagName("bustime-response").item(0);
				NodeList directionList = response.getChildNodes();
				directions.clear();
				for(int i = 0; i < directionList.getLength(); i++) {
					Element direction = (Element) directionList.item(i);
					String dir = direction.getTextContent();
					Log.d("DIR", dir);
					directions.add(dir);
				}
			} else {
				Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
				MapActivity.this.finish();
			}
		}

	}
}
