package org.cameracapture.telegram.miri.util;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.cameracapture.telegram.miri.exceptions.FileAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides file utilities. 
 * @author Miri Yehezkel
 */
public class FileUtil {
	private static Logger logger = LoggerFactory.getLogger(FileUtil.class);
	
	/**
	 * Makes requested directory.
	 * @param dirName directory name
	 * @throws FileAccessException if unable to make directory
	 */
	public static void mkdir(String dirName) throws FileAccessException {
		Path path = Paths.get(dirName);
		if (! Files.exists(path)) {
			try {
				Files.createDirectory(path);
			} catch (IOException e) {
				throw new FileAccessException("Unable to make directory '" + dirName + "'", e);
			}
		}
	}
	
	/**
	 * Checks if requested file exists. If not exists, file is created.
	 * Ensure requested file exists. If file not found it is created.
	 * @param fileName file to create
	 * @return {@code true} if file had ti be created, {@code false} if it already existed
	 * @throws FileAccessException if unable to create file
	 */
	public static boolean isNewFileCreated(String fileName) throws FileAccessException {
		Path path = Paths.get(fileName);
		if (!Files.exists(path))
			try {
				Files.createFile(path);
				return true;
			} catch (IOException e) {
				throw new FileAccessException("Unable to create file '" + fileName + "'", e);
			}
		return false;
	}
	
	/**
	 * Reads line from file. Used to read bot token and last update Id.
	 * @param filePath path of file to read
	 * @return a String read from file
	 * @throws FileAccessException if unable to read from file
	 */
	public static String readLine(String filePath) throws FileAccessException {
		String line = null;
		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader(filePath);
			br = new BufferedReader(fr);
			line = br.readLine();
		} catch (IOException e) {
			throw new FileAccessException("Could not read from file (path: " + filePath + ")", e);
		} finally {
			closeResource(br);
			closeResource(fr);
		}
		return line;
	}
	
	/**
	 * Write data to file.
	 * @param filePath path of file to write to 
	 * @param data String to write
	 * @throws FileAccessException if unable to write to file
	 */
	public static void write(String filePath, String data) throws FileAccessException {
		FileWriter writer = null;
		try {
			writer = new FileWriter(filePath);
			writer.write(data);
		} catch (IOException e) {
			throw new FileAccessException("Could not write to file (path: " + filePath + ")", e);
		} finally {
			closeResource(writer);
		}
	}
	
	/**
	 * Closes a resource.
	 * @param resource resource to .close()
	 */
	public static void closeResource(Closeable resource) {
		try {
			if (resource != null)
			resource.close();
		} catch (IOException e) {
			logger.warn("Could not close " + resource.getClass().getName() + " resource", e);
			e.printStackTrace();
		}
		
	}
	
}
