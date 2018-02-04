package org.cameracapture.telegram.miri.bot;

import java.io.IOException;

import org.cameracapture.telegram.miri.TelegramCalls;
import org.cameracapture.telegram.miri.camera.CameraCapture;
import org.cameracapture.telegram.miri.exceptions.*;
import org.cameracapture.telegram.miri.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the telegram bot used to communicate with the camera.
 * @author Miri Yehezkel
 */
public class CameraCaptureBot {
	private static CameraCaptureBot instance; 
	private final String botTokenFileName 	= 	"CameraCaptureBotToken";
	private final Logger logger = LoggerFactory.getLogger(CameraCaptureBot.class);
	private static TelegramCalls telegramCalls;
	private static CameraCapture cameraCapture;
	private String botToken;
	
	/**
	 * Private constructor.
	 */
	private CameraCaptureBot() {
		logger.trace("Initialized CameraCaptureBot instance");
	}
	
	/**
	 * Initializes CameraCaptureBot.
	 * @return an initialized instance 
	 * @throws CameraCaptureException if unable to initiate camera capture
	 */
	public static CameraCaptureBot init() throws CameraCaptureException {
		if (instance == null) {
			synchronized (CameraCaptureBot.class) {
				if (instance == null) {
					instance = new CameraCaptureBot();
					telegramCalls = TelegramCalls.init();
					cameraCapture = CameraCapture.init();
				}
			}
		}
		return instance;
	}
	
	/**
	 * @return bot token.
	 * @throws FileAccessException if unable to get token
	 */
	public String getBotToken() throws FileAccessException {
		if (botToken == null)
			readToken();
		return botToken;
	}
	
	/**
	 * Handles commands received from bot.<br>
	 * Bot can either send a capture photo back to chat or send a message explaining the bot's function.
	 * @param chatId Id of the chat
	 * @param command command given
	 * @throws IOException if unable to connect to telegram
	 * @throws CameraCaptureException if unable to complete photo capture
	 * @see TelegramCalls#sendMessage(String, String)
	 * @see TelegramCalls#sendMessage(String, String)
	 */
	public void handleCommand(String chatId, String command) throws IOException {
		logger.trace("received command ({}) from chat id ({})", command, chatId);
		
		if (command.contains("/capture")) {
			telegramCalls.sendMessage(chatId, "Capturing image...");
			try { //send message to user if unable to capture image
				telegramCalls.sendPhoto(chatId, cameraCapture.capture(chatId));
			} catch (CameraCaptureException e) {
				telegramCalls.sendMessage(chatId, "Unable to capture image: " + e.getMessage());
				logger.error("Unable to send captured image to chatId " + chatId, e);
			}
		}
		else
			telegramCalls.sendMessage(chatId, explainFunction());
	}
		
	/**
	 * @return a message explaining the bot's function.
	 */
	private static String explainFunction() {
		return "@CameraCaptureBot allows you to capture images from a webcam.\n" 
				+ "Use the command \"/capture\" to capture an image.\n"
				+ "For more info, check out github.com/MirYeh/telegram-camera-capture.";
	}
	
	/**
	 * Reads bot token from file.
	 * @throws FileAccessException if unable to read token from file
	 * @see CameraCaptureBot#botTokenFileName
	 */
	private void readToken() throws FileAccessException {
		botToken = FileUtil.readLine(botTokenFileName);
		logger.trace("read bot token from file");
	}
	
}
