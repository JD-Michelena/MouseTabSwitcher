package com.mousetabswitcher;

import java.util.EnumSet;
import java.util.Set;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Keybind;
import net.runelite.client.config.Range;
import net.runelite.client.config.Units;

@ConfigGroup(MouseTabSwitcherConfig.GROUP)
public interface MouseTabSwitcherConfig extends Config
{
	String GROUP = "mousetabswitcher";

	// Settings from version 1.0.0. SettingsMigration moves them to the new keys on startup.
	String OLD_BUTTON4_TAB = "mouse4Tab";
	String OLD_BUTTON4_DISABLED = "disableMouse4";
	String OLD_BUTTON5_TAB = "mouse5Tab";
	String OLD_BUTTON5_DISABLED = "disableMouse5";
	// Set to true in each profile after the 1.0.0 settings were moved. Not shown in the panel.
	String MIGRATED = "migratedFrom1";

	@ConfigSection(
		name = "Mouse buttons",
		description = "What each mouse button does on a single press: open a tab, go to the next or previous tab, or work like Space or a number key (1-9). Buttons 6-8 only work if your OS and mouse driver report them as mouse buttons.",
		position = 0
	)
	String buttonsSection = "buttons";

	@ConfigSection(
		name = "Double-click",
		description = "What each mouse button does when pressed twice quickly. A button with a double-click action waits briefly before doing its single action.",
		position = 1,
		closedByDefault = true
	)
	String doubleSection = "double";

	@ConfigSection(
		name = "Next / previous tab",
		description = "How the Next and Previous actions step through the tabs",
		position = 2
	)
	String cycleSection = "cycle";

	@ConfigSection(
		name = "Behaviour",
		description = "General behaviour",
		position = 3
	)
	String behaviourSection = "behaviour";

	// ---- Mouse buttons ----

	@ConfigItem(
		keyName = "button4",
		name = "Button 4",
		description = "Mouse button 4, usually the rear side button (browser Back)",
		section = buttonsSection,
		position = 0
	)
	default TabAction button4()
	{
		return TabAction.PREVIOUS;
	}

	@ConfigItem(
		keyName = "button5",
		name = "Button 5",
		description = "Mouse button 5, usually the front side button (browser Forward)",
		section = buttonsSection,
		position = 1
	)
	default TabAction button5()
	{
		return TabAction.NEXT;
	}

	@ConfigItem(
		keyName = "button3",
		name = "Middle",
		description = "Middle mouse button (wheel click). Binding it stops middle-click camera dragging.",
		section = buttonsSection,
		position = 2
	)
	default TabAction middleButton()
	{
		return TabAction.NONE;
	}

	@ConfigItem(
		keyName = "button6",
		name = "Button 6",
		description = "Mouse button 6, if your mouse and OS report it",
		section = buttonsSection,
		position = 3
	)
	default TabAction button6()
	{
		return TabAction.NONE;
	}

	@ConfigItem(
		keyName = "button7",
		name = "Button 7",
		description = "Mouse button 7, if your mouse and OS report it",
		section = buttonsSection,
		position = 4
	)
	default TabAction button7()
	{
		return TabAction.NONE;
	}

	@ConfigItem(
		keyName = "button8",
		name = "Button 8",
		description = "Mouse button 8, if your mouse and OS report it",
		section = buttonsSection,
		position = 5
	)
	default TabAction button8()
	{
		return TabAction.NONE;
	}

	@ConfigItem(
		keyName = "shiftLayer",
		name = "Shift reverses",
		description = "Holding Shift while pressing a Next/Previous button steps the other way",
		section = buttonsSection,
		position = 6
	)
	default boolean shiftReverses()
	{
		return false;
	}

	// ---- Double-click ----

	@ConfigItem(
		keyName = "button4Double",
		name = "Button 4",
		description = "Double-click action for mouse button 4",
		section = doubleSection,
		position = 0
	)
	default TabAction button4Double()
	{
		return TabAction.NONE;
	}

	@ConfigItem(
		keyName = "button5Double",
		name = "Button 5",
		description = "Double-click action for mouse button 5",
		section = doubleSection,
		position = 1
	)
	default TabAction button5Double()
	{
		return TabAction.NONE;
	}

	@ConfigItem(
		keyName = "button3Double",
		name = "Middle",
		description = "Double-click action for the middle mouse button",
		section = doubleSection,
		position = 2
	)
	default TabAction middleButtonDouble()
	{
		return TabAction.NONE;
	}

	@ConfigItem(
		keyName = "button6Double",
		name = "Button 6",
		description = "Double-click action for mouse button 6",
		section = doubleSection,
		position = 3
	)
	default TabAction button6Double()
	{
		return TabAction.NONE;
	}

	@ConfigItem(
		keyName = "button7Double",
		name = "Button 7",
		description = "Double-click action for mouse button 7",
		section = doubleSection,
		position = 4
	)
	default TabAction button7Double()
	{
		return TabAction.NONE;
	}

	@ConfigItem(
		keyName = "button8Double",
		name = "Button 8",
		description = "Double-click action for mouse button 8",
		section = doubleSection,
		position = 5
	)
	default TabAction button8Double()
	{
		return TabAction.NONE;
	}

	@Range(min = 100, max = 1000)
	@Units(Units.MILLISECONDS)
	@ConfigItem(
		keyName = "doubleClickMs",
		name = "Double-click speed",
		description = "How fast the second press must follow the first. Buttons with a double-click action wait this long before doing their single action.",
		section = doubleSection,
		position = 6
	)
	default int doubleClickMs()
	{
		return 250;
	}

	// ---- Next / previous ----

	@ConfigItem(
		keyName = "wrapAround",
		name = "Wrap around",
		description = "Going past the last tab continues from the first one, and the other way round",
		section = cycleSection,
		position = 0
	)
	default boolean wrapAround()
	{
		return true;
	}

	@ConfigItem(
		keyName = "skippedTabs",
		name = "Skip tabs",
		description = "Tabs that Next/Previous step over. Ctrl-click to select several. Direct bindings still open them.",
		section = cycleSection,
		position = 1
	)
	default Set<Tab> skippedTabs()
	{
		return EnumSet.of(Tab.LOGOUT);
	}

	@ConfigItem(
		keyName = "nextTabKey",
		name = "Next hotkey",
		description = "Optional keyboard key for Next, e.g. a mouse side button your mouse software maps to a key. Do not use a key that you type in chat.",
		section = cycleSection,
		position = 2
	)
	default Keybind nextTabKey()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "previousTabKey",
		name = "Previous hotkey",
		description = "Optional keyboard key for Previous, e.g. a mouse side button your mouse software maps to a key. Do not use a key that you type in chat.",
		section = cycleSection,
		position = 3
	)
	default Keybind previousTabKey()
	{
		return Keybind.NOT_SET;
	}

	// ---- Behaviour ----

	@ConfigItem(
		keyName = "closeIfOpen",
		name = "Close tab if already open",
		description = "In resizable layouts, a direct binding for the tab that is already open closes the side panel (like clicking it). Off keeps it open.",
		section = behaviourSection,
		position = 0
	)
	default boolean closeIfOpen()
	{
		return false;
	}
}
