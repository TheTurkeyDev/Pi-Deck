package dev.theturkey.pideckapp;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.theturkey.pideckapp.profile.Button;
import dev.theturkey.pideckapp.profile.Profile;
import dev.theturkey.pideckapp.profile.ProfileManager;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class PiDeckConnection
{
	private DataOutputStream os = null;
	private Socket clientSocket = null;
	private BufferedReader is = null;

	public void connect()
	{
		String hostname = "raspberrypi.local";
		int port = 49494;

		try
		{
			clientSocket = new Socket(hostname, port);
			os = new DataOutputStream(clientSocket.getOutputStream());
			is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} catch(UnknownHostException e)
		{
			System.err.println("Don't know about host: " + hostname);
		} catch(IOException e)
		{
			System.err.println("Couldn't get I/O for the connection to: " + hostname);
		}

		if(clientSocket == null || os == null || is == null)
		{
			System.err.println("Something is wrong. One variable is null.");
			return;
		}

		Thread t = new Thread(() ->
		{
			boolean closed = false;
			while(clientSocket.isConnected() && !closed)
			{
				try
				{
					String responseLine = is.readLine();
					JsonObject json = JsonParser.parseString(responseLine).getAsJsonObject();
					switch(json.get("event").getAsString())
					{
						case "click":
							ProfileManager.getCurrentProfile().onButtonPress(json.get("id").getAsString());
							break;
						case "ping":
							JsonObject pong = new JsonObject();
							pong.addProperty("event", "pong");
							sendMessage(pong);
							break;
						default:
							System.out.println(responseLine);
							break;
					}
				} catch(IOException e)
				{
					closed = true;
				} catch(Exception e)
				{
					e.printStackTrace();
				}

			}
		});
		t.start();

		updatePiDisplay();
	}

	public void updatePiDisplay()
	{
		JsonObject json = new JsonObject();
		json.addProperty("event", "set_grid");
		json.addProperty("columns", Core.displayColumns);
		json.addProperty("rows", Core.displayColumns);
		sendMessage(json);

		Profile profile = ProfileManager.getCurrentProfile();
		for(Button btn : profile.getVisibleButtons())
		{
			if(!btn.getBgColor().isEmpty())
			{
				json = new JsonObject();
				json.addProperty("event", "set_btn");
				json.addProperty("id", btn.getID());
				json.addProperty("color", btn.getBgColor());
				json.addProperty("x", btn.getX());
				json.addProperty("y", btn.getY());
				sendMessage(json);
			}
		}
	}

	public void close()
	{
		try
		{
			os.close();
			is.close();
			clientSocket.close();
		} catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void sendMessage(JsonObject json)
	{
		try
		{
			os.writeBytes(json.toString() + "\n");
		} catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
