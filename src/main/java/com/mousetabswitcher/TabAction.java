package com.mousetabswitcher;

import java.awt.event.KeyEvent;
import lombok.Getter;

/**
 * What a mouse button does: nothing, step to the next/previous tab, open one specific tab,
 * or work like one key (Space or 1-9).
 */
@Getter
public enum TabAction
{
	NONE("None", null),
	PREVIOUS("Previous", null),
	NEXT("Next", null),
	COMBAT(Tab.COMBAT),
	SKILLS(Tab.SKILLS),
	QUESTS(Tab.QUESTS),
	INVENTORY(Tab.INVENTORY),
	EQUIPMENT(Tab.EQUIPMENT),
	PRAYER(Tab.PRAYER),
	MAGIC(Tab.MAGIC),
	CLAN(Tab.CLAN),
	ACCOUNT(Tab.ACCOUNT),
	FRIENDS(Tab.FRIENDS),
	LOGOUT(Tab.LOGOUT),
	SETTINGS(Tab.SETTINGS),
	EMOTES(Tab.EMOTES),
	MUSIC(Tab.MUSIC),
	SPACE("Space", KeyEvent.VK_SPACE, ' '),
	KEY_1("1", KeyEvent.VK_1, '1'),
	KEY_2("2", KeyEvent.VK_2, '2'),
	KEY_3("3", KeyEvent.VK_3, '3'),
	KEY_4("4", KeyEvent.VK_4, '4'),
	KEY_5("5", KeyEvent.VK_5, '5'),
	KEY_6("6", KeyEvent.VK_6, '6'),
	KEY_7("7", KeyEvent.VK_7, '7'),
	KEY_8("8", KeyEvent.VK_8, '8'),
	KEY_9("9", KeyEvent.VK_9, '9');

	private final String label;
	private final Tab tab;
	/** The key this action works like, or {@link KeyEvent#VK_UNDEFINED} for tab actions. */
	private final int keyCode;
	private final char keyChar;

	TabAction(Tab tab)
	{
		this(tab.getLabel(), tab);
	}

	TabAction(String label, Tab tab)
	{
		this.label = label;
		this.tab = tab;
		this.keyCode = KeyEvent.VK_UNDEFINED;
		this.keyChar = KeyEvent.CHAR_UNDEFINED;
	}

	TabAction(String label, int keyCode, char keyChar)
	{
		this.label = label;
		this.tab = null;
		this.keyCode = keyCode;
		this.keyChar = keyChar;
	}

	/** @return true if this action works like a key press instead of a tab action */
	public boolean isKey()
	{
		return keyCode != KeyEvent.VK_UNDEFINED;
	}

	@Override
	public String toString()
	{
		return label;
	}
}
