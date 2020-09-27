package dev.theturkey.pideckapp;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
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

	public static Image getScaledImage(Image srcImg, int w, int h)
	{
		BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = resizedImg.createGraphics();

		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(srcImg, 0, 0, w, h, null);
		g2.dispose();

		return resizedImg;
	}

	public static String genUUID()
	{
		return UUID.randomUUID().toString();
	}

	public static URL getRes(String path)
	{
		return Util.class.getClassLoader().getResource(path);
	}
}
