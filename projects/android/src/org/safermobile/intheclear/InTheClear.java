package org.safermobile.intheclear;

import org.safermobile.intheclear.apps.Panic;
import org.safermobile.intheclear.apps.Shout;
import org.safermobile.intheclear.apps.Wipe;
import org.safermobile.intheclear.screens.Splash;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class InTheClear extends Activity implements OnClickListener {
	SharedPreferences _sp;
	LinearLayout launchWizard,launchPanic,launchPreferences; 
	ImageButton launchWipe,launchShout;
	Splash splash;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        
        _sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        if(_sp.getBoolean("IsVirginUser", true)) {
        	launchWizard();
      	}
        
        launchWizard = (LinearLayout) findViewById(R.id.launchWizard);
        launchWizard.setOnClickListener(this);
        
        launchPreferences = (LinearLayout) findViewById(R.id.launchPreferences);
        launchPreferences.setOnClickListener(this);
        
        launchPanic = (LinearLayout) findViewById(R.id.launchPanic);
        launchPanic.setOnClickListener(this);
        
        launchWipe = (ImageButton) findViewById(R.id.launchWipe);
        launchWipe.setOnClickListener(this);
        
        launchShout = (ImageButton) findViewById(R.id.launchShout);
        launchShout.setOnClickListener(this);
        
        
    }
	
	private void launchWizard() {
		Intent i = new Intent(this,Wizard.class);
		startActivity(i);
	}
	
	private void launchWipe() {
		Intent i = new Intent(this,Wipe.class);
		startActivity(i);
	}
	
	private void launchShout() {
		Intent i = new Intent(this,Shout.class);
		startActivity(i);
	}
	
	private void launchPanic() {
		Intent i = new Intent(this,Panic.class);
		startActivity(i);
	}
	
	private void launchPreferences() {
		Intent i = new Intent(this,ITCPreferences.class);
		startActivity(i);
	}

	@Override
	public void onClick(View v) {
		if(v == launchWizard)
			launchWizard();
		else if(v == launchShout)
			launchShout();
		else if(v == launchWipe)
			launchWipe();
		else if(v == launchPanic)
			launchPanic();
		else if(v == launchPreferences)
			launchPreferences();
		
	}
}