package org.safermobile.intheclear.sms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.util.Log;

public class Utils {


		 public static String readString (InputStream stream)
		    {
		    	String line = null;
		    
		    	StringBuffer out = new StringBuffer();
		    	
		    	try {
			    	BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

					while ((line = reader.readLine()) != null)
					{
						out.append(line);
						out.append('\n');
						
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return out.toString();
		    	
		    }
		/*
		 * Load the log file text
		 */
		 public static String loadTextFile (File file)
		    {
		    	String line = null;
		    
		    	StringBuffer out = new StringBuffer();
		    	
		    	try {
			    	BufferedReader reader = new BufferedReader((new FileReader(file)));

					while ((line = reader.readLine()) != null)
					{
						out.append(line);
						out.append('\n');
						
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return out.toString();
		    	
		    }
		 
		 public static String loadAssetText (Context context, String path) throws IOException
		 {
			  InputStream is = context.getAssets().open(path);

	          // We guarantee that the available method returns the total
	          // size of the asset...  of course, this does mean that a single
	          // asset can't be more than 2 gigs.
	          int size = is.available();

	          // Read the entire asset into a local byte buffer.
	          byte[] buffer = new byte[size];
	          is.read(buffer);
	          is.close();

	          // Convert the buffer into a string.
	          String text = new String(buffer);
	          
	          return text;
		 }

			/*
			 * Load the log file text
			 */
			 public static boolean saveTextFile (File file, String contents, boolean append)
			    {
				 	
			    	try {
			    		
			    		//make sure folders all exist
			    		if (!file.exists())
			    			new File(file.getParent()).mkdirs();
			    		
			    		//now write the file
			    		
			    		 FileWriter writer = new FileWriter( file, append );
	                     writer.write( contents );
	                     
	                     writer.close();

	                     
			    		
			    		return true;
				    	
					} catch (IOException e) {
						
						Log.d("Utils", "error writing file: " + file.toString(), e);
					
						return false;
					}
					
					
			    	
			    }
		
}
