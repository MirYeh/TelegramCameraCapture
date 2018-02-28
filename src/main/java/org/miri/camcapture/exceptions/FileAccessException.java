package org.miri.camcapture.exceptions;

/**
 * Thrown if unable to access file.
 * @author Miri Yehezkel
 *
 */
public class FileAccessException extends Exception {
	private static final long serialVersionUID = 884186926678238028L;
	
	public FileAccessException(String msg) {
		super(msg);
	}
	
	public FileAccessException(String msg, Throwable throwable) {
		super(msg, throwable);
	}
	
}
