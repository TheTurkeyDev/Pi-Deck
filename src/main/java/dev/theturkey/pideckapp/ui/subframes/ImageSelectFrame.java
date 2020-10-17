package dev.theturkey.pideckapp.ui.subframes;

import dev.theturkey.pideckapp.Util;
import dev.theturkey.pideckapp.config.Config;
import dev.theturkey.pideckapp.ui.InfoPanel;
import dev.theturkey.pideckapp.ui.UIFrame;
import dev.theturkey.pideckapp.ui.layouts.WrapLayout;

import javax.swing.*;
import javax.swing.plaf.metal.MetalButtonUI;
import java.awt.*;
import java.io.File;

public class ImageSelectFrame extends JFrame
{
	public ImageSelectFrame(InfoPanel infoPanel)
	{
		setSize(400, 600);
		setLayout(new BorderLayout());
		setBackground(UIFrame.BACKGROUND_PRIMARY);

		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setBackground(UIFrame.BACKGROUND_PRIMARY);

		JPanel actionsPanel = new JPanel();
		actionsPanel.setBackground(UIFrame.BACKGROUND_PRIMARY);
		actionsPanel.setLayout(new WrapLayout());
		actionsPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

		GridBagConstraints gbc;

		for(File image : Config.getAllSavedImages())
		{
			JButton btn = new JButton();
			btn.setIcon(Util.getScaledImage(new ImageIcon(image.getPath()), 90, 90));
			btn.setPreferredSize(new Dimension(100, 100));
			btn.setUI(new MetalButtonUI());
			btn.setBackground(UIFrame.PRIMARY_MAIN);
			btn.setFocusPainted(false);

			btn.addActionListener(e ->
			{
				infoPanel.setCurrentButtonImage(image);
				ImageSelectFrame.this.dispose();
			});

			actionsPanel.add(btn);


		}

		JScrollPane pane = new JScrollPane(actionsPanel,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridy = 0;
		gbc.gridx = 0;
		gbc.fill = GridBagConstraints.BOTH;
		panel.add(pane, gbc);

		JButton importImageBtn = new JButton("Import");
		importImageBtn.setBackground(UIFrame.PRIMARY_MAIN);
		importImageBtn.setForeground(UIFrame.TEXT_PRIMARY);
		importImageBtn.setUI(new MetalButtonUI());
		importImageBtn.setOpaque(true);
		importImageBtn.setFocusPainted(false);
		importImageBtn.addActionListener(e ->
		{
			JFileChooser fileChooser = new JFileChooser();
			JFrame popup = new JFrame();
			int result = fileChooser.showOpenDialog(popup);
			if(result == JFileChooser.APPROVE_OPTION)
			{
				File newFile = Config.addImageToSaved(fileChooser.getSelectedFile());
				infoPanel.setCurrentButtonImage(newFile);
			}
			ImageSelectFrame.this.dispose();
		});

		gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.gridy = 1;
		gbc.gridx = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(importImageBtn, gbc);

		add(panel, BorderLayout.CENTER);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
