package dev.theturkey.pideckapp.ui;


import dev.theturkey.pideckapp.Core;
import dev.theturkey.pideckapp.Util;
import dev.theturkey.pideckapp.config.Config;
import dev.theturkey.pideckapp.connection.ConnectionManager;
import dev.theturkey.pideckapp.profile.ActionInfo;
import dev.theturkey.pideckapp.profile.Button;
import dev.theturkey.pideckapp.ui.componenets.JDelayedSaveTextField;
import dev.theturkey.pideckapp.ui.subframes.ImageSelectFrame;
import dev.theturkey.pideckapp.ui.subframes.IntegrationsFrame;

import javax.swing.*;
import javax.swing.plaf.metal.MetalButtonUI;
import java.awt.*;
import java.io.File;

import static javax.swing.BorderFactory.createEmptyBorder;

public class InfoPanel extends JPanel
{
	private Button currentBtn;

	private JLabel buttonIDLabel;
	private JButton bgColorButton;
	private JButton bgImageButton;
	private JTextField buttonTextInput;

	private JPanel actionsPanelActions;


	public InfoPanel()
	{
		setLayout(new GridBagLayout());
		setBackground(UIFrame.BACKGROUND_SECONDARY);

		setVisible(false);
		setPreferredSize(new Dimension(200, getHeight()));

		JPanel titlePanel = new JPanel();
		titlePanel.setBackground(UIFrame.BACKGROUND_SECONDARY);
		buttonIDLabel = new JLabel("Button Info");
		buttonIDLabel.setForeground(UIFrame.TEXT_PRIMARY);
		buttonIDLabel.getInsets().set(10, 0, 10, 0);
		titlePanel.add(buttonIDLabel);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(titlePanel, gbc);

		JPanel colorPanel = new JPanel();
		colorPanel.setBackground(UIFrame.BACKGROUND_SECONDARY);
		JLabel bgColorLabel = new JLabel("Color: ");
		bgColorLabel.setForeground(UIFrame.TEXT_PRIMARY);
		colorPanel.add(bgColorLabel);

		bgColorButton = new JButton();
		bgColorButton.setSize(32, 32);
		bgColorButton.setPreferredSize(new Dimension(32, 32));
		bgColorButton.setUI(new MetalButtonUI());
		bgColorButton.setOpaque(true);
		bgColorButton.setFocusPainted(false);
		bgColorButton.addActionListener(e ->
		{
			if(currentBtn == null)
				return;

			Color newColor = JColorChooser.showDialog(null, "Choose a color", bgColorButton.getBackground());
			if(newColor != null)
			{
				bgColorButton.setBackground(newColor);
				currentBtn.setBgColor(newColor);
				if(ConnectionManager.isConnected())
					ConnectionManager.getCurrentConnection().updateButton(currentBtn);
				Core.getUI().updateSim();
				Config.saveProfiles();
			}
		});
		colorPanel.add(bgColorButton);

		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridy = 1;
		gbc.weightx = 1;
		add(colorPanel, gbc);

		JPanel imagePanel = new JPanel();
		imagePanel.setBackground(UIFrame.BACKGROUND_SECONDARY);
		JLabel bgImageLabel = new JLabel("Image: ");
		bgImageLabel.setForeground(UIFrame.TEXT_PRIMARY);
		imagePanel.add(bgImageLabel);

		bgImageButton = new JButton();
		bgImageButton.setSize(32, 32);
		bgImageButton.setPreferredSize(new Dimension(32, 32));
		bgImageButton.setUI(new MetalButtonUI());
		bgImageButton.setOpaque(true);
		bgImageButton.setFocusPainted(false);
		bgImageButton.addActionListener(e ->
		{
			if(currentBtn == null)
				return;

			new ImageSelectFrame(this);
		});
		imagePanel.add(bgImageButton);

		JButton removeImageButton = new JButton();
		removeImageButton.setPreferredSize(new Dimension(20, 16));
		removeImageButton.setIcon(Util.getScaledImage(new ImageIcon(Util.getRes("icons/x_mark.png")), 16, 16));
		removeImageButton.setBackground(UIFrame.BACKGROUND_SECONDARY);
		removeImageButton.setUI(new MetalButtonUI());
		removeImageButton.setOpaque(true);
		removeImageButton.setFocusPainted(false);
		removeImageButton.addActionListener(e ->
		{
			bgImageButton.setIcon(null);
			currentBtn.setImageSrc("");
			if(ConnectionManager.isConnected())
				ConnectionManager.getCurrentConnection().updateButton(currentBtn);
			Core.getUI().updateSim();
			Config.saveProfiles();
		});
		imagePanel.add(removeImageButton);

		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridy = 2;
		gbc.weightx = 1;
		add(imagePanel, gbc);


		JPanel textInputPanel = new JPanel();
		textInputPanel.setBackground(UIFrame.BACKGROUND_SECONDARY);
		JLabel textInputLabel = new JLabel("Text: ");
		textInputLabel.setForeground(UIFrame.TEXT_PRIMARY);
		textInputPanel.add(textInputLabel);

		buttonTextInput = new JDelayedSaveTextField("", text ->
		{
			if(currentBtn.getImageSrc().isEmpty())
				currentBtn.setText(text);
			if(ConnectionManager.isConnected())
				ConnectionManager.getCurrentConnection().updateButton(currentBtn);
			Core.getUI().updateSim();
			Config.saveProfiles();
		});
		buttonTextInput.setBackground(UIFrame.BACKGROUND_PRIMARY);
		buttonTextInput.setForeground(UIFrame.TEXT_LIGHT);
		buttonTextInput.setSize(100, 25);
		buttonTextInput.setPreferredSize(new Dimension(100, 25));
		textInputPanel.add(buttonTextInput);

		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1;
		gbc.gridy = 3;
		add(textInputPanel, gbc);

		JPanel actionsPanelTopBar = new JPanel();
		actionsPanelTopBar.setBackground(UIFrame.BACKGROUND_PRIMARY);

		JLabel actionsPanelLabel = new JLabel("Actions");
		actionsPanelLabel.setForeground(UIFrame.TEXT_PRIMARY);
		actionsPanelTopBar.add(actionsPanelLabel);

		JButton addAction = new JButton("+");
		addAction.setBackground(UIFrame.PRIMARY_MAIN);
		addAction.setForeground(UIFrame.TEXT_PRIMARY);
		addAction.setUI(new MetalButtonUI());
		addAction.setOpaque(true);
		addAction.setFocusPainted(false);
		addAction.addActionListener(e -> new IntegrationsFrame(this, currentBtn));
		actionsPanelTopBar.add(addAction);
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1;
		gbc.gridy = 4;
		add(actionsPanelTopBar, gbc);

		actionsPanelActions = new JPanel();
		actionsPanelActions.setLayout(new BoxLayout(actionsPanelActions, BoxLayout.PAGE_AXIS));
		actionsPanelActions.setBackground(UIFrame.BACKGROUND_PRIMARY);

		JScrollPane pane = new JScrollPane(actionsPanelActions,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		pane.setBorder(createEmptyBorder());

		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridy = 5;
		add(pane, gbc);
	}


	public void setInfoPanelButton(Button button)
	{
		currentBtn = button;
		if(button != null)
		{
			buttonIDLabel.setText("Button Info");

			buttonTextInput.setText(button.getText());
			bgColorButton.setBackground(Util.hex2Rgb(button.getBgColor()));
			bgImageButton.setIcon(Util.getScaledImage(Config.getSavedImageFile(button.getImageSrc()).getPath(), 32, 32));

			updateActionsPanel();

			setPreferredSize(new Dimension(200, getHeight()));
			setVisible(true);
		}
		else
		{
			setPreferredSize(new Dimension(0, getHeight()));
			setVisible(false);
		}


		updateUI();
	}

	public void setCurrentButtonImage(File selectedFile)
	{
		if(currentBtn == null)
			return;

		bgImageButton.setIcon(Util.getScaledImage(selectedFile.getPath(), 16, 16));
		currentBtn.setImageSrc(selectedFile.getName());
		if(ConnectionManager.isConnected())
			ConnectionManager.getCurrentConnection().updateButton(currentBtn);
		Core.getUI().updateSim();
		Config.saveProfiles();
	}

	public void updateActionsPanel()
	{
		for(int i = actionsPanelActions.getComponentCount() - 1; i >= 0; i--)
			if(actionsPanelActions.getComponent(i) instanceof ActionPanel || actionsPanelActions.getComponent(i) instanceof Box.Filler)
				actionsPanelActions.remove(i);

		actionsPanelActions.add(Box.createVerticalStrut(5));
		for(ActionInfo action : currentBtn.getActions())
		{
			actionsPanelActions.add(new ActionPanel(this, currentBtn, action));
			actionsPanelActions.add(Box.createVerticalStrut(5));
		}
	}
}
