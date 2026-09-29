package com.mousetabswitcher;

import net.runelite.api.gameval.InterfaceID;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("mousetabswitcher")
public interface MouseTabSwitcherConfig extends Config
{
	@ConfigItem(
			keyName = "mouse4Tab",
			name = "Button 4",
			description = "Select the tab that Button 4 will switch to. Make sure RuneLite's 'Block extra mouse buttons' option is disabled.",
			position = 1
	)
	default Tab mouse4Tab()
	{
		return Tab.INVENTORY;
	}

	@ConfigItem(
			keyName = "disableMouse4",
			name = "Disable Button 4",
			description = "Disables button 4 functionality.",
			position = 2
	)
	default boolean disableMouse4()
	{
		return false;
	}

	@ConfigItem(
			keyName = "mouse5Tab",
			name = "Button 5",
			description = "Select the tab that Button 5 will switch to. Make sure RuneLite's 'Block extra mouse buttons' option is disabled.",
			position = 3
	)
	default Tab mouse5Tab()
	{
		return Tab.EQUIPMENT;
	}

	@ConfigItem(
			keyName = "disableMouse5",
			name = "Disable Button 5",
			description = "Disables button 5 functionality.",
			position = 4
	)
	default boolean disableMouse5()
	{
		return false;
	}

	/**
	 * OSRS interface tabs supported by the plugin.
	 * Each tab has widget IDs for the supported client layouts.
	 */
	enum Tab
	{
		COMBAT(
				"Combat Options",
				InterfaceID.Toplevel.STONE0,
				InterfaceID.ToplevelOsrsStretch.STONE0,
				InterfaceID.ToplevelPreEoc.STONE0
		),

		SKILLS(
				"Skills",
				InterfaceID.Toplevel.STONE1,
				InterfaceID.ToplevelOsrsStretch.STONE1,
				InterfaceID.ToplevelPreEoc.STONE1
		),

		QUESTS(
				"Quest List",
				InterfaceID.Toplevel.STONE2,
				InterfaceID.ToplevelOsrsStretch.STONE2,
				InterfaceID.ToplevelPreEoc.STONE2
		),

		INVENTORY(
				"Inventory",
				InterfaceID.Toplevel.STONE3,
				InterfaceID.ToplevelOsrsStretch.STONE3,
				InterfaceID.ToplevelPreEoc.STONE3
		),

		EQUIPMENT(
				"Worn Equipment",
				InterfaceID.Toplevel.STONE4,
				InterfaceID.ToplevelOsrsStretch.STONE4,
				InterfaceID.ToplevelPreEoc.STONE4
		),

		PRAYER(
				"Prayer",
				InterfaceID.Toplevel.STONE5,
				InterfaceID.ToplevelOsrsStretch.STONE5,
				InterfaceID.ToplevelPreEoc.STONE5
		),

		MAGIC(
				"Magic",
				InterfaceID.Toplevel.STONE6,
				InterfaceID.ToplevelOsrsStretch.STONE6,
				InterfaceID.ToplevelPreEoc.STONE6
		),

		FRIENDS(
				"Friends List",
				InterfaceID.Toplevel.STONE9,
				InterfaceID.ToplevelOsrsStretch.STONE9,
				InterfaceID.ToplevelPreEoc.STONE9
		),

		ACCOUNT(
				"Account Management",
				InterfaceID.Toplevel.STONE8,
				InterfaceID.ToplevelOsrsStretch.STONE8,
				InterfaceID.ToplevelPreEoc.STONE8
		),

		SETTINGS(
				"Settings",
				InterfaceID.Toplevel.STONE11,
				InterfaceID.ToplevelOsrsStretch.STONE11,
				InterfaceID.ToplevelPreEoc.STONE11
		),

		EMOTES(
				"Emotes",
				InterfaceID.Toplevel.STONE12,
				InterfaceID.ToplevelOsrsStretch.STONE12,
				InterfaceID.ToplevelPreEoc.STONE12
		),

		LOGOUT(
				"Logout",
				InterfaceID.Toplevel.STONE10,
				InterfaceID.ToplevelOsrsStretch.STONE10,
				InterfaceID.ToplevelPreEoc.STONE10
		);

		private final String name;
		private final int fixedWidgetId;
		private final int classicResizableWidgetId;
		private final int modernResizableWidgetId;

		Tab(
				String name,
				int fixedWidgetId,
				int classicResizableWidgetId,
				int modernResizableWidgetId
		)
		{
			this.name = name;
			this.fixedWidgetId = fixedWidgetId;
			this.classicResizableWidgetId = classicResizableWidgetId;
			this.modernResizableWidgetId = modernResizableWidgetId;
		}

		public int getFixedWidgetId()
		{
			return fixedWidgetId;
		}

		public int getClassicResizableWidgetId()
		{
			return classicResizableWidgetId;
		}

		public int getModernResizableWidgetId()
		{
			return modernResizableWidgetId;
		}

		@Override
		public String toString()
		{
			return name;
		}
	}
}