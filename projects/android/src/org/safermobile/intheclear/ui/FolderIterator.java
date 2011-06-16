package org.safermobile.intheclear.ui;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

public class FolderIterator {
	private static File pathToSDCard;
	final static int FOLDER = 5;
	private static SharedPreferences _sp;

	private static final String ITC = "[InTheClear:FolderIterator] ************************* ";
		
	public FolderIterator() {
		pathToSDCard = Environment.getExternalStorageDirectory();
	}
	
	public static ArrayList<WipeSelector> getFolderList(Context c) {
		ArrayList<WipeSelector> folders = new ArrayList<WipeSelector>();
		File[] folder = pathToSDCard.listFiles();
		_sp = PreferenceManager.getDefaultSharedPreferences(c);
		
		for(File f : folder) {
			if (f.isDirectory()) {
				boolean isSelected = false;
				WipeSelector theFolder = new WipeSelector(f.getName(), FOLDER, isSelected);
				theFolder.setFilePath(f);
				folders.add(theFolder);
			}
		}
		
		return folders;
	}
	
	public static ArrayList<File> updateListPreference() {
		ArrayList<File> folders = new ArrayList<File>();
		File[] folder = pathToSDCard.listFiles();
		
		for(File f : folder) {
			if(f.isDirectory()) {
				folders.add(f);
			}
		}
		return folders;
	}
}
