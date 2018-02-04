package org.cameracapture.telegram.miri;

import java.io.IOException;

import org.cameracapture.telegram.miri.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Starts program as a thread checking for updates from users.
 * <br><br><b>NOTE: Program stops if it's unable to initiate bot, unable<br>to retrieve 
 * bot token or unable to read last update Id.</b>
 * @author Miri Yehezkel
 */
public class TelegramCameraCapture implements Runnable {
	private final Logger logger = LoggerFactory.getLogger(TelegramCameraCapture.class);
	private TelegramCalls telegramCalls = TelegramCalls.init();
	
	/**
	 * Retrieves updates from telegram users.
	 */
	@Override
	public void run() {
		while(true) {
			try {
				telegramCalls.getUpdates();
			} catch (IOException | FileAccessException | TelegramCallsException e) {
				logger.error("Failed to retrieve updates from telegram", e);
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Starts a CameraCaptureTelegram thread.
	 */
	public static void main(String[] args) {
		Thread thread = new Thread(new TelegramCameraCapture());
		thread.start();
	}
	
}
