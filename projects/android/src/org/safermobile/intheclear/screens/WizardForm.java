package org.safermobile.intheclear.screens;

import java.util.ArrayList;

import org.safermobile.intheclear.ITCConstants;
import org.safermobile.intheclear.R;
import org.safermobile.intheclear.data.MovementTracker;
import org.safermobile.intheclear.data.PhoneInfo;
import org.safermobile.intheclear.sms.SMSSender;
import org.safermobile.intheclear.ui.*;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

public class WizardForm extends View implements OnClickListener {
	ArrayList<View> views;
	String[] wizardText;
	LinearLayout _l;
	LayoutParams lp;	
	int[] _screen;
	
	boolean _hasPreferenceData = false;
		
	public WizardForm(final Context c, int wNum, int[] screen) {
		super(c);
		views = new ArrayList<View>();
		wizardText = c.getResources().getStringArray(R.array.WIZARD_TEXT);
				
		lp = new LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		_screen = screen;
		
		_l = new LinearLayout(c);		
		_l.setOrientation(LinearLayout.VERTICAL);
		_l.setLayoutParams(lp);
						
		TextView intro = new TextView(c);
		intro.setText(wizardText[wNum - 1]);
		views.add(intro);
		
		switch(wNum) {
		case 2:
			LinearLayout ynHolder = new LinearLayout(c);
			ynHolder.setLayoutParams(lp);
			ynHolder.setOrientation(LinearLayout.HORIZONTAL);
			
			TextView yourNumberTitle = new TextView(c);
			yourNumberTitle.setWidth((int) (_screen[0] * 0.3));
			yourNumberTitle.setText(c.getResources().getString(R.string.WIZARD_YOUR_MOBILE_NUMBER));
			ynHolder.addView(yourNumberTitle);
			
			EditText yourNumberTxt = new EditText(c);
			yourNumberTxt.setWidth((int) (_screen[0] * 0.6));
			yourNumberTxt.setId(R.string.yourNumberStatic);
			ynHolder.addView(yourNumberTxt);
			
			views.add(ynHolder);
			
			Button send = new Button(c);
			send.setText(c.getResources().getString(R.string.WIZARD_SEND_TEST));
			send.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// Test SMS message
					SMSSender sms = new SMSSender(c);
					final Dialog d = new Dialog(c);
					d.setContentView(R.layout.confirmation);
					
					TextView confirmationTitle = (TextView) d.findViewById(R.id.confirmationTitle);
					TextView confirmationText = (TextView) d.findViewById(R.id.confirmationText);
					Button dismissConfirmation = (Button) d.findViewById(R.id.dismissConfirmation);
					
					String testSmsNumber = ((EditText) ((LinearLayout) views.get(1)).getChildAt(1)).getText().toString();
					
					if(sms.sendSMS(testSmsNumber,
							c.getResources().getString(R.string.KEY_WIZARD_SMSTESTMSG))) {
								
						confirmationTitle.setText(c.getResources().getString(R.string.WIZARD_CONFIRMATION_SMSTEST_TITLE));
						confirmationText.setText(c.getResources().getString(R.string.WIZARD_CONFIRMATION_SMSTEST));
						dismissConfirmation.setText(c.getResources().getString(R.string.KEY_OK));
						dismissConfirmation.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								d.dismiss();
								// TODO: grey out send button
							}
						});
						((Button) views.get(2)).setVisibility(View.GONE);
					} else {
						confirmationTitle.setText(c.getResources().getString(R.string.WIZARD_CONFIRMATION_SMSTEST_FAIL_TITLE));
						confirmationText.setText(c.getResources().getString(R.string.WIZARD_CONFIRMATION_SMSTEST_FAIL));
						
						Button details = new Button(c);
						details.setText(c.getResources().getString(R.string.KEY_DETAILS));
						details.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								d.dismiss();
							}
							
						});
						LinearLayout parent = (LinearLayout) dismissConfirmation.getParent();
						
						parent.removeView(dismissConfirmation);
						parent.addView(details);
						
						dismissConfirmation.setText(c.getResources().getString(R.string.KEY_OK));
						dismissConfirmation.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								d.dismiss();
							}
						});
						parent.addView(dismissConfirmation);
						
						/*
						 *  TODO: fix error notification
						
						TextView titleHolder = (TextView) findViewById(R.id.wizardTitle);
						titleHolder.setText(c.getResources().getString(R.string.WIZARD_CONFIRMATION_SMSTEST_FAIL));
						
						confirmationText = new TextView(c);
						confirmationText.setText(R.string.WIZARD_SMS_TEST_FAILURES);
						
						TextView supportEmail = new TextView(c);
						supportEmail.setText(R.string.SAFERMOBILE_EMAIL);
						supportEmail.setAutoLinkMask(Linkify.EMAIL_ADDRESSES);
						
						*/
						
					}
					d.show();
				}
			});
			views.add(send);
			
			break;
		case 3:
			LinearLayout rnHolder = new LinearLayout(c);
			rnHolder.setLayoutParams(lp);
			rnHolder.setOrientation(LinearLayout.HORIZONTAL);
			
			TextView recipientNumbersTitle = new TextView(c);
			recipientNumbersTitle.setText(c.getResources().getString(R.string.WIZARD_RECIPIENT_MOBILE_NUMBERS));
			recipientNumbersTitle.setWidth((int) (_screen[0] * 0.3));
			rnHolder.addView(recipientNumbersTitle);
			
			EditText recipientNumbers = new EditText(c);
			recipientNumbers.setContentDescription(ITCConstants.Preference.CONFIGURED_FRIENDS);
			recipientNumbers.setWidth((int) (_screen[0] * 0.6));
			rnHolder.addView(recipientNumbers);
			views.add(rnHolder);
			
			LinearLayout emHolder = new LinearLayout(c);
			emHolder.setLayoutParams(lp);
			emHolder.setOrientation(LinearLayout.HORIZONTAL);
			emHolder.setPadding(0, 10, 0, 10);
			
			TextView emergencyMessageTitle = new TextView(c);
			emergencyMessageTitle.setText(c.getResources().getString(R.string.WIZARD_EMERGENCY_MESSAGE));
			emergencyMessageTitle.setWidth((int) (_screen[0] * 0.3));
			emergencyMessageTitle.setPadding(0, 5, 0, 5);
			emergencyMessageTitle.setGravity(Gravity.TOP);
			emHolder.addView(emergencyMessageTitle);
			
			EditText emergencyMessage = new EditText(c);
			emergencyMessage.setContentDescription(ITCConstants.Preference.DEFAULT_PANIC_MSG);
			emergencyMessage.setWidth((int) (_screen[0] * 0.6));
			emergencyMessage.setHeight((int) (_screen[1] * 0.25));
			emergencyMessage.setPadding(0, 5, 0, 5);	
			emHolder.addView(emergencyMessage);
			
			views.add(emHolder);
			_hasPreferenceData = true;
			
			break;
		case 4:
			LinearLayout warningHolder = new LinearLayout(c);
			warningHolder.setLayoutParams(lp);
			warningHolder.setOrientation(LinearLayout.HORIZONTAL);
			
			ImageView warningImg = new ImageView(c);
			warningImg.setBackgroundDrawable(c.getResources().getDrawable(R.drawable.warning));
			warningHolder.addView(warningImg);
			
			TextView warningText = new TextView(c);
			warningText.setText(c.getResources().getString(R.string.WIZARD_WIPE_WARNING));
			warningHolder.addView(warningText);
			
			views.add(warningHolder);
			
			Button selectWipeData = new Button(c);
			selectWipeData.setText(c.getResources().getString(R.string.WIZARD_SELECT_DATA_BUTTON));
			selectWipeData.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// broadcast listener prompts wizard class to open up the wipe selector
					Intent i = new Intent();
					i.putExtra(ITCConstants.Wizard.WIZARD_ACTION,ITCConstants.Wizard.LAUNCH_WIPE_SELECTOR);
					c.sendBroadcast(i.setAction(ITCConstants.Wizard.ACTION));
				}
			});
			
			views.add(selectWipeData);
			/*
			final LinearLayout testLocationL = new LinearLayout(c);
			testLocationL.setOrientation(LinearLayout.VERTICAL);
			testLocationL.setLayoutParams(auxLP);
			
			Button enable = new Button(c);
			enable.setText(c.getResources().getString(R.string.KEY_WIZARD_ENABLE));
			enable.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					new PhoneInfo(c);
					new MovementTracker(c);
					StringBuffer sb = new StringBuffer();
					String phoneTest = PhoneInfo.testData();
					String locTest = MovementTracker.testData();
					if(phoneTest != null && phoneTest.compareTo("") != 0) {
						sb.append(c.getResources().getString(R.string.WIZARD_CONFIRMATION_LOCATIONTEST) + "\n\n");
						sb.append(phoneTest);
						sb.append(locTest);
					} else {
						sb.append(c.getResources().getString(R.string.WIZARD_FAIL_LOCATIONTEST));
					}
					testLocationL.removeAllViews();
					
					TextView testResults = new TextView(c);
					testResults.setText(sb.toString());
					testLocationL.addView(testResults);
				}
			});
			testLocationL.addView(enable);
			views.add(testLocationL);
			*/
			
			break;
		case 5:
			// results of wipe preferences activity
			LinearLayout oneTouchHolder = new LinearLayout(c);
			oneTouchHolder.setLayoutParams(lp);
			oneTouchHolder.setOrientation(LinearLayout.HORIZONTAL);
			
			CheckBox oneTouch = new CheckBox(c);
			oneTouch.setChecked(true);
			oneTouch.setContentDescription(ITCConstants.Preference.DEFAULT_ONE_TOUCH_PANIC);
			oneTouchHolder.addView(oneTouch);
			
			TextView oneTouchText = new TextView(c);
			oneTouchText.setText(c.getResources().getString(R.string.WIZARD_ONE_TOUCH_PANIC_DESCRIPTION));
			oneTouchHolder.addView(oneTouchText);
			
			views.add(oneTouchHolder);
			_hasPreferenceData = true;
			
			break;
		case 6:
			LinearLayout homescreenHolder = new LinearLayout(c);
			homescreenHolder.setLayoutParams(lp);
			homescreenHolder.setOrientation(LinearLayout.HORIZONTAL);
			
			CheckBox homeScreen = new CheckBox(c);
			homeScreen.setChecked(true);
			homeScreen.setContentDescription(ITCConstants.Preference.DEFAULT_ADD_TO_HOMESCREEN);
			homescreenHolder.addView(homeScreen);
			
			TextView homeScreenText = new TextView(c);
			homeScreenText.setText(c.getResources().getString(R.string.WIZARD_ONE_TOUCH_HOMESCREEN_DESCRIPTION));
			homescreenHolder.addView(homeScreenText);
			
			views.add(homescreenHolder);
			_hasPreferenceData = true;
			break;
		case 7:
			// TODO: change buttons from back/next to back/finish
			break;
			/*
		case 6:
			ArrayList<WipeSelector> wipeSelector = new ArrayList<WipeSelector>();
			wipeSelector.add(new WipeSelector(
					c.getString(R.string.KEY_WIPE_WIPECONTACTS), 
					ITCConstants.Wipe.CONTACTS, 
					false));
			wipeSelector.add(
					new WipeSelector(
							c.getString(R.string.KEY_WIPE_WIPEPHOTOS), 
							ITCConstants.Wipe.PHOTOS, 
							false));
			wipeSelector.add(new WipeSelector(
					c.getString(R.string.KEY_WIPE_CALLLOG),
					ITCConstants.Wipe.CALLLOG,
					false));
			wipeSelector.add(new WipeSelector(
					c.getString(R.string.KEY_WIPE_SMS),
					ITCConstants.Wipe.SMS,
					false));
			wipeSelector.add(new WipeSelector(
					c.getString(R.string.KEY_WIPE_CALENDAR),
					ITCConstants.Wipe.CALENDAR,
					false));
			ListView wipeList = new ListView(c);
			wipeList.setLayoutParams(lp);
			wipeList.setAdapter(new WipeArrayAdaptor(c,wipeSelector));
			wipeList.setContentDescription(ITCConstants.Preference.WIPE_SELECTOR);
			views.add(wipeList);
			
			break;
		case 7:

			ListView folderList = new ListView(c);
			folderList.setLayoutParams(lp);
			folderList.setContentDescription(ITCConstants.Preference.WIPE_SELECTOR);

			new FolderIterator();
			ArrayList<WipeSelector> folderSelector = FolderIterator.getFolderList(c);
			folderList.setAdapter(new WipeArrayAdaptor(c,folderSelector));
			
			views.add(folderList);
			
			break;
			*/
		}
		for(View v : views) {
			_l.addView(v);
		}
		
	}
	
	public boolean hasPreferenceData() {
		return _hasPreferenceData;
	}
	
	public LinearLayout returnForm() {
		return _l;
	}

	@Override
	public void onClick(View v) {
		
	}

}
