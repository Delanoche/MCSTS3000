package com.connorhenke.mcts;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class MapActivity extends Activity {
	
	private GoogleMap map;
	private String route;
	private List<Bus> vehicles;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);

		route = getIntent().getStringExtra("NUMBER");
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		new VehiclesRequester(route).execute();
		
		map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(43.038940, -87.906448), 12.0f));
		vehicles = new ArrayList<Bus>();
		
	}

	class VehiclesRequester extends AsyncTask<String, String, Document> {
		
		String route;
		
		public VehiclesRequester(String route) {
			this.route = route;
		}

		@Override
		protected Document doInBackground(String... params) {
			Document vehicles = Constants.getVehicles(route);
			Log.i(WIFI_SERVICE, "Request completed");
			return vehicles;
		}
		
		@Override
	    protected void onPostExecute(Document result) {
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
		}
		
	}
}
