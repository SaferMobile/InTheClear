package org.safermobile.intheclear;

import java.util.ArrayList;

import org.safermobile.intheclear.screens.WipeSelectionForm;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

public class Wipe extends Activity implements OnClickListener {
	private SharedPreferences _sp;
	
	Button wipeButton;
	ListView checkBoxHolder;
	
	
	private final static String ITC = "[InTheClear:Wipe] ************************ ";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wipe);
		
		_sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		checkBoxHolder = (ListView) findViewById(R.id.checkBoxHolder);
		
				
		
		
	}

	@Override
	public void onClick(View v) {
		
	}
}
