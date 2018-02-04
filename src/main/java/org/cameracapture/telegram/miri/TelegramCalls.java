package org.cameracapture.telegram.miri;

import java.io.File;
import java.io.IOException;

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
import org.cameracapture.telegram.miri.bot.CameraCaptureBot;
import org.cameracapture.telegram.miri.exceptions.*;
import org.cameracapture.telegram.miri.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Performs telegram calls.<br>
 * Uses CameraCaptureBot to communicate with the telegram bot. Supports checking for updates
 *  from users and sending users messages and photos.
 * @author Miri Yehezkel
 * @see <a href="https://telegram.org/">telegram.org</a>
 */
public class TelegramCalls {
	private static TelegramCalls instance;
	private final String offsetFileName 	= 	"lastUpdateId";
	private static final Logger logger = LoggerFactory.getLogger(TelegramCalls.class);
	private static String baseUrl;
	private int lastUpdateId = 0;
	private static CameraCaptureBot bot;
	
	/**
	 * Initializes TelegramCalls.
	 * @return an initialized instance
	 */
	public static TelegramCalls init() {
		if (instance == null) {
			synchronized (TelegramCalls.class) {
				if (instance == null) {
					try {
						instance = new TelegramCalls();
						bot = CameraCaptureBot.init();
						baseUrl = Constants.BASE_URL + bot.getBotToken();
					} catch (CameraCaptureException | FileAccessException e) {
						logger.error(e.getMessage(), e);
						e.printStackTrace();
						logger.error("Terminating Program");
						System.exit(1);
					}
					logger.trace("Initialized TelegramCalls instance");
				}
			}
		}
		return instance;
	}
	
	/**
	 * Private constructor.
	 * @throws FileAccessException if unable to read last update Id
	 */
	private TelegramCalls() throws FileAccessException {
		readLastUpdateId();
	}	
	
	/**
	 * Reads last update Id.
	 * @throws FileAccessException if unable to read last update Id
	 */
	private void readLastUpdateId() throws FileAccessException {
		if (lastUpdateId == 0) {
			if (! FileUtil.isNewFileCreated(offsetFileName)) { //if not new file, read data
				String res = FileUtil.readLine(offsetFileName);
				lastUpdateId = Integer.valueOf(res);
				logger.trace("Read last updated Id ({})", lastUpdateId);
			}
		}
	}
	
	/**
	 * Writes last update Id for later use.
	 * @throws FileAccessException if unable to write last update Id
	 */
	private void writeLastUpdateId() throws FileAccessException {
		FileUtil.write(offsetFileName, lastUpdateId+"");
		logger.trace("Wrote last updated Id ({})", lastUpdateId);
	}
	
	/**
	 * Retrieves incoming updates from bot.
	 * @throws FileAccessException if unable to write last update Id
	 * @throws IOException if unable to get content from update entity
	 * @throws TelegramCallsException if call to telegram is not valid
	 */
	public void getUpdates() throws IOException, FileAccessException, TelegramCallsException {
		HttpResponse response = fetchUpdates();
		StatusLine statusLine = response.getStatusLine();
		if (! isValidStatusCode(statusLine))
			throw new TelegramCallsException("Status line not valid", 
						new Throwable(statusLine.getReasonPhrase()));
		JsonArray results = Json.createReader(response.getEntity()
				.getContent())
				.readObject()
				.getJsonArray(Constants.RESULT_FIELD);
		if (results.size() <= 0) //no new updates
			return;
		executeResponse(results);
	}
	
	/**
	 * Fetches incoming updates. Returns a {@link HttpResponse}.
	 * @return a HttpResponse object.
	 * @throws IOException
	 */
	private HttpResponse fetchUpdates() throws IOException {
		String url = baseUrl + Constants.GET_UPDATES;
		if (lastUpdateId != 0)
			url += "?offset=" + (lastUpdateId+1);
		HttpGet httpGet = new HttpGet(url);
		HttpClient client = HttpClientBuilder.create().build();
		HttpResponse response = client.execute(httpGet);
		return response;
	}
	
