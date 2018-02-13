package org.cameracapture.telegram.miri.bot;

import java.io.IOException;

import org.cameracapture.telegram.miri.exceptions.FileAccessException;

/**
 * Defines bot methods.
 * @author Miri Yehezkel
 *
 */
public interface ICameraBot {
	public String getBotToken() throws FileAccessException;
	public void handleCommand(String chatId, String command) throws IOException;
	
}
