package org.safermobile.intheclear;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Shout extends Activity implements Runnable, OnClickListener {
	private SharedPreferences _sp;
	
	TextView panicMsg;
	Button sendShout,changeMsg;
	
	long firstWarning = 5000L;
	long countDown = 1000L;
	
	boolean keepPanicing = true;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shout);
        
        _sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        
        panicMsg = (TextView) findViewById(R.id.panicMsg);
        panicMsg.setText(panicMsg.getText() + "\n" + _sp.getString("DefaultPanicMsg", ""));
        
        changeMsg = (Button) findViewById(R.id.changePanicMsg_btn);
        changeMsg.setOnClickListener(this);
        
        sendShout = (Button) findViewById(R.id.shoutBtn);
        sendShout.setOnClickListener(this);
    }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		if(v == sendShout) {
			
		} else if(v == changeMsg) {
			
		}
	}
}
