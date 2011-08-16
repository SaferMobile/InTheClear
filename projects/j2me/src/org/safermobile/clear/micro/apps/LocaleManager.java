package org.safermobile.clear.micro.apps;

import org.safermobile.clear.micro.L10nConstants;
import org.safermobile.clear.micro.L10nResources;

public class LocaleManager {

	public static String DEFAULT_LOCALE = L10nConstants.locales.EN_US;

	private static L10nResources lResources;
	
	public static void setDefaultLocale (String defLoc)
	{
		DEFAULT_LOCALE = defLoc;
	}
	
	public static L10nResources getResources ()
	{
		if (lResources == null)
			lResources = L10nResources.getL10nResources(DEFAULT_LOCALE);

		return lResources;
		/*
		else
		{
		
			String locale = System.getProperty("microedition.locale");
			
			if (locale == null)
				lResources = L10nResources.getL10nResources(DEFAULT_LOCALE);
			else if (locale.startsWith("en"))
				lResources = L10nResources.getL10nResources(DEFAULT_LOCALE);
		//	else if (locale.startsWith("ar"))
			//	lResources = L10nResources.getL10nResources(L10nConstants.locales.AR_EG);		
			else
				lResources = L10nResources.getL10nResources(DEFAULT_LOCALE);
	
			return lResources;
		}*/
	}
}
