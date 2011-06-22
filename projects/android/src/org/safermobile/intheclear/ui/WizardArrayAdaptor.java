package org.safermobile.intheclear.ui;

import java.util.ArrayList;

import org.safermobile.intheclear.R;
import org.safermobile.intheclear.Wizard;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WizardArrayAdaptor extends BaseAdapter {
	private LayoutInflater li;
	private ArrayList<WizardSelector> _wizardSelector;
	public Context _c;
	
	public WizardArrayAdaptor(Context c, ArrayList<WizardSelector> wizardSelector) {
		_c = c;
		li = LayoutInflater.from(_c);
		_wizardSelector = wizardSelector;
	}

	@Override
	public int getCount() {
		return _wizardSelector.size();
	}

	@Override
	public Object getItem(int position) {
		return _wizardSelector.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		convertView = li.inflate(R.layout.wizard_select, null);
		
		TextView listItemNum = (TextView) convertView.findViewById(R.id.listItemNum);
		listItemNum.setText(Integer.toString(_wizardSelector.get(position)._listItemNum));
		
		TextView listItemText = (TextView) convertView.findViewById(R.id.listItemText);
		listItemText.setText(_wizardSelector.get(position)._listItemText);
		listItemText.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent me) {
				Intent i = new Intent(_c,Wizard.class);
				i.putExtra("wNum",position + 3);
				_c.startActivity(i);
				return false;
			}
			
		});
		
		ImageView listItemIcon = (ImageView) convertView.findViewById(R.id.listItemIcon);		
		return convertView;
	}

}
