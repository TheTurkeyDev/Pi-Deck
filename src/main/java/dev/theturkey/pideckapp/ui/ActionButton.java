package dev.theturkey.pideckapp.ui;

import dev.theturkey.pideckapp.Core;
import dev.theturkey.pideckapp.Util;
import dev.theturkey.pideckapp.profile.Button;
import dev.theturkey.pideckapp.profile.ProfileManager;

import javax.swing.*;
import javax.swing.plaf.metal.MetalButtonUI;
import java.awt.*;


public class ActionButton extends JPanel
{

	public ActionButton(Button btn)
	{
		setLayout(new BorderLayout());

		JButton button = new JButton();
		button.setUI(new MetalButtonUI());
		button.setSize(getSize());
		button.setBackground(Util.hex2Rgb(btn.getBgColor()));

		button.addActionListener(e ->
		{
			Core.getUI().setInfoPanelButton(btn.getID());
		});
		add(button);
	}
}
