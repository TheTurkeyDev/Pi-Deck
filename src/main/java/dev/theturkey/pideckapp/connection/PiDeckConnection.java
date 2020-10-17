package dev.theturkey.pideckapp.connection;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import dev.theturkey.pideckapp.Util;
import dev.theturkey.pideckapp.config.Config;
import dev.theturkey.pideckapp.profile.Button;
import dev.theturkey.pideckapp.profile.Profile;
import dev.theturkey.pideckapp.profile.ProfileManager;
import jssc.SerialPort;
import jssc.SerialPortException;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class PiDeckConnection
{
	private SerialPort port;

	private BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();

	public void connectTo(String portName)
	{
		port = new SerialPort(portName);
		Thread thread = new Thread(() ->
		{
			try
			{
				port.openPort();
				port.setParams(SerialPort.BAUDRATE_115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
				System.out.println("Connected!");
			} catch(SerialPortException e)
			{
				//TODO Don't throw, just mark as issue in UI
				throw new RuntimeException("Error opening serial port " + e.getPortName() + ": " + e.getExceptionType());
			}
			try
			{
				while(!Thread.interrupted())
				{
					String input = port.readString();
					if(input == null || input.trim().isEmpty())
						continue;
					for(String msg : input.split("\n"))
					{
						if(msg.isEmpty())
							continue;

						JsonObject json;
						try
						{
							json = JsonParser.parseString(msg).getAsJsonObject();
						} catch(JsonSyntaxException e)
						{
							System.out.println("Failed to parse '" + msg + "'");
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
							case "request_img":
								String imageId = json.get("id").getAsString();
								JsonObject response = new JsonObject();
								response.addProperty("event", "respond_img");
								response.addProperty("btn_id", json.get("btn_id").getAsString());
								response.addProperty("id", imageId);
								response.addProperty("image", Util.imageToBase64(Config.getSavedImageFile(imageId)));
								sendMessage(response);
								break;
							default:
								System.out.println("Unknown Message: " + json.toString());
								break;
						}
					}
					Thread.sleep(5);
				}
			} catch(Exception ex)
			{
				ex.printStackTrace();
			}
		});
		thread.start();

		Thread sendThread = new Thread(() ->
		{
			while(!Thread.interrupted())
			{
				try
				{
					if(port != null && port.isOpened())
					{
						if(!messageQueue.isEmpty())
						{
							String message = messageQueue.take();
							port.writeString(message + "\n");
						}
					}
				} catch(SerialPortException | InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		});
		sendThread.start();

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
		json.addProperty("image", btn.getImageSrc());
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
		messageQueue.add(json.toString());
	}
}
