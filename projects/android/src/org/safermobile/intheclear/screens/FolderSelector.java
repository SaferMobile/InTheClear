package org.safermobile.intheclear.screens;

import java.io.File;
import java.util.ArrayList;

import org.safermobile.intheclear.ITCConstants;
import org.safermobile.intheclear.R;
import org.safermobile.intheclear.ui.FolderIterator;
import org.safermobile.intheclear.ui.WipeArrayAdaptor;
import org.safermobile.intheclear.ui.WipeSelector;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class FolderSelector extends Activity implements OnClickListener {
	Button confirmSelection;
	ListView wipeList;
	ArrayList<WipeSelector> wipeSelect;
	ArrayList<WipeSelector> folderSelect;
	
	ArrayList<File> selectedFolders;
	Intent i;
	
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.folder_selector);
		
		i = getIntent();
		
		selectedFolders = new ArrayList<File>();
		
		wipeSelect = new ArrayList<WipeSelector>();
		wipeSelect.add(
				new WipeSelector(
					getResources().getString(R.string.KEY_NONE),
					ITCConstants.Wipe.NONE,
					true
				)
		);
		wipeSelect.add(
				new WipeSelector(
					getResources().getString(R.string.KEY_WIPE_WIPECONTACTS), 
					ITCConstants.Wipe.CONTACTS, 
					false
				)
		);
		wipeSelect.add(
				new WipeSelector(
					getResources().getString(R.string.KEY_WIPE_WIPEPHOTOS), 
					ITCConstants.Wipe.PHOTOS, 
					false
				)
		);
		wipeSelect.add(
				new WipeSelector(
					getResources().getString(R.string.KEY_WIPE_CALLLOG),
					ITCConstants.Wipe.CALLLOG,
					false
				)
		);
		wipeSelect.add(
				new WipeSelector(
					getResources().getString(R.string.KEY_WIPE_SMS),
					ITCConstants.Wipe.SMS,
					false
				)
		);
		wipeSelect.add(
				new WipeSelector(
					getResources().getString(R.string.KEY_WIPE_CALENDAR),
					ITCConstants.Wipe.CALENDAR,
					false
				)
		);
		
		wipeSelect.add(
				new WipeSelector(
						getResources().getString(R.string.KEY_WIPE_SDCARD),
						ITCConstants.Wipe.SDCARD,
						false
				)
		);
		
		new FolderIterator();
		folderSelect = new ArrayList<WipeSelector>();
		folderSelect = FolderIterator.getFolderList(this);
		
		for(WipeSelector ws : folderSelect) {
			wipeSelect.add(ws);
		}
		
		wipeList = (ListView) findViewById(R.id.folderSelectionHolder);
		wipeList.setAdapter(new WipeArrayAdaptor(this, wipeSelect));
		
		/*
		if(i.hasExtra("selectedFolders")) {
			selectedWipes.clear();
			selectedWipes = (ArrayList<File>) i.getSerializableExtra("selectedFolders");
			
			// run through the folders and set them as selected if they are indeed selected
			if(!selectedWipes.isEmpty()) {
				for(WipeSelector w : wipeSelect) {
					if(selectedWipes.contains(w.getFilePath())) {
						w.setSelected(true);
					}
				}
			}
		}
		*/
		confirmSelection = (Button) findViewById(R.id.confirmSelection);		
		confirmSelection.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		if(v == confirmSelection) {
			/*
			selectedWipes.clear();
			for(WipeSelector w : wipeSelect) {
				if(w.getSelected() && !selectedFolders.contains(w.getFilePath())) {
					selectedFolders.add(w.getFilePath());
				}
			}
			

			i.putExtra("selectedWipes", selectedWipes);
			setResult(RESULT_OK,i);

			*/
			finish();
		}
	}

}