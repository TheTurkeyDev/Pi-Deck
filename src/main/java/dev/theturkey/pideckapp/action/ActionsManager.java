package dev.theturkey.pideckapp.action;

import dev.theturkey.pideckapp.integrations.IAID;
import dev.theturkey.pideckapp.integrations.IIntegration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ActionsManager
{
	private static final Map<IAID, BaseAction> ACTIONS = new HashMap<>();

	public static void registerAction(IIntegration integration, String actionID, BaseAction action)
	{
		registerAction(integration.getType(), actionID, action);
	}

	public static void registerAction(String integration, String actionID, BaseAction action)
	{
		ACTIONS.put(new IAID(integration, actionID), action);
	}

	public static BaseAction getAction(String integration, String actionID)
	{
		return getAction(new IAID(integration, actionID));
	}

	public static BaseAction getAction(String fullActionID)
	{
		return getAction(new IAID(fullActionID));
	}

	public static BaseAction getAction(IAID fullActionID)
	{
		return ACTIONS.get(fullActionID);
	}


	public static List<String> getAllIntegration()
	{
		return ACTIONS.keySet().stream().map(iaid -> iaid.integrationID).distinct().collect(Collectors.toList());
	}

	public static List<String> getAllActions(String integrationID)
	{
		return ACTIONS.keySet().stream().filter(iaid -> iaid.integrationID.equals(integrationID)).map(iaid -> iaid.actionID).collect(Collectors.toList());
	}

}
