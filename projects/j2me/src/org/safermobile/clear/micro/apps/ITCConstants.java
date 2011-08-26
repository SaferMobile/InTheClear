package org.safermobile.clear.micro.apps;

public interface ITCConstants {


	public final static String TAG = "Panic";
	public final static int DEFAULT_RESEND_TIMEOUT = 60000;
	public final static String PANIC_PREFS_DB = "panicprefs";
	
	
	public final static String PREFS_KEY_RECIPIENT = "user.recp";
	public final static String PREFS_KEY_NAME = "user.name";
	public final static String PREFS_KEY_MESSAGE = "user.msg";
	public final static String PREFS_KEY_LOCATION = "user.loc";
	
	public final static String PREFS_KEY_WIPE_CONTACTS = "wipe.contacts";
	public final static String PREFS_KEY_WIPE_EVENTS = "wipe.events";
	public final static String PREFS_KEY_WIPE_PHOTOS = "wipe.photos";
	public final static String PREFS_KEY_WIPE_ALL_FILES = "wipe.allfiles";
	
	public final static String PREFS_KEY_ONE_TOUCH_PANIC = "panic.onetouch";
	
	public final static int SCREEN_WIDTH_LARGE = 320;
	
	public final static String [][] LOCALES = {{"en","English"}, {"ar","لعربية Arabic"}, {"fa","ارسی Farsi"}};
}
