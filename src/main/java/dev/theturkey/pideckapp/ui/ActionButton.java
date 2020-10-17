package dev.theturkey.pideckapp.ui;

import dev.theturkey.pideckapp.Core;
import dev.theturkey.pideckapp.Util;
import dev.theturkey.pideckapp.config.Config;
import dev.theturkey.pideckapp.profile.Button;

import javax.swing.*;
import javax.swing.plaf.metal.MetalButtonUI;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;


public class ActionButton extends JPanel
{

	public ActionButton(Button btn)
	{
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(1, 1));
		getInsets().set(0, 0, 0, 0);

		File buttonIconFile = Config.getSavedImageFile(btn.getImageSrc());
		JButton button = btn.getImageSrc().isEmpty() || !buttonIconFile.exists() ? new JButton(btn.getText()) : new JButton();
		button.setUI(new MetalButtonUI());
		button.setBackground(Util.hex2Rgb(btn.getBgColor()));
		button.setFocusPainted(false);
		button.getInsets().set(0, 0, 0, 0);
		button.setMargin(new Insets(0, 0, 0, 0));

		button.addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentResized(ComponentEvent e)
			{
				Dimension size = button.getSize();
				Insets insets = button.getInsets();
				size.width -= insets.left + insets.right;
				size.height -= insets.top + insets.bottom;
				if(size.width > size.height)
					size.width = -1;
				else
					size.height = -1;

				button.setIcon(Util.getScaledImage(buttonIconFile.getPath(), size.width, size.height));
			}
		});


		button.addActionListener(e -> Core.getUI().setInfoPanelButton(btn.getID()));
		add(button);
	}
}
