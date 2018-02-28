package org.miri.camcapture.impl;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import org.miri.camcapture.api.ICamera;
import org.miri.camcapture.exceptions.CameraException;
import org.miri.camcapture.exceptions.FileAccessException;
import org.miri.camcapture.exceptions.TelegramException;
import org.miri.camcapture.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.sarxos.webcam.Webcam;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.video.ConverterFactory;
import com.xuggle.xuggler.video.IConverter;

/**
 * Allows to capture and save images and videos from the default camera to the local machine.
 * @author Miri Yehezkel
 * @see ICamera
 */
public enum DefaultCamera implements ICamera {
	INSTANCE;
	private final Logger logger = LoggerFactory.getLogger(DefaultCamera.class);
	private static final String captureDirName = "captures";
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HH-mm-ss.SSS");
	private Webcam webcam;
	
	private DefaultCamera() {
		try {
			FileUtil.mkdir(captureDirName);
			webcam = Webcam.getDefault();
			setOptimalResolution();
			logger.trace("Initialized DefaultCamera");
		} catch (FileAccessException e) {
			logger.error(e.getMessage(), e.getCause());
		}
	}
	
	/**
	 * Sets optimal resolution for device.
	 */
	private void setOptimalResolution() {
		if (webcam == null) {
			logger.error("Unable to find camera");
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
		webcam.setViewSize(new Dimension(w, h));
		logger.trace("Set Optimal Camera Resolution ({}x{})", w, h);
	}
	
	/**
	 * Captures image from camera and saves it to the local machine.
	 */
	@Override
	public File saveImage(String chatId) throws CameraException, FileAccessException {
		if (webcam == null)
			throw new CameraException("Unable to find camera");
		webcam.open(true);
		BufferedImage img = webcam.getImage();
		return saveImage(img, chatId);
	}
	
	/**
	 * Saves a {@link BufferedImage} to the local machine.
	 */
	@Override
	public File saveImage(BufferedImage img, String chatId) throws FileAccessException {
		File file = null;
		try {
			String filePath = createFilePath(chatId, ".png");
			file = new File(filePath);
			ImageIO.write(img, "PNG", file);
		} catch (IOException e) {
			throw new FileAccessException("Unable to capture image", e);
		} 
		return file;
	}
	
	/**
	 * Captures a series of images from camera and saves it as a video to the local machine.
	 */
	@Override
	public File saveVideo(String chatId) throws TelegramException, FileAccessException {
		Dimension size = webcam.getViewSize();
		File file = new File(createFilePath(chatId, ".mp4"));
		try {
			IMediaWriter writer = ToolFactory.makeWriter(file.getPath());
			writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_H264, size.width, size.height);
			webcam.open(true);
			convertToFrames(writer, 50);
			writer.close();
		} catch (IllegalArgumentException | UnsupportedOperationException exc) {
			throw new TelegramException(exc.getMessage(), exc);
		}
		return file;
	}
	
	
	
	/**
	 * Converts captured images to video frames.
	 * @param writer {@link IMediaWriter} encoding video
	 * @param frames Number of requested frames
	 */
	private void convertToFrames(IMediaWriter writer, int frames) {
		long start = System.currentTimeMillis();
		for (int i = 0; i < frames; i++) {
			BufferedImage image = ConverterFactory.convertToType(webcam.getImage(), BufferedImage.TYPE_3BYTE_BGR);
			IConverter converter = ConverterFactory.createConverter(image, IPixelFormat.Type.YUV420P);
			IVideoPicture frame = converter.toPicture(image, (System.currentTimeMillis() - start) * 1000);
			frame.setKeyFrame(i == 0);
			frame.setQuality(0);
			writer.encodeVideo(0, frame);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {}
		}
		
	}
	
	
	/**
	 * Returns file path of requested chat and ensured directories exists.
	 * @param chatId Id of the chat requesting file
	 * @param fileName Name of file
	 * @param fileType Type of file (.png, .jpg, .mp4 etc.)
	 * @return A String representing the relative location of the file
	 * @throws FileAccessException if unable to make directory
	 */
	private String createFilePath(String chatId, String fileType) throws FileAccessException {
		String chatDir = captureDirName + "/" + chatId;
		FileUtil.mkdir(captureDirName);
		FileUtil.mkdir(chatDir);
		return chatDir + "/" + sdf.format(new Date()) + fileType;
		
	}

	
}
