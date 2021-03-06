package dev.theturkey.pideckapp.ui.subframes;

import dev.theturkey.pideckapp.Util;
import dev.theturkey.pideckapp.action.ActionsManager;
import dev.theturkey.pideckapp.config.Config;
import dev.theturkey.pideckapp.integrations.IIntegration;
import dev.theturkey.pideckapp.profile.ActionInfo;
import dev.theturkey.pideckapp.profile.Button;
import dev.theturkey.pideckapp.ui.InfoPanel;
import dev.theturkey.pideckapp.ui.UIFrame;
import dev.theturkey.pideckapp.ui.layouts.WrapLayout;

import javax.swing.*;
import javax.swing.plaf.metal.MetalButtonUI;
import java.awt.*;

import static javax.swing.BorderFactory.createEmptyBorder;

public class IntegrationsFrame extends JFrame
{

	public IntegrationsFrame(InfoPanel infoPanel, Button currentBtn)
	{
		setSize(400, 600);
		setLayout(new BorderLayout());
		setBackground(UIFrame.BACKGROUND_PRIMARY);

		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setBackground(UIFrame.BACKGROUND_PRIMARY);

		int y = 0;
		GridBagConstraints gbc;
		for(String integration : ActionsManager.getAllIntegration())
		{
			JPanel integrationPanel = new JPanel();
			integrationPanel.setLayout(new BorderLayout());
			integrationPanel.setBackground(UIFrame.BACKGROUND_SECONDARY);

			JPanel actionsPanel = new JPanel();

			IIntegration iIntegration = ActionsManager.getIntegrationFromId(integration);
			JButton intBtn = new JButton(iIntegration != null ? iIntegration.getDisplay() : "ERROR");
			intBtn.setForeground(UIFrame.TEXT_PRIMARY);
			intBtn.setUI(new MetalButtonUI());
			intBtn.setBackground(UIFrame.BACKGROUND_SECONDARY);
			intBtn.setFocusPainted(false);
			Font currentFont = intBtn.getFont();
			intBtn.setFont(new Font(currentFont.getName(), Font.PLAIN, 20));
			integrationPanel.add(intBtn, BorderLayout.PAGE_START);
			intBtn.addActionListener(e -> actionsPanel.setVisible(!actionsPanel.isVisible()));

			actionsPanel.setBackground(UIFrame.BACKGROUND_PRIMARY);
			actionsPanel.setLayout(new WrapLayout());
			actionsPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

			for(String action : ActionsManager.getAllActions(integration))
			{
				JButton btn = new JButton();
				try
				{
					btn.setIcon(Util.getScaledImage(new ImageIcon(Util.getRes("icons/" + action + ".png")), 90, 90));
				} catch(Exception e)
				{
					btn.setText(action);
				}
				btn.setPreferredSize(new Dimension(100, 100));
				btn.setUI(new MetalButtonUI());
				btn.setBackground(UIFrame.PRIMARY_MAIN);
				btn.setFocusPainted(false);

				btn.addActionListener(e ->
				{
					currentBtn.addAction(new ActionInfo(integration, action));
					Config.saveProfiles();
					infoPanel.updateActionsPanel();
					infoPanel.updateUI();
					IntegrationsFrame.this.dispose();
				});

				actionsPanel.add(btn);
			}


			integrationPanel.add(actionsPanel, BorderLayout.CENTER);

			gbc = new GridBagConstraints();
			gbc.anchor = GridBagConstraints.NORTH;
			gbc.weightx = 1;
			gbc.weighty = 0;
			gbc.gridy = y;
			gbc.gridx = 0;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			panel.add(integrationPanel, gbc);

			y++;
		}

		gbc = new GridBagConstraints();
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridy = y;
		gbc.gridx = 0;
		gbc.fill = GridBagConstraints.BOTH;
		JPanel spacer = new JPanel();
		spacer.setBackground(UIFrame.BACKGROUND_PRIMARY);
		panel.add(spacer, gbc);

		JScrollPane pane = new JScrollPane(panel,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		pane.setBorder(createEmptyBorder());
		add(pane, BorderLayout.CENTER);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
