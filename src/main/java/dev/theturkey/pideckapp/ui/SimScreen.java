package dev.theturkey.pideckapp.ui;

import dev.theturkey.pideckapp.Core;
import dev.theturkey.pideckapp.profile.ProfileManager;

import javax.swing.*;
import java.awt.*;

public class SimScreen extends JPanel
{
	public SimScreen()
	{
		setLayout(new GridBagLayout());

		setSize(800, 480);
		setPreferredSize(new Dimension(800, 480));
		setMaximumSize(new Dimension(800, 480));
		setBackground(Color.BLACK);

		setupButtons();
	}

	public void setRows(int newRowCount)
	{
		Core.displayRows = newRowCount;
		setupButtons();
	}

	public void setColumns(int newColumnCount)
	{
		Core.displayColumns = newColumnCount;
		setupButtons();
	}

	public void setupButtons()
	{
		removeAll();
		for(int y = 0; y < Core.displayRows; y++)
			for(int x = 0; x < Core.displayColumns; x++)
				addButton(x, y);
		updateUI();
	}

	public void addButton(int x, int y)
	{
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		ActionButton btn = new ActionButton(ProfileManager.getCurrentProfile().getButtonAt(x, y));
		add(btn, gbc);
	}
}
