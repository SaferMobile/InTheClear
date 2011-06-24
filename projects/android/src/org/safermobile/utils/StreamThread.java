package org.safermobile.utils;

import java.io.IOException;
import java.io.InputStream;

import android.util.Log;

/**
 * Created by IntelliJ IDEA.
 * User: kevin
 * Date: 4/1/11
 * Time: 3:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class StreamThread extends Thread
{
	InputStream i;
	StreamUpdate update;

	StreamThread(InputStream i)
	{
		this.i = i;
	}

	StreamThread(InputStream i, StreamUpdate update)
	{
		this.i = i;
		this.update = update;
	}

	@Override
	public void run()
	{
		try
		{
			byte[] readBuffer = new byte[512];
			int readCount = -1;
			while ((readCount = i.read(readBuffer)) > 0)
			{
				String readString = new String(readBuffer, 0, readCount);
				update.update(readString);
			}
		}
		catch (IOException e)
		{
			Log.e(ShellUtils.TAG, "error reading shell stream", e);
			
		}
	}

	public String dump()
	{
		if(update instanceof StringBufferStreamUpdate)
		{
			return ((StringBufferStreamUpdate)update).dump();
		}

		return null;
	}

	public interface StreamUpdate
	{
		public abstract void update(String val);
	}

	public class StringBufferStreamUpdate implements StreamUpdate
	{
		StringBuilder sb = new StringBuilder();

		@Override
		public void update(String val)
		{
			sb.append(val);
		}

		public String dump()
		{
			return sb.toString();
		}
	}
}