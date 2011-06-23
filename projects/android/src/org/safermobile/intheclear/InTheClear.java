package org.safermobile.intheclear;

import org.safermobile.intheclear.screens.Splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class InTheClear extends Activity implements OnClickListener {
	Button toPrefs,toWizard;
	Splash splash;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splash = new Splash(this,ITCConstants.Duriation.SPLASH);
        setContentView(R.layout.main);
        
        toPrefs = (Button) findViewById(R.id.toPrefs);
        toPrefs.setOnClickListener(this);
        toWizard = (Button) findViewById(R.id.toWizard);
        toWizard.setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		if(v == toPrefs) {
			Intent i = new Intent(this,ITCPreferences.class);
			startActivity(i);
		} else if(v == toWizard) {
			Intent w = new Intent(this,Wizard.class);
			w.putExtra("wNum", 1);
			startActivity(w);
		}
		
	}
}