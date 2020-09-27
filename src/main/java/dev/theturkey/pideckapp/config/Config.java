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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

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
				if(profilesFile.createNewFile())
				{
					JsonObject json = new JsonObject();
					JsonArray profilesArray = new JsonArray();
					JsonObject profileJson = new JsonObject();
					profileJson.addProperty("name", "Default Profile");
					profileJson.addProperty("columns", 4);
					profileJson.addProperty("rows", 2);
					profileJson.add("buttons", new JsonObject());
					profilesArray.add(profileJson);
					json.add("Profiles", profilesArray);
					try(Writer writer = new OutputStreamWriter(new FileOutputStream(profilesFile), StandardCharsets.UTF_8))
					{
						GSON.toJson(json, writer);
					}
				}
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
			return JsonParser.parseReader(new InputStreamReader(new FileInputStream(profilesFile), StandardCharsets.UTF_8));
		} catch(Exception e)
		{
			e.printStackTrace();
		}

		return JsonNull.INSTANCE;
	}

	public static void saveProfiles()
	{
		try(Writer writer = new OutputStreamWriter(new FileOutputStream(profilesFile), StandardCharsets.UTF_8))
		{
			//System.out.println("SAVING!");
			GSON.toJson(ProfileManager.saveProfiles(), writer);
		} catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
