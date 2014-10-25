package com.connorhenke.mcts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import android.util.Log;

public class Constants {
	
	public static final String API_KEY = "wHRcbttmWX6FFh9t25u8Ea6K9";
	public static final String API = "http://realtime.ridemcts.com/bustime/api/v2/";
	public static final String GOOGLE_DIRECTIONS = "maps.google.com/maps/api/directions/json";
	public static final String ROUTES = "getroutes";
	public static final String VEHICLES = "getvehicles";
	public static final String STOPS = "getstops";
	
	public static Document getVehicles(String route) {
		String params = "?key=" + API_KEY + "&rt=" + route;
		HttpResponse response = httpGet(API + VEHICLES + params);
		Document doc = null;
		if(response.getStatusLine().getStatusCode() == 200) {
			String body = "";
			try {
				BufferedReader content = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				int val;
				while((val = content.read()) !=  -1) {
					char character = (char)val;
					if(!Character.isWhitespace(character))
						body += ((char)val);
				}
				doc = loadXml(body);
			} catch(IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return doc;
	}
	
	public static JSONObject getStops(String route, String dir) {
		String params = "?key=" + API_KEY + "&rt=" + route + "&dir=" + dir + "&format=json";
		Log.d("request", API + STOPS + params);
		HttpResponse response = httpGet(API + STOPS + params);
		JSONObject j = null;
		if(response.getStatusLine().getStatusCode() == 200) {
			String body = "";
			try {
				BufferedReader content = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				int val;
				while((val = content.read()) !=  -1) {
					char character = (char)val;
					if(!Character.isWhitespace(character))
						body += ((char)val);
				}
				Log.d("response", body);
				j = new JSONObject(body);
			} catch(IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return j;
	}
	
	public static Document getRoutes() {
		String params = "?key=" + API_KEY;
		HttpResponse response = httpGet(API + ROUTES + params);
		Document doc = null;
		if(response.getStatusLine().getStatusCode() == 200) {
			String body = "";
			try {
				BufferedReader content = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				int val;
				while((val = content.read()) !=  -1) {
					char character = (char)val;
					if(!Character.isWhitespace(character))
						body += ((char)val);
				}
				doc = loadXml(body);
			} catch(IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return doc;
	}
	
	public static Document loadXml(String xml) throws Exception {
	   DocumentBuilderFactory fctr = DocumentBuilderFactory.newInstance();
	   DocumentBuilder bldr = fctr.newDocumentBuilder();
	   InputSource src = new InputSource(new StringReader(xml));
	   return bldr.parse(src);
	}
	
	public static HttpResponse httpGet(String url) {
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		HttpResponse response;
		try {
			response = client.execute(get);
		} catch (ClientProtocolException e) {
			response = null;
			e.printStackTrace();
		} catch (IOException e) {
			response = null;
			e.printStackTrace();
		}
		return response;
	}
	
}
