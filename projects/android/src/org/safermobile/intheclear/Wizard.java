package org.safermobile.intheclear;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.safermobile.intheclear.screens.WipePreferences;
import org.safermobile.intheclear.screens.WizardForm;
import org.safermobile.intheclear.ui.WipeSelector;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

public class Wizard extends Activity implements OnClickListener {
	int wNum,nextWizard,lastWizard = 0;
	
	ScrollView sv;
	WizardForm form;
	LinearLayout wizardStatusTrack,formFrame,wizardNavigation;
	TextView wizardTitle;

	Button wizardForward,wizardBackward;
	SharedPreferences _sp;
	SharedPreferences.Editor _ed;
	
	boolean nextButtonClickable, backButtonClickable;
	int[] statusColors;
	
	private BroadcastReceiver br;
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wizard);
		
		_sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		_ed = _sp.edit();
		
		/* if this is the first time the user has launched the app,
		 * the user will immediately be brought here (with a bundle from the main activity)
		 * set the "IsVirginUser" preference to false now
		 * that the user's cherry is sufficiently popped.
		 */
		if(_sp.getBoolean("IsVirginUser", true)) {
			_ed.putBoolean(ITCConstants.Preference.IS_VIRGIN_USER, false);
			_ed.commit();
		}
		
		wizardForward = (Button) findViewById(R.id.wizardForward);
		wizardForward.setText(getResources().getString(R.string.KEY_WIZARD_NEXT));
		wizardForward.setOnClickListener(this);
		
		wizardBackward = (Button) findViewById(R.id.wizardBackward);
		wizardBackward.setText(getResources().getString(R.string.KEY_WIZARD_BACK));
		wizardBackward.setOnClickListener(this);
		
		wizardStatusTrack = (LinearLayout) findViewById(R.id.wizardStatusTrack);
		wizardNavigation = (LinearLayout) findViewById(R.id.wizardNavigation);
		
		if(getIntent().hasExtra("wNum")) {
			wNum = getIntent().getIntExtra("wNum", 0);
		} else {
			wNum = 1;
			wizardBackward.setClickable(false);
		}
		
		wizardTitle = (TextView) findViewById(R.id.wizardTitle);
		
		sv = (ScrollView) findViewById(R.id.wizardDisplay);
		
		initWizardFrame();
		form = new WizardForm(
				this,
				wNum,
				new int[] {
					getWindowManager().getDefaultDisplay().getWidth(),
					getWindowManager().getDefaultDisplay().getHeight()
				}
		);
		sv.addView(form.returnForm());
		if(form.hasPreferenceData())
			populateDefaults(sv);
		
		br = new BroadcastReceiver() {
			@Override
			public void onReceive(Context c, Intent i) {
				if(i.hasExtra(ITCConstants.Wizard.WIZARD_ACTION)) {
					switch(i.getIntExtra(ITCConstants.Wizard.WIZARD_ACTION, 0)) {
					case ITCConstants.Wizard.LAUNCH_WIPE_SELECTOR:
						Intent w = new Intent(Wizard.this,WipePreferences.class);
						Wizard.this.startActivityForResult(w,ITCConstants.Results.PREFERENCES_UPDATED);
						break;
					}
				}
				
			}
			
		};
			
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		// start broadcast receiver
		IntentFilter filter = new IntentFilter();
		filter.addAction(ITCConstants.Wizard.ACTION);
		registerReceiver(br,filter);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent i) {
		Log.d(ITCConstants.Log.ITC,"back from activity: " + requestCode);
		if(resultCode == RESULT_OK) {
			if(requestCode == ITCConstants.Results.PREFERENCES_UPDATED) {
				// save these prefs,
				ArrayList<Map<Integer,Boolean>> wipePreferencesHolder = 
						(ArrayList<Map<Integer, Boolean>>) i.getSerializableExtra(ITCConstants.Preference.WIPE_SELECTOR);
				
				Map<Integer,Boolean> wipePreferences = wipePreferencesHolder.get(0);
				
				_ed.putBoolean(ITCConstants.Preference.DEFAULT_WIPE_CALENDAR, wipePreferences.get(ITCConstants.Wipe.CALENDAR)).commit();
				_ed.putBoolean(ITCConstants.Preference.DEFAULT_WIPE_CALLLOG, wipePreferences.get(ITCConstants.Wipe.CALLLOG)).commit();	
				_ed.putBoolean(ITCConstants.Preference.DEFAULT_WIPE_CONTACTS, wipePreferences.get(ITCConstants.Wipe.CONTACTS)).commit();
				_ed.putBoolean(ITCConstants.Preference.DEFAULT_WIPE_FOLDERS, wipePreferences.get(ITCConstants.Wipe.SDCARD)).commit();
				_ed.putBoolean(ITCConstants.Preference.DEFAULT_WIPE_PHOTOS, wipePreferences.get(ITCConstants.Wipe.PHOTOS)).commit();
				_ed.putBoolean(ITCConstants.Preference.DEFAULT_WIPE_SMS, wipePreferences.get(ITCConstants.Wipe.SMS)).commit();

				// un-grey out the next button
				
			}
		}
	}
	
	private void populateDefaults(View view) {
		/*
		 *  iterate through the scroll view's content,
		 *  and fill in the default/preference data
		 *  if necessary
		 */
		
		for(int x=0;x<((ViewGroup) view).getChildCount();x++) {
			View v = ((ViewGroup) view).getChildAt(x);
			//Log.d(ITCConstants.Log.ITC,"THIS VIEW IS: " + v.getClass().toString());
			if(v instanceof EditText && ((String) v.getContentDescription()).compareTo("") != 0) {
				((EditText) v).setHint(_sp.getString((String) v.getContentDescription(), ""));
				Log.d(ITCConstants.Log.ITC,"data should be found for " + (String) v.getContentDescription() + "\n" + _sp.getString((String) v.getContentDescription(),""));
			} else if(v instanceof Button && ((String) v.getContentDescription()).compareTo("") != 0) {
				// actually, it's a checkbox, so we have to cast it.
				((CheckBox) v).setSelected(_sp.getBoolean((String) v.getContentDescription(), false));
				Log.d(ITCConstants.Log.ITC,"data should be found for " + (String) v.getContentDescription() + "\n" + _sp.getBoolean((String) v.getContentDescription(), false));

			} else if(v instanceof LinearLayout) {
				populateDefaults(v);
			}
		}
		 
	}
	
	private void savePreferenceData(View view) {
		/*
		 *  iterate through the scroll view's content,
		 *  and commit the changes made to preference data
		 *  if necessary
		 */
		
		for(int x=0;x<((ViewGroup) view).getChildCount();x++) {
			View v = ((ViewGroup) view).getChildAt(x);
			if(v instanceof EditText && ((String) v.getContentDescription()).compareTo("") != 0 && ((EditText) v).getText().length() > 0) {
				_ed.putString((String) v.getContentDescription(), ((EditText) v).getText().toString()).commit();
			} else if(v instanceof Button && ((String) v.getContentDescription()).compareTo("") != 0) {
				_ed.putBoolean((String) v.getContentDescription(), ((CheckBox) v).isSelected()).commit();
			} else if(v instanceof LinearLayout) {
				savePreferenceData(v);
			}
		}
	}

	/*
	private LinearLayout populateDefaults(View view) {
		Log.d(ITCConstants.Log.ITC,"updating view with data");
		
		LinearLayout ff = (LinearLayout) view;
		for(int x=0;x<ff.getChildCount();x++) {
			View v = ff.getChildAt(x);
			Log.d(ITCConstants.Log.ITC,"view: " + v.getClass());
			
			// TODO: if it's a linearlayout, drill down into it...
			if(v.getContentDescription() != null && v.getContentDescription().length() > 0) {
				if(v instanceof EditText) {
					EditText et = (EditText) v;
					String hint = _sp.getString((String) v.getContentDescription(), "");
					et.setHint(hint);

				} else if(v instanceof ListView) {
					ListView lv = (ListView) v;
					for(int l=0;l<lv.getCount();l++) {
						WipeSelector w = (WipeSelector) lv.getItemAtPosition(l);
						switch(w.getWipeType()) {
						case ITCConstants.Wipe.CALENDAR:
							w.setSelected(_sp.getBoolean(ITCConstants.Preference.DEFAULT_WIPE_CALENDAR, false));
							break;
						case ITCConstants.Wipe.CALLLOG:
							w.setSelected(_sp.getBoolean(ITCConstants.Preference.DEFAULT_WIPE_CALLLOG, false));
							break;
						case ITCConstants.Wipe.CONTACTS:
							w.setSelected(_sp.getBoolean(ITCConstants.Preference.DEFAULT_WIPE_CONTACTS, false));
							break;
						case ITCConstants.Wipe.FOLDER:
							w.setSelected(_sp.getBoolean(ITCConstants.Preference.DEFAULT_WIPE_FOLDERS, false));
							break;
						case ITCConstants.Wipe.PHOTOS:
							w.setSelected(_sp.getBoolean(ITCConstants.Preference.DEFAULT_WIPE_PHOTOS, false));
							break;
						case ITCConstants.Wipe.SMS:
							w.setSelected(_sp.getBoolean(ITCConstants.Preference.DEFAULT_WIPE_SMS, false));
							break;
						case ITCConstants.Preference.ONE_TOUCH:
							w.setSelected(_sp.getBoolean(ITCConstants.Preference.DEFAULT_ONE_TOUCH_PANIC, false));
							break;
						default:
							// TODO: handle individual folders selected for wipe
							break;
						}
					}
				}
			}
		}
		return ff;
	}
	
	private void savePreferenceState() {
		for(int x=0;x<form.returnForm().getChildCount();x++) {
			View v = form.returnForm().getChildAt(x);
			if(v.getContentDescription() != null && v.getContentDescription().length() > 0) {
				if(v instanceof EditText) {
					String key = (String) v.getContentDescription();
					String val = ((EditText)v).getText().toString();
					
					if(val.compareTo("") != 0) {
						_ed.putString(key, val);
					}
				} else if(v instanceof ListView) {
					if(v.getContentDescription().toString().compareTo(ITCConstants.Preference.WIPE_SELECTOR) == 0 ||
							v.getContentDescription().toString().compareTo(ITCConstants.Preference.DEFAULT_ONE_TOUCH_PANIC) == 0) {
						ListView lv = (ListView) v;
						for(int l=0;l<lv.getCount();l++) {
							WipeSelector w = (WipeSelector) lv.getItemAtPosition(l);
							String key;
							switch(w.getWipeType()) {
							case ITCConstants.Wipe.CALENDAR:
								key = ITCConstants.Preference.DEFAULT_WIPE_CALENDAR;
								_ed.putBoolean(key, w.getSelected());
								break;
							case ITCConstants.Wipe.CALLLOG:
								key = ITCConstants.Preference.DEFAULT_WIPE_CALLLOG;
								_ed.putBoolean(key, w.getSelected());
								break;
							case ITCConstants.Wipe.CONTACTS:
								key = ITCConstants.Preference.DEFAULT_WIPE_CONTACTS;
								_ed.putBoolean(key, w.getSelected());
								break;
							case ITCConstants.Wipe.FOLDER:
								break;
							case ITCConstants.Wipe.PHOTOS:
								key = ITCConstants.Preference.DEFAULT_WIPE_PHOTOS;
								_ed.putBoolean(key, w.getSelected());
								break;
							case ITCConstants.Wipe.SMS:
								key = ITCConstants.Preference.DEFAULT_WIPE_SMS;
								_ed.putBoolean(key, w.getSelected());
								break;
							case ITCConstants.Preference.ONE_TOUCH:
								key = ITCConstants.Preference.DEFAULT_ONE_TOUCH_PANIC;
								_ed.putBoolean(key, w.getSelected());
								break;
							default:
								// TODO: handle individual folders for wiping
								break;
							}
						}
					}
				}
			}
		}
		_ed.commit();
	}
	*/
	
	@Override 
	public void onPause() {
		super.onPause();
		// destroy broadcast receiver
		unregisterReceiver(br);
	}

	@Override
	public void onClick(View v) {
		if(v == wizardForward) {
			if(form.hasPreferenceData())
				savePreferenceData(sv);
			if(wNum < getResources().getStringArray(R.array.WIZARD_TITLES).length) {
				Intent i = new Intent(this,Wizard.class);
				int nextTarget;
				switch(wNum) {
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
			savePreferenceData(sv);
			if(wNum > 1) {
				Intent i = new Intent(this,Wizard.class);
				int backTarget;
				switch(wNum) {
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
	
	private void initWizardFrame() {
		/*
		 * what page in the wizard are we on?
		 * this block of code sets the indicator on top
		 * and the title to be displayed
		 */
		String[] titles = getResources().getStringArray(R.array.WIZARD_TITLES);
		
		final StatusCircle[] circles = new StatusCircle[titles.length];
		for(int c=0;c<titles.length;c++) {
			int color = Color.GRAY;
			if(c == (wNum - 1))
				color = Color.YELLOW;
			
			circles[c] = new StatusCircle(color,c * 40);
		}
		
		
		
		wizardStatusTrack.setBackgroundDrawable(new Drawable() {
			private Paint p = new Paint();

			@Override
			public void draw(Canvas canvas) {
				for(StatusCircle sc : circles) {
					p.setColor(sc.color);
					canvas.drawCircle(sc.x, sc.y, sc.r, p);
				}
				
			}

			@Override
			public int getOpacity() {
				return 0;
			}

			@Override
			public void setAlpha(int alpha) {}

			@Override
			public void setColorFilter(ColorFilter cf) {}
			
		});
		
		wizardTitle.setText(titles[wNum - 1]);
	}
	
	private class StatusCircle  {
		float x;
		int color;
		float y = 30f;
		float r = 8f;
		
		public StatusCircle(int color, float x) {
			this.x = x + 20f;
			this.color = color;
		}
	}
}
