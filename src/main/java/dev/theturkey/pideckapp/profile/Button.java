package dev.theturkey.pideckapp.profile;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.theturkey.pideckapp.action.ActionsManager;

import java.util.ArrayList;
import java.util.List;

public class Button
{
	private String id;
	private String imageSrc = "";
	private String bgColor = "";
	private List<ActionInfo> actions;
	private int x;
	private int y;

	public Button(String id, JsonObject json)
	{
		this.id = id;

		if(json.has("x") && json.get("x").isJsonPrimitive())
			x = json.get("x").getAsInt();

		if(json.has("y") && json.get("y").isJsonPrimitive())
			y = json.get("y").getAsInt();

		if(json.has("img") && json.get("img").isJsonPrimitive())
			imageSrc = json.get("img").getAsString();

		if(json.has("bg_color") && json.get("bg_color").isJsonPrimitive())
			bgColor = json.get("bg_color").getAsString();

		actions = new ArrayList<>();
		if(json.has("actions") && json.get("actions").isJsonArray())
			for(JsonElement actionElem : json.getAsJsonArray("actions"))
				if(actionElem.isJsonObject())
					actions.add(new ActionInfo(actionElem.getAsJsonObject()));
	}

	public void onClick()
	{
		for(ActionInfo actionInfo : actions)
			ActionsManager.getAction(actionInfo.getType(), actionInfo.getAction()).run(actionInfo);
	}

	public String getBgColor()
	{
		return this.bgColor;
	}

	public int getX()
	{
		return this.x;
	}

	public int getY()
	{
		return this.y;
	}

	public String getID()
	{
		return this.id;
	}
}
