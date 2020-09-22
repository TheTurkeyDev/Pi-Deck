package dev.theturkey.pideckapp.profile;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.theturkey.pideckapp.Core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Profile
{
	private String name;
	private Map<String, Button> buttons;
	private List<String> visibleButtons;

	public Profile(String name, Map<String, Button> buttons, List<String> initalButtons)
	{
		this.name = name;
		this.buttons = buttons;
		this.visibleButtons = initalButtons;
	}

	public Profile(JsonObject json)
	{
		if(json.has("name"))
			this.name = json.get("name").getAsString();

		this.buttons = new HashMap<>();
		if(json.has("buttons") && json.get("buttons").isJsonObject())
			for(Map.Entry<String, JsonElement> btn : json.getAsJsonObject("buttons").entrySet())
				this.buttons.put(btn.getKey(), new Button(btn.getKey(), btn.getValue().getAsJsonObject()));
	}

	public String getName()
	{
		return name;
	}

	public List<Button> getVisibleButtons()
	{
		return buttons.values().stream().filter(btn -> btn.getX() < Core.displayColumns && btn.getY() < Core.displayRows).collect(Collectors.toList());
	}

	public Button getButtonAt(int x, int y)
	{
		return buttons.values().stream().filter(btn -> btn.getX() == x && btn.getY() == y).findFirst().orElse(null);
	}

	public Button getButtonFromID(String id)
	{
		return buttons.get(id);
	}

	public void onButtonPress(String buttonID)
	{
		buttons.get(buttonID).onClick();
	}
}
