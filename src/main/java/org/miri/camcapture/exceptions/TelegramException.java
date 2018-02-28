package org.miri.camcapture.exceptions;

public class TelegramException extends Exception {
	private static final long serialVersionUID = -7210276854525333234L;
	
	public TelegramException(String msg) {
		super(msg);
	}
	
	public TelegramException(String msg, Throwable throwable) {
		super(msg, throwable);
	}
	
}
