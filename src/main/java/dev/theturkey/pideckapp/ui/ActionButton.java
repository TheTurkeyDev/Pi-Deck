package dev.theturkey.pideckapp.ui;

import dev.theturkey.pideckapp.profile.Button;

import javax.swing.*;
import java.awt.*;


public class ActionButton extends JPanel
{

	public ActionButton(Button btn)
	{
		if(btn == null)
			return;

		setBackground(UIFrame.hex2Rgb(btn.getBgColor()));
	}
}
