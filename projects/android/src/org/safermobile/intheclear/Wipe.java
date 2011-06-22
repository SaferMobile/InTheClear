package org.safermobile.intheclear;

import java.io.File;
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
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wipe);
		
		wipeButton = (Button) findViewById(R.id.wipeButton);
		wipeButton.setOnClickListener(this);
		
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
		folderSelector = FolderIterator.getFolderList(this);
		checkBoxHolder_group2 = (ListView) findViewById(R.id.checkBoxHolder_group2);
		checkBoxHolder_group2.setAdapter(new WipeArrayAdaptor(this, folderSelector));
		
	}
	
	private void doWipe() {
		// iterate through options to see what's checked
		ArrayList<File> checkedFolders = new ArrayList<File>();
		
		for(WipeSelector w : folderSelector) {
			if(w.getSelected())
				checkedFolders.add(w.getFilePath());
		}
		
		// create a wipe controller instance
		wc = new WipeController(getBaseContext());
		
		// wipe baby!
		wc.wipePIMData(
				wipeSelector.get(0).getSelected(),
				wipeSelector.get(1).getSelected(),
				wipeSelector.get(2).getSelected(),
				wipeSelector.get(3).getSelected(),
				checkedFolders);
	}

	@Override
	public void onClick(View v) {
		if(v == wipeButton) {
			doWipe();
		}
	}
}
