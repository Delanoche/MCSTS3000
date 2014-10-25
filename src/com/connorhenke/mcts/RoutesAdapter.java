package com.connorhenke.mcts;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.inputmethodservice.Keyboard.Row;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

public class RoutesAdapter extends ArrayAdapter<Route> {
	
	Context context;
	int layoutResourceId;
	List<Route> routes = null;
	
	public RoutesAdapter(Context context, int layoutResourceId, List<Route> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.routes = data;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		
		if(view == null) {
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			view = inflater.inflate(layoutResourceId, parent, false);
		}
		TextView number = (TextView) view.findViewById(R.id.number);
		TextView name = (TextView) view.findViewById(R.id.name);
		
		number.setText(routes.get(position).getNumber());
		name.setText(routes.get(position).getName());
		
		TextView color = (TextView) view.findViewById(R.id.color_block);
		int bg = Color.parseColor(routes.get(position).getColor());
		Log.d("COLOR", "" + bg);
		color.setBackgroundColor(bg);
		
		return view;
	}
	
	private static int parseColor(String color) {
		return Color.rgb(Integer.valueOf(color.substring(1,3),16).intValue(), Integer.valueOf(color.substring(3,5),16).intValue(), Integer.valueOf(color.substring(5,7),16).intValue());
	}
}
