package dev.theturkey.pideckapp.action.misc;

import dev.theturkey.pideckapp.Core;
import dev.theturkey.pideckapp.action.BaseAction;
import dev.theturkey.pideckapp.integrations.ActionProperty;
import dev.theturkey.pideckapp.profile.ActionInfo;
import dev.theturkey.pideckapp.profile.Profile;
import dev.theturkey.pideckapp.profile.ProfileManager;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SwitchProfileAction extends BaseAction
{
	@Override
	public void run(ActionInfo info)
	{
		super.run(info);

		Core.getUI().switchProfile(info.getProps().get("profile"));
	}

	@Override
	public List<ActionProperty> getPropDefs()
	{
		return Arrays.asList(
				new ActionProperty("profile", ActionProperty.PropType.DROP_DOWN, "", ProfileManager.getProfiles().stream().map(Profile::getName).collect(Collectors.toList()).toArray(new String[0]))
		);
	}
}
