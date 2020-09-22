package dev.theturkey.pideckapp.profile;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class ActionInfo
{
	private String type;
	private String action;
	private long delay = 0;
	private Map<String, String> props;

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


	public String getType()
	{
		return type;
	}

	public String getAction()
	{
		return action;
	}

	public long getDelay()
	{
		return delay;
	}

	public Map<String, String> getProps()
	{
		return props;
	}
}
