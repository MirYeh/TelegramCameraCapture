package org.miri.camcapture.exceptions;

/**
 * Thrown when unable to operate camera.
 * @author Miri Yehezkel
 *
 */
public class CameraException extends Exception {
	private static final long serialVersionUID = 7181440260720031517L;
	
	public CameraException(String msg) {
		super(msg);
	}
	
	public CameraException(String msg, Throwable throwable) {
		super(msg, throwable);
	}
	
}
