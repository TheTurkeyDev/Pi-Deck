package dev.theturkey.pideckapp.integrations;

import dev.theturkey.pideckapp.action.ActionsManager;
import dev.theturkey.pideckapp.action.io.KeyAction;
import dev.theturkey.pideckapp.action.io.TypingAction;

public class IOIntegration implements IIntegration
{
	@Override
	public void load()
	{
		ActionsManager.registerAction(this, "type", new TypingAction());
		ActionsManager.registerAction(this, "key", new KeyAction());
	}

	@Override
	public String getType()
	{
		return "io";
	}
}
