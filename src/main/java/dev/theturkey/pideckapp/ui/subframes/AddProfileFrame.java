package dev.theturkey.pideckapp.ui.subframes;

import dev.theturkey.pideckapp.config.Config;
import dev.theturkey.pideckapp.profile.Profile;
import dev.theturkey.pideckapp.profile.ProfileManager;
import dev.theturkey.pideckapp.ui.UIFrame;

import javax.swing.*;
import javax.swing.plaf.metal.MetalButtonUI;
import java.awt.*;

public class AddProfileFrame extends JFrame
{
	public AddProfileFrame(UIFrame frame)
	{
		setSize(400, 600);
		setPreferredSize(new Dimension(400, 200));
		setLayout(new BorderLayout());
		setBackground(UIFrame.BACKGROUND_PRIMARY);

		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setBackground(UIFrame.BACKGROUND_PRIMARY);
		GridBagConstraints gbc;


		JLabel nameLabel = new JLabel("Name:");
		nameLabel.setForeground(UIFrame.TEXT_PRIMARY);
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(nameLabel, gbc);
		JTextField nameInput = new JTextField();
		nameInput.setPreferredSize(new Dimension(100, 25));
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		panel.add(nameInput, gbc);

		JLabel rowsLabel = new JLabel("Rows:");
		rowsLabel.setForeground(UIFrame.TEXT_PRIMARY);
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		panel.add(rowsLabel, gbc);
		JSpinner rows = new JSpinner(new SpinnerNumberModel(2, 1, 10, 1));
		rows.setBackground(UIFrame.BACKGROUND_SECONDARY);
		rows.setForeground(UIFrame.TEXT_PRIMARY);
		setSize(100, 25);
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 1;
		panel.add(rows, gbc);

		JLabel colsLabel = new JLabel("Columns:");
		colsLabel.setForeground(UIFrame.TEXT_PRIMARY);
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 2;
		panel.add(colsLabel, gbc);
		JSpinner columns = new JSpinner(new SpinnerNumberModel(4, 1, 10, 1));
		columns.setBackground(UIFrame.BACKGROUND_SECONDARY);
		columns.setForeground(UIFrame.TEXT_PRIMARY);
		columns.setSize(100, 25);
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 2;
		panel.add(columns, gbc);

		JButton addBtn = new JButton("Add");
		addBtn.setUI(new MetalButtonUI());
		addBtn.addActionListener(e ->
		{
			String name = nameInput.getText();
			if(name == null || name.trim().isEmpty())
			{
				//TODO Alert message
				return;
			}
			int columnsNum = (int) columns.getModel().getValue();
			int rowsNum = (int) rows.getModel().getValue();
			Profile profile = new Profile(name, columnsNum, rowsNum);
			ProfileManager.addProfile(profile);
			Config.saveProfiles();
			frame.refreshProfilesComboBox();
			AddProfileFrame.this.dispose();
		});
		addBtn.getInsets().set(0, 5, 0, 5);
		addBtn.setForeground(UIFrame.TEXT_LIGHT);
		addBtn.setBackground(UIFrame.PRIMARY_MAIN);
		addBtn.setOpaque(true);
		addBtn.setFocusPainted(false);
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		panel.add(addBtn, gbc);

		add(panel, BorderLayout.CENTER);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		pack();
		setVisible(true);
	}
}
