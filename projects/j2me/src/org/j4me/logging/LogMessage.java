package org.j4me.logging;

/**
 * A message logged by the application.
 */
public class LogMessage
{
	/**
	 * The level of this log message.
	 * 
	 * @see Level
	 */
	public Level level;

	/**
	 * The <code>System.cucurrentTimeMillis</code> at the time the message was
	 * logged.
	 */
	public long time;
	
	/**
	 * The message logged.
	 */
	public String message;
	
	/**
	 * Constructs a log message.
	 * 
	 * @param level is the severity of the log.
	 * @param message is the text of the log message.
	 */
	protected LogMessage (Level level, String message)
	{
		setLogMessage( level, message );
	}
	
	/**
	 * Replaces the contents of a log message.
	 * 
	 * @param level is the severity of the log.
	 * @param message is the text of the log message.
	 */
	protected void setLogMessage (Level level, String message)
	{
		this.level = level;
		this.message = message;
		this.time = System.currentTimeMillis();
	}
	
	/**
	 * The message as it will appear in the log.
	 */
	public String toString ()
	{
		return "[" + level + "] " + message;
	}
}
