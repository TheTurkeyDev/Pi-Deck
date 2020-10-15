package dev.theturkey.pideckapp.profile;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Profile
{
	private String name;
	private Map<String, Button> buttons;
	private int columns;
	private int rows;

	public Profile(String name, int columns, int rows)
	{
		this.name = name;
		this.columns = columns;
		this.rows = rows;
		buttons = new HashMap<>();
	}

	public Profile(JsonObject json)
	{
		if(json.has("name"))
			this.name = json.get("name").getAsString();

		if(json.has("columns"))
			this.columns = json.get("columns").getAsInt();
		if(json.has("rows"))
			this.rows = json.get("rows").getAsInt();

		this.buttons = new HashMap<>();
		if(json.has("buttons") && json.get("buttons").isJsonObject())
			for(Map.Entry<String, JsonElement> btn : json.getAsJsonObject("buttons").entrySet())
				this.buttons.put(btn.getKey(), new Button(btn.getKey(), btn.getValue().getAsJsonObject()));
	}

	public JsonObject saveProfile()
	{
		JsonObject json = new JsonObject();
		json.addProperty("name", this.getName());
		json.addProperty("rows", rows);
		json.addProperty("columns", columns);

		JsonObject buttonsJson = new JsonObject();
		for(Button btn : buttons.values())
			buttonsJson.add(btn.getID(), btn.saveButton());

		json.add("buttons", buttonsJson);

		return json;
	}

	public String getName()
	{
		return name;
	}

	public List<Button> getVisibleButtons()
	{
		return buttons.values().stream().filter(btn -> btn.getX() < columns && btn.getY() < rows).collect(Collectors.toList());
	}

	public Button getButtonAt(int x, int y)
	{
		Button button = buttons.values().stream().filter(btn -> btn.getX() == x && btn.getY() == y).findFirst().orElse(null);
		if(button == null)
		{
			button = new Button(x, y);
			this.buttons.put(button.getID(), button);
		}
		return button;
	}

	public Button getButtonFromID(String id)
	{
		return buttons.get(id);
	}

	public void onButtonPress(String buttonID)
	{
		buttons.get(buttonID).onClick();
	}

	public int getColumns()
	{
		return columns;
	}

	public void setColumns(int columns)
	{
		this.columns = columns;
	}

	public int getRows()
	{
		return rows;
	}

	public void setRows(int rows)
	{
		this.rows = rows;
	}

	@Override
	public String toString()
	{
		return name;
	}
}
