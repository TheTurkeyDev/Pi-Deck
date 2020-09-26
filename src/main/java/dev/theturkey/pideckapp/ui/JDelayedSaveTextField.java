package dev.theturkey.pideckapp.ui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class JDelayedSaveTextField extends JTextField implements DocumentListener
{
	private Timer t;
	private Callback callback;

	public JDelayedSaveTextField(String defaultVal, Callback callback)
	{
		super(defaultVal);
		this.callback = callback;
		setForeground(UIFrame.TEXT_PRIMARY);
		getDocument().addDocumentListener(this);
	}

	@Override
	public void insertUpdate(DocumentEvent e)
	{
		onChange();
	}

	@Override
	public void removeUpdate(DocumentEvent e)
	{
		onChange();
	}

	@Override
	public void changedUpdate(DocumentEvent e)
	{
		onChange();
	}

	public void onChange()
	{
		if(t != null && t.isRunning())
		{
			t.restart();
		}
		else
		{
			t = new Timer(1000, x ->
			{
				t.stop();
				callback.callback(this.getText());
			});

			t.setRepeats(false);
			t.start();
		}
	}

	public interface Callback
	{
		void callback(String text);
	}
}
