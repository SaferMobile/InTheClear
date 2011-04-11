/* Copyright (c) 2011, SaferMobile/MobileActive - https://safermobile.org */
/* See LICENSE for licensing information */
package org.safermobile.micro.utils;

public class Logger {

	public static void debug(final String tag, final String message) {
	  //#if !release.build
		System.out.print('[');
		System.out.print(tag);
		System.out.print(']');
		System.out.print(' ');
	    System.out.print(message);
	    System.out.println();
	    //#endif
	  }
	
	public static void error(final String tag, final String message, Exception e) {
	    //#if !release.build
		debug(tag, message + ": " + e.getMessage());
	  //  e.printStackTrace();
	    //#endif
	  }
}
