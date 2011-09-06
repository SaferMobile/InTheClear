package org.safermobile.intheclear;

import java.util.Locale;

import org.safermobile.intheclear.apps.Panic;
import org.safermobile.intheclear.apps.Shout;
import org.safermobile.intheclear.apps.Wipe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class InTheClear extends Activity implements OnClickListener {
	SharedPreferences _sp;
	ImageView logoPanic;
	GridView launchGrid;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        
        _sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        if(_sp.getBoolean("IsVirginUser", true)) {
        	AlertDialog.Builder ad = new AlertDialog.Builder(this);
        	ad.setTitle(getResources().getString(R.string.KEY_PREF_LANGUAGE_TITLE));
        	
        	CharSequence[] langs = getResources().getStringArray(R.array.languages);
        	ad.setItems(langs, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(setNewLocale(getResources().getStringArray(R.array.languages_values)[which]))
						launchWizard();
				}
			});
        	
        	AlertDialog alert = ad.create();
        	alert.show();
      	}
        
        logoPanic = (ImageView) findViewById(R.id.logoPanic);
        logoPanic.setOnClickListener(this);
        
        launchGrid = (GridView)findViewById(R.id.launchGrid);
        launchGrid.setAdapter(new ImageAdapter(this));
		launchGrid.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				if(position == 0)
					launchShout();
				else if(position == 1)
					launchWipe();
				else if(position == 2)
					launchWizard();
				else if(position == 3)
					launchPreferences();
			}
		});
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
	
	public boolean setNewLocale(String localeCode) {
		Configuration config = new Configuration();
		config.locale = new Locale(localeCode);
		getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
		Log.d(ITCConstants.Log.ITC,"current configuration = " + getBaseContext().getResources().getConfiguration().locale);

		return true;
	}
	
	@Override
	public void onClick(View v) {
		if(v == logoPanic)
			launchPanic();			
	}
	
	public class ImageAdapter extends BaseAdapter
	{
		Context mContext;
		
		public ImageAdapter(Context c)
		{
			mContext = c;
		}
		
		@Override
		public int getCount()
		{
			return 4;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			View MyView = convertView;
			
			if (convertView == null)
			{
				//Inflate the layout
				LayoutInflater li = getLayoutInflater();
				MyView = li.inflate(R.layout.grid_item, null);
				
				//Add image & text
				TextView tv = (TextView)MyView.findViewById(R.id.grid_item_text);
				ImageView iv = (ImageView)MyView.findViewById(R.id.grid_item_image);
				MyView.setId(position);

				switch(position){
				case 0:
					tv.setText(getResources().getString(R.string.KEY_EMERGENCY_SMS_TITLE));
					iv.setImageResource(R.drawable.btn_shout);
					break;
				case 1:
					tv.setText(getResources().getString(R.string.KEY_WIPE_ACTIVITY_TITLE));
					iv.setImageResource(R.drawable.btn_wipe);
					break;
				case 2:
					tv.setText(getResources().getString(R.string.KEY_MAIN_TOWIZARD));
					iv.setImageResource(R.drawable.btn_wizard);
					break;
				case 3:
					tv.setText(getResources().getString(R.string.KEY_MAIN_TOPREFS));
					iv.setImageResource(R.drawable.btn_settings);
					break;
				}
			}
			return MyView;
		}
		
		@Override
		public Object getItem(int arg0) {
			// TODO auto-generated method stub
			return null;
		}
		
		@Override
		public long getItemId(int arg0) {
			// TODO auto-generated method stub
			return 0;
		}
	}
}