package com.mousetabswitcher;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("mousetabswitcher")
public interface MouseTabSwitcherConfig extends Config
{
	@ConfigItem(
			keyName = "mouse4Tab",
			name = "Button 4",
			description = "Select the tab or action that Button 4 will execute.",
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
			description = "Select the tab or action that Button 5 will execute.",
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
		COMBAT("Combat Options", 10551355),
		SKILLS("Skills", 10551356),
		QUESTS("Quest List", 10551357),
		INVENTORY("Inventory", 10551358),
		EQUIPMENT("Worn Equipment", 10551359),
		PRAYER("Prayer", 10551360),
		MAGIC("Magic", 10551361),
		FRIENDS("Friends List", 10551341),
		ACCOUNT("Account Management", 10551340),
		SETTINGS("Settings", 10551343),
		EMOTES("Emotes", 10551344),
		LOGOUT("Logout", 10551342);

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