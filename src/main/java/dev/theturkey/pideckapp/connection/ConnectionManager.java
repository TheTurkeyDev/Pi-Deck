package dev.theturkey.pideckapp.connection;

import jssc.SerialPortList;

import java.util.HashMap;
import java.util.Map;

public class ConnectionManager
{
	private static String currentPiDeck = "";
	private static Map<String, PiDeckConnection> PIDECKS = new HashMap<>();


	public static String[] getPORTS()
	{
		return SerialPortList.getPortNames();
	}

	public static void connectToPort(String portName)
	{
		PiDeckConnection pideck = new PiDeckConnection();
		pideck.connectTo(portName);
		currentPiDeck = portName;
		PIDECKS.put(portName, pideck);
	}

	public static boolean isConnected()
	{
		return PIDECKS.size() > 0;
	}

	public static PiDeckConnection getCurrentConnection()
	{
		return PIDECKS.get(currentPiDeck);
	}
}
