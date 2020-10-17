package dev.theturkey.pideckapp.integrations;

public class ActionProperty
{
	public String key;
	public PropType type;
	public String defaultVal;

	public String[] values = new String[0];

	public ActionProperty(String key, PropType type, String defaultVal)
	{
		this.key = key;
		this.type = type;
		this.defaultVal = defaultVal;
	}

	public ActionProperty(String key, PropType type, String defaultVal, String[] values)
	{
		this.key = key;
		this.type = type;
		this.defaultVal = defaultVal;
		this.values = values;
	}

	public enum PropType
	{
		STRING,
		BOOLEAN,
		INTEGER,
		DOUBLE,
		FILE,
		DROP_DOWN
	}
}
