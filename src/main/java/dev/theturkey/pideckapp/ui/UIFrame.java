package dev.theturkey.pideckapp.ui;

import dev.theturkey.pideckapp.Core;
import dev.theturkey.pideckapp.Util;
import dev.theturkey.pideckapp.profile.ProfileManager;

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

	public UIFrame()
	{
		simScreen = new SimScreen();

		setLayout(new BorderLayout());

		JPanel topBar = new JPanel();
		topBar.setBackground(UIFrame.BACKGROUND_PRIMARY);

		JLabel rowsLabel = new JLabel("Rows");
		rowsLabel.setForeground(UIFrame.TEXT_PRIMARY);
		topBar.add(rowsLabel);
		JSpinner rows = new JSpinner(new SpinnerNumberModel(2, 1, 10, 1));
		rows.setBackground(UIFrame.BACKGROUND_SECONDARY);
		rows.setForeground(UIFrame.TEXT_PRIMARY);
		rows.addChangeListener(e ->
		{
			simScreen.setRows((int) ((JSpinner) e.getSource()).getModel().getValue());
		});
		rows.setSize(100, 25);
		topBar.add(rows);

		JLabel colsLabel = new JLabel("Columns");
		colsLabel.setForeground(UIFrame.TEXT_PRIMARY);
		topBar.add(colsLabel);
		JSpinner columns = new JSpinner(new SpinnerNumberModel(4, 1, 10, 1));
		columns.setBackground(UIFrame.BACKGROUND_SECONDARY);
		columns.setForeground(UIFrame.TEXT_PRIMARY);
		columns.addChangeListener(e ->
		{
			simScreen.setColumns((int) ((JSpinner) e.getSource()).getModel().getValue());
		});
		columns.setSize(100, 25);
		topBar.add(columns);

		JButton saveBtn = new JButton("SAVE");
		saveBtn.setUI(new MetalButtonUI());
		saveBtn.addActionListener(e -> Core.getPiDeck().updatePiDisplay());
		saveBtn.getInsets().set(0, 5, 0, 5);
		saveBtn.setForeground(UIFrame.TEXT_LIGHT);
		saveBtn.setBackground(UIFrame.PRIMARY_MAIN);
		saveBtn.setOpaque(true);
		saveBtn.setFocusPainted(false);
		topBar.add(saveBtn);

		ImageIcon loading = new ImageIcon(Util.getRes("loading.gif"));
		ImageIcon check = new ImageIcon(Util.getScaledImage((new ImageIcon(Util.getRes("icons/check_mark.png"))).getImage(), 16, 16));
		ImageIcon xmark = new ImageIcon(Util.getScaledImage((new ImageIcon(Util.getRes("icons/x_mark.png"))).getImage(), 16, 16));
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
					Core.getPiDeck().close();
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
				Core.getPiDeck().close();
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
			trayIcon = new TrayIcon(image, "SystemTray Demo", popup);
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

	public void updateSim()
	{
		//TODO: This is gross and very not efficient
		simScreen.setupButtons();
	}
}
