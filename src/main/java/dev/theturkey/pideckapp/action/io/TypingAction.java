package dev.theturkey.pideckapp.action.io;

import dev.theturkey.pideckapp.action.BaseAction;
import dev.theturkey.pideckapp.integrations.ActionProperty;
import dev.theturkey.pideckapp.profile.ActionInfo;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class TypingAction extends BaseAction
{
	@Override
	public void run(ActionInfo info)
	{
		super.run(info);

		try
		{
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			String str = info.getProps().getOrDefault("message", "No message specified");
			StringSelection selec = new StringSelection(str);
			clipboard.setContents(selec, selec);
			Robot r = new Robot();
			r.keyPress(KeyEvent.VK_CONTROL);
			r.keyPress(KeyEvent.VK_V);
			r.keyRelease(KeyEvent.VK_V);
			r.keyRelease(KeyEvent.VK_CONTROL);
			clipboard.setContents(selec, selec);
		} catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public List<ActionProperty> getPropDefs()
	{
		return Arrays.asList(
				new ActionProperty("message", ActionProperty.PropType.STRING, "")
		);
	}

	private static String escapeNonAscii(String str)
	{

		StringBuilder retStr = new StringBuilder();
		for(int i = 0; i < str.length(); i++)
		{
			int cp = Character.codePointAt(str, i);
			int charCount = Character.charCount(cp);
			if(charCount > 1)
			{
				i += charCount - 1; // 2.
				if(i >= str.length())
				{
					throw new IllegalArgumentException("truncated unexpectedly");
				}
			}

			if(cp < 128)
			{
				retStr.appendCodePoint(cp);
			}
			else
			{
				retStr.append(String.format("\\u%x", cp));
			}
		}
		return retStr.toString();
	}
}
