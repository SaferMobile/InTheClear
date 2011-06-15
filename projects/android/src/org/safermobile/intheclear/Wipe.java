package org.safermobile.intheclear;

import java.util.ArrayList;

import org.safermobile.intheclear.ui.WipeArrayAdaptor;
import org.safermobile.intheclear.ui.WipeSelector;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Wipe extends Activity implements OnClickListener {
	private SharedPreferences _sp;
	
	Button wipeButton;
	ListView checkBoxHolder;
	ArrayList<WipeSelector> wipeSelector;
	
	
	private final static String ITC = "[InTheClear:Wipe] ************************ ";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wipe);
		
		_sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		boolean shouldWipeContacts = _sp.getBoolean("DefaultWipeContacts",false);
		boolean shouldWipePhotos = _sp.getBoolean("DefaultWipePhotos", false);
		
		wipeSelector = new ArrayList<WipeSelector>();
		
		wipeSelector.add(new WipeSelector(getString(R.string.KEY_WIPE_WIPECONTACTS), 1, shouldWipeContacts));
		wipeSelector.add(new WipeSelector(getString(R.string.KEY_WIPE_WIPEPHOTOS), 2, shouldWipePhotos));

		
		checkBoxHolder = (ListView) findViewById(R.id.checkBoxHolder);
		checkBoxHolder.setAdapter(new WipeArrayAdaptor(this, wipeSelector));
		
	}

	@Override
	public void onClick(View v) {
		
	}
}
