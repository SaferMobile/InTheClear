package org.safermobile.intheclear.apps;

import org.safermobile.intheclear.ITCConstants;
import org.safermobile.intheclear.R;
import org.safermobile.intheclear.controllers.ShoutController;
import org.safermobile.intheclear.controllers.WipeController;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Panic extends Activity implements OnClickListener {
	SharedPreferences _sp;
	TextView panicReadout,panicProgress;
	LinearLayout panicControl;
	Button controlPanic;
	
	ShoutController sc;
	WipeController wc;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.panic);
		
		_sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		
		sc = new ShoutController(this);
		
		panicReadout = (TextView) findViewById(R.id.panicReadout);
		panicReadout.setText(
				this.getResources().getString(R.string.KEY_PANIC_MSG_TITLE) +
				"\n\n" + _sp.getString(ITCConstants.Preference.DEFAULT_PANIC_MSG, "") +
				"\n\n" + sc.buildShoutData(_sp.getString(ITCConstants.Preference.USER_DISPLAY_NAME, "")));
		
		panicControl = (LinearLayout) findViewById(R.id.panicControl);
		
		if(!_sp.getBoolean(ITCConstants.Preference.DEFAULT_ONE_TOUCH_PANIC, false)) {
			controlPanic = new Button(this);
			controlPanic.setText(this.getResources().getString(R.string.KEY_PANIC_BTN_PANIC));
			controlPanic.setOnClickListener(this);
			panicControl.addView(controlPanic);
		} else {
			panicProgress = new TextView(this);
			panicProgress.setText(this.getResources().getString(R.string.KEY_PANIC_PROGRESS_1));
		}
	}
	
	public void cancelPanic() {
		
	}
	
	public void startPanic() {
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu m) {
		MenuInflater mi = getMenuInflater();
		mi.inflate(R.menu.panic_menu, m);
		return true;
	}
	
	public boolean onMenuOptionSelected(MenuItem i) {
		switch(i.getItemId()) {
		case R.id.cancelPanic:
			cancelPanic();
			return true;
		default:
			return false;
		}
	}

	@Override
	public void onClick(View v) {
		if(v == controlPanic) {
			startPanic();
		}
		
	}
}
