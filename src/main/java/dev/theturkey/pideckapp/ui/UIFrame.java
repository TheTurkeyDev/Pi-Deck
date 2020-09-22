package dev.theturkey.pideckapp.ui;

import dev.theturkey.pideckapp.Core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class UIFrame extends JFrame
{
	private static SystemTray tray;
	private static TrayIcon trayIcon;

	public UIFrame()
	{
		SimScreen screen = new SimScreen();

		setLayout(new BorderLayout());

		JPanel topBar = new JPanel();
		topBar.setBackground(hex2Rgb("#212529"));

		JLabel rowsLabel = new JLabel("Rows");
		rowsLabel.setForeground(hex2Rgb("#d1d1d1"));
		topBar.add(rowsLabel);
		JSpinner rows = new JSpinner(new SpinnerNumberModel(2, 1, 10, 1));
		rows.setBackground(hex2Rgb("#343a40"));
		rows.setForeground(hex2Rgb("#d1d1d1"));
		rows.addChangeListener(e ->
		{
			screen.setRows((int) ((JSpinner) e.getSource()).getModel().getValue());
		});
		rows.setSize(100, 25);
		topBar.add(rows);

		JLabel colsLabel = new JLabel("Columns");
		colsLabel.setForeground(hex2Rgb("#d1d1d1"));
		topBar.add(colsLabel);
		JSpinner columns = new JSpinner(new SpinnerNumberModel(4, 1, 10, 1));
		columns.setBackground(hex2Rgb("#343a40"));
		columns.setForeground(hex2Rgb("#d1d1d1"));
		columns.addChangeListener(e ->
		{
			screen.setColumns((int) ((JSpinner) e.getSource()).getModel().getValue());
		});
		columns.setSize(100, 25);
		topBar.add(columns);

		JButton saveBtn = new JButton("SAVE");
		saveBtn.addActionListener(e -> Core.getPiDeck().updatePiDisplay());
		saveBtn.getInsets().set(0, 5, 0, 5);
		saveBtn.setForeground(hex2Rgb("#d1d1d1"));
		saveBtn.setBackground(hex2Rgb("#343a40"));
		saveBtn.setOpaque(true);
		saveBtn.setBorderPainted(false);
		topBar.add(saveBtn);

		add(topBar, BorderLayout.PAGE_START);

		JPanel simContainer = new JPanel();
		simContainer.setBackground(hex2Rgb("#212529"));
		simContainer.add(screen);

		JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, simContainer, new InfoPanel());
		sp.setContinuousLayout(true);
		sp.setDividerSize(3);
		sp.setBorder(BorderFactory.createEmptyBorder());

		JSplitPane sp2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new LeftPanel(), sp);
		sp2.setContinuousLayout(true);
		sp2.setDividerSize(3);
		sp2.setBorder(BorderFactory.createEmptyBorder());

		JSplitPane sp3 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, sp2, new BottomPanel());

		sp3.setContinuousLayout(true);
		sp3.setDividerSize(1);
		add(sp3, BorderLayout.CENTER);

		setSize(1280, 720);

		setVisible(true);


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

			Image image = Toolkit.getDefaultToolkit().getImage("./res/turkeyDerp.png");
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
		setIconImage(Toolkit.getDefaultToolkit().getImage("./res/turkeyDerp.png"));
	}

	public static Color hex2Rgb(String colorStr)
	{
		if(colorStr.startsWith("#"))
			colorStr = colorStr.substring(1);
		return new Color(
				Integer.valueOf(colorStr.substring(0, 2), 16),
				Integer.valueOf(colorStr.substring(2, 4), 16),
				Integer.valueOf(colorStr.substring(4, 6), 16));
	}
}