	/**
	 * Reads response from retrieved updates.<br>
	 * Forwards the retrieved commands to the bot.
	 * @param results a JasonArray object
	 * @throws IOException if unable to connect to telegram
	 * @throws FileAccessException if unable to write last update Id
	 * @see CameraCaptureBot#handleCommand(String, String)
	 */
	private void executeResponse(JsonArray results) throws IOException, FileAccessException {
		logger.trace("Got {} updates", results.size());
		for(int i=0; i < results.size(); i++) {
			JsonObject jObject = results.getJsonObject(i).getJsonObject(Constants.MESSAGE_FIELD);
			String command = jObject.getJsonString(Constants.TEXT_FIELD).toString();
			int chatId = jObject.getJsonObject(Constants.CHAT_FIELD).getInt(Constants.ID_FIELD);
			lastUpdateId++;
			bot.handleCommand(chatId+"", command);
		}
		lastUpdateId = results.getJsonObject(results.size()-1).getInt("update_id");
		writeLastUpdateId();
	}
	
	/**
	 * Sends a captured photo to given chatId.
	 * @param chatId chat to send capture photo to
	 * @param capturefile file of photo to send
	 * @throws IOException if unable to connect to telegram
	 */
	public void sendPhoto(String chatId, File capturefile) throws IOException {
		String url = baseUrl + Constants.SEND_PHOTO;
		HttpPost httpPost = new HttpPost(url);
		//adds chat id and photo file to http request
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.addTextBody(Constants.CHAT_ID_FIELD, chatId);
		builder.addBinaryBody(Constants.PHOTO_FIELD, capturefile);
		httpPost.setEntity(builder.build());
		//executes request
		HttpClient client = HttpClientBuilder.create().build();
		HttpResponse response = client.execute(httpPost);
		
		StatusLine statusLine = response.getStatusLine();
		if (! isValidStatusCode(statusLine))
			logger.error("sendPhoto(String, java.io.File): Unable to send photo to chatId " + chatId);
		
	}

	/**
	 * Sends a text message to given chatId.
	 * @param chatId chat to send message to
	 * @param messageText message to send
	 * @throws IOException if unable to connect to telegram
	 */
	public void sendMessage(String chatId, String messageText) throws IOException {
		String url = baseUrl + Constants.SEND_MESSAGE;
		HttpPost httpPost = new HttpPost(url);
		//adds chat id and message text to http request
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.addTextBody(Constants.CHAT_ID_FIELD, chatId);
		builder.addTextBody(Constants.TEXT_FIELD, messageText);
		//executes request
		httpPost.setEntity(builder.build());
		HttpClient client = HttpClientBuilder.create().build();
		HttpResponse response = client.execute(httpPost);

		StatusLine statusLine = response.getStatusLine();
		if (! isValidStatusCode(statusLine))
			logger.error("sendMessage(String, String): Unable to send message to chatId " + chatId);
	}
	
	/**
	 * Checks if StatusLine is valid.
	 * @param statusLine StatusLine to check
	 * @return {@code true} if status line is valid, {@code false} otherwise
	 */
	private boolean isValidStatusCode(StatusLine statusLine) {
		return statusLine.getStatusCode() == 200;
			
	}
	
	
	/**
	 * Telegram API String constants.
	 */
	public static class Constants {
		public static final String BASE_URL 			=	"https://api.telegram.org/bot"; 
		public static final String SEND_PHOTO 			=	"/sendPhoto";
		public static final String SEND_MESSAGE 		=	"/sendMessage";
		public static final String GET_UPDATES 			=	"/getUpdates";
		public static final String CHAT_ID_FIELD 		=	"chat_id";
		public static final String TEXT_FIELD 			=	"text";
		public static final String MESSAGE_FIELD 		=	"message";
		public static final String PHOTO_FIELD 			=	"photo";
		public static final String CHAT_FIELD			=	"chat";
		public static final String ID_FIELD				=	"id";
		public static final String RESULT_FIELD			=	"result";
		
		
	}
}
