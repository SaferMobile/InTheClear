package org.safermobile.intheclear.screens;

import java.util.ArrayList;

import org.safermobile.intheclear.R;
import org.safermobile.intheclear.ui.*;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;


public class WipeSelectionForm implements ListAdapter {
	public ArrayList<FolderSelector> folders;
	public ArrayList<WipeSelector> wipeTargets;
	
	public WipeSelectionForm(boolean shouldWipeContacts, boolean shouldWipePhotos) {
		folders = new ArrayList<FolderSelector>();
		wipeTargets = new ArrayList<WipeSelector>();
		
		//wipeTargets.add(new WipeSelector(R.string.KEY_WIPE_WIPECONTACTS, 1, false));
	}

	@Override
	public int getCount() {
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public int getItemViewType(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		return null;
	}

	@Override
	public int getViewTypeCount() {
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		return false;
	}

}
