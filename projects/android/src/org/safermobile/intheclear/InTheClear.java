package org.safermobile.intheclear;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class InTheClear extends Activity implements OnClickListener {
	Button toPrefs;
	private final int RESULT_PREFERENCES_UPDATED = 1;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        toPrefs = (Button) findViewById(R.id.toPrefs);
        toPrefs.setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		if(v == toPrefs) {
			Intent i = new Intent(this,ITCPreferences.class);
			startActivityForResult(i,RESULT_PREFERENCES_UPDATED);
		}
		
	}
}