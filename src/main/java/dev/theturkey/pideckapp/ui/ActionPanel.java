package dev.theturkey.pideckapp.ui;

import dev.theturkey.pideckapp.Util;
import dev.theturkey.pideckapp.config.Config;
import dev.theturkey.pideckapp.integrations.ActionProperty;
import dev.theturkey.pideckapp.profile.ActionInfo;
import dev.theturkey.pideckapp.profile.Button;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ActionPanel extends JPanel
{
	private JPanel propsPanel;

	public ActionPanel(InfoPanel parent, Button btn, ActionInfo action)
	{
		setLayout(new GridBagLayout());
		setBackground(UIFrame.BACKGROUND_SECONDARY);
		getInsets().set(10, 0, 0, 0);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.insets = new Insets(0, 5, 0, 5);
		add(new Box.Filler(new Dimension(0, 0), new Dimension(0, 0), new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE)), gbc);

		JButton removeBtn = new JButton();
		removeBtn.setPreferredSize(new Dimension(20, 16));
		removeBtn.setIcon(Util.getScaledImage(new ImageIcon(Util.getRes("icons/x_mark.png")), 16, 16));
		removeBtn.addActionListener(e ->
		{
			btn.removeAction(action);
			Config.saveProfiles();
			parent.updateActionsPanel();
			parent.updateUI();
		});

		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.VERTICAL;
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 0;
		gbc.insets = new Insets(5, 5, 5, 5);
		add(removeBtn, gbc);

		propsPanel = new JPanel();
		propsPanel.getInsets().set(0, 20, 0, 0);
		propsPanel.setBackground(UIFrame.BACKGROUND_SECONDARY);

		propsPanel.setLayout(new GridBagLayout());

		updateProps(action);

		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 3;
		add(propsPanel, gbc);
	}

	public void updateProps(ActionInfo action)
	{
		propsPanel.removeAll();

		int y = 0;
		for(ActionProperty prop : action.getDefaultProps())
		{
			JLabel keyLabel = new JLabel(prop.key);
			keyLabel.setForeground(UIFrame.TEXT_PRIMARY);
			keyLabel.setMinimumSize(new Dimension(100, keyLabel.getHeight()));
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(5, 5, 5, 5);
			gbc.fill = GridBagConstraints.BOTH;
			gbc.gridx = 0;
			gbc.gridy = y;
			propsPanel.add(keyLabel, gbc);

			String val = action.getProps().getOrDefault(prop.key, prop.defaultVal);

			JComponent component;
			switch(prop.type)
			{
				case INTEGER:
					component = new JSpinner(new SpinnerNumberModel(Integer.parseInt(val), Integer.MIN_VALUE, Integer.MAX_VALUE, 1));
					((JSpinner) component).addChangeListener(e ->
					{
						action.setPropsValue(prop.key, String.valueOf(((JSpinner) component).getModel().getValue()));
						Config.saveProfiles();
					});
					break;
				case DOUBLE:
					component = new JSpinner(new SpinnerNumberModel(Float.parseFloat(val), Integer.MIN_VALUE, Integer.MAX_VALUE, 0.01));
					((JSpinner) component).addChangeListener(e ->
					{
						action.setPropsValue(prop.key, String.valueOf(((JSpinner) component).getModel().getValue()));
						Config.saveProfiles();
					});
					break;
				case BOOLEAN:
					component = new JCheckBox("", Boolean.parseBoolean(val));
					((JCheckBox) component).addItemListener(e ->
					{
						action.setPropsValue(prop.key, String.valueOf(((JCheckBox) component).isSelected()));
						Config.saveProfiles();
					});
					break;
				case FILE:
					File currentFile = new File(val);
					component = new JButton(currentFile.exists() ? currentFile.getName() : "Not Selected");
					component.setMaximumSize(new Dimension(100, component.getHeight()));
					((JButton) component).addActionListener(e ->
					{
						JFileChooser fileChooser = new JFileChooser();
						JFrame popup = new JFrame();
						int result = fileChooser.showOpenDialog(popup);
						if(result == JFileChooser.APPROVE_OPTION)
						{
							File selectedFile = fileChooser.getSelectedFile();
							((JButton) component).setText(selectedFile.getName());
							action.setPropsValue(prop.key, selectedFile.getPath());
							Config.saveProfiles();
						}
					});
					break;
				default:
					component = new JDelayedSaveTextField(val, text ->
					{
						action.setPropsValue(prop.key, text);
						Config.saveProfiles();
					});
					break;
			}

			gbc = new GridBagConstraints();
			gbc.fill = GridBagConstraints.BOTH;
			gbc.gridx = 1;
			gbc.gridy = y;
			gbc.weightx = 1;
			gbc.insets = new Insets(5, 0, 5, 5);

			component.setBackground(UIFrame.BACKGROUND_SECONDARY);
			propsPanel.add(component, gbc);
			y++;
		}
	}
}
