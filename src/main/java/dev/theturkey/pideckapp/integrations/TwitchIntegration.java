package dev.theturkey.pideckapp.integrations;

import dev.theturkey.pideckapp.action.ActionsManager;
import dev.theturkey.pideckapp.action.io.TypingAction;

public class TwitchIntegration implements IIntegration
{

	@Override
	public void load()
	{
		ActionsManager.registerAction(this, "message", new TypingAction());
	}

	@Override
	public String getType()
	{
		return "twitch";
	}

	@Override
	public String getDisplay()
	{
		return "Twitch";
	}
}
