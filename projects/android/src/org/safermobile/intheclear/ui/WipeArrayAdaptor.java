package org.safermobile.intheclear.ui;

import java.util.ArrayList;

import org.safermobile.intheclear.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class WipeArrayAdaptor extends BaseAdapter {
	private ArrayList<WipeSelector> _wipeSelector;
	private LayoutInflater li;
	
	public WipeArrayAdaptor(Context c, ArrayList<WipeSelector> wipeSelector) {
		li = LayoutInflater.from(c);
		_wipeSelector = wipeSelector;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = li.inflate(R.layout.wipe_select, null);
		CheckBox cb = (CheckBox) convertView.findViewById(R.id.wipeCheckBox);
		TextView tv = (TextView) convertView.findViewById(R.id.wipeText);
		
		cb.setChecked(_wipeSelector.get(position)._wipeSelect);
		tv.setText(_wipeSelector.get(position)._wipeTarget);
		return convertView;
	}

}
