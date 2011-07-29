package org.safermobile.intheclear.ui;

import java.util.ArrayList;

import org.safermobile.intheclear.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WipeDisplayAdaptor extends BaseAdapter {
	private ArrayList<WipeDisplay> _wipeDisplay;
	LayoutInflater li;
	
	public WipeDisplayAdaptor(Context c, ArrayList<WipeDisplay> wipeDisplay) {
		_wipeDisplay = wipeDisplay;
		li = LayoutInflater.from(c);
	}
	
	@Override
	public int getCount() {
		return _wipeDisplay.size();
	}

	@Override
	public Object getItem(int i) {
		return null;
	}

	@Override
	public long getItemId(int i) {
		return 0;
	}

	@Override
	public View getView(final int position, View compositeView, ViewGroup parent) {
		compositeView = li.inflate(R.layout.wipe_display, null);
		ImageView selectionIcon = (ImageView) compositeView.findViewById(R.id.selectionIcon);
		TextView selectionText = (TextView) compositeView.findViewById(R.id.selectionText);
		
		selectionIcon.setBackgroundDrawable(_wipeDisplay.get(position)._displayIcon);
		selectionText.setText(_wipeDisplay.get(position)._displayText);
		
		return compositeView;
	}

}
