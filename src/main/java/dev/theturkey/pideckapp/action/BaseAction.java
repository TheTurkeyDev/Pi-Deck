package dev.theturkey.pideckapp.action;

import dev.theturkey.pideckapp.integrations.ActionProperty;
import dev.theturkey.pideckapp.profile.ActionInfo;

import java.util.List;

public abstract class BaseAction
{
	public void run(ActionInfo info)
	{

	}

	public abstract List<ActionProperty> getPropDefs();
}
