package com.mousetabswitcher;

import lombok.Getter;
import net.runelite.api.gameval.InterfaceID;

/**
 * The side-panel tabs. Each tab has a "stone" (the clickable tab button) whose
 * widget id differs per client layout. The stone index matches the value of
 * {@code VarClientID.TOPLEVEL_PANEL} while that tab is open.
 */
@Getter
public enum Tab
{
	COMBAT("Combat", 0,
		InterfaceID.Toplevel.STONE0, InterfaceID.ToplevelOsrsStretch.STONE0, InterfaceID.ToplevelPreEoc.STONE0),
	SKILLS("Skills", 1,
		InterfaceID.Toplevel.STONE1, InterfaceID.ToplevelOsrsStretch.STONE1, InterfaceID.ToplevelPreEoc.STONE1),
	QUESTS("Quests", 2,
		InterfaceID.Toplevel.STONE2, InterfaceID.ToplevelOsrsStretch.STONE2, InterfaceID.ToplevelPreEoc.STONE2),
	INVENTORY("Inventory", 3,
		InterfaceID.Toplevel.STONE3, InterfaceID.ToplevelOsrsStretch.STONE3, InterfaceID.ToplevelPreEoc.STONE3),
	EQUIPMENT("Equipment", 4,
		InterfaceID.Toplevel.STONE4, InterfaceID.ToplevelOsrsStretch.STONE4, InterfaceID.ToplevelPreEoc.STONE4),
	PRAYER("Prayer", 5,
		InterfaceID.Toplevel.STONE5, InterfaceID.ToplevelOsrsStretch.STONE5, InterfaceID.ToplevelPreEoc.STONE5),
	MAGIC("Magic", 6,
		InterfaceID.Toplevel.STONE6, InterfaceID.ToplevelOsrsStretch.STONE6, InterfaceID.ToplevelPreEoc.STONE6),
	CLAN("Clan", 7,
		InterfaceID.Toplevel.STONE7, InterfaceID.ToplevelOsrsStretch.STONE7, InterfaceID.ToplevelPreEoc.STONE7),
	ACCOUNT("Account", 8,
		InterfaceID.Toplevel.STONE8, InterfaceID.ToplevelOsrsStretch.STONE8, InterfaceID.ToplevelPreEoc.STONE8),
	FRIENDS("Friends", 9,
		InterfaceID.Toplevel.STONE9, InterfaceID.ToplevelOsrsStretch.STONE9, InterfaceID.ToplevelPreEoc.STONE9),
	LOGOUT("Logout", 10,
		InterfaceID.Toplevel.STONE10, InterfaceID.ToplevelOsrsStretch.STONE10, InterfaceID.ToplevelPreEoc.STONE10),
	SETTINGS("Settings", 11,
		InterfaceID.Toplevel.STONE11, InterfaceID.ToplevelOsrsStretch.STONE11, InterfaceID.ToplevelPreEoc.STONE11),
	EMOTES("Emotes", 12,
		InterfaceID.Toplevel.STONE12, InterfaceID.ToplevelOsrsStretch.STONE12, InterfaceID.ToplevelPreEoc.STONE12),
	MUSIC("Music", 13,
		InterfaceID.Toplevel.STONE13, InterfaceID.ToplevelOsrsStretch.STONE13, InterfaceID.ToplevelPreEoc.STONE13);

	private final String label;
	private final int stoneIndex;
	private final int fixedWidgetId;
	private final int classicResizableWidgetId;
	private final int modernResizableWidgetId;

	Tab(String label, int stoneIndex, int fixedWidgetId, int classicResizableWidgetId, int modernResizableWidgetId)
	{
		this.label = label;
		this.stoneIndex = stoneIndex;
		this.fixedWidgetId = fixedWidgetId;
		this.classicResizableWidgetId = classicResizableWidgetId;
		this.modernResizableWidgetId = modernResizableWidgetId;
	}

	/**
	 * @return the stone widget id for the given top-level interface, or -1 for an unsupported layout
	 */
	public int widgetIdFor(int topLevelInterfaceId)
	{
		switch (topLevelInterfaceId)
		{
			case InterfaceID.TOPLEVEL:
				return fixedWidgetId;
			case InterfaceID.TOPLEVEL_OSRS_STRETCH:
				return classicResizableWidgetId;
			case InterfaceID.TOPLEVEL_PRE_EOC:
				return modernResizableWidgetId;
			default:
				return -1;
		}
	}

	public static Tab byStoneIndex(int stoneIndex)
	{
		for (Tab tab : values())
		{
			if (tab.stoneIndex == stoneIndex)
			{
				return tab;
			}
		}
		return null;
	}

	@Override
	public String toString()
	{
		return label;
	}
}
