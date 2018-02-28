package org.miri.camcapture.impl;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.miri.camcapture.api.BotCommands;
import org.miri.camcapture.api.ICamera;
import org.miri.camcapture.api.ICameraBot;
import org.miri.camcapture.api.ITelegram;
import org.miri.camcapture.api.TelegramConstants;
import org.miri.camcapture.exceptions.BotTokenAccessException;
import org.miri.camcapture.exceptions.CameraException;
import org.miri.camcapture.exceptions.FileAccessException;
import org.miri.camcapture.exceptions.TelegramException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Used to communicate with the telegram bot.
 * @author Miri Yehezkel
 *
 */
public enum Telegram implements ITelegram {
	INSTANCE;
	private final Logger logger = LoggerFactory.getLogger(Telegram.class);
	private String botURL;
	private ICameraBot bot = CameraBot.INSTANCE;
	private ICamera camera = DefaultCamera.INSTANCE;;
	private int lastOffset = 0;
	
	
	/**
	 * Retrieves incoming updates from Telegram bot.<br>
	 * Uses {@link HttpGet} to fetch updates from URL and executes incoming commands.
	 */
	public void getUpdates() throws TelegramException, BotTokenAccessException, IOException {
		String url = getBotURL() + TelegramConstants.GET_UPDATES_URL + "?offset=" + (lastOffset);
		//fetches updates from URL
		HttpResponse response = fetchUpdates(url);
		StatusLine statusLine = response.getStatusLine();
		if (! isValidStatusCode(statusLine))
			throw new TelegramException("Status line not valid (" + 
				statusLine.getStatusCode() + ")", new Throwable(statusLine.getReasonPhrase()));
		//reads JSON response
		JsonArray results = Json.createReader(response.getEntity()
				.getContent()).readObject()
				.getJsonArray(TelegramConstants.RESULT_FIELD);
		//executes incoming commands
		if(results.size() > 0)
			executeResponse(results);
	}
	
	/**
	 * Forwards the retrieved command to the right method.<br>
	 * Defaults to sending message to chat (if retrieved command isn't listed).
	 * @throws BotTokenAccessException 
	 * @throws FileAccessException 
	 * @throws TelegramException 
	 */
	public void executeCommand(String chatId, String command) 
			throws IOException, CameraException, BotTokenAccessException, FileAccessException, TelegramException  {
		command = command.substring(1, command.length() -1); //removes quotation marks from command
		//TODO remove print
		System.out.printf("executeCommand(%s, %s) thread = %s \n", chatId, command,
				Thread.currentThread().getName());
		switch (command) {
			case BotCommands.IMAGE:
				sendImage(chatId);
				break;
	
			case BotCommands.VIDEO:
				sendVideo(chatId);
				break;
				
			case BotCommands.DETECT_ON:
				MotionDetector.INSTANCE.start(chatId);
				break;
				
			case BotCommands.DETECT_OFF:
				MotionDetector.INSTANCE.stop();
				break;
			default:
				sendMessage(chatId, "Command not recognized. Please speak Bot to me.");
				break;
		}
	}
	
	

	/**
	 * Sends a text message to given chatId. <br>
	 * Uses a {@link HttpPost} object to send a POST request to Telegram with requested method fields.
	 * @param chatId chat to send message to
	 * @param content message to send
	 * @throws BotTokenAccessException 
	 * @throws IOException if unable to connect to telegram
	 */
	public void sendMessage(String chatId, String msg) throws IOException, BotTokenAccessException {
		//adds chat id and message text to HTTP request
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.addTextBody(TelegramConstants.CHAT_ID_FIELD, chatId);
		builder.addTextBody(TelegramConstants.TEXT_FIELD, msg);
		sendPostRequest(getBotURL() + TelegramConstants.SEND_MESSAGE_URL, builder);
	}

	/**
	 * Sends a captured image to given chatId. <br>
	 * Uses a {@link HttpPost} object to send a POST request to Telegram with requested method fields.
	 * @param chatId chat to send image to
	 * @param capturefile file of image to send
	 * @throws BotTokenAccessException 
	 * @throws IOException if unable to connect to telegram
	 * @throws CameraException 
	 * @throws FileAccessException 
	 */
	public void sendImage(String chatId) throws CameraException, BotTokenAccessException, 
														IOException, FileAccessException {
		File image = camera.saveImage(chatId);
		sendImage(chatId, image);
	}
	
