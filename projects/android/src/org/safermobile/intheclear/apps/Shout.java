package org.safermobile.intheclear.apps;

import org.safermobile.intheclear.ITCConstants;
import org.safermobile.intheclear.R;
import org.safermobile.intheclear.controllers.ShoutController;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Shout extends Activity implements OnClickListener, OnDismissListener {
	private SharedPreferences _sp;
		
	TextView panicMsg,countdownReadout;
	Button sendShout,changeMsg,cancelCountdown;
	EditText panicEdit;
	LinearLayout panicMsgHolder;
	android.view.ViewGroup.LayoutParams lp;
	Dialog countdown;
	
	String msg,shoutMsg,shoutData,configuredFriends,userDisplayName,userDisplayLocation;
	
	CountDownTimer cd = null;
	int t;
	
	boolean keepPanicing = true;
	boolean isEditable = false;
	volatile boolean isCountingDown = false;
	
	ShoutController sc;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shout);
        
        _sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        
        panicMsg = (TextView) findViewById(R.id.panicMsg);
        lp = panicMsg.getLayoutParams();
        
        changeMsg = (Button) findViewById(R.id.changePanicMsg_btn);
        changeMsg.setOnClickListener(this);
        
        sendShout = (Button) findViewById(R.id.shoutBtn);
        sendShout.setOnClickListener(this);
        
        panicMsgHolder = (LinearLayout) findViewById(R.id.panicMsgHolder);
        panicEdit = new EditText(this);
        panicEdit.setLayoutParams(lp);
    }
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		alignPreferences();
	}
	
	private void alignPreferences() {
		msg = _sp.getString("DefaultPanicMsg", "");
        panicMsg.setText(panicMsg.getText() + "\n" + msg);

		configuredFriends = _sp.getString("ConfiguredFriends","");
		userDisplayName = _sp.getString("UserDisplayName", "");
		userDisplayLocation = _sp.getString("UserDisplayLocation", "");
	}
	
	public void doCountdown() {
		countdown = new Dialog(this);
		countdown.setContentView(R.layout.countdown);
		countdown.setCancelable(false);
		countdown.setOnDismissListener(this);
		
		countdownReadout = (TextView) countdown.findViewById(R.id.countdownReadout);
		
		cancelCountdown = (Button) countdown.findViewById(R.id.cancelCountdown);
		cancelCountdown.setText(R.string.KEY_SHOUT_MENU_CANCEL);
		cancelCountdown.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(v == cancelCountdown) {
					countdown.dismiss();
				}
			}
		});
		countdown.show();
		
		t = 0;
		cd = new CountDownTimer(ITCConstants.Duriation.COUNTDOWN, ITCConstants.Duriation.COUNTDOWNINTERVAL) {
			@Override
			public void onFinish() {
				sc.sendSMSShout(configuredFriends, shoutMsg, shoutData);
				countdown.dismiss();
			}

			@Override
			public void onTick(long countDown) {
				String secondString = 
					getString(R.string.KEY_SHOUT_COUNTDOWNMSG) + " " + (5 - t) +
					" " + getString(R.string.KEY_SECONDS);
				Log.d(ITCConstants.Log.ITC,secondString);
				countdownReadout.setText(secondString);
				t++;
			}
		};
		cd.start();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu m) {
		MenuInflater mi = getMenuInflater();
		mi.inflate(R.menu.shout_menu, m);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem i) {
		Log.d(ITCConstants.Log.ITC,"CHOOSING " + i.getItemId());
		switch(i.getItemId()) {
		case R.id.cancelShout:
			if(cd != null) {
				cd.cancel();
			}
			return true;
		case R.id.restoreDefaultMsg:
			msg = _sp.getString("DefaultPanicMsg", "");
			isEditable = true;
			Log.d(ITCConstants.Log.ITC,"RESTORE DEFAULT PLS");
			toggleMessageUI();
			return true;
		default:
			return false;	
		}
	}
	
	public void toggleMessageUI() {
		if(isEditable) {
			if (panicMsgHolder.getChildAt(0) instanceof android.widget.EditText &&
				!panicEdit.getText().toString().equals("")){
					msg = panicEdit.getText().toString();
			}
			
			panicMsgHolder.removeAllViews();
			panicMsgHolder.addView(panicMsg);
			panicMsgHolder.addView(changeMsg);
			
			panicMsg.setText(msg);
			changeMsg.setText(R.string.KEY_SHOUT_CHANGEPANICMSG_BTN);
			isEditable = false;
		} else {
			panicMsgHolder.removeAllViews();
			panicMsgHolder.addView(panicEdit);
			panicMsgHolder.addView(changeMsg);
			changeMsg.setText(R.string.KEY_SHOUT_SAVECHANGEDPANICMSG_BTN);
			isEditable = true;
		}
	}
	
	public void makeToast(String toast) {
		Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onClick(View v) {
		if(v == sendShout) {
			sc = new ShoutController(this);
			shoutMsg = sc.buildShoutMessage(userDisplayName, msg,userDisplayLocation);
			shoutData = sc.buildShoutData(userDisplayName);
			doCountdown();
		} else if(v == changeMsg) {
			toggleMessageUI();
		}
	}

	@Override
	public void onDismiss(DialogInterface d) {
		cd.cancel();
		
	}
}
