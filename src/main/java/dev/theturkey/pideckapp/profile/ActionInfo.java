package dev.theturkey.pideckapp.profile;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.theturkey.pideckapp.action.ActionsManager;
import dev.theturkey.pideckapp.integrations.ActionProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionInfo
{
	private String type;
	private String action;
	private long delay = 0;
	private Map<String, String> props;

	public ActionInfo(String type, String action)
	{
		this.type = type;
		this.action = action;
		props = new HashMap<>();
	}

	public ActionInfo(JsonObject json)
	{
		if(json.has("type") && json.get("type").isJsonPrimitive())
			type = json.get("type").getAsString();

		if(json.has("action") && json.get("action").isJsonPrimitive())
			action = json.get("action").getAsString();

		if(json.has("delay") && json.get("delay").isJsonPrimitive())
			delay = json.get("delay").getAsLong();

		props = new HashMap<>();
		if(json.has("props") && json.get("props").isJsonObject())
			for(Map.Entry<String, JsonElement> entry : json.getAsJsonObject("props").entrySet())
				if(entry.getValue().isJsonPrimitive())
					props.put(entry.getKey(), entry.getValue().getAsString());
	}

	public JsonObject saveAction()
	{
		JsonObject json = new JsonObject();
		json.addProperty("type", this.getType());
		json.addProperty("action", this.getAction());
		json.addProperty("delay", this.getDelay());
		JsonObject propsJson = new JsonObject();
		for(Map.Entry<String, String> prop : props.entrySet())
			propsJson.addProperty(prop.getKey(), prop.getValue());

		json.add("props", propsJson);

		return json;
	}


	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getAction()
	{
		return action;
	}

	public void setAction(String action)
	{
		this.action = action;
	}

	public long getDelay()
	{
		return delay;
	}

	public Map<String, String> getProps()
	{
		return props;
	}

	public void setPropsValue(String key, String val)
	{
		this.props.put(key, val);
	}

	public List<ActionProperty> getDefaultProps()
	{
		if(type.isEmpty() || action.isEmpty())
			return new ArrayList<>();
		return ActionsManager.getAction(type, action).getPropDefs();
	}
}
