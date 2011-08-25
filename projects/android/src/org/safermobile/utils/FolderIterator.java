package org.safermobile.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.safermobile.intheclear.ITCConstants;
import org.safermobile.intheclear.ui.WipeSelector;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

public class FolderIterator {
	public static File pathToSDCard;
	private static SharedPreferences _sp;
		
	public FolderIterator() {
		pathToSDCard = Environment.getExternalStorageDirectory();
	}
	
	public static ArrayList<File> getFoldersOnSDCard() {
		File[] folder = pathToSDCard.listFiles();
		ArrayList<File> folders = new ArrayList<File>();
		
		folders.add(pathToSDCard);
		
		for(File f : folder) {
			if(f.isDirectory() && f.canWrite()) {
				folders.add(f);
			}
		}
		
		return folders;
	}
}
