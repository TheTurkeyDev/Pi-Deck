package dev.theturkey.pideckapp;

import dev.theturkey.pideckapp.integrations.IOIntegration;
import dev.theturkey.pideckapp.profile.ProfileManager;
import dev.theturkey.pideckapp.ui.UIFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;

public class Core
{
	private static PiDeckConnection pideck;
	private static UIFrame ui;

	//I don't like this, but idk what else to do
	public static int displayRows = 2;
	public static int displayColumns = 4;

	public static void main(String[] args)
	{

		try
		{
			IOIntegration ioIntegration = new IOIntegration();
			ioIntegration.load();
			ProfileManager.loadProfiles();
		} catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}

		pideck = new PiDeckConnection();
		pideck.connect();

		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e)
		{
			System.out.println("Unable to set LookAndFeel");
		}

		System.out.println("UI");
		ui = new UIFrame();
	}

	public static void sendKey(Robot robot, int key, boolean shift)
	{
		if(shift)
			robot.keyPress(KeyEvent.VK_SHIFT);
		robot.keyPress(key);
		robot.keyRelease(key);
		if(shift)
			robot.keyRelease(KeyEvent.VK_SHIFT);
	}

	public static PiDeckConnection getPiDeck()
	{
		return pideck;
	}

	public static UIFrame getUI()
	{
		return ui;
	}
}
