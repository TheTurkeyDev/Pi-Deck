package dev.theturkey.pideckapp.integrations;

import dev.theturkey.pideckapp.action.ActionsManager;
import dev.theturkey.pideckapp.action.misc.SwitchProfileAction;

public class MiscIntegration implements IIntegration
{
	@Override
	public void load()
	{
		ActionsManager.registerAction(this, "switch_profile", new SwitchProfileAction());
	}

	@Override
	public String getType()
	{
		return "misc";
	}

	@Override
	public String getDisplay()
	{
		return "Misc";
	}
}