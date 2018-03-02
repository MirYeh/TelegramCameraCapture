package org.miri.camcapture.api;

/**
 * Contains useful strings for Telegram API such as URLs and fields in Telegram JSON objects.
 * @author Miri Yehezkel
 *
 */
public class TelegramConstants {

	//Telegram URLs
	public static final String BASE_URL 		= "https://api.telegram.org/bot";
	public static final String SEND_MESSAGE_URL = "/sendMessage";
	public static final String SEND_PHOTO_URL 	= "/sendPhoto";
	public static final String SEND_VIDEO_URL 	= "/sendVideo";
	public static final String GET_UPDATES_URL 	= "/getUpdates";
	
	//Fields in Telegram JSON objects
	public static final String CHAT_ID_FIELD 	= "chat_id";
	public static final String TEXT_FIELD 		= "text";
	public static final String MESSAGE_FIELD 	= "message";
	public static final String PHOTO_FIELD 		= "photo";
	public static final String VIDEO_FIELD 		= "video";
	public static final String CHAT_FIELD 		= "chat";
	public static final String ID_FIELD 		= "id";
	public static final String RESULT_FIELD 	= "result";
	public static final String UPDATE_ID_FIELD 	= "update_id";
	
}
