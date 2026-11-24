package com.mousetabswitcher;

import lombok.Getter;

/**
 * What a mouse button does: nothing, step to the next/previous tab, or open one specific tab.
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
	MUSIC(Tab.MUSIC);

	private final String label;
	private final Tab tab;

	TabAction(Tab tab)
	{
		this(tab.getLabel(), tab);
	}

	TabAction(String label, Tab tab)
	{
		this.label = label;
		this.tab = tab;
	}

	@Override
	public String toString()
	{
		return label;
	}
}
