package org.safermobile.intheclear;

import org.safermobile.intheclear.ui.ITCConstants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

public class Wizard extends Activity implements OnClickListener {
	int wNum,nextWizard,lastWizard = 0;
	String[] wizardTexts;
	ScrollView sv;
	Button wizardForward,wizardBackward;
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wizard);
		
		wizardForward = (Button) findViewById(R.id.wizardForward);
		wizardForward.setOnClickListener(this);
		
		wizardBackward = (Button) findViewById(R.id.wizardBackward);
		wizardBackward.setOnClickListener(this);
		
		sv = (ScrollView) findViewById(R.id.wizardDisplay);
		
		wizardTexts = getResources().getStringArray(R.array.WIZARD_TEXT);
		if(getIntent().hasExtra("wNum")) {
			wNum = getIntent().getIntExtra("wNum", 0);
			TextView t = new TextView(this);
			t.setText(wizardTexts[wNum]);
			sv.addView(t);
			
			
			Log.d(ITCConstants.Log.ITC,"wnum = " + wNum);
		}
			
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
		if(v == wizardForward) {
			if(wNum < (wizardTexts.length - 1)) {
				Intent i = new Intent(this,Wizard.class);
				i.putExtra("wNum", (wNum + 1));
				startActivity(i);
			} else {
				Log.d(ITCConstants.Log.ITC,"going back now!");
				Intent i = new Intent(this,InTheClear.class);
				startActivity(i);
			}
		} else if(v == wizardBackward) {
			if(wNum > 1) {
				Intent i = new Intent(this,Wizard.class);
				i.putExtra("wNum", (wNum - 1));
				startActivity(i);
			} else if(wNum == 1) {
				Intent i = new Intent(this,InTheClear.class);
				startActivity(i);
			}
		}
	}
}
