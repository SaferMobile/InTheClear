package org.safermobile.intheclear.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.safermobile.intheclear.ITCConstants;
import org.safermobile.intheclear.R;
import org.safermobile.intheclear.ui.WipeSelector.Color;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class WipeArrayAdaptor extends BaseAdapter {
	private ArrayList<WipeSelector> _wipeSelector;
	private LayoutInflater li;
	
	public ArrayList<Map<Integer,Integer>> associatedViews = new ArrayList<Map<Integer,Integer>>();
		
	public WipeArrayAdaptor(Context c, ArrayList<WipeSelector> wipeSelector) {
		li = LayoutInflater.from(c);
		_wipeSelector = wipeSelector;
		
	}
	
	private void redrawItems() {
		for(Map<Integer,Integer> m : associatedViews) {
			Map.Entry pair = (Map.Entry) m;
			// TODO: set the colors appropriately
		}
	}
	
	@Override
	public int getCount() {
		return _wipeSelector.size();
	}

	@Override
	public Object getItem(int position) {
		return _wipeSelector.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		convertView = li.inflate(R.layout.wipe_select, null);
		CheckBox cb = (CheckBox) convertView.findViewById(R.id.wipeCheckBox);
		TextView tv = (TextView) convertView.findViewById(R.id.wipeText);
		tv.setTextColor(_wipeSelector.get(position)._color);
		
		cb.setChecked(_wipeSelector.get(position)._wipeSelect);
		cb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CheckBox cb = (CheckBox) v;
				WipeSelector w = (WipeSelector) getItem(position);
				w.setSelected(cb.isChecked());
				if(w.isToggleControl()) {
					for(WipeSelector ws : _wipeSelector) {
						if(ws.getColor() == Color.SELECTABLE) {
							ws.setColor(Color.UNSELECTABLE);
						} else {
							ws.setColor(Color.SELECTABLE);
						}
					}
					redrawItems();
					
				}
			}
			
		});
		
		tv.setText(_wipeSelector.get(position)._wipeTarget);
		
		Map<Integer,Integer> m = new HashMap<Integer,Integer>();
		m.put(tv.getId(), _wipeSelector.get(position)._color);
		associatedViews.add(m);
		
		return convertView;
	}

}
