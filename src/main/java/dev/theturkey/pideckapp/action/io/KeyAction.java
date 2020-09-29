package dev.theturkey.pideckapp.action.io;

import dev.theturkey.pideckapp.action.BaseAction;
import dev.theturkey.pideckapp.integrations.ActionProperty;
import dev.theturkey.pideckapp.profile.ActionInfo;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

import static java.awt.event.KeyEvent.*;

public class KeyAction extends BaseAction
{
	@Override
	public void run(ActionInfo info)
	{
		super.run(info);
		try
		{
			Robot robot = new Robot();
			int delay = Integer.parseInt(info.getProps().getOrDefault("step_delay", "50"));
			robot.setAutoDelay(delay);
			int key = getKey(info.getProps().getOrDefault("key", ""));
			boolean press = Boolean.parseBoolean(info.getProps().getOrDefault("press", "true"));
			boolean release = Boolean.parseBoolean(info.getProps().getOrDefault("release", "false"));
			if(key != -1)
			{
				if(press)
					robot.keyPress(key);
				if(release)
					robot.keyRelease(key);
			}
		} catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public int getKey(String character)
	{
		switch(character.toLowerCase())
		{
			case "a":
				return VK_A;
			case "b":
				return VK_B;
			case "c":
				return VK_C;
			case "d":
				return VK_D;
			case "e":
				return VK_E;
			case "f":
				return VK_F;
			case "g":
				return VK_G;
			case "h":
				return VK_H;
			case "i":
				return VK_I;
			case "j":
				return VK_J;
			case "k":
				return VK_K;
			case "l":
				return VK_L;
			case "m":
				return VK_M;
			case "n":
				return VK_N;
			case "o":
				return VK_O;
			case "p":
				return VK_P;
			case "q":
				return VK_Q;
			case "r":
				return VK_R;
			case "s":
				return VK_S;
			case "t":
				return VK_T;
			case "u":
				return VK_U;
			case "v":
				return VK_V;
			case "w":
				return VK_W;
			case "x":
				return VK_X;
			case "y":
				return VK_Y;
			case "z":
				return VK_Z;
			case "`":
			case "~":
				return VK_BACK_QUOTE;
			case "0":
				return VK_0;
			case "1":
				return VK_1;
			case "2":
				return VK_2;
			case "3":
				return VK_3;
			case "4":
				return VK_4;
			case "5":
			case "%":
				return VK_5;
			case "6":
				return VK_6;
			case "7":
				return VK_7;
			case "8":
				return VK_8;
			case "9":
				return VK_9;
			case "-":
				return VK_MINUS;
			case "=":
				return VK_EQUALS;
			case "!":
				return VK_EXCLAMATION_MARK;
			case "@":
				return VK_AT;
			case "#":
				return VK_NUMBER_SIGN;
			case "$":
				return VK_DOLLAR;
			case "^":
				return VK_CIRCUMFLEX;
			case "&":
				return VK_AMPERSAND;
			case "*":
				return VK_ASTERISK;
			case "(":
				return VK_LEFT_PARENTHESIS;
			case ")":
				return VK_RIGHT_PARENTHESIS;
			case "_":
				return VK_UNDERSCORE;
			case "+":
				return VK_PLUS;
			case "\t":
				return VK_TAB;
			case "\n":
				return VK_ENTER;
			case "[":
			case "{":
				return VK_OPEN_BRACKET;
			case "]":
			case "}":
				return VK_CLOSE_BRACKET;
			case "\\":
			case "|":
				return VK_BACK_SLASH;
			case ";":
				return VK_SEMICOLON;
			case ":":
				return VK_COLON;
			case "'":
				return VK_QUOTE;
			case "\"":
				return VK_QUOTEDBL;
			case ",":
			case "<":
				return VK_COMMA;
			case ".":
			case ">":
				return VK_PERIOD;
			case "/":
			case "?":
				return VK_SLASH;
			case " ":
				return VK_SPACE;
			case "f1":
				return VK_F1;
			case "f2":
				return VK_F2;
			case "f3":
				return VK_F3;
			case "f4":
				return VK_F4;
			case "f5":
				return VK_F5;
			case "f6":
				return VK_F6;
			case "f7":
				return VK_F7;
			case "f8":
				return VK_F8;
			case "f9":
				return VK_F9;
			case "f10":
				return VK_F10;
			case "f11":
				return VK_F11;
			case "f12":
				return VK_F12;
			case "f13":
				return VK_F13;
			case "f14":
				return VK_F14;
			case "f15":
				return VK_F15;
			case "f16":
				return VK_F16;
			case "f17":
				return VK_F17;
			case "f18":
				return VK_F18;
			case "f19":
				return VK_F19;
			case "f20":
				return VK_F20;
			case "f21":
				return VK_F21;
			case "f22":
				return VK_F22;
			case "f23":
				return VK_F23;
			case "f24":
				return VK_F24;
			default:
				return -1;
		}
	}

	@Override
	public List<ActionProperty> getPropDefs()
	{
		return Arrays.asList(
				new ActionProperty("key", ActionProperty.PropType.STRING, "F13"),
				new ActionProperty("press", ActionProperty.PropType.BOOLEAN, "true"),
				new ActionProperty("release", ActionProperty.PropType.BOOLEAN, "false"),
				new ActionProperty("step_delay", ActionProperty.PropType.INTEGER, "50")
		);
	}
}
