package dev.theturkey.pideckapp;

import dev.theturkey.pideckapp.config.Config;
import dev.theturkey.pideckapp.integrations.IOIntegration;
import dev.theturkey.pideckapp.profile.ProfileManager;
import dev.theturkey.pideckapp.ui.UIFrame;

import javax.swing.*;

public class Core
{
	private static PiDeckConnection pideck;
	private static UIFrame ui;

	//I don't like this, but idk what else to do
	public static int displayRows = 2;
	public static int displayColumns = 4;

	public static void main(String[] args)
	{
		if(!Config.init())
		{
			System.err.println("FAILED TO START! Could not create configs and save files!");
			return;
		}

		IOIntegration ioIntegration = new IOIntegration();
		ioIntegration.load();
		ProfileManager.loadProfiles();

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

	public static PiDeckConnection getPiDeck()
	{
		return pideck;
	}

	public static UIFrame getUI()
	{
		return ui;
	}
}
