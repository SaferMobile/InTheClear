package org.safermobile.clear.micro.apps;

import org.safermobile.clear.micro.L10nResources;

public class LocaleManager {

	public final static String DEFAULT_LOCALE = "en-US";

	private static L10nResources lResources;
	
	public static L10nResources getResources ()
	{
		if (lResources != null)
			return lResources;
		else
		{
		
			String locale = System.getProperty("microedition.locale");
			
			if (locale == null)
				lResources = L10nResources.getL10nResources(DEFAULT_LOCALE);
			else if (locale.startsWith("en"))
				lResources = L10nResources.getL10nResources(DEFAULT_LOCALE);
			//else if locale is arabic, spanish, etc
			//  return L10nResources.getL10nResources("ar");
			else
				lResources = L10nResources.getL10nResources(DEFAULT_LOCALE);
	
			return lResources;
		}
	}
}
