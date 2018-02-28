package org.miri.camcapture.impl;

import org.miri.camcapture.api.ICameraBot;
import org.miri.camcapture.exceptions.BotTokenAccessException;
import org.miri.camcapture.exceptions.FileAccessException;
import org.miri.camcapture.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the Telegram bot.
 * @author Miri Yehezkel
 *
 */
public enum CameraBot implements ICameraBot {
	INSTANCE;
	private final String botTokenFileName 	= 	"CameraCaptureBotToken";
	private final Logger logger = LoggerFactory.getLogger(CameraBot.class);
	private String botToken;
	
	
	/**
	 * Retrieves the bot token from file.
	 * @return A String representing the bot's token
	 * @throws BotTokenAccessException if unable to get token
	 */
	@Override
	public String getBotToken() throws BotTokenAccessException {
		if (botToken == null)
			readToken();
		return botToken;
	}
	
	
	/**
	 * Reads bot token from file.
	 * @throws BotTokenAccessException if unable to read token from file
	 */
	private void readToken() throws BotTokenAccessException {
		try {
			botToken = FileUtil.readLine(botTokenFileName);
			logger.trace("read bot token from file");
		} catch(FileAccessException exc) {
			throw new BotTokenAccessException(exc.getMessage(), exc.getCause());
		}
	}
}
