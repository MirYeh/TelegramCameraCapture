package org.cameracapture.telegram.miri.camera;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;

import org.cameracapture.telegram.miri.exceptions.*;
import org.cameracapture.telegram.miri.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.sarxos.webcam.Webcam;

/**
 * Handles camera setup and photo capture.
 * @author Miri Yehezkel
 */
public class CameraCapture implements ICamera {
	private static CameraCapture instance;
	private final String captureDirName = "captures";
	private final Logger logger = LoggerFactory.getLogger(CameraCapture.class);
	private final SimpleDateFormat sdf = new SimpleDateFormat("'D'yyyy-MM-dd-'H'hhmm.ss");
	private Webcam webcam; 
	
	/**
	 * Private constructor.
	 * @throws CameraCaptureException if unable to create camera directory
	 */
	private CameraCapture() throws CameraCaptureException  {
		webcam = Webcam.getDefault();
		setOptimalResolution();
		try {
			FileUtil.mkdir(captureDirName);
		} catch (FileAccessException e) {
			throw new CameraCaptureException("Unable to create camera directory", e);
		}
		logger.trace("Initialized CameraCapture instance");
	}
	
	/**
	 * Initializes CameraCapture.
	 * @return an initialized instance 
	 * @throws CameraCaptureException if unable to create camera directory
	 */
	public static CameraCapture init() throws CameraCaptureException {
		if (instance == null) {
			synchronized (CameraCapture.class) {
				if (instance == null)
					instance = new CameraCapture();
			}
		}
		return instance;
	}
	
	
	/**
	 * Sets optimal resolution for device.
	 */
	private void setOptimalResolution() {
		if (webcam == null) {
			logger.warn("Unable to find camera");
			return;
		}
		int w=0, h=0;
		Dimension[] dms = webcam.getDevice().getResolutions();
		for (Dimension d : dms) {
			if (w < d.getWidth()) {
				w = (int) d.getWidth();  
				h = (int) d.getHeight();
			}
		}
		logger.trace("Set Optimal Camera Resolution ({}x{})", w, h);
		webcam.setViewSize(new Dimension(w, h));
	}
	
	/**
	 * Captures a photo of the camera and saves it to {@code /captures} folder.
	 * @return A File representing the path to the photo
	 * @throws CameraCaptureException if unable to complete image capture
	 */
	public File capture(String chatId) throws CameraCaptureException {
		if (webcam == null)
			throw new CameraCaptureException("Unable to find camera");
		webcam.open();
		BufferedImage image = webcam.getImage();
		return capture(image, chatId);
	}
	
	/**
	 * Captures a photo of the camera and saves it to {@code /captures} folder.
	 * @param image BufferedImage to save to folder
	 * @param chatId chat Id to return captured photo to
	 * @return A File representing the path to the photo
	 * @throws CameraCaptureException if unable to complete image capture
	 */
	public File capture(BufferedImage image, String chatId) throws CameraCaptureException {
		File file = null;
		try {
			FileUtil.mkdir(captureDirName);
			FileUtil.mkdir(captureDirName + "/" + chatId);
			String filepath = captureDirName + "/" + chatId + "/" +sdf.format(new Date()) + ".png";
			file = new File(filepath);
			ImageIO.write(image, "PNG", file);
		} catch (IllegalArgumentException | IOException e) {
			throw new CameraCaptureException("Unable to capture image", e);
		} catch (FileAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file;
	}
	

}
