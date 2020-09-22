package dev.theturkey.pideckapp.profile;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;

public class ProfileManager
{
	private static final HashMap<String, Profile> PROFILES = new HashMap<>();

	private static Profile currentProfile;

	public static void loadProfiles() throws FileNotFoundException
	{
		JsonElement jsonElement = JsonParser.parseReader(new FileReader(new File("./res/save.json")));
		if(jsonElement.isJsonObject())
		{
			JsonObject json = jsonElement.getAsJsonObject();
			if(json.has("Profiles") && json.get("Profiles").isJsonArray())
			{
				for(JsonElement profileElem : json.getAsJsonArray("Profiles"))
				{
					if(profileElem.isJsonObject())
					{
						Profile profile = new Profile(profileElem.getAsJsonObject());
						PROFILES.put(profile.getName(), profile);
					}
				}
			}
		}

		currentProfile = PROFILES.get("Profile 1");
	}

	public static Profile getCurrentProfile()
	{
		return currentProfile;
	}
}
