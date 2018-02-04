package org.cameracapture.telegram.miri.exceptions;

public class TelegramCallsException extends Exception {
	private static final long serialVersionUID = -7210276854525333234L;
	
	public TelegramCallsException(String msg) {
		super(msg);
	}
	
	public TelegramCallsException(String msg, Throwable throwable) {
		super(msg, throwable);
	}
	
}
