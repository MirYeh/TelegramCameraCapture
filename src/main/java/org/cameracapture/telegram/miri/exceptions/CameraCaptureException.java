package org.cameracapture.telegram.miri.exceptions;

public class CameraCaptureException extends Exception {
	private static final long serialVersionUID = 7181440260720031517L;
	
	public CameraCaptureException(String msg) {
		super(msg);
	}
	
	public CameraCaptureException(String msg, Throwable throwable) {
		super(msg, throwable);
	}
	
}
