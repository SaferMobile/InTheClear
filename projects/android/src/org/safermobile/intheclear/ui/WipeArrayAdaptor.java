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
import android.widget.LinearLayout;
import android.widget.TextView;

public class WipeArrayAdaptor extends BaseAdapter {
	private ArrayList<WipeSelector> _wipeSelector;
	private LayoutInflater li;
	
	public ArrayList<Map<Integer,Integer>> associatedViews = new ArrayList<Map<Integer,Integer>>();
		
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
	public View getView(final int position, View convertView, final ViewGroup parent) {
		convertView = li.inflate(R.layout.wipe_select, null);
		CheckBox cb = (CheckBox) convertView.findViewById(R.id.wipeCheckBox);
		TextView tv = (TextView) convertView.findViewById(R.id.wipeText);
		tv.setTextColor(_wipeSelector.get(position)._color);
		
		cb.setChecked(_wipeSelector.get(position)._wipeSelect);
		cb.setEnabled(_wipeSelector.get(position).isToggleControl());

		cb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CheckBox cb = (CheckBox) v;
				WipeSelector w = (WipeSelector) getItem(position);
				w.setSelected(cb.isChecked());
				if(w.isToggleControl()) {
					int count = 0;
					for(WipeSelector ws : _wipeSelector) {
						CheckBox checkBox = (CheckBox) ((LinearLayout) parent.getChildAt(count)).getChildAt(0);
						TextView textView = (TextView) ((LinearLayout) parent.getChildAt(count)).getChildAt(1);
						
						int newColor;
						boolean newCheckedStatus, newLockedStatus;
						
						if(ws.getColor() == Color.SELECTABLE) {
							newColor = Color.UNSELECTABLE;
							newCheckedStatus = false;
							if(!ws.isToggleControl())
								newLockedStatus = false;
							else
								newLockedStatus = true;
						} else {
							newColor = Color.SELECTABLE;
							newCheckedStatus = ws.getSelected();
							newLockedStatus = true;
						}
						
						ws.setColor(newColor);
						ws.setSelected(newCheckedStatus);
						checkBox.setChecked(newCheckedStatus);
						checkBox.setEnabled(newLockedStatus);
						textView.setTextColor(newColor);
						count++;
					}
					
				}
			}
			
		});
		
		tv.setText(_wipeSelector.get(position)._wipeTarget);
		
		return convertView;
	}

}
