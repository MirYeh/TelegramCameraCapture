package org.miri.camcapture.api;

import java.io.File;
import java.io.IOException;

import org.miri.camcapture.exceptions.BotTokenAccessException;
import org.miri.camcapture.exceptions.CameraException;
import org.miri.camcapture.exceptions.FileAccessException;
import org.miri.camcapture.exceptions.TelegramException;


/**
 * Defines the methods available to communicate with the Telegram bot. <br>
 * Supports checking for updates from users and sending users messages, photos and videos.
 * @author Miri Yehezkel
 * @see <a href="https://telegram.org/">telegram.org</a>
 *
 */
public interface ITelegram {
	
	/**
	 * Retrieves incoming updates from Telegram bot and executes incoming commands.
	 * @throws IOException if unable to connect to telegram
	 * @throws TelegramException is unable to retrieve updates from telegram
	 * @throws BotTokenAccessException if unable to access bot token
	 */
	void getUpdates() throws TelegramException, BotTokenAccessException, IOException;
	
	/**
	 * Executes retrieved command.
	 * @param chatId Id of telegram chat
	 * @param command String sent
	 * @throws IOException if unable to connect to telegram
	 * @throws CameraException if unable to complete capture
	 */
	void executeCommand(String chatId, String command) 
			throws IOException, CameraException, BotTokenAccessException, FileAccessException, TelegramException;
	
	/**
	 * Sends a text message to given chatId.
	 * @param chatId chat to send message to
	 * @param msg String to send
	 * @throws IOException if unable to connect to telegram 
	 * @throws BotTokenAccessException if unable to access bot token
	 */
	void sendMessage(String chatId, String msg) throws IOException, BotTokenAccessException;
	
	/**
	 * Sends a captured image to given chatId.
	 * @param chatId chat to send image to
	 * @throws IOException if unable to connect to telegram
	 * @throws BotTokenAccessException
	 */
	void sendImage(String chatId) throws CameraException, BotTokenAccessException, IOException, FileAccessException;
	
	/**
	 * Sends a given image to chatId.
	 * @param chatId chat to send image to
	 * @param image image to send
	 * @throws IOException if unable to connect to telegram
	 * @throws CameraException 
	 * @throws BotTokenAccessException
	 */
	void sendImage(String chatId, File image) throws IOException, CameraException, BotTokenAccessException;
	
	/**
	 * Sends a captured video to given chatId.
	 * @param chatId chat to send video to
	 * @throws IOException if unable to connect to telegram
	 */
	void sendVideo(String chatId) throws IOException, CameraException, BotTokenAccessException, TelegramException, FileAccessException;
	
	
	
}
