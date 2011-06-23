package org.safermobile.intheclear;

import org.safermobile.intheclear.screens.WizardForm;
import org.safermobile.intheclear.ui.WipeSelector;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.EditText;
import android.widget.ScrollView;

public class Wizard extends Activity implements OnClickListener {
	int wNum,nextWizard,lastWizard = 0;
	ScrollView sv;
	WizardForm form;
	Button wizardForward,wizardBackward;
	SharedPreferences _sp;
	SharedPreferences.Editor _ed;
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wizard);
		
		_sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		_ed = _sp.edit();
		
		wizardForward = (Button) findViewById(R.id.wizardForward);
		wizardForward.setOnClickListener(this);
		
		wizardBackward = (Button) findViewById(R.id.wizardBackward);
		wizardBackward.setOnClickListener(this);
		
		sv = (ScrollView) findViewById(R.id.wizardDisplay);
		
		if(getIntent().hasExtra("wNum")) {
			wNum = getIntent().getIntExtra("wNum", 0);
			
			form = new WizardForm(this,wNum);
			sv.addView(form.returnForm());
			
			Log.d(ITCConstants.Log.ITC,"wnum = " + wNum);
		}
			
	}
	
	private void savePreferenceState() {
		for(int x=0;x<form.returnForm().getChildCount();x++) {
			View v = form.returnForm().getChildAt(x);
			if(v.getContentDescription() != null && v.getContentDescription().length() > 0) {
				if(v instanceof EditText) {
					String val = ((EditText) v).getText().toString();
					
					if(val.compareTo("") != 0) {
						String key = (String) v.getContentDescription();
						_ed.putString(key, val);
					}
				} else if(v instanceof ListView) {
					if(v.getContentDescription().toString().compareTo(ITCConstants.Preference.WIPE_SELECTOR) == 0) {
						ListView lv = (ListView) v;
						for(int l=0;l<lv.getCount();l++) {
							WipeSelector w = (WipeSelector) lv.getItemAtPosition(l);
							String key;
							if(w.getSelected()) {
								switch(w.getWipeType()) {
								case ITCConstants.Wipe.CALENDAR:
									key = ITCConstants.Preference.DEFAULT_WIPE_CALENDAR;
									_ed.putBoolean(key, true);
									break;
								case ITCConstants.Wipe.CALLLOG:
									key = ITCConstants.Preference.DEFAULT_WIPE_CALLLOG;
									_ed.putBoolean(key,true);
									break;
								case ITCConstants.Wipe.CONTACTS:
									key = ITCConstants.Preference.DEFAULT_WIPE_CONTACTS;
									_ed.putBoolean(key, true);
									break;
								case ITCConstants.Wipe.FOLDER:
									break;
								case ITCConstants.Wipe.PHOTOS:
									key = ITCConstants.Preference.DEFAULT_WIPE_PHOTOS;
									_ed.putBoolean(key, true);
									break;
								case ITCConstants.Wipe.SMS:
									key = ITCConstants.Preference.DEFAULT_WIPE_SMS;
									_ed.putBoolean(key, true);
									break;
								case ITCConstants.Preference.ONE_TOUCH:
									key = ITCConstants.Preference.DEFAULT_ONE_TOUCH_PANIC;
									_ed.putBoolean(key, true);
									break;
								}
							}
						}
					}
				}
			}
		}
		_ed.commit();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu m) {
		MenuInflater mi = getMenuInflater();
		mi.inflate(R.menu.wizard_menu, m);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem i) {
		if(i.getItemId() == R.id.toHome) {
			Intent h = new Intent(this,InTheClear.class);
			startActivity(h);
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		if(form.hasPreferenceData()) {			
			savePreferenceState();
		}
		
		if(v == wizardForward) {
			if(wNum < ITCConstants.FormLength) {
				Intent i = new Intent(this,Wizard.class);
				int nextTarget;
				switch(wNum) {
				case 3:
					nextTarget = 2;
					break;
				case 4:
					nextTarget = 2;
					break;
				case 5:
					nextTarget = 2;
					break;
				case 6:
					nextTarget = 2;
					break;
				default:
					nextTarget = wNum + 1;
					break;
				}
				i.putExtra("wNum", nextTarget);
				startActivity(i);
			} else {
				Log.d(ITCConstants.Log.ITC,"going back now!");
				Intent i = new Intent(this,InTheClear.class);
				startActivity(i);
			}
		} else if(v == wizardBackward) {
			if(wNum > 1) {
				Intent i = new Intent(this,Wizard.class);
				int backTarget;
				switch(wNum) {
				case 4:
					backTarget = 2;
					break;
				case 5:
					backTarget = 2;
					break;
				case 6:
					backTarget = 2;
					break;
				case 7:
					backTarget = 2;
				default:
					backTarget = wNum - 1;
					break;
				}
				i.putExtra("wNum", backTarget);
				startActivity(i);
			} else if(wNum == 1) {
				Intent i = new Intent(this,InTheClear.class);
				startActivity(i);
			}
		}
	}
}
