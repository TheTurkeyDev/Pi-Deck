package dev.theturkey.pideckapp.ui.subframes;

import dev.theturkey.pideckapp.connection.ConnectionManager;
import dev.theturkey.pideckapp.ui.UIFrame;

import javax.swing.*;
import javax.swing.plaf.metal.MetalButtonUI;
import java.awt.*;

public class PiDeckConnectFrame extends JFrame
{
	public PiDeckConnectFrame(UIFrame frame)
	{
		setSize(400, 600);
		setPreferredSize(new Dimension(400, 200));
		setLayout(new BorderLayout());
		setBackground(UIFrame.BACKGROUND_PRIMARY);

		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setBackground(UIFrame.BACKGROUND_PRIMARY);
		GridBagConstraints gbc;


		JLabel nameLabel = new JLabel("PORTS:");
		nameLabel.setForeground(UIFrame.TEXT_PRIMARY);
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(nameLabel, gbc);

		JComboBox<String> profilesComboBox = new JComboBox<>(ConnectionManager.getPORTS());
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		panel.add(profilesComboBox, gbc);

		JButton addBtn = new JButton("Connect");
		addBtn.setUI(new MetalButtonUI());
		addBtn.addActionListener(e ->
		{
			String port = (String) profilesComboBox.getSelectedItem();
			if(port == null || port.trim().isEmpty())
			{
				//TODO Alert message
				return;
			}
			ConnectionManager.connectToPort(port);
			PiDeckConnectFrame.this.dispose();
		});
		addBtn.getInsets().set(0, 5, 0, 5);
		addBtn.setForeground(UIFrame.TEXT_LIGHT);
		addBtn.setBackground(UIFrame.PRIMARY_MAIN);
		addBtn.setOpaque(true);
		addBtn.setFocusPainted(false);
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		panel.add(addBtn, gbc);

		add(panel, BorderLayout.CENTER);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		pack();
		setVisible(true);
	}
}
