package org.cameracapture.telegram.miri.camera;

import java.awt.image.BufferedImage;
import java.io.File;

import org.cameracapture.telegram.miri.exceptions.CameraCaptureException;

/**
 * Defines camera methods.
 * @author Miri Yehezkel
 *
 */
public interface ICamera {
	public File capture(String chatId) throws CameraCaptureException;
	public File capture(BufferedImage image, String chatId) throws CameraCaptureException;
	
}
