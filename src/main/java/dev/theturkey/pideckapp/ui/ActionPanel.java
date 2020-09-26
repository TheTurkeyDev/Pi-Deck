package dev.theturkey.pideckapp.ui;

import dev.theturkey.pideckapp.action.ActionsManager;
import dev.theturkey.pideckapp.profile.ActionInfo;

import javax.swing.*;
import java.util.List;

public class ActionPanel extends JPanel
{
	private JComboBox<String> actionComboBox;
	private JComboBox<String> typeComboBox;

	public ActionPanel()
	{
		setBackground(UIFrame.BACKGROUND_SECONDARY);

		actionComboBox = new JComboBox<>(new String[0]);
		actionComboBox.setSelectedIndex(-1);
		actionComboBox.addActionListener(e ->
		{
			System.out.println("HERE 2");
		});

		typeComboBox = new JComboBox<>(ActionsManager.getAllIntegration().toArray(new String[0]));
		typeComboBox.setSelectedIndex(-1);
		typeComboBox.addActionListener(e ->
		{
			String selected = String.valueOf(((JComboBox<String>) e.getSource()).getSelectedItem());
			DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(ActionsManager.getAllActions(selected).toArray(new String[0]));
			actionComboBox.setModel(model);
			System.out.println("HERE");
		});

		add(typeComboBox);
		add(actionComboBox);

		JPanel subPanel = new JPanel();
		subPanel.setBackground(UIFrame.BACKGROUND_SECONDARY);
		add(subPanel);
	}

	public void setAction(ActionInfo action)
	{
		List<String> integrations = ActionsManager.getAllIntegration();
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(integrations.toArray(new String[0]));
		typeComboBox.setModel(model);
		typeComboBox.setSelectedIndex(integrations.indexOf(action.getType()));

		List<String> actions = ActionsManager.getAllActions(action.getType());
		model = new DefaultComboBoxModel<>(actions.toArray(new String[0]));
		actionComboBox.setModel(model);
		actionComboBox.setSelectedIndex(actions.indexOf(action.getAction()));
	}
}
