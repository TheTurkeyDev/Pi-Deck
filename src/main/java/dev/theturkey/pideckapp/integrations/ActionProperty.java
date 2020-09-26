package dev.theturkey.pideckapp.integrations;

public class ActionProperty
{
	public String key;
	public PropType type;
	public String defaultVal;

	public ActionProperty(String key, PropType type, String defaultVal)
	{
		this.key = key;
		this.type = type;
		this.defaultVal = defaultVal;
	}

	public enum PropType
	{
		STRING,
		BOOLEAN,
		INTEGER,
		DOUBLE,
		FILE
	}
}
