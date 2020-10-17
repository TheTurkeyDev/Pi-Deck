package dev.theturkey.pideckapp.profile;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.theturkey.pideckapp.config.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProfileManager
{
	private static final HashMap<String, Profile> PROFILES = new HashMap<>();

	private static Profile currentProfile;

	public static void loadProfiles()
	{
		JsonElement jsonElement = Config.loadProfiles();
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

			String currentProfileJson = json.has("Current Profile") ? json.get("Current Profile").getAsString() : "";

			if(currentProfileJson.isEmpty() || !PROFILES.containsKey(currentProfileJson))
			{
				currentProfile = PROFILES.get(PROFILES.keySet().stream().findFirst().orElseThrow(() -> new RuntimeException("Missing Profiles Exception")));
				Config.saveProfiles();
			}
			else
			{
				currentProfile = PROFILES.get(currentProfileJson);
			}
		}
	}

	public static JsonObject saveProfiles()
	{
		JsonObject save = new JsonObject();
		JsonArray profilesArray = new JsonArray();
		for(Profile profile : PROFILES.values())
			profilesArray.add(profile.saveProfile());

		save.add("Profiles", profilesArray);
		save.addProperty("Current Profile", currentProfile.getName());

		return save;
	}

	public static Profile getCurrentProfile()
	{
		return currentProfile;
	}

	public static List<Profile> getProfiles()
	{
		return new ArrayList<>(PROFILES.values());
	}

	public static void addProfile(Profile profile)
	{
		PROFILES.put(profile.getName(), profile);
	}

	public static void setCurrentProfile(Profile profile)
	{
		currentProfile = profile;
	}

	public static Profile getProfileFromName(String name)
	{
		return PROFILES.get(name);
	}
}
