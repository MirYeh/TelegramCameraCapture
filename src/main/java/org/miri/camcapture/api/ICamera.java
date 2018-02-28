package org.miri.camcapture.api;

import java.awt.image.BufferedImage;
import java.io.File;

import org.miri.camcapture.exceptions.CameraException;
import org.miri.camcapture.exceptions.FileAccessException;
import org.miri.camcapture.exceptions.TelegramException;

/**
 * Defines camera methods. Supports capturing images and videos from camera
 *  and saving them on the local machine. 
 * @author Miri Yehezkel
 *
 */
public interface ICamera {
	
	/**
	 * Captures image from camera and saves it to the local machine.
	 * @param chatId Id of telegram chat
	 * @return A {@link File} representing the location of the captured image
	 * @throws CameraException if unable to complete capture
	 * @throws FileAccessException if unable to save file
	 */
	File saveImage(String chatId) throws CameraException, FileAccessException;
	
	/**
	 * Saves {@link BufferedImage} to the local machine.
	 * @param img {@link BufferedImage} to save
	 * @param chatId Id of telegram chat
	 * @return A {@link File} representing the location of the image
	 * @throws FileAccessException if unable to save file
	 */
	File saveImage(BufferedImage img, String chatId) throws FileAccessException;
	
	/**
	 * Captures a series of images from camera and saves it as a video to the local machine.
	 * @param chatId Id of telegram chat
	 * @return A {@link File} representing the location of the video
	 * @throws TelegramException if unable to complete video capture
	 * @throws FileAccessException if unable to save file
	 */
	File saveVideo(String chatId) throws TelegramException, FileAccessException;
	
}
