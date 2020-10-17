package dev.theturkey.pideckapp.ui;

import dev.theturkey.pideckapp.Util;
import dev.theturkey.pideckapp.config.Config;
import dev.theturkey.pideckapp.connection.ConnectionManager;
import dev.theturkey.pideckapp.profile.Profile;
import dev.theturkey.pideckapp.profile.ProfileManager;
import dev.theturkey.pideckapp.ui.subframes.AddProfileFrame;
import dev.theturkey.pideckapp.ui.subframes.PiDeckConnectFrame;

import javax.swing.*;
import javax.swing.plaf.metal.MetalButtonUI;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class UIFrame extends JFrame
{
	public static final Color BACKGROUND_PRIMARY = Util.hex2Rgb("#212529");
	public static final Color BACKGROUND_SECONDARY = Util.hex2Rgb("#343a40");
	public static final Color TEXT_PRIMARY = Util.hex2Rgb("#d1d1d1");
	public static final Color TEXT_LIGHT = Util.hex2Rgb("#ffffff");
	public static final Color PRIMARY_MAIN = Util.hex2Rgb("#009FBF");

	private SystemTray tray;
	private TrayIcon trayIcon;

	private SimScreen simScreen;
	private InfoPanel infoPanel;

	private JSplitPane sp;
	private JSplitPane sp3;

	private JComboBox<Profile> profilesComboBox;
	private JSpinner rowsSpinner;
	private JSpinner columnsSpinner;

	public UIFrame()
	{
		simScreen = new SimScreen();

		setLayout(new BorderLayout());

		JPanel topBar = new JPanel();
		topBar.setBackground(UIFrame.BACKGROUND_PRIMARY);

		JButton addProfileBtn = new JButton("Connect");
		addProfileBtn.setUI(new MetalButtonUI());
		addProfileBtn.addActionListener(e -> new PiDeckConnectFrame(this));
		addProfileBtn.getInsets().set(0, 5, 0, 5);
		addProfileBtn.setForeground(UIFrame.TEXT_LIGHT);
		addProfileBtn.setBackground(UIFrame.PRIMARY_MAIN);
		addProfileBtn.setOpaque(true);
		addProfileBtn.setFocusPainted(false);
		topBar.add(addProfileBtn);

		JLabel profileLabel = new JLabel("Profile:");
		profileLabel.setForeground(UIFrame.TEXT_PRIMARY);
		topBar.add(profileLabel);

		Profile[] profs = ProfileManager.getProfiles().toArray(new Profile[0]);
		profilesComboBox = new JComboBox<>(profs);
		int index = 0;
		for(int i = 0; i < profs.length; i++)
		{
			if(profs[i].getName().equals(ProfileManager.getCurrentProfile().getName()))
			{
				index = i;
				break;
			}
		}
		profilesComboBox.setSelectedIndex(index);
		profilesComboBox.addItemListener((e) ->
		{
			Profile prof = (Profile) e.getItem();
			switchProfile(prof.getName());
		});
		topBar.add(profilesComboBox);

		JButton addPiDeckBtn = new JButton("Add");
		addPiDeckBtn.setUI(new MetalButtonUI());
		addPiDeckBtn.addActionListener(e -> new AddProfileFrame(this));
		addPiDeckBtn.getInsets().set(0, 5, 0, 5);
		addPiDeckBtn.setForeground(UIFrame.TEXT_LIGHT);
		addPiDeckBtn.setBackground(UIFrame.PRIMARY_MAIN);
		addPiDeckBtn.setOpaque(true);
		addPiDeckBtn.setFocusPainted(false);
		topBar.add(addPiDeckBtn);

		JLabel rowsLabel = new JLabel("Rows");
		rowsLabel.setForeground(UIFrame.TEXT_PRIMARY);
		topBar.add(rowsLabel);
		rowsSpinner = new JSpinner(new SpinnerNumberModel(ProfileManager.getCurrentProfile().getRows(), 1, 10, 1));
		rowsSpinner.setBackground(UIFrame.BACKGROUND_SECONDARY);
		rowsSpinner.setForeground(UIFrame.TEXT_PRIMARY);
		rowsSpinner.addChangeListener(e ->
		{
			simScreen.setRows((int) rowsSpinner.getModel().getValue());
		});
		rowsSpinner.setSize(100, 25);
		topBar.add(rowsSpinner);

		JLabel colsLabel = new JLabel("Columns");
		colsLabel.setForeground(UIFrame.TEXT_PRIMARY);
		topBar.add(colsLabel);
		columnsSpinner = new JSpinner(new SpinnerNumberModel(ProfileManager.getCurrentProfile().getColumns(), 1, 10, 1));
		columnsSpinner.setBackground(UIFrame.BACKGROUND_SECONDARY);
		columnsSpinner.setForeground(UIFrame.TEXT_PRIMARY);
		columnsSpinner.addChangeListener(e ->
		{
			simScreen.setColumns((int) columnsSpinner.getModel().getValue());
		});
		columnsSpinner.setSize(100, 25);
		topBar.add(columnsSpinner);

		JButton saveBtn = new JButton("SAVE");
		saveBtn.setUI(new MetalButtonUI());
		saveBtn.addActionListener(e -> ConnectionManager.getCurrentConnection().updatePiDisplay());
		saveBtn.getInsets().set(0, 5, 0, 5);
		saveBtn.setForeground(UIFrame.TEXT_LIGHT);
		saveBtn.setBackground(UIFrame.PRIMARY_MAIN);
		saveBtn.setOpaque(true);
		saveBtn.setFocusPainted(false);
		topBar.add(saveBtn);

		ImageIcon loading = new ImageIcon(Util.getRes("loading.gif"));
		ImageIcon check = Util.getScaledImage(new ImageIcon(Util.getRes("icons/check_mark.png")), 16, 16);
		ImageIcon xmark = Util.getScaledImage(new ImageIcon(Util.getRes("icons/x_mark.png")), 16, 16);
		topBar.add(new JLabel("", xmark, JLabel.CENTER));

		add(topBar, BorderLayout.PAGE_START);

		JPanel simContainer = new JPanel();
		simContainer.setBackground(UIFrame.BACKGROUND_PRIMARY);
		simContainer.add(simScreen);

		infoPanel = new InfoPanel();
		sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, simContainer, infoPanel);
		sp.setContinuousLayout(true);
		sp.setDividerSize(3);
		sp.setBorder(BorderFactory.createEmptyBorder());
		sp.setResizeWeight(1.0);

		sp3 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, sp, new BottomPanel());

		sp3.setContinuousLayout(true);
		sp3.setDividerSize(1);
		add(sp3, BorderLayout.CENTER);

		setSize(1280, 720);

		setVisible(true);

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				int i = JOptionPane.showConfirmDialog(null, "Are you sure you want to close?");
				if(i == 0)
				{
					if(ConnectionManager.isConnected())
						ConnectionManager.getCurrentConnection().close();
					System.exit(0);
				}
			}
		});

		if(SystemTray.isSupported())
		{
			tray = SystemTray.getSystemTray();

			Image image = Toolkit.getDefaultToolkit().getImage(Util.getRes("turkeyDerp.png"));
			PopupMenu popup = new PopupMenu();
			MenuItem defaultItem = new MenuItem("Exit");
			defaultItem.addActionListener(e ->
			{
				ConnectionManager.getCurrentConnection().close();
				System.exit(0);
			});
			popup.add(defaultItem);
			defaultItem = new MenuItem("Open");
			defaultItem.addActionListener(e ->
			{
				setVisible(true);
				setExtendedState(Frame.NORMAL);
			});
			popup.add(defaultItem);
			trayIcon = new TrayIcon(image, "PiDeck App", popup);
			trayIcon.setImageAutoSize(true);
		}
		else
		{
			System.out.println("system tray not supported");
		}
		addWindowStateListener(e ->
		{
			if(e.getNewState() == ICONIFIED)
			{
				try
				{
					tray.add(trayIcon);
					setVisible(false);
				} catch(AWTException ex)
				{
					System.out.println("unable to add to tray");
				}
			}
			if(e.getNewState() == 7)
			{
				try
				{
					tray.add(trayIcon);
					setVisible(false);
				} catch(AWTException ex)
				{
					System.out.println("unable to add to system tray");
				}
			}
			if(e.getNewState() == MAXIMIZED_BOTH)
			{
				tray.remove(trayIcon);
				setVisible(true);
			}
			if(e.getNewState() == NORMAL)
			{
				tray.remove(trayIcon);
				setVisible(true);
			}
		});
		setIconImage(Toolkit.getDefaultToolkit().getImage(Util.getRes("turkeyDerp.png")));

		System.out.println("UI DONE");
	}

	public void setInfoPanelButton(String buttonID)
	{
		infoPanel.setInfoPanelButton(ProfileManager.getCurrentProfile().getButtonFromID(buttonID));
		sp.resetToPreferredSizes();
	}

	public void refreshProfilesComboBox()
	{
		profilesComboBox.setModel(new DefaultComboBoxModel<>(ProfileManager.getProfiles().toArray(new Profile[0])));
	}

	public void updateSim()
	{
		//TODO: This is gross and very not efficient
		simScreen.setupButtons();
	}

	public void switchProfile(String profile)
	{
		Profile prof = ProfileManager.getProfileFromName(profile);
		if(prof == null || prof.equals(ProfileManager.getCurrentProfile()))
			return;

		ProfileManager.setCurrentProfile(prof);
		Config.saveProfiles();
		updateSim();
		if(ConnectionManager.isConnected())
			ConnectionManager.getCurrentConnection().updatePiDisplay();
		setInfoPanelButton(null);
		columnsSpinner.setValue(prof.getColumns());
		rowsSpinner.setValue(prof.getRows());
	}
}
