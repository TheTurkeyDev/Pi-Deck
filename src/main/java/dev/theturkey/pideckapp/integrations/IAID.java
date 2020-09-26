package dev.theturkey.pideckapp.integrations;

import java.util.Objects;

public class IAID
{
	public String integrationID;
	public String actionID;

	public IAID(String id)
	{
		String[] split = id.split("/");
		this.integrationID = split[0];
		this.actionID = split[1];
	}

	public IAID(String integrationID, String actionID)
	{
		this.integrationID = integrationID;
		this.actionID = actionID;
	}

	public String toString()
	{
		return integrationID + "/" + actionID;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o)
			return true;
		if(o == null || getClass() != o.getClass())
			return false;
		IAID iaid = (IAID) o;
		return integrationID.equals(iaid.integrationID) && actionID.equals(iaid.actionID);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(integrationID, actionID);
	}
}
