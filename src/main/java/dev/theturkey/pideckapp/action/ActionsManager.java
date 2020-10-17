package dev.theturkey.pideckapp.action;

import dev.theturkey.pideckapp.integrations.IAID;
import dev.theturkey.pideckapp.integrations.IIntegration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ActionsManager
{
	private static final Map<String, IIntegration> INTEGRATIONS = new HashMap<>();
	private static final Map<IAID, BaseAction> ACTIONS = new HashMap<>();

	public static void registerAction(IIntegration integration, String actionID, BaseAction action)
	{
		INTEGRATIONS.putIfAbsent(integration.getType(), integration);
		ACTIONS.put(new IAID(integration.getType(), actionID), action);
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


	public static IIntegration getIntegrationFromId(String id)
	{
		return INTEGRATIONS.get(id);
	}

	public static List<String> getAllIntegration()
	{
		return new ArrayList<>(INTEGRATIONS.keySet());
	}

	public static List<String> getAllActions(String integrationID)
	{
		return ACTIONS.keySet().stream().filter(iaid -> iaid.integrationID.equals(integrationID)).map(iaid -> iaid.actionID).collect(Collectors.toList());
	}

}
