package dev.theturkey.pideckapp;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.theturkey.pideckapp.profile.Button;
import dev.theturkey.pideckapp.profile.Profile;
import dev.theturkey.pideckapp.profile.ProfileManager;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

public class PiDeckConnection
{
	private DataOutputStream os = null;
	private SerialPort comPort = null;

	public void connect()
	{
		comPort = SerialPort.getCommPorts()[1];
		comPort.setBaudRate(115200);
		comPort.openPort();

		os = new DataOutputStream(comPort.getOutputStream());

		comPort.addDataListener(new SerialPortDataListener()
		{
			@Override
			public int getListeningEvents()
			{
				return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
			}

			@Override
			public void serialEvent(SerialPortEvent event)
			{
				if(event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
					return;

				byte[] newData = new byte[comPort.bytesAvailable()];
				comPort.readBytes(newData, newData.length);
				String responseLine = new String(newData);
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
			}
		});

		Thread t = new Thread(() ->
		{
			//This is done because apparently there's a delay when changing the baud rate
			try
			{
				Thread.sleep(3000);
			} catch(InterruptedException e)
			{
				e.printStackTrace();
			}

			updatePiDisplay();
			JsonObject synJson = new JsonObject();
			synJson.addProperty("event", "pi-deck-syn");
			sendMessage(synJson);
		});
		t.start();
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
		if(os == null)
			return;

		try
		{
			os.close();
			comPort.closePort();
		} catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void sendMessage(JsonObject json)
	{
		try
		{
			if(os != null)
				os.writeBytes(json.toString() + "\n");
		} catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
