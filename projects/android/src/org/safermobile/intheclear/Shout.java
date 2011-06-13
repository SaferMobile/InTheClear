package org.safermobile.intheclear;

import org.safermobile.intheclear.controllers.ShoutController;

import android.app.Activity;
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

public class Shout extends Activity implements OnClickListener {
	private SharedPreferences _sp;
	
	private final static String ITC = "[InTheClear:Shout] ************************ ";
	
	TextView panicMsg;
	Button sendShout,changeMsg;
	EditText panicEdit;
	LinearLayout panicMsgHolder;
	android.view.ViewGroup.LayoutParams lp;
	
	String msg,shoutMsg,shoutData;
	
	long countDown = 5000L;
	long countDownInterval = 1000L;
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
        msg = _sp.getString("DefaultPanicMsg", "");
        
        panicMsg = (TextView) findViewById(R.id.panicMsg);
        lp = panicMsg.getLayoutParams();
        panicMsg.setText(panicMsg.getText() + "\n" + msg);
        
        changeMsg = (Button) findViewById(R.id.changePanicMsg_btn);
        changeMsg.setOnClickListener(this);
        
        sendShout = (Button) findViewById(R.id.shoutBtn);
        sendShout.setOnClickListener(this);
        
        panicMsgHolder = (LinearLayout) findViewById(R.id.panicMsgHolder);
        panicEdit = new EditText(this);
        panicEdit.setLayoutParams(lp);
    }
	
	public void doCountdown() {
		t = 0;
		cd = new CountDownTimer(countDown, countDownInterval) {
			@Override
			public void onFinish() {
				sc.sendSMSShout(_sp.getString("ConfiguredFriends",""), shoutMsg, shoutData);				
			}

			@Override
			public void onTick(long countDown) {
				String secondString = 
					getString(R.string.KEY_SHOUT_COUNTDOWNMSG) + " " + (5 - t) +
					" " + getString(R.string.KEY_SECONDS) + 
					"\n" + getString(R.string.KEY_SHOUT_COUNTDOWNCANCEL);
				Log.d(ITC,secondString);
				makeToast(secondString);
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
		Log.d(ITC,"CHOOSING " + i.getItemId());
		switch(i.getItemId()) {
		case R.id.cancelShout:
			if(cd != null) {
				cd.cancel();
			}
			return true;
		case R.id.restoreDefaultMsg:
			msg = _sp.getString("DefaultPanicMsg", "");
			isEditable = true;
			Log.d(ITC,"RESTORE DEFAULT PLS");
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
			shoutMsg = sc.buildShoutMessage(_sp.getString("UserDisplayName", ""), msg, _sp.getString("UserDisplayLocation", ""));
			shoutData = sc.buildShoutData(_sp.getString("UserDisplayName", ""));
			doCountdown();
		} else if(v == changeMsg) {
			toggleMessageUI();
		}
	}
}