	public void sendImage(String chatId, File image) throws IOException, BotTokenAccessException {
		sendMessage(chatId, "Sending image...");
		//composing attributes for POST request
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.addTextBody(TelegramConstants.CHAT_ID_FIELD, chatId);
		builder.addBinaryBody(TelegramConstants.PHOTO_FIELD, image);
		//sending POST request to URL with attributes
		HttpResponse response = sendPostRequest(getBotURL() + TelegramConstants.SEND_PHOTO_URL, builder);
		logInvalidResponseStatus("Unable to send image", response.getStatusLine(), chatId);
	}
	
	
	/**
	 * Sends a captured video to given chatId. <br>
	 * Uses a {@link HttpPost} object to send a POST request to Telegram with requested method fields.
	 * @param chatId chat to send video to
	 * @param capturefile file of video to send
	 * @throws IOException if unable to connect to telegram
	 * @throws CameraException 
	 * @throws BotTokenAccessException 
	 * @throws FileAccessException 
	 * @throws TelegramException 
	 */
	public void sendVideo(String chatId) throws IOException, CameraException, BotTokenAccessException, TelegramException, FileAccessException {
		sendMessage(chatId, "Sending video...");
		//composing attributes for POST request
		File video = camera.saveVideo(chatId);
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.addTextBody(TelegramConstants.CHAT_ID_FIELD, chatId);
		builder.addBinaryBody(TelegramConstants.VIDEO_FIELD, video);
		//sending POST request to URL with attributes
		HttpResponse response = sendPostRequest(getBotURL() + TelegramConstants.SEND_VIDEO_URL, builder);
		logInvalidResponseStatus("Unable to send video", response.getStatusLine(), chatId);
	}
	
	
	
	/*
	 * Private Class Methods
	 */
	
	
	/**
	 * Retrieves bot URL.
	 * @return
	 * @throws BotTokenAccessException
	 */
	private String getBotURL() throws BotTokenAccessException {
		if (botURL == null || botURL.isEmpty())
			botURL = TelegramConstants.BASE_URL + bot.getBotToken();
		return botURL;
	}
	
	/**
	 * 
	 * @param url URL to send the HTTP request to. 
	 * @param builder a {@link MultipartEntityBuilder} object
	 * @return an {@link HttpResponse} object
	 * @throws IOException if unable to connect to telegram
	 */
	private HttpResponse sendPostRequest(String url, MultipartEntityBuilder builder) throws IOException {
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(builder.build());
		HttpClient client = HttpClientBuilder.create().build();
		return client.execute(httpPost);
	}
	
	/**
	 * Fetches incoming updates. Returns a {@link HttpResponse}. <br>
	 * Uses a {@link HttpGet} object to send a GET request to Telegram.
	 * @return a HttpResponse object.
	 * @throws IOException if unable to connect to telegram
	 */
	private HttpResponse fetchUpdates(String url) throws IOException {
		//TOOD remove
		/*
		if (lastOffset != 0)
			url += "?offset=" + (lastOffset+1);*/
		HttpGet httpGet = new HttpGet(url);
		HttpClient client = HttpClientBuilder.create().build();
		HttpResponse response = client.execute(httpGet);
		return response;
	}
	
	/**
	 * Executes response from retrieved updates.<br>
	 * Uses a {@link JsonObject} object to extract the sent command and chatId from response. 
	 *  Activates command on bot.
	 * @param results a JasonArray object
	 * @throws IOException if unable to connect to telegram
	 */
	private void executeResponse(JsonArray results) {
		logger.trace("Got {} updates", results.size());
		for(int i=0; i < results.size(); i++) {
			JsonObject jObject = results.getJsonObject(i).getJsonObject(TelegramConstants.MESSAGE_FIELD);
			String command = jObject.getJsonString(TelegramConstants.TEXT_FIELD).toString();
			int chatId = jObject.getJsonObject(TelegramConstants.CHAT_FIELD).getInt(TelegramConstants.ID_FIELD);
			try {
				CompletableFuture.anyOf(executeCommand(chatId+"", command));
			} catch (Exception exc) {
				logger.error(exc.getMessage(), exc);
			}
		}
		lastOffset = results.getJsonObject(results.size()-1).getInt("update_id") + 1;
	}
	
	
	
	/**
	 * Logs error message if statusLine is invalid.
	 * @param statusLine a {@link StatusLine} object to check
	 * @param errorMessage message to log if status code is invalid
	 */
	private void logInvalidResponseStatus(String logMessage, StatusLine statusLine, String chatId) {
		if (! isValidStatusCode(statusLine)) {
			logger.error("{} - {}, (chatId {})", logMessage, statusLine.getReasonPhrase(), chatId);
		}
	}
	
	/**
	 * Checks if StatusLine is valid.
	 * @param statusLine StatusLine to check
	 * @return {@code true} if status line is valid, {@code false} otherwise
	 */
	private boolean isValidStatusCode(StatusLine statusLine) {
		return statusLine.getStatusCode() == 200;
	}
	
	
}
