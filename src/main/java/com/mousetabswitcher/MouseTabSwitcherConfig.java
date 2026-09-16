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

	enum Tab
	{
		COMBAT("Combat Options", InterfaceID.ToplevelOsrsStretch.STONE0),
		SKILLS("Skills", InterfaceID.ToplevelOsrsStretch.STONE1),
		QUESTS("Quest List", InterfaceID.ToplevelOsrsStretch.STONE2),
		INVENTORY("Inventory", InterfaceID.ToplevelOsrsStretch.STONE3),
		EQUIPMENT("Worn Equipment", InterfaceID.ToplevelOsrsStretch.STONE4),
		PRAYER("Prayer", InterfaceID.ToplevelOsrsStretch.STONE5),
		MAGIC("Magic", InterfaceID.ToplevelOsrsStretch.STONE6),
		FRIENDS("Friends List", InterfaceID.ToplevelOsrsStretch.STONE9),
		ACCOUNT("Account Management", InterfaceID.ToplevelOsrsStretch.STONE8),
		SETTINGS("Settings", InterfaceID.ToplevelOsrsStretch.STONE11),
		EMOTES("Emotes", InterfaceID.ToplevelOsrsStretch.STONE12),
		LOGOUT("Logout", InterfaceID.ToplevelOsrsStretch.STONE10);

		private final String name;
		private final int widgetId;

		Tab(String name, int widgetId)
		{
			this.name = name;
			this.widgetId = widgetId;
		}

		public int getWidgetId()
		{
			return widgetId;
		}

		@Override
		public String toString()
		{
			return name;
		}
	}
}