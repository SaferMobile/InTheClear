package org.safermobile.intheclear;

import org.safermobile.intheclear.screens.WizardForm;

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

public class Wizard extends Activity implements OnClickListener {
	int wNum,nextWizard,lastWizard = 0;
	ScrollView sv;
	WizardForm form;
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
		
		if(getIntent().hasExtra("wNum")) {
			wNum = getIntent().getIntExtra("wNum", 0);
			
			form = new WizardForm(this,wNum);
			sv.addView(form.returnForm());
			
			if(form.containsPreferenceData()) {
				// then the form must look through the EditTexts for the input
			}
			
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
			if(wNum < ITCConstants.FormLength) {
				Intent i = new Intent(this,Wizard.class);
				int nextTarget;
				switch(wNum) {
				case 3:
					nextTarget = 2;
					break;
				case 4:
					nextTarget = 2;
					break;
				case 5:
					nextTarget = 2;
					break;
				case 6:
					nextTarget = 2;
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
			if(wNum > 1) {
				Intent i = new Intent(this,Wizard.class);
				int backTarget;
				switch(wNum) {
				case 4:
					backTarget = 2;
					break;
				case 5:
					backTarget = 2;
					break;
				case 6:
					backTarget = 2;
					break;
				case 7:
					backTarget = 2;
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
}
