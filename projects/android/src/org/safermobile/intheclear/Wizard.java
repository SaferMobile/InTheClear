package org.safermobile.intheclear;

import java.util.ArrayList;
import java.util.Map;

import org.safermobile.intheclear.screens.WipePreferences;
import org.safermobile.intheclear.sms.SMSSender;
import org.safermobile.intheclear.sms.SMSTesterConstants;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class Wizard extends Activity implements OnClickListener, SMSTesterConstants {
	int wNum,nextWizard,lastWizard = 0;
	
	ScrollView sv;
	WizardForm form;
	LinearLayout wizardStatusTrack,formFrame,wizardNavigation;
	TextView wizardTitle;

	Button wizardForward,wizardBackward;
	SharedPreferences _sp;
	SharedPreferences.Editor _ed;
	
	public ProgressDialog pd;
	public Dialog rd;
			
	boolean nextButtonClickable, backButtonClickable;
	int[] statusColors;
			
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wizard);
		
		_sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		_ed = _sp.edit();
		
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
		}
		
		wizardTitle = (TextView) findViewById(R.id.wizardTitle);
		initWizardFrame();
		
		sv = (ScrollView) findViewById(R.id.wizardDisplay);
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
			
	}
	
	public void showFailureDetails() {
		wizardTitle.setText(getResources().getString(R.string.WIZARD_CONFIRMATION_SMSTEST_FAIL_TITLE));
		LinearLayout l = (LinearLayout) sv.getChildAt(0);
		l.removeAllViews();
		
		TextView failures = new TextView(this);
		failures.setTextColor(android.graphics.Color.BLACK);
		failures.setText(getResources().getString(R.string.WIZARD_SMS_TEST_FAILURES));
		l.addView(failures);
		
		TextView email = new TextView(this);
		email.setText(getResources().getString(R.string.SAFERMOBILE_EMAIL));
		Linkify.addLinks(email, Linkify.EMAIL_ADDRESSES);
		l.addView(email);
		
		
		wizardForward.setClickable(false);
		wNum = 3;
	}
	
	@Override
	public void onResume() {
		super.onResume();
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
				wizardForward.setEnabled(true);
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
			} else if(v instanceof Button && ((String) v.getContentDescription()).compareTo("") != 0) {
				// actually, it's a checkbox, so we have to cast it.
				((CheckBox) v).setSelected(_sp.getBoolean((String) v.getContentDescription(), false));
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
	
	@Override 
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onClick(View v) {
		if(v == wizardForward) {
			if(form.hasPreferenceData())
				try {
					savePreferenceData(sv);
				} catch(NullPointerException e) {}
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
				Intent i = new Intent(this,InTheClear.class);
				startActivity(i);
			}
		} else if(v == wizardBackward) {
			try {
				savePreferenceData(sv);
			} catch(NullPointerException e) {}
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
				color = getResources().getColor(R.color.maGreen);
			
			circles[c] = new StatusCircle(color,c * 70);
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
	
	private class WizardForm extends View {
		ArrayList<View> views;
		String[] wizardText;
		LinearLayout _l;
		LayoutParams lp;	
		int[] _screen;
		
		Context c;
		
		boolean _hasPreferenceData = false;
		
		OnFocusChangeListener editTextFocusChangeListener = new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(v.hasFocus()) {
					String textToEdit = ((EditText) v).getHint().toString();
					((EditText) v).setText(textToEdit);
				}
				
			}
			
		};
		
		public WizardForm(Context c, int wNum, int[] screen) {
			super(c);
			this.c = c;
			
			views = new ArrayList<View>();
			wizardText = getResources().getStringArray(R.array.WIZARD_TEXT);
					
			lp = new LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
			_screen = screen;
			
			_l = new LinearLayout(c);		
			_l.setOrientation(LinearLayout.VERTICAL);
			_l.setLayoutParams(lp);
							
			TextView intro = new TextView(c);
			intro.setText(wizardText[wNum - 1]);
			intro.setTextColor(android.graphics.Color.BLACK);
			views.add(intro);
			
			switch(wNum) {
			case 1:
				
				break;
			case 2:
				wizardForward.setEnabled(false);
				LinearLayout ynHolder = new LinearLayout(c);
				ynHolder.setLayoutParams(lp);
				ynHolder.setOrientation(LinearLayout.HORIZONTAL);
				
				TextView yourNumberTitle = new TextView(c);
				yourNumberTitle.setWidth((int) (_screen[0] * 0.3));
				yourNumberTitle.setText(getResources().getString(R.string.WIZARD_YOUR_MOBILE_NUMBER));
				yourNumberTitle.setTextColor(android.graphics.Color.BLACK);
				ynHolder.addView(yourNumberTitle);
				
				EditText yourNumberTxt = new EditText(c);
				yourNumberTxt.setWidth((int) (_screen[0] * 0.6));
				yourNumberTxt.setId(R.string.yourNumberStatic);
				ynHolder.addView(yourNumberTxt);
				
				views.add(ynHolder);
				
				Button send = new Button(c);
				send.setText(getResources().getString(R.string.WIZARD_SEND_TEST));
				send.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// Test SMS message
						testSMS();
						
					}
				});
				views.add(send);
				
				break;
			case 3:
				LinearLayout rnHolder = new LinearLayout(c);
				rnHolder.setLayoutParams(lp);
				rnHolder.setOrientation(LinearLayout.HORIZONTAL);
				rnHolder.setGravity(Gravity.CENTER_VERTICAL);
				
				TextView recipientNumbersTitle = new TextView(c);
				recipientNumbersTitle.setText(getResources().getString(R.string.WIZARD_RECIPIENT_MOBILE_NUMBERS));
				recipientNumbersTitle.setWidth((int) (_screen[0] * 0.3));
				recipientNumbersTitle.setTextColor(android.graphics.Color.BLACK);
				rnHolder.addView(recipientNumbersTitle);
				
				EditText recipientNumbers = new EditText(c);
				recipientNumbers.setContentDescription(ITCConstants.Preference.CONFIGURED_FRIENDS);
				recipientNumbers.setWidth((int) (_screen[0] * 0.6));
				recipientNumbers.setId(R.string.configuredFriendsStatic);
				recipientNumbers.setOnFocusChangeListener(editTextFocusChangeListener);
				rnHolder.addView(recipientNumbers);
				views.add(rnHolder);
				
				LinearLayout emHolder = new LinearLayout(c);
				emHolder.setLayoutParams(lp);
				emHolder.setOrientation(LinearLayout.HORIZONTAL);
				emHolder.setGravity(Gravity.CENTER_VERTICAL);
				emHolder.setPadding(0, 10, 0, 10);
				
				TextView emergencyMessageTitle = new TextView(c);
				emergencyMessageTitle.setText(getResources().getString(R.string.WIZARD_EMERGENCY_MESSAGE));
				emergencyMessageTitle.setWidth((int) (_screen[0] * 0.3));
				emergencyMessageTitle.setPadding(0, 5, 0, 5);
				emergencyMessageTitle.setTextColor(android.graphics.Color.BLACK);
				emergencyMessageTitle.setGravity(Gravity.TOP);
				emHolder.addView(emergencyMessageTitle);
				
				EditText emergencyMessage = new EditText(c);
				emergencyMessage.setContentDescription(ITCConstants.Preference.DEFAULT_PANIC_MSG);
				emergencyMessage.setWidth((int) (_screen[0] * 0.6));
				emergencyMessage.setHeight((int) (_screen[1] * 0.25));
				//emergencyMessage.setPadding(10, 5, 5, 7);
				emergencyMessage.setId(R.string.defaultPanicMessageStatic);
				emergencyMessage.setOnFocusChangeListener(editTextFocusChangeListener);
				emergencyMessage.setOnKeyListener(new OnKeyListener() {

					@Override
					public boolean onKey(View v, int i, KeyEvent k) {
						if(((EditText) v).getText().length() > 0)
							if(!wizardForward.isEnabled())
								wizardForward.setEnabled(true);
						else
							wizardForward.setEnabled(false);
						return false;
					}
					
				});
				emHolder.addView(emergencyMessage);
				
				views.add(emHolder);
				_hasPreferenceData = true;
				
				break;
			case 4:
				wizardForward.setEnabled(false);
				LinearLayout warningHolder = new LinearLayout(c);
				warningHolder.setLayoutParams(lp);
				warningHolder.setOrientation(LinearLayout.HORIZONTAL);
				warningHolder.setGravity(Gravity.CENTER_VERTICAL);
				
				ImageView warningImg = new ImageView(c);
				warningImg.setBackgroundDrawable(getResources().getDrawable(R.drawable.warning));
				warningHolder.addView(warningImg);
				
				TextView warningText = new TextView(c);
				warningText.setText(getResources().getString(R.string.WIZARD_WIPE_WARNING));
				warningText.setTextColor(android.graphics.Color.BLACK);
				warningHolder.addView(warningText);
				
				views.add(warningHolder);
				
				Button selectWipeData = new Button(c);
				selectWipeData.setText(getResources().getString(R.string.WIZARD_SELECT_DATA_BUTTON));
				selectWipeData.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent i = new Intent(Wizard.this,WipePreferences.class);
						Wizard.this.startActivityForResult(i, ITCConstants.Results.PREFERENCES_UPDATED);
					}
				});
				
				views.add(selectWipeData);
				break;
			case 5:
				LinearLayout oneTouchHolder = new LinearLayout(c);
				oneTouchHolder.setLayoutParams(lp);
				oneTouchHolder.setOrientation(LinearLayout.HORIZONTAL);
				oneTouchHolder.setPadding(0, 10, 0, 0);
				oneTouchHolder.setGravity(Gravity.CENTER_VERTICAL);
				
				CheckBox oneTouch = new CheckBox(c);
				oneTouch.setChecked(false);
				oneTouch.setContentDescription(ITCConstants.Preference.DEFAULT_ONE_TOUCH_PANIC);
				oneTouchHolder.addView(oneTouch);
				
				TextView oneTouchText = new TextView(c);
				oneTouchText.setText(getResources().getString(R.string.WIZARD_ONE_TOUCH_PANIC_DESCRIPTION));
				oneTouchText.setTextColor(android.graphics.Color.BLACK);
				oneTouchHolder.addView(oneTouchText);
				
				views.add(oneTouchHolder);
				_hasPreferenceData = true;
				
				break;
			case 6:
				// change buttons from back/next to back/finish
				
				/* if this is the first time the user has launched the app,
				 * the user will immediately be brought here (with a bundle from the main activity)
				 * set the "IsVirginUser" preference to false now
				 * that the user's cherry is sufficiently popped.
				 */
				if(_sp.getBoolean("IsVirginUser", true)) {
					_ed.putBoolean(ITCConstants.Preference.IS_VIRGIN_USER, false);
					_ed.commit();
				}
				wizardForward.setText(getResources().getString(R.string.KEY_FINISH));
				break;
			case 7:
				// The result from the data wipe selection?
				break;
			}
			
			for(View v : views) {
				_l.addView(v);
			}
		}
		
		@SuppressWarnings("unchecked")
		public void displayTestSMSResults(Message message) {
			rd = new Dialog(c);
			rd.setContentView(R.layout.confirmation);
			
			LinearLayout confirmationHolder = (LinearLayout) rd.findViewById(R.id.confirmationHolder);
			TextView confirmationTitle = (TextView) rd.findViewById(R.id.confirmationTitle);
			TextView confirmationText = (TextView) rd.findViewById(R.id.confirmationText);
			
			Button dismissConfirmation = new Button(c);
			dismissConfirmation.setText(getResources().getString(R.string.KEY_OK));
			dismissConfirmation.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					rd.dismiss();
				}
			});
			
			Map<String,Integer> msg = (Map<String, Integer>) message.obj;

			if(msg.get("status") == Activity.RESULT_OK) {
				confirmationTitle.setText(getResources().getString(R.string.WIZARD_CONFIRMATION_SMSTEST_TITLE));
				confirmationText.setText(getResources().getString(R.string.WIZARD_CONFIRMATION_SMSTEST));
				
				dismissConfirmation.setWidth(_screen[0]);
				confirmationHolder.addView(dismissConfirmation);
				wizardForward.setEnabled(true);
				((Button) views.get(2)).setVisibility(View.GONE);
			} else {
				confirmationTitle.setText(getResources().getString(R.string.WIZARD_CONFIRMATION_SMSTEST_FAIL_TITLE));
				confirmationText.setText(getResources().getString(R.string.WIZARD_CONFIRMATION_SMSTEST_FAIL));
				
				Button details = new Button(c);
				details.setText(getResources().getString(R.string.KEY_DETAILS));
				details.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						rd.dismiss();
						showFailureDetails();
					}
					
				});
				
				details.setWidth((int) (_screen[0] * 0.5));
				dismissConfirmation.setWidth((int) (_screen[0] * 0.5));
				confirmationHolder.addView(details);
				confirmationHolder.addView(dismissConfirmation);
				
				/*
				 * TODO: handle this better.
				TextView titleHolder = (TextView) findViewById(R.id.wizardTitle);
				titleHolder.setText(c.getResources().getString(R.string.WIZARD_CONFIRMATION_SMSTEST_FAIL));
				
				confirmationText = new TextView(c);
				confirmationText.setText(R.string.WIZARD_SMS_TEST_FAILURES);
				
				TextView supportEmail = new TextView(c);
				supportEmail.setText(R.string.SAFERMOBILE_EMAIL);
				supportEmail.setAutoLinkMask(Linkify.EMAIL_ADDRESSES);
				*/
			}
			rd.show();
		}
		
		public void testSMS() {			
			// get the input phone number
			String testSmsNumber = ((EditText) ((LinearLayout) views.get(1)).getChildAt(1)).getText().toString();

			// create a progress dialog
			pd = ProgressDialog.show(
					c,
					getResources().getString(R.string.KEY_WIZARD_PLEASE_WAIT),
					getResources().getString(R.string.KEY_WIZARD_TESTING_SMS_TITLE),
					true);
			
			// initiate SMSSender instance with customized handler
			// so UI will update according to SMSSender results
			SMSSender sms = new SMSSender(c,new Handler() {
				
				@SuppressWarnings("unchecked")
				public void handleMessage(Message message) {
					pd.dismiss();
					displayTestSMSResults(message);
				}
			});
			
			// send test
			sms.sendSMS(testSmsNumber,getResources().getString(R.string.KEY_WIZARD_SMSTESTMSG));
		}
		
		public boolean hasPreferenceData() {
			return _hasPreferenceData;
		}
		
		public LinearLayout returnForm() {
			return _l;
		}
	}
}
