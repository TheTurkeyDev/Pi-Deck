package dev.theturkey.pideckapp;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.UUID;

public class Util
{

	public static Color hex2Rgb(String colorStr)
	{
		if(colorStr.isEmpty())
			return Color.WHITE;

		if(colorStr.startsWith("#"))
			colorStr = colorStr.substring(1);

		return new Color(
				Integer.valueOf(colorStr.substring(0, 2), 16),
				Integer.valueOf(colorStr.substring(2, 4), 16),
				Integer.valueOf(colorStr.substring(4, 6), 16));
	}

	public static ImageIcon getScaledImage(String path, int w, int h)
	{
		return getScaledImage(new ImageIcon(path), w, h);
	}

	public static ImageIcon getScaledImage(ImageIcon srcImg, int w, int h)
	{
		return getScaledImage(srcImg.getImage(), w, h);
	}

	public static ImageIcon getScaledImage(Image srcImg, int w, int h)
	{
		return new ImageIcon(srcImg.getScaledInstance(w, h, java.awt.Image.SCALE_SMOOTH));
	}

	public static String genUUID()
	{
		return UUID.randomUUID().toString();
	}

	public static URL getRes(String path)
	{
		return Util.class.getClassLoader().getResource(path);
	}

	public static String imageToBase64(File file)
	{
		String encodedFile = "";
		try
		{
			FileInputStream fileInputStreamReader = new FileInputStream(file);
			byte[] bytes = new byte[(int) file.length()];
			fileInputStreamReader.read(bytes);
			encodedFile = Base64.getEncoder().encodeToString(bytes);
		} catch(IOException e)
		{
			e.printStackTrace();
		}

		return encodedFile;
	}
}
