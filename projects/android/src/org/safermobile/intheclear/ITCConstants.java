package org.safermobile.intheclear;

import android.app.Activity;
import android.provider.CallLog;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.ContactsContract.Contacts;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Video;

public class ITCConstants {
	public static class Wizard {
		public static final String ACTION = "org.safermobile.intheclear.Wizard";
		public static final String WIZARD_ACTION = "wizardAction";
		public static final int LAUNCH_WIPE_SELECTOR = 1;
		public static final int SAVE_PREFERENCE_DATA = 2;
	}
	
	public static class Wipe {
		public static final int NONE = 0;
		public static final int CONTACTS = 1;
		public static final int PHOTOS = 2;
		public static final int CALLLOG = 3;
		public static final int SMS = 4;
		public static final int CALENDAR = 5;
		public static final int SDCARD = 6;
	}
	
	public static class Panic {
		public static final int RETURN = Activity.RESULT_OK;
	}
	
	public static class Results {
		public static final int PREFERENCES_UPDATED = 1;
		public static final int SETUP_WIZARD = 2;
		public static final int OVERRIDE_WIPE_PREFERENCES = 3;
		public static final int RETURN_FROM_PANIC = 4;
		public static final int A_OK = 1;
		public static final int FAIL = -1;
	}
	
	public static class ContentTargets {
		public static class CONTACT {
			public static final String[] STRINGS = {
				Contacts.DISPLAY_NAME
			};
			
			public static final String[] FILES = {
				Photo.PHOTO,
				
			};
		}
		
		public static class PHONE_NUMBER {
			public static final String[] STRINGS = {
				Phone.DISPLAY_NAME,
				Phone.NUMBER,
				Phone.DATA,
				Phone.SYNC1,
				Phone.SYNC2,
				Phone.SYNC3,
				Phone.SYNC4
			};
		}
		
		public static class EMAIL {
			public static final String[] STRINGS = {
				Email.DISPLAY_NAME,
				Email.DATA,
				Email.SYNC1,
				Email.SYNC2,
				Email.SYNC3,
				Email.SYNC4
			};
		}
			
		public static class IMAGE {
			public static final String[] STRINGS = {
				Images.Media.DISPLAY_NAME,
				Images.Media.TITLE,
				Images.Media.DATE_ADDED,
				Images.Media.DATE_TAKEN,
				Images.Media.DATE_MODIFIED,
				Images.Media.DESCRIPTION,
				Images.Media.LATITUDE,
				Images.Media.LONGITUDE,
				Images.Media.MINI_THUMB_MAGIC
			};
			
			public static final String[] FILES = {
				Images.Media.DATA
			};
		}
		
		public static class VIDEO {
			public static final String[] STRINGS = {
				Video.Media.ALBUM,
				Video.Media.ARTIST,
				Video.Media.CATEGORY,
				Video.Media.DATE_TAKEN,
				Video.Media.DESCRIPTION,
				Video.Media.LANGUAGE,
				Video.Media.LATITUDE,
				Video.Media.LONGITUDE,
				Video.Media.MINI_THUMB_MAGIC,
				Video.Media.TAGS,
				Video.Media.DATE_ADDED,
				Video.Media.DATE_MODIFIED,
				Video.Media.DISPLAY_NAME,
				Video.Media.TITLE
			};
			
			public static final String[] FILES = {
				Video.Media.DATA
			};
		}
		
		public static class IMAGE_THUMBNAIL {
			public static final String[] STRINGS = {
				Images.Thumbnails.IMAGE_ID
			};
			
			public static final String[] FILES = {
				Images.Thumbnails.DATA
			};
		}
		
		public static class VIDEO_THUMBNAIL {
			public static final String[] STRINGS = {
				Video.Thumbnails.VIDEO_ID
			};
			
			public static final String[] FILES = {
				Video.Thumbnails.DATA
			};
		}
		
		public static class CALL_LOG {
			public static final String[] STRINGS = {
				CallLog.Calls.NUMBER,
				CallLog.Calls.CACHED_NAME,
				CallLog.Calls.DATE,
				CallLog.Calls.DURATION,
				CallLog.Calls.CACHED_NUMBER_LABEL,
				CallLog.Calls.CACHED_NUMBER_TYPE,
				CallLog.Calls.TYPE
			};
		}
		
		public static class SMS {
			public static final String[] STRINGS = {
				"thread_id",
				"address",
				"date",
				"person",
				"subject",
				"body"
			};
		}
		
		public static class CALENDAR {
			public static final String[] STRINGS = {
				"displayName",
				"sync_events"
			};
		}
	}
	
	public static class Duriation {
		public static final long SPLASH = 3000L;
		public static final long COUNTDOWN = 6000L;
		public static final long COUNTDOWNINTERVAL = 1000L;
		public static final long CONTINUED_PANIC = 60000L;
	}
	
	public static class Preference {
		public static final String WIPERCAT = "DefaultWipers";
		public static final String DEFAULT_LANGUAGE = "DefaultLanguage";
		public static final String WIPE_SELECTOR = "wipe_selector";
		public static final String FOLDER_SELECTOR = "folder_selector";
		public static final String USER_DISPLAY_NAME = "UserDisplayName";
		public static final String USER_DISPLAY_LOCATION = "UserDisplayLocation";
		public static final String CONFIGURED_FRIENDS = "ConfiguredFriends";
		public static final String DEFAULT_PANIC_MSG = "DefaultPanicMsg";
		public static final String DEFAULT_WIPE_CONTACTS = "DefaultWipeContacts";
		public static final String DEFAULT_WIPE_PHOTOS = "DefaultWipePhotos";
		public static final String DEFAULT_WIPE_CALLLOG = "DefaultWipeCallLog";
		public static final String DEFAULT_WIPE_SMS = "DefaultWipeSMS";
		public static final String DEFAULT_WIPE_CALENDAR = "DefaultWipeCalendar";
		public static final String DEFAULT_WIPE_FOLDERS = "DefaultWipeFolders";
		public static final String DEFAULT_WIPE_FOLDER_LIST = "DefaultWipeFoldersList";
		public static final String DEFAULT_ONE_TOUCH_PANIC = "DefaultOneTouchPanic";
		public static final String DEFAULT_ADD_TO_HOMESCREEN = "DefaultHomescreenPanicButton";
		public static final String IS_VIRGIN_USER = "IsVirginUser";
	}
	
	public static class Log {
		public static final String ITC = "[InTheClear] ******************************** ";
	}
	
	public static final String UPDATE_UI = "updateUi";
	
	public static class PanicState {
		public static final int IN_COUNTDOWN = 1;
		public static final int IN_FIRST_SHOUT = 2;
		public static final int IN_WIPE = 3;
		public static final int IN_CONTINUED_PANIC = 4;
		public static final int AT_REST = 0;
	}
	
	public static final int SERVICE_START = 1;
	
	public static final int FormLength = 8;
}