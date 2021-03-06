package dev.theturkey.pideckapp.action.io;

import dev.theturkey.pideckapp.action.BaseAction;
import dev.theturkey.pideckapp.integrations.ActionProperty;
import dev.theturkey.pideckapp.profile.ActionInfo;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class SoundAction extends BaseAction
{
	@Override
	public void run(ActionInfo info)
	{
		super.run(info);
		try
		{
			File f = new File(info.getProps().get("file"));
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
			Clip clip = AudioSystem.getClip();
			clip.open(audioIn);
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(Float.parseFloat(info.getProps().get("gain")));
			clip.start();
		} catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public List<ActionProperty> getPropDefs()
	{
		return Arrays.asList(
				new ActionProperty("file", ActionProperty.PropType.FILE, ""),
				new ActionProperty("gain", ActionProperty.PropType.DOUBLE, "0")
		);
	}
}
