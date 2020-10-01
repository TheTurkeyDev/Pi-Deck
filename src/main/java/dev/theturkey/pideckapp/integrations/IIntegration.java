package dev.theturkey.pideckapp.integrations;

public interface IIntegration
{
	void load();
	String getType();
	String getDisplay();
}
