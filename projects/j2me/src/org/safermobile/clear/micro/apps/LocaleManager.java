package org.safermobile.clear.micro.apps;

import org.safermobile.clear.micro.L10nConstants;
import org.safermobile.clear.micro.L10nResources;

public class LocaleManager {

	public static String DEFAULT_LOCALE = L10nConstants.locales.EN_US;

	private static L10nResources lResources;
	
	private static String mLocale;
	
	public static void setDefaultLocale (String defLoc)
	{
		DEFAULT_LOCALE = defLoc;
	}
	
	public static void setCurrentLocale (String locale)
	{
		mLocale = locale;
	}

	public static L10nResources getResources ()
	{
		if (mLocale == null)
			mLocale = System.getProperty("microedition.locale");
		
		if (mLocale == null)
			lResources = L10nResources.getL10nResources(DEFAULT_LOCALE);
		else if (mLocale.startsWith("en"))
			lResources = L10nResources.getL10nResources(DEFAULT_LOCALE);
		else if (mLocale.startsWith("ar"))
			lResources = L10nResources.getL10nResources(L10nConstants.locales.AR_EG);		
		else if (mLocale.startsWith("fa"))
			lResources = L10nResources.getL10nResources(L10nConstants.locales.FA_IR);
		else
			lResources = L10nResources.getL10nResources(DEFAULT_LOCALE);

		return lResources;
		
	}
}
