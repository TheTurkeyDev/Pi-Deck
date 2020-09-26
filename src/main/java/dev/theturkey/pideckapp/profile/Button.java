package dev.theturkey.pideckapp.profile;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.theturkey.pideckapp.Util;
import dev.theturkey.pideckapp.action.ActionsManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Button
{
	private String id;
	private String imageSrc = "";
	private String bgColor = "";
	private List<ActionInfo> actions = new ArrayList<>();
	private int x;
	private int y;

	public Button(int x, int y)
	{
		this.id = Util.genUUID();
		this.x = x;
		this.y = y;
	}

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

	public JsonObject saveButton()
	{
		JsonObject json = new JsonObject();
		json.addProperty("x", this.getX());
		json.addProperty("y", this.getY());
		json.addProperty("img", this.imageSrc);
		json.addProperty("bg_color", this.getBgColor());
		JsonArray actionsArray = new JsonArray();
		for(ActionInfo action : actions)
			actionsArray.add(action.saveAction());
		json.add("actions", actionsArray);

		return json;
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

	public void setBgColor(Color color)
	{
		this.bgColor = "#" + Integer.toHexString(color.getRGB()).substring(2);
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

	public List<ActionInfo> getActions()
	{
		return this.actions;
	}

	public void addAction(ActionInfo actionInfo)
	{
		this.actions.add(actionInfo);
	}

	public void removeAction(ActionInfo actionInfo)
	{
		this.actions.remove(actionInfo);
	}
}
