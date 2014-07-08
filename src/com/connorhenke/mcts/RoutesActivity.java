package com.connorhenke.mcts;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class RoutesActivity extends Activity {
	
	protected List<Route> routes = new ArrayList<Route>();
	protected RoutesAdapter adapter;
	ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.routes);
		
    	listView = (ListView) this.findViewById(android.R.id.list);
    	listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				TextView number = (TextView) view.findViewById(R.id.number);
				TextView name = (TextView) view.findViewById(R.id.name);
				Intent intent = new Intent(getApplicationContext(), MapActivity.class);
				intent.putExtra("NUMBER", number.getText());
				intent.putExtra("NAME", name.getText());
	        	startActivity(intent);
				overridePendingTransition(R.anim.lefttoright, R.anim.righttoleft);
				
			}
    	});
		
    	if(adapter == null) {
    		adapter = new RoutesAdapter(this, R.layout.route_item, routes);
    	}
    	listView.setAdapter(adapter);

		new RoutesRequester().execute();
	}
	
	class RoutesRequester extends AsyncTask<String, String, Document> {

		@Override
		protected Document doInBackground(String... params) {
			Document routes = Constants.getRoutes();
			Log.i(WIFI_SERVICE, "Request completed");
			return routes;
		}
		
		@Override
	    protected void onPostExecute(Document result) {
			Element response = (Element) result.getElementsByTagName("bustime-response").item(0);
			NodeList routeList = response.getChildNodes();
			routes.clear();
			for(int i = 0; i < routeList.getLength(); i++) {
				Element route = (Element) routeList.item(i);
				Node rt = route.getFirstChild();
				Node rtnm = rt.getNextSibling();
				Node rtclr = rtnm.getNextSibling();
				routes.add(new Route(rt.getTextContent(), rtnm.getTextContent(), rtclr.getTextContent()));
			}
			adapter.notifyDataSetChanged();
		}
		
	}
}
