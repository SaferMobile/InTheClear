package org.safermobile.intheclear;

import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.provider.ContactsContract.CommonDataKinds.Nickname;
import android.provider.ContactsContract.CommonDataKinds.Note;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;

public class ITCConstants {
	public static class Wipe {
		public static final int CONTACTS = 1;
		public static final int PHOTOS = 2;
		public static final int CALLLOG = 3;
		public static final int SMS = 4;
		public static final int CALENDAR = 5;
		public static final int FOLDER = 6;
	}
	
	public static class Results {
		public static final int RESULT_PREFERENCES_UPDATED = 1;
		public static final int SETUP_WIZARD = 2;
		public static final int SELECTED_FOLDERS = 3;
	}
	
	public static class ContentTargets {
		public static class CONTACT {
			public static final String[] STRINGS = {
				Data.SYNC1,
				Data.SYNC2,
				Data.SYNC3,
				Data.SYNC4,
				Phone.DISPLAY_NAME,
				Phone.NUMBER,
				Photo.PHOTO_ID,
				StructuredName.DISPLAY_NAME,
				StructuredName.GIVEN_NAME,
				StructuredName.MIDDLE_NAME,
				StructuredName.FAMILY_NAME,
				StructuredName.PREFIX,
				StructuredName.SUFFIX,
				Note.NOTE,
				StructuredPostal.FORMATTED_ADDRESS,
				StructuredPostal.STREET,
				StructuredPostal.CITY,
				StructuredPostal.NEIGHBORHOOD,
				StructuredPostal.REGION,
				StructuredPostal.POBOX,
				StructuredPostal.POSTCODE,
				StructuredPostal.COUNTRY,
				Nickname.NAME,
				Im.DATA,
				Im.PROTOCOL,
				Im.LABEL
			};
			
			public static final String[] BLOBS = {
				Photo.PHOTO,
				
			};
		}
			
		public static final String[] IMAGE = {
			android.provider.MediaStore.Images.Media.DATA,
			android.provider.MediaStore.Images.Media.SIZE,
			android.provider.MediaStore.Images.Media.DISPLAY_NAME,
			android.provider.MediaStore.Images.Media.MIME_TYPE,
			android.provider.MediaStore.Images.Media.TITLE,
			android.provider.MediaStore.Images.Media.DATE_ADDED,
			android.provider.MediaStore.Images.Media.DATE_MODIFIED,
			android.provider.MediaStore.Images.Media.DESCRIPTION
		};
		public static final String[] VIDEO = {
			android.provider.MediaStore.Video.VideoColumns.ALBUM,
			android.provider.MediaStore.Video.VideoColumns.ARTIST,
			android.provider.MediaStore.Video.VideoColumns.CATEGORY,
			android.provider.MediaStore.Video.VideoColumns.DATE_TAKEN,
			android.provider.MediaStore.Video.VideoColumns.DESCRIPTION,
			android.provider.MediaStore.Video.VideoColumns.LANGUAGE,
			android.provider.MediaStore.Video.VideoColumns.LATITUDE,
			android.provider.MediaStore.Video.VideoColumns.LONGITUDE,
			android.provider.MediaStore.Video.VideoColumns.MINI_THUMB_MAGIC,
			android.provider.MediaStore.Video.VideoColumns.TAGS,
			android.provider.MediaStore.Video.VideoColumns.DATA,
			android.provider.MediaStore.Video.VideoColumns.DATE_ADDED,
			android.provider.MediaStore.Video.VideoColumns.DATE_MODIFIED,
			android.provider.MediaStore.Video.VideoColumns.DISPLAY_NAME,
			android.provider.MediaStore.Video.VideoColumns.TITLE
		};
		public static final String[] IMAGE_THUMBNAIL = {
			android.provider.MediaStore.Images.Thumbnails.DATA,
			android.provider.MediaStore.Images.Thumbnails.IMAGE_ID
		};
		public static final String[] VIDEO_THUMBNAIL = {
			android.provider.MediaStore.Video.Thumbnails.DATA,
			android.provider.MediaStore.Video.Thumbnails.VIDEO_ID
		};
	}
	
	public static class Duriation {
		public static final long SPLASH = 3000L;
		public static final long COUNTDOWN = 5000L;
		public static final long COUNTDOWNINTERVAL = 1000L;
	}
	
	public static class Preference {
		public static final String WIPERCAT = "DefaultWipers";
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
		public static final String DEFAULT_WIPE_FOLDER_LIST = "DefaultWipeFoldersList";
		public static final int ONE_TOUCH = 7;
		public static final String DEFAULT_ONE_TOUCH_PANIC = "DefaultOneTouchPanic";
	}
	
	public static class Log {
		public static final String ITC = "[InTheClear] ******************************** ";
	}
	
	public static int FormLength = 8;
}
