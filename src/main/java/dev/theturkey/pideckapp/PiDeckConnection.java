package dev.theturkey.pideckapp;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import dev.theturkey.pideckapp.profile.Button;
import dev.theturkey.pideckapp.profile.Profile;
import dev.theturkey.pideckapp.profile.ProfileManager;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

import java.io.File;

public class PiDeckConnection
{
	private SerialPort port;

	public void connect()
	{
		String[] ports = SerialPortList.getPortNames();
		port = new SerialPort(ports[1]);
		try
		{
			port.openPort();
			port.setParams(SerialPort.BAUDRATE_115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
		} catch(SerialPortException e)
		{
			//TODO Don't throw, just mark as issue in UI
			throw new RuntimeException("Error opening serial port " + e.getPortName() + ": " + e.getExceptionType());
		}

		Thread thread = new Thread(() ->
		{
			try
			{
				while(!Thread.interrupted())
				{
					String input = port.readString();
					if(input == null || input.trim().isEmpty())
						continue;
					JsonObject json;
					try
					{
						json = JsonParser.parseString(input).getAsJsonObject();
					} catch(JsonSyntaxException e)
					{
						System.out.println("Failed to parse '" + input + "'");
						continue;
					}

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
							System.out.println(input);
							break;
					}
					Thread.sleep(5);
				}
			} catch(Exception ex)
			{
				ex.printStackTrace();
			}
		});
		thread.start();


		updatePiDisplay();
		JsonObject synJson = new JsonObject();
		synJson.addProperty("event", "pi-deck-syn");
		sendMessage(synJson);
	}

	public void updatePiDisplay()
	{
		JsonObject json = new JsonObject();
		json.addProperty("event", "set_grid");

		Profile profile = ProfileManager.getCurrentProfile();
		json.addProperty("columns", profile.getColumns());
		json.addProperty("rows", profile.getRows());
		sendMessage(json);

		for(Button btn : profile.getVisibleButtons())
			if(!btn.getBgColor().isEmpty())
				updateButton(btn);
	}

	public void updateButton(Button btn)
	{
		JsonObject json = new JsonObject();
		json.addProperty("event", "set_btn");
		json.addProperty("id", btn.getID());
		json.addProperty("color", btn.getBgColor());
		json.addProperty("text", btn.getText());
		File file = new File(btn.getImageSrc());
		if(file.exists())
			json.addProperty("image", Util.imageToBase64(file));
		else
			json.addProperty("image", "");
		json.addProperty("x", btn.getX());
		json.addProperty("y", btn.getY());
		sendMessage(json);
	}

	public void close()
	{
		if(port == null)
			return;

		try
		{
			port.closePort();
		} catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void sendMessage(JsonObject json)
	{
		try
		{
			if(port != null)
				port.writeString(json.toString() + "\n");
		} catch(SerialPortException e)
		{
			e.printStackTrace();
		}
	}
}
