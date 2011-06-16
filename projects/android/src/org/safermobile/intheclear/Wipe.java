package org.safermobile.intheclear;

import java.util.ArrayList;

import org.safermobile.intheclear.controllers.WipeController;
import org.safermobile.intheclear.ui.FolderIterator;
import org.safermobile.intheclear.ui.WipeArrayAdaptor;
import org.safermobile.intheclear.ui.WipeSelector;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class Wipe extends Activity implements OnClickListener {
	private SharedPreferences _sp;
	
	Button wipeButton;
	ListView checkBoxHolder_group1,checkBoxHolder_group2;
	ArrayList<WipeSelector> wipeSelector;
	ArrayList<WipeSelector> folderSelector;
	
	final int CONTACTS = 1;
	final int PHOTOS = 2;
	final int CALLLOG = 3;
	final int SMS = 4;
	final int FOLDER = 5;
	
	WipeController wc;
	
	private final static String ITC = "[InTheClear:Wipe] ************************ ";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wipe);
		
		wipeButton = (Button) findViewById(R.id.wipeButton);
		
		_sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		boolean shouldWipeContacts = _sp.getBoolean("DefaultWipeContacts",false);
		boolean shouldWipePhotos = _sp.getBoolean("DefaultWipePhotos", false);
		boolean shouldWipeCallLog = _sp.getBoolean("DefaultWipeCallLog", false);
		boolean shouldWipeSMS = _sp.getBoolean("DefaultWipeSMS", false);
		
		wipeSelector = new ArrayList<WipeSelector>();
		
		wipeSelector.add(new WipeSelector(getString(R.string.KEY_WIPE_WIPECONTACTS), CONTACTS, shouldWipeContacts));
		wipeSelector.add(new WipeSelector(getString(R.string.KEY_WIPE_WIPEPHOTOS), PHOTOS, shouldWipePhotos));
		wipeSelector.add(new WipeSelector(getString(R.string.KEY_WIPE_CALLLOG),CALLLOG,shouldWipeCallLog));
		wipeSelector.add(new WipeSelector(getString(R.string.KEY_WIPE_SMS),SMS,shouldWipeSMS));

		checkBoxHolder_group1 = (ListView) findViewById(R.id.checkBoxHolder_group1);
		checkBoxHolder_group1.setAdapter(new WipeArrayAdaptor(this, wipeSelector));
				
		new FolderIterator();
		checkBoxHolder_group2 = (ListView) findViewById(R.id.checkBoxHolder_group2);
		checkBoxHolder_group2.setAdapter(new WipeArrayAdaptor(this, FolderIterator.getFolderList(this)));
		
	}
	
	private void doWipe() {
		// iterate through options to see what's checked
		
		
		for(WipeSelector w : wipeSelector) {

		}
		
		// create a wipe controller instance
		wc = new WipeController();
		
		// wipe baby!
		
	}

	@Override
	public void onClick(View v) {
		if(v == wipeButton) {
			doWipe();
		}
	}
}
