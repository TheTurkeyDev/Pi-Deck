package dev.theturkey.pideckapp.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.theturkey.pideckapp.profile.ProfileManager;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Config
{
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static File folder;
	private static File profilesFile;

	public static boolean init()
	{
		try
		{
			folder = new File(System.getProperty("user.home"), ".pideck");
			if(!folder.exists())
			{
				folder.mkdirs();
			}

			profilesFile = new File(folder, "profiles.json");
			if(!profilesFile.exists())
			{
				profilesFile.createNewFile();
				JsonObject json = new JsonObject();
				JsonArray profilesArray = new JsonArray();
				JsonObject profileJson = new JsonObject();
				profileJson.addProperty("name", "Default Profile");
				profileJson.add("buttons", new JsonObject());
				profilesArray.add(profileJson);
				json.add("Profiles", profilesArray);
				GSON.toJson(json, new FileWriter(profilesFile));
			}
		} catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static JsonElement loadProfiles()
	{
		try
		{
			return JsonParser.parseReader(new FileReader(profilesFile));
		} catch(Exception e)
		{
			e.printStackTrace();
		}

		return JsonNull.INSTANCE;
	}

	public static void saveProfiles()
	{
		try(FileWriter writer = new FileWriter(profilesFile))
		{
			System.out.println("SAVING!");
			GSON.toJson(ProfileManager.saveProfiles(), writer);
		} catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
