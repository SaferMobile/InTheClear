package org.safermobile.intheclear.screens;

import java.io.File;
import java.util.ArrayList;

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
	ListView folderList;
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
		
		new FolderIterator();
		folderSelect = new ArrayList<WipeSelector>();
		folderSelect = FolderIterator.getFolderList(this);
		
		folderList = (ListView) findViewById(R.id.folderSelectionHolder);
		folderList.setAdapter(new WipeArrayAdaptor(this, folderSelect));
		
		if(i.hasExtra("selectedFolders")) {
			selectedFolders.clear();
			selectedFolders = (ArrayList<File>) i.getSerializableExtra("selectedFolders");
			
			// run through the folders and set them as selected if they are indeed selected
			if(!selectedFolders.isEmpty()) {
				for(WipeSelector w : folderSelect) {
					if(selectedFolders.contains(w.getFilePath())) {
						w.setSelected(true);
					}
				}
			}
		}
		
		confirmSelection = (Button) findViewById(R.id.confirmSelection);		
		confirmSelection.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		if(v == confirmSelection) {
			for(WipeSelector w : folderSelect) {
				if(w.getSelected() && !selectedFolders.contains(w.getFilePath())) {
					selectedFolders.add(w.getFilePath());
				}
			}

			i.putExtra("selectedFolders", selectedFolders);
			setResult(RESULT_OK,i);
			finish();
		}
	}

}
