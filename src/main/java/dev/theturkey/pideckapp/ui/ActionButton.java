package dev.theturkey.pideckapp.ui;

import dev.theturkey.pideckapp.Core;
import dev.theturkey.pideckapp.Util;
import dev.theturkey.pideckapp.profile.Button;

import javax.swing.*;
import javax.swing.plaf.metal.MetalButtonUI;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;


public class ActionButton extends JPanel
{

	public ActionButton(Button btn)
	{
		setLayout(new BorderLayout());

		JButton button = new JButton();
		button.setUI(new MetalButtonUI());
		button.setIcon(Util.getScaledImage(btn.getImageSrc(), 64, 64));
		button.setBackground(Util.hex2Rgb(btn.getBgColor()));
		button.setFocusPainted(false);

		button.addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentResized(ComponentEvent e)
			{
				Dimension size = button.getSize();
				Insets insets = button.getInsets();
				size.width -= insets.left + insets.right;
				size.height -= insets.top + insets.bottom;
				if (size.width > size.height) {
					//size.width = -1;
				} else {
					//size.height = -1;
				}
				System.out.println(size);
				//btn2.setIcon(Util.getScaledImage(btn.getImageSrc(), size.width, size.height));
			}
		});


		button.addActionListener(e ->
		{
			Core.getUI().setInfoPanelButton(btn.getID());
		});
		add(button);
	}
}
