package org.miri.camcapture.exceptions;

/**
 * Thrown if unable to access file.
 * @author Miri Yehezkel
 *
 */
public class BotTokenAccessException extends Exception {
	private static final long serialVersionUID = -7450203586300997943L;
	
	public BotTokenAccessException(String msg) {
		super(msg);
	}
	
	public BotTokenAccessException(String msg, Throwable throwable) {
		super(msg, throwable);
	}
	
}
