package org.miri.camcapture.exceptions;

public class NoSuchFileException extends Exception {
	private static final long serialVersionUID = -3091479662014849959L;
	
	public NoSuchFileException(String msg) {
		super(msg);
	}
	
	public NoSuchFileException(String msg, Throwable throwable) {
		super(msg, throwable);
	}
	
}
