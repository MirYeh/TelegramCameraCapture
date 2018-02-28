package org.miri.camcapture.impl;

import java.io.File;

import org.miri.camcapture.api.ICamera;
import org.miri.camcapture.api.ITelegram;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamMotionDetector;
import com.github.sarxos.webcam.WebcamMotionEvent;
import com.github.sarxos.webcam.WebcamMotionListener;

public enum MotionDetector implements WebcamMotionListener {
	INSTANCE;
	private final Logger logger = LoggerFactory.getLogger(MotionDetector.class);
	private WebcamMotionDetector detector;
	private String chatId;
	private ITelegram telegram;
	private ICamera camera;
	
	
	private MotionDetector() {
		telegram = Telegram.INSTANCE;
		camera = DefaultCamera.INSTANCE;
		detector = new WebcamMotionDetector(Webcam.getDefault());
		detector.setInterval(10000); // 10000 ms = 1 s
	}
	
	public void start(String chatId) {
		this.chatId = chatId;
		detector.addMotionListener(this);
		detector.start();
	}
	
	public void stop() {
		detector.removeMotionListener(this);
	}
	
	@Override
	public void motionDetected(WebcamMotionEvent wme) {
		try {
			File image = camera.saveImage(wme.getCurrentImage(), chatId);
			telegram.sendMessage(chatId, "Motion detected!");
			telegram.sendImage(chatId, image);
		} catch (Exception exc) {
			logger.error("Unable to send image for detected motion: " + exc.getMessage(), exc);
		}
	}
	
	
}
