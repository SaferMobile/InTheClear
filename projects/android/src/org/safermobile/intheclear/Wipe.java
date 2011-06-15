package org.safermobile.intheclear;

import java.util.ArrayList;

import org.safermobile.intheclear.controllers.WipeController;
import org.safermobile.intheclear.ui.WipeArrayAdaptor;
import org.safermobile.intheclear.ui.WipeSelector;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class Wipe extends Activity implements OnClickListener {
	private SharedPreferences _sp;
	
	Button wipeButton;
	ListView checkBoxHolder_group1,checkBoxHolder_group2;
	ArrayList<WipeSelector> wipeSelector;
	
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
		
		wipeSelector.add(new WipeSelector(getString(R.string.KEY_WIPE_WIPECONTACTS), 1, shouldWipeContacts));
		wipeSelector.add(new WipeSelector(getString(R.string.KEY_WIPE_WIPEPHOTOS), 2, shouldWipePhotos));
		wipeSelector.add(new WipeSelector(getString(R.string.KEY_WIPE_CALLLOG),3,shouldWipeCallLog));
		wipeSelector.add(new WipeSelector(getString(R.string.KEY_WIPE_SMS),4,shouldWipeSMS));

		checkBoxHolder_group1 = (ListView) findViewById(R.id.checkBoxHolder_group1);
		checkBoxHolder_group1.setAdapter(new WipeArrayAdaptor(this, wipeSelector));
		
		checkBoxHolder_group2 = (ListView) findViewById(R.id.checkBoxHolder_group2);
		
		
	}
	
	private void doWipe() {
		// iterate through options to see what's checked
		
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
