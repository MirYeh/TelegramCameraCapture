package org.miri.camcapture.api;

import org.miri.camcapture.exceptions.BotTokenAccessException;

/**
 * Defines the Telegram bot.
 * @author Miri Yehezkel
 *
 */
public interface ICameraBot {
	
	/**
	 * Retrieves the Telegram bot token.
	 * @return A String representing the bot's token
	 * @throws BotTokenAccessException if unable to retrieve token from file
	 */
	String getBotToken() throws BotTokenAccessException;
	
}
