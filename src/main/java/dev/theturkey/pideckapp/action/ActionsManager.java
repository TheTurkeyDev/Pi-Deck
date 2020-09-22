package dev.theturkey.pideckapp.action;

import dev.theturkey.pideckapp.integrations.IIntegration;

import java.util.HashMap;
import java.util.Map;

public class ActionsManager
{
	private static final Map<String, BaseAction> ACTIONS = new HashMap<>();

	public static void registerAction(IIntegration integration, String actionID, BaseAction action)
	{
		registerAction(integration.getType(), actionID, action);
	}

	public static void registerAction(String integration, String actionID, BaseAction action)
	{
		ACTIONS.put(integration + "/" + actionID, action);
	}

	public static BaseAction getAction(String integration, String actionID)
	{
		return getAction(integration + "/" + actionID);
	}

	public static BaseAction getAction(String fullActionID)
	{
		return ACTIONS.get(fullActionID);
	}

}
