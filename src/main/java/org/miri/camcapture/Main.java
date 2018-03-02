package org.miri.camcapture;

import org.miri.camcapture.api.ITelegram;
import org.miri.camcapture.exceptions.BotTokenAccessException;
import org.miri.camcapture.impl.Telegram;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Checks for updates from users.
 * @author Miri Yehezkel
 *
 */
public class Main {
	private final Logger logger = LoggerFactory.getLogger(Main.class);
	private ITelegram telegram = Telegram.INSTANCE;
	
	/**
	 * Retrieves updates from telegram.
	 */
	public void getUpdates() {
		while(true) {
			try {
				telegram.getUpdates();
				Thread.sleep(1000);
			} catch (BotTokenAccessException exc) {
				//unable to proceed without bot token
				logger.error(exc.getMessage(), exc);
				System.exit(1);
			} catch (Exception exc) {
				logger.error(exc.getMessage(), exc);
				exc.printStackTrace();
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {}
			}
		}
	}
	
	public static void main(String[] args) {
		new Main().getUpdates();
	}
	
	
}
